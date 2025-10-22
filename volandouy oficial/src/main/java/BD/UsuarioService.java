package BD;

import Logica.Aerolinea;
import Logica.Cliente;
import Logica.DataAerolinea;
import Logica.DataCliente;
import Logica.DataRuta;
import Logica.DataUsuario;
import Logica.DataUsuarioAux;
import Logica.DataVueloEspecifico;
import Logica.JPAUtil;
import Logica.ManejadorRuta;
import Logica.Ruta;
import Logica.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar usuarios (Clientes y Aerolíneas) en la base de datos.
 */
public class UsuarioService {

  // ===============================
  // CREAR
  // ===============================
  /**
   * Crea un nuevo cliente en la base de datos.
  public void crearCliente(Cliente c) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      em.persist(c);
      em.getTransaction().commit();
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw ex; // propagar para que la UI decida qué mostrar
    } finally {
      em.close();
    }
  }

  /**
   * Crea una nueva aerolínea en la base de datos.
   */
  public void crearAerolinea(Aerolinea a) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      em.persist(a);
      em.getTransaction().commit();
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw ex; // propagar para que la UI decida qué mostrar
    } finally {
      em.close();
    }
  }

  // ===============================
  // LISTAR
  // ===============================

  /**
   * Lista todos los usuarios (Clientes y Aerolíneas) en la base de datos.
   */
  public List<DataUsuario> listarUsuarios() {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      var usuarios = em
          .createQuery("SELECT u FROM Usuario u ORDER BY LOWER(COALESCE(u.nickname, ''))",
              Usuario.class)
          .getResultList();
      em.getTransaction().commit();

      return usuarios.stream().map(u -> {
        if (u instanceof Aerolinea a) {
          return new DataAerolinea(a.getNombre(), a.getNickname(), a.getEmail(), a.getContrasenia(),
              a.getDescGeneral(), a.getLinkWeb());
        } else if (u instanceof Cliente c) {
          return new DataCliente(c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(),
              c.getApellido(), c.getFechaNac(), c.getNacionalidad(), c.getTipoDocumento(),
              c.getNumDocumento());
        } else {
          return new DataUsuarioAux(u.getNombre(), u.getNickname(), u.getEmail(),
              u.getContrasenia());
        }
      }).collect(Collectors.toList());
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw ex;
    } finally {
      em.close();
    }
  }

  /**
   * Lista las rutas asociadas a una aerolínea específica.
   */
  public List<DataRuta> listarRutasPorAerolinea(String nicknameAerolinea) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      List<Ruta> rutas = em
          .createQuery("SELECT r FROM Ruta r JOIN r.aerolineas a WHERE a.nickname = :nickname",
              Ruta.class)
          .setParameter("nickname", nicknameAerolinea).getResultList();
      em.getTransaction().commit();
      // Usar ManejadorRuta para convertir de entidad a DTO
      return rutas.stream().map(ManejadorRuta::toData).collect(Collectors.toList());
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw ex;
    } finally {
      em.close();
    }
  }

  // ===============================
  // VER INFO
  // ===============================
  /**
   * Obtiene la información detallada de un usuario por su nickname.
   * Devuelve null si el usuario no existe.
   */
  public DataUsuario verInfoUsuario(String nickname) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      List<Usuario> usuarios = em
          .createQuery("from Usuario u where u.nickname = :nickname", Usuario.class)
          .setParameter("nickname", nickname).getResultList();
      em.getTransaction().commit();
      if (usuarios.isEmpty()) {
        return null;
      }
      Usuario u = usuarios.get(0);
      if (u instanceof Cliente c) {
        return new DataCliente(c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(),
            c.getApellido(), c.getFechaNac(), c.getNacionalidad(), c.getTipoDocumento(),
            c.getNumDocumento());
      } else if (u instanceof Aerolinea a) {
        return new DataAerolinea(a.getNombre(), a.getNickname(), a.getEmail(), a.getContrasenia(),
            a.getDescGeneral(), a.getLinkWeb());
      } else {
        return new DataUsuarioAux(u.getNombre(), u.getNickname(), u.getEmail(), u.getContrasenia());
      }
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw ex; // propagar para que la UI decida qué mostrar
    } finally {
      em.close();
    }
  }

  // ===============================
  // ACTUALIZAR
  // ===============================
  /**
   * Actualiza la información de un usuario en la base de datos.
   */
  public void actualizarUsuario(Usuario usuario) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      em.merge(usuario);
      em.getTransaction().commit();
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw ex;
    } finally {
      em.close();
    }
  }

  // ===============================
  // OBTENER POR NICK
  // ===============================
  /**
   * Obtiene un cliente por su nickname.
   */
  public Cliente obtenerClientePorNickname(String nickname) {
	  EntityManager em = JPAUtil.getEntityManager();
	  String key = canonical(nickname); 
	  try {
	    return em.createQuery(
	        "select c from Cliente c where lower(c.nickname) = :nick", Cliente.class)
	        .setParameter("nick", key)
	        .getResultStream().findFirst().orElse(null);
	  } finally {
	    em.close();
	  }
	}

  /**
   * Obtiene una aerolínea por su nickname.
   */
  public Aerolinea obtenerAerolineaPorNickname(String key) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      List<Aerolinea> aerolineas = em
          .createQuery("from Aerolinea a where a.nickname = :nickname", Aerolinea.class)
          .setParameter("nickname", key).getResultList();
      em.getTransaction().commit();
      return aerolineas.isEmpty() ? null : aerolineas.get(0);
    } finally {
      em.close();
    }
  }

  /**
   * Verifica si un cliente ya compró un paquete.
   */
  public boolean clienteYaComproPaquete(String nicknameCliente, String nombrePaquete) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      Long count = em.createQuery(
          "SELECT COUNT(cp) FROM CompraPaquete cp WHERE cp.cliente.nickname = :nickname AND cp.paquete.nombre = :nombre",
          Long.class).setParameter("nickname", nicknameCliente)
          .setParameter("nombre", nombrePaquete).getSingleResult();
      em.getTransaction().commit();
      return count != null && count > 0;
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw ex;
    } finally {
      em.close();
    }
  }

  /**
   * Lista los vuelos específicos por ruta y aerolínea.
   */
  public List<DataVueloEspecifico> listarVuelosPorRuta(String nickname, String nombre) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      List<DataVueloEspecifico> vuelos = em.createQuery(
          "SELECT new DataVueloEspecifico(ve.nombre, ve.fecha, ve.duracion, ve.maxAsientosTur, ve.maxAsientosEjec, ve.fechaAlta) "
              + "FROM VueloEspecifico ve " + "JOIN ve.ruta r " + "JOIN r.aerolineas a "
              + "WHERE a.nickname = :nickname AND r.nombre = :nombre",
          DataVueloEspecifico.class).setParameter("nickname", nickname)
          .setParameter("nombre", nombre).getResultList();
      em.getTransaction().commit();
      return vuelos;
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw ex; // propagar para que la UI decida qué mostrar
    } finally {
      em.close();
    }
  }

  // ===============================
  // VALIDACIONES
  // ===============================

  /**
   * Verifica si existe un usuario con el nickname dado.
   */
  public boolean existeNickname(String nickname) {
	  EntityManager em = JPAUtil.getEntityManager();
	  String key = canonical(nickname);
	  try {
	    // sin transacción alcanza para reads
	    Long count = em.createQuery(
	        "select count(u) from Usuario u where lower(u.nickname) = :nick",
	        Long.class)
	      .setParameter("nick", key)
	      .getSingleResult();
	    return count != null && count > 0;
	  } finally {
	    em.close();
	  }
	}

	public boolean existeEmail(String email) {
	  EntityManager em = JPAUtil.getEntityManager();
	  String key = canonical(email);
	  try {
	    Long count = em.createQuery(
	        "select count(u) from Usuario u where lower(u.email) = :mail",
	        Long.class)
	      .setParameter("mail", key)
	      .getSingleResult();
	    return count != null && count > 0;
	  } finally {
	    em.close();
	  }
	}

  /**
   * Verifica si existe una cédula (número de documento) en clientes.
   */
  public boolean existeCedula(String numDoc) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      Long count = em
          .createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.numDocumento = :numDoc", Long.class)
          .setParameter("numDoc", numDoc).getSingleResult();
      em.getTransaction().commit();
      return count != null && count > 0;
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw ex;
    } finally {
      em.close();
    }
  }

  // ===============================
  // AUTENTICACIÓN
  // ===============================

  /**
   * Autentica un usuario por login (nickname o email) y contraseña.
   */
  public Usuario autenticarUsuario(String login, String password) {
	  EntityManager em = JPAUtil.getEntityManager();

	  // === agregar string canónico (trim + lower) ===
	  final String key = (login == null) ? null : login.trim().toLowerCase();

	  try {
	    em.getTransaction().begin();
	    List<Usuario> usuarios = em.createQuery(
	        "from Usuario u " +
	        "where lower(u.nickname) = :login or lower(u.email) = :login",
	        Usuario.class)
	      .setParameter("login", key)   // <-- pasamos key
	      .getResultList();
	    em.getTransaction().commit();

	    if (usuarios.isEmpty()) return null;

	    Usuario u = usuarios.get(0);
	    // TODO: reemplazar por comparación de hash si usás encriptación
	    return (u.getContrasenia() != null && u.getContrasenia().equals(password)) ? u : null;

	  } catch (RuntimeException ex) {
	    if (em.getTransaction().isActive()) em.getTransaction().rollback();
	    throw ex;
	  } finally {
	    em.close();
	  }
	}

  /**
   * Busca un usuario por su nickname.
   */
  public Usuario findByNickname(String nickname) {
    if (nickname == null) {
      return null;
    }
    String nick = nickname.trim(); // normalizo básico
    EntityManager em = JPAUtil.getEntityManager();
    try {
      return em.createQuery("SELECT u FROM Usuario u WHERE u.nickname = :nick", Usuario.class)
          .setParameter("nick", nick).getSingleResult();
    } catch (NoResultException e) {
      return null;
    } finally {
      em.close();
    }
  }

  /**
   * Autenticación provisorio para pruebas: compara plano vs plano (jet / 1234).
   */
  public Usuario autenticarPorNicknamePlano(String nickname, String passwordPlain) {
    if (nickname == null || passwordPlain == null) {
      return null;
    }
    Usuario u = findByNickname(nickname);
    if (u == null) {
      return null;
    }
    // ⚠ Solo para desarrollo: contraseña en texto plano
    return passwordPlain.equals(u.getContrasenia()) ? u : null;
  }

  
  private static String canonical(String s) {
	    return (s == null) ? null : s.trim().toLowerCase(Locale.ROOT);
  }
}