package Logica;

import java.util.*;
import java.util.stream.Collectors;

public class ManejadorAerolinea {

    private ManejadorAerolinea() {}

    // Obtener
    public static Aerolinea toEntity(DataAerolinea aero) {
        Objects.requireNonNull(aero, "Los datos no pueden ser nulos");
        return new Aerolinea(aero.getNickname(), aero.getNombre(), aero.getEmail(), aero.getDescripcion(), aero.getSitioWeb());
    }

    public static DataAerolinea toData(Aerolinea a) {
    	Objects.requireNonNull(a, "La aerolinea no puede ser nula");
        return new DataAerolinea(a.getNickname(), a.getNombre(), a.getEmail(),
                                 a.getDescGeneral(), a.getLinkWeb());
    }
    
    // HELLPERS
    
    public static List<Aerolinea> toEntities(List<? extends DataAerolinea> dtos) {
		if (dtos == null) return Collections.emptyList();
		return dtos.stream()
				   .filter(Objects::nonNull)
				   .map(ManejadorAerolinea::toEntity)
				   .collect(Collectors.toList());
	}
    
    public static List<DataAerolinea> toDatas(List<? extends Aerolinea> entities) {
		if (entities == null) return Collections.emptyList();
		return entities.stream()
					   .filter(Objects::nonNull)
					   .map(ManejadorAerolinea::toData)
					   .collect(Collectors.toList());
	}
}
