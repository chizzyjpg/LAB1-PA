package BD;


import Logica.Aerolinea;
import Logica.DataCategoria;
import Logica.DataCiudad;
import Logica.DataRuta;
import Logica.JPAUtil;
import Logica.Ruta;
import Logica.DataRutaMasVisitada;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con Ruta de Vuelo.
 */
public class RutaVueloService {

  /**
   * 
   * Crea una nueva ruta de vuelo en la base de datos y la asocia con una aerolínea.
   */
  public void crearRutaVuelo(Ruta r, String nicknameAerolinea) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      // Obtener la aerolínea por nickname
      UsuarioService usuarioService = new UsuarioService();
      Aerolinea aerolinea = usuarioService.obtenerAerolineaPorNickname(nicknameAerolinea);
      if (aerolinea != null) {
        r.getAerolineas().add(aerolinea);
      }
      em.persist(r);
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
   * Lista todas las rutas de vuelo en la base de datos.
   */
  public List<DataRuta> listarRutas() {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      List<Ruta> rutas = em.createQuery("from Ruta", Ruta.class).getResultList();
      // Inicializar la colección aerolineas de cada ruta
      for (Ruta r : rutas) {
        r.getAerolineas().size(); // fuerza la carga de la colección
      }
      em.getTransaction().commit();

      return rutas.stream().map(r -> {
        DataRuta dto = new DataRuta(
            r.getNombre(),
            r.getDescripcion(),
            new DataCiudad(r.getOrigen().getNombre(), r.getOrigen().getPais(),
                r.getOrigen().getNombreAeropuerto(), r.getOrigen().getDescripcionAeropuerto(),
                r.getOrigen().getFechaAlta(), r.getOrigen().getSitioWeb()),
            new DataCiudad(r.getDestino().getNombre(), r.getDestino().getPais(),
                r.getDestino().getNombreAeropuerto(), r.getDestino().getDescripcionAeropuerto(),
                r.getDestino().getFechaAlta(), r.getDestino().getSitioWeb()),
            r.getHora(), r.getFechaAlta(), r.getCostoTurista(), r.getCostoEquipajeExtra(),
            r.getCostoEjecutivo(), new DataCategoria(r.getCategoriaR().getNombre()),
            "",                // nicknameAerolinea no conocido aquí
            r.getEstado(), r.getDescripcionCorta(),
            r.getVideoUrl()      // nuevo campo
        );
        dto.setIdRuta(r.getIdRuta());
        return dto;
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
   * Busca una ruta por su nombre y devuelve su ID.
   */
  public Integer buscarRutaPorNombreYObtenerId(String nombre) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      Ruta ruta = em.createQuery("SELECT r FROM Ruta r WHERE r.nombre = :nombre", Ruta.class)
          .setParameter("nombre", nombre).getSingleResult();
      em.getTransaction().commit();
      return ruta != null ? ruta.getIdRuta() : null;
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
   * Busca una ruta por su ID.
   */
  public Ruta buscarRutaPorId(Integer idRuta) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      Ruta ruta = em.find(Ruta.class, idRuta);
      // Inicializar la colección aerolineas antes de cerrar el EntityManager
      if (ruta != null) {
        ruta.getAerolineas().size();
      }
      em.getTransaction().commit();
      return ruta;
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
   * Actualiza una ruta existente en la base de datos.
   */
  public void actualizarRuta(Ruta r) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      em.merge(r);
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
   * Busca una ruta por su nombre.
   */
  public Ruta buscarRutaPorNombre(String nombre) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      List<Ruta> rutas = em.createQuery("from Ruta r where r.nombre = :nombre", Ruta.class)
          .setParameter("nombre", nombre).getResultList();
      em.getTransaction().commit();
      return rutas.isEmpty() ? null : rutas.get(0);
    } finally {
      em.close();
    }
  }

  /**
   * Lista todas las rutas asociadas a una aerolínea específica por su nickname.
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

      return rutas.stream().map(r -> {
        DataRuta dto = new DataRuta(
            r.getNombre(),
            r.getDescripcion(),
            new DataCiudad(r.getOrigen().getNombre(), r.getOrigen().getPais(),
                r.getOrigen().getNombreAeropuerto(), r.getOrigen().getDescripcionAeropuerto(),
                r.getOrigen().getFechaAlta(), r.getOrigen().getSitioWeb()),
            new DataCiudad(r.getDestino().getNombre(), r.getDestino().getPais(),
                r.getDestino().getNombreAeropuerto(), r.getDestino().getDescripcionAeropuerto(),
                r.getDestino().getFechaAlta(), r.getDestino().getSitioWeb()),
            r.getHora(), r.getFechaAlta(), r.getCostoTurista(), r.getCostoEquipajeExtra(),
            r.getCostoEjecutivo(), new DataCategoria(r.getCategoriaR().getNombre()),
            nicknameAerolinea,       // aerolínea asociada conocida aquí
            r.getEstado(), r.getDescripcionCorta(),
            r.getVideoUrl()          // nuevo campo
        );
        dto.setIdRuta(r.getIdRuta());
        return dto;
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
   * Lista las 5 rutas más visitadas.
   */
  public List<Ruta> listar5RutasMasVisitadas() {
      EntityManager em = JPAUtil.getEntityManager();
      try {
          // Para una consulta de solo lectura no hace falta transacción
          return em.createQuery(
                          "SELECT r FROM Ruta r ORDER BY r.visitas DESC",
                          Ruta.class)
                  .setMaxResults(5)
                  .getResultList();
      } finally {
          em.close();
      }
  }
  /**
   * Busca una ruta en un paquete
   */
  public boolean estaEnPaquete(int idRuta){
      EntityManager em = JPAUtil.getEntityManager();
      try {
          em.getTransaction().begin();
          // Recuperar el nombre canónico de la ruta
          Ruta ruta = em.find(Ruta.class, idRuta);
          if (ruta == null) {
              em.getTransaction().commit();
              return false;
          }
          String nombreCanonico = ruta.getNombre().trim().toLowerCase(java.util.Locale.ROOT);
          Long count = em.createQuery(
                          "SELECT COUNT(p) FROM Paquete p JOIN p.rutasIncluidas r WHERE r = :nombreRuta", Long.class)
                  .setParameter("nombreRuta", nombreCanonico)
                  .getSingleResult();
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
     * Busca una ruta para cuadro de búsqueda
     */
    public List<Ruta> buscarPorTexto(String texto) {
        EntityManager em = JPAUtil.getEntityManager();
        if (texto == null || texto.trim().isEmpty()) {
            try {
                em.getTransaction().begin();
                List<Ruta> rutas = em.createQuery("SELECT r FROM Ruta r ORDER BY r.fechaAlta DESC", Ruta.class).getResultList();
                em.getTransaction().commit();
                return rutas;
            } finally {
                em.close();
            }
        } else {
            try {
                String patron = "%" + texto.toLowerCase().trim() + "%";
                return em.createQuery(
                                "SELECT r FROM Ruta r " +
                                        "WHERE LOWER(r.nombre) LIKE :patron " +
                                        "   OR LOWER(r.descripcion) LIKE :patron " +
                                        "ORDER BY r.fechaAlta DESC",
                                Ruta.class).setParameter("patron", patron).getResultList();
            } finally {
                em.close();
            }
        }
    }

}