package Logica;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * DataPasaje class represents a data structure for storing passenger information.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dataPasaje")
@XmlType(propOrder = {"nombre", "apellido", "asientoAsignado" })
public class DataPasaje {
  private String nombre;
  private String apellido;
  private String asientoAsignado;

  public DataPasaje() {}
  /**
   * Constructor to initialize DataPasaje with given name and surname.
   *
   */
  public DataPasaje(String nombre, String apellido, String asientoAsignado) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.asientoAsignado = asientoAsignado;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public String getAsientoAsignado() {return asientoAsignado;}

  /**
   * Returns a string representation of the DataPasaje object.
   */
  public String toString() {
    return "Nombre: " + nombre + " Apellido: " + apellido;
  }
}