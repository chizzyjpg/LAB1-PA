package Logica;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Clase que representa una categoría de un producto.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dataCategoria")
@XmlType(propOrder = { "nombre" })
public class DataCategoria {
  private String nombre;

  public DataCategoria() {}
  /**
   * Constructor de la clase DataCategoria.
   * 
   */
  public DataCategoria(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

}