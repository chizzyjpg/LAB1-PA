package Logica;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

/**
 * Data type para la compra de un paquete turístico.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dataCompraPaquete")
@XmlType(propOrder = {
        "nombrePaquete",
        "nicknameCliente",
        "fechaCompra",
        "vencimiento",
        "costo"
})
public class DataCompraPaquete {
  private String nombrePaquete; // identifica el paquete (único por nombre)
  private String nicknameCliente; // identifica el cliente (único por nickname)
  private Date fechaCompra; // requerida por el caso de uso
  private Date vencimiento; // si es null -> calcular = fechaCompra + p.validez
  private BigDecimal costo; // si es null -> usar p.getCosto()

    public DataCompraPaquete(){
        super();
    }
  /**
   * Constructor de DataCompraPaquete.
   * 
   */
  public DataCompraPaquete(String nombrePaquete, String nicknameCliente, Date fechaCompra,
      BigDecimal costo, Date vencimiento) {
    this.nombrePaquete = nombrePaquete;
    this.nicknameCliente = nicknameCliente;
    this.fechaCompra = (fechaCompra == null) ? null : new Date(fechaCompra.getTime());
    this.costo = costo;
    this.vencimiento = (vencimiento == null) ? null : new Date(vencimiento.getTime());
  }

  public String getNombrePaquete() {
    return nombrePaquete;
  }

  public String getNicknameCliente() {
    return nicknameCliente;
  }

  public Date getFechaCompra() {
    return (fechaCompra == null) ? null : new Date(fechaCompra.getTime());
  }

  public BigDecimal getCosto() {
    return costo;
  }

  public Date getVencimiento() {
    return (vencimiento == null) ? null : new Date(vencimiento.getTime());
  }
}
