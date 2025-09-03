package BD;

import java.util.List;

import Logica.JPAUtil;
import Logica.Paquete;
import Logica.Ruta;
import jakarta.persistence.EntityManager;

	public class PaqueteService {
	
		// ===============================
		//  CREAR PAQUETE
		// ===============================
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
		
		// ===============================
		//  LISTAR PAQUETES
		// ===============================
		public List<Paquete> listarPaquetes(){
			EntityManager em = JPAUtil.getEntityManager();
			try {
				em.getTransaction().begin();
				List<Paquete> paquetes = em.createQuery("from Paquete", Paquete.class).getResultList();
				em.getTransaction().commit();
				return paquetes;
				} catch (RuntimeException ex) {
					if (em.getTransaction().isActive()) em.getTransaction().rollback();
					throw ex; // propagar para que la UI decida qué mostrar
				} finally {
						em.close();
				}
		}
		
		// ===============================
		//  EXISTE / BUSCAR / ACTUALIZAR
		// ===============================
		public Paquete existePaquete(String nombre) {
			EntityManager em = JPAUtil.getEntityManager();
			try {
				em.getTransaction().begin();
				List<Paquete> paquetes = em.createQuery("from Paquete p where p.nombre = :nombre", Paquete.class)
					.setParameter("nombre", nombre)
					.getResultList();
				em.getTransaction().commit();
				return paquetes.isEmpty() ? null : paquetes.get(0);
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex;
			} finally {
				em.close();
			}
		}
		
		public Ruta buscarRutaEnAerolinea(String nicknameAerolinea, String nombreRuta) {
	        EntityManager em = JPAUtil.getEntityManager();
	        try {
	            em.getTransaction().begin();
	            List<Ruta> rutas = em.createQuery(
	                "SELECT r FROM Ruta r WHERE r.aerolinea.nickname = :nickname AND r.nombre = :nombre", Logica.Ruta.class)
	                .setParameter("nickname", nicknameAerolinea)
	                .setParameter("nombre", nombreRuta)
	                .getResultList();
	            em.getTransaction().commit();
	            return rutas.isEmpty() ? null : rutas.get(0);
	        } catch (RuntimeException ex) {
	            if (em.getTransaction().isActive()) em.getTransaction().rollback();
	            throw ex;
	        } finally {
	            em.close();
	        }
	    }
		
		public void actualizarPaquete(Paquete paquete) {
	        EntityManager em = JPAUtil.getEntityManager();
	        try {
	            em.getTransaction().begin();
	            em.merge(paquete);
	            em.getTransaction().commit();
	        } catch (RuntimeException ex) {
	            if (em.getTransaction().isActive()) em.getTransaction().rollback();
	            throw ex;
	        } finally {
	            em.close();
	        }
	    }
		
	}