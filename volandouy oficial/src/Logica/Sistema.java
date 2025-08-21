
package Logica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Sistema implements ISistema {
    

    // “Persistencia” en memoria por ahora.
    private final Map<String, Usuario> usuariosPorNickname = new HashMap<>(); // guardamos ENTIDADES (dominio), indexadas por nickname

    public Sistema() {}
    
    
 // Normaliza claves para que "Juan", "juAN" y "juan" choquen correctamente
    private static String canonical(String s) {
        return (s == null) ? null : s.trim().toLowerCase(Locale.ROOT);
    }

    @Override
    public void registrarUsuario(DataUsuario data) {
        if (data == null) throw new IllegalArgumentException("Los datos no pueden ser nulos");

        // Validaciones de unicidad
        if (existeNickname(data.getNickname())) {
            throw new IllegalArgumentException("El nickname ya está en uso");
        }
        if (existeEmail(data.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        // DTO -> Entidad (polimórfico)
        Usuario entity = ManejadorUsuario.toEntity(data);

        // Clave canónica
        String key = canonical(entity.getNickname());
        usuariosPorNickname.put(key, entity);
    }

    @Override
    public boolean existeNickname(String nickname) {
        if (nickname == null) return false;
        return usuariosPorNickname.containsKey(canonical(nickname));
    }

    @Override
    public boolean existeEmail(String email) {
        if (email == null) return false;
        return usuariosPorNickname.values().stream()
                .anyMatch(u -> u.getNombre() != null
                        && u.getNombre().equalsIgnoreCase(email));
    }

    @Override
    public DataCliente verInfoCliente(String nickname) {
        Usuario u = usuariosPorNickname.get(canonical(nickname));
        if (u instanceof Cliente c) {
        	return (DataCliente) ManejadorUsuario.toDTO(c);
        }
        return null;
    }

    @Override
    public DataAerolinea verInfoAerolinea(String nickname) {
        Usuario u = usuariosPorNickname.get(canonical(nickname));
        if (u instanceof Aerolinea a) {
            return (DataAerolinea) ManejadorUsuario.toDTO(a);
        }
        return null;
    }
    
    @Override
    public List<DataUsuario> listarUsuarios() {
        return ManejadorUsuario.toDTOs(new ArrayList<>(usuariosPorNickname.values()));
    }

    
}