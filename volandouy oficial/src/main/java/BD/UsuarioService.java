package BD;

import Logica.Usuario;
import Logica.JPAUtil;
import Logica.ManejadorRuta;
import Logica.Ruta;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import Logica.Aerolinea;
import Logica.Cliente;
import Logica.DataAerolinea;
import Logica.DataCliente;
import Logica.DataRuta;
import Logica.DataUsuario;
import Logica.DataUsuarioAux;

public class UsuarioService {
	
	public List<DataUsuario> listarUsuarios() {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        em.getTransaction().begin();
	        var usuarios = em.createQuery(
	            "SELECT u FROM Usuario u ORDER BY LOWER(COALESCE(u.nickname, ''))",
	            Usuario.class
	        ).getResultList();
	        em.getTransaction().commit();

	        return usuarios.stream()
	            .map(u -> {
	                if (u instanceof Aerolinea a) {
	                    // DataAerolinea extiende DataUsuario (concretA)
	                    return new DataAerolinea(
	                        a.getNombre(),
	                        a.getNickname(),
	                        a.getEmail(),
	                        a.getDescGeneral(),
	                        a.getLinkWeb()
	                    );
	                } else {
	                    // “Usuario” genérico (Cliente u otros): usamos la concreta Aux
	                    return new DataUsuarioAux(
	                        u.getNombre(),
	                        u.getNickname(),
	                        u.getEmail()
	                    );
	                }
	            })
	            .collect(Collectors.toList());
	    } catch (RuntimeException ex) {
	        if (em.getTransaction().isActive()) em.getTransaction().rollback();
	        throw ex;
	    } finally {
	        em.close();
	    }
	}

		
		public void crearCliente (Cliente c) {
			
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
		
		public void crearAerolinea(Aerolinea a) {
			
			EntityManager em = JPAUtil.getEntityManager();
			try {
				em.getTransaction().begin();

				em.persist(a);

				em.getTransaction().commit();
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
				em.close();
			}
		}
		
		public DataUsuario verInfoUsuario (String nickname){
			EntityManager em = JPAUtil.getEntityManager();
			try {
				em.getTransaction().begin();
				List<Usuario> usuarios = em.createQuery("from Usuario u where u.nickname = :nickname", Usuario.class)
					.setParameter("nickname", nickname)
					.getResultList();
				em.getTransaction().commit();
				if (usuarios.isEmpty()) return null;
				
				Usuario u = usuarios.get(0);
				
				if (u instanceof Cliente c) {
					return new DataCliente(
			                c.getNombre(),
			                c.getNickname(),
			                c.getEmail(),
			                c.getApellido(),
			                c.getFechaNac(),
			                c.getNacionalidad(),
			                c.getTipoDocumento(),
			                c.getNumDocumento()
			            );
				} else if (u instanceof Aerolinea a) {
					return new DataAerolinea(
			                a.getNombre(),
			                a.getNickname(),
			                a.getEmail(),
			                a.getDescGeneral(),
			                a.getLinkWeb()
			            );
				} else {
					return new DataUsuarioAux(
	                        u.getNombre(),
	                        u.getNickname(),
	                        u.getEmail()
	                    );
				}
				
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
				em.close();
			}
		}
		
		public List<DataRuta> listarRutasPorAerolinea(String nicknameAerolinea) {
	        EntityManager em = JPAUtil.getEntityManager();
	        try {
	            em.getTransaction().begin();
	            List<Ruta> rutas = em.createQuery(
	                "SELECT r FROM Ruta r WHERE r.aerolinea.nickname = :nickname", Ruta.class)
	                .setParameter("nickname", nicknameAerolinea)
	                .getResultList();
	            em.getTransaction().commit();
	            // Usar ManejadorRuta para convertir de entidad a DTO
	            return rutas.stream()
	                .map(ManejadorRuta::toData)
	                .collect(Collectors.toList());
	        } catch (RuntimeException ex) {
	            if (em.getTransaction().isActive()) em.getTransaction().rollback();
	            throw ex;
	        } finally {
	            em.close();
	        }
	    }
		
		public void actualizarUsuario(Usuario usuario) {
	        EntityManager em = JPAUtil.getEntityManager();
	        try {
	            em.getTransaction().begin();
	            em.merge(usuario);
	            em.getTransaction().commit();
	        } catch (RuntimeException ex) {
	            if (em.getTransaction().isActive()) em.getTransaction().rollback();
	            throw ex;
	        } finally {
	            em.close();
	        }
	    }
		
}