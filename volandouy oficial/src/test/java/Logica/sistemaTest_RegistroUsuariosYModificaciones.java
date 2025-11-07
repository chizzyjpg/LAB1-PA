package Logica;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;



public class sistemaTest_RegistroUsuariosYModificaciones {
	
	private Sistema sistema;
	private UsuarioServiceFake usuarios;
	//private ClienteServiceFake cFake;
	
	
	@BeforeEach
	  void setup() {
	    usuarios = new UsuarioServiceFake();
	    // ManejadorUsuario.setServiceForTests(usuarios); -> Se divide en llamada a AerolineaSercive y ClienteService
	    
	 // Usar un CLIENTE real (no Usuario) porque DataCliente necesita esos campos
	    Cliente cliente = new Cliente(
	        "Lucas",           // nombre
	        "luko",                 // nickname
	        "crisrey@ex.com",          // email
	        "1234",                // contra (plana)
	        "Marcenal",            // apellido
	        new Date(),            // fechaNac (dummy)
	        "Uruguay",             // nacionalidad
	        TipoDocumento.CEDULA,  // enum tuyo
	        "5.555.555-5"          // numDocumento
	    );
	    usuarios.agregarCliente(cliente);
	    
	    Aerolinea aerolinea = new Aerolinea(
	        "Jet Airways",    // nombre
	        "jet",                 // nickname
	        "jet@gmail.com",
	        "abcd",               // contra (plana)
	        "La mejor aerolínea", // descripcion
	        "www.jetairways.com"  // sitioWeb
	        );
	    usuarios.agregarAerolinea(aerolinea);
	    
	    var clienteServiceFake    = new ClienteServiceFake(usuarios);
	    var aerolineaServiceFake  = new AerolineaServiceFake();
	    
	    ManejadorUsuario.setClienteServiceForTests(clienteServiceFake);
	    ManejadorUsuario.setAerolineaServiceForTests(aerolineaServiceFake);
	    
	    sistema = new Sistema(
	        usuarios,
	        new CategoriaServiceFake(),
	        new CiudadServiceFake(),
	        new PaqueteServiceFake(),
	        clienteServiceFake,
	        new ReservaServiceFake(),
	        new RutaVueloServiceFake()
	    );

	}
	
	
	// -------- registrarUsuario --------
	  @Test
	  void registrarUsuario_dataNull_lanzaIAE() {
	    assertThrows(IllegalArgumentException.class, () -> sistema.registrarUsuario(null));
	  }

	  @Test
	  void registrarUsuario_nicknameDuplicado_lanzaIAE() {

	    DataUsuario nuevo = new DataCliente(
	        "Otro", "luko", "luko@mail.com", "pass",
	        "Apellido", new Date(), "UY",
	        TipoDocumento.CEDULA, "2.222.222-2"
	    );
	    var ex = assertThrows(IllegalArgumentException.class, () -> sistema.registrarUsuario(nuevo));
	    assertTrue(ex.getMessage().toLowerCase().contains("nickname"));
	  }

	  @Test
	  void registrarUsuario_emailDuplicado_lanzaIAE() {

	    DataUsuario nuevo = new DataCliente(
	        "Beta", "beta", "crisrey@ex.com", "pass",
	        "Ap", new Date(), "UY",
	        TipoDocumento.CEDULA, "3.333.333-3"
	    );
	    var ex = assertThrows(IllegalArgumentException.class, () -> sistema.registrarUsuario(nuevo));
	    assertTrue(ex.getMessage().toLowerCase().contains("email"));
	  }

	  @Test
	  void registrarUsuario_caminoOk() {
	    DataUsuario nuevo = new DataCliente(
	        "Nova", "nova", "nova@mail.com", "pass",
	        "Ap", new Date(), "UY",
	        TipoDocumento.CEDULA, "4.444.444-4"
	    );
	    assertDoesNotThrow(() -> sistema.registrarUsuario(nuevo));
	  }

	  // -------- verInfoCliente --------
	  @Test
	  void verInfoCliente_devuelveCliente_cuandoExisteYEsCliente() {
	    DataCliente dc = sistema.verInfoCliente("luko");
	    assertNotNull(dc);
	    assertEquals("luko", dc.getNickname());
	  }

	  @Test
	  void verInfoCliente_null_cuandoEsAerolinea() {
	    assertNull(sistema.verInfoCliente("jet"));
	  }

	  @Test
	  void verInfoCliente_null_cuandoNoExiste() {
	    assertNull(sistema.verInfoCliente("nadie"));
	  }

	  // -------- verInfoAerolinea --------
	  @Test
	  void verInfoAerolinea_devuelveAerolinea_cuandoExisteYEsAerolinea() {
	    Aerolinea aerolinea = new Aerolinea ("AA","aa", "aa@mail.com", "AA", "Desc AA", "www.aa.com");
	    usuarios.agregarAerolinea(aerolinea);
	    DataAerolinea da = sistema.verInfoAerolinea("aa");
	    assertNotNull(da);
	    assertEquals("aa", da.getNickname());
	  }

	  @Test
	  void verInfoAerolinea_null_cuandoEsCliente() {
	    assertNull(sistema.verInfoAerolinea("luko"));
	  }

	  @Test
	  void verInfoAerolinea_null_cuandoNoExiste() {
	    assertNull(sistema.verInfoAerolinea("nadie"));
	  }

	  // -------- listarUsuarios (orden case-insensitive) --------
	  @Test
	  void listarUsuarios_ordenCaseInsensitive() {
		Cliente roberto = new Cliente("Roberto", "robert", "rob@mail.com", "pass", "Gomez", new Date(), "UY", TipoDocumento.CEDULA, "6.666.666-6");
		usuarios.agregarCliente(roberto);
		Cliente ana = new Cliente("Ana", "ana", "ana@mail.com", "pass", "Lopez", new Date(), "UY", TipoDocumento.CEDULA, "7.777.777-7");
		usuarios.agregarCliente(ana);
		Aerolinea latam = new Aerolinea("Latam", "latam", "lat@mail.com", "pass", "Desc Latam", "www.latam.com");
		usuarios.agregarAerolinea(latam);

	    List<DataUsuario> out = sistema.listarUsuarios();
	    assertEquals(3, out.size());
	    assertEquals("Alpha", out.get(0).getNickname()); // A...
	    assertEquals("beta",  out.get(1).getNickname()); // b...
	    assertEquals("delta", out.get(2).getNickname()); // d...
	  }

	  // -------- listarAerolineas (filtro + orden) --------
	  @Test
	  void listarAerolineas_filtraYAcomoda() {
	  	Aerolinea sky = new Aerolinea("Sky", "sky", "sky@mail.com", "pass", "Desc sky", "www.sky.com");
	    usuarios.agregarAerolinea(sky);
		Cliente ana = new Cliente("Ana", "ana", "ana@mail.com", "pass", "Lopez", new Date(), "UY", TipoDocumento.CEDULA, "7.777.777-7");
		usuarios.agregarCliente(ana);
		Aerolinea latam = new Aerolinea("Latam", "latam", "lat@mail.com", "pass", "Desc Latam", "www.latam.com");
		usuarios.agregarAerolinea(latam);
		
	    List<DataAerolinea> out = sistema.listarAerolineas();
	    assertEquals(2, out.size());
	    assertEquals("sky", out.get(0).getNickname());
	    assertEquals("latam",  out.get(1).getNickname());
	  }

	  // -------- listarClientes (orden) --------
	  @Test
	  void listarClientes_ordenCaseInsensitive() {
	  	
	  	Cliente roberto = new Cliente("Roberto", "robert", "rob@mail.com", "pass", "Gomez", new Date(), "UY", TipoDocumento.CEDULA, "6.666.666-6");
		usuarios.agregarCliente(roberto);
		Cliente ana = new Cliente("Ana", "ana", "ana@mail.com", "pass", "Lopez", new Date(), "UY", TipoDocumento.CEDULA, "7.777.777-7");
		usuarios.agregarCliente(ana);
		Cliente carlos = new Cliente("Carlos", "carlos", "carl@gmail.com", "pass", "Perez", new Date(), "UY", TipoDocumento.CEDULA, "8.888.888-8");
		usuarios.agregarCliente(carlos);

	    List<DataCliente> out = sistema.listarClientes();
	    assertEquals(3, out.size());
	    assertEquals("robert", out.get(0).getNickname());
	    assertEquals("ana",  out.get(1).getNickname());
	    assertEquals("carlos", out.get(2).getNickname());
	  }
	  
	// ====================== MODIFICAR CLIENTE ======================

	  @Test
	  void modificarCliente_datosNull_lanzaIAE() {
	      assertThrows(IllegalArgumentException.class, () -> sistema.modificarCliente("luko", null));
	  }

	  @Test
	  void modificarCliente_clienteNoExiste_lanzaIAE() {
	      DataCliente nuevos = new DataCliente("X","luko","x@x.com","p","Ap", new Date(),"UY", TipoDocumento.CEDULA, "1");
	      assertThrows(IllegalArgumentException.class, () -> sistema.modificarCliente("noexiste", nuevos));
	  }

	  @Test
	  void modificarCliente_bloqueaCambioEmail() {
	      DataCliente nuevos = new DataCliente(
	          "LucasNuevo", "luko", "otro@mail.com", "1234",
	          "MarcenalNuevo", new Date(), "AR", TipoDocumento.PASAPORTE, "A-999"
	      );
	      var ex = assertThrows(IllegalArgumentException.class, () -> sistema.modificarCliente("luko", nuevos));
	      assertTrue(ex.getMessage().toLowerCase().contains("correo"));
	  }

	  @Test
	  void modificarCliente_bloqueaCambioNickname() {
	      DataCliente nuevos = new DataCliente(
	          "LucasNuevo", "otroNick", "crisrey@ex.com", "1234",
	          "MarcenalNuevo", new Date(), "AR", TipoDocumento.PASAPORTE, "A-999"
	      );
	      var ex = assertThrows(IllegalArgumentException.class, () -> sistema.modificarCliente("luko", nuevos));
	      assertTrue(ex.getMessage().toLowerCase().contains("nickname"));
	  }

	  @Test
	  void modificarCliente_actualizaCamposPermitidos_yPersiste() {
	      Date nuevaFecha = new Date(System.currentTimeMillis() - 86400000L);
	      DataCliente nuevos = new DataCliente(
	          "LucasNuevo", "luko", "crisrey@ex.com", "1234",
	          "MarcenalNuevo", nuevaFecha, "AR", TipoDocumento.PASAPORTE, "A-999"
	      );

	      assertDoesNotThrow(() -> sistema.modificarCliente("LuKo", nuevos)); // case-insensitive

	      Cliente actualizado = usuarios.obtenerClientePorNickname("luko");
	      assertNotNull(actualizado);
	      assertEquals("LucasNuevo", actualizado.getNombre());
	      assertEquals("MarcenalNuevo", actualizado.getApellido());
	      assertEquals("AR", actualizado.getNacionalidad());
	      assertEquals(TipoDocumento.PASAPORTE, actualizado.getTipoDocumento());
	      assertEquals("A-999", actualizado.getNumDocumento());
	      // Nick y email intactos
	      assertEquals("luko", actualizado.getNickname());
	      assertEquals("crisrey@ex.com", actualizado.getEmail());
	  }
	  
	  @Test
	  void modificarCliente_emailNuevoNull() {
	      DataCliente nuevos = new DataCliente(
	          "LucasNuevo", "luko", null, "1234",
	          "Ap", new Date(), "UY", TipoDocumento.CEDULA, "1"
	      );
	      assertDoesNotThrow(() -> sistema.modificarCliente("luko", nuevos));
	  }

	  @Test
	  void modificarCliente_emailIgual() {
	      DataCliente nuevos = new DataCliente(
	          "LucasNuevo", "luko", "crisrey@ex.com", "1234",
	          "Ap", new Date(), "UY", TipoDocumento.CEDULA, "1"
	      );
	      assertDoesNotThrow(() -> sistema.modificarCliente("luko", nuevos));
	  }
	  
	  @Test
	  void modificarCliente_nicknameNull() {
	      DataCliente nuevos = new DataCliente(
	          "LucasNuevo", null, "crisrey@ex.com", "1234",
	          "Ap", new Date(), "UY", TipoDocumento.CEDULA, "1"
	      );
	      assertDoesNotThrow(() -> sistema.modificarCliente("luko", nuevos));
	  }
	  
	  // ====================== ACTUALIZAR PERFIL CLIENTE ======================

	  @Test
	  void actualizarPerfilCliente_null_lanzaIAE() {
	      assertThrows(IllegalArgumentException.class, () -> sistema.actualizarPerfilCliente(null));
	  }

	  @Test
	  void actualizarPerfilCliente_inexistente_lanzaIAE() {
	      PerfilClienteUpdate upd = new PerfilClienteUpdate(
	          "noexiste", "n@x.com", "X", "Ap", "UY", TipoDocumento.CEDULA, "1", new Date(), null, false
	      );
	      assertThrows(IllegalArgumentException.class, () -> sistema.actualizarPerfilCliente(upd));
	  }

	  @Test
	  void actualizarPerfilCliente_bloqueaCambioEmail() {
	      PerfilClienteUpdate upd = new PerfilClienteUpdate(
	          "luko", "otro@mail.com", "LucasNuevo", "Ap", "AR", TipoDocumento.PASAPORTE, "P-1", new Date(), null, false
	      );
	      var ex = assertThrows(IllegalArgumentException.class, () -> sistema.actualizarPerfilCliente(upd));
	      assertTrue(ex.getMessage().toLowerCase().contains("correo"));
	  }

	  @Test
	  void actualizarPerfilCliente_actualizaCamposPermitidos_yAvatarSetYCLEAR() {
	      // SET avatar + campos
	      byte[] avatar = new byte[]{1,2,3};
	      Date fnac = new Date(System.currentTimeMillis() - 1234567L);
	      PerfilClienteUpdate upd = new PerfilClienteUpdate(
	          "luko", "crisrey@ex.com", "Luko Nuevo", "Ap Nuevo", "BR",
	          TipoDocumento.CEDULA, "9.999.999-9", fnac, avatar, false
	      );

	      DataCliente out = sistema.actualizarPerfilCliente(upd);
	      assertNotNull(out);
	      assertEquals("luko", out.getNickname());
	      assertEquals("Luko Nuevo", out.getNombre());
	      assertEquals("Ap Nuevo", out.getApellido());
	      assertEquals("BR", out.getNacionalidad());

	      Cliente ent = usuarios.obtenerClientePorNickname("luko");
	      assertArrayEquals(avatar, ent.getAvatar());

	      // CLEAR avatar (tu método no usa clearAvatar; limpiamos pasando avatar = null)
	      PerfilClienteUpdate updClear = new PerfilClienteUpdate(
	          "luko", "crisrey@ex.com", "Lucas", "Marcenal", "Uruguay",
	          TipoDocumento.CEDULA, "5.555.555-5", new Date(), null, true
	      );
	      DataCliente out2 = sistema.actualizarPerfilCliente(updClear);
	      assertNotNull(out2);
	      assertNull(usuarios.obtenerClientePorNickname("luko").getAvatar());
	  }
	  
	  @Test
	  void actualizarPerfilCliente_emailNull() {
	      PerfilClienteUpdate upd = new PerfilClienteUpdate(
	          "luko", null, "Lucas", "Ap", "UY",
	          TipoDocumento.CEDULA, "1", new Date(), null, false
	      );
	      assertDoesNotThrow(() -> sistema.actualizarPerfilCliente(upd));
	  }

	  @Test
	  void actualizarPerfilCliente_emailIgual() {
	      PerfilClienteUpdate upd = new PerfilClienteUpdate(
	          "luko", "crisrey@ex.com", "Lucas", "Ap", "UY",
	          TipoDocumento.CEDULA, "1", new Date(), null, false
	      );
	      assertDoesNotThrow(() -> sistema.actualizarPerfilCliente(upd));
	  }
	  
	  @Test
	  void actualizarPerfilCliente_avatarNullYNoClear_mantieneAvatar() {
	      // poner un avatar previo
	      Cliente c = usuarios.obtenerClientePorNickname("luko");
	      c.setAvatar(new byte[]{9,9,9});

	      PerfilClienteUpdate upd = new PerfilClienteUpdate(
	          "luko", "crisrey@ex.com", "Lucas", "Ap", "UY",
	          TipoDocumento.CEDULA, "1", new Date(), null, false // avatar null, no clear
	      );
	      sistema.actualizarPerfilCliente(upd);

	      assertArrayEquals(new byte[]{9,9,9}, usuarios.obtenerClientePorNickname("luko").getAvatar());
	  }
	  
	  

	  // ====================== MODIFICAR AEROLINEA ======================
	  @Test
	  void modificarAerolinea_datosNull_lanzaIAE() {
	      assertThrows(IllegalArgumentException.class, () -> sistema.modificarAerolinea("jet", null));
	  }

	  @Test
	  void modificarAerolinea_inexistente_lanzaIAE() {
	      DataAerolinea nuevos = new DataAerolinea("N","noexiste","n@x.com","p","d","w");
	      assertThrows(IllegalArgumentException.class, () -> sistema.modificarAerolinea("noexiste", nuevos));
	  }

	  @Test
	  void modificarAerolinea_bloqueaCambioEmail_yNickname() {
	      DataAerolinea cambiaMail = new DataAerolinea("Jet","jet","otro@mail.com","abcd","Desc","www.jet.com");
	      var ex1 = assertThrows(IllegalArgumentException.class, () -> sistema.modificarAerolinea("jet", cambiaMail));
	      assertTrue(ex1.getMessage().toLowerCase().contains("correo"));

	      DataAerolinea cambiaNick = new DataAerolinea("Jet","otroNick","jet@gmail.com","abcd","Desc","www.jet.com");
	      var ex2 = assertThrows(IllegalArgumentException.class, () -> sistema.modificarAerolinea("jet", cambiaNick));
	      assertTrue(ex2.getMessage().toLowerCase().contains("nickname"));
	  }

	  @Test
	  void modificarAerolinea_actualizaCamposPermitidos_yPersiste() {
	      DataAerolinea nuevos = new DataAerolinea("Jet Nueva","jet","jet@gmail.com","abcd","Desc nueva","www.nuevo.com");
	      assertDoesNotThrow(() -> sistema.modificarAerolinea("JET", nuevos));

	      Aerolinea a = usuarios.obtenerAerolineaPorNickname("jet");
	      assertNotNull(a);
	      assertEquals("Jet Nueva", a.getNombre());
	      assertEquals("Desc nueva", a.getDescGeneral());
	      assertEquals("www.nuevo.com", a.getLinkWeb());
	      assertEquals("jet", a.getNickname());
	      assertEquals("jet@gmail.com", a.getEmail());
	  }
	  
	  @Test
	  void modificarAerolinea_emailNull() {
	      DataAerolinea nuevos = new DataAerolinea("Jet", "jet", null, "abcd", "Desc", "web");
	      assertDoesNotThrow(() -> sistema.modificarAerolinea("jet", nuevos));
	  }

	  @Test
	  void modificarAerolinea_emailIgual_noLanza() {
	      DataAerolinea nuevos = new DataAerolinea("Jet", "jet", "jet@gmail.com", "abcd", "Desc", "web");
	      assertDoesNotThrow(() -> sistema.modificarAerolinea("jet", nuevos));
	  }
	  
	  @Test
	  void modificarAerolinea_nicknameNull() {
	      DataAerolinea nuevos = new DataAerolinea("Jet", null, "jet@gmail.com", "abcd", "Desc", "web");
	      assertDoesNotThrow(() -> sistema.modificarAerolinea("jet", nuevos));
	  }


	  
	  // ====================== ACTUALIZAR PERFIL AEROLINEA ======================
	  
	  @Test
	  void actualizarPerfilAerolinea_null_lanzaIAE() {
	      assertThrows(IllegalArgumentException.class, () -> sistema.actualizarPerfilAerolinea(null));
	  }

	  @Test
	  void actualizarPerfilAerolinea_inexistente_lanzaIAE() {
	      PerfilAerolineaUpdate upd = new PerfilAerolineaUpdate(
	          "noexiste","n@x.com","X","D","w", null, false
	      );
	      assertThrows(IllegalArgumentException.class, () -> sistema.actualizarPerfilAerolinea(upd));
	  }

	  @Test
	  void actualizarPerfilAerolinea_bloqueaCambioEmail() {
	      PerfilAerolineaUpdate upd = new PerfilAerolineaUpdate(
	          "jet","otro@mail.com","Jet","Desc","www.jet.com", null, false
	      );
	      var ex = assertThrows(IllegalArgumentException.class, () -> sistema.actualizarPerfilAerolinea(upd));
	      assertTrue(ex.getMessage().toLowerCase().contains("correo"));
	  }

	  @Test
	  void actualizarPerfilAerolinea_actualizaCamposPermitidos_setYclearAvatar_yRetornaDTO() {
	      byte[] avatar = new byte[]{7,8};

	      // set avatar
	      PerfilAerolineaUpdate upd = new PerfilAerolineaUpdate(
	          "jet","jet@gmail.com","Jet Super","Desc super","www.super.com",
	          avatar, false
	      );
	      DataAerolinea dto = sistema.actualizarPerfilAerolinea(upd);
	      assertNotNull(dto);
	      assertEquals("jet", dto.getNickname());
	      assertArrayEquals(avatar, usuarios.obtenerAerolineaPorNickname("jet").getAvatar());

	      // clear avatar
	      PerfilAerolineaUpdate updClear = new PerfilAerolineaUpdate(
	          "jet","jet@gmail.com","Jet Super","Desc super","www.super.com",
	          null, true
	      );
	      DataAerolinea dto2 = sistema.actualizarPerfilAerolinea(updClear);
	      assertNotNull(dto2);
	      assertNull(usuarios.obtenerAerolineaPorNickname("jet").getAvatar());
	  }

	  @Test
	  void actualizarPerfilAerolinea_emailNull_noLanza() {
	      PerfilAerolineaUpdate upd = new PerfilAerolineaUpdate(
	          "jet", null, "Jet", "Desc", "web", null, false
	      );
	      assertDoesNotThrow(() -> sistema.actualizarPerfilAerolinea(upd));
	  }

	  @Test
	  void actualizarPerfilAerolinea_emailIgual_noLanza() {
	      PerfilAerolineaUpdate upd = new PerfilAerolineaUpdate(
	          "jet", "jet@gmail.com", "Jet", "Desc", "web", null, false
	      );
	      assertDoesNotThrow(() -> sistema.actualizarPerfilAerolinea(upd));
	  }

	  @Test
	  void actualizarPerfilAerolinea_avatarNullYNoClear_mantieneAvatar() {
	      // avatar previo
	      Aerolinea a = usuarios.obtenerAerolineaPorNickname("jet");
	      a.setAvatar(new byte[]{5,5});

	      PerfilAerolineaUpdate upd = new PerfilAerolineaUpdate(
	          "jet", "jet@gmail.com", "Jet", "Desc", "web", null, false // null y no clear
	      );
	      sistema.actualizarPerfilAerolinea(upd);

	      assertArrayEquals(new byte[]{5,5}, usuarios.obtenerAerolineaPorNickname("jet").getAvatar());
	  }

	  // ====================== Sobre Login y Avatar, Sistema al final ======================

	  @Test
	  void existeNickname_retornaTrueYFalse() {
	      // sembramos un cliente y una aerolínea
	      Cliente c = new Cliente("Ana","ana","ana@mail.com","1234","Ap", new Date(),"UY",TipoDocumento.CEDULA,"1");
	      usuarios.agregarCliente(c);
	      Aerolinea a = new Aerolinea("Jet","jet","jet@mail.com","abcd","Desc","web");
	      usuarios.agregarAerolinea(a);

	      assertTrue(sistema.existeNickname("ana"));
	      assertTrue(sistema.existeNickname("jet"));

	      assertFalse(sistema.existeNickname("nadie"));
	  }

	  @Test
	  void existeEmail_retornaTrueYFalse() {
	      Cliente c = new Cliente("Beto","beto","beto@mail.com","1234","Ap", new Date(),"UY",TipoDocumento.CEDULA,"2");
	      usuarios.agregarCliente(c);

	      assertTrue(sistema.existeEmail("beto@mail.com"));
	      assertFalse(sistema.existeEmail("otro@mail.com"));
	  }

	  @Test
	  void altaCliente_conAvatar_registraYActualizaPerfil_avatarSeteado() {
	      // Datos del nuevo cliente
	      DataCliente data = new DataCliente(
	          "Nova", "nova", "nova@mail.com", "pass",
	          "Apellido", new Date(), "UY", TipoDocumento.CEDULA, "3.333.333-3"
	      );
	      byte[] avatar = new byte[]{1,2,3,4};

	      // Act
	      assertDoesNotThrow(() -> sistema.altaCliente(data, avatar));

	      // Assert: se registró
	      Cliente creado = usuarios.obtenerClientePorNickname("nova");
	      assertNotNull(creado, "Debe quedar registrado el cliente");
	      // y se actualizó perfil (avatar)
	      assertArrayEquals(avatar, creado.getAvatar(), "Debe quedar seteado el avatar");
	  }

	  @Test
	  void altaCliente_avatarNull_registraSinActualizarPerfil_avatarQuedaNull() {
	      DataCliente data = new DataCliente(
	          "Solo", "solo", "solo@mail.com", "p",
	          "Ap", new Date(), "UY", TipoDocumento.CEDULA, "4.444.444-4"
	      );
	      byte[] avatar = null;

	      assertDoesNotThrow(() -> sistema.altaCliente(data, avatar));

	      Cliente creado = usuarios.obtenerClientePorNickname("solo");
	      assertNotNull(creado);
	      assertNull(creado.getAvatar(), "Sin avatar no debe llamar a actualizarPerfil (avatar null)");
	  }

	  @Test
	  void altaCliente_avatarVacio_registraSinActualizarPerfil_avatarQuedaNull() {
	      DataCliente data = new DataCliente(
	          "Vac", "vac", "vac@mail.com", "p",
	          "Ap", new Date(), "UY", TipoDocumento.CEDULA, "5.555.555-5"
	      );
	      byte[] avatar = new byte[0];

	      assertDoesNotThrow(() -> sistema.altaCliente(data, avatar));

	      Cliente creado = usuarios.obtenerClientePorNickname("vac");
	      assertNotNull(creado);
	      assertNull(creado.getAvatar(), "Con avatar.length==0 no debe actualizar perfil");
	  }

	  @Test
	  void altaAerolinea_registraOk() {
	      DataAerolinea d = new DataAerolinea(
	          "Sky High", "sky", "sky@mail.com", "abcd",
	          "Desc sky", "www.sky.com"
	      );
	      assertDoesNotThrow(() -> sistema.altaAerolinea(d));

	      Aerolinea aero = usuarios.obtenerAerolineaPorNickname("sky");
	      assertNotNull(aero, "Debe quedar registrada la aerolínea");
	  }
	  
	
	  			// ====== FAKES ======

	 /** Fake que respeta las firmas reales de UsuarioService. */
	  static class UsuarioServiceFake extends BD.UsuarioService {
	    private final Map<String, Cliente> clientes = new HashMap<>();
	    private final Map<String, Aerolinea> aerolineas = new HashMap<>();

	    void agregarCliente(Cliente c) {
	      clientes.put(c.getNickname().toLowerCase(Locale.ROOT), c);
	    }
	    void agregarAerolinea(Aerolinea a) {
	      aerolineas.put(a.getNickname().toLowerCase(Locale.ROOT), a);
	    }
	    
	    @Override
	    public List<DataUsuario> listarUsuarios() {
	      List<DataUsuario> out = new ArrayList<>();
	      for (Cliente c : clientes.values()) {
	        out.add(new DataCliente(
	            c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(),
	            c.getApellido(), c.getFechaNac(), c.getNacionalidad(),
	            c.getTipoDocumento(), c.getNumDocumento()
	        ));
	      }
	      for (Aerolinea a : aerolineas.values()) {
	          out.add(new DataAerolinea(
	              a.getNombre(), a.getNickname(), a.getEmail(), a.getContrasenia(),
	              a.getDescGeneral(), a.getLinkWeb()
	          ));
	        }
	        return out;
	      }
	      
	    
	    @Override
	    public DataUsuario verInfoUsuario(String nick) {
	      if (nick == null) return null;
	      String k = nick.toLowerCase(Locale.ROOT);

	      Cliente c = clientes.get(k);
	      if (c != null) {
	        return new DataCliente(
	            c.getNombre(),
	            c.getNickname(),
	            c.getEmail(),
	            c.getContrasenia(),
	            c.getApellido(),
	            c.getFechaNac(),
	            c.getNacionalidad(),
	            c.getTipoDocumento(),
	            c.getNumDocumento()
	        );
	      }

	      Aerolinea a = aerolineas.get(k);
	      if (a != null) {
	        return new DataAerolinea(
	            a.getNombre(),
	            a.getNickname(),
	            a.getEmail(),
	            a.getContrasenia(),
	            a.getDescGeneral(),
	            a.getLinkWeb()
	        );
	      }
	      return null;
	    }

	    @Override
	    public boolean existeNickname(String nick) {
	      if (nick == null) return false;
	      String k = nick.toLowerCase(Locale.ROOT);
	      return clientes.containsKey(k) || aerolineas.containsKey(k);
	    }

	    @Override
	    public boolean existeEmail(String email) {
	      if (email == null) return false;
	      String e = email.toLowerCase(Locale.ROOT);
	      return clientes.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(e))
	          || aerolineas.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(e));
	    }

	    @Override
	    public Usuario autenticarPorNicknamePlano(String nick, String pass) {
	      if (nick == null || pass == null) return null;
	      String k = nick.toLowerCase(Locale.ROOT);
	      Cliente c = clientes.get(k);
	      if (c != null && pass.equals(c.getContrasenia())) return c;
	      Aerolinea a = aerolineas.get(k);
	      if (a != null && pass.equals(a.getContrasenia())) return a;
	      return null;
	    }

	    @Override
	    public Usuario autenticarUsuario(String nick, String pass) {
	      return autenticarPorNicknamePlano(nick, pass);
	    }

	    @Override
	    public void actualizarUsuario(Usuario u, boolean photo) {
	      String k = u.getNickname().toLowerCase(Locale.ROOT);
	      if (u instanceof Cliente c) clientes.put(k, c);
	      else if (u instanceof Aerolinea a) aerolineas.put(k, a);
	    }

	    @Override
	    public Cliente obtenerClientePorNickname(String nick) {
	      if (nick == null) return null;
	      return clientes.get(nick.toLowerCase(Locale.ROOT));
	    }

	    @Override
	    public Aerolinea obtenerAerolineaPorNickname(String nick) {
	      if (nick == null) return null;
	      return aerolineas.get(nick.toLowerCase(Locale.ROOT));
	    }
	  }

	  static class CategoriaServiceFake extends BD.CategoriaService {}
	  static class CiudadServiceFake extends BD.CiudadService {}
	  static class PaqueteServiceFake extends BD.PaqueteService {}
	  static class ReservaServiceFake extends BD.ReservaService {}
	  static class RutaVueloServiceFake extends BD.RutaVueloService {}

	  /** Fake mínimo de ClienteService que devuelve un DataCliente válido. */
	  static class ClienteServiceFake extends BD.ClienteService {
	    private final UsuarioServiceFake usuarios;
	    ClienteServiceFake(UsuarioServiceFake u) { this.usuarios = u; }

	    @Override
	    public List<DataCliente> listarClientes() {
	      // Devuelve TODOS los clientes del fake de usuarios (no ordenados)
	      List<DataCliente> out = new ArrayList<>();
	      for (DataUsuario du : usuarios.listarUsuarios()) {
	        if (du instanceof DataCliente dc) out.add(dc);
	      }
	      return out;
	    }
	    
	    @Override
	    public void crearCliente(Cliente c) {
	      if (c == null) throw new IllegalArgumentException("Cliente null");
	      usuarios.agregarCliente(c); // guardamos en memoria
	    }   
	  }
	  
	  static class AerolineaServiceFake extends BD.AerolineaService {
		  final Map<String, Aerolinea> store = new HashMap<>();
		  
		  @Override public void crearAerolinea(Aerolinea a) {
		    store.put(a.getNickname().toLowerCase(Locale.ROOT), a);
		  }
		}
}
	
	
