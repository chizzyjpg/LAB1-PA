package Logica;


public enum TipoDocumento {
	CEDULA("Cedula"),
	PASAPORTE("Pasaporte"),
	OTRO("Otro");
	
	private final String descripcion;
	
	private TipoDocumento(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return descripcion;
	}

}
