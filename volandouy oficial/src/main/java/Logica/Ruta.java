package Logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;

@Entity
@Table(name = "Ruta")
public class Ruta {

    @OneToMany(mappedBy = "ruta", fetch = FetchType.EAGER)
    private Set<VueloEspecifico> vuelosEspecificos = new HashSet<>();
	 
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int idRuta;

    @Column(name = "Nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(name = "Descripcion", nullable = false, length = 200)
    private String descripcion;
    
    @Column(name = "Descripcion corta", nullable = true, length = 200)
    private String descripcionCorta;
    
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
    
    //si Categoria es enum:
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_nombre", nullable = false, referencedColumnName = "nombre")
    private Categoria categoria;
    
    @ManyToMany
    @JoinTable(
        name = "aerolinea_ruta",
        joinColumns = @JoinColumn(name = "idRuta"),
        inverseJoinColumns = @JoinColumn(name = "nicknameAerolinea", referencedColumnName = "nickname")
    )
    
    private Set<Aerolinea> aerolineas = new HashSet<>();
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoRuta estado = EstadoRuta.INGRESADA;

    @Column(name = "visitas", nullable = true)
    private int visitas;
    
    protected Ruta() {}

    public Ruta(String n, String desc,
                Ciudad origen, Ciudad destino,
                int hora, Date fechaAlta,
                BigDecimal costoTurista, int costoEquipajeExtra, BigDecimal costoEjecutivo, Categoria categoria, String descCorta) {
        this.nombre = n;
        this.descripcion = desc;
        this.origen = origen;
        this.destino = destino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoTurista = costoTurista;
        this.costoEquipajeExtra = costoEquipajeExtra;
        this.vuelosEspecificos = new HashSet<>();
        this.costoEjecutivo = costoEjecutivo;
        this.categoria = categoria;
        this.estado = EstadoRuta.INGRESADA;
        this.descripcionCorta = descCorta;
        this.visitas = 0;
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
    public Categoria getCategoriaR() { return categoria; }
    public Set<VueloEspecifico> getVuelosEspecificos() { return vuelosEspecificos; }
    public Set<Aerolinea> getAerolineas() { return aerolineas; }
    public EstadoRuta getEstado() { return estado; }
    public String getDescripcionCorta() { return descripcionCorta; }
    public int getVisitas() { return visitas; }

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
    public void setCategoriaR(Categoria categoria) { this.categoria = categoria; }
    public void setVuelosEspecificos(Set<VueloEspecifico> vuelos) { this.vuelosEspecificos = vuelos; }
    public void setAerolineas(Set<Aerolinea> aerolineas) { this.aerolineas = aerolineas; }
    public void setEstado(EstadoRuta estado) { this.estado = estado; }
    public void setDescripcionCorta(String descCorta) { this.descripcionCorta = descCorta; }
    public void setVisitas(int visitas) { this.visitas = visitas; }

    @Override public String toString() {
        return "Ruta [idRuta=" + idRuta +
               ", nombre=" + nombre +
               ", descripcion=" + descripcion +
               ", descripcionCorta=" + descripcionCorta +
               ", origen=" + (origen!=null ? (origen.getNombre()+", "+origen.getPais()) : null) +
               ", destino=" + (destino!=null ? (destino.getNombre()+", "+destino.getPais()) : null) +
               ", hora=" + hora +
               ", fechaAlta=" + fechaAlta +
               ", costoTurista=" + costoTurista +
               ", costoEjecutivo=" + costoEjecutivo +
               ", costoEquipajeExtra=" + costoEquipajeExtra +
               ", categoria=" + categoria + "]" +
               " vuelosEspecificos=" + vuelosEspecificos + "]" +
               " estado=" + estado + "]" +
                " visitas=" + visitas + "]";
    }

	public void addVuelosEspecificos(VueloEspecifico v) {
        vuelosEspecificos.add(v);
    }
}