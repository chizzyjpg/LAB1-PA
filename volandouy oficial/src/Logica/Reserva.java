package Logica;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "Reserva")
public class Reserva {
	
	@ManyToOne
	@JoinColumn(name = "idCliente", referencedColumnName = "nickname")
	private Cliente cliente;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int idReserva;
	
	@Column(name = "fechaReserva", nullable = false)
	private Date fechaReserva;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoAsiento", nullable = false)
	private TipoAsiento tipoAsiento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "Equipaje", nullable = false)
	private Equipaje equipaje;
	
	@Column(name = "cantEquipajeExtra", nullable = false)
	private int cantEquipajeExtra;
	
	@Column(name = "costoTotal", nullable = false)
	private Float costoTotal;
	
	
	@ManyToOne
	private Cliente nickname;
	
	@ManyToOne
	private VueloEspecifico vueloEspecifico;
	
	@ManyToOne
	private Paquetes paquete;	
	
	
	private String aerolinea;
	private String rutasVuelo;
	private int cantPasajes;
	//private Pasajero pasajero;
	
	protected Reserva() {}	
	
	public Reserva(String aerolinea, String rutasVuelo, Cliente cliente, int cantPasajes/*,Pasajero pasajero*/,Date fechaReserva, TipoAsiento tipoAsiento, Equipaje equipaje, int cantEquipajeExtra, Float costoTotal){
		this.aerolinea = aerolinea;
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
	
	//Getters
	public int getIdReserva() {
		return idReserva;
	}
	public String getAerolinea() {
		return aerolinea;
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
	
	//Setters
	public void setAerolinea(String aerolinea) {
		this.aerolinea = aerolinea;
	}
	public void setRutasVuelo(String rutasVuelo) {
		this.rutasVuelo = rutasVuelo;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public void setCantPasajes(int cantPasajes) {
		this.cantPasajes = cantPasajes;
	}
	/*public void setPasajero(Pasajero pasajero) {
		this.pasajero = pasajero;
	}*/
	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;	
	}
	
	public void setTipoAsiento(TipoAsiento tipoAsiento) {
		this.tipoAsiento = tipoAsiento;
	}
	
	public void setEquipaje(Equipaje equipaje) {
		this.equipaje = equipaje;
	}
	
	public void setCantEquipajeExtra(int cantEquipajeExtra) {
		this.cantEquipajeExtra = cantEquipajeExtra;
	}
	public void setCostoTotal(Float costoTotal) {
		this.costoTotal = costoTotal;
	}
	
	
	@Override public String toString() {
		return "Reserva [fechaReserva=" + fechaReserva + ", tipoAsiento=" + tipoAsiento
				+ ", equipaje=" + equipaje + ", cantEquipajeExtra=" + cantEquipajeExtra + ", costoTotal=" + costoTotal
				+ "]";
	}
	
}
