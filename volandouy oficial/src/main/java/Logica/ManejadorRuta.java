package Logica;

import java.util.*;
import java.util.stream.Collectors;


import BD.CategoriaService;
import BD.CiudadService;

public class ManejadorRuta {

    private ManejadorRuta() {}


    // ===== Obtener / Listar =====
    public static Ruta toEntity(DataRuta data) {
        Objects.requireNonNull(data, "Los datos no pueden ser nulos");
        // Buscar Ciudad y Categoria reales a partir de los DTOs
        Ciudad origen = new CiudadService().buscarPorNombre(data.getCiudadOrigen().getNombre());
        Ciudad destino = new CiudadService().buscarPorNombre(data.getCiudadDestino().getNombre());
        Categoria categoria = new CategoriaService().buscarPorNombre(data.getCategoria().getNombre());
        Ruta r = new Ruta(
            data.getNombre(), data.getDescripcion(),
            origen, destino,
            data.getHora(), data.getFechaAlta(),
            data.getCostoTurista(), data.getCostoEquipajeExtra(), data.getCostoEjecutivo(), categoria, data.getDescripcionCorta()
        );
        // Ya no se persiste aquí, solo se crea la entidad
        return r;
    }

    public static DataRuta toData(Ruta r) {
        Objects.requireNonNull(r, "La ruta no puede ser nula");
        DataCiudad origen = new DataCiudad(
            r.getOrigen().getNombre(),
            r.getOrigen().getPais(),
            r.getOrigen().getNombreAeropuerto(),
            r.getOrigen().getDescripcionAeropuerto(),
            r.getOrigen().getFechaAlta(),
            r.getOrigen().getSitioWeb()
        );
        DataCiudad destino = new DataCiudad(
            r.getDestino().getNombre(),
            r.getDestino().getPais(),
            r.getDestino().getNombreAeropuerto(),
            r.getDestino().getDescripcionAeropuerto(),
            r.getDestino().getFechaAlta(),
            r.getDestino().getSitioWeb()
        );
        DataCategoria categoria = new DataCategoria(r.getCategoriaR().getNombre());
        DataRuta dto = new DataRuta(
            r.getNombre(), r.getDescripcion(),
            origen, destino,
            r.getHora(), r.getFechaAlta(),
            r.getCostoTurista(), r.getCostoEquipajeExtra(), r.getCostoEjecutivo(), categoria,
            "" // No se conoce el nicknameAerolinea aquí
            , r.getEstado(), r.getDescripcionCorta()
        );
        dto.setIdRuta(r.getIdRuta());
        return dto;
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