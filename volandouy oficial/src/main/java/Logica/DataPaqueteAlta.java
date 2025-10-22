package Logica;

import java.util.Date;

/**
 * Clase que representa los datos necesarios para dar de alta un paquete.
 */
public class DataPaqueteAlta {
  private final String nombre;
  private final String descripcion;
  private final int validez; // días
  private final int descuento; // 0..100
  private final Date fechaAlta;

  /**
   *  Constructor de DataPaqueteAlta.
   */
  public DataPaqueteAlta(String nombre, String descripcion, int validez, int descuento,
      Date fechaAlta) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.validez = validez;
    this.descuento = descuento;
    this.fechaAlta = (fechaAlta == null) ? null : new Date(fechaAlta.getTime());
  }

  public String getNombre() {
    return nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public int getValidez() {
    return validez;
  }

  public int getDescuento() {
    return descuento;
  }

  public Date getFechaAlta() {
    return (fechaAlta == null) ? null : new Date(fechaAlta.getTime());
  }
}
