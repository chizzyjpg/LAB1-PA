package Logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Ruta")
public class Ruta {
	
	@ManyToMany
	private Map<String, VueloEspecifico> vuelosEspecificos = new HashMap<>();

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int idRuta;

    @Column(name = "Nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(name = "Descripcion", nullable = false, length = 200)
    private String descripcion;
    
    // Ahora el ManyToOne es por idCiudad
    @ManyToOne(optional = false)
    @JoinColumn(name = "origen_idCiudad", nullable = false, referencedColumnName = "idCiudad")
    private Ciudad origen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destino_idCiudad", nullable = false, referencedColumnName = "idCiudad")
    private Ciudad destino;

    @Column(name = "Hora", nullable = false)
    private int hora;

    @Temporal(TemporalType.DATE)
    @Column(name = "fechaAlta", nullable = false)
    private Date fechaAlta;

    @Column(name = "costoTurista", nullable = false)
    private BigDecimal costoTurista;
    
    @Column(name = "costoEjecutivo", nullable = false)
    private BigDecimal costoEjecutivo;

    @Column(name = "costoEquipajeExtra", nullable = false)
    private int costoEquipajeExtra;

    @ManyToOne
    private Categoria categoria;

    protected Ruta() {}

    public Ruta(String n, String desc,
                Ciudad origen, Ciudad destino,
                int hora, Date fechaAlta,
                BigDecimal costoTurista, int costoEquipajeExtra, BigDecimal costoEjecutivo) {
        this.nombre = n;
        this.descripcion = desc;
        this.origen = origen;
        this.destino = destino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoTurista = costoTurista;
        this.costoEquipajeExtra = costoEquipajeExtra;
        this.vuelosEspecificos = new HashMap<>();
        this.costoEjecutivo = costoEjecutivo;
    }

    // getters
    public int getIdRuta() { return idRuta; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Ciudad getOrigen() { return origen; }
    public Ciudad getDestino() { return destino; }
    public int getHora() { return hora; }
    public Date getFechaAlta() { return fechaAlta; }
    public BigDecimal getCostoTurista() { return costoTurista; }
    public BigDecimal getCostoEjecutivo() { return costoEjecutivo; }
    public int getCostoEquipajeExtra() { return costoEquipajeExtra; }
    public List<Categoria> getCategorias() { return categorias; }
    public Map<String, VueloEspecifico> getVuelosEspecificos() { return vuelosEspecificos; }

    // setters
    public void setNombre(String n) { this.nombre = n; }
    public void setDescripcion(String desc) { this.descripcion = desc; }
    public void setOrigen(Ciudad origen) { this.origen = origen; }
    public void setDestino(Ciudad destino) { this.destino = destino; }
    public void setHora(int hora) { this.hora = hora; }
    public void setFechaAlta(Date fechaAlta) { this.fechaAlta = fechaAlta; }
    public void setCostoBase(BigDecimal costoTurista) { this.costoTurista = costoTurista; }
    public void setCostoEjecutivo(BigDecimal costoEjecutivo) { this.costoEjecutivo = costoEjecutivo; }
    public void setCostoEquipajeExtra(int costoEquipajeExtra) { this.costoEquipajeExtra = costoEquipajeExtra; }
    public void setCategorias(List<Categoria> cat) { this.categorias = cat; }
    public void setVuelosEspecificos(Map<String, VueloEspecifico> vuelos) { this.vuelosEspecificos = vuelos; }

    @Override public String toString() {
        return "Ruta [idRuta=" + idRuta +
               ", nombre=" + nombre +
               ", descripcion=" + descripcion +
               ", origen=" + (origen!=null ? (origen.getNombre()+", "+origen.getPais()) : null) +
               ", destino=" + (destino!=null ? (destino.getNombre()+", "+destino.getPais()) : null) +
               ", hora=" + hora +
               ", fechaAlta=" + fechaAlta +
               ", costoTurista=" + costoTurista +
               ", costoEjecutivo=" + costoEjecutivo +
               ", costoEquipajeExtra=" + costoEquipajeExtra +
               ", categoria=" + categoria + "]" +
               " vuelosEspecificos=" + vuelosEspecificos + "]";
    }

	public void addVuelosEspecificos(VueloEspecifico v) {
		vuelosEspecificos.put(v.getNombre(), v);
		
	}
}
