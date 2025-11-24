package Logica;

public enum TipoUsuario {
    CLIENTE("Cliente"), AEROLINEA ("Aerolinea");
    private final String descripcion;

    private TipoUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}


