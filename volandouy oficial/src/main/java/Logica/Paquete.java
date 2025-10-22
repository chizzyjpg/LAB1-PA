package Logica;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Clase que representa un paquete promocional de rutas.
 */
@Entity
@Table(name = "Paquete")
public class Paquete {

  @Id
  @Column(name = "Nombre", nullable = false, length = 50)
  private String nombre;

  @Column(name = "Descripcion", nullable = false, length = 200)
  private String descripcion;

  @Column(name = "cantRutas", nullable = true)
  private int cantRutas;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipoAsiento", nullable = true)
  private TipoAsiento tipoAsiento;

  @Column(name = "descuento", nullable = false)
  private int descuento;

  @Column(name = "Validez", nullable = false)
  private int validez;

  @Column(name = "costo", nullable = true, precision = 12, scale = 2)
  private BigDecimal costo; // precio final promocional

  @Temporal(TemporalType.DATE)
  @Column(name = "fechaAlta", nullable = true)
  private Date fechaAlta;

  // Usamos una lista de rutas incluidas (solo nombres, sin cupos)
  @ElementCollection(fetch = FetchType.EAGER)
  private Set<String> rutasIncluidas = new LinkedHashSet<>();

  @Column(name = "cuposMaximos", nullable = false)
  private Integer cuposMaximos;

  @Column(name = "cuposDisponibles", nullable = false)
  private Integer cuposDisponibles;
  
  /**
   * Constructor por defecto de Paquete.
   */
  protected Paquete() {
  }

  /**
   * Constructor de Paquete.
   */
  public Paquete(String n, String desc, int cantRut, TipoAsiento tipoAsiento, int dto,
      /* Date fechaComp, */ int val, BigDecimal costo) {
    this.nombre = n;
    this.descripcion = desc;
    this.cantRutas = cantRut;
    this.tipoAsiento = tipoAsiento;
    this.descuento = dto;
    this.validez = val;
    this.costo = costo;
    this.cuposDisponibles = this.cuposMaximos = 0; // inicialmente 0, se seteará luego
  }

  /**
   * Devuelve el nombre del paquete.
   */
  public String getNombre() {
    return nombre;
  }

  /**
   * Devuelve la descripción del paquete.
   */
  public String getDescripcion() {
    return descripcion;
  }

  /**
   * Devuelve la cantidad de rutas incluidas en el paquete.
   */
  public int getCantRutas() {
    return cantRutas;
  }

  /**
   * Devuelve el tipo de asiento del paquete.
   */
  public TipoAsiento getTipoAsiento() {
    return tipoAsiento;
  }

  /**
   * Devuelve el descuento aplicado al paquete.
   */
  public int getDescuento() {
    return descuento;
  }

  /**
   * Devuelve la validez del paquete en días.
   */
  public int getValidez() {
    return validez;
  }

  /**
   * Devuelve el costo final del paquete.
   */
  public BigDecimal getCosto() {
    return costo;
  }

  /**
   * Devuelve la fecha de alta del paquete.
   */
  public Date getFechaAlta() {
    return fechaAlta;
  }

  /**
   * Devuelve las rutas incluidas en el paquete.
   */
  public Set<String> getRutasIncluidas() {
    // solo lectura hacia afuera
    return Collections.unmodifiableSet(rutasIncluidas);
  }

  /**
   * Devuelve la cantidad máxima de cupos del paquete.
   */
  public int getCuposMaximos() {
    return cuposMaximos;
  }

  /**
   * Devuelve la cantidad de cupos disponibles del paquete.
   */
  public int getCuposDisponibles() {
    return cuposDisponibles;
  }

  /**
   * Establece el nombre del paquete.
   */
  public void setNombre(String n) {
    this.nombre = n;
  }

  /**
   * Establece la descripción del paquete.
   */
  public void setDescripcion(String desc) {
    this.descripcion = desc;
  }

  /**
   * Establece la cantidad de rutas del paquete.
   */
  public void setCantRutas(int cantRut) {
    this.cantRutas = cantRut;
  }

  /**
   * Establece el tipo de asiento del paquete.
   */
  public void setTipoAsiento(TipoAsiento tipoAsiento) {
    this.tipoAsiento = tipoAsiento;
  }

  /**
   * Establece el descuento del paquete.
   */
  public void setDescuento(int dto) {
    this.descuento = dto;
  }

  /**
   * Establece la validez del paquete.
   */
  public void setValidez(int val) {
    this.validez = val;
  }

  /**
   * Establece el costo del paquete.
   */
  public void setCosto(BigDecimal costo) {
    this.costo = costo;
  }

  /**
   * Establece la fecha de alta del paquete.
   */
  public void setFechaAlta(Date fecha) {
    this.fechaAlta = fecha;
  }

  /**
   * Establece la cantidad máxima de cupos del paquete.
   */
  public void setCuposMaximos(int cuposMaximos) {
    this.cuposMaximos = cuposMaximos;
  }

  /**
   * Establece la cantidad de cupos disponibles del paquete.
   */
  public void setCuposDisponibles(int cuposDisponibles) {
    this.cuposDisponibles = cuposDisponibles;
  }

  /**
   * Devuelve la representación en texto del paquete.
   */
  @Override
  public String toString() {
    return "Paquete [nombre= " + nombre + ", descripcion=" + descripcion + ", cantRutas="
        + cantRutas + ", tipoAsiento=" + tipoAsiento + ", descuento=" + descuento
        + /*
           * ", fechaCompra=" + fechaCompra +
           */", validez=" + validez + "]";
  }

  /**
   * Agrega una ruta al paquete por su nombre.
   */
  public void addRutaPorNombre(String nombreRuta) {
    String key = canonical(nombreRuta);
    if (key == null || key.isBlank()) {
      throw new IllegalArgumentException("Nombre de ruta inválido");
    }
    if (rutasIncluidas.add(key)) {
      this.cantRutas++; // ++ una vez por ruta distinta
    }
  }

  /**
   * Devuelve el nombre canónico de la ruta.
   */
  private static String canonical(String s) {
    if (s == null) {
      return null;
    }
    return s.trim().toLowerCase(java.util.Locale.ROOT);
  }
}