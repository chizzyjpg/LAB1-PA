package Logica;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.*;

public class sistemaTest_Paquetes {

  private UsuarioServiceFake usuarios; //Esta vez reutilice de TestRegistro
  private PaqueteServiceFake paquetes; // Esta vez reutilice de TestPaquete
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
  
  // ================== listarPaquetesSinCompras ==================

  @Test
  void listarPaquetesSinCompras_excluyeComprados_yOrdenaCaseInsensitive() {
    // Sembramos más paquetes
    TipoAsiento tipo = TipoAsiento.TURISTA;
    paquetes.agregar(new Paquete("beta","desc", 1, tipo, 0, 10, new BigDecimal("1.00")));
    paquetes.agregar(new Paquete("Alpha","desc", 1, tipo, 0, 10, new BigDecimal("1.00")));
    paquetes.agregar(new Paquete("  BeTa  ","desc", 1, tipo, 0, 10, new BigDecimal("1.00"))); // duplicado “canónico”

    // Marcamos como “comprados” (el servicio devuelve nombres canónicos)
    paquetes.setPaquetesComprados(List.of("  beta  ")); // mezcla espacios/case

    var out = sistema.listarPaquetesSinCompras();

    // Debe filtrar “beta” (en todas sus variantes) y dejar Escapada + Alpha
    assertEquals(2, out.size());
    assertEquals("Alpha", out.get(0).getNombre());     // orden case-insensitive
    assertEquals("Escapada", out.get(1).getNombre());
  }

	  // ================== agregarRutaAPaquete ==================
	
	  @Test
	  void agregarRutaAPaquete_validaParametrosObligatorios() {
	    assertThrows(IllegalArgumentException.class,
	        () -> sistema.agregarRutaAPaquete(null, "aero", "ruta", TipoAsiento.TURISTA, 1));
	    assertThrows(IllegalArgumentException.class,
	        () -> sistema.agregarRutaAPaquete("P", null, "ruta", TipoAsiento.TURISTA, 1));
	    assertThrows(IllegalArgumentException.class,
	        () -> sistema.agregarRutaAPaquete("P", "aero", null, TipoAsiento.TURISTA, 1));
	    assertThrows(IllegalArgumentException.class,
	        () -> sistema.agregarRutaAPaquete("P", "aero", "ruta", null, 1));
	    assertThrows(IllegalArgumentException.class,
	        () -> sistema.agregarRutaAPaquete("P", "aero", "ruta", TipoAsiento.TURISTA, 0));
	  }
	
	  @Test
	  void agregarRutaAPaquete_lanza_siPaqueteNoExiste() {
	    // Necesitamos que verInfoUsuario(nick aero) no devuelva null
	    usuarios.agregarAerolinea(new Aerolinea("Aero","aero","a@x.com","p","d","w"));
	    assertThrows(IllegalArgumentException.class,
	        () -> sistema.agregarRutaAPaquete("NoExiste", "aero", "RutaX", TipoAsiento.TURISTA, 2));
	  }
	
	  @Test
	  void agregarRutaAPaquete_lanza_siPaqueteTieneComprasPrevias() {
	    // Paquete existente “Escapada” ya está sembrado en @BeforeEach
	    usuarios.agregarAerolinea(new Aerolinea("Aero","aero","a@x.com","p","d","w"));
	    // Simulamos compra previa de “Escapada” (Sistema revisa su lista interna de compras)
	    var dto = new DataCompraPaquete("Escapada","mmarce", new Date(), null, null);
	    assertDoesNotThrow(() -> sistema.comprarPaquete(dto)); // ahora el paquete tiene compras
	
	    assertThrows(IllegalStateException.class,
	        () -> sistema.agregarRutaAPaquete("Escapada", "aero", "Ruta MVD-MAD", TipoAsiento.TURISTA, 2));
	  }
	
	  @Test
	  void agregarRutaAPaquete_fijaTipoSiEsNull_yCalculaCostoTurista_conDescuento() {
	    // Creamos un paquete nuevo con tipoAsiento = null y descuento conocido
	    Paquete promo = new Paquete("Promo","desc", 0, null /*tipo*/, 20 /*descuento%*/, 30, null /*costo*/);
	    paquetes.agregar(promo);
	
	    // Sembramos aerolínea y ruta con costos
	    usuarios.agregarAerolinea(new Aerolinea("Jet","jet","jet@x.com","p","d","w"));
	    Ruta r = new Ruta("MVD-MIA","COD", null, null, 0, null, null, 0, null, null, null);
	    r.setCostoBase(new BigDecimal("100.00"));
	    r.setCostoEjecutivo(new BigDecimal("200.00"));
	    paquetes.registrarRutaEnAerolinea("jet", "MVD-MAD", r);
	
	    // cantidad = 3 ; descuento 20% => 100 * 3 * 0.8 = 240.00
	    assertDoesNotThrow(() ->
	        sistema.agregarRutaAPaquete("Promo", "jet", "MVD-MIA", TipoAsiento.TURISTA, 3));
	
	    Paquete actualizado = paquetes.get("Promo");
	    assertEquals(TipoAsiento.TURISTA, actualizado.getTipoAsiento());
	    assertEquals(0, actualizado.getCosto().compareTo(new BigDecimal("240.00")));
	    assertEquals(3, actualizado.getCuposMaximos());
	    assertEquals(3, actualizado.getCuposDisponibles());
	    assertTrue(actualizado.getRutasIncluidas().contains("mvd-mia"));
	  }
	
	  @Test
	  void agregarRutaAPaquete_acumulaCostoEjecutivo_conDescuento_yRespetaTipoFijado() {
	    // Paquete con tipoAsiento EJECUTIVO ya fijado y costo inicial 0, descuento 10%
	    Paquete execPack = new Paquete("ExecPack","desc", 0, TipoAsiento.EJECUTIVO, 10, 30, BigDecimal.ZERO);
	    paquetes.agregar(execPack);
	
	    usuarios.agregarAerolinea(new Aerolinea("Jet","jet","jet@x.com","p","d","w"));
	    Ruta r = new Ruta("MVD-MIA","COD", null, null, 0, null, null, 0, null, null, null);
	    r.setCostoBase(new BigDecimal("50.00"));
	    r.setCostoEjecutivo(new BigDecimal("200.00"));
	    paquetes.registrarRutaEnAerolinea("jet", "MVD-MIA", r);
	
	    // 1) agregar 2 ejecutivos: 200 * 2 * 0.9 = 360
	    sistema.agregarRutaAPaquete("ExecPack", "jet", "MVD-MIA", TipoAsiento.EJECUTIVO, 2);
	    assertEquals(0, paquetes.get("ExecPack").getCosto().compareTo(new BigDecimal("360.00")));
	
	    // 2) intentar agregar TURISTA debe fallar porque tipo ya fijado a EJECUTIVO
	    assertThrows(IllegalArgumentException.class,
	        () -> sistema.agregarRutaAPaquete("ExecPack", "jet", "MVD-MIA", TipoAsiento.TURISTA, 1));
	  }
	
	  @Test
	  void agregarRutaAPaquete_conComprasVarias_ejecutaEqualsFalseYTrue_yLanzaPorCompras() {
	    // Paquete objetivo ya existe: "Escapada"
	    // Creamos OTRO paquete y lo compramos para que el anyMatch vea primero 'false', linea 642 Sistema
	    TipoAsiento tipo = TipoAsiento.TURISTA;
	    Paquete otro = new Paquete("  oTrO  ","d", 1, tipo, 0, 10, BigDecimal.ZERO);
	    paquetes.agregar(otro);

	    // Aerolínea/ruta mínimas para pasar validaciones previas
	    usuarios.agregarAerolinea(new Aerolinea("Jet","jet","jet@x.com","p","d","w"));
	    Ruta r = new Ruta("MVD-MIA","COD", null, null, 0, null, null, 0, null, null, null);
	    r.setCostoBase(new BigDecimal("100"));
	    r.setCostoEjecutivo(new BigDecimal("200"));
	    paquetes.registrarRutaEnAerolinea("jet", "MVD-MAD", r);

	    // 1) Compra de OTRO paquete (hará equals=false en la lambda)
	    assertDoesNotThrow(() ->
	        sistema.comprarPaquete(new DataCompraPaquete("  oTrO  ", "mmarce", new Date(), null, null)));

	    // 2) Compra del mismo paquete "Escapada" (hará equals=true en la siguiente iteración)
	    assertDoesNotThrow(() ->
	        sistema.comprarPaquete(new DataCompraPaquete("Escapada", "mmarce", new Date(), null, null)));

	    // 3) Al intentar agregar ruta, debe lanzar por "ya tiene compras"
	    assertThrows(IllegalStateException.class, () ->
	        sistema.agregarRutaAPaquete("Escapada", "jet", "MVD-MAD", TipoAsiento.TURISTA, 1));
	  }

	  @Test
	  void agregarRutaAPaquete_ejecutivo_conCostoNull_yDescuento() {
	    // Paquete con costo NULL y sin tipo aún; descuento 25%
	    Paquete pack = new Paquete("BusinessNull","d", 0, null, 25, 10, null);
	    paquetes.agregar(pack);

	    usuarios.agregarAerolinea(new Aerolinea("Jet","jet","jet@x.com","p","d","w"));
	    Ruta r = new Ruta("MVD-MIA","COD", null, null, 0, null, null, 0, null, null, null);
	    r.setCostoBase(new BigDecimal("50"));
	    r.setCostoEjecutivo(new BigDecimal("120"));
	    paquetes.registrarRutaEnAerolinea("jet", "MVD-MIA", r);

	    // cantidad=4; 120 * 4 * 0.75 = 360
	    assertDoesNotThrow(() ->
	        sistema.agregarRutaAPaquete("BusinessNull", "jet", "MVD-MIA", TipoAsiento.EJECUTIVO, 4));

	    Paquete actualizado = paquetes.get("BusinessNull");
	    assertEquals(TipoAsiento.EJECUTIVO, actualizado.getTipoAsiento());
	    assertEquals(0, actualizado.getCosto().compareTo(new BigDecimal("360")));
	    assertEquals(4, actualizado.getCuposMaximos());
	    assertEquals(4, actualizado.getCuposDisponibles());
	    assertTrue(actualizado.getRutasIncluidas().contains("mvd-mia"));
	  }
	  
	  @Test
	  void agregarRutaAPaquete_sinCompras_listaVacia_noIteraYNoLanza() {
	    // Paquete objetivo
	    Paquete p = new Paquete("PackVacio","d", 0, null, 0, 10, null);
	    paquetes.agregar(p);

	    // Aerolínea y ruta mínimas
	    usuarios.agregarAerolinea(new Aerolinea("Jet","jet","jet@x.com","p","d","w"));
	    Ruta r = new Ruta("R1","C", null, null, 0, null, null, 0, null, null, null);
	    r.setCostoBase(new BigDecimal("10"));
	    r.setCostoEjecutivo(new BigDecimal("20"));
	    paquetes.registrarRutaEnAerolinea("jet","R1", r);

	    // compras del sistema VACÍAS → anyMatch no itera, tieneCompras=false
	    assertDoesNotThrow(() ->
	        sistema.agregarRutaAPaquete("PackVacio","jet","R1", TipoAsiento.TURISTA, 1));
	  }

	  @Test
	  void agregarRutaAPaquete_comprasDeOtros_noLanza() {
	    // Paquete objetivo
	    Paquete p = new Paquete("PackOtro","d", 0, null, 0, 10, null);
	    paquetes.agregar(p);

	    // Paquete "OtroPack" que SÍ existe (para poder comprarlo)
	    Paquete otroPack = new Paquete("OtroPack","d", 1, TipoAsiento.TURISTA, 0, 10, BigDecimal.ZERO);
	    paquetes.agregar(otroPack);

	    // Aerolínea y ruta mínimas
	    usuarios.agregarAerolinea(new Aerolinea("Jet","jet","jet@x.com","p","d","w"));
	    Ruta r = new Ruta("R1","C", null, null, 0, null, null, 0, null, null, null);
	    // Usa los setters que tu producción realmente lee (si tu código usa getCostoTurista/getCostoEjecutivo, ponelos):
	    r.setCostoBase(new BigDecimal("10"));
	    r.setCostoEjecutivo(new BigDecimal("20"));
	    paquetes.registrarRutaEnAerolinea("jet","R1", r);

	    // Registramos una compra de OTRO paquete -> el anyMatch itera y equals(...) da FALSE
	    assertDoesNotThrow(() ->
	        sistema.comprarPaquete(new DataCompraPaquete("OtroPack","mmarce", new Date(), null, null)));

	    // Debe NO lanzar (tieneCompras=false)
	    assertDoesNotThrow(() ->
	        sistema.agregarRutaAPaquete("PackOtro","jet","R1", TipoAsiento.TURISTA, 1));
	  }

	  
	  @Test
	  void agregarRutaAPaquete_ejecutivo_acumulaSobreCostoNoNull() {
	    // Paquete con costo INICIAL no-nulo (0) y tipo ya EJECUTIVO
	    Paquete exec = new Paquete("ExecAcum","d", 0, TipoAsiento.EJECUTIVO, 0, 10, BigDecimal.ZERO);
	    paquetes.agregar(exec);

	    usuarios.agregarAerolinea(new Aerolinea("Jet","jet","jet@x.com","p","d","w"));
	    Ruta r = new Ruta("R2","C", null, null, 0, null, null, 0, null, null, null);
	    r.setCostoBase(new BigDecimal("1"));
	    r.setCostoEjecutivo(new BigDecimal("50"));
	    paquetes.registrarRutaEnAerolinea("jet","R2", r);

	    // primera suma: 50 * 2 = 100
	    sistema.agregarRutaAPaquete("ExecAcum","jet","R2", TipoAsiento.EJECUTIVO, 2);
	    assertEquals(0, paquetes.get("ExecAcum").getCosto().compareTo(new BigDecimal("100")));

	    // segunda suma: + 50 * 1 = 150 (acumulado)
	    sistema.agregarRutaAPaquete("ExecAcum","jet","R2", TipoAsiento.EJECUTIVO, 1);
	    assertEquals(0, paquetes.get("ExecAcum").getCosto().compareTo(new BigDecimal("150")));
	  }	  
	  
	  
   // ===== helpers =====
	  
	  private static String canon(String s) {
		  return (s == null) ? null : s.trim().toLowerCase(Locale.ROOT);
		}
	  
  // =================== FAKES ===================

  static class UsuarioServiceFake extends BD.UsuarioService {
	    private final Map<String, Cliente> clientes = new HashMap<>();
	    private final Map<String, Aerolinea> aerolineas = new HashMap<>();
	    private final Set<String> compras = new HashSet<>();

	    void agregarCliente(Cliente c) { clientes.put(c.getNickname().toLowerCase(), c); }
	    void agregarAerolinea(Aerolinea a) { aerolineas.put(a.getNickname().toLowerCase(), a); }
	    void marcarCompra(String nick, String paquete) { compras.add(key(nick, paquete)); }
	    private String key(String n, String p) { return n.toLowerCase()+"||"+p.toLowerCase(); }

	    @Override public Cliente obtenerClientePorNickname(String nick) {
	      if (nick == null) return null; return clientes.get(nick.toLowerCase());
	    }
	    @Override public Aerolinea obtenerAerolineaPorNickname(String nick) {
	      if (nick == null) return null; return aerolineas.get(nick.toLowerCase());
	    }
	    @Override public boolean clienteYaComproPaquete(String nick, String paq) {
	      if (nick == null || paq == null) return false;
	      return compras.contains(key(nick, paq));
	    }
	    @Override public boolean existeNickname(String nick) {
	      return nick != null && (clientes.containsKey(nick.toLowerCase()) || aerolineas.containsKey(nick.toLowerCase()));
	    }
	    @Override public boolean existeEmail(String email) {
	      if (email == null) return false;
	      String e = email.toLowerCase();
	      boolean enClientes = clientes.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(e));
	      boolean enAeros    = aerolineas.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(e));
	      return enClientes || enAeros;
	    }
	    @Override public Usuario autenticarPorNicknamePlano(String nick, String pass) {
	      Cliente c = obtenerClientePorNickname(nick);
	      return (c != null && pass != null && pass.equals(c.getContrasenia())) ? c : null;
	    }
	    @Override public Usuario autenticarUsuario(String nick, String pass) { return autenticarPorNicknamePlano(nick, pass); }

	    @Override public void actualizarUsuario(Usuario u) {
	      if (u instanceof Cliente c) clientes.put(c.getNickname().toLowerCase(), c);
	      if (u instanceof Aerolinea a) aerolineas.put(a.getNickname().toLowerCase(), a);
	    }

	    @Override public DataUsuario verInfoUsuario(String nick) {
	      if (nick == null) return null;
	      Cliente c = obtenerClientePorNickname(nick);
	      if (c != null) {
	        return new DataCliente(
	            c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(),
	            c.getApellido(), c.getFechaNac(), c.getNacionalidad(),
	            c.getTipoDocumento(), c.getNumDocumento()
	        );
	      }
	      Aerolinea a = obtenerAerolineaPorNickname(nick);
	      if (a != null) {
	        return new DataAerolinea(a.getNombre(), a.getNickname(), a.getEmail(),
	            a.getContrasenia(), a.getDescGeneral(), a.getLinkWeb());
	      }
	      return null;
	    }
	  }


  static class PaqueteServiceFake extends BD.PaqueteService {
	    private final Map<String, Paquete> porNombre = new HashMap<>();
	    private final Set<String> nombresCompradosCanonicos = new HashSet<>();
	    private final Map<String, Map<String, Ruta>> rutasPorAero = new HashMap<>();
	    CompraPaquete ultimaCompra;

	    void agregar(Paquete p) { porNombre.put(p.getNombre(), p); }
	    Paquete get(String n) { return porNombre.get(n); }

	    void setPaquetesComprados(List<String> nombres) {
	    	  nombresCompradosCanonicos.clear();
	    	  if (nombres != null) {
	    	    for (String s: nombres) {
	    	      nombresCompradosCanonicos.add(canon(s));
	    	    }
	    	  }
	    	}

	    void registrarRutaEnAerolinea(String nick, String nombreRuta, Ruta r) {
	      rutasPorAero.computeIfAbsent(nick.toLowerCase(Locale.ROOT), k -> new HashMap<>())
	          .put(nombreRuta, r);
	    }

	    @Override public Paquete existePaquete(String nombre) { return porNombre.get(nombre); }
	    @Override public void actualizarPaquete(Paquete p) { porNombre.put(p.getNombre(), p); }
	    @Override public List<Paquete> listarPaquetes() { return new ArrayList<>(porNombre.values()); }

	    @Override public List<String> listarNombresPaquetesComprados() {
	      return new ArrayList<>(nombresCompradosCanonicos);
	    }

	    @Override public Ruta buscarRutaEnAerolinea(String nicknameAerolinea, String nombreRuta) {
	      Map<String, Ruta> m = rutasPorAero.get(nicknameAerolinea.toLowerCase(Locale.ROOT));
	      return (m == null) ? null : m.get(nombreRuta);
	    }

	    @Override
	    public CompraPaquete comprarPaquete(CompraPaquete cp, Cliente c, Paquete p) {
	      this.ultimaCompra = cp;
	      if (p != null && p.getNombre() != null) {
	        nombresCompradosCanonicos.add(canon(p.getNombre())); // <- antes: Sistema.canonical(...)
	      }
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
