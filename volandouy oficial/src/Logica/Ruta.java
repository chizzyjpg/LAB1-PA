package Logica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Ruta")
public class Ruta {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)	
	private int idRuta;
	
	@ManyToMany(mappedBy = "aerolinea")
	
	@Column(name = "Nombre", nullable = false, length = 50)
	private String nombre;
	
	@Column(name = "Descripcion", nullable = false, length = 200)
	private String descripcion;
	
	@Column(name = "ciudadOrigen", nullable = false, length = 50)
	private String ciudadOrigen;
	
	@Column(name = "ciudadDestino", nullable = false, length = 50)
	private String ciudadDestino;
	
	@Column(name = "Hora", nullable = false)
	private int hora;
	
	@Column(name = "fechaAlta", nullable = false)
	private Date fechaAlta;
	
	@Column(name = "costoBase", nullable = false)
	private int costoBase;
	
	@Column(name = "costoEquipajeExtra", nullable = false)
	private int costoEquipajeExtra;
	
	@Column(name = "Categorias", nullable = false, length = 100)
	private List<Categoria> categorias;
	
	
	protected Ruta() {}
	
	public Ruta(String n, String desc, String ciudadOr, String ciudadDe, int hora, Date fechaAlta, int costoBase, int costoEquipajeExtra) {
		this.nombre = n;
		this.descripcion = desc;
		this.ciudadOrigen = ciudadOr;
		this.ciudadDestino = ciudadDe;
		this.hora = hora;
		this.fechaAlta = fechaAlta;
		this.costoBase = costoBase;
		this.costoEquipajeExtra = costoEquipajeExtra;
		this.categorias = new ArrayList<>();
	}
	
	//Getters
	public String getNombre() {
		return nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public String getCiudadOrigen() {
		return ciudadOrigen;
	}
	public String getCiudadDestino() {
		return ciudadDestino;
	}
	public int getHora() {
		return hora;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public int getCostoBase() {
		return costoBase;
	}
	public int getCostoEquipajeExtra() {
		return costoEquipajeExtra;
	}
	public List<Categoria> getCategorias() {
		return categorias;
	}
	
	//Setters
	public void setNombre(String n) {
		this.nombre = n;
	}
	public void setDescripcion(String desc) {
		this.descripcion = desc;
	}
	public void setCiudadOrigen(String ciudadO) {
		this.ciudadOrigen = ciudadO;
	}
	public void setCiudadDestino(String ciudadD) {
		this.ciudadDestino = ciudadD;
	}
	public void setHora(int hora) {
		this.hora = hora;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public void setCostoBase(int costoBase) {
		this.costoBase = costoBase;
	}
	public void setCostoEquipajeExtra(int costoEquipajeExtra) {
		this.costoEquipajeExtra = costoEquipajeExtra;
	}
	public void setCategorias(List<Categoria> cat) {
		this.categorias = cat;
	}
	
	@Override public String toString() {
		return "Rutas [idRuta=" + idRuta + ", nombre=" + nombre + ", descripcion=" + descripcion 
				+ ", ciudadOrigen=" + ciudadOrigen + ", ciudadDestino=" + ciudadDestino 
				+ ", hora=" + hora + ", fechaAlta=" + fechaAlta 
				+ ", costoBase=" + costoBase + ", costoEquipajeExtra=" + costoEquipajeExtra 
				+ ", categorias=" + categorias + "]";
		}
}
