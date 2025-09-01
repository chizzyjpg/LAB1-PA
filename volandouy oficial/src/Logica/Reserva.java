package Logica;

import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name = "Reserva")
public class Reserva {
	
	@ManyToMany
	private List<Pasaje> pasajes = new ArrayList<>();
	
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
	private Paquete paquete;	
	
	protected Reserva() {}	
	
	public Reserva(Date fechaReserva, TipoAsiento tipoAsiento, Equipaje equipaje, int cantEquipajeExtra, Float costoTotal, Cliente nickCliente) {
		this.fechaReserva = fechaReserva;
		this.tipoAsiento = tipoAsiento;
		this.equipaje = equipaje;
		this.cantEquipajeExtra = cantEquipajeExtra;
		this.costoTotal = costoTotal;
		this.cliente = nickCliente;
		this.pasajes = new ArrayList<>();
	}
	
	//Getters
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
	
	public int getIdReserva() {
		return idReserva;
	}
	
	public List<Pasaje> getPasajes() {
		return pasajes;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	//Setters
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
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}
	
	@Override public String toString() {
		return "Reserva [idReserva=" + idReserva + ", fechaReserva=" + fechaReserva + ", tipoAsiento=" + tipoAsiento
				+ ", equipaje=" + equipaje + ", cantEquipajeExtra=" + cantEquipajeExtra + ", costoTotal=" + costoTotal
				+ "]";
	}
	
}
