package Logica;

import java.util.Date;

public class DataRuta {
    private String nombre;
    private String descripcion;
    private DataCiudad ciudadOrigen;
    private DataCiudad ciudadDestino;
    private int hora;
    private Date fechaAlta;
    private int costoBase;
    private int costoEquipajeExtra;

    public DataRuta(String nombre, String descripcion,
                    DataCiudad ciudadOrigen, DataCiudad ciudadDestino,
                    int hora, Date fechaAlta,
                    int costoBase, int costoEquipajeExtra) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoBase = costoBase;
        this.costoEquipajeExtra = costoEquipajeExtra;
    }

    // Getters

	public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public DataCiudad getCiudadOrigen() { return ciudadOrigen; }
    public DataCiudad getCiudadDestino() { return ciudadDestino; }
    public int getHora() { return hora; }
    public Date getFechaAlta() { return fechaAlta; }
    public int getCostoBase() { return costoBase; }
    public int getCostoEquipajeExtra() { return costoEquipajeExtra; }
    
    @Override
    public String toString() {
    			return  nombre + " De (" + ciudadOrigen.getNombre() + ") " + "A (" +ciudadDestino.getNombre() + ")";
    			
    }
}
