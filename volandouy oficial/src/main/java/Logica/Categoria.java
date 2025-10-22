package Logica;

import java.util.List;
import jakarta.persistence.*;



@Entity
@Table(name = "Categoria")
public class Categoria{
	
	@Id
	private String nombre;

	@OneToMany(mappedBy = "categoria")
	private List<Ruta> rutas; // NOPMD - used by JPA
	
	public Categoria(){}
	
	public Categoria (String nombre){ this.nombre = nombre; }
	
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Categoria [nombre=" + nombre + "]";
	}
}