package Logica;

import java.util.Date;

public class VueloEspecifico {

	private String nombre;
	private Date fecha;
	private int duracion;
	private int maxAsientosTur;
	private int maxAsientosEjec;
	private Date fechaAlta;
	
	public VueloEspecifico(String n, Date fecha, int dur, int maxTur, int maxEjec, Date fechaAlta) {
		this.nombre = n;
		this.fecha = fecha;
		this.duracion = dur;
		this.maxAsientosTur = maxTur;
		this.maxAsientosEjec = maxEjec;
		this.fechaAlta = fechaAlta;
	}
	
	//Getters
	public String getNombre() {
		return nombre;
	}
	public Date getFecha() {
		return fecha;
	}
	public int getDuracion() {
		return duracion;
	}
	public int getMaxAsientosTur() {
		return maxAsientosTur;
	}
	public int getMaxAsientosEjec() {
		return maxAsientosEjec;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	
	//Setters
	public void setNombre(String n) {
		this.nombre = n;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public void setDuracion(int dur) {
		this.duracion = dur;
	}
	public void setMaxAsientosTur(int maxTur) {
		this.maxAsientosTur = maxTur;
	}
	public void setMaxAsientosEjec(int maxEjec) {
		this.maxAsientosEjec = maxEjec;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
}
