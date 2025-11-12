package Logica;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DataReserva class represents reservation data including details such as.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dataReserva")
@XmlType(propOrder = {"idReserva", "fechaReserva", "tipoAsiento", "equipaje", "cantEquipajeExtra", "costoTotal", "pasajes", "nickCliente" })
public class DataReserva {
  private int idReserva;
  private Date fechaReserva;
  private TipoAsiento tipoAsiento;
  private Equipaje equipaje;
  private int cantEquipajeExtra;
  private Float costoTotal;
  private List<DataPasaje> pasajes;
  private DataCliente nickCliente;

  public DataReserva() {}
  /**
   * Constructor for DataReserva.
   * 
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
   * Overrides toString method to provide a string representation of the reservation.
   */
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String sfd = sdf.format(fechaReserva);

    return "Id " + idReserva + " - " + "Nickname: " + nickCliente.getNickname() + " " + sfd
        + " Tipo Asiento: " + tipoAsiento;
  }

  /**
   * Sets the list of DataPasaje for the reservation.
   * 
   */
  public void setPasajes(List<DataPasaje> pasajes) {
    if (pasajes == null) {
      System.err.println(
          "[DataReserva] Advertencia: Se intentó asignar una lista de pasajes null. Se usará lista vacía.");
      this.pasajes = new ArrayList<>();
    } else if (pasajes.isEmpty()) {
      System.err.println("[DataReserva] Advertencia: Se asignó una lista de pasajes vacía.");
      this.pasajes = new ArrayList<>();
    } else {
      this.pasajes = new ArrayList<>(pasajes);
    }
  }

}
