package BD;

import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import Logica.Ciudad;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import Logica.DataCiudad;



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
	
	public List<DataCiudad> listarCiudades(){
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			List<Ciudad> ciudades = em.createQuery("from Ciudad", Ciudad.class).getResultList();
			em.getTransaction().commit();
			return ciudades.stream().map(c -> new DataCiudad(c.getNombre(), c.getPais(), c.getNombreAeropuerto(), c.getDescripcionAeropuerto(), c.getFechaAlta(), c.getSitioWeb())).collect(Collectors.toList());
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
					em.close();
			}		
		}
	}