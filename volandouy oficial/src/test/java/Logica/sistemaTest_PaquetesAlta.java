package Logica;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.*;

public class sistemaTest_PaquetesAlta {

  private PaqueteServiceFake paquetes;
  private Sistema sistema;

  @BeforeEach
  void setup() {
    paquetes = new PaqueteServiceFake();
    // para no tocar BD real
    ManejadorPaquete.setServiceForTests(paquetes);

    sistema = new Sistema(
        new UsuarioServiceFake(),
        new CategoriaServiceFake(),
        new CiudadServiceFake(),
        paquetes,
        new ClienteServiceFake(),
        new ReservaServiceFake(),
        new RutaVueloServiceFake()
    );
  }

  @AfterEach
  void tearDown() {
    ManejadorPaquete.setServiceForTests(null);
  }

  /* ====== registrarPaquete: validaciones ====== */

  @Test
  void registrarPaquete_lanza_siDataNull() {
    assertThrows(IllegalArgumentException.class, () -> sistema.registrarPaquete(null));
  }

  @Test
  void registrarPaquete_lanza_siNombreVacioONull() {
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarPaquete(nuevoDto(null, "Desc", 30, 10, new Date())));
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarPaquete(nuevoDto("   ", "Desc", 30, 10, new Date())));
  }

  @Test
  void registrarPaquete_lanza_siDescripcionVaciaONull() {
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarPaquete(nuevoDto("Pack", null, 30, 10, new Date())));
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarPaquete(nuevoDto("Pack", "   ", 30, 10, new Date())));
  }

  @Test
  void registrarPaquete_lanza_siValidezNoPositiva() {
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarPaquete(nuevoDto("Pack", "Desc", 0, 10, new Date())));
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarPaquete(nuevoDto("Pack", "Desc", -5, 10, new Date())));
  }

  @Test
  void registrarPaquete_lanza_siDescuentoFueraDeRango() {
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarPaquete(nuevoDto("Pack", "Desc", 30, -1, new Date())));
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarPaquete(nuevoDto("Pack", "Desc", 30, 101, new Date())));
  }

  @Test
  void registrarPaquete_lanza_siFechaAltaNull() {
    assertThrows(IllegalArgumentException.class,
        () -> sistema.registrarPaquete(nuevoDto("Pack", "Desc", 30, 10, null)));
  }

  /* ====== registrarPaquete: OK (camino feliz) ====== */

  @Test
  void registrarPaquete_ok_invocaAltaToEntity_enElFake() {
    paquetes.ultimaAlta = null;

    var dto = nuevoDto("EscapadaPlus", "Descripción", 30, 10, new Date());
    sistema.registrarPaquete(dto);

    assertNotNull(paquetes.ultimaAlta);
    assertEquals("EscapadaPlus", paquetes.ultimaAlta.getNombre());
    assertTrue(sistema.existePaquete("EscapadaPlus"));
  }

  /* ====== existePaquete / listarPaquetes / verPaquete ====== */

  @Test
  void existePaquete_false_siNombreNull() {
    assertFalse(sistema.existePaquete(null));
  }

  @Test
  void listarPaquetes_mapeaADataPaquete() {
    paquetes.agregar(new Paquete("A", "d", 1, TipoAsiento.TURISTA, 10, 30, new BigDecimal("1000")));
    paquetes.agregar(new Paquete("B", "d", 2, TipoAsiento.EJECUTIVO, 0, 15, new BigDecimal("2000")));

    var lista = sistema.listarPaquetes();
    assertEquals(2, lista.size());
    var nombres = lista.stream().map(DataPaquete::getNombre).toList();
    assertTrue(nombres.containsAll(List.of("A", "B")));
  }

  @Test
  void verPaquete_null_siNombreNull_oNoExiste() {
    assertNull(sistema.verPaquete(null));
    assertNull(sistema.verPaquete("no-existe"));
  }

  @Test
  void verPaquete_ok_devuelveDTO() {
    paquetes.agregar(new Paquete("PackX","d",1,TipoAsiento.TURISTA,5,10,new BigDecimal("999")));
    var dp = sistema.verPaquete("PackX");
    assertNotNull(dp);
    assertEquals("PackX", dp.getNombre());
  }
  
  @Test
  void existePaquete_false_siNoExisteNombre() {
    assertFalse(sistema.existePaquete("PackZ")); // ← evalúa (… != null) como FALSE
  }

  /* ====== helper ====== */

  private static DataPaqueteAlta nuevoDto(String nombre, String desc, int validez, int descuento, Date fechaAlta) {
    return new DataPaqueteAlta(nombre, desc, validez, descuento, fechaAlta);
  }

  /* ====== fakes ====== */

  static class PaqueteServiceFake extends BD.PaqueteService {
    private final Map<String, Paquete> porNombre = new HashMap<>();
    Paquete ultimaAlta;

    void agregar(Paquete p) { porNombre.put(p.getNombre(), p); }
    @Override public Paquete existePaquete(String nombre) { return porNombre.get(nombre); }
    @Override public List<Paquete> listarPaquetes() { return new ArrayList<>(porNombre.values()); }
    @Override public void actualizarPaquete(Paquete p) { porNombre.put(p.getNombre(), p); }
    @Override public void crearPaquete(Paquete p) { ultimaAlta = p; porNombre.put(p.getNombre(), p); }
  }

  static class UsuarioServiceFake extends BD.UsuarioService {}
  static class CategoriaServiceFake extends BD.CategoriaService {}
  static class CiudadServiceFake extends BD.CiudadService {}
  static class ClienteServiceFake extends BD.ClienteService {}
  static class ReservaServiceFake extends BD.ReservaService {}
  static class RutaVueloServiceFake extends BD.RutaVueloService {}
}
