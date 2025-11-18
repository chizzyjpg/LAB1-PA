package Logica;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * DataPaquete: clase que representa la información de un paquete turístico.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "dataPaquete",
    propOrder = { "nombre", "descripcion", "cantRutas", "tipoAsiento", "descuento", "validez",
        "costo", "rutasIncluidas" }
)

public class DataPaquete {
  private String nombre;
  private String descripcion;
  private int cantRutas;
  private TipoAsiento tipoAsiento;
  private int descuento; // ej. 20 (%)
  private int validez; // días
  private BigDecimal costo;

  // NUEVO: ruta -> cupos (inmutable hacia afuera)
  private Set<String> rutasIncluidas;

  /* ================== CONSTRUCTORES ================== */

    public DataPaquete() {
        super();
    }
  // Nuevo constructor que recibe el mapa de cupos
  /**
   * Constructor de DataPaquete.
   *
   */
  public DataPaquete(String nombre, String descripcion, int cantRutas, TipoAsiento tipoAsiento,
      int descuento, int validez, BigDecimal costo, Set<String> rutasIncluidas) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.cantRutas = cantRutas;
    this.tipoAsiento = tipoAsiento;
    this.descuento = descuento;
    this.validez = validez;
    this.costo = costo;

    // copia defensiva + vista inmutable
    if (rutasIncluidas == null || rutasIncluidas.isEmpty()) {
      this.rutasIncluidas = Collections.emptySet();
    } else {
      this.rutasIncluidas = Collections.unmodifiableSet(new LinkedHashSet<>(rutasIncluidas));
    }
  }

  // Constructor viejo (compatibilidad): si no pasan mapa, queda vacío
  /**
   * Constructor de DataPaquete sin rutas incluidas.
   *
   */
  public DataPaquete(String nombre, String descripcion, int cantRutas, TipoAsiento tipoAsiento,
      int descuento, int validez, BigDecimal costo) {
    this(nombre, descripcion, cantRutas, tipoAsiento, descuento, validez, costo, null);
  }

  /* ================== GETTERS ================== */
  public String getNombre() {
    return nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public int getCantRutas() {
    return cantRutas;
  }

  public TipoAsiento getTipoAsiento() {
    return tipoAsiento;
  }

  public int getDescuento() {
    return descuento;
  }

  public int getValidez() {
    return validez;
  }

  public BigDecimal getCosto() {
    return costo;
  }

  // NUEVO
  public Set<String> getRutasIncluidas() {
    return rutasIncluidas;
  }

  @Override
  public String toString() {
    return nombre;
  }
}
