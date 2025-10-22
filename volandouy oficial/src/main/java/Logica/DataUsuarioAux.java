package Logica;

/**
 * Clase auxiliar para representar un DataUsuario con su tipo específico.
 */
public class DataUsuarioAux extends DataUsuario {
  private String tipoUsuario;

  /** Constructor para DataUsuarioAux con tipo "Usuario".
   *
   */
  public DataUsuarioAux(String nombre, String nickname, String email, String contra) {
    super(nombre, nickname, email, contra);
    this.tipoUsuario = "Usuario";
  }

  /** Constructor para DataUsuarioAux que determina el tipo de usuario
   * basado en la instancia de DataUsuario proporcionada.
   */
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