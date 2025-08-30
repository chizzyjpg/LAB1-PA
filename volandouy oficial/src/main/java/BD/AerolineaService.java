package BD;

import Logica.Aerolinea;
import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import Logica.DataAerolinea;



public class AerolineaService {
	
		public List<DataAerolinea> listarAerolineas() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			var aerolineas = em.createQuery("SELECT a FROM Aerolinea a", Aerolinea.class).getResultList();
			em.getTransaction().commit();
			// Convertir Aerolinea a DataAerolinea
			return aerolineas.stream()
				.map(a -> new DataAerolinea(a.getNombre(), a.getNickname(), a.getEmail(), a.getDescGeneral(), a.getLinkWeb()))
				.collect(Collectors.toList());
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex;
		} finally {
			em.close();
		}
	}
	
	public void crearAerolinea(String nombre, String nick, String email, String descGeneral, String linkWeb) {
		if (nombre == null || nombre.isBlank() || nick == null || nick.isBlank() || email == null || email.isBlank() || descGeneral == null || descGeneral.isBlank() || linkWeb == null || linkWeb.isBlank()) {
			throw new IllegalArgumentException("Ningún campo puede estar vacío.");
		}

		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			// Evitar duplicados (como el @Id es 'nickname')
			Aerolinea existente = em.find(Aerolinea.class, nick);
			if (existente != null) {
				throw new IllegalStateException("Ya existe una aerolínea con ese nickname.");
			}

			Aerolinea a = new Aerolinea(nombre.trim(), nick.trim(), email.trim(), descGeneral.trim(), linkWeb.trim());
			em.persist(a);

			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex; // propagar para que la UI decida qué mostrar
		} finally {
			em.close();
		}
	}
}
