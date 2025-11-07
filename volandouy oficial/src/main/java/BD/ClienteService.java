package BD;

import Logica.Cliente;
import Logica.DataCliente;
import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con clientes en la base de datos.
 */
public class ClienteService {
  /**
   * Lista todos los clientes en la base de datos y los convierte a objetos DataCliente.
   * 
   */
  public List<DataCliente> listarClientes() {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      var clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
      em.getTransaction().commit();
      // Convertir Cliente a DataCliente
      return clientes.stream()
          .map(c -> new DataCliente(c.getNombre(), c.getNickname(), c.getEmail(),
              c.getContrasenia(), c.getApellido(), c.getFechaNac(), c.getNacionalidad(),
              c.getTipoDocumento(), c.getNumDocumento()))
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
   * Crea un nuevo cliente en la base de datos.
   * 
   */
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
}
