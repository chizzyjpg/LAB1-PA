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
        "Lourdes", "lourdes", "l@ex.com", "abc",
        "Hernández", new Date(), "Uruguay",
        TipoDocumento.CEDULA, "5.555.555-5"
    ));

    paquetes = new PaqueteServiceFake();
    TipoAsiento tipo = TipoAsiento.EJECUTIVO;
    paquetes.agregar(new Paquete("Escapada","Desc", 2, tipo, 10, 20, new BigDecimal("15000.00")));

    sistema = new Sistema(
        usuarios, new CategoriaServiceFake(), new CiudadServiceFake(),
        paquetes, new ClienteServiceFake(usuarios), new ReservaServiceFake()
    );
  }

  @Test
  void comprarPaquete_ok_descuentaCupo() {
    DataCompraPaquete dto = nuevaCompra("lourdes", "Escapada", new Date());
    sistema.comprarPaquete(dto);
    usuarios.marcarCompra("lourdes", "Escapada"); // simulamos persistencia para el fake
    assertEquals(9, paquetes.get("Escapada").getCuposDisponibles());
  }

  @Test
  void comprarPaquete_lanza_siClienteYaCompro() {
    DataCompraPaquete dto = nuevaCompra("lourdes", "Escapada", new Date());
    sistema.comprarPaquete(dto);
    usuarios.marcarCompra("lourdes", "Escapada");

    DataCompraPaquete dto2 = nuevaCompra("lourdes", "Escapada", new Date());
    assertThrows(IllegalArgumentException.class, () -> sistema.comprarPaquete(dto2));
  }

  @Test
  void comprarPaquete_lanza_siSinCupos() {
    paquetes.get("Escapada").setCuposDisponibles(0);
    DataCompraPaquete dto = nuevaCompra("lourdes", "Escapada", new Date());
    assertThrows(IllegalStateException.class, () -> sistema.comprarPaquete(dto));
  }

  // ===== helper corregido para TU constructor real =====
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
    void agregar(Paquete p) { porNombre.put(p.getNombre(), p); }
    Paquete get(String n) { return porNombre.get(n); }

    @Override public Paquete existePaquete(String nombre) { return porNombre.get(nombre); }
    @Override public void actualizarPaquete(Paquete p) { porNombre.put(p.getNombre(), p); }
    @Override public List<Paquete> listarPaquetes() { return new ArrayList<>(porNombre.values()); }
  }

  static class CategoriaServiceFake extends BD.CategoriaService {}
  static class CiudadServiceFake   extends BD.CiudadService   {}
  static class ReservaServiceFake  extends BD.ReservaService  {}

  static class ClienteServiceFake extends BD.ClienteService {
    private final UsuarioServiceFake usuarios;
    ClienteServiceFake(UsuarioServiceFake u) { this.usuarios = u; }
    @Override public List<DataCliente> listarClientes() {
      Cliente c = usuarios.obtenerClientePorNickname("lourdes");
      if (c == null) return List.of();
      return List.of(new DataCliente(
          c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(),
          c.getApellido(), c.getFechaNac(), c.getNacionalidad(),
          c.getTipoDocumento(), c.getNumDocumento()
      ));
    }
  }
}
