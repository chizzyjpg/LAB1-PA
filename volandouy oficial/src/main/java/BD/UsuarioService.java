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
import Logica.DataVueloEspecifico;

	public class UsuarioService {
	
		// ===============================
		//  CREAR
		// ===============================
		
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
		
		// ===============================
		//  LISTAR
		// ===============================
	
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
		                        a.getContrasenia(),
		                        a.getDescGeneral(),
		                        a.getLinkWeb()
		                    );
		                } else {
		                    // “Usuario” genérico (Cliente u otros): usamos la concreta Aux
		                    return new DataUsuarioAux(
		                        u.getNombre(),
		                        u.getNickname(),
		                        u.getEmail(),
		                        u.getContrasenia()
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

		public List<DataRuta> listarRutasPorAerolinea(String nicknameAerolinea) {
            EntityManager em = JPAUtil.getEntityManager();
            try {
                em.getTransaction().begin();
                List<Ruta> rutas = em.createQuery(
                    "SELECT r FROM Ruta r JOIN r.aerolineas a WHERE a.nickname = :nickname", Ruta.class)
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

		// ===============================
		//  VER INFO
		// ===============================
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
			                c.getContrasenia(),
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
			                a.getContrasenia(),
			                a.getDescGeneral(),
			                a.getLinkWeb()
			            );
				} else {
					return new DataUsuarioAux(
	                        u.getNombre(),
	                        u.getNickname(),
	                        u.getEmail(),
	                        u.getContrasenia()
	                    );
				}
				
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
				em.close();
			}
		}
		
		// ===============================
		//  ACTUALIZAR
		// ===============================
		
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


		// ===============================
		//  OBTENER POR NICK
		// ===============================
		public Cliente obtenerClientePorNickname(String nickname) {
		    EntityManager em = JPAUtil.getEntityManager();
		    try {
		        em.getTransaction().begin();
		        List<Cliente> clientes = em.createQuery(
		            "from Cliente c where c.nickname = :nickname", Cliente.class)
		            .setParameter("nickname", nickname)
		            .getResultList();
		        em.getTransaction().commit();
		        return clientes.isEmpty() ? null : clientes.get(0);
		    } finally {
		        em.close();
		    }
		}


		public Aerolinea obtenerAerolineaPorNickname(String key) {
			EntityManager em = JPAUtil.getEntityManager();
		    try {
		        em.getTransaction().begin();
		        List<Aerolinea> aerolineas = em.createQuery(
		            "from Aerolinea a where a.nickname = :nickname", Aerolinea.class)
		            .setParameter("nickname", key)
		            .getResultList();
		        em.getTransaction().commit();
		        return aerolineas.isEmpty() ? null : aerolineas.get(0);
		    } finally {
		        em.close();
		    }
		}
		
		public boolean clienteYaComproPaquete(String nicknameCliente, String nombrePaquete) {
			EntityManager em = JPAUtil.getEntityManager();
					    try {
		        em.getTransaction().begin();
		        Long count = em.createQuery(
		            "SELECT COUNT(cp) FROM CompraPaquete cp WHERE cp.cliente.nickname = :nickname AND cp.paquete.nombre = :nombre",
		            Long.class)
		            .setParameter("nickname", nicknameCliente)
		            .setParameter("nombre", nombrePaquete)
		            .getSingleResult();
		        em.getTransaction().commit();
		        return count != null && count > 0;
		    } catch (RuntimeException ex) {
		        if (em.getTransaction().isActive()) em.getTransaction().rollback();
		        throw ex;
		    } finally {
		        em.close();
		    }
		}

		public List<DataVueloEspecifico> listarVuelosPorRuta(String nickname, String nombre) {
			EntityManager em = JPAUtil.getEntityManager();
			try {
				em.getTransaction().begin();
				List<DataVueloEspecifico> vuelos = em.createQuery(
					"SELECT new DataVueloEspecifico(ve.nombre, ve.fecha, ve.duracion, ve.maxAsientosTur, ve.maxAsientosEjec, ve.fechaAlta) " +
					"FROM VueloEspecifico ve " +
					"JOIN ve.ruta r " +
					"JOIN r.aerolineas a " +
					"WHERE a.nickname = :nickname AND r.nombre = :nombre", DataVueloEspecifico.class)
					.setParameter("nickname", nickname)
					.setParameter("nombre", nombre)
					.getResultList();
				em.getTransaction().commit();
				return vuelos;
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
				em.close();
			}
		}
		
		// ===============================
		//  VALIDACIONES
		// ===============================
		
		public boolean existeNickname(String nickname) {
			EntityManager em = JPAUtil.getEntityManager();
		    try {
		        em.getTransaction().begin();
		        Long count = em.createQuery(
		            "SELECT COUNT(u) FROM Usuario u WHERE u.nickname = :nickname",
		            Long.class)
		            .setParameter("nickname", nickname)
		            .getSingleResult();
		        em.getTransaction().commit();
		        return count != null && count > 0;
		    } catch (RuntimeException ex) {
		        if (em.getTransaction().isActive()) em.getTransaction().rollback();
		        throw ex;
		    } finally {
		        em.close();
		    }
		}
		
		public boolean existeEmail(String email) {
			EntityManager em = JPAUtil.getEntityManager();
		    try {
		        em.getTransaction().begin();
		        Long count = em.createQuery(
		            "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email",
		            Long.class)
		            .setParameter("email", email)
		            .getSingleResult();
		        em.getTransaction().commit();
		        return count != null && count > 0;
		    } catch (RuntimeException ex) {
		        if (em.getTransaction().isActive()) em.getTransaction().rollback();
		        throw ex;
		    } finally {
		        em.close();
		    }
		}
		
		public boolean existeCedula(String numDoc) {
			EntityManager em = JPAUtil.getEntityManager();
		    try {
		        em.getTransaction().begin();
		        Long count = em.createQuery(
		            "SELECT COUNT(c) FROM Cliente c WHERE c.numDocumento = :numDoc",
		            Long.class)
		            .setParameter("numDoc", numDoc)
		            .getSingleResult();
		        em.getTransaction().commit();
		        return count != null && count > 0;
		    } catch (RuntimeException ex) {
		        if (em.getTransaction().isActive()) em.getTransaction().rollback();
		        throw ex;
		    } finally {
		        em.close();
		    }
		}
		
		// ===============================
		//  AUTENTICACIÓN
		// ===============================

		public Usuario autenticarUsuario(String login, String password) {
			EntityManager em = JPAUtil.getEntityManager();
			try {
				em.getTransaction().begin();
				List<Usuario> usuarios = em.createQuery(
					"from Usuario u where u.nickname = :login or u.email = :login", Usuario.class)
					.setParameter("login", login)
					.getResultList();
				em.getTransaction().commit();
				if (usuarios.isEmpty()) return null;
				
				Usuario u = usuarios.get(0);
				// En un sistema real, la contraseña debería estar hasheada y se compararía el hash
				if (u.getContrasenia().equals(password)) {
					return u;
				} else {
					return null;
				}
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
				em.close();
			}
		}
	
	}
	