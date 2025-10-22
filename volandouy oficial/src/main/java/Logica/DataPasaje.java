package Logica;

/**
 * DataPasaje class represents a data structure for storing passenger information.
 */
public class DataPasaje {
  private String nombre;
  private String apellido;

  /**
   * Constructor to initialize DataPasaje with given name and surname.
   *
   */
  public DataPasaje(String nombre, String apellido) {
    this.nombre = nombre;
    this.apellido = apellido;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  /**
   * Returns a string representation of the DataPasaje object.
   */
  public String toString() {
    return "Nombre: " + nombre + " Apellido: " + apellido;
  }
}