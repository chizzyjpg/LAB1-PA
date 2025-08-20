
package Logica;

import java.util.HashMap;
import java.util.Map;


public class Sistema implements ISistema {
    

    // “Persistencia” en memoria por ahora.
    private final Map<String, Usuario> usuariosPorNickname = new HashMap<>();

    public Sistema() {}

    @Override
    public void registrarUsuario(DataUsuario data) {
        if (data == null) throw new IllegalArgumentException("Los datos no pueden ser nulos");

        // Validaciones de unicidad
        if (existeNickname(data.getNickname())) {
            throw new IllegalArgumentException("El nickname ya está en uso");
        }
        if (existeNombre(data.getNombre())) {
            throw new IllegalArgumentException("El nombre ya está en uso");
        }

        // DTO -> Entidad (polimórfico)
        Usuario entity = ManejadorUsuario.toEntity(data);

        // Guardamos por nickname (clave)
        usuariosPorNickname.put(entity.getNickname(), entity);
    }

    @Override
    public boolean existeNickname(String nickname) {
        if (nickname == null) return false;
        return usuariosPorNickname.containsKey(nickname.toLowerCase())
            || usuariosPorNickname.containsKey(nickname); // por si no normalizás
    }

    @Override
    public boolean existeNombre(String nombre) {
        if (nombre == null) return false;
        return usuariosPorNickname.values().stream()
                .anyMatch(u -> u.getNombre() != null
                        && u.getNombre().equalsIgnoreCase(nombre));
    }

    @Override
    public DataCliente verInfoCliente(String nickname) {
        Usuario u = usuariosPorNickname.get(nickname);
        if (u instanceof Cliente c) {
            // Entidad -> DTO
            DataUsuario dto = ManejadorUsuario.toDTO(c);
            return (DataCliente) dto; // seguro, porque c es Cliente
        }
        return null;
    }

    @Override
    public DataAerolinea verInfoAerolinea(String nickname) {
        Usuario u = usuariosPorNickname.get(nickname);
        if (u instanceof Aerolinea a) {
            DataUsuario dto = ManejadorUsuario.toDTO(a);
            return (DataAerolinea) dto;
        }
        return null;
    }
}