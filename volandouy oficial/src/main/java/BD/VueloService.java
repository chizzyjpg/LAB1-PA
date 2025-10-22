package BD;

import Logica.JPAUtil;
import Logica.VueloEspecifico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * Servicio para gestionar operaciones relacionadas con VueloEspecifico.
 */
public class VueloService {

  /**
   * Registra un nuevo vuelo en la base de datos.
   * 
   */
  public void registrarVuelo(VueloEspecifico datos) {

    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();

      em.persist(datos);

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
   * Verifica si existe un vuelo con el nombre dado en la base de datos.
   * 
   */
  public boolean existeVueloConNombre(String nombreVuelo) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(v) FROM VueloEspecifico v WHERE v.nombre = :nombre", Long.class);
      query.setParameter("nombre", nombreVuelo);
      Long count = query.getSingleResult();
      return count != null && count > 0;
    } finally {
      em.close();
    }
  }
}