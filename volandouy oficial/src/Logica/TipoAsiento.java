package Logica;

public enum TipoAsiento {
	TURISTA("Turista"),
	EJECUTIVO("Ejecutivo");
	
	private final String descripcion;
	
	private TipoAsiento(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return descripcion;
	}

}
