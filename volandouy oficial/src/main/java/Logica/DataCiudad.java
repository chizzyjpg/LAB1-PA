package Logica;

import java.util.Date;

/**
 * Clase que representa los datos de una ciudad.
 */
public class DataCiudad {
  private final String nombre;
  private final String pais;
  private final String nombreAeropuerto;
  private final String descripcionAeropuerto;
  private final Date fechaAlta;
  private final String sitioWeb;

  /**
   * Constructor de DataCiudad.
   *
   * @param nombre Nombre de la ciudad
   * @param pais País de la ciudad
   * @param nombreAeropuerto Nombre del aeropuerto
   * @param descripcionAeropuerto Descripción del aeropuerto
   * @param fechaAlta Fecha de alta
   * @param sitioWeb Sitio web del aeropuerto
   */
  public DataCiudad(String nombre, String pais, String nombreAeropuerto,
      String descripcionAeropuerto, Date fechaAlta, String sitioWeb) {
    this.nombre = nombre;
    this.pais = pais;
    this.nombreAeropuerto = nombreAeropuerto;
    this.descripcionAeropuerto = descripcionAeropuerto;
    this.fechaAlta = fechaAlta;
    this.sitioWeb = sitioWeb;
  }

  public String getNombre() {
    return nombre;
  }

  public String getPais() {
    return pais;
  }

  public String getNombreAeropuerto() {
    return nombreAeropuerto;
  }

  public String getDescripcionAeropuerto() {
    return descripcionAeropuerto;
  }

  public Date getFechaAlta() {
    return fechaAlta;
  }

  public String getSitioWeb() {
    return sitioWeb;
  }

  @Override
  public String toString() {
    return nombre + pais; // Solo muestra el nombre de la ciudad
  }
}