package Logica;

public enum EstadoRuta {
	CONFIRMADA("Confirmada"),
	RECHAZADA("Rechazado"),
	INGRESADA("Ingresada");
	
	private final String descripcion;
	
	EstadoRuta(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return descripcion;
	}
}
