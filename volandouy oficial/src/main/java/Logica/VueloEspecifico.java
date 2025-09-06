package Logica;

import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name = "VueloEspecifico")
public class VueloEspecifico {
	@OneToMany(mappedBy = "vueloEspecifico")
	private Set<Reserva> reservas = new HashSet<>();

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int idVueloEspecifico;
	
	@Column(name = "Nombre", nullable = false, length = 50)
	private String nombre;
	
	@Column(name = "Fecha", nullable = false)
	private Date fecha;
	
	@Column(name = "Duracion", nullable = false)
	private int duracion;
	
	@Column(name = "MaxAsientosTur", nullable = false)
	private int maxAsientosTur;
	
	@Column(name = "MaxAsientosEjec", nullable = false)
	private int maxAsientosEjec;
	
	@Column(name = "fechaAlta", nullable = false)
	private Date fechaAlta;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "idRuta", nullable = false)
    private Ruta ruta;
	
	protected VueloEspecifico() {}
	
	public VueloEspecifico(String n, Date fecha, int dur, int maxTur, int maxEjec, Date fechaAlta, Ruta ruta) {
		this.nombre = n;
		this.fecha = fecha;
		this.duracion = dur;
		this.maxAsientosTur = maxTur;
		this.maxAsientosEjec = maxEjec;
		this.fechaAlta = fechaAlta;
		this.reservas = new HashSet<>();
		this.ruta = ruta;
	}
	
	//Getters
	public String getNombre() {
		return nombre;
	}
	public Date getFecha() {
		return fecha;
	}
	public int getDuracion() {
		return duracion;
	}
	public int getMaxAsientosTur() {
		return maxAsientosTur;
	}
	public int getMaxAsientosEjec() {
		return maxAsientosEjec;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	
	public int getIdVueloEspecifico() {
		return idVueloEspecifico;
	}
	
	public Set<Reserva> getReservas() {
		return reservas;
	}
	public Ruta getRuta() {
        return ruta;
    }
	
	//Setters
	public void setNombre(String n) {
		this.nombre = n;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public void setDuracion(int dur) {
		this.duracion = dur;
	}
	public void setMaxAsientosTur(int maxTur) {
		this.maxAsientosTur = maxTur;
	}
	public void setMaxAsientosEjec(int maxEjec) {
		this.maxAsientosEjec = maxEjec;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public void setReservas(Set<Reserva> reservas) {
        this.reservas = reservas;
    }
	public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }
	@Override public String toString() {
        return "VueloEspecifico [idVueloEspecifico=" + idVueloEspecifico + ", nombre=" + nombre + ", fecha=" + fecha
                + ", duracion=" + duracion + ", maxAsientosTur=" + maxAsientosTur + ", maxAsientosEjec="
                + maxAsientosEjec + ", fechaAlta=" + fechaAlta + ", ruta=" + (ruta != null ? ruta.getIdRuta() : null) + "]";
    }
}