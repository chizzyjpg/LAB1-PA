package Logica;

import java.util.Collection;
import jakarta.persistence.*;

@Entity
@Table(name = "Aerolinea")
@PrimaryKeyJoinColumn(name = "nickname")

public class Aerolinea extends Usuario {
	
	@ManyToMany
	private Collection<Rutas> rutas;
	
	@Column(name = "descGeneral", nullable = false, length = 200)
	private String descGeneral;
	
	@Column(name = "linkWeb", nullable = false, length = 50)
	private String linkWeb;
	
	protected Aerolinea() {}
	
	public Aerolinea(String n, String nick, String email, String descGeneral, String linkWeb) {
	super(n, nick, email);
	this.setLinkWeb(linkWeb);
	this.setDescGeneral(descGeneral);
	}

	//Getters
	public String getDescGeneral() {
		return descGeneral;
	}


	public String getLinkWeb() {
		return linkWeb;
	}

	//Setters
	
	public void setLinkWeb(String linkWeb) {
		this.linkWeb = linkWeb;
	}
	
	public void setDescGeneral(String descGeneral) {
		this.descGeneral = descGeneral;
	}
	
	
		//Metodos
	
	/*
	@Override
    public String toString() {
        return this.getNombre(); // útil para mostrar en el ComboBox
    }
	*/
	@Override public String toString() {
		return "Aerolinea [nickname=" + this.getNickname() + ","
				+ " nombre=" + this.getNombre() + ","
				+ " email=" + this.getEmail() + ","
				+ " descGeneral=" + descGeneral + ", linkWeb=" + linkWeb + "]";
	}
		
}
