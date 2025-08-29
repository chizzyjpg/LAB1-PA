package Logica;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            p.getCosto()
        );
    }

    public static List<DataPaquete> toDTOs(List<Paquete> paquetes) {
        return paquetes.stream().map(ManejadorPaquete::toDTO).collect(Collectors.toList());
    }
    
    public static List<Paquete> toEntities(List<? extends DataPaquete> dtos) {
		if (dtos == null) return Collections.emptyList();
		return dtos.stream().filter(Objects::nonNull).map(ManejadorPaquete::toEntity).collect(Collectors.toList());
	}
}