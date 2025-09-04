package Logica;

import java.util.List;
import java.util.Objects;

import BD.VueloService;

public class ManejadorVueloEspecifico {
	
	public static VueloEspecifico toEntity(DataVueloEspecifico v) {
		Objects.requireNonNull(v, "Los datos no pueden ser nulos");
		VueloEspecifico ve = new VueloEspecifico(
				v.getNombre(),
				v.getFecha(),
				v.getDuracion(),
				v.getMaxAsientosTur(),
				v.getMaxAsientosEjec(),
				v.getFechaAlta()//,
				//v.getRuta()
		);
		
		try {
			new VueloService().registrarVuelo(ve);
		} catch (Exception ex) {
			throw new IllegalStateException("Error al crear el vuelo: " + ex.getMessage(), ex);
		}
		return ve;
	}
	
	public static DataVueloEspecifico toData(VueloEspecifico v) {
		Objects.requireNonNull(v, "El vuelo no puede ser nulo");
		return new DataVueloEspecifico(
				v.getNombre(),
				v.getFecha(),
				v.getDuracion(),
				v.getMaxAsientosTur(),
				v.getMaxAsientosEjec(),
				v.getFechaAlta()//,
				//v.getRuta()
		);
	}
	
	// HELLPERS
	
	public static List<VueloEspecifico> toEntities(List<? extends DataVueloEspecifico> dtos) {
		if (dtos == null) return List.of();
		return dtos.stream()
				   .filter(Objects::nonNull)
				   .map(ManejadorVueloEspecifico::toEntity)
				   .toList();
	}
	
	public static List<DataVueloEspecifico> toDatas(List<? extends VueloEspecifico> entities) {
		if (entities == null) return List.of();
		return entities.stream()
					   .filter(Objects::nonNull)
					   .map(ManejadorVueloEspecifico::toData)
					   .toList();
	}

}
