package Logica;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name = "Ciudad")
@IdClass(Ciudad.CiudadId.class)
public class Ciudad {

    // ===== Clave compuesta =====
    @Id
    @Column(name = "nombre", length = 80, nullable = false)
    private String nombre;

    @Id
    @Column(name = "pais", length = 80, nullable = false)
    private String pais;

    // ===== Atributos adicionales =====
    @Column(name = "nombre_aeropuerto", length = 120, nullable = false)
    private String nombreAeropuerto;

    @Column(name = "descripcion_aeropuerto", length = 255)
    private String descripcionAeropuerto;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_alta", nullable = false)
    private Date fechaAlta;

    // ===== Constructores =====
    public Ciudad() {} // requerido por JPA

    public Ciudad(String nombre, String pais,
                  String nombreAeropuerto,
                  String descripcionAeropuerto,
                  Date fechaAlta) {
        this.nombre = nombre;
        this.pais = pais;
        this.nombreAeropuerto = nombreAeropuerto;
        this.descripcionAeropuerto = descripcionAeropuerto;
        this.fechaAlta = fechaAlta;
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

    // ===== Clase Id compuesta (estática interna) =====
    public static class CiudadId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String nombre;
        private String pais;

        public CiudadId() {} // requerido por JPA
        public CiudadId(String nombre, String pais) {
            this.nombre = nombre;
            this.pais = pais;
        }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getPais() { return pais; }
        public void setPais(String pais) { this.pais = pais; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CiudadId)) return false;
            CiudadId that = (CiudadId) o;
            return Objects.equals(nombre, that.nombre) &&
                   Objects.equals(pais, that.pais);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nombre, pais);
        }
    }
}
