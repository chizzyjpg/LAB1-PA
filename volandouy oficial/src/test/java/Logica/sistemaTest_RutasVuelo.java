package Logica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import BD.RutaVueloService;

import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class sistemaTest_RutasVuelo {

  private Sistema sistema;
  private UsuarioServiceFake usuarios;
  private RutaVueloServiceFake rutaServiceFake;
  private static CiudadServiceFake ciudadSrvFake;
  private static CategoriaServiceFake categoriaSrvFake;
  
  @BeforeEach
  void setup() {
    usuarios = new UsuarioServiceFake();
    rutaServiceFake = new RutaVueloServiceFake(usuarios);
    ciudadSrvFake = new CiudadServiceFake();
    categoriaSrvFake = new CategoriaServiceFake();
    
    // Semillas de catálogo
    Ciudad Montevideo = new Ciudad("Montevideo","Uruguay","Carrasco","", new Date(),"");
    ciudadSrvFake.agregar(Montevideo);
    Ciudad Madrid = new Ciudad("Madrid","España","Barajas","", new Date(),"");
    ciudadSrvFake.agregar(Madrid);
    
    Categoria Internacional = new Categoria("Internacional");
    categoriaSrvFake.agregar(Internacional);

    // Inyección en ManejadorRuta (evita BD)
    ManejadorRuta.setCiudadServiceForTests(ciudadSrvFake);
    ManejadorRuta.setCategoriaServiceForTests(categoriaSrvFake);

    // Aerolínea con una ruta existente (para duplicado)
    Aerolinea jet = new Aerolinea("Jet","jet","jet@mail.com","abcd","Desc","web");
    jet.getRutas().add(new Ruta("Montevideo – Madrid","MVD-MAD", Montevideo, Madrid, 10, new Date(), new BigDecimal(100), 100, new BigDecimal(200), Internacional, "Ruta corta"));
    usuarios.agregarAerolinea(jet);

    // Un cliente para probar “no es aerolínea”
    usuarios.agregarCliente(new Cliente("Ana","ana","ana@mail.com","1234","Ap",new Date(),"UY",TipoDocumento.CEDULA,"1"));

    sistema = new Sistema(
        usuarios,
        categoriaSrvFake, // no se usa, el manejador llama al estático inyectado
        ciudadSrvFake,   // no se usa, el manejador llama al estático inyectado
        new PaqueteServiceFake(),
        new ClienteServiceFake(),
        new ReservaServiceFake(),
        rutaServiceFake
    );
  }

  // ========== registrarRuta ==========

  @Test
  void registrarRuta_null_lanzaIAE() {
    assertThrows(IllegalArgumentException.class, () -> sistema.registrarRuta(null));
  }

  @Test
  void registrarRuta_nombreVacio_lanzaIAE() {
	  assertThrows(IllegalArgumentException.class,
	  () -> sistema.registrarRuta(dataRutaSimple("   ", "jet", "Montevideo", "Madrid", "Internacional")));
	  assertThrows(IllegalArgumentException.class,
	  () -> sistema.registrarRuta(dataRutaSimple(null, "jet", "Montevideo", "Madrid", "Internacional")));

  }

  @Test
  void registrarRuta_aerolineaInexistente_lanzaIAE() {
    var d = dataRutaSimple("Ruta X","noexiste","Montevideo","Madrid","Internacional");
    var ex = assertThrows(IllegalArgumentException.class, () -> sistema.registrarRuta(d));
    assertTrue(ex.getMessage().toLowerCase().contains("aerolínea"));
  }

  @Test
  void registrarRuta_nombreDuplicadoExacto_lanzaIAE() {
    var d = dataRutaSimple("Montevideo – Madrid","jet","Montevideo","Madrid","Internacional");
    var ex = assertThrows(IllegalArgumentException.class, () -> sistema.registrarRuta(d));
    assertTrue(ex.getMessage().contains("exactamente"));
  }

  @Test
  void registrarRuta_ok_resuelveCiudadesYCategoria_yPersisteEnFakeYAerolinea() {
    var d = dataRutaSimple("Montevideo – París","jet","Montevideo","Madrid","Internacional");
    assertDoesNotThrow(() -> sistema.registrarRuta(d));

    // Ruta creada por el fake
    assertTrue(rutaServiceFake.creadas.stream().anyMatch(r -> "Montevideo – París".equals(r.getNombre())));

    // Agregada a la aerolínea
    Aerolinea jet = usuarios.obtenerAerolineaPorNickname("jet");
    Ruta r = jet.getRutas().stream().filter(x -> "Montevideo – París".equals(x.getNombre())).findFirst().orElse(null);
    assertNotNull(r);
    // Verifica que toEntity resolvió entidades reales (no null)
    assertEquals("Montevideo", r.getOrigen().getNombre());
    assertEquals("Madrid", r.getDestino().getNombre());
    assertEquals("Internacional", r.getCategoriaR().getNombre());
  }

  // ========== listarPorAerolinea ==========

  @Test
  void listarPorAerolinea_noEsAerolinea_lanzaIAE() {
    var ex = assertThrows(IllegalArgumentException.class, () -> sistema.listarPorAerolinea("ana"));
    assertTrue(ex.getMessage().toLowerCase().contains("aerolínea"));
  }

  @Test
  void listarPorAerolinea_orden_caseInsensitive_nullPrimero() {
    usuarios.setRutasData("jet", Arrays.asList(
        dataRutaNombre(null, "jet"),
        dataRutaNombre("beta", "jet"),
        dataRutaNombre("Alpha", "jet")
    ));
    var out = sistema.listarPorAerolinea("jet");
    assertEquals(3, out.size());
    assertNull(out.get(0).getNombre());
    assertEquals("Alpha", out.get(1).getNombre());
    assertEquals("beta", out.get(2).getNombre());
  }

  // ========== ManejadorRuta: toData / toEntity / helpers ==========

  @Test
  void toData_incluyeNicknameAerolinea_siHayRelacion() {
    Aerolinea a = usuarios.obtenerAerolineaPorNickname("jet");
    Ruta r = new Ruta("Montevideo – Madrid","MVD-MAD",
        ciudadSrvFake.buscarPorNombre("Montevideo"),
        ciudadSrvFake.buscarPorNombre("Madrid"),
        10, new Date(), new BigDecimal(100), 100, new BigDecimal(200),
        categoriaSrvFake.buscarPorNombre("Internacional"), "Ruta corta");

    r.getAerolineas().add(a); // <-- faltaba

    DataRuta dto = ManejadorRuta.toData(r);
    assertEquals("jet", dto.getNicknameAerolinea());
  }


  @Test
  void toEntities_y_toDatas_manejanNulls_y_filtranNulls() {
    // toEntities
    List<DataRuta> in = Arrays.asList(
        dataRutaSimple("R1","jet","Montevideo","Madrid","Internacional"),
        null
    );
    List<Ruta> outEnt = ManejadorRuta.toEntities(in);
    assertEquals(1, outEnt.size());
    assertEquals("R1", outEnt.get(0).getNombre());

    // toDatas
    Ruta r = new Ruta("Montevideo – Madrid","MVD-MAD", ciudadSrvFake.buscarPorNombre("Montevideo"), ciudadSrvFake.buscarPorNombre("Madrid"), 10, new Date(), new BigDecimal(100), 100, new BigDecimal(200), categoriaSrvFake.buscarPorNombre("Internacional"), "Ruta corta");
    List<DataRuta> outDto = ManejadorRuta.toDatas(Arrays.asList(r, null));
    assertEquals(1, outDto.size());
    assertEquals("Montevideo – Madrid", outDto.get(0).getNombre());

    // null total => lista vacía
    assertTrue(ManejadorRuta.toEntities(null).isEmpty());
    assertTrue(ManejadorRuta.toDatas(null).isEmpty());
  }
  
	//---------- cambiarEstadoRuta: tests ----------
	
	@Test
	void cambiarEstadoRuta_estadoNuevoNull_lanzaIAE() {
	 assertThrows(IllegalArgumentException.class, () -> sistema.cambiarEstadoRuta(999, null));
	}
	
	@Test
	void cambiarEstadoRuta_idInexistente_lanzaIAE() {
	 assertThrows(IllegalArgumentException.class, () -> sistema.cambiarEstadoRuta(404, EstadoRuta.INGRESADA));
	}
	
	@Test
	void cambiarEstadoRuta_mismoEstado_lanzaIAE() {
	 // Ruta en INGRESADA con id 100
	 rutaServiceFake.putRuta(100, "R-igual", EstadoRuta.INGRESADA);
	 assertThrows(IllegalArgumentException.class, () -> sistema.cambiarEstadoRuta(100, EstadoRuta.INGRESADA));
	}
	
	@Test
	void cambiarEstadoRuta_noIngresada_lanzaIllegalState() {
	  // Ruta en CONFIRMADA con id 101 (no es INGRESADA)
	  rutaServiceFake.putRuta(101, "R-noing", EstadoRuta.CONFIRMADA);

	  // Intentar cambiar a RECHAZADA debe fallar por "solo INGRESADA puede cambiar"
	  assertThrows(IllegalStateException.class, () -> sistema.cambiarEstadoRuta(101, EstadoRuta.RECHAZADA));
	}

	
	@Test
	void cambiarEstadoRuta_ok_deIngresadaAOtro_actualizaYPersiste() {
	  rutaServiceFake.putRuta(102, "R-ok", EstadoRuta.INGRESADA);

	  assertDoesNotThrow(() -> sistema.cambiarEstadoRuta(102, EstadoRuta.CONFIRMADA));

	  Ruta post = rutaServiceFake.buscarRutaPorId(102);
	  assertEquals(EstadoRuta.CONFIRMADA, post.getEstado());

	  assertTrue(rutaServiceFake.actualizadas.contains(102));
	}

  // ---------- helpers de test ----------

  private static DataRuta dataRutaSimple(String nombre, String nickAero, String nomOri, String nomDes, String cat) {
    DataCiudad ori = new DataCiudad(nomOri, "", "", "", new Date(), "");
    DataCiudad des = new DataCiudad(nomDes, "", "", "", new Date(), "");
    DataCategoria dc = new DataCategoria(cat);
    BigDecimal decimal = new BigDecimal(100);
    EstadoRuta estado = EstadoRuta.INGRESADA;
    // constructor “completo” de tu DataRuta (ajustá si difiere)
    return new DataRuta(nombre, "desc", ori, des, 10, new Date(), decimal, 10, decimal, dc, nickAero, estado, "s"
    );
  }

  private static DataRuta dataRutaNombre(String nombre, String nickAero) {
    DataCiudad ori = new DataCiudad("Montevideo", "", "", "", new Date(), "");
    DataCiudad des = new DataCiudad("Madrid", "", "", "", new Date(), "");
    return new DataRuta(nombre, "desc", ori, des, 10, new Date(), null, 10, null, null, nickAero, null, "s");
  }

  // ================== FAKES ==================

  // UsuarioService con soporte para rutas y aerolíneas
  static class UsuarioServiceFake extends BD.UsuarioService {
    private final Map<String, Cliente> clientes   = new HashMap<>();
    private final Map<String, Aerolinea> aerolineas = new HashMap<>();
    private final Map<String, List<DataRuta>> rutasPorAero = new HashMap<>();

    void agregarCliente(Cliente c){ clientes.put(c.getNickname().toLowerCase(Locale.ROOT), c); }
    void agregarAerolinea(Aerolinea a){ aerolineas.put(a.getNickname().toLowerCase(Locale.ROOT), a); }

    void setRutasData(String nick, List<DataRuta> rutas){
      rutasPorAero.put(nick.toLowerCase(Locale.ROOT), new ArrayList<>(rutas));
    }

    @Override public Aerolinea obtenerAerolineaPorNickname(String nick) {
      return nick == null ? null : aerolineas.get(nick.toLowerCase(Locale.ROOT));
    }

    @Override public DataUsuario verInfoUsuario(String nick) {
      if (nick == null) return null;
      String k = nick.toLowerCase(Locale.ROOT);
      if (aerolineas.containsKey(k)) {
        Aerolinea a = aerolineas.get(k);
        return new DataAerolinea(a.getNombre(), a.getNickname(), a.getEmail(), a.getContrasenia(), a.getDescGeneral(), a.getLinkWeb());
      }
      if (clientes.containsKey(k)) {
        Cliente c = clientes.get(k);
        return new DataCliente(c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(), c.getApellido(), c.getFechaNac(), c.getNacionalidad(), c.getTipoDocumento(), c.getNumDocumento());
      }
      return null;
    }

    @Override public List<DataRuta> listarRutasPorAerolinea(String nicknameAerolinea) {
      List<DataRuta> l = rutasPorAero.get(nicknameAerolinea.toLowerCase(Locale.ROOT));
      return l == null ? Collections.emptyList() : new ArrayList<>(l);
    }

    @Override public void actualizarUsuario(Usuario u) {
      String k = u.getNickname().toLowerCase(Locale.ROOT);
      if (u instanceof Aerolinea a) aerolineas.put(k, a);
      if (u instanceof Cliente c)    clientes.put(k, c);
    }
  }

  // Fake de RutaVueloService: guarda creadas y las mete en la aerolínea
  static class RutaVueloServiceFake extends RutaVueloService {
	  
    final UsuarioServiceFake usuarios;
    final List<Ruta> creadas = new ArrayList<>();
    final Map<Integer, Ruta> porId = new HashMap<>();
    final Set<Integer> actualizadas = new HashSet<>();
    
    
    RutaVueloServiceFake(UsuarioServiceFake u){ 
    	this.usuarios = u; 
    	}

    @Override public void crearRutaVuelo(Ruta ruta, String nicknameAerolinea) {
      if (ruta == null) throw new IllegalArgumentException("ruta null");
      creadas.add(ruta);
      Aerolinea a = usuarios.obtenerAerolineaPorNickname(nicknameAerolinea);
      if (a != null) a.getRutas().add(ruta);
    }
    
    Ruta putRuta(int id, String nombre, EstadoRuta estado) {
        Ruta r = new Ruta ("San Jose – Montevideo","SJ-MDO", ciudadSrvFake.buscarPorNombre("Montevideo"), ciudadSrvFake.buscarPorNombre("Madrid"), 10, new Date(), new BigDecimal(100), 100, new BigDecimal(200), categoriaSrvFake.buscarPorNombre("Internacional"), "Ruta corta");
        r.setEstado(estado);
        porId.put(id, r);
        return r;
      }
    
    @Override
    public Ruta buscarRutaPorId(Integer id) {
      return porId.get(id);
    }

    @Override
    public void actualizarRuta(Ruta ruta) {
      // marcamos como actualizada para asertar en tests
      // si tu Ruta tiene getIdRuta(), podés usarlo; si no, usamos la key ya buscada
      porId.entrySet().stream()
          .filter(e -> e.getValue() == ruta)
          .map(Map.Entry::getKey)
          .findFirst()
          .ifPresent(actualizadas::add);
    }
    
  }

  // Fakes de catálogo que usa el manejador
  static class CiudadServiceFake extends BD.CiudadService {
    private final Map<String,Ciudad> porNombre = new HashMap<>();
    void agregar(Ciudad c){ porNombre.put(c.getNombre().toLowerCase(Locale.ROOT), c); }
    @Override public Ciudad buscarPorNombre(String nombre){
      return nombre == null ? null : porNombre.get(nombre.toLowerCase(Locale.ROOT));
    }
  }
  static class CategoriaServiceFake extends BD.CategoriaService {
    private final Map<String,Categoria> porNombre = new HashMap<>();
    void agregar(Categoria c){ porNombre.put(c.getNombre().toLowerCase(Locale.ROOT), c); }
    @Override public Categoria buscarPorNombre(String nombre){
      return nombre == null ? null : porNombre.get(nombre.toLowerCase(Locale.ROOT));
    }
  }

  // Fakes “passthrough” para cumplir el constructor de Sistema (no se usan)
  static class PaqueteServiceFake  extends BD.PaqueteService {}
  static class ClienteServiceFake  extends BD.ClienteService {}  
  static class ReservaServiceFake  extends BD.ReservaService {}
}
