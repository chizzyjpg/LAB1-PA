package Logica;

import java.util.Date;

public class Reserva {
	
	private Date fechaReserva;
	private TipoAsiento tipoAsiento;
	private Equipaje equipaje;
	private int cantEquipajeExtra;
	private Float costoTotal;
	
	
	public Date getFechaReserva() {
		return fechaReserva;
	}
	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
		
		
	}
	public TipoAsiento getTipoAsiento() {
		return tipoAsiento;
	}
	public void setTipoAsiento(TipoAsiento tipoAsiento) {
		this.tipoAsiento = tipoAsiento;
	}
	
	
	public Equipaje getEquipaje() {
		return equipaje;
	}
	public void setEquipaje(Equipaje equipaje) {
		this.equipaje = equipaje;
	}
	
	
	public int getCantEquipajeExtra() {
		return cantEquipajeExtra;
	}
	public void setCantEquipajeExtra(int cantEquipajeExtra) {
		this.cantEquipajeExtra = cantEquipajeExtra;
	}
	
	
	public Float getCostoTotal() {
		return costoTotal;
	}
	public void setCostoTotal(Float costoTotal) {
		this.costoTotal = costoTotal;
	}
	
	
	

}
