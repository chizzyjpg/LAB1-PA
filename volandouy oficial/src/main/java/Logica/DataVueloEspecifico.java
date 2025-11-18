package Logica;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que representa los datos de un vuelo espec��fico.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dataVueloEspecifico")
@XmlType(propOrder = {"nombre", "fecha", "duracion", "maxAsientosTur", "maxAsientosEjec", "fechaAlta", "Druta" })
public class DataVueloEspecifico {

  private String nombre;
  private Date fecha;
  private int duracion;
  private int maxAsientosTur;
  private int maxAsientosEjec;
  private Date fechaAlta;
  private DataRuta Druta;

  public DataVueloEspecifico() {}
  /**
   * Constructor de la clase DataVueloEspecifico.
   *
   */
  public DataVueloEspecifico(String nombre, Date fecha, int duracion, int maxAsientosTur,
      int maxAsientosEjec, Date fechaAlta, DataRuta Druta) {
    this.nombre = nombre;
    this.fecha = fecha;
    this.duracion = duracion;
    this.maxAsientosTur = maxAsientosTur;
    this.maxAsientosEjec = maxAsientosEjec;
    this.fechaAlta = fechaAlta;
    this.Druta = Druta;
  }

  /**
   * Constructor de la clase DataVueloEspecifico sin DataRuta.
   *
   */
  public DataVueloEspecifico(String nombre, Date fecha, int duracion, int maxAsientosTur,
      int maxAsientosEjec, Date fechaAlta) {
    this.nombre = nombre;
    this.fecha = fecha;
    this.duracion = duracion;
    this.maxAsientosTur = maxAsientosTur;
    this.maxAsientosEjec = maxAsientosEjec;
    this.fechaAlta = fechaAlta;
    this.Druta = null;
  }

  public String getNombre() {
    return nombre;
  }

  public Date getFecha() {
    return fecha;
  }

  public int getDuracion() {
    return duracion;
  }

  public int getMaxAsientosTur() {
    return maxAsientosTur;
  }

  public int getMaxAsientosEjec() {
    return maxAsientosEjec;
  }

  public Date getFechaAlta() {
    return fechaAlta;
  }

  /**
   * Devuelve el DataRuta asociado al vuelo espec��fico.
   */
  public DataRuta getDRuta() {
    return Druta;
  }

  /**
   * Devuelve una representaci��n en cadena del vuelo específico.
   */
  public String toString() {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String sfd = sdf.format(fecha);

    return nombre + " - " + sfd;
  }
}