package Logica;

import BD.PaqueteService;
import java.math.BigDecimal;
import java.util.Date;


/**
 * ManejadorCompraPaquete: clase de utilidad 
 * para convertir entre DTOs de compra de paquete y entidades.
 */
public final class ManejadorCompraPaquete {

  private static PaqueteService paqueteService = new PaqueteService();

  private ManejadorCompraPaquete() {
  }
  
  /**
   * Convierte un DTO de compra de paquete a su entidad de dominio correspondiente.
   * 
   */
  public static CompraPaquete toEntity(DataCompraPaquete dto, Cliente cliente, Paquete paquete) {
    if (dto == null || cliente == null || paquete == null) {
      throw new IllegalArgumentException("Datos insuficientes para crear la compra");      
    }
    Date fecha = copy(dto.getFechaCompra());
    // costo: si dto trae null, usar el del paquete
    BigDecimal costo = paquete.getCosto();// (dto.getCosto() != null) ? dto.getCosto() :
                                          // paquete.getCosto();

    // vencimiento: si dto trae null, calcular = fecha + validez (días)
    Date venc = (dto.getVencimiento() != null) ? copy(dto.getVencimiento())
        : addDays(fecha, paquete.getValidez());

    CompraPaquete cp = new CompraPaquete(cliente, paquete, fecha, venc, costo);
    try {
      paqueteService.comprarPaquete(cp, cliente, paquete);
    } catch (Exception ex) {
      throw new IllegalStateException("Error al crear la compra: " + ex.getMessage(), ex);
    }
    return cp;
  }

  /**
   * Convierte una entidad de compra de paquete a su DTO correspondiente.
   * 
   */
  public static DataCompraPaquete toDTO(CompraPaquete e) {
    return new DataCompraPaquete(e.getPaquete().getNombre(), e.getCliente().getNickname(),
        e.getFechaCompra(), e.getCosto(), e.getVencimiento());
  }

  // utils
  private static Date copy(Date d) {
    return (d == null) ? null : new Date(d.getTime());
  }

  private static Date addDays(Date base, int days) {
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.setTime(base);
    cal.add(java.util.Calendar.DAY_OF_YEAR, days);
    return cal.getTime();
  }

  static void setServiceForTests(PaqueteService s) {
    paqueteService = (s == null) ? new PaqueteService() : s;
  }
}
