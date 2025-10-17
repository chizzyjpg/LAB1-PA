package Logica;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.*;

public class sistemaTest_LoginYPassword {

  private UsuarioServiceFake usuarioService;
  private Sistema sistema;

  @BeforeEach
  void setup() {
    usuarioService = new UsuarioServiceFake();

    // 👉 Usar un CLIENTE real (no Usuario) porque DataCliente necesita esos campos
    Cliente cli = new Cliente(
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
    usuarioService.agregarCliente(cli);

    sistema = new Sistema(
        usuarioService,
        new CategoriaServiceFake(),
        new CiudadServiceFake(),
        new PaqueteServiceFake(),
        new ClienteServiceFake(usuarioService),
        new ReservaServiceFake()
    );
  }

  @Test
  void loguearUsuario_ok_conNicknameYPasswordCorrectos() {
    DataUsuario du = sistema.loguearUsuario("luko", "1234");
    assertNotNull(du);
    assertEquals("jet", du.getNickname());
  }

  @Test
  void loguearUsuario_null_cuandoNickInexistente() {
    assertNull(sistema.loguearUsuario("nadie", "1234"));
  }

  @Test
  void cambiarPassword_ok_actualiza() {
    sistema.cambiarPassword("luko", "1234", "nueva");
    DataUsuario du = sistema.loguearUsuario("luko", "nueva");
    assertNotNull(du);
  }
  
  @Test
  void cambiarPassword_no_actualiza_PassIncorrecta() {
	  assertThrows(IllegalArgumentException.class,
	  () -> sistema.cambiarPassword("luko", "marce", "crisrey"));
  }
  
  @Test
  void cambiarPassword_sinPass() {
    assertThrows(IllegalArgumentException.class, ()-> sistema.cambiarPassword("luko", null, null));
    assertThrows(IllegalArgumentException.class, ()-> sistema.cambiarPassword("luko", null, "pepe"));
    assertThrows(IllegalArgumentException.class, ()-> sistema.cambiarPassword("luko", "pepe", null)); 
  }

  @Test
  void cambiarPassword_lanza_siDatosNulos() {
    assertThrows(IllegalArgumentException.class,
        () -> sistema.cambiarPassword(null, "1234", "nueva"));
  }
  
  @Test void loguear_lanza_siNickOPassNulos() {
	  assertThrows(IllegalArgumentException.class, () -> sistema.loguearUsuario(null, "x"));
	  assertThrows(IllegalArgumentException.class, () -> sistema.loguearUsuario("jet", null));
	  
  }
  @Test void loguear_retornaNull_siCredencialesMal() {
	  assertNull(sistema.loguearUsuario("noexiste", "mala"));
  }
  @Test void loguear_ok_devuelveDataUsuario() {
	  assertNotNull(sistema.loguearUsuario("jet", "1234"));
  }
  @Test void cambiarPassword_lanza_siUsuarioNoExiste() {
	  assertThrows(IllegalArgumentException.class, () -> sistema.cambiarPassword("noexiste", "x", "y"));
	  
  }
  @Test void cambiarPassword_lanza_siPassMenorATres() {
	  assertThrows(IllegalArgumentException.class, () -> sistema.cambiarPassword("luko", "1234", "ab"));
  }
  
  @Test void obtenerAvatarUsuarioSinAvatar() {
	  assertNull(sistema.obtenerAvatar("luko"));
  }
  @Test void obtenerAvatarUsuarioConAvatar() {
	  Cliente c = usuarioService.obtenerClientePorNickname("luko");
	  byte[] avatar = new byte[] {1,2,3};
	  c.setAvatar(avatar);
	  usuarioService.actualizarUsuario(c);
	  assertArrayEquals(avatar, sistema.obtenerAvatar("luko"));
  }
  
  @Test void obtenerAvatarSinUsuarioExistente() {
	  assertNull(sistema.obtenerAvatar("noexiste"));
  }
  
  @Test void obtenerAvatarSinUsuario() {
	  assertNull(sistema.obtenerAvatar(""));
  }
  @Test void obtenerAvatarSinUsuarioNull() {
	  assertNull(sistema.obtenerAvatar(null));
  }
  

  // ======= FAKES SOLO CON LO NECESARIO =======

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
    public void actualizarUsuario(Usuario u) {
      String k = u.getNickname().toLowerCase(Locale.ROOT);
      if (u instanceof Cliente c) clientes.put(k, c);
      else if (u instanceof Aerolinea a) aerolineas.put(k, a);
    }

    @Override
    public DataUsuario verInfoUsuario(String nick) {
      if (nick == null) return null;
      String k = nick.toLowerCase(Locale.ROOT);
      Cliente c = clientes.get(k);
      if (c != null) {
        // 👉 Tu DataCliente no tiene setters ni ctor vacío: usar ctor completo
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
        // Si querés soportar aerolínea acá, construí tu DataAerolinea con su ctor real.
        // Para este test no es necesario.
        return null;
      }
      return null;
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

  /** Fake mínimo de ClienteService que devuelve un DataCliente válido. */
  static class ClienteServiceFake extends BD.ClienteService {
    private final UsuarioServiceFake usuarios;
    ClienteServiceFake(UsuarioServiceFake u) { this.usuarios = u; }

    @Override
    public java.util.List<DataCliente> listarClientes() {
      Cliente c = usuarios.obtenerClientePorNickname("jet");
      if (c == null) return List.of();
      return List.of(new DataCliente(
          c.getNombre(),
          c.getNickname(),
          c.getEmail(),
          c.getContrasenia(),
          c.getApellido(),
          c.getFechaNac(),
          c.getNacionalidad(),
          c.getTipoDocumento(),
          c.getNumDocumento()
      ));
    }
  }
}
