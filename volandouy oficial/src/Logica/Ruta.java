package Logica;

import java.util.Date;

public class Ruta {

	private String nombre;
	private String descripcion;
	private String ciudadOrigen;
	private String ciudadDestino;
	private int hora;
	private Date fechaAlta;
	private int costoBase;
	private int costoEquipajeExtra;
	private String categorias;
	
	public Ruta(String n, String desc, String ciudadOr, String ciudadDe, int hora, Date fechaAlta, int costoBase, int costoEquipajeExtra, String cat) {
		this.nombre = n;
		this.descripcion = desc;
		this.ciudadOrigen = ciudadOr;
		this.ciudadDestino = ciudadDe;
		this.hora = hora;
		this.fechaAlta = fechaAlta;
		this.costoBase = costoBase;
		this.costoEquipajeExtra = costoEquipajeExtra;
		this.categorias = cat;
	}
	
	//Getters
	public String getNombre() {
		return nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public String getCiudadOrigen() {
		return ciudadOrigen;
	}
	public String getCiudadDestino() {
		return ciudadDestino;
	}
	public int getHora() {
		return hora;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public int getCostoBase() {
		return costoBase;
	}
	public int getCostoEquipajeExtra() {
		return costoEquipajeExtra;
	}
	public String getCategorias() {
		return categorias;
	}
	
	//Setters
	public void setNombre(String n) {
		this.nombre = n;
	}
	public void setDescripcion(String desc) {
		this.descripcion = desc;
	}
	public void setCiudadOrigen(String ciudadO) {
		this.ciudadOrigen = ciudadO;
	}
	public void setCiudadDestino(String ciudadD) {
		this.ciudadDestino = ciudadD;
	}
	public void setHora(int hora) {
		this.hora = hora;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public void setCostoBase(int costoBase) {
		this.costoBase = costoBase;
	}
	public void setCostoEquipajeExtra(int costoEquipajeExtra) {
		this.costoEquipajeExtra = costoEquipajeExtra;
	}
	public void setCategorias(String cat) {
		this.categorias = cat;
	}
}
