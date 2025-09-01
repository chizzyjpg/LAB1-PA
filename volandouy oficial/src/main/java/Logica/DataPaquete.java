package Logica;

import java.math.BigDecimal;

public class DataPaquete {
    private final String nombre;
    private final String descripcion;
    private final int cantRutas;
    private final TipoAsiento tipoAsiento;
    private final int descuento;       // ej. 20 (%)
    private final int validez;         // días
    private final BigDecimal costo;

    public DataPaquete(String nombre, String descripcion, int cantRutas,
                       TipoAsiento tipoAsiento, int descuento, int validez, BigDecimal costo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantRutas = cantRutas;
        this.tipoAsiento = tipoAsiento;
        this.descuento = descuento;
        this.validez = validez;
        this.costo = costo;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getCantRutas() { return cantRutas; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }
    public int getDescuento() { return descuento; }
    public int getValidez() { return validez; }
    public BigDecimal getCosto() { return costo; }
}