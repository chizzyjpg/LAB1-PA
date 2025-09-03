package BD;

import Logica.JPAUtil;
import Logica.Paquete;
import jakarta.persistence.EntityManager;

public class PaqueteService {

	public void crearPaquete(Paquete p) {
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			em.getTransaction().begin();
			
			em.persist(p);
			
			em.getTransaction().commit();
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
					em.close();
			}
	}
}
