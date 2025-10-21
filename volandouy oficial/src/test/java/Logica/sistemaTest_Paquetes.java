package Logica;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.*;

public class sistemaTest_Paquetes {

  private UsuarioServiceFake usuarios;
  private PaqueteServiceFake paquetes;
  private Sistema sistema;

  @BeforeEach
  void setup() {
    usuarios = new UsuarioServiceFake();
    usuarios.agregarCliente(new Cliente(
        "Eze", "mmarce", "em@ex.com", "abc",
        "Marcenal", new Date(), "Uruguay",
        TipoDocumento.CEDULA, "5.555.555-5"
    ));

    paquetes = new PaqueteServiceFake();
    TipoAsiento tipo = TipoAsiento.EJECUTIVO;
    Paquete packEscapada = new Paquete ("Escapada","Desc", 2, tipo, 10, 20, new BigDecimal("15000.00"));
    packEscapada.setCuposMaximos(10);
    packEscapada.setCuposDisponibles(10);
    paquetes.agregar(packEscapada);
    

    sistema = new Sistema(
        usuarios, new CategoriaServiceFake(), new CiudadServiceFake(),
        paquetes, new ClienteServiceFake(usuarios), new ReservaServiceFake(), new RutaVueloServiceFake()
    );
    
    paquetes.ultimaCompra = null;
    ManejadorCompraPaquete.setServiceForTests(paquetes);
    

  }

  @Test
  void comprarPaquete_lanza_siClienteYaCompro() {
	  // sanity: al inicio no compró
	  assertFalse(sistema.clienteYaComproPaquete("mmarce", "Escapada"));

	  // 1) compra válida (no debería lanzar)
	  assertDoesNotThrow(() ->
	      sistema.comprarPaquete(nuevaCompra("mmarce", "Escapada", new Date()))
	  );

	  // 2) simulamos que quedó registrada en el service de usuarios (fake)
	  usuarios.marcarCompra("mmarce", "Escapada");
	  assertTrue(sistema.clienteYaComproPaquete("mmarce", "Escapada"));

	  // 3) segunda compra debe lanzar por “ya compró”
	  DataCompraPaquete dto2 = nuevaCompra("mmarce", "Escapada", new Date());
	  assertThrows(IllegalArgumentException.class, () -> sistema.comprarPaquete(dto2));
  }
  
  @Test
  void comprarPaquete_lanza_siClienteNull() {
	assertThrows(IllegalArgumentException.class, () -> sistema.comprarPaquete(null));
  }


  @Test
  void listarPaquetesDisponibles_filtra_mapea_y_ordena_conNull() {
    TipoAsiento tipo = TipoAsiento.EJECUTIVO;

    // 0) Este NO debe aparecer (cantRutas = 0)
    Paquete p0 = new Paquete("zzz","d", 0, tipo, 0, 10, new BigDecimal("1.00"));

    // 1) Este SÍ aparece y además fuerza rama nombre==null en el Comparator
    Paquete pNull = new Paquete(null,"d", 1, tipo, 0, 10, new BigDecimal("2.00"));

    // 2) Este SÍ aparece (nombre no nulo) y sirve para el orden
    Paquete pB = new Paquete("beta","d", 2, tipo, 0, 10, new BigDecimal("3.00"));

    paquetes.agregar(p0);
    paquetes.agregar(pNull);
    paquetes.agregar(pB);

    var lista = sistema.listarPaquetesDisponiblesParaCompra();

    // Debe excluir el de cantRutas=0 → quedan 2
    assertEquals(2, lista.size());

    // El null queda primero ("" < "beta"), luego "beta"
    assertNull(lista.get(0).getNombre());
    assertEquals("beta", lista.get(1).getNombre());
  }
  
  @Test
  void clienteYaComproPaquete_delega_enUsuarioService() {
    assertFalse(sistema.clienteYaComproPaquete("mmarce", "Escapada"));
    usuarios.marcarCompra("mmarce", "Escapada"); // nuestro fake
    assertTrue(sistema.clienteYaComproPaquete("mmarce", "Escapada"));
  }
  
  @Test
  void listarClientesParaCompra_usaElServiceInyectado() {
    var lista = sistema.listarClientesParaCompra();
    assertEquals(1, lista.size());
    assertEquals("mmarce", lista.get(0).getNickname());
  }
  
  @Test
  void comprarPaquete_ok_haceDescuentoCupo_yPersisteCompra() {
    Date hoy = new Date();
    DataCompraPaquete dto = nuevaCompra("mmarce", "Escapada", hoy);

    // estado inicial limpio
    paquetes.ultimaCompra = null;
    assertEquals(10, paquetes.get("Escapada").getCuposDisponibles());

    sistema.comprarPaquete(dto);

    // 1) cupo descontado
    assertEquals(9, paquetes.get("Escapada").getCuposDisponibles());

    // 2) “persistencia” final ejecutada en el fake
    assertNotNull(paquetes.ultimaCompra);
    assertEquals("mmarce", paquetes.ultimaCompra.getCliente().getNickname());
    assertEquals("Escapada", paquetes.ultimaCompra.getPaquete().getNombre());

    // 3) costo/vencimiento calculados (en tu helper los pasás null)
    assertNotNull(paquetes.ultimaCompra.getCosto());
    assertNotNull(paquetes.ultimaCompra.getVencimiento());
  }

  @Test
  void comprarPaquete_lanza_siClienteNoExiste() {
    DataCompraPaquete dto = nuevaCompra("noexiste", "Escapada", new Date());
    assertThrows(IllegalArgumentException.class, () -> sistema.comprarPaquete(dto));
    assertNull(paquetes.ultimaCompra);
  }

  @Test
  void comprarPaquete_lanza_siFechaCompraNull() {
    DataCompraPaquete dto = new DataCompraPaquete("Escapada", "mmarce", null, null, null);
    assertThrows(IllegalArgumentException.class, () -> sistema.comprarPaquete(dto));
    assertNull(paquetes.ultimaCompra);
  }

  @Test
  void comprarPaquete_lanza_siSinCupos() {
    paquetes.get("Escapada").setCuposDisponibles(0);
    DataCompraPaquete dto = nuevaCompra("mmarce", "Escapada", new Date());
    assertThrows(IllegalStateException.class, () -> sistema.comprarPaquete(dto));
    assertNull(paquetes.ultimaCompra);
  }

	
  @Test
  void comprarPaquete_lanza_porFechaNula() {
  paquetes.ultimaCompra = null;
  var dtoSinFecha = new DataCompraPaquete("Escapada", "mmarce", null, null, null);
  assertThrows(IllegalArgumentException.class, () -> sistema.comprarPaquete(dtoSinFecha));
  assertNull(paquetes.ultimaCompra); // no llegó a persistir
  }


  @Test 
  void comprarPaqueteNoExiste() {
	  var dtoSinExist = new DataCompraPaquete("Escapadita", "mmarce", null, null, null);
	  assertThrows(IllegalArgumentException.class, () -> sistema.comprarPaquete(dtoSinExist));
  }
  
  @Test
  void comprarPaqueteSinRutas() {
	  TipoAsiento tipo = TipoAsiento.EJECUTIVO;
	  Paquete packSinRuta = new Paquete ("SinRuta","Desc", 0, tipo, 10, 20, new BigDecimal("15000.00"));
	  packSinRuta.setCuposMaximos(10);
	  packSinRuta.setCuposDisponibles(10);
	  paquetes.agregar(packSinRuta);
	  var dtoSinRuta = new DataCompraPaquete("SinRuta", "mmarce", new Date(), null, null);
	  assertThrows(IllegalStateException.class, () -> sistema.comprarPaquete(dtoSinRuta));
  }
  
  
  // ===== helper =====
  private static DataCompraPaquete nuevaCompra(String nick, String paq, Date fecha) {
    // (nombrePaquete, nicknameCliente, fechaCompra, costo, vencimiento)
    return new DataCompraPaquete(paq, nick, fecha, null, null);
  }
  
  

  // =================== FAKES ===================

  static class UsuarioServiceFake extends BD.UsuarioService {
    private final Map<String, Cliente> clientes = new HashMap<>();
    private final Set<String> compras = new HashSet<>(); // nick||paquete

    void agregarCliente(Cliente c) { clientes.put(c.getNickname().toLowerCase(), c); }
    void marcarCompra(String nick, String paquete) { compras.add(key(nick, paquete)); }
    private String key(String n, String p) { return n.toLowerCase()+"||"+p.toLowerCase(); }

    @Override public Cliente obtenerClientePorNickname(String nick) {
      if (nick == null) return null; return clientes.get(nick.toLowerCase());
    }
    @Override public Aerolinea obtenerAerolineaPorNickname(String nick) { return null; }

    @Override public boolean clienteYaComproPaquete(String nick, String paq) {
      if (nick == null || paq == null) return false;
      return compras.contains(key(nick, paq));
    }

    @Override public boolean existeNickname(String nick) {
      return nick != null && clientes.containsKey(nick.toLowerCase());
    }
    @Override public boolean existeEmail(String email) {
      if (email == null) return false;
      String e = email.toLowerCase();
      return clientes.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(e));
    }

    @Override public Usuario autenticarPorNicknamePlano(String nick, String pass) {
      Cliente c = obtenerClientePorNickname(nick);
      return (c != null && pass != null && pass.equals(c.getContrasenia())) ? c : null;
    }
    @Override public Usuario autenticarUsuario(String nick, String pass) { return autenticarPorNicknamePlano(nick, pass); }

    @Override public void actualizarUsuario(Usuario u) {
      if (u instanceof Cliente c) clientes.put(c.getNickname().toLowerCase(), c);
    }

    @Override public DataUsuario verInfoUsuario(String nick) {
      Cliente c = obtenerClientePorNickname(nick);
      if (c == null) return null;
      return new DataCliente(
          c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(),
          c.getApellido(), c.getFechaNac(), c.getNacionalidad(),
          c.getTipoDocumento(), c.getNumDocumento()
      );
    }
  }

  static class PaqueteServiceFake extends BD.PaqueteService {
    private final Map<String, Paquete> porNombre = new HashMap<>();
    CompraPaquete ultimaCompra;
    void agregar(Paquete p) { porNombre.put(p.getNombre(), p); }
    Paquete get(String n) { return porNombre.get(n); }

    @Override public Paquete existePaquete(String nombre) { return porNombre.get(nombre); }
    @Override public void actualizarPaquete(Paquete p) { porNombre.put(p.getNombre(), p); }
    @Override public List<Paquete> listarPaquetes() { return new ArrayList<>(porNombre.values()); }
    @Override public CompraPaquete comprarPaquete(CompraPaquete cp, Cliente c, Paquete p) {
        this.ultimaCompra = cp; // con esto alcanza para los asserts
        return cp;
      }
  }


  static class CategoriaServiceFake extends BD.CategoriaService {}
  static class CiudadServiceFake   extends BD.CiudadService   {}
  static class ReservaServiceFake  extends BD.ReservaService  {}
  static class RutaVueloServiceFake extends BD.RutaVueloService {}

  static class ClienteServiceFake extends BD.ClienteService {
    private final UsuarioServiceFake usuarios;
    ClienteServiceFake(UsuarioServiceFake u) { this.usuarios = u; }
    @Override public List<DataCliente> listarClientes() {
      Cliente c = usuarios.obtenerClientePorNickname("mmarce");
      if (c == null) return List.of();
      return List.of(new DataCliente(
          c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(),
          c.getApellido(), c.getFechaNac(), c.getNacionalidad(),
          c.getTipoDocumento(), c.getNumDocumento()
      ));
    }
  }
}
