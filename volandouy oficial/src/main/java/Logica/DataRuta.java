package Logica;

import java.math.BigDecimal;
import java.util.Date;

public class DataRuta {
    private int idRuta;
	private String nombre;
    private String descripcion;
    private DataCiudad ciudadOrigen;
    private DataCiudad ciudadDestino;
    private int hora;
    private Date fechaAlta;
    private BigDecimal costoTurista;
    private BigDecimal costoEjecutivo;
    private int costoEquipajeExtra;
    private DataCategoria categoria;
    private String nicknameAerolinea;
    
    public DataRuta(String nombre, String descripcion,
                    DataCiudad ciudadOrigen, DataCiudad ciudadDestino,
                    int hora, Date fechaAlta,
                    BigDecimal costoTurista, int costoEquipajeExtra, BigDecimal costoEjecutivo, DataCategoria categoria,
                    String nicknameAerolinea) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoTurista = costoTurista;
        this.costoEquipajeExtra = costoEquipajeExtra;
        this.costoEjecutivo = costoEjecutivo;
        this.categoria = categoria;
        this.nicknameAerolinea = nicknameAerolinea;
    }

    // Getters
	public int getIdRuta() { return idRuta; }
	public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public DataCiudad getCiudadOrigen() { return ciudadOrigen; }
    public DataCiudad getCiudadDestino() { return ciudadDestino; }
    public int getHora() { return hora; }
    public Date getFechaAlta() { return fechaAlta; }
    public BigDecimal getCostoTurista() { return costoTurista; }
    public BigDecimal getCostoEjecutivo() { return costoEjecutivo; }
    public int getCostoEquipajeExtra() { return costoEquipajeExtra; }
    public DataCategoria getCategoria() { return categoria; }
    public String getNicknameAerolinea() { return nicknameAerolinea; }
    
    @Override
    public String toString() {
        return ciudadOrigen.getNombre() + " - " + ciudadOrigen.getPais()
            + " --> "
            + ciudadDestino.getNombre() + " - " + ciudadDestino.getPais();
    }

}