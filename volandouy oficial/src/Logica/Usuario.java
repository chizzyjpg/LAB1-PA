package Logica;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "nickname", length = 50, nullable = false)
    private String nickname;

    @Column(name = "nombre", nullable = false, length = 40)
    private String nombre;

    @Column(name = "email", nullable = false, length = 40)
    private String email;

    // Constructor vacío requerido por JPA
    protected Usuario() {}

    public Usuario(String nickname, String nombre, String email) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
    }

    //Getters
    public String getNickname() {
		return nickname;
	}
    public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNombre() {
		return nombre;
	}
	
	//Setters
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override public String toString() {
		return "Usuario [nickname=" + nickname + ", nombre=" + nombre + ", email=" + email + "]";
	}
	
}