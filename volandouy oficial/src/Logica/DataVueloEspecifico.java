package Logica;

import java.util.Date;

public class DataVueloEspecifico {
	
	private String nombre;
	private Date fecha;
	private int duracion;
	private int maxAsientosTur;
	private int maxAsientosEjec;
	private Date fechaAlta;
	private Ruta ruta;
	
	public DataVueloEspecifico(String nombre, Date fecha, int duracion, int maxAsientosTur, int maxAsientosEjec, Date fechaAlta, Ruta ruta) {
		this.nombre = nombre;
		this.fecha = fecha;
		this.duracion = duracion;
		this.maxAsientosTur = maxAsientosTur;
		this.maxAsientosEjec = maxAsientosEjec;
		this.fechaAlta = fechaAlta;
		this.ruta = ruta;
	}
	
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
	public Ruta getRuta() {
		return ruta;
	}
}
