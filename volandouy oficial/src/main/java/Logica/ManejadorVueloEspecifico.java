package Logica;

import java.util.List;
import java.util.Objects;

import BD.RutaVueloService;
import BD.VueloService;

public class ManejadorVueloEspecifico {
	
	public static VueloEspecifico toEntity(DataVueloEspecifico v) {
        Objects.requireNonNull(v, "Los datos no pueden ser nulos");
        Ruta ruta;
        if (v.getDRuta() != null && v.getDRuta().getIdRuta() > 0) {
            ruta = new RutaVueloService().buscarRutaPorId(v.getDRuta().getIdRuta());
            if (ruta == null) {
                throw new IllegalStateException("No se encontró la Ruta con id: " + v.getDRuta().getIdRuta());
            }
        } else {
            ruta = ManejadorRuta.toEntity(v.getDRuta());
        }
        VueloEspecifico vuelo = new VueloEspecifico(
            v.getNombre(),
            v.getFecha(),
            v.getDuracion(),
            v.getMaxAsientosTur(),
            v.getMaxAsientosEjec(),
            v.getFechaAlta(),
            ruta
        );
        // Persistir el vuelo aquí
        new VueloService().registrarVuelo(vuelo);
        return vuelo;
    }
	
	public static DataVueloEspecifico toData(VueloEspecifico v) {
		Objects.requireNonNull(v, "El vuelo no puede ser nulo");
		DataRuta dr = ManejadorRuta.toData(v.getRuta());
		return new DataVueloEspecifico(
				v.getNombre(),
				v.getFecha(),
				v.getDuracion(),
				v.getMaxAsientosTur(),
				v.getMaxAsientosEjec(),
				v.getFechaAlta(),
				dr
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