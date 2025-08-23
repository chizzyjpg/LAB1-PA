package Logica;

import java.util.Date;

public class DataReserva {
	private String Aerolinea;
	private String rutasVuelo;
	private Cliente cliente;
	private int cantPasajes;
	//private Pasajero pasajero;
	private Date fechaReserva;
	private TipoAsiento tipoAsiento;
	private Equipaje equipaje;
	private int cantEquipajeExtra;
	private Float costoTotal;
	
	public DataReserva(String aerolinea, String rutasVuelo, Cliente cliente, int cantPasajes/*,Pasajero pasajero*/,Date fechaReserva, TipoAsiento tipoAsiento, Equipaje equipaje, int cantEquipajeExtra, Float costoTotal) {
		this.Aerolinea = aerolinea;
		this.rutasVuelo = rutasVuelo;
		this.cliente = cliente;
		this.cantPasajes = cantPasajes;
		//this.pasajero = pasajero;
		this.fechaReserva = fechaReserva;
		this.tipoAsiento = tipoAsiento;
		this.equipaje = equipaje;
		this.cantEquipajeExtra = cantEquipajeExtra;
		this.costoTotal = costoTotal;
	}
	
	public String getAerolinea() {
		return Aerolinea;
	}
	
	public String getRutasVuelo() {
		return rutasVuelo;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public int getCantPasajes() {
		return cantPasajes;
	}
	
	/*public Pasajero getPasajero() {
		return pasajero;
	}*/
	
	
	public Date getFechaReserva() {
		return fechaReserva;
	}
	
	public TipoAsiento getTipoAsiento() {
		return tipoAsiento;
	}
	
	public Equipaje getEquipaje() {
		return equipaje;
	}
	
	public int getCantEquipajeExtra() {
		return cantEquipajeExtra;
	}
	
	public Float getCostoTotal() {
		return costoTotal;
	}
	
}
