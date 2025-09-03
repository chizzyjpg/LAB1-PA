package BD;

import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import Logica.Ciudad;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import Logica.DataCiudad;



public class CiudadService {
	
	public void crearCiudad(Ciudad c) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			
			em.persist(c);
			
			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex; // propagar para que la UI decida qué mostrar
		} finally {
			em.close();
		}
	}
	
	public List<Ciudad> listarCiudades(){
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			List<Ciudad> ciudades = em.createQuery("from Ciudad", Ciudad.class).getResultList();
			em.getTransaction().commit();
			return ciudades;
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
					em.close();
			}
		}
	
	public Ciudad buscarPorNombre(String nombre) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			List<Ciudad> ciudades = em.createQuery("from Ciudad c where c.nombre = :nombre", Ciudad.class)
				.setParameter("nombre", nombre)
				.getResultList();
			em.getTransaction().commit();
			return ciudades.isEmpty() ? null : ciudades.get(0);
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex;
		} finally {
			em.close();
		}
	}
	}