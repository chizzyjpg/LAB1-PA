package Logica;

import java.util.Collection;
import java.util.Date;
import jakarta.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "Cliente")
@PrimaryKeyJoinColumn(name = "nickname")
public class Cliente extends Usuario{
	
	@OneToMany(mappedBy = "reserva")
	private Collection<Reserva> reservas;
	
	@Column(name = "Apellido", nullable = false, length = 40)
	private String apellido;
	
	@Column(name = "fechaNacimiento", nullable = false)
	private Date fechaNac;
	
	@Column(name = "Nacionalidad", nullable = false, length = 40)
	private String nacionalidad;
	
	@Column(name = "TipoDocumento", nullable = false)
	private TipoDocumento tipoDocumento;
	
	@Column(name = "NumDocumento", nullable = false, length = 20)
	private String numDocumento;
	
	private List<Reserva> reservas;

	protected Cliente() {}
	
	public Cliente(String n, String nick, String email, String ap, Date fechaNac, String nac, TipoDocumento tipoDoc, String numDoc) {
		super(n, nick, email);
		this.apellido = ap;
		this.fechaNac = fechaNac;
		this.nacionalidad = nac;
		this.tipoDocumento = tipoDoc;
		this.numDocumento = numDoc;
		this.reservas = new ArrayList<>();
	}

	//Getters
	public String getApellido() {
		return apellido;
	}
	public Date getFechaNac() {
		return fechaNac;
	}
	public String getNacionalidad() {
		return nacionalidad;
	}
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}
	public String getNumDocumento() {
		return numDocumento;
	}
	public List<Reserva> getReservas(){
		return reservas;
	}
	
	//Setters
	public void setApellido(String ap) {
		this.apellido = ap;
	}
	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}
	public void setNacionalidad(String nac) {
		this.nacionalidad = nac;
	}
	public void setTipoDocumento(TipoDocumento tipoDoc) {
		this.tipoDocumento = tipoDoc;
	}
	public void setNumDocumento(String numDoc) {
		this.numDocumento = numDoc;
	}
	
	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	@Override public String toString() {
		return "Cliente [nickname=" + getNickname() + ", nombre=" + getNombre() + ", email=" + getEmail() +
				", apellido=" + apellido + ", fechaNac=" + fechaNac + ", nacionalidad=" + nacionalidad +
				", tipoDocumento=" + tipoDocumento + ", numDocumento=" + numDocumento + "]";
	}
}

