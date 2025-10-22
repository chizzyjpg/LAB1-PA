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
				.map(a -> new DataAerolinea(a.getNombre(), a.getNickname(), a.getEmail(), a.getContrasenia(), a.getDescGeneral(), a.getLinkWeb()))
				.collect(Collectors.toList());
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex;
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
}
