package Logica;

public enum TipoResultado {
    RUTA("Ruta"), PAQUETE ("Paquete");
    private final String descripcion;

    private TipoResultado(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
