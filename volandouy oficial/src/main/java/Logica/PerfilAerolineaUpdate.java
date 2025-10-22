package Logica;

/**
 * Clase que representa los datos para actualizar el perfil de una aerolínea.
 */
public class PerfilAerolineaUpdate {
  String nickname;
  String email;
  String nombre;
  String sitioWeb;
  String descGeneral;
  byte[] avatar;
  boolean clearAvatar;

  /**
   * Constructor de PerfilAerolineaUpdate.
   *
   * @param nickname Apodo de la aerolínea
   * @param email Correo electrónico
   * @param nombre Nombre de la aerolínea
   * @param descGeneral Descripción general
   * @param linkWeb Sitio web
   * @param avatar Imagen de avatar
   * @param clearAvatar Indica si se debe limpiar el avatar
   */
  public PerfilAerolineaUpdate(String nickname, String email, String nombre, String descGeneral,
      String linkWeb, byte[] avatar, boolean clearAvatar) {
    this.nickname = nickname;
    this.email = email;
    this.nombre = nombre;
    this.descGeneral = descGeneral;
    this.sitioWeb = linkWeb;
    this.avatar = avatar;
    this.clearAvatar = clearAvatar;
  }

  public String getNickname() {
    return nickname;
  }

  public String getEmail() {
    return email;
  }

  public String getNombre() {
    return nombre;
  }

  public String getDescGeneral() {
    return descGeneral;
  }

  public String getSitioWeb() {
    return sitioWeb;
  }

  public byte[] getAvatar() {
    return avatar;
  }

  public boolean isClearAvatar() {
    return clearAvatar;
  }

}