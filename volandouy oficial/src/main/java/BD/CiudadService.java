package BD;

import Logica.Ciudad;
import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Servicio para manejar operaciones relacionadas con la entidad Ciudad.
 */
public class CiudadService {

  /**
   * Crea una nueva ciudad en la base de datos.
   * 
   */
  public void crearCiudad(Ciudad c) {
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
   * Lista todas las ciudades en la base de datos.
   * 
   */
  public List<Ciudad> listarCiudades() {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      List<Ciudad> ciudades = em.createQuery("from Ciudad", Ciudad.class).getResultList();
      em.getTransaction().commit();
      return ciudades;
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
   * Busca una ciudad por su nombre.
   * 
   */
  public Ciudad buscarPorNombre(String nombre) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      List<Ciudad> ciudades = em.createQuery("from Ciudad c where c.nombre = :nombre", Ciudad.class)
          .setParameter("nombre", nombre).getResultList();
      em.getTransaction().commit();
      return ciudades.isEmpty() ? null : ciudades.get(0);
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
   * Verifica si una ciudad con el mismo nombre y país ya existe.
   * 
   */
  public boolean existeCiudad(String nombre, String pais) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      Long count = em
          .createQuery("SELECT COUNT(c) FROM Ciudad c WHERE c.nombre = :nombre AND c.pais = :pais",
              Long.class)
          .setParameter("nombre", nombre).setParameter("pais", pais).getSingleResult();
      em.getTransaction().commit();
      return count > 0;
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();        
      }
      throw ex;
    } finally {
      em.close();
    }
  }
}