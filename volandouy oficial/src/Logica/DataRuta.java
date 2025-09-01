package Logica;

import java.math.BigDecimal;
import java.util.Date;

public class DataRuta {
    private String nombre;
    private String descripcion;
    private DataCiudad ciudadOrigen;
    private DataCiudad ciudadDestino;
    private int hora;
    private Date fechaAlta;
    private BigDecimal costoTurista;
    private BigDecimal costoEjecutivo;
    private int costoEquipajeExtra;

    public DataRuta(String nombre, String descripcion,
                    DataCiudad ciudadOrigen, DataCiudad ciudadDestino,
                    int hora, Date fechaAlta,
                    BigDecimal costoTurista, int costoEquipajeExtra, BigDecimal costoEjecutivo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoTurista = costoTurista;
        this.costoEquipajeExtra = costoEquipajeExtra;
        this.costoEjecutivo = costoEjecutivo;
    }

    // Getters

	public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public DataCiudad getCiudadOrigen() { return ciudadOrigen; }
    public DataCiudad getCiudadDestino() { return ciudadDestino; }
    public int getHora() { return hora; }
    public Date getFechaAlta() { return fechaAlta; }
    public BigDecimal getCostoTurista() { return costoTurista; }
    public BigDecimal getCostoEjecutivo() { return costoEjecutivo; }
    public int getCostoEquipajeExtra() { return costoEquipajeExtra; }
    
    @Override
    public String toString() {
    			return  nombre + " De (" + ciudadOrigen.getNombre() + ") " + "A (" +ciudadDestino.getNombre() + ")";
    			
    }
}
