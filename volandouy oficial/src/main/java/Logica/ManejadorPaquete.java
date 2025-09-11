package Logica;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import BD.PaqueteService;

public final class ManejadorPaquete {
    private ManejadorPaquete() {}

    public static Paquete toEntity(DataPaquete dto) {
        if (dto == null) return null;
        Paquete p = new Paquete(
            dto.getNombre(),
            dto.getDescripcion(),
            dto.getCantRutas(),
            dto.getTipoAsiento(),
            dto.getDescuento(),
            dto.getValidez(),
            dto.getCosto()
        );
        
        return p;
    }

    public static DataPaquete toDTO(Paquete p) {
        if (p == null) return null;
        return new DataPaquete(
            p.getNombre(),
            p.getDescripcion(),
            p.getCantRutas(),
            p.getTipoAsiento(),
            p.getDescuento(),
            p.getValidez(),
            p.getCosto(),
            p.getRutasIncluidas()
        );
    }

    public static List<DataPaquete> toDTOs(List<Paquete> paquetes) {
        return paquetes.stream().map(ManejadorPaquete::toDTO).collect(Collectors.toList());
    }
    
    public static List<Paquete> toEntities(List<? extends DataPaquete> dtos) {
		if (dtos == null) return Collections.emptyList();
		return dtos.stream().filter(Objects::nonNull).map(ManejadorPaquete::toEntity).collect(Collectors.toList());
	}
    
    ////////////////////////
    ///
    ///
    /// USO EL MISMO MANEJADOR PARA LAS ALTAS QUE TIENEN MENOS PARAMETROS
    ///
    ///////////////////
    
    public static Paquete altaToEntity(DataPaqueteAlta dto) {
        if (dto == null) return null;
        Paquete p = new Paquete(
        		dto.getNombre(),
                dto.getDescripcion(),
                /* cantRutas */ 0,
                /* tipoAsiento */ null,        // un solo tipo por paquete, se definirá luego si corresponde
                /* descuento */ dto.getDescuento(),
                /* validez */ dto.getValidez(),
                /* costo */ null              // aún no definido
        );
        p.setFechaAlta(dto.getFechaAlta());
        try {
        	new PaqueteService().crearPaquete(p);
        } catch (Exception ex) {
        	throw new IllegalStateException("Error al crear el paquete: " + ex.getMessage(), ex);
        }
        p.setFechaAlta(copia(dto.getFechaAlta()));
        return p;
    }
    
    public static List<Paquete> altaToEntities(List<? extends DataPaqueteAlta> dtos) {
		if (dtos == null) return Collections.emptyList();
		return dtos.stream().filter(Objects::nonNull).map(ManejadorPaquete::altaToEntity).collect(Collectors.toList());
	}
    
    ///
    //////// HELPERS DE FECHA
    ///
    
    private static java.util.Date copia(java.util.Date d) {
        return (d == null) ? null : new java.util.Date(d.getTime());
    }
}