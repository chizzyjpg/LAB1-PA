package BD;

import Logica.Cliente;
import Logica.JPAUtil;
import Logica.Reserva;
import Logica.VueloEspecifico;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Reserva.
 */
public class ReservaService {
  /**
   * Registra una nueva reserva en la base de datos.
   *
   * 
   */
  public void registrarReserva(Reserva reserva) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      // Validar que las entidades asociadas no sean null
      if (reserva.getCliente() == null || reserva.getCliente().getNickname() == null) {
        throw new IllegalArgumentException("La reserva debe tener un cliente con nickname válido");
      }
      if (reserva.getVueloEspecifico() == null) {
        throw new IllegalArgumentException(
            "La reserva debe tener un vuelo específico con ID válido");
      }
      // Buscar entidades gestionadas por la sesión
      Cliente cliente = em.find(Cliente.class, reserva.getCliente().getNickname());
      VueloEspecifico vuelo = em.find(VueloEspecifico.class,
          reserva.getVueloEspecifico().getIdVueloEspecifico());
      if (cliente == null) {
        throw new IllegalArgumentException("No se encontró el cliente en la base de datos");
      }
      if (vuelo == null) {
        throw new IllegalArgumentException(
            "No se encontró el vuelo específico en la base de datos");
      }
      reserva.setCliente(cliente);
      reserva.setVueloEspecifico(vuelo);
      // Si la reserva tiene ruta, setear la gestionada
      if (reserva.getRutasVuelo() != null) {
        reserva.setRutasVuelo(
            em.find(reserva.getRutasVuelo().getClass(), reserva.getRutasVuelo().getNombre()));
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
  public Reserva buscarReservaPorId(int idReserva) {
      System.out.println("[buscarReservaPorId] Buscando reserva con id: " + idReserva);
      EntityManager em = JPAUtil.getEntityManager();
      try {
          Reserva reserva = em.find(Reserva.class, idReserva);
          if (reserva != null) {
              System.out.println("[buscarReservaPorId] Reserva encontrada: " + reserva.toString());
              return reserva;
          } else {
              System.out.println("[buscarReservaPorId] No se encontró ninguna reserva con ID: " + idReserva);
          }
      } catch (Exception ex) {
          ex.printStackTrace();
      } finally {
          em.close();
      }
      return null;
  }

    public List<Reserva> listarReservasPendientesCheckIn(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Reserva> reservas = null;
        try {
            reservas = em.createQuery(
                    "SELECT r FROM Reserva r WHERE r.cliente.nickname = :nickname AND r.checkInRealizado = false",
                    Reserva.class)
                    .setParameter("nickname", cliente.getNickname())
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
        return reservas;
    }

    public void actualizarReserva(Reserva reserva) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            System.out.println("[actualizarReserva] Antes de merge: " + reserva);
            // Obtener el vuelo gestionado
            VueloEspecifico vuelo = null;
            if (reserva.getVueloEspecifico() != null) {
                vuelo = em.find(VueloEspecifico.class, reserva.getVueloEspecifico().getIdVueloEspecifico());
                if (vuelo != null) {
                    // Actualizar los campos de asientos directamente sobre la instancia gestionada
                    vuelo.setMaxAsientosTur(reserva.getVueloEspecifico().getMaxAsientosTur());
                    vuelo.setMaxAsientosEjec(reserva.getVueloEspecifico().getMaxAsientosEjec());
                    System.out.println("[actualizarReserva] VueloEspecifico gestionado actualizado: " + vuelo);
                }
            }
            Reserva managedReserva = em.merge(reserva);
            em.flush();
            em.refresh(managedReserva);
            System.out.println("[actualizarReserva] Después de merge y flush: " + managedReserva);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }
}