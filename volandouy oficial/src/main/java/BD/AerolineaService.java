package BD;

import Logica.Aerolinea;
import Logica.DataAerolinea;
import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con Aerolinea.
 */
public class AerolineaService {
  
  /**
   * Lista todas las aerolíneas en la base de datos.
   *
   * 
   */
  public List<DataAerolinea> listarAerolineas() {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      var aerolineas = em.createQuery("SELECT a FROM Aerolinea a", Aerolinea.class).getResultList();
      em.getTransaction().commit();
      // Convertir Aerolinea a DataAerolinea
      return aerolineas.stream().map(a -> new DataAerolinea(a.getNombre(), a.getNickname(),
          a.getEmail(), a.getContrasenia(), a.getDescGeneral(), a.getLinkWeb()))
          .collect(Collectors.toList());
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
   * Crea una nueva aerolínea en la base de datos.
   *
   * 
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
}
