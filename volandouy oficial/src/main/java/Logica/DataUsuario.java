package Logica;

/**
 * Clase abstracta que representa los datos comunes de un usuario.
 */
public abstract class DataUsuario {

  private String nombre;
  private String nickname;
  private String email;
  private String contrasenia;

  /** Constructor de DataUsuario.
   */
  public DataUsuario(String nombre, String nickname, String email, String contrasenia) {
    this.nombre = nombre;
    this.nickname = nickname;
    this.email = email;
    this.contrasenia = contrasenia;
  }

  // Getters

  public String getNombre() {
    return nombre;
  }

  public String getNickname() {
    return nickname;
  }

  public String getEmail() {
    return email;
  }

  public String getContrasenia() {
    return contrasenia;
  }
}
