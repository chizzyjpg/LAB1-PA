package Logica;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase que representa la compra de un paquete por parte de un cliente.
 */
@Entity
@Table(name = "CompraPaquete")
public class CompraPaquete {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(optional = false)
  private Cliente cliente;
  @ManyToOne(optional = false)
  private Paquete paquete;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date fechaCompra;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date vencimiento;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal costo; // normalmente = paquete.costo

  
  /*
  * Constructor por defecto requerido por JPA.
  */
  protected CompraPaquete() {
  }

  /**
   * Constructor de CompraPaquete.
   */
  public CompraPaquete(Cliente cliente, Paquete paquete, Date fechaCompra, Date vencimiento,
      BigDecimal costo) {
    this.cliente = cliente;
    this.paquete = paquete;
    this.fechaCompra = (fechaCompra == null) ? new Date() : new Date(fechaCompra.getTime());
    this.vencimiento = (vencimiento == null) ? null : new Date(vencimiento.getTime());
    this.costo = costo;
  }

  void setId(int id) {
    this.id = id;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public Paquete getPaquete() {
    return paquete;
  }

  public void setPaquete(Paquete paquete) {
    this.paquete = paquete;
  }

  public Date getFechaCompra() {
    return fechaCompra;
  }

  public void setFechaCompra(Date fechaCompra) {
    this.fechaCompra = fechaCompra;
  }

  public Date getVencimiento() {
    return vencimiento;
  }

  public void setVencimiento(Date vencimiento) {
    this.vencimiento = vencimiento;
  }

  public BigDecimal getCosto() {
    return costo;
  }

  public void setCosto(BigDecimal costo) {
    this.costo = costo;
  }
}
