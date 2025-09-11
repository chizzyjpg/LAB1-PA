package Logica;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "Ciudad")
public class Ciudad {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int idCiudad;
   
    // ===== Atributos adicionales =====
    @Column(name = "nombre", length = 80, nullable = false)
    private String nombre;

    @Column(name = "pais", length = 80, nullable = false)
    private String pais;

    @Column(name = "nombre_aeropuerto", length = 120, nullable = false)
    private String nombreAeropuerto;

    @Column(name = "descripcion_aeropuerto", length = 255)
    private String descripcionAeropuerto;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_alta", nullable = false)
    private Date fechaAlta;

    // 👉 NUEVO: sitio web (si tu letra lo requiere, podés hacerlo NOT NULL)
    @Column(name = "sitio_web", length = 200, nullable = true)
    private String sitioWeb;

    @Transient
    @ManyToOne(optional = true)
    @JoinColumn(name = "idRuta", nullable = true, referencedColumnName = "idRuta")
    private Ruta ruta;
    
    // ===== Constructores =====
    public Ciudad() {}

    public Ciudad(String nombre, String pais,
                  String nombreAeropuerto,
                  String descripcionAeropuerto,
                  Date fechaAlta,
                  String sitioWeb) {
        this.nombre = nombre;
        this.pais = pais;
        this.nombreAeropuerto = nombreAeropuerto;
        this.descripcionAeropuerto = descripcionAeropuerto;
        this.fechaAlta = fechaAlta;
        this.sitioWeb = sitioWeb;
    }

    // ===== Getters/Setters =====
    public int getIdCiudad() { return idCiudad; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getNombreAeropuerto() { return nombreAeropuerto; }
    public void setNombreAeropuerto(String nombreAeropuerto) { this.nombreAeropuerto = nombreAeropuerto; }

    public String getDescripcionAeropuerto() { return descripcionAeropuerto; }
    public void setDescripcionAeropuerto(String descripcionAeropuerto) { this.descripcionAeropuerto = descripcionAeropuerto; }

    public Date getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(Date fechaAlta) { this.fechaAlta = fechaAlta; }

    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

}