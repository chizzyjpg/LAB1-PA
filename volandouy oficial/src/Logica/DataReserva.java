package Logica;

import java.util.Date;

public class DataReserva {
	private String Aerolinea;
	private Ruta rutasVuelo;
	private Cliente cliente;
	private int cantPasajes;
	private String nomPasajero;
	private String apePasajero;
	//private Pasajero pasajero;
	private Date fechaReserva;
	private TipoAsiento tipoAsiento;
	private Equipaje equipaje;
	private int cantEquipajeExtra;
	private Float costoTotal;
	
	public DataReserva(String aerolinea, Ruta rutasVuelo, Cliente cliente, int cantPasajes, String nomPasajero, String apePasajero/*,Pasajero pasajero*/,Date fechaReserva, TipoAsiento tipoAsiento, Equipaje equipaje, int cantEquipajeExtra, Float costoTotal) {
		this.Aerolinea = aerolinea;
		this.rutasVuelo = rutasVuelo;
		this.cliente = cliente;
		this.cantPasajes = cantPasajes;
		this.nomPasajero = nomPasajero;
		this.apePasajero = apePasajero;
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
	
	public Ruta getRutasVuelo() {
		return rutasVuelo;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public int getCantPasajes() {
		return cantPasajes;
	}
	public String getNomPasajero() {
		return nomPasajero;
	}
	public String getApePasajero() {
		return apePasajero;
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
