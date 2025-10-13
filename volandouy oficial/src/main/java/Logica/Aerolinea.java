package Logica;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Aerolinea")
@PrimaryKeyJoinColumn(name = "nickname")
public class Aerolinea extends Usuario {

	@ManyToMany(mappedBy = "aerolineas", fetch = FetchType.EAGER)
	private Set<Ruta> rutas = new HashSet<>();
	
	@Column(name = "descGeneral", nullable = false, length = 200)
	private String descGeneral;
	
	@Column(name = "linkWeb", nullable = false, length = 50)
	private String linkWeb;
	
	@Column(name = "avatar", nullable = true)
	private byte[] avatar;
		

	protected Aerolinea() {}

	public Aerolinea(String n, String nick, String email, String contra, String descGeneral, String linkWeb) {
		super(n, nick, email, contra);
		this.setLinkWeb(linkWeb);
		this.setDescGeneral(descGeneral);
		this.rutas = new HashSet<>();
	}
	
	public Aerolinea(String n, String nick, String email, String contra, String descGeneral, String linkWeb, byte[] avatar) {
		super(n, nick, email, contra);
		this.setLinkWeb(linkWeb);
		this.setDescGeneral(descGeneral);
		this.rutas = new HashSet<>();
		this.avatar = avatar;
	}

	//Getters
	public String getDescGeneral() {
		return descGeneral;
	}


	public String getLinkWeb() {
		return linkWeb;
	}
	
	public Set<Ruta> getRutas() {
		return rutas;
	}
	
	public byte[] getAvatar() {
		return avatar;
	}

	//Setters
	
	public void setLinkWeb(String linkWeb) {
		this.linkWeb = linkWeb;
	}
	
	public void setDescGeneral(String descGeneral) {
		this.descGeneral = descGeneral;
	}
	
	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}
	
	
		//Metodos
	public void addRuta(Ruta r) {
        rutas.add(r);
        r.getAerolineas().add(this);
    }
	
	@Override public String toString() {
		return "Aerolinea [nickname=" + this.getNickname() + ","
				+ " nombre=" + this.getNombre() + ","
				+ " email=" + this.getEmail() + ","
				+ " descGeneral=" + descGeneral + ", linkWeb=" + linkWeb + "]";
	}
		
}