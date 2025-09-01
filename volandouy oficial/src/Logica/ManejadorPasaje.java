package Logica;

import java.util.*;
import java.util.stream.Collectors;

public class ManejadorPasaje {
	
	private ManejadorPasaje() {}
		
		
	public static Pasaje toEntity(DataPasaje pas) {
		Objects.requireNonNull(pas, "Los datos no pueden ser nulos");
		return new Pasaje(pas.getNombre(), pas.getApellido());
	}
		
	public static DataPasaje toData(Pasaje pas) {
		Objects.requireNonNull(pas, "El pasaje no puede ser nulo");
		return new DataPasaje(pas.getNombre(), pas.getApellido());
	}
		
	// HELLPERS
		
	public static List<Pasaje> toEntities(List<? extends DataPasaje> dtos) {
		if (dtos == null) return Collections.emptyList();
		return dtos.stream()
						   .filter(Objects::nonNull)
						   .map(ManejadorPasaje::toEntity)
						   .collect(Collectors.toList());
	}
		
	public static List<DataPasaje> toDatas(List<? extends Pasaje> entities) {
		if (entities == null) return Collections.emptyList();
		return entities.stream()
						   .filter(Objects::nonNull)
						   .map(ManejadorPasaje::toData)
						   .collect(Collectors.toList());
	}
	
}
