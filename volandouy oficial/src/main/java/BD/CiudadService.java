package BD;

import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import Logica.Ciudad;
import java.util.Date;



public class CiudadService {
	
	public void crearCiudad(String nombre,String pais,String nombreAeropuerto,String descripcionAeropuerto,Date fechaAlta,String sitioWeb) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			
			Ciudad c = new Ciudad(nombre, pais, nombreAeropuerto, descripcionAeropuerto, fechaAlta, sitioWeb);
			em.persist(c);
			
			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex; // propagar para que la UI decida qué mostrar
		} finally {
			em.close();
		}
	}
}	
