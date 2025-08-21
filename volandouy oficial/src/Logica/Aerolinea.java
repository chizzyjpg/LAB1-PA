package Logica;

import java.util.HashMap;
import java.util.Map;

public class Aerolinea extends Usuario {
	
	private String descGeneral;
	private String linkWeb;
	private Map<String, Ruta> rutas;
	
		public Aerolinea(String n, String nick, String email, String descGeneral, String linkWeb) {
		super(n, nick, email);
		this.setLinkWeb(linkWeb);
		this.setDescGeneral(descGeneral);
		this.rutas = new HashMap<>();
		// Getters
	}

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
	
	@Override
    public String toString() {
        return this.getNombre(); // útil para mostrar en el ComboBox
    }
	
	public void agregarRuta(Ruta r) {
		rutas.put(r.getNombre(), r);
	}
	
	
		
}
