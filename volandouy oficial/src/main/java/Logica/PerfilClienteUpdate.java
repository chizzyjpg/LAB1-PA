package Logica;

import java.util.Date;

/**
 * Clase que representa los datos para actualizar el perfil de un cliente.
 */
public class PerfilClienteUpdate {
  private String nickname;
  private String email;
  private String nombre;
  private String apellido;
  private String nacionalidad;
  private String numDocumento;
  TipoDocumento tipoDocumento;
  private byte[] avatar;
  private boolean clearAvatar;
  private Date fechaNac;

  /**
   * Constructor de PerfilClienteUpdate.
   *
   * @param nickname Apodo del cliente
   * @param email Correo electrónico
   * @param nombre Nombre del cliente
   * @param apellido Apellido del cliente
   * @param nacionalidad Nacionalidad del cliente
   * @param tipoDocumento Tipo de documento
   * @param numDocumento Número de documento
   * @param fechaNac Fecha de nacimiento
   * @param avatar Imagen de avatar
   * @param clearAvatar Indica si se debe limpiar el avatar
   */
  public PerfilClienteUpdate(String nickname, String email, String nombre, String apellido,
      String nacionalidad, TipoDocumento tipoDocumento, String numDocumento, Date fechaNac,
      byte[] avatar, boolean clearAvatar) {
    this.nickname = nickname;
    this.email = email;
    this.nombre = nombre;
    this.apellido = apellido;
    this.avatar = avatar;
    this.clearAvatar = clearAvatar;
    this.nacionalidad = nacionalidad;
    this.tipoDocumento = tipoDocumento;
    this.numDocumento = numDocumento;
    this.fechaNac = fechaNac;
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

  public String getApellido() {
    return apellido;
  }

  public byte[] getAvatar() {
    return avatar;
  }

  public boolean isClearAvatar() {
    return clearAvatar;
  }

  public String getNacionalidad() {
    return nacionalidad;
  }

  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  public String getNumDocumento() {
    return numDocumento;
  }

  public Date getFechaNac() {
    return fechaNac;
  }

}