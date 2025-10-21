package Logica;

public class DataUsuarioAux extends DataUsuario {
    private String tipoUsuario;

    public DataUsuarioAux(String nombre, String nickname, String email, String contra) {
        super(nombre, nickname, email, contra);
        // default type when created from raw fields
        this.tipoUsuario = "Usuario";
    }

    public DataUsuarioAux(DataUsuario du) {
        super(du.getNombre(), du.getNickname(), du.getEmail(), du.getContrasenia());
        if (du instanceof DataCliente) {
            tipoUsuario = "Cliente";
        } else if (du instanceof DataAerolinea) {
            tipoUsuario = "Aerolínea";
        } else {
            tipoUsuario = "Usuario";
        }
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }
}