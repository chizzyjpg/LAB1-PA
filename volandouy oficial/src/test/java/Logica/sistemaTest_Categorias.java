package Logica;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;

public class sistemaTest_Categorias {

  private CategoriaServiceFake categorias;
  private Sistema sistema;

  @BeforeEach
  void setup() {
    categorias = new CategoriaServiceFake();
    

    // 👉 clave: hacemos que el manejador use nuestro fake
    ManejadorCategoria.setServiceForTests(categorias);

    sistema = new Sistema(
        new UsuarioServiceFake(),
        categorias,
        new CiudadServiceFake(),
        new PaqueteServiceFake(),
        new ClienteServiceFake(),
        new ReservaServiceFake()
    );
  }

  @AfterEach
  void tearDown() {
    // opcional: volver al real si lo necesitás en otros tests
    ManejadorCategoria.setServiceForTests(null);
  }

  @Test
  void registrarCategoria_ok_siNoExiste() {
    sistema.registrarCategoria(new DataCategoria("Promo"));
    assertTrue(sistema.existeCategoria("Promo"));

    var nombres = sistema.listarCategorias().stream()
        .map(DataCategoria::getNombre).collect(Collectors.toSet());
    assertTrue(nombres.contains("Promo"));
  }

  @Test
  void registrarCategoria_lanza_siDuplicada() {
    sistema.registrarCategoria(new DataCategoria("Premium"));
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarCategoria(new DataCategoria("Premium")));
  }

  @Test
  void registrarCategoria_lanza_siNombreNuloOVacio() {
    assertThrows(IllegalArgumentException.class, () -> sistema.registrarCategoria(new DataCategoria(null)));
    assertThrows(IllegalArgumentException.class, () -> sistema.registrarCategoria(new DataCategoria("   ")));
  }
  
  @Test
  void listarCategorias_conNullYNoNull_ejecutaAmbasRamasDelTernario() {
    // Preparo el fake ya inyectado en setup()
    categorias.seedNombreNull();   // fuerza c.getNombre() == null
    categorias.seedNombre("beta"); // fuerza c.getNombre() != null

    var lista = sistema.listarCategorias();

    assertEquals(2, lista.size());
    // Como el Comparator usa "" cuando el nombre es null, el null queda primero
    assertNull(lista.get(0).getNombre());
    assertEquals("beta", lista.get(1).getNombre());
  }

  @Test
  void listarCategorias_ordenadasPorNombre_caseInsensitive() {
    sistema.registrarCategoria(new DataCategoria("beta"));
    sistema.registrarCategoria(new DataCategoria("Alfa"));

    var lista = sistema.listarCategorias();
    assertEquals(List.of("Alfa", "beta"),
        lista.stream().map(DataCategoria::getNombre).toList());
  }

  // ===== FAKES =====

  static class CategoriaServiceFake extends BD.CategoriaService {
	 private final java.util.List<Categoria> nombres = new java.util.ArrayList<>();
	 
	 void seedNombreNull() { nombres.add(new Categoria()); }
	 void seedNombre(String n) { var c = new Categoria(); c.setNombre(n); nombres.add(c); }

   /* private static String canon(String s) {
      return s == null ? null : s.trim().toLowerCase(Locale.ROOT);
    }*/

    @Override public java.util.List<Categoria> listarCategorias() { return new java.util.ArrayList<>(nombres); }
    @Override public boolean existeCategoria(String nombre) { return nombres.stream().anyMatch(c -> java.util.Objects.equals(c.getNombre(), nombre)); }
    @Override public void crearCategoria(Categoria c) { nombres.add(c); }
  }
 

  // Los demás servicios no se usan acá; fakes vacíos están bien
  static class UsuarioServiceFake extends BD.UsuarioService {}
  static class CiudadServiceFake   extends BD.CiudadService   {}
  static class PaqueteServiceFake  extends BD.PaqueteService  {}
  static class ClienteServiceFake  extends BD.ClienteService  {}
  static class ReservaServiceFake  extends BD.ReservaService  {}
}
