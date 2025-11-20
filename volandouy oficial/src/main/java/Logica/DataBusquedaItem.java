package Logica;

import java.util.Date;

public class DataBusquedaItem {
    private String identificador;
    private String nombre;
    private String descripcion;
    private Date fechaAlta;
    private TipoResultado tipo;

    public DataBusquedaItem() {}

    public DataBusquedaItem(String identificador,String nombre, String descripcion, Date fechaAlta, TipoResultado tipo) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaAlta = fechaAlta;
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return  descripcion;
    }
    public Date getFechaAlta() {
        return fechaAlta;
    }
    public TipoResultado getTipo() {
        return tipo;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    public void setTipo(TipoResultado tipo) {
        this.tipo = tipo;
    }
}