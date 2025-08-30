
package Logica;

import java.math.BigDecimal;
import java.util.Date;

public final class ManejadorCompraPaquete {

    private ManejadorCompraPaquete() {}

    public static CompraPaquete toEntity(DataCompraPaquete dto, Cliente cliente, Paquete paquete) {
        if (dto == null || cliente == null || paquete == null)
            throw new IllegalArgumentException("Datos insuficientes para crear la compra");

        Date fecha = copy(dto.getFechaCompra());
        // costo: si dto trae null, usar el del paquete
        BigDecimal costo = (dto.getCosto() != null) ? dto.getCosto() : paquete.getCosto();

        // vencimiento: si dto trae null, calcular = fecha + validez (días)
        Date venc = (dto.getVencimiento() != null)? copy(dto.getVencimiento()) : addDays(fecha, paquete.getValidez());

        // IMPORTANTE: no hacemos 'new' en Sistema: lo hacemos acá.
        return new CompraPaquete(
                cliente,
                paquete,
                fecha,
                venc,
                costo
        );
    }

    public static DataCompraPaquete toDTO(CompraPaquete e) {
        return new DataCompraPaquete(
                e.getPaquete().getNombre(),
                e.getCliente().getNickname(),
                e.getFechaCompra(),
                e.getCosto(),
                e.getVencimiento()                             
        );
    }

    // utils
    private static Date copy(Date d) { return (d == null) ? null : new Date(d.getTime()); }

    private static Date addDays(Date base, int days) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(base);
        cal.add(java.util.Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }
}
