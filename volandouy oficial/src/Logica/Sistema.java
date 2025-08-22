
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
    
    
    // ======================
    //  REGISTRAR USUARIOS
    // ======================
    
    
 // Normaliza claves para que "Juan", "juAN" y "juan" choquen correctamente ////// HELPERS
    
    
    private static String canonical(String s) {
        return (s == null) ? null : s.trim().toLowerCase(Locale.ROOT);
    }
    private static String canonicalEmail(String s) {
        return (s == null) ? null : s.trim().toLowerCase(Locale.ROOT);
    }
    
    private static java.util.Date copia(java.util.Date d) {
        return (d == null) ? null : new java.util.Date(d.getTime());
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
        String e = canonicalEmail(email);
        return usuariosPorNickname.values().stream()
                .anyMatch(u -> u.getEmail() != null
                        && canonicalEmail(u.getEmail()).equals(e));
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

    
    // ======================
    //  MODIFICAR USUARIOS
    // ======================
    
    @Override
    public void modificarCliente(String nickname, DataCliente nuevos) {
        if (nuevos == null) throw new IllegalArgumentException("Datos de cliente no pueden ser nulos");

        String key = canonical(nickname);
        Usuario u = usuariosPorNickname.get(key);
        if (!(u instanceof Cliente c))
            throw new IllegalArgumentException("No existe un cliente con ese nickname");

        // Validar que NO se cambie email ni nickname (según caso de uso)
        String emailActual = c.getEmail();
        String emailNuevo  = nuevos.getEmail();
        if (emailNuevo != null && !canonicalEmail(emailNuevo).equals(canonicalEmail(emailActual))) {
            throw new IllegalArgumentException("No se permite modificar el correo electrónico.");
        }
        if (nuevos.getNickname() != null && !canonical(nuevos.getNickname()).equals(key)) {
            throw new IllegalArgumentException("No se permite modificar el nickname.");
        }

        // Actualizar SOLO campos básicos permitidos
        c.setNombre(nuevos.getNombre());
        c.setApellido(nuevos.getApellido());
        c.setFechaNac(copia(nuevos.getFechaNac()));
        c.setNacionalidad(nuevos.getNacionalidad());
        c.setTipoDocumento(nuevos.getTipoDocumento());
        c.setNumDocumento(nuevos.getNumDocumento());
    }

    @Override
    public void modificarAerolinea(String nickname, DataAerolinea nuevos) {
        if (nuevos == null) throw new IllegalArgumentException("Datos de aerolínea no pueden ser nulos");

        String key = canonical(nickname);
        Usuario u = usuariosPorNickname.get(key);
        if (!(u instanceof Aerolinea a))
            throw new IllegalArgumentException("No existe una aerolínea con ese nickname");

        // Validar que NO se cambie email ni nickname
        String emailActual = a.getEmail();
        String emailNuevo  = nuevos.getEmail();
        if (emailNuevo != null && !canonicalEmail(emailNuevo).equals(canonicalEmail(emailActual))) {
            throw new IllegalArgumentException("No se permite modificar el correo electrónico.");
        }
        if (nuevos.getNickname() != null && !canonical(nuevos.getNickname()).equals(key)) {
            throw new IllegalArgumentException("No se permite modificar el nickname.");
        }

        // Actualizar SOLO campos básicos permitidos
        a.setNombre(nuevos.getNombre());
        a.setDescGeneral(nuevos.getDescripcion());
        a.setLinkWeb(nuevos.getSitioWeb());
    }
    
    
}