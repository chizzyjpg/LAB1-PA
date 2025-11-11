package Logica;

import java.util.Date;

public class PerfilClienteUpdate {
  private String nickname, email, nombre, apellido, nacionalidad, numDocumento;
  TipoDocumento tipoDocumento;
  private byte[] avatar;
  private boolean clearAvatar;
  private Date fechaNac;
  

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
