package Logica;

import java.util.*;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import BD.CiudadService;

public class ManejadorCiudad {
	
	private static CiudadService ciudadService = new CiudadService();
    
	public static Ciudad toEntity(DataCiudad ciudad) {
		Objects.requireNonNull(ciudad, "Los datos no pueden ser nulos");
		Ciudad c = new Ciudad(
				ciudad.getNombre(),
				ciudad.getPais(),
				ciudad.getNombreAeropuerto(),
				ciudad.getDescripcionAeropuerto(),
				ciudad.getFechaAlta(),
				ciudad.getSitioWeb()
		);
		try {
        	ciudadService.crearCiudad(c);
        	JOptionPane.showMessageDialog(null, "Se insertó correctamente");
        }catch (Exception ex) {
        	JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
		return c;
	}

    public static DataCiudad toData(Ciudad c) {
    	Objects.requireNonNull(c, "La ciudad no puede ser nula");
        return new DataCiudad(
                c.getNombre(),
                c.getPais(),
                c.getNombreAeropuerto(),
                c.getDescripcionAeropuerto(),
                c.getFechaAlta(),
                c.getSitioWeb()
        );
    }
    
    // HELLPERS
    
    public static List<Ciudad> toEntities(List<? extends DataCiudad> dtos) {
		if (dtos == null) return Collections.emptyList();
		return dtos.stream()
				   .filter(Objects::nonNull)
				   .map(ManejadorCiudad::toEntity)
				   .collect(Collectors.toList());
	}
    
    public static List<DataCiudad> toDatas(List<? extends Ciudad> entities) {
		if (entities == null) return Collections.emptyList();
		return entities.stream()
					   .filter(Objects::nonNull)
					   .map(ManejadorCiudad::toData)
					   .collect(Collectors.toList());
	}
    
    public static void setCiudadServiceForTests(CiudadService cs) {
        ciudadService = cs;
      }
}
