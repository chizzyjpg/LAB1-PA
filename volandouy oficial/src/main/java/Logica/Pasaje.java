package Logica;

import jakarta.persistence.*;

@Entity
@Table(name = "Pasaje")
public class Pasaje {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int idPasaje;
	
	@Column(name = "Nombre", nullable = false, length = 40)
	private String nombre;
	
	@Column(name = "Apellido", nullable = false, length = 40)
	private String apellido;
	
	@ManyToOne
	@JoinColumn(name = "idReserva")
	private Reserva reserva;
	
	
	protected Pasaje() {}
	
	public Pasaje(String nombre, String apellido) {
		this.nombre = nombre;
		this.apellido = apellido;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public Reserva getReserva() {
		return reserva;
	}
	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	@Override
	public String toString() {
		return idPasaje + " " + nombre + " " + apellido;
	}
}