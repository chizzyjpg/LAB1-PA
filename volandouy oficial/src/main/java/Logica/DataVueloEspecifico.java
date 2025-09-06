package Logica;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;

public class DataVueloEspecifico {
	
	private String nombre;
	private Date fecha;
	private int duracion;
	private int maxAsientosTur;
	private int maxAsientosEjec;
	private Date fechaAlta;
	private DataRuta Druta;
	
	public DataVueloEspecifico(String nombre, Date fecha, int duracion, int maxAsientosTur, int maxAsientosEjec, Date fechaAlta, DataRuta Druta) {
		this.nombre = nombre;
		this.fecha = fecha;
		this.duracion = duracion;
		this.maxAsientosTur = maxAsientosTur;
		this.maxAsientosEjec = maxAsientosEjec;
		this.fechaAlta = fechaAlta;
		this.Druta = Druta;
	}
	
	public DataVueloEspecifico(String nombre, Date fecha, int duracion, int maxAsientosTur, int maxAsientosEjec, Date fechaAlta) {
		this.nombre = nombre;
		this.fecha = fecha;
		this.duracion = duracion;
		this.maxAsientosTur = maxAsientosTur;
		this.maxAsientosEjec = maxAsientosEjec;
		this.fechaAlta = fechaAlta;
		this.Druta = null;
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
	
	public DataRuta getDRuta() {
		return Druta;
	}
	public String toString() {

    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	String sfd = sdf.format(fecha);
		
		return nombre + " - " + sfd;
	}
}