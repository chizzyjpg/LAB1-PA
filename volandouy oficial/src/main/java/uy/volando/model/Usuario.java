package uy.volando.model;

/**
 * Almacena la informacion de un usuario
 */

public class Usuario {
	
	public Usuario(String nickname, String nombre, String email, String password) {
		this.nickname = nickname;
		this.nombre = nombre;
		this.email = email;
		this.password = password;
	}
	
	protected String nickname;
	
	/**
	 * Get the value of nickname
	 *
	 * @return the value of nickname
	 */
	
	public String getNickname() {
		return nickname;
	}
	
	/**
	 * Set the value of nickname
	 *
	 * @param nickname new value of nickname
	 */
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	protected String nombre;
	
	/**
	 * Get the value of nombre
	 *
	 * @return the value of nombre
	 */
	
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Set the value of nombre
	 *
	 * @param nombre new value of nombre
	 */
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	protected String email;
	
	/**
	 * Get the value of email
	 *
	 * @return the value of email
	 */
	
	public String getEmail() {
		return email;
	}
	
	/**
	 * Set the value of email
	 *
	 * @param email new value of email
	 */
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	protected String password;
	
	/**
	 * Get the value of password
	 *
	 * @return the value of password
	 */
	
	public String getPassword() {
		return password;
	}
	
	/**
	 * Set the value of password
	 *
	 * @param password new value of password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}


