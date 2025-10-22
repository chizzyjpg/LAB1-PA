package Logica;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que representa un vuelo específico en el sistema de gestión de vuelos.
 */
@Entity
@Table(name = "VueloEspecifico")
public class VueloEspecifico {
  @OneToMany(mappedBy = "vueloEspecifico", fetch = FetchType.EAGER)
  private Set<Reserva> reservas = new HashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int idVueloEspecifico;

  @Column(name = "Nombre", nullable = false, length = 50)
  private String nombre;

  @Temporal(TemporalType.DATE)
  @Column(name = "Fecha", nullable = false)
  private Date fecha;

  @Column(name = "Duracion", nullable = false)
  private int duracion;

  @Column(name = "MaxAsientosTur", nullable = false)
  private int maxAsientosTur;

  @Column(name = "MaxAsientosEjec", nullable = false)
  private int maxAsientosEjec;

  @Column(name = "fechaAlta", nullable = false)
  private Date fechaAlta;

  @Temporal(TemporalType.DATE)
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "idRuta", nullable = false)
  private Ruta ruta;
  
  /**
   * Constructor por defecto requerido por JPA.
   */
  protected VueloEspecifico() {
  }

  /**
   * Constructor parametrizado para crear un vuelo específico.
   */
  public VueloEspecifico(String n, Date fecha, int dur, int maxTur, int maxEjec, Date fechaAlta,
      Ruta ruta) {
    this.nombre = n;
    this.fecha = fecha;
    this.duracion = dur;
    this.maxAsientosTur = maxTur;
    this.maxAsientosEjec = maxEjec;
    this.fechaAlta = fechaAlta;
    this.reservas = new HashSet<>();
    this.ruta = ruta;
  }

  // Getters
  public String getNombre() {
    return nombre;
  }

  public Date getFecha() {
    return fecha;
  }

  public int getDuracion() {
    return duracion;
  }

  public int getMaxAsientosTur() {
    return maxAsientosTur;
  }

  public int getMaxAsientosEjec() {
    return maxAsientosEjec;
  }

  public Date getFechaAlta() {
    return fechaAlta;
  }

  public int getIdVueloEspecifico() {
    return idVueloEspecifico;
  }

  public Set<Reserva> getReservas() {
    return reservas;
  }

  public Ruta getRuta() {
    return ruta;
  }

  // Setters
  public void setNombre(String n) {
    this.nombre = n;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public void setDuracion(int dur) {
    this.duracion = dur;
  }

  public void setMaxAsientosTur(int maxTur) {
    this.maxAsientosTur = maxTur;
  }

  public void setMaxAsientosEjec(int maxEjec) {
    this.maxAsientosEjec = maxEjec;
  }

  public void setFechaAlta(Date fechaAlta) {
    this.fechaAlta = fechaAlta;
  }

  public void setReservas(Set<Reserva> reservas) {
    this.reservas = reservas;
  }

  public void setRuta(Ruta ruta) {
    this.ruta = ruta;
  }

  @Override
  public String toString() {
    return "VueloEspecifico [idVueloEspecifico=" + idVueloEspecifico + ", nombre=" + nombre
        + ", fecha=" + fecha + ", duracion=" + duracion + ", maxAsientosTur=" + maxAsientosTur
        + ", maxAsientosEjec=" + maxAsientosEjec + ", fechaAlta=" + fechaAlta + ", ruta="
        + (ruta != null ? ruta.getIdRuta() : null) + "]";
  }
}