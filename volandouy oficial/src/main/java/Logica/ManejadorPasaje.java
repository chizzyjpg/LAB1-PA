package Logica;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ManejadorPasaje class handles the conversion between DataPasaje and Pasaje entities.
 */
public class ManejadorPasaje {

  private ManejadorPasaje() {
  }

  /**
   * Converts DataPasaje to Pasaje entity.
   */
  public static Pasaje toEntity(DataPasaje pas) {
    Objects.requireNonNull(pas, "Los datos no pueden ser nulos");
    return new Pasaje(pas.getNombre(), pas.getApellido());
  }

  /**
   * Converts Pasaje entity to DataPasaje.
   */
  public static DataPasaje toData(Pasaje pas) {
    Objects.requireNonNull(pas, "El pasaje no puede ser nulo");
    return new DataPasaje(pas.getNombre(), pas.getApellido());
  }

  // HELLPERS

  /**
   * Converts a list of DataPasaje to a list of Pasaje entities.
   */
  public static List<Pasaje> toEntities(List<? extends DataPasaje> dtos) {
    if (dtos == null) {
      return Collections.emptyList();      
    }
    return dtos.stream().filter(Objects::nonNull).map(ManejadorPasaje::toEntity)
        .collect(Collectors.toList());
  }
  
  /**
   * Converts a list of Pasaje entities to a list of DataPasaje.
   */
  public static List<DataPasaje> toDatas(List<? extends Pasaje> entities) {
    if (entities == null) {
      return Collections.emptyList();      
    }
    return entities.stream().filter(Objects::nonNull).map(ManejadorPasaje::toData)
        .collect(Collectors.toList());
  }

}
