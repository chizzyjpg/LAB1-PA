package Logica;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataPaquete {
    private final String nombre;
    private final String descripcion;
    private final int cantRutas;
    private final TipoAsiento tipoAsiento;
    private final int descuento;       // ej. 20 (%)
    private final int validez;         // días
    private final BigDecimal costo;

    // NUEVO: ruta -> cupos (inmutable hacia afuera)
    private final Map<String, Integer> cuposPorRuta;

    /* ================== CONSTRUCTORES ================== */

    // Nuevo constructor que recibe el mapa de cupos
    public DataPaquete(String nombre, String descripcion, int cantRutas,
                       TipoAsiento tipoAsiento, int descuento, int validez,
                       BigDecimal costo, Map<String, Integer> cuposPorRuta) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantRutas = cantRutas;
        this.tipoAsiento = tipoAsiento;
        this.descuento = descuento;
        this.validez = validez;
        this.costo = costo;

        // copia defensiva + vista inmutable
        if (cuposPorRuta == null || cuposPorRuta.isEmpty()) {
            this.cuposPorRuta = Collections.emptyMap();
        } else {
            this.cuposPorRuta = Collections.unmodifiableMap(new LinkedHashMap<>(cuposPorRuta));
        }
    }

    // Constructor viejo (compatibilidad): si no pasan mapa, queda vacío
    public DataPaquete(String nombre, String descripcion, int cantRutas,
                       TipoAsiento tipoAsiento, int descuento, int validez, BigDecimal costo) {
        this(nombre, descripcion, cantRutas, tipoAsiento, descuento, validez, costo, null);
    }

    /* ================== GETTERS ================== */
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getCantRutas() { return cantRutas; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }
    public int getDescuento() { return descuento; }
    public int getValidez() { return validez; }
    public BigDecimal getCosto() { return costo; }


    // NUEVO
    public Map<String, Integer> getCuposPorRuta() { return cuposPorRuta; }
    
    @Override
    public String toString() {
        return nombre; 
    }
}
