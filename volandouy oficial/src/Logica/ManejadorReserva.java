package Logica;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ManejadorReserva {

	private ManejadorReserva() {}
	
	//DTO -> ENTIDAD
	
	public static Reserva toEntity (DataReserva dto) {
		Objects.requireNonNull(dto, "DataReserva no puede ser null"); // Valida que dto no sea null; si lo es, lanza NullPointerException con mensaje.
		return new Reserva (
				dto.getIdReserva(),
				dto.getFechaReserva(),
				dto.getTipoAsiento(),
				dto.getEquipaje(),
				dto.getCantEquipajeExtra(),
				dto.getCostoTotal()
				);		
	}
	
	//ENTIDAD -> DTO
	
	public static DataReserva toDTO (Reserva r) {
		Objects.requireNonNull(r, "Reserva no puede ser null"); // Valida argumento no nulo.
		return new DataReserva (
				r.getIdReserva(),
				r.getFechaReserva(),
				r.getTipoAsiento(),
				r.getEquipaje(),
				r.getCantEquipajeExtra(),
				r.getCostoTotal()
				);			
	}
	
	//HELPERS
	
	public static List<Reserva> toEntities(List<? extends DataReserva> dtos) {  // Convierte una lista de DTOs a entidades.
		return dtos.stream().map(ManejadorReserva::toEntity).collect(Collectors.toList()); 
	        //return dtos.stream() Convierte la lista a stream… // .map(ManejadorUsuario::toEntity aplica toEntity a cada elemento (method reference)… // .collect(Collectors.toList()); // y vuelve a materializar una List.
		}
	
	public static List<DataReserva> toDTOs(List<? extends Reserva> reservas) {// Convierte una lista de entidades a DTOs
		return reservas.stream().map(ManejadorReserva::toDTO).collect(Collectors.toList());
	        //return user.stream() Stream sobre la lista de usuarios… // .map(ManejadorUsuario::toDTO mapea cada entidad a su DTO… // .collect(Collectors.toList y colecta en una List.
		}
}
