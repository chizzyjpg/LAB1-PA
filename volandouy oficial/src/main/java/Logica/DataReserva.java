package Logica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa los datos de una reserva.
 */
public class DataReserva {
  private int idReserva;
  private Date fechaReserva;
  private TipoAsiento tipoAsiento;
  private Equipaje equipaje;
  private int cantEquipajeExtra;
  private Float costoTotal;
  private List<DataPasaje> pasajes;
  private DataCliente nickCliente;

  /**
   * Constructor de DataReserva.
   */
  public DataReserva(int idReserva, Date fechaReserva, TipoAsiento tipoAsiento, Equipaje equipaje,
      int cantEquipajeExtra, Float costoTotal, DataCliente nickCliente) {
    this.idReserva = idReserva;
    this.fechaReserva = fechaReserva;
    this.tipoAsiento = tipoAsiento;
    this.equipaje = equipaje;
    this.cantEquipajeExtra = cantEquipajeExtra;
    this.costoTotal = costoTotal;
    this.nickCliente = nickCliente;
    this.pasajes = new ArrayList<>();
  }

  // Getters
  public int getIdReserva() {
    return idReserva;
  }

  public Date getFechaReserva() {
    return fechaReserva;
  }

  public TipoAsiento getTipoAsiento() {
    return tipoAsiento;
  }

  public Equipaje getEquipaje() {
    return equipaje;
  }

  public int getCantEquipajeExtra() {
    return cantEquipajeExtra;
  }

  public Float getCostoTotal() {
    return costoTotal;
  }

  public List<DataPasaje> getPasajes() {
    return pasajes;
  }

  public DataCliente getNickCliente() {
    return nickCliente;
  }

  /**
   * Representación en cadena de la reserva.
   */
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String sfd = sdf.format(fechaReserva);

    return "Id " + idReserva + " - " + "Nickname: " + nickCliente.getNickname() + " " + sfd
        + " Tipo Asiento: " + tipoAsiento;
  }

  /** Establece la lista de pasajes asociados a la reserva.
   * Si la lista es null o vacía, se asigna una lista vacía y se muestra una advertencia.
   */
  public void setPasajes(List<DataPasaje> pasajes) {
    if (pasajes == null) {
      System.err.println(
          "[DataReserva] Advertencia: Se intentó asignar"
          + " una lista de pasajes null. Se usará lista vacía.");
      this.pasajes = new ArrayList<>();
    } else if (pasajes.isEmpty()) {
      System.err.println("[DataReserva] Advertencia: Se asignó una lista de pasajes vacía.");
      this.pasajes = new ArrayList<>();
    } else {
      this.pasajes = new ArrayList<>(pasajes);
    }
  }

}