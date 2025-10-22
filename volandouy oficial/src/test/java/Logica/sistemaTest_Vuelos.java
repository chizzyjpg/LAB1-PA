package Logica;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class sistemaTest_Vuelos {

  private UsuarioServiceFake usuarioService;
  private RutaVueloServiceFake rutaService;
  private VueloServiceFake vueloService;

  private Sistema sistema;

  // Escenario base
  private Aerolinea aerolinea;
  private Ruta ruta;

  @BeforeEach
  void setup() {
	rutaService      = new RutaVueloServiceFake();
    vueloService 	 = new VueloServiceFake();
    
 // Aerolínea y ruta base (sin vuelos)
    aerolinea = new Aerolinea();
    aerolinea.setNickname("jetair");
    aerolinea.setNombre("Jet Air S.A.");

    ruta = new Ruta();
    ruta.setNombre("Montevideo - Buenos Aires");
    
    if (ruta.getVuelosEspecificos() == null) ruta.setVuelosEspecificos(new HashSet<>());
    aerolinea.addRuta(ruta);  
    
    usuarioService   = new UsuarioServiceFake(aerolinea, ruta);
    
    sistema = new Sistema(
            usuarioService, null, null,
            null, null, null, rutaService
        ); 
    
        usuarioService.agregarAerolinea(aerolinea);

        rutaService.persistRuta(ruta);
        ManejadorVueloEspecifico.setServicesForTests(rutaService, vueloService);
       
      }
  
  @org.junit.jupiter.api.AfterEach
  void tearDown() {
    ManejadorVueloEspecifico.setServicesForTests(null, null);
  }

      // -------------
      // registrarVuelo
      // -------------

      @Test
      void registrarVuelo_error_datosNulos() {
        assertThrows(IllegalArgumentException.class, () ->
            sistema.registrarVuelo("jetair", "Montevideo - Buenos Aires", null));
      }

      @Test
      void registrarVuelo_error_idRutaNull() {
        rutaService.clear();
        DataVueloEspecifico datos = dataVuelo("UY900");
        assertThrows(IllegalArgumentException.class, () ->
            sistema.registrarVuelo("jetair", "Montevideo - Buenos Aires", datos));
      }

      @Test
      void registrarVuelo_error_rutaNull() {
        rutaService.mapNombreToId.put("Ruta Fantasma", 123);
        DataVueloEspecifico datos = dataVuelo("UY901");
        assertThrows(IllegalArgumentException.class, () ->
            sistema.registrarVuelo("jetair", "Ruta Fantasma", datos));
      }

      @Test
      void registrarVuelo_ok_agregaVueloEnRuta() {
        int antes = ruta.getVuelosEspecificos().size();

        DataVueloEspecifico datos = dataVuelo("UY999");
        sistema.registrarVuelo("jetair", "Montevideo - Buenos Aires", datos);

        int despues = ruta.getVuelosEspecificos().size();
        assertEquals(antes + 1, despues, "Debe agregarse un vuelo a la ruta");

        boolean existe = ruta.getVuelosEspecificos().stream()
            .anyMatch(v -> "UY999".equalsIgnoreCase(v.getNombre()));
        assertTrue(existe, "Debe existir el vuelo UY999 en la ruta");
      }

      // -------------
      // listarVuelos
      // -------------

      @Test
      void listarVuelos_error_noAirline() {
        assertThrows(IllegalArgumentException.class, () ->
            sistema.listarVuelos("noexiste", "Montevideo - Buenos Aires"));
      }

      @Test
      void listarVuelos_error_noRuta() {
        assertThrows(IllegalArgumentException.class, () ->
            sistema.listarVuelos("jetair", "Ruta Fantasma"));
      }

      @Test
      void listarVuelos_ok_vacio_sinVuelos() {
        var vuelos = sistema.listarVuelos("jetair", "Montevideo - Buenos Aires");
        assertNotNull(vuelos);
        assertTrue(vuelos.isEmpty(), "Si no hay vuelos, debe devolver lista vacía");
      }

      @Test
      void listarVuelos_ok_conVuelos() {
        VueloEspecifico v = new VueloEspecifico();
        v.setNombre("UY123");
        ruta.getVuelosEspecificos().add(v);

        var vuelos = sistema.listarVuelos("jetair", "Montevideo - Buenos Aires");
        assertNotNull(vuelos);
        assertFalse(vuelos.isEmpty(), "Debe listar los vuelos existentes");
        assertTrue(vuelos.stream().anyMatch(d -> "UY123".equalsIgnoreCase(d.getNombre())));
      }

      // ------------
      // buscarVuelo
      // ------------

      @Test
      void buscarVuelo_error_noAirline() {
        assertThrows(IllegalArgumentException.class, () ->
            sistema.buscarVuelo("noexiste", "Montevideo - Buenos Aires", "UY123"));
      }
      

	@Test
	void buscarVuelo_error_idRutaNull() {
	  rutaService.clear(); 
	  var ex = assertThrows(IllegalArgumentException.class, () ->
	      sistema.buscarVuelo("jetair", "Montevideo - Buenos Aires", "UY123"));
	  assertTrue(ex.getMessage().toLowerCase().contains("base de datos"),
	      "Debe indicar que no existe una ruta con ese nombre en la base de datos");
	}
	
	@Test
	void buscarVuelo_error_rutaNull() {
	  rutaService.clear();
	  rutaService.mapNombreToId.put("Montevideo - Buenos Aires", 777);
	  var ex = assertThrows(IllegalArgumentException.class, () ->
	      sistema.buscarVuelo("jetair", "Montevideo - Buenos Aires", "UY123"));
	  assertTrue(ex.getMessage().contains("id: 777"),
	      "El mensaje debe mencionar el id inexistente");
	}
	
	@Test
	void buscarVuelo_error_vueloInexistente() {
	  var ex = assertThrows(IllegalArgumentException.class, () ->
	      sistema.buscarVuelo("jetair", "Montevideo - Buenos Aires", "ZZ999"));
	  assertTrue(ex.getMessage().toLowerCase().contains("código"),
	      "Debe indicar que no existe un vuelo con ese código en la ruta");
	}
	
	@Test
	void buscarVuelo_ok_encuentraPorCodigo() {
		if (ruta.getOrigen() == null) {
			Ciudad origen = new Ciudad("Montevideo", "Uruguay", "Carrasco", "Intl", new Date(), "https://mvd");
		    Ciudad destino = new Ciudad("Buenos Aires", "Argentina", "AEP", "Aeroparque", new Date(), "https://aep");
		    ruta.setOrigen(origen);
		    ruta.setDestino(destino);
		    ruta.setHora(10);
		    ruta.setFechaAlta(new Date());
		    ruta.setCostoBase(java.math.BigDecimal.ZERO);
		    ruta.setCostoEjecutivo(java.math.BigDecimal.ZERO);
		    ruta.setCostoEquipajeExtra(0);
		}
	    if (ruta.getCategoriaR() == null) ruta.setCategoriaR(new Categoria("Económica"));
	  
	  VueloEspecifico v = new VueloEspecifico();
	  v.setNombre("UY123");
	  v.setRuta(ruta);
	  ruta.getVuelosEspecificos().add(v);
	
	  DataVueloEspecifico dto = sistema.buscarVuelo("jetair", "Montevideo - Buenos Aires", "uy123");
	  assertNotNull(dto, "Debe devolver el vuelo");
	  assertEquals("UY123", dto.getNombre(), "Debe respetar el código del vuelo encontrado");
	}

	@Test
	void buscarVuelo_ok_canonical_y_caseInsensitive() {
	  if (ruta.getOrigen() == null) {
	    Ciudad origen = new Ciudad("Montevideo", "Uruguay", "Carrasco", "Intl", new Date(), "https://mvd");
	    Ciudad destino = new Ciudad("Buenos Aires", "Argentina", "AEP", "Aeroparque", new Date(), "https://aep");
	    ruta.setOrigen(origen);
	    ruta.setDestino(destino);
	    ruta.setHora(10);
	    ruta.setFechaAlta(new Date());
	    ruta.setCostoBase(java.math.BigDecimal.ZERO);
	    ruta.setCostoEjecutivo(java.math.BigDecimal.ZERO);
	    ruta.setCostoEquipajeExtra(0);
	    if (ruta.getCategoriaR() == null) ruta.setCategoriaR(new Categoria("Económica"));
	  }

	  VueloEspecifico v = new VueloEspecifico();
	  v.setNombre("UY123");
	  v.setRuta(ruta);
	  ruta.getVuelosEspecificos().add(v);

	  DataVueloEspecifico dto = sistema.buscarVuelo("   JETAir   ", "Montevideo - Buenos Aires", "uy123");

	  assertNotNull(dto);
	  assertEquals("UY123", dto.getNombre(), "Debe ignorar mayúsculas/minúsculas en el código");
	}

	@Test
	void listarVuelos_ok_siServiceDevuelveNull_entregaListaVacia() {
	  RutaVueloServiceFake rutaSvcLocal = new RutaVueloServiceFake();

	  Aerolinea aero = new Aerolinea();
	  aero.setNickname("jetair");
	  aero.setNombre("Jet Air S.A.");

	  Ruta r = new Ruta();
	  r.setNombre("Montevideo - Buenos Aires");
	  if (r.getVuelosEspecificos() == null) r.setVuelosEspecificos(new HashSet<>());
	  aero.addRuta(r);
	  rutaSvcLocal.persistRuta(r);

	  class UsuarioServiceFakeNullVuelos extends UsuarioServiceFake {
	    UsuarioServiceFakeNullVuelos(Aerolinea a, Ruta ru) { super(a, ru); }
	    @Override
	    public List<DataVueloEspecifico> listarVuelosPorRuta(String nickname, String nombreRuta) {
	      return null;
	    }
	  }

	  UsuarioServiceFake userLocal = new UsuarioServiceFakeNullVuelos(aero, r);

	  Sistema sis = new Sistema(userLocal, null, null, null, null, null, rutaSvcLocal);

	  var vuelos = sis.listarVuelos("jetair", "Montevideo - Buenos Aires");
	  assertNotNull(vuelos);
	  assertTrue(vuelos.isEmpty(), "Si el service devuelve null, debe retornar lista vacía");
	}

	



      // --- Helpers -------------------------------------------------------------

      private DataVueloEspecifico dataVuelo(String codigo) {
        DataVueloEspecifico d = new DataVueloEspecifico(codigo, null, 0, 0, 0, null);
        return d;
      }

 	 // Fakes 
      
      static class RutaVueloServiceFake extends BD.RutaVueloService {
    	private int seq = 1;
        final Map<String,Integer> mapNombreToId = new HashMap<>();
        final Map<Integer,Ruta>   mapIdToRuta   = new HashMap<>();

        void persistRuta(Ruta r) {
          int id = seq++;
          mapNombreToId.put(r.getNombre(), id);
          mapIdToRuta.put(id, r);
        }
        void clear() {
          mapNombreToId.clear();
          mapIdToRuta.clear();
        }

        @Override
        public Integer buscarRutaPorNombreYObtenerId(String nombre) {
          return mapNombreToId.get(nombre);
        }

        @Override
        public Ruta buscarRutaPorId(Integer id) {
          return mapIdToRuta.get(id);
        }

      }
      
      class UsuarioServiceFake extends BD.UsuarioService {
    	  private final Aerolinea aeroRef;
    	  private final Ruta rutaRef;
      	  private final Map<String, Aerolinea> aerolineas = new HashMap<>();

    	  UsuarioServiceFake(Aerolinea a, Ruta r) {
    	    this.aeroRef = a;
    	    this.rutaRef = r;
    	  }

    	  @Override
    	  public DataUsuario verInfoUsuario(String nickname) {
    	    // sólo si coincide con la aerolínea del escenario
    	    if (aeroRef != null && nickname != null &&
    	        nickname.equalsIgnoreCase(aeroRef.getNickname())) {
    	      return new DataAerolinea(
    	          aeroRef.getNombre(),
    	          aeroRef.getNickname(),
    	          aeroRef.getEmail(),          // poné algo si lo necesitás
    	          null,                        // contraseña no usada
    	          aeroRef.getDescGeneral(),
    	          aeroRef.getLinkWeb()
    	      );
    	    }
    	    return null;
    	  }

    	  @Override
    	  public List<DataRuta> listarRutasPorAerolinea(String nickname) {
    	    if (aeroRef == null || rutaRef == null) return List.of();
    	    if (!nickname.equalsIgnoreCase(aeroRef.getNickname())) return List.of();

    	    DataRuta dr = new DataRuta(
    	        rutaRef.getNombre(),
    	        rutaRef.getDescripcion(),
    	        null, // DataCiudad origen 
    	        null, // DataCiudad destino
    	        rutaRef.getHora(),
    	        rutaRef.getFechaAlta(),
    	        rutaRef.getCostoTurista(),
    	        rutaRef.getCostoEquipajeExtra(),
    	        rutaRef.getCostoEjecutivo(),
    	        (rutaRef.getCategoriaR() != null ? new DataCategoria(rutaRef.getCategoriaR().getNombre()) : null),
    	        aeroRef.getNickname(),
    	        rutaRef.getEstado(),
    	        rutaRef.getDescripcionCorta()
    	    );
    	    dr.setIdRuta(1);
    	    return List.of(dr);
    	  }

    	  @Override
    	  public List<DataVueloEspecifico> listarVuelosPorRuta(String nickname, String nombreRuta) {
    	    if (aeroRef == null || rutaRef == null) return List.of();
    	    if (!nickname.equalsIgnoreCase(aeroRef.getNickname())) return List.of();
    	    if (nombreRuta == null || !nombreRuta.equalsIgnoreCase(rutaRef.getNombre())) return List.of();

    	    return rutaRef.getVuelosEspecificos().stream()
    	        .map(v -> new DataVueloEspecifico(
    	            v.getNombre(),
    	            v.getFecha(),
    	            v.getDuracion(),
    	            v.getMaxAsientosTur(),
    	            v.getMaxAsientosEjec(),
    	            v.getFechaAlta(),
    	            null // DataRuta 
    	        ))
    	        .toList();
    	  }

    	  @Override
    	  public Aerolinea obtenerAerolineaPorNickname(String nick) {
    	    if (aeroRef != null && nick != null && nick.equalsIgnoreCase(aeroRef.getNickname())) {
    	      return aeroRef; // ENTIDAD en memoria (para buscarVuelo / reservas)
    	    }
    	    return null;
    	  }
    	  
    	  void agregarAerolinea(Aerolinea a) {
    	      aerolineas.put(a.getNickname().toLowerCase(Locale.ROOT), a);
    	    }
    	}
      
      static class VueloServiceFake extends BD.VueloService{
    	  @Override
    	  public void registrarVuelo(VueloEspecifico vuelo) {
    	    if (vuelo != null && vuelo.getRuta() != null) {
    	      vuelo.getRuta().getVuelosEspecificos().add(vuelo);
    	    }
    	  }  
      }
      
      // Fakes externos que ya venimos usando
      static class PaqueteServiceFake   extends BD.PaqueteService {}
      static class ClienteServiceFake   extends BD.ClienteService {}
      static class CategoriaServiceFake  extends BD.CategoriaService{}
      static class CiudadServiceFake    extends BD.CiudadService{}  
      static class ReservaServiceFake extends BD.ReservaService{}
		
}
      