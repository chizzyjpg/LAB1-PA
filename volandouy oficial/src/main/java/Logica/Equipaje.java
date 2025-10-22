package Logica;

/**
 * Enum que representa los tipos de equipaje permitidos.
 */
public enum Equipaje {
  BOLSO("Bolso"), MOCHILAYCARRYON("Mochila y Carry-on");

  private final String descripcion;

  private Equipaje(String descripcion) {
    this.descripcion = descripcion;
  }

  @Override
  public String toString() {
    return descripcion;
  }
}
