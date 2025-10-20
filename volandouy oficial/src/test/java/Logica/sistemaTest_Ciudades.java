package Logica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class sistemaTest_Ciudades {

  private Sistema sistema;
  private CiudadServiceFake ciudadServiceFake;

  @BeforeEach
  void setup() {
    ciudadServiceFake = new CiudadServiceFake();

    // Inyección en el manejador para NO tocar la BD real
    ManejadorCiudad.setCiudadServiceForTests(ciudadServiceFake);

    sistema = new Sistema(
		new UsuarioServiceFake(),
		new CategoriaServiceFake(),
		ciudadServiceFake,
		new PaqueteServiceFake(),
		new ClienteServiceFake(),
		new ReservaServiceFake()
        );
  }

  // =============== registrarCiudad ===============

  @Test
  void registrarCiudad_dataNull_lanzaIAE() {
    assertThrows(IllegalArgumentException.class, () -> sistema.registrarCiudad(null));
  }

  @Test
  void registrarCiudad_duplicada_lanzaIAE() {
    ciudadServiceFake.agregarCiudad(new Ciudad("Montevideo", "Uruguay", "Carrasco", "Desc Aeropuerto", new Date(), "http://montevideo.com"));
    DataCiudad data = new DataCiudad("Montevideo", "Uruguay", "Carrasco", "Desc Aeropuerto", new Date(), "http://montevideo.com");

    var ex = assertThrows(IllegalArgumentException.class, () -> sistema.registrarCiudad(data));
    assertTrue(ex.getMessage().toLowerCase().contains("registrada")); // "La ciudad ya está registrada"
  }

  @Test
  void registrarCiudad_caminoOk_noLanza_yPersisteEnFake() {
    DataCiudad data = new DataCiudad("Salto", "Uruguay", "Salto Airport", "Desc Aeropuerto", new Date(), "http://salto.com");;
    assertDoesNotThrow(() -> sistema.registrarCiudad(data));

    // Verificamos que el fake la tenga
    Optional<Ciudad> encontrada = ciudadServiceFake
        .listarCiudades().stream()
        .filter(c -> "Salto".equals(c.getNombre()) && "Uruguay".equals(c.getPais()))
        .findFirst();

    assertTrue(encontrada.isPresent());
  }

  // =============== listarCiudades ===============

  @Test
  void listarCiudades_mapeaYOrdena_caseInsensitive_nullPrimero() {
    // Mezcla con null y mayúsculas/minúsculas
	ciudadServiceFake.agregarCiudad(new Ciudad(null, "X", "aerop", "desc", new Date(), "http://x"));
	ciudadServiceFake.agregarCiudad(new Ciudad("beta",  "PaisA", "ap", "d", new Date(), "http://b"));
	ciudadServiceFake.agregarCiudad(new Ciudad("Alpha", "PaisB", "ap", "d", new Date(), "http://a"));
    List<DataCiudad> out = sistema.listarCiudades();

    assertEquals(3, out.size());
    assertNull(out.get(0).getNombre());           // null primero
    assertEquals("Alpha", out.get(1).getNombre()); // luego "Alpha"
    assertEquals("beta",  out.get(2).getNombre()); // luego "beta"
  }

  // =============== buscarCiudad ===============

  @Test
  void buscarCiudad_parametrosInvalidos_devuelveNull() {
    assertNull(sistema.buscarCiudad(null, "UY"));
    assertNull(sistema.buscarCiudad("   ", "UY"));
    assertNull(sistema.buscarCiudad("Salto", null));
    assertNull(sistema.buscarCiudad("Salto", "   "));
  }

  @Test
  void buscarCiudad_ok_devuelveCiudad() {
    // Registrar debe, vía ManejadorCiudad.toEntity(...), crear la entidad y (asumido) dar de alta en los índices usados por buscarCiudad
    sistema.registrarCiudad(new DataCiudad("Rocha", "Uruguay", "Rochita", "Desc Aeropuerto", new Date(), "http://rochita.com"));
    Ciudad c = sistema.buscarCiudad("Rocha", "Uruguay");
    assertNotNull(c);
    assertEquals("Rocha", c.getNombre());
    assertEquals("Uruguay", c.getPais());
  }

  // =================== Fake service ===================

  /** Fake de CiudadService que trabaja en memoria y expone exactamente lo que usa tu código. */
  static class CiudadServiceFake extends BD.CiudadService {
    // Key: nombre|pais en minúsculas
    private final Map<String, Ciudad> base = new HashMap<>();

    private static String key(String nombre, String pais) {
      String n = nombre == null ? null : nombre.toLowerCase(Locale.ROOT);
      String p = pais    == null ? null : pais.toLowerCase(Locale.ROOT);
      return n + "|" + p;
    }

    void agregarCiudad(Ciudad c) {
      base.put(key(c.getNombre(), c.getPais()), c);
    }

    @Override
    public boolean existeCiudad(String nombre, String pais) {
      if (nombre == null || pais == null) return false;
      return base.containsKey(key(nombre, pais));
    }

    @Override
    public void crearCiudad(Ciudad c) {
      if (c == null) throw new IllegalArgumentException("Ciudad null");
      base.put(key(c.getNombre(), c.getPais()), c);
      // Si tu ManejadorCiudad.toEntity llena alguna estructura estática (p.ej. CiudadPorHash),
      // también podrías simularlo acá si fuera necesario.
    }

    @Override
    public List<Ciudad> listarCiudades() {
      return new ArrayList<>(base.values()); // sin ordenar
    }
  }

	//Los demás servicios no se usan acá; fakes vacíos están bien
	 static class UsuarioServiceFake extends BD.UsuarioService {}
	 static class PaqueteServiceFake  extends BD.PaqueteService  {}
	 static class ClienteServiceFake  extends BD.ClienteService  {}
	 static class ReservaServiceFake  extends BD.ReservaService  {}
	 static class CategoriaServiceFake extends BD.CategoriaService {
	}	

}