package Logica;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Ciudad con PK compuesta (nombre, pais)
    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "origen_nombre",  referencedColumnName = "nombre", nullable = false),
        @JoinColumn(name = "origen_pais",    referencedColumnName = "pais",   nullable = false)
    })
    private Ciudad origen;

    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "destino_nombre", referencedColumnName = "nombre", nullable = false),
        @JoinColumn(name = "destino_pais",   referencedColumnName = "pais",   nullable = false)
    })
    private Ciudad destino;

    @Column(name = "Hora", nullable = false)
    private int hora;

    @Temporal(TemporalType.DATE)
    @Column(name = "fechaAlta", nullable = false)
    private Date fechaAlta;

    @Column(name = "costoBase", nullable = false)
    private int costoBase;

    @Column(name = "costoEquipajeExtra", nullable = false)
    private int costoEquipajeExtra;

    // si Categoria es enum:
    @ElementCollection(targetClass = Categoria.class)
    @CollectionTable(name = "ruta_categoria", joinColumns = @JoinColumn(name = "ruta_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", length = 40, nullable = false)
    private List<Categoria> categorias = new ArrayList<>();

    protected Ruta() {}

    public Ruta(String n, String desc,
                Ciudad origen, Ciudad destino,
                int hora, Date fechaAlta,
                int costoBase, int costoEquipajeExtra) {
        this.nombre = n;
        this.descripcion = desc;
        this.origen = origen;
        this.destino = destino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoBase = costoBase;
        this.costoEquipajeExtra = costoEquipajeExtra;
        this.vuelosEspecificos = new HashMap<>();
    }

    // getters
    public int getIdRuta() { return idRuta; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Ciudad getOrigen() { return origen; }
    public Ciudad getDestino() { return destino; }
    public int getHora() { return hora; }
    public Date getFechaAlta() { return fechaAlta; }
    public int getCostoBase() { return costoBase; }
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
    public void setCostoBase(int costoBase) { this.costoBase = costoBase; }
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
               ", costoBase=" + costoBase +
               ", costoEquipajeExtra=" + costoEquipajeExtra +
               ", categorias=" + categorias + "]" +
               " vuelosEspecificos=" + vuelosEspecificos.values() + "]";
    }

	public void addVuelosEspecificos(VueloEspecifico v) {
		vuelosEspecificos.put(v.getNombre(), v);
		
	}
}
