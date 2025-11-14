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
        DataRuta dto = new DataRuta(r.getNombre(), r.getDescripcion(),
            new DataCiudad(r.getOrigen().getNombre(), r.getOrigen().getPais(),
                r.getOrigen().getNombreAeropuerto(), r.getOrigen().getDescripcionAeropuerto(),
                r.getOrigen().getFechaAlta(), r.getOrigen().getSitioWeb()),
            new DataCiudad(r.getDestino().getNombre(), r.getDestino().getPais(),
                r.getDestino().getNombreAeropuerto(), r.getDestino().getDescripcionAeropuerto(),
                r.getDestino().getFechaAlta(), r.getDestino().getSitioWeb()),
            r.getHora(), r.getFechaAlta(), r.getCostoTurista(), r.getCostoEquipajeExtra(),
            r.getCostoEjecutivo(), new DataCategoria(r.getCategoriaR().getNombre()), "", // no
                                                                                         // conocemos
                                                                                         // el
                                                                                         // nicknameAerolinea
                                                                                         // aquí
            r.getEstado(), r.getDescripcionCorta());
        dto.setIdRuta(r.getIdRuta()); // ← CLAVE: pasar el ID real
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
        DataRuta dto = new DataRuta(r.getNombre(), r.getDescripcion(),
            new DataCiudad(r.getOrigen().getNombre(), r.getOrigen().getPais(),
                r.getOrigen().getNombreAeropuerto(), r.getOrigen().getDescripcionAeropuerto(),
                r.getOrigen().getFechaAlta(), r.getOrigen().getSitioWeb()),
            new DataCiudad(r.getDestino().getNombre(), r.getDestino().getPais(),
                r.getDestino().getNombreAeropuerto(), r.getDestino().getDescripcionAeropuerto(),
                r.getDestino().getFechaAlta(), r.getDestino().getSitioWeb()),
            r.getHora(), r.getFechaAlta(), r.getCostoTurista(), r.getCostoEquipajeExtra(),
            r.getCostoEjecutivo(), new DataCategoria(r.getCategoriaR().getNombre()),
            nicknameAerolinea, // setea el nickname de la aerolínea asociada
            r.getEstado(), r.getDescripcionCorta());
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
}