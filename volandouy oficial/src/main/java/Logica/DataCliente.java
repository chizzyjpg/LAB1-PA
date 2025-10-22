package Logica;

import java.util.Date;

public class DataCliente extends DataUsuario {

    private String apellido;
    private Date fechaNac;
    private String nacionalidad;
    private TipoDocumento tipoDocumento;
    private String numDocumento;

    public DataCliente(
            String nombre,
            String nickname,
            String email,
            String contra,
            String apellido,
            Date fechaNac,
            String nacionalidad,
            TipoDocumento tipo,
            String numeroDocumento
    ) {
        super(nombre, nickname, email, contra);
        this.apellido = apellido;
        // copia defensiva para no exponer el Date interno
        this.fechaNac = (fechaNac == null) ? null : new Date(fechaNac.getTime());
        this.nacionalidad = nacionalidad;
        this.tipoDocumento = tipo;
        this.numDocumento = numeroDocumento;
    }

    // GETTERS

    public String getApellido() {
        return apellido;
    }

    public Date getFechaNac() {
        // devolver copia para preservar inmutabilidad hacia afuera
        return (fechaNac == null) ? null : new Date(fechaNac.getTime());
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }
    
    public String getTipoUsuario() {
        return "Cliente";
    }
    
    @Override
    public String toString() {
    			return this.getNickname() + " - " + this.getNombre();
    }
}