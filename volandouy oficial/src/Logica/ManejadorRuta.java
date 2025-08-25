package Logica;

import java.util.*;
import java.util.stream.Collectors;

public class ManejadorRuta {

    private ManejadorRuta() {}


    // ===== Obtener / Listar =====
    public static Ruta toEntity(DataRuta data) {
    	Objects.requireNonNull(data, "Los datos no pueden ser nulos");
        return new Ruta(
			data.getNombre(), data.getDescripcion(),
			data.getCiudadOrigen(), data.getCiudadDestino(),
			data.getHora(), data.getFechaAlta(),
			data.getCostoBase(), data.getCostoEquipajeExtra()
		);
    }

    
    public static DataRuta toData(Ruta r) {
    	Objects.requireNonNull(r, "La ruta no puede ser nula");
        return new DataRuta(
            r.getNombre(), r.getDescripcion(),
            r.getOrigen(), r.getDestino(),
            r.getHora(), r.getFechaAlta(),
            r.getCostoBase(), r.getCostoEquipajeExtra()
        );
    }
    
    // HELLPERS
    
    public static List<Ruta> toEntities(List<? extends DataRuta> dtos) {
		if (dtos == null) return Collections.emptyList();
		return dtos.stream()
				   .filter(Objects::nonNull)
				   .map(ManejadorRuta::toEntity)
				   .collect(Collectors.toList());
	}
    
    public static List<DataRuta> toDatas(List<? extends Ruta> entities) {
		if (entities == null) return Collections.emptyList();
		return entities.stream()
					   .filter(Objects::nonNull)
					   .map(ManejadorRuta::toData)
					   .collect(Collectors.toList());
	}
}
