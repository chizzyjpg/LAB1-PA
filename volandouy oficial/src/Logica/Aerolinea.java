package Logica;

public class Aerolinea extends Usuario {
	
	private int id;
	private String nombre;

	
		public Aerolinea(String n, String nick, String email, int id, String nombre) {
		super(n, nick, email);
		
		// TODO Auto-generated constructor stub
	}


	@Override
    public String toString() {
        return nombre; // útil para mostrar en el ComboBox
    }
	
	
	
	
		
}
