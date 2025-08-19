package Logica;

import java.util.Date;

public class Paquetes {

	private String nombre;
	private String descripcion;
	private int cantRutas;
	private Tipo_Asiento tipoAsiento;
	private int descuento;
	private Date fechaCompra;
	private int validez;
	
	
	public Paquetes(String n, String desc, int cantRut,Tipo_Asiento tipoAsiento, int dto, Date fechaComp, int val) {
		this.nombre = n;
		this.descripcion = desc;
		this.cantRutas = cantRut;
		this.tipoAsiento = tipoAsiento;
		this.descuento = dto;
		this.fechaCompra = fechaComp;
		this.validez = val;
	}
	
	//Getters
	public String getNombre() {
		return nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public int getCantRutas() {
		return cantRutas;
	}
	public int getDescuento() {
		return descuento;
	}
	public Date getFechaCompra() {
		return fechaCompra;
	}
	public int getValidez() {
		return validez;
	}
	/*
	public double getCostoAsociado() {
	    double costoBasePorRuta;

	    switch (tipoAsiento) {
	        case ECONOMICO:
	            costoBasePorRuta = 100; 
	            break;
	        case EJECUTIVO:
	            costoBasePorRuta = 200; 
	            break;
	        case PRIMERA:
	            costoBasePorRuta = 300; 
	            break;
	        default:
	            costoBasePorRuta = 0;
	    }

	    double costoTotal = cantRutas * costoBasePorRuta;
	    double costoConDescuento = costoTotal - (costoTotal * descuento / 100.0);

	    return costoConDescuento;
	}
	*/
	//Setters
	public void setNombre(String n) {
		this.nombre = n;
	}
	public void setDescripcion(String desc) {
		this.descripcion = desc;
	}
	public void setCantRutas(int cantRut) {
		this.cantRutas = cantRut;
	}
	public void setDescuento(int dto) {
		this.descuento = dto;
	}
	public void setFechaCompra(Date fechaComp) {
		this.fechaCompra = fechaComp;
	}
	public void setValidez(int val) {
		this.validez = val;
	}
}
