package Logica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cliente extends Usuario{

	private String apellido;
	private Date fechaNac;
	private String nacionalidad;
	private TipoDocumento tipoDocumento;
	private String numDocumento;
	
	private List<Reserva> reservas;
	
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
}

