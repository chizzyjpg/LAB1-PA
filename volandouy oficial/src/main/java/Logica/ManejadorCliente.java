package Logica;

import java.util.Objects;
import java.util.List;

public class ManejadorCliente {
	
	private ManejadorCliente() {}
	
	// Obtener
	public static Cliente toEntity(DataCliente cli) {
		Objects.requireNonNull(cli, "Los datos no pueden ser nulos");
		return new Cliente(cli.getNombre(), cli.getNickname(), cli.getEmail(), cli.getContrasenia(), cli.getApellido(), cli.getFechaNac(),
						   cli.getNacionalidad(), cli.getTipoDocumento(), cli.getNumDocumento());
		
	}
	
	public static DataCliente toData(Cliente c) {
		Objects.requireNonNull(c, "El cliente no puede ser nulo");
		return new DataCliente(c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(), c.getApellido(), c.getFechaNac(),
				   c.getNacionalidad(), c.getTipoDocumento(), c.getNumDocumento());
	}
	
	// HELLPERS
	
	public static List<DataCliente> toDatas(Cliente[] entities) {
		if (entities == null) return List.of();
		return List.of(entities).stream()
					   .filter(Objects::nonNull)
					   .map(ManejadorCliente::toData)
					   .toList();
	}
	
	public static List<Cliente> toEntities(List<? extends DataCliente> dtos) {
		if (dtos == null) return List.of();
		return dtos.stream()
				   .filter(Objects::nonNull)
				   .map(ManejadorCliente::toEntity)
				   .toList();
	}
}
