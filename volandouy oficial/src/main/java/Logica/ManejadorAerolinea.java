package Logica;

import java.util.*;
import java.util.stream.Collectors;

import BD.AerolineaService;

public class ManejadorAerolinea {

    private ManejadorAerolinea() {}

    // Obtener
    public static Aerolinea toEntity(DataAerolinea aero) {
        Objects.requireNonNull(aero, "Los datos no pueden ser nulos");
        Aerolinea a = new Aerolinea(aero.getNombre(), aero.getNickname(), aero.getEmail(), aero.getDescripcion(), aero.getSitioWeb());
        try {
			new AerolineaService().crearAerolinea(a.getNombre(), a.getNickname(), a.getEmail(), a.getDescGeneral(), a.getLinkWeb());
		} catch (Exception ex) {
			throw new IllegalStateException("Error al crear la aerolínea: " + ex.getMessage(), ex);
		}
        return a;
    }

    public static DataAerolinea toData(Aerolinea a) {
    	Objects.requireNonNull(a, "La aerolinea no puede ser nula");
        return new DataAerolinea(a.getNombre(), a.getNickname(), a.getEmail(),
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
