package Logica;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Logica.sistemaTest_RegistroUsuariosYModificaciones.UsuarioServiceFake;


public class sistemaTest_Reservas {

  private UsuarioServiceFake usuarioService;
  private ReservaServiceFake reservaService;
  private Sistema sistema;

  // Entidades base para armar el escenario
  private Aerolinea aerolinea;         // nickname: "jetair"
  private Ruta ruta;                   // nombre: "Montevideo - Buenos Aires"
  private VueloEspecifico vuelo;       // nombre: "UY123"
  private Cliente clienteAna;          // nickname: "ana"
  private Cliente clienteBeto;         // nickname: "beto"

  @BeforeEach
  void setup() {
    usuarioService = new UsuarioServiceFake();
    reservaService = new ReservaServiceFake();

    sistema = new Sistema(
        usuarioService, new CategoriaServiceFake(), new CiudadServiceFake(),
        new PaqueteServiceFake(), new ClienteServiceFake(), reservaService, new RutaVueloServiceFake()
    );

    // ---------- Aerolínea + Ruta + Vuelo ----------
    aerolinea = new Aerolinea(); // ctor vacío
    aerolinea.setNickname("jetair");
    aerolinea.setNombre("Jet Air S.A.");

    ruta = new Ruta(); // ctor vacío
    ruta.setNombre("Montevideo - Buenos Aires");
    if (ruta.getVuelosEspecificos() == null) {
      ruta.setVuelosEspecificos(new HashSet<>());
    }

    vuelo = new VueloEspecifico();
    vuelo.setNombre("UY123"); // “código de vuelo” que filtras

    // vínculos ruta <-> vuelo
    ruta.getVuelosEspecificos().add(vuelo);

    // >>> Agregar rutas a la aerolínea usando un bucle for hasta el largo del set <<<
    Set<Ruta> rutasParaAgregar = new HashSet<>();
    rutasParaAgregar.add(ruta);

    // Convertimos a lista para poder usar índice y cumplir "for hasta el largo del set"
    List<Ruta> listaRutas = new ArrayList<>(rutasParaAgregar);
    for (int i = 0; i < listaRutas.size(); i++) {
      aerolinea.addRuta(listaRutas.get(i));
    }

    usuarioService.agregarAerolinea(aerolinea);

    // ---------- Clientes ----------
    clienteAna = new Cliente("Ana", "ana", "ana@ex.com", "pw", "López", new Date(), "Uruguay",
        TipoDocumento.CEDULA, "4.444.444-4");
    clienteBeto = new Cliente("Beto", "beto", "beto@ex.com", "pw", "Pérez", new Date(), "Uruguay",
        TipoDocumento.CEDULA, "3.333.333-3");

    usuarioService.agregarCliente(clienteAna);
    usuarioService.agregarCliente(clienteBeto);

    // ---------- Reserva existente (ID=1) para duplicados/orden ----------
    Reserva r1 = new Reserva();
    r1.setIdReserva(1);
    r1.setCliente(clienteAna);
    r1.setVueloEspecifico(vuelo);
    if (vuelo.getReservas() == null) {
      vuelo.setReservas(new HashSet<>());
    }
    vuelo.getReservas().add(r1);
  }

  // ------------------------
  // listarReservas
  // ------------------------

  @Test
  void listarReservas_ok_ordenado() {
    // Agrego otra reserva con id mayor para verificar orden:
    Reserva r2 = new Reserva();
    r2.setIdReserva(5);
    r2.setCliente(clienteBeto);
    r2.setVueloEspecifico(vuelo);
    vuelo.getReservas().add(r2);

    var datas = sistema.listarReservas("JetAir", "Montevideo - Buenos Aires", "uy123");
    assertEquals(2, datas.size(), "Debe listar 2 reservas");
    assertTrue(datas.get(0).getIdReserva() < datas.get(1).getIdReserva(),
        "Las reservas deben venir ordenadas por id ascendente");
    assertEquals(1, datas.get(0).getIdReserva());
    assertEquals(5, datas.get(1).getIdReserva());
  }

  @Test
  void listarReservas_error_noAirline() {
    var ex = assertThrows(IllegalArgumentException.class, () ->
        sistema.listarReservas("inexistente", "Montevideo - Buenos Aires", "UY123"));
    assertTrue(ex.getMessage().toLowerCase().contains("aerolínea"),
        "Mensaje debe indicar problema de aerolínea");
  }

  @Test
  void listarReservas_error_noRuta() {
    var ex = assertThrows(IllegalArgumentException.class, () ->
        sistema.listarReservas("jetair", "Ruta Fantasma", "UY123"));
    assertTrue(ex.getMessage().toLowerCase().contains("ruta"),
        "Mensaje debe indicar problema de ruta");
  }

  @Test
  void listarReservas_error_noVuelo() {
    var ex = assertThrows(IllegalArgumentException.class, () ->
        sistema.listarReservas("jetair", "Montevideo - Buenos Aires", "ZZ999"));
    assertTrue(ex.getMessage().toLowerCase().contains("vuelo"),
        "Mensaje debe indicar problema de vuelo");
  }

  // ------------------------
  // buscarReserva
  // ------------------------

  @Test
  void buscarReserva_ok() {
    var data = sistema.buscarReserva("jetair", "Montevideo - Buenos Aires", "uy123", 1);
    assertNotNull(data, "Debe devolver la reserva");
    assertEquals(1, data.getIdReserva());
    assertEquals("ana", data.getNickCliente().getNickname());
  }

  @Test
  void buscarReserva_error_noExiste() {
    var ex = assertThrows(IllegalArgumentException.class, () ->
        sistema.buscarReserva("jetair", "Montevideo - Buenos Aires", "uy123", 999));
    assertTrue(ex.getMessage().toLowerCase().contains("reserva"),
        "Mensaje debe indicar reserva inexistente");
  }
  
  @Test
  void buscarReserva_error_noAirline() {
    assertThrows(IllegalArgumentException.class, () ->
        sistema.buscarReserva("inexistente", "Montevideo - Buenos Aires", "UY123", 1));
  }

  @Test
  void buscarReserva_error_noRuta() {
    assertThrows(IllegalArgumentException.class, () ->
        sistema.buscarReserva("jetair", "Ruta Fantasma", "UY123", 1));
  }

  @Test
  void buscarReserva_error_noVuelo() {
    assertThrows(IllegalArgumentException.class, () ->
        sistema.buscarReserva("jetair", "Montevideo - Buenos Aires", "ZZ999", 1));
  }

  // ------------------------
  // registrarReserva
  // ------------------------

  @Test
  void registrarReserva_error_datosNulos() {
    var ex = assertThrows(IllegalArgumentException.class, () ->
        sistema.registrarReserva("jetair", "Montevideo - Buenos Aires", "uy123", null));
    assertTrue(ex.getMessage().toLowerCase().contains("nulos"));
  }

  @Test
  void registrarReserva_error_duplicada() {
    // ANA ya tiene r1
    DataCliente cliente = new DataCliente("ana", "ana", "ana@mail.com", null, "Perez", null, null, null, null);
    DataReserva datos = new DataReserva(0, null, null, null, 0, null, cliente);

    var ex = assertThrows(IllegalArgumentException.class, () ->
        sistema.registrarReserva("jetair", "Montevideo - Buenos Aires", "uy123", datos));
    assertTrue(ex.getMessage().toLowerCase().contains("ya existe"),
        "Debe impedir duplicados de cliente en mismo vuelo");
  }

  @Test
  void registrarReserva_error_clienteInexistente() {
    DataCliente clienteFantasma = new DataCliente("fantasma", "fantasma", "fanta@mail.com", null, "susto", null, null, null, null);
    DataReserva datos = new DataReserva(0, null, null, null, 0, null, clienteFantasma);

    var ex = assertThrows(IllegalArgumentException.class, () ->
        sistema.registrarReserva("jetair", "Montevideo - Buenos Aires", "uy123", datos));
    assertTrue(ex.getMessage().toLowerCase().contains("cliente"),
        "Debe indicar que el cliente no existe");
  }

  @Test
  void registrarReserva_ok() {
    DataCliente cliente = new DataCliente("beto", "beto", "beto@mail.com", null, "Carra", null, null, null, null);
    DataReserva datos = new DataReserva(0, null, null, null, 0, null, cliente);

    int antes = vuelo.getReservas().size();

    sistema.registrarReserva("jetair", "Montevideo - Buenos Aires", "uy123", datos);

    int despues = vuelo.getReservas().size();
    assertEquals(antes + 1, despues, "Debe haberse agregado una reserva");

    Reserva ultima = vuelo.getReservas().stream()
        .max(Comparator.comparingInt(Reserva::getIdReserva))
        .orElseThrow();

    assertEquals("beto", ultima.getCliente().getNickname(), "Cliente asociado incorrecto");
    assertEquals("UY123", ultima.getVueloEspecifico().getNombre(), "Debe vincular el VueloEspecifico");
    assertTrue(ultima.getIdReserva() > 1, "El id nuevo debe ser incremental (>1)");
  }
  
  @Test
  void registrarReserva_error_noAirline() {
    // DataReserva válido mínimo (cliente existente para no interferir con otras validaciones)
    DataCliente cliente = new DataCliente("beto", "beto", "beto@mail.com", null, "Carra", null, null, null, null);
    DataReserva datos = new DataReserva(0, null, null, null, 0, null, cliente);

    assertThrows(IllegalArgumentException.class, () ->
        sistema.registrarReserva("inexistente", "Montevideo - Buenos Aires", "UY123", datos));
  }

  @Test
  void registrarReserva_error_noRuta() {
    DataCliente cliente = new DataCliente("beto", "beto", "beto@mail.com", null, "Carra", null, null, null, null);
    DataReserva datos = new DataReserva(0, null, null, null, 0, null, cliente);

    assertThrows(IllegalArgumentException.class, () ->
        sistema.registrarReserva("jetair", "Ruta Fantasma", "UY123", datos));
  }

  @Test
  void registrarReserva_error_noVuelo() {
    DataCliente cliente = new DataCliente("beto", "beto", "beto@mail.com", null, "Carra", null, null, null, null);
    DataReserva datos = new DataReserva(0, null, null, null, 0, null, cliente);

    assertThrows(IllegalArgumentException.class, () ->
        sistema.registrarReserva("jetair", "Montevideo - Buenos Aires", "ZZ999", datos));
  }
  
	//================================
	//Casos con nombre de RUTA == null
	//================================
	
	@Test
	void listarReservas_error_rutaConNombreNull() {
	 // Aerolínea con una ruta cuyo nombre es null
	 Aerolinea a2 = new Aerolinea();
	 a2.setNickname("nullair");
	 a2.setNombre("Null Air");
	
	 Ruta rNull = new Ruta();
	 rNull.setNombre(null); // <- cubre rt.getNombre() == null dentro del filter
	 if (rNull.getVuelosEspecificos() == null) rNull.setVuelosEspecificos(new HashSet<>());
	 a2.addRuta(rNull);
	 usuarioService.agregarAerolinea(a2);
	
	 assertThrows(IllegalArgumentException.class, () ->
	     sistema.listarReservas("nullair", "Cualquier Ruta", "X"));
	}
	
	@Test
	void buscarReserva_error_rutaConNombreNull() {
	 Aerolinea a2 = new Aerolinea();
	 a2.setNickname("nullair2");
	 a2.setNombre("Null Air 2");
	
	 Ruta rNull = new Ruta();
	 rNull.setNombre(null); // <- cubre rt.getNombre() == null
	 if (rNull.getVuelosEspecificos() == null) rNull.setVuelosEspecificos(new HashSet<>());
	 a2.addRuta(rNull);
	 usuarioService.agregarAerolinea(a2);
	
	 assertThrows(IllegalArgumentException.class, () ->
	     sistema.buscarReserva("nullair2", "Cualquier Ruta", "X", 1));
	}
	
	//==================================
	//Casos con nombre de VUELO == null
	//==================================
	
	@Test
	void listarReservas_error_vueloConNombreNull() {
	 // Aerolínea y ruta válidas, pero el único vuelo tiene nombre null
	 Aerolinea a3 = new Aerolinea();
	 a3.setNickname("nullflight");
	 a3.setNombre("Null Flight Air");
	
	 Ruta r = new Ruta();
	 r.setNombre("Ruta Con Vuelos Null");
	 if (r.getVuelosEspecificos() == null) r.setVuelosEspecificos(new HashSet<>());
	
	 VueloEspecifico vNull = new VueloEspecifico();
	 vNull.setNombre(null); // <- cubre ve.getNombre() == null dentro del filter
	 r.getVuelosEspecificos().add(vNull);
	
	 a3.addRuta(r);
	 usuarioService.agregarAerolinea(a3);
	
	 assertThrows(IllegalArgumentException.class, () ->
	     sistema.listarReservas("nullflight", "Ruta Con Vuelos Null", "CUALQUIERA"));
	}
	
	@Test
	void buscarReserva_error_vueloConNombreNull() {
	 Aerolinea a3 = new Aerolinea();
	 a3.setNickname("nullflight2");
	 a3.setNombre("Null Flight Air 2");
	
	 Ruta r = new Ruta();
	 r.setNombre("Ruta Con Vuelos Null 2");
	 if (r.getVuelosEspecificos() == null) r.setVuelosEspecificos(new HashSet<>());
	
	 VueloEspecifico vNull = new VueloEspecifico();
	 vNull.setNombre(null); // <- cubre ve.getNombre() == null
	 r.getVuelosEspecificos().add(vNull);
	
	 a3.addRuta(r);
	 usuarioService.agregarAerolinea(a3);
	
	 assertThrows(IllegalArgumentException.class, () ->
	     sistema.buscarReserva("nullflight2", "Ruta Con Vuelos Null 2", "CUALQUIERA", 1));
	}
	
	//===================================================
	//registrarReserva con vuelo nombre == null
	//===================================================
	
	@Test
	void registrarReserva_error_vueloConNombreNull() {
	 Aerolinea a4 = new Aerolinea();
	 a4.setNickname("nullflight3");
	 a4.setNombre("Null Flight Air 3");
	
	 Ruta r = new Ruta();
	 r.setNombre("Ruta Con Vuelos Null 3");
	 if (r.getVuelosEspecificos() == null) r.setVuelosEspecificos(new HashSet<>());
	
	 VueloEspecifico vNull = new VueloEspecifico();
	 vNull.setNombre(null); // <- cubre ve.getNombre() == null
	 r.getVuelosEspecificos().add(vNull);
	
	 a4.addRuta(r);
	 usuarioService.agregarAerolinea(a4);
	
	 // DataReserva mínimo válido (cliente existente para no interferir)
	 DataCliente cliente = new DataCliente("beto", "beto", "beto@mail.com", null, "Carra", null, null, null, null);
	 DataReserva datos = new DataReserva(0, null, null, null, 0, null, cliente);
	
	 assertThrows(IllegalArgumentException.class, () ->
	     sistema.registrarReserva("nullflight3", "Ruta Con Vuelos Null 3", "CUALQUIERA", datos));
	}
	
	@Test
	void registrarReserva_ok_ignoraReservasConClienteNull() {
	  // Reserva previa con cliente == null (cubre rama rsv.getCliente() == null)
	  Reserva rNull = new Reserva();
	  rNull.setIdReserva(9);
	  rNull.setCliente(null);
	  rNull.setVueloEspecifico(vuelo);
	  if (vuelo.getReservas() == null) vuelo.setReservas(new HashSet<>());
	  vuelo.getReservas().add(rNull);

	  // Intento registrar "beto" => no debe considerarse duplicada
	  DataCliente cliente = new DataCliente("beto", "beto", "beto@mail.com", null, "Carra", null, null, null, null);
	  DataReserva datos = new DataReserva(0, null, null, null, 0, null, cliente);

	  int antes = vuelo.getReservas().size();

	  sistema.registrarReserva("jetair", "Montevideo - Buenos Aires", "uy123", datos);

	  int despues = vuelo.getReservas().size();
	  assertEquals(antes + 1, despues, "Debe agregar la reserva ignorando las que tienen cliente==null");
	}

	@Test
	void registrarReserva_ok_variasReservasConClienteNullYDistintoNick() {
	  // Dos reservas "ruidosas": una con cliente null y otra con cliente distinto
	  Reserva rNull = new Reserva();
	  rNull.setIdReserva(11);
	  rNull.setCliente(null);
	  rNull.setVueloEspecifico(vuelo);

	  Reserva rOtro = new Reserva();
	  rOtro.setIdReserva(12);
	  rOtro.setCliente(clienteAna); // "ana" ya existe en setup
	  rOtro.setVueloEspecifico(vuelo);

	  if (vuelo.getReservas() == null) vuelo.setReservas(new HashSet<>());
	  vuelo.getReservas().add(rNull);
	  vuelo.getReservas().add(rOtro);

	  // Registro "beto": ni el null ni "ana" deben disparar duplicado
	  DataCliente cliente = new DataCliente("beto", "beto", "beto@mail.com", null, "Carra", null, null, null, null);
	  DataReserva datos = new DataReserva(0, null, null, null, 0, null, cliente);

	  int antes = vuelo.getReservas().size();

	  sistema.registrarReserva("jetair", "Montevideo - Buenos Aires", "uy123", datos);

	  int despues = vuelo.getReservas().size();
	  assertEquals(antes + 1, despues, "Debe permitirse registrar porque no hay coincidencia exacta de nick");
	}

	@Test
	void registrarReserva_error_rutaConNombreNull() {
	  // Aerolínea separada sólo para este caso
	  Aerolinea aN = new Aerolinea();
	  aN.setNickname("nullroute");
	  aN.setNombre("Null Route Air");

	  Ruta rNull = new Ruta();
	  rNull.setNombre(null); // <- fuerza rt.getNombre() == null
	  if (rNull.getVuelosEspecificos() == null) rNull.setVuelosEspecificos(new HashSet<>());

	  aN.addRuta(rNull);                // usa tu addRuta(...)
	  usuarioService.agregarAerolinea(aN);

	  // DataReserva cualquiera (no es null) — uso cliente existente para no interferir
	  DataCliente cliente = new DataCliente("beto", "beto", "beto@mail.com", null, "Carra", null, null, null, null);
	  DataReserva datos = new DataReserva(0, null, null, null, 0, null, cliente);

	  assertThrows(IllegalArgumentException.class, () ->
	      sistema.registrarReserva("nullroute", "Cualquier Ruta", "X", datos));
	}



  // Fakes externos que ya venías usando
  static class RutaVueloServiceFake extends BD.RutaVueloService {}
  static class PaqueteServiceFake   extends BD.PaqueteService {}
  static class ClienteServiceFake   extends BD.ClienteService {}
  static class CategoriaServiceFake  extends BD.CategoriaService {}
  static class CiudadServiceFake    extends BD.CiudadService   {}
  
  
  static class ReservaServiceFake   extends BD.ReservaService  {
	  private final List<Reserva> mem = new ArrayList<>();

	  public ReservaServiceFake() {
	    super(); 
	  }
	  @Override
	  public void registrarReserva(Reserva r) {
	    mem.add(r);
	  }
  }
  
}
