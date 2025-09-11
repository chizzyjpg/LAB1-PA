package Logica;

import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name = "Reserva")
public class Reserva {
	
	@OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Pasaje> pasajes = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "idCliente", referencedColumnName = "nickname")
	private Cliente cliente;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int idReserva;
	
	@Temporal(TemporalType.DATE)
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
	private VueloEspecifico vueloEspecifico;

	@Transient
	private Ruta rutasVuelo;

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
	public int getIdReserva() {
		return idReserva;
	}
	public Ruta getRutasVuelo() {
		return rutasVuelo;
	}
	public Cliente getCliente() {
		return cliente;
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
	
	public List<Pasaje> getPasajes() {
		return pasajes;
	}
	public VueloEspecifico getVueloEspecifico() {
		return vueloEspecifico;
	}
	
	//Setters
	public void setRutasVuelo(Ruta rutasVuelo) {
		this.rutasVuelo = rutasVuelo;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
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
	
	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}
	public void setVueloEspecifico(VueloEspecifico vueloEspecifico) {
		this.vueloEspecifico = vueloEspecifico;
	}
	@Override public String toString() {
		return "Reserva [fechaReserva=" + fechaReserva + ", tipoAsiento=" + tipoAsiento
				+ ", equipaje=" + equipaje + ", cantEquipajeExtra=" + cantEquipajeExtra + ", costoTotal=" + costoTotal
				+ "]";
	}
}