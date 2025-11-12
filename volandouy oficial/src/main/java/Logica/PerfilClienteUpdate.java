package Logica;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PerfilClienteUpdate")
@XmlType(propOrder = {"nickname", "email", "nombre", "apellido", "nacionalidad", "numDocumento", "tipoDocumento", "avatar", "clearAvatar", "fechaNac"})
public class PerfilClienteUpdate {
  private String nickname, email, nombre, apellido, nacionalidad, numDocumento;
  TipoDocumento tipoDocumento;
  private byte[] avatar;
  private boolean clearAvatar;
  private Date fechaNac;

  public PerfilClienteUpdate() {}

  public PerfilClienteUpdate(String nickname, String email, String nombre, String apellido, String nacionalidad, TipoDocumento tipoDocumento, String numDocumento, Date fechaNac,
                             byte[] avatar, boolean clearAvatar) {
    this.nickname = nickname; this.email = email;
    this.nombre = nombre; this.apellido = apellido;
    this.avatar = avatar; this.clearAvatar = clearAvatar;
    this.nacionalidad = nacionalidad; 
    this.tipoDocumento = tipoDocumento; 
    this.numDocumento = numDocumento; 
    this.fechaNac = fechaNac;
  }

    public String getNickname() { return nickname; }
  public String getEmail() { return email; }
  public String getNombre() { return nombre; }
  public String getApellido() { return apellido; }
  public byte[] getAvatar() { return avatar; }
  public boolean isClearAvatar() { return clearAvatar; }
  public String getNacionalidad() { return nacionalidad; }
  public TipoDocumento getTipoDocumento() { return tipoDocumento; }
  public String getNumDocumento() { return numDocumento; }
  public Date getFechaNac() { return fechaNac; }
  	
}
