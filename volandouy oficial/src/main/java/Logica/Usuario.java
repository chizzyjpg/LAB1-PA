package Logica;

import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

/**
 * Clase que representa a un usuario en el sistema.
 */
@DynamicUpdate
@Entity
@Table(name = "Usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

  @Id
  @Column(name = "nickname", length = 50, nullable = false)
  private String nickname;

  @Column(name = "Nombre", nullable = false, length = 40)
  private String nombre;

  @Column(name = "Email", nullable = false, length = 40)
  private String email;

  @Column(name = "Contraseña", nullable = false, length = 64)
  private String contrasenia;

  @Column(name = "avatar", nullable = true)
  private byte[] avatar;

  // Constructor vacío requerido por JPA
  /**
   * Constructor vacío requerido por JPA.
   */
  protected Usuario() {
  }

  /**
   * Constructor de la clase Usuario.
   * 
   */
  public Usuario(String nombre, String nickname, String email, String contrasenia) {
    this.nombre = nombre;
    this.nickname = nickname;
    this.email = email;
    this.contrasenia = contrasenia;
  }
  
  /**
   * Constructor de la clase Usuario con avatar.
   * 
   */
  public Usuario(String nombre, String nickname, String email, String contrasenia, byte[] avatar) {
    this.nombre = nombre;
    this.nickname = nickname;
    this.email = email;
    this.contrasenia = contrasenia;
    this.avatar = avatar;
  }

  // Getters
  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getNombre() {
    return nombre;
  }

  public String getContrasenia() {
    return contrasenia;
  }

  public byte[] getAvatar() {
    return avatar;
  }

  // Setters
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }

  public void setAvatar(byte[] avatar) {
    this.avatar = avatar;
  }

  @Override
  public String toString() {
    return "Usuario [nickname=" + nickname + ", nombre=" + nombre + ", email=" + email + "]";
  }

}
