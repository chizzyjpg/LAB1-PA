package Logica;

import java.util.Collection;
import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Aerolinea")
@PrimaryKeyJoinColumn(name = "nickname")
public class Aerolinea extends Usuario {
	
	private Map<String, Ruta> rutasMap;

	@ManyToMany
	private Collection<Ruta> rutas;
	
	@Column(name = "descGeneral", nullable = false, length = 200)
	private String descGeneral;
	
	@Column(name = "linkWeb", nullable = false, length = 50)
	private String linkWeb;
	

	protected Aerolinea() {}

	public Aerolinea(String n, String nick, String email, String descGeneral, String linkWeb) {
		super(n, nick, email);
		this.setLinkWeb(linkWeb);
		this.setDescGeneral(descGeneral);
		this.rutasMap = new HashMap<>();
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
	public void agregarRuta(Ruta r) {
		rutasMap.put(r.getNombre(), r);
	}
	
	@Override public String toString() {
		return "Aerolinea [nickname=" + this.getNickname() + ","
				+ " nombre=" + this.getNombre() + ","
				+ " email=" + this.getEmail() + ","
				+ " descGeneral=" + descGeneral + ", linkWeb=" + linkWeb + "]";
	}
		
}
