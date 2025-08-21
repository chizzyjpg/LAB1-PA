
package Logica;


import java.util.HashMap;
import java.util.Map;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ManejadorUsuario {

    private ManejadorUsuario() {}

    // =========================
    // DTO -> ENTIDAD (dominio)
    // =========================
    public static Usuario toEntity(DataUsuario dto) {
        Objects.requireNonNull(dto, "DataUsuario no puede ser null");

        if (dto instanceof DataCliente dc) {
            return new Cliente(
                /* nombre   */ dc.getNombre(),
                /* nickname */ dc.getNickname(),
                /* email    */ dc.getEmail(),
                /* apellido */ dc.getApellido(),
                /* fechaNac */ copy(dc.getFechaNac()),
                /* nacional */ dc.getNacionalidad(),
                /* tipoDoc  */ dc.getTipoDocumento(),
                /* numDoc   */ dc.getNumDocumento()
            );
        } else if (dto instanceof DataAerolinea da) {
            return new Aerolinea(
                /* nombre     */ da.getNombre(),
                /* nickname   */ da.getNickname(),
                /* email      */ da.getEmail(),
                /* descripcion*/ da.getDescripcion(),
                /* sitioWeb   */ da.getSitioWeb()
            );
        }

        throw new IllegalArgumentException("Tipo de DataUsuario no soportado: " + dto.getClass());
    }

    // =========================
    // ENTIDAD -> DTO
    // =========================
    public static DataUsuario toDTO(Usuario u) {
        Objects.requireNonNull(u, "Usuario no puede ser null");

        if (u instanceof Cliente c) {
            return new DataCliente(
                /* nombre   */ c.getNombre(),
                /* nickname */ c.getNickname(),
                /* email    */ c.getEmail(),
                /* apellido */ c.getApellido(),
                /* fechaNac */ copy(c.getFechaNac()),
                /* nacional */ c.getNacionalidad(),
                /* tipoDoc  */ c.getTipoDocumento(),
                /* numDoc   */ c.getNumDocumento()
            );
        } else if (u instanceof Aerolinea a) {
            return new DataAerolinea(
                /* nombre     */ a.getNombre(),
                /* nickname   */ a.getNickname(),
                /* email      */ a.getEmail(),
                /* descripcion*/ a.getDescGeneral(),
                /* sitioWeb   */ a.getLinkWeb()
            );
        }

        throw new IllegalArgumentException("Tipo de Usuario no soportado: " + u.getClass());
    }

    // =========================
    // Helpers para listas
    // =========================
    public static List<Usuario> toEntities(List<? extends DataUsuario> dtos) {
        return dtos.stream().map(ManejadorUsuario::toEntity).collect(Collectors.toList());
    }

    public static List<DataUsuario> toDTOs(List<? extends Usuario> users) {
        return users.stream().map(ManejadorUsuario::toDTO).collect(Collectors.toList());
    }

    // =========================
    // Utilidad: copia defensiva
    // =========================
    private static Date copy(Date d) {
        return (d == null) ? null : new Date(d.getTime());
    }
}