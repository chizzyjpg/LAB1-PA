package Logica;


public enum TipoDocumento {
	CEDULA("Cedula"),
	PASAPORTE("Pasaporte"),
	OTRO("Otro");
	
	private final String descripcion;
	
	TipoDocumento(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return descripcion;
	}

}
