package BD;

import Logica.Usuario;
import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import Logica.DataUsuario;
import Logica.DataUsuarioAux;

public class UsuarioService {
	
		public List<DataUsuario> listarUsuarios() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			var usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
			em.getTransaction().commit();
			// Convertir Usuario a DataUsuario
			return usuarios.stream()
				.map(u -> new DataUsuarioAux(u.getNombre(), u.getNickname(), u.getEmail()))
				.collect(Collectors.toList());
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex;
		} finally {
			em.close();
		}
	}
	
}
