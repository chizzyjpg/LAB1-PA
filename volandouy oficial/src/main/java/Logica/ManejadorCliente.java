package Logica;

import java.util.List;
import java.util.Objects;

/**
 * ManejadorCliente es una clase utilitaria que proporciona métodos para convertir entre
 * objetos de tipo Cliente y DataCliente.
 */
public class ManejadorCliente {

  private ManejadorCliente() {
  }

  // Obtener
  /**
   * Convierte un objeto DataCliente a un objeto Cliente.
   */
  public static Cliente toEntity(DataCliente cli) {
    Objects.requireNonNull(cli, "Los datos no pueden ser nulos");
    return new Cliente(cli.getNombre(), cli.getNickname(), cli.getEmail(), cli.getContrasenia(),
        cli.getApellido(), cli.getFechaNac(), cli.getNacionalidad(), cli.getTipoDocumento(),
        cli.getNumDocumento());

  }

  /**
   * Convierte un objeto Cliente a un objeto DataCliente.
   */
  public static DataCliente toData(Cliente c) {
    Objects.requireNonNull(c, "El cliente no puede ser nulo");
    return new DataCliente(c.getNombre(), c.getNickname(), c.getEmail(), c.getContrasenia(),
        c.getApellido(), c.getFechaNac(), c.getNacionalidad(), c.getTipoDocumento(),
        c.getNumDocumento());
  }

  // HELLPERS
  /**
   * Convierte un arreglo de objetos Cliente a una lista de objetos DataCliente.
   */
  public static List<DataCliente> toDatas(Cliente[] entities) {
    if (entities == null) {
      return List.of();      
    }
    return List.of(entities).stream().filter(Objects::nonNull).map(ManejadorCliente::toData)
        .toList();
  }

  /**
   * Convierte una lista de objetos DataCliente a una lista de objetos Cliente.
   */
  public static List<Cliente> toEntities(List<? extends DataCliente> dtos) {
    if (dtos == null) {
      return List.of();      
    }
    return dtos.stream().filter(Objects::nonNull).map(ManejadorCliente::toEntity).toList();
  }
}
