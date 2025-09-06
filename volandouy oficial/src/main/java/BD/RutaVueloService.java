package BD;


import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import Logica.Aerolinea;
import Logica.Categoria;
import Logica.Ciudad;
import Logica.DataCategoria;
import Logica.DataCiudad;
import Logica.DataRuta;
import Logica.JPAUtil;
import Logica.Ruta;
import BD.UsuarioService;

public class RutaVueloService {

	public void crearRutaVuelo(Ruta r, String nicknameAerolinea) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Obtener la aerolínea por nickname
            UsuarioService usuarioService = new UsuarioService();
            Aerolinea aerolinea = usuarioService.obtenerAerolineaPorNickname(nicknameAerolinea);
            if (aerolinea != null) {
                r.getAerolineas().add(aerolinea);
            }
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
					new DataCategoria(r.getCategoriaR().getNombre()),
					"" // No se conoce el nicknameAerolinea aquí
				))
				.collect(Collectors.toList());
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex; // propagar para que la UI decida qué mostrar
		} finally {
			em.close();
		}
	}

	public Integer buscarRutaPorNombreYObtenerId(String nombre) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Ruta ruta = em.createQuery("SELECT r FROM Ruta r WHERE r.nombre = :nombre", Ruta.class)
					.setParameter("nombre", nombre)
					.getSingleResult();
			em.getTransaction().commit();
			return ruta != null ? ruta.getIdRuta() : null;
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex; // propagar para que la UI decida qué mostrar
		} finally {
			em.close();
		}
	}

	public Ruta buscarRutaPorId(Integer idRuta) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Ruta ruta = em.find(Ruta.class, idRuta);
			em.getTransaction().commit();
			return ruta;
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex; // propagar para que la UI decida qué mostrar
		} finally {
			em.close();
		}
	}
}