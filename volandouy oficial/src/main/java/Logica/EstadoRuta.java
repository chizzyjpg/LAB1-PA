package Logica;

/**
 * Enum que representa los posibles estados de una ruta.
 */
public enum EstadoRuta {
  CONFIRMADA("Confirmada"), RECHAZADA("Rechazado"), INGRESADA("Ingresada"), FINALIZADA("Finalizada");

  private final String descripcion;

  private EstadoRuta(String descripcion) {
    this.descripcion = descripcion;
  }

  @Override
  public String toString() {
    return descripcion;
  }
}
