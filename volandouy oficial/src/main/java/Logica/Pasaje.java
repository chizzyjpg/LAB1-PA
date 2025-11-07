package Logica;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa un pasaje de vuelo.
 */
@Entity
@Table(name = "Pasaje")
public class Pasaje {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int idPasaje;

  @Column(name = "Nombre", nullable = false, length = 40)
  private String nombre;

  @Column(name = "Apellido", nullable = false, length = 40)
  private String apellido;

  @ManyToOne
  @JoinColumn(name = "idReserva")
  private Reserva reserva;

  /**
   * Constructor por defecto.
   */
  protected Pasaje() {
  }

  /**
   * Constructor con parámetros.
   * 
   */
  public Pasaje(String nombre, String apellido) {
    this.nombre = nombre;
    this.apellido = apellido;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public Reserva getReserva() {
    return reserva;
  }

  public void setReserva(Reserva reserva) {
    this.reserva = reserva;
  }

  @Override
  public String toString() {
    return idPasaje + " " + nombre + " " + apellido;
  }
}