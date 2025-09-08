package BD;

import Logica.Cliente;
import Logica.JPAUtil;
import Logica.Reserva;
import Logica.VueloEspecifico;
import jakarta.persistence.EntityManager;

public class ReservaService {
	public void registrarReserva(Reserva reserva) {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        em.getTransaction().begin();
	        // Validar que las entidades asociadas no sean null
	        if (reserva.getCliente() == null || reserva.getCliente().getNickname() == null) {
	            throw new IllegalArgumentException("La reserva debe tener un cliente con nickname válido");
	        }
	        if (reserva.getVueloEspecifico() == null) {
	            throw new IllegalArgumentException("La reserva debe tener un vuelo específico con ID válido");
	        }
	        // Buscar entidades gestionadas por la sesión
	        Cliente cliente = em.find(Cliente.class, reserva.getCliente().getNickname());
	        VueloEspecifico vuelo = em.find(VueloEspecifico.class, reserva.getVueloEspecifico().getIdVueloEspecifico());
	        if (cliente == null) {
	            throw new IllegalArgumentException("No se encontró el cliente en la base de datos");
	        }
	        if (vuelo == null) {
	            throw new IllegalArgumentException("No se encontró el vuelo específico en la base de datos");
	        }
	        reserva.setCliente(cliente);
	        reserva.setVueloEspecifico(vuelo);
	        // Si la reserva tiene ruta, setear la gestionada
	        if (reserva.getRutasVuelo() != null) {
	            reserva.setRutasVuelo(em.find(reserva.getRutasVuelo().getClass(), reserva.getRutasVuelo().getNombre()));
	        }
	        // Persistir o mergear según corresponda
	        if (reserva.getIdReserva() == 0) {
	            em.persist(reserva);
	        } else {
	            em.merge(reserva);
	        }
	        em.getTransaction().commit();
	    } catch (Exception ex) {
	        ex.printStackTrace(); // Imprime el error real en consola
	        throw ex;
	    } finally {
	        em.close();
	    }
	}
}