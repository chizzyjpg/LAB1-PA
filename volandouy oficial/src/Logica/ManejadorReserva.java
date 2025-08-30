package Logica;

import java.util.*;
import java.util.stream.Collectors;

public class ManejadorReserva {

    private ManejadorReserva() {}

    // Obtener
    public static Reserva toEntity(DataReserva res) {
		Objects.requireNonNull(res, "Los datos no pueden ser nulos");
		return new Reserva(res.getFechaReserva(), res.getTipoAsiento(), res.getEquipaje(),
						   res.getCantEquipajeExtra(), res.getCostoTotal());
    }


    public static DataReserva toData(Reserva res) {
    	Objects.requireNonNull(res, "La reserva no puede ser nula");
		return new DataReserva(res.getIdReserva, res.getFechaReserva(), res.getTipoAsiento(), res.getEquipaje(),
							   res.getCantEquipajeExtra(), res.getCostoTotal());
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
