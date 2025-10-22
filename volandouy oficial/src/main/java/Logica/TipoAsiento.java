package Logica;

public enum TipoAsiento {
	TURISTA("Turista"),
	EJECUTIVO("Ejecutivo");
	
	private final String descripcion;
	
	TipoAsiento(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return descripcion;
	}

}
