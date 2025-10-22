package BD;

import java.util.List;

import Logica.Cliente;
import Logica.CompraPaquete;
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
	                "SELECT r FROM Ruta r JOIN r.aerolineas a WHERE a.nickname = :nickname AND r.nombre = :nombre")
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
		
		public CompraPaquete comprarPaquete(CompraPaquete cp, Cliente c, Paquete p) {
			EntityManager em = JPAUtil.getEntityManager();
			try {
				em.getTransaction().begin();
				
				// Asociar el cliente y el paquete a la compra
				cp.setCliente(c);
				cp.setPaquete(p);
				
				// Persistir la compra
				em.persist(cp);
				/*
				// Actualizar las relaciones en Cliente y Paquete
				c.getNickname().add(cp);
				p.getNombre().add(cp);
				
				// Hacer merge para actualizar las entidades
				em.merge(c);
				em.merge(p);
				*/
				em.getTransaction().commit();
				return cp;
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
					em.close();
			}
		}
		
		public List<String> listarNombresPaquetesComprados() {
		    EntityManager em = JPAUtil.getEntityManager();
		    try {
		        return em.createQuery("SELECT DISTINCT cp.paquete.nombre FROM CompraPaquete cp", String.class)
		                 .getResultList();
		    } finally {
		        em.close();
		    }
		}

		
	}