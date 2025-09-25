package Logica;

import java.util.*;
import java.util.stream.Collectors;

public class ManejadorReserva {

    private ManejadorReserva() {}

    public static Reserva toEntity(DataReserva res) {
		Objects.requireNonNull(res, "Los datos no pueden ser nulos");
		Reserva r = new Reserva(res.getFechaReserva(), res.getTipoAsiento(), res.getEquipaje(),
						   res.getCantEquipajeExtra(), res.getCostoTotal(), ManejadorCliente.toEntity(res.getNickCliente()));
		
		if (res.getPasajes() != null) {
	        for (DataPasaje dp : res.getPasajes()) {
	            Pasaje p = ManejadorPasaje.toEntity(dp);
	            p.setReserva(r); // Setear la reserva en el pasaje
	            r.getPasajes().add(p);
	        }
	    }
	    return r;
    }


    public static DataReserva toData(Reserva res) {
        Objects.requireNonNull(res, "La reserva no puede ser nula");
        DataReserva dr = new DataReserva(res.getIdReserva(), res.getFechaReserva(), res.getTipoAsiento(), res.getEquipaje(),
                res.getCantEquipajeExtra(), res.getCostoTotal(), ManejadorCliente.toData(res.getCliente()));
        // Copiar pasajes
        if (res.getPasajes() != null && !res.getPasajes().isEmpty()) {
            List<DataPasaje> dataPasajes = res.getPasajes().stream()
                    .map(ManejadorPasaje::toData)
                    .collect(Collectors.toList());
            dr.setPasajes(dataPasajes);
        }
        return dr;
    }
    
    // HELLPERS
    
    public static List<Reserva> toEntities(List<? extends DataReserva> dtos) {
    	if (dtos == null) return Collections.emptyList();
    	return dtos.stream()
						   .filter(Objects::nonNull)
						   .map(ManejadorReserva::toEntity)
						   .collect(Collectors.toList());
	}
    
    public static List<DataReserva> toDatas(List<? extends Reserva> entities) {
		if (entities == null) return Collections.emptyList();
		return entities.stream()
						   .filter(Objects::nonNull)
						   .map(ManejadorReserva::toData)
						   .collect(Collectors.toList());
	}
}