package Logica;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data type para la compra de un paquete turístico.
 */
public class DataCompraPaquete {
  private final String nombrePaquete; // identifica el paquete (único por nombre)
  private final String nicknameCliente; // identifica el cliente (único por nickname)
  private final Date fechaCompra; // requerida por el caso de uso
  private final Date vencimiento; // si es null -> calcular = fechaCompra + p.validez
  private final BigDecimal costo; // si es null -> usar p.getCosto()

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
