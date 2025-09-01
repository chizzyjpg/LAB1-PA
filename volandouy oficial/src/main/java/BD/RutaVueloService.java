package BD;


import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Date;

import Logica.Categoria;
import Logica.Ciudad;
import Logica.JPAUtil;
import Logica.Ruta;

public class RutaVueloService {

	public void crearRutaVuelo(String nombre, String descripcion, Ciudad ciudadOrigen,
			Ciudad ciudadDestino, int hora, Date fechaAlta, BigDecimal costoTurista, int costoEquipajeExtra, BigDecimal costoEjecutivo) {
		// TODO Auto-generated method stub
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			em.getTransaction().begin();
			
			// Verificar y persistir ciudadOrigen si es nueva
			if (ciudadOrigen.getIdCiudad() == 0) {
				em.persist(ciudadOrigen);
			}
			// Verificar y persistir ciudadDestino si es nueva
			if (ciudadDestino.getIdCiudad() == 0) {
				em.persist(ciudadDestino);
			}

			Ruta r = new Ruta(nombre, descripcion, ciudadOrigen, ciudadDestino, hora, fechaAlta, costoTurista, costoEquipajeExtra, costoEjecutivo);
			em.persist(r);
			
			em.getTransaction().commit();
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
					em.close();
			}
	}
}