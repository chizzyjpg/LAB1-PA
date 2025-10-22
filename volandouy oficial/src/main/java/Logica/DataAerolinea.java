package Logica;

public class DataAerolinea extends DataUsuario {
   
    private String descGeneral;
    private String sitioWeb;

    public DataAerolinea(String nombre, String nickname, String email, String contra,
                         String descripcion, String sitioWeb) {
        super(nombre, nickname, email, contra);
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
    
    public String getTipoUsuario() {
        return "Aerolínea";
    }
    
    @Override
    public String toString() {
    			return this.getNickname() + " - " + this.getNombre();
    }
}