package Logica;

public class DataAerolinea extends DataUsuario {
   
    private String descGeneral;
    private String sitioWeb;

    public DataAerolinea(String nombre, String nickname, String email,
                         String descripcion, String sitioWeb) {
        super(nombre, nickname, email);
        this.descGeneral = descripcion;
        this.sitioWeb = sitioWeb;
    }

    // GETTERS
    public String getDescripcion() {
        return descGeneral;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }
    
    @Override
    public String toString() {
    			return "DataAerolinea [nickname=" + this.getNickname() + ","
				+ " nombre=" + this.getNombre() + ","
				+ " email=" + this.getEmail() + ","
				+ " descGeneral=" + descGeneral + ", sitioWeb=" + sitioWeb + "]";
    }
}

