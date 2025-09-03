package BD;


import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import Logica.Categoria;
import Logica.Ciudad;
import Logica.DataCategoria;
import Logica.DataCiudad;
import Logica.DataRuta;
import Logica.JPAUtil;
import Logica.Ruta;

public class RutaVueloService {

	public void crearRutaVuelo(Ruta r) {
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			em.getTransaction().begin();
			
			em.persist(r);
			
			em.getTransaction().commit();
			} catch (RuntimeException ex) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				throw ex; // propagar para que la UI decida qué mostrar
			} finally {
					em.close();
			}
	}
	
	public List<DataRuta> listarRutas(){
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			List<Ruta> rutas = em.createQuery("from Ruta", Ruta.class).getResultList();
			em.getTransaction().commit();
			// Convertir Ruta a DataRuta usando DTOs
			return rutas.stream()
				.map(r -> new DataRuta(
					r.getNombre(),
					r.getDescripcion(),
					new DataCiudad(
						r.getOrigen().getNombre(),
						r.getOrigen().getPais(),
						r.getOrigen().getNombreAeropuerto(),
						r.getOrigen().getDescripcionAeropuerto(),
						r.getOrigen().getFechaAlta(),
						r.getOrigen().getSitioWeb()
					),
					new DataCiudad(
						r.getDestino().getNombre(),
						r.getDestino().getPais(),
						r.getDestino().getNombreAeropuerto(),
						r.getDestino().getDescripcionAeropuerto(),
						r.getDestino().getFechaAlta(),
						r.getDestino().getSitioWeb()
					),
					r.getHora(),
					r.getFechaAlta(),
					r.getCostoTurista(),
					r.getCostoEquipajeExtra(),
					r.getCostoEjecutivo(),
					new DataCategoria(r.getCategoriaR().getNombre())
				))
				.collect(Collectors.toList());
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex; // propagar para que la UI decida qué mostrar
		} finally {
			em.close();
		}
	}
}