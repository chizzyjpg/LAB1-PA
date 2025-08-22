package Logica;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ManejadorCategoria{
	
	private ManejadorCategoria() {}
	
	
	//////// DTO -> ENTIDAD
	
	public static Categoria toEntity (DataCategoria dto) {
		Objects.requireNonNull(dto, "DataCategoria no puede ser null"); // Valida que dto no sea null; si lo es, lanza NullPointerException con mensaje.
		return new Categoria (
				dto.getNombre());		
		}
	
	///// ENITIDAD -> DTO
	
	public static DataCategoria toDTO (Categoria c) {
		Objects.requireNonNull(c, "Categoria no puede ser null"); // Valida argumento no nulo.
		return new DataCategoria (c.getNombre());			
		}
	
	// HELPERS
		
	public static List<Categoria> toEntities(List<? extends DataCategoria> dtos) {  // Convierte una lista de DTOs a entidades.
		return dtos.stream().map(ManejadorCategoria::toEntity).collect(Collectors.toList()); 
	        //return dtos.stream() Convierte la lista a stream… // .map(ManejadorUsuario::toEntity aplica toEntity a cada elemento (method reference)… // .collect(Collectors.toList()); // y vuelve a materializar una List.
		}

	public static List<DataCategoria> toDTOs(List<? extends Categoria> users) {// Convierte una lista de entidades a DTOs.
		return users.stream().map(ManejadorCategoria::toDTO).collect(Collectors.toList());
	        //return user.stream() Stream sobre la lista de usuarios… // .map(ManejadorUsuario::toDTO mapea cada entidad a su DTO… // .collect(Collectors.toList y colecta en una List.
		}
	

}
