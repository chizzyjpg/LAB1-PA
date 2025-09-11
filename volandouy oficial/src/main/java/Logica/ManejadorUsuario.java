package Logica;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import BD.ClienteService;
import BD.AerolineaService;

public final class ManejadorUsuario {

    private ManejadorUsuario() {} // Constructor privado: evita instanciar la clase (solo utilidad estática).
    
    ////DTO ES DATAT TRANSFER OBJET ---- OBJETO HECHO DATA TYPE PARA PASAR A CAPA DE PRESENTACION

    // =========================
    // DTO -> ENTIDAD (dominio)
    // =========================
    public static Usuario toEntity(DataUsuario dto) { // Convierte un DTO (capa de transporte) a Entidad de dominio.
        Objects.requireNonNull(dto, "DataUsuario no puede ser null"); // Valida que dto no sea null; si lo es, lanza NullPointerException con mensaje.

        if (dto instanceof DataCliente dc) {
            Cliente c = new Cliente(				 // si dto es DataCliente, lo "captura" en la variable dc ya casteada. // Crea una entidad Cliente desde el DTO DataCliente.
                /* nombre   */ dc.getNombre(),
                /* nickname */ dc.getNickname(),
                /* email    */ dc.getEmail(),
                /* apellido */ dc.getApellido(),
                /* fechaNac */ copy(dc.getFechaNac()), // Copia defensiva del Date (Date es mutable).
                /* nacional */ dc.getNacionalidad(),
                /* tipoDoc  */ dc.getTipoDocumento(),
                /* numDoc   */ dc.getNumDocumento()
            );
            
            try {
    			new ClienteService().crearCliente(c);
    		} catch (Exception ex) {
    			throw new IllegalStateException("Error al crear el cliente: " + ex.getMessage(), ex);
    		}
    		return c;
    		
        } else if (dto instanceof DataAerolinea da) { // Si el dto es de aerolínea, lo captura como da.
            Aerolinea a = new Aerolinea(					  // Crea la entidad Aerolinea a partir del DataAerolinea.
                /* nombre     */ da.getNombre(),
                /* nickname   */ da.getNickname(),
                /* email      */ da.getEmail(),
                /* descripcion*/ da.getDescripcion(),
                /* sitioWeb   */ da.getSitioWeb()
            );
            
            try {
    			new AerolineaService().crearAerolinea(a);
    		} catch (Exception ex) {
    			throw new IllegalStateException("Error al crear Aerolinea: " + ex.getMessage(), ex);
    		}
    		return a;
    	
 
        }
        throw new IllegalArgumentException("Tipo de DataUsuario no soportado: " + dto.getClass()); // Si llega aquí, el tipo de dto no es ninguno de los soportados.
    }

    // =========================
    // ENTIDAD -> DTO
    // =========================
    public static DataUsuario toDTO(Usuario u) { // Convierte una entidad de dominio a su DTO correspondiente.
        Objects.requireNonNull(u, "Usuario no puede ser null"); // Valida argumento no nulo.

        if (u instanceof Cliente c) { // Si la entidad es Cliente, la captura en c (ya casteada).
            return new DataCliente(   // Crea el DTO DataCliente a partir de la entidad Cliente.
                /* nombre   */ c.getNombre(),
                /* nickname */ c.getNickname(),
                /* email    */ c.getEmail(),
                /* apellido */ c.getApellido(),
                /* fechaNac */ copy(c.getFechaNac()), // Copia defensiva del Date.
                /* nacional */ c.getNacionalidad(),
                /* tipoDoc  */ c.getTipoDocumento(),
                /* numDoc   */ c.getNumDocumento()
            );
            
            
        } else if (u instanceof Aerolinea a) { // Si la entidad es Aerolinea, la captura en a.
            return new DataAerolinea(		   // Crea el DTO DataAerolinea desde la entidad.
                /* nombre     */ a.getNombre(),
                /* nickname   */ a.getNickname(),
                /* email      */ a.getEmail(),
                /* descripcion*/ a.getDescGeneral(), // Nota: nombres de getters distintos a los del DTO.
                /* sitioWeb   */ a.getLinkWeb()
            );
        }

        throw new IllegalArgumentException("Tipo de Usuario no soportado: " + u.getClass()); // Si la entidad no es un tipo soportado, se informa claramente.
    }

    // =========================
    // Helpers para listas
    // =========================
    public static List<Usuario> toEntities(List<? extends DataUsuario> dtos) {  // Convierte una lista de DTOs a entidades.
        return dtos.stream().map(ManejadorUsuario::toEntity).collect(Collectors.toList()); 
        //return dtos.stream() Convierte la lista a stream… // .map(ManejadorUsuario::toEntity aplica toEntity a cada elemento (method reference)… // .collect(Collectors.toList()); // y vuelve a materializar una List.
    }

    public static List<DataUsuario> toDTOs(List<? extends Usuario> users) {// Convierte una lista de entidades a DTOs.
        return users.stream().map(ManejadorUsuario::toDTO).collect(Collectors.toList());
        //return user.stream() Stream sobre la lista de usuarios… // .map(ManejadorUsuario::toDTO mapea cada entidad a su DTO… // .collect(Collectors.toList y colecta en una List.
    }

    // =========================
    // Utilidad: copia defensiva
    // =========================
    private static Date copy(Date d) { // Helper para evitar exponer mutabilidad de Date.
        return (d == null) ? null : new Date(d.getTime()); // Si d es null, retorna null; si no, crea un Date nuevo con el mismo epoch.
    }
}