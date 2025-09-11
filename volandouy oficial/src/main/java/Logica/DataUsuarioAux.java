package Logica;

public class DataUsuarioAux extends DataUsuario {
    private String tipoUsuario;

    public DataUsuarioAux(String nombre, String nickname, String email) {
        super(nombre, nickname, email);
    }

    public DataUsuarioAux(DataUsuario du) {
        super(du.getNombre(), du.getNickname(), du.getEmail());
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