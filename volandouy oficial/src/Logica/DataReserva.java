package Logica;

import java.util.Date;

public class DataReserva {
	private int idReserva;
	private Date fechaReserva;
	private TipoAsiento tipoAsiento;
	private Equipaje equipaje;
	private int cantEquipajeExtra;
	private Float costoTotal;
	
	public DataReserva(int idReserva,Date fechaReserva, TipoAsiento tipoAsiento, Equipaje equipaje, int cantEquipajeExtra, Float costoTotal) {
		this.idReserva = idReserva;
		this.fechaReserva = fechaReserva;
		this.tipoAsiento = tipoAsiento;
		this.equipaje = equipaje;
		this.cantEquipajeExtra = cantEquipajeExtra;
		this.costoTotal = costoTotal;
	}
	
	
	public int getIdReserva() {
		return idReserva;
	}
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
