package Logica;

public class Pasajero {

	private String nombre;
	private String apellido;
	
	public Pasajero(String n, String ap) {
		this.nombre = n;
		this.apellido = ap;
	}
	
	//Getters
	public String getNombre() {
		return nombre;
	}
	public String getApellido() {
		return apellido;
	}
	
	//Setters
	public void setNombre(String n) {
		this.nombre = n;
	}
	
	public void setApellido(String ap) {
		this.apellido = ap;
	}
	
	
	
}
