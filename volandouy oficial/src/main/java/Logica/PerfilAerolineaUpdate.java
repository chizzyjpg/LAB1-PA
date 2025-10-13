package Logica;

public class PerfilAerolineaUpdate {
	String nickname, email, nombre, sitioWeb, descGeneral;
	byte[] avatar;
	boolean clearAvatar;

	public PerfilAerolineaUpdate(String nickname, String email, String nombre, String descGeneral, String linkWeb,
								 byte[] avatar, boolean clearAvatar) {
		this.nickname = nickname; this.email = email;
		this.nombre = nombre; this.descGeneral = descGeneral;
		this.sitioWeb = linkWeb;
		this.avatar = avatar; this.clearAvatar = clearAvatar;
	}
	public String getNickname() { return nickname; }
	public String getEmail() { return email; }
	public String getNombre() { return nombre; }
	public String getDescGeneral() { return descGeneral; }
	public String getSitioWeb() { return sitioWeb; }
	public byte[] getAvatar() { return avatar; }
	public boolean isClearAvatar() { return clearAvatar; }

}
