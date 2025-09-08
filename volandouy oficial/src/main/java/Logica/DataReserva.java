package Logica;

import java.text.SimpleDateFormat;
import java.util.*;

public class DataReserva {
	private int idReserva;
	private Date fechaReserva;
	private TipoAsiento tipoAsiento;
	private Equipaje equipaje;
	private int cantEquipajeExtra;
	private Float costoTotal;
	private List<DataPasaje> pasajes;
	private DataCliente nickCliente;
	
	public DataReserva(int idReserva, Date fechaReserva, TipoAsiento tipoAsiento, Equipaje equipaje, int cantEquipajeExtra, Float costoTotal, DataCliente nickCliente) {
		this.idReserva = idReserva;
		this.fechaReserva = fechaReserva;
		this.tipoAsiento = tipoAsiento;
		this.equipaje = equipaje;
		this.cantEquipajeExtra = cantEquipajeExtra;
		this.costoTotal = costoTotal;
		this.nickCliente = nickCliente;
		this.pasajes = new ArrayList<>();
	}
	

	//Getters
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
	
	public List<DataPasaje> getPasajes() {
		return pasajes;
	}
	
	public DataCliente getNickCliente() {
		return nickCliente;
	}
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	String sfd = sdf.format(fechaReserva);
		
		return "Id " + idReserva + " - " + "Nickname: " + nickCliente.getNickname() + " " + sfd + " Tipo Asiento: " + tipoAsiento;
	}
	
	 public void setPasajes(List<DataPasaje> pasajes) {
	        if (pasajes == null) {
	            System.err.println("[DataReserva] Advertencia: Se intentó asignar una lista de pasajes null. Se usará lista vacía.");
	            this.pasajes = new ArrayList<>();
	        } else if (pasajes.isEmpty()) {
	            System.err.println("[DataReserva] Advertencia: Se asignó una lista de pasajes vacía.");
	            this.pasajes = new ArrayList<>();
	        } else {
	            this.pasajes = new ArrayList<>(pasajes);
	        }
	    }

}
