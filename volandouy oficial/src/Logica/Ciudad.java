package Logica;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;

//private boolean ciudadMatch(Ciudad c, String nombre, String pais) {
//    if (nombre == null && pais == null) return true;
//    if (c == null) return false;
//    boolean okNombre = (nombre == null) || (c.getNombre()!=null && c.getNombre().equalsIgnoreCase(nombre));
//    boolean okPais   = (pais   == null) || (c.getPais()!=null   && c.getPais().equalsIgnoreCase(pais));
//    return okNombre && okPais;
//}

@Entity
@Table(name = "Ciudad")
@IdClass(Ciudad.CiudadId.class)
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "idRuta", nullable = false, referencedColumnName = "idRuta")
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

    // ===== Clase Id compuesta =====
    public static class CiudadId implements Serializable {
        private static final long serialVersionUID = 1L;
        private String nombre;
        private String pais;

        public CiudadId() {}
        public CiudadId(String nombre, String pais) {
            this.nombre = nombre;
            this.pais = pais;
        }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getPais() { return pais; }
        public void setPais(String pais) { this.pais = pais; }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CiudadId)) return false;
            CiudadId that = (CiudadId) o;
            return Objects.equals(nombre, that.nombre) &&
                   Objects.equals(pais, that.pais);
        }
        @Override public int hashCode() {
            return java.util.Objects.hash(nombre, pais);
        }
    }
}
