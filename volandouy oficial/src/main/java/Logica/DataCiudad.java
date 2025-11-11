package Logica;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.Date;

/**
 * Clase que representa los datos de una ciudad, incluyendo información sobre su aeropuerto.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dataCiudad")
@XmlType(propOrder = {"nombre", "pais", "nombreAeropuerto", "descripcionAeropuerto", "fechaAlta", "sitioWeb"})
public class DataCiudad {
  private String nombre;
  private String pais;
  private String nombreAeropuerto;
  private String descripcionAeropuerto;
  private Date fechaAlta;
  private String sitioWeb;


  /**
   * Constructor de la clase DataCiudad.
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

  // Constructor por defecto requerido por JAXB
  public DataCiudad() {
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