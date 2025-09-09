package Logica;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Set;

//import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "Paquete")
public class Paquete {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int idPaquete;
	
	@Column(name = "Nombre", nullable = false, length = 50)
	private String nombre;
	
	@Column(name = "Descripcion", nullable = false, length = 200)
	private String descripcion;
	
	@Column(name = "cantRutas", nullable = false)
	private int cantRutas;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoAsiento", nullable = false)
	private TipoAsiento tipoAsiento;
	
	@Column(name = "descuento", nullable = false)
	private int descuento;
	
	/*@Column(name = "fechaCompra", nullable = false)
	private Date fechaCompra;*/
	
	@Column(name = "Validez", nullable = false)
	private int validez;
	
	 @Column(name = "costo", nullable = false, precision = 12, scale = 2)
	    private BigDecimal costo; // precio final promocional
	
	 @Column(name = "fechaAlta", nullable = false)
	 	private Date fechaAlta;
	 
	 // Usamos una lista de rutas incluidas (solo nombres, sin cupos)
	 private final java.util.Set<String> rutasIncluidas = new java.util.LinkedHashSet<>();
	 
	 private int cuposMaximos;
	 
	 private int cuposDisponibles;
	
	protected Paquete() { } // JPA 
	
	public Paquete(String n, String desc, int cantRut,TipoAsiento tipoAsiento, int dto,/* Date fechaComp,*/ int val, BigDecimal costo) {
		this.nombre = n;
		this.descripcion = desc;
		this.cantRutas = cantRut;
		this.tipoAsiento = tipoAsiento;
		this.descuento = dto;
		//this.fechaCompra = fechaComp;
		this.validez = val;
		this.costo = costo;
		this.cuposDisponibles = this.cuposMaximos = 0; // inicialmente 0, se seteará luego
	}
	
	//Getters
	public String getNombre() {
		return nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public int getCantRutas() {
		return cantRutas;
	}
	public TipoAsiento getTipoAsiento() {
	    return tipoAsiento;
	}
	public int getDescuento() {
		return descuento;
	}
	/*public Date getFechaCompra() {
		return fechaCompra;
	}*/
	public int getValidez() {
		return validez;
	}
	public BigDecimal getCosto() {
		return costo;
	}
	
	public Date getFechaAlta() {
		return fechaAlta;
	}
	
	public java.util.Set<String> getRutasIncluidas() {
	    // solo lectura hacia afuera
	    return Collections.unmodifiableSet(rutasIncluidas);
	}
	 public int getCuposMaximos() {
			return cuposMaximos;
	}
		
	public int getCuposDisponibles() {
		return cuposDisponibles;
	}
		
	
	/*
	public double getCostoAsociado() {
	    double costoBasePorRuta;

	    switch (tipoAsiento) {
	        case ECONOMICO:
	            costoBasePorRuta = 100; 
	            break;
	        case EJECUTIVO:
	            costoBasePorRuta = 200; 
	            break;
	        case PRIMERA:
	            costoBasePorRuta = 300; 
	            break;
	        default:
	            costoBasePorRuta = 0;
	    }

	    double costoTotal = cantRutas * costoBasePorRuta;
	    double costoConDescuento = costoTotal - (costoTotal * descuento / 100.0);

	    return costoConDescuento;
	}
	*/
	//Setters
	public void setNombre(String n) {
		this.nombre = n;
	}
	public void setDescripcion(String desc) {
		this.descripcion = desc;
	}
	public void setCantRutas(int cantRut) {
		this.cantRutas = cantRut;
	}
	public void setTipoAsiento(TipoAsiento tipoAsiento) {
	    this.tipoAsiento = tipoAsiento;
	}
	public void setDescuento(int dto) {
		this.descuento = dto;
	}
	/*public void setFechaCompra(Date fechaComp) {
		this.fechaCompra = fechaComp;
	}*/
	public void setValidez(int val) {
		this.validez = val;
	}
	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}
	public void setFechaAlta(Date fecha) {
		this.fechaAlta = fecha;
	}
	public void setCuposMaximos(int cuposMaximos) {
			this.cuposMaximos = cuposMaximos;
	}	
	public void setCuposDisponibles(int cuposDisponibles) {
			this.cuposDisponibles = cuposDisponibles;
	}
	@Override public String toString() {
		return "Paquete [idPaquete=" + idPaquete + ", nombre=" + nombre + ", descripcion=" + descripcion 
				+ ", cantRutas=" + cantRutas + ", tipoAsiento=" + tipoAsiento 
				+ ", descuento=" + descuento +/* ", fechaCompra=" + fechaCompra 
				+ */", validez=" + validez + "]";
	}
	
	// Método para agregar una ruta al paquete
	public void addRutaPorNombre(String nombreRuta) {
	    String key = canonical(nombreRuta);
	    if (key == null || key.isBlank())
	        throw new IllegalArgumentException("Nombre de ruta inválido");
	    if (rutasIncluidas.add(key)) { // solo si no existía
	        this.cantRutas++;          // ++ una vez por ruta distinta
	    }
	}

	  private static String canonical(String s) {
	        return (s == null) ? null : s.trim().toLowerCase(java.util.Locale.ROOT);
	    }
}