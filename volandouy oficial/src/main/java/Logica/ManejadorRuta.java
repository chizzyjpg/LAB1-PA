package Logica;

import java.util.*;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import BD.RutaVueloService;

public class ManejadorRuta {

    private ManejadorRuta() {}


    // ===== Obtener / Listar =====
    public static Ruta toEntity(DataRuta data) {
    	Objects.requireNonNull(data, "Los datos no pueden ser nulos");
    	Ciudad origen = ManejadorCiudad.toEntity(data.getCiudadOrigen());
    	Ciudad destino = ManejadorCiudad.toEntity(data.getCiudadDestino());
        Ruta r = new Ruta(
			data.getNombre(), data.getDescripcion(),
			origen, destino,
			data.getHora(), data.getFechaAlta(),
			data.getCostoBase(), data.getCostoEquipajeExtra(), data.getCategoria()
		);
        try {
        	new RutaVueloService().crearRutaVuelo(r.getNombre(), r.getDescripcion(), origen, destino, r.getHora(), r.getFechaAlta(), r.getCostoBase(), r.getCostoEquipajeExtra(), r.getCategoria());
        	JOptionPane.showMessageDialog(null, "Se insertó correctamente");
        }catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
		}
        return r;
    }

    
    public static DataRuta toData(Ruta r) {
    	Objects.requireNonNull(r, "La ruta no puede ser nula");
    	DataCiudad origen = ManejadorCiudad.toData(r.getOrigen());
    	DataCiudad destino = ManejadorCiudad.toData(r.getDestino());
        return new DataRuta(
            r.getNombre(), r.getDescripcion(),
            origen, destino,
            r.getHora(), r.getFechaAlta(),
            r.getCostoBase(), r.getCostoEquipajeExtra(), r.getCategoria()
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
