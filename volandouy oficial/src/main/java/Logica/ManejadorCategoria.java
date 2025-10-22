package Logica;

import BD.CategoriaService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

/**
 * Clase encargada de manejar la conversión entre objetos DataCategoria y Categoria.
 */
public final class ManejadorCategoria {

  private ManejadorCategoria() {
  }

  private static CategoriaService service = new CategoriaService();

  //////// DTO -> ENTIDAD

  /**
   * Convierte un DataCategoria a una entidad Categoria.
   * 
   */
  public static Categoria toEntity(DataCategoria dto) {
    Objects.requireNonNull(dto, "DataCategoria no puede ser null"); // Valida que dto no sea null;
                                                                    // si lo es, lanza
                                                                    // NullPointerException con
                                                                    // mensaje.
    Categoria c = new Categoria(dto.getNombre());
    try {
      service.crearCategoria(c);
      JOptionPane.showMessageDialog(null, "Se insertó correctamente");
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
    }
    return c;
  }

  ///////////// Sobre carga con servicio inyectado (para tests)
  /**
   * Convierte un DataCategoria a una entidad Categoria usando el servicio proporcionado.
   * 
   */
  public static Categoria toEntity(DataCategoria dto, CategoriaService svc) {
    Objects.requireNonNull(dto, "DataCategoria no puede ser null");
    Categoria c = new Categoria(dto.getNombre());
    (svc == null ? service : svc).crearCategoria(c);
    return c;
  }
  ///// ENITIDAD -> DTO
  
  /**
   * Convierte una entidad Categoria a un DataCategoria.
   * 
   */
  public static DataCategoria toData(Categoria c) {
    Objects.requireNonNull(c, "Categoria no puede ser null"); // Valida argumento no nulo.
    return new DataCategoria(c.getNombre());
  }

  // HELPERS

  /**
   * Convierte una lista de DataCategoria a una lista de entidades Categoria.
   * 
   */
  public static List<Categoria> toEntities(List<? extends DataCategoria> dtos) { // Convierte una
                                                                                 // lista de DTOs a
                                                                                 // entidades.
    return dtos.stream().peek(Objects::requireNonNull).map(d -> new Categoria(d.getNombre()))
        .collect(Collectors.toList());
    // return dtos.stream() Convierte la lista a stream… //
    // .map(ManejadorUsuario::toEntity aplica toEntity a cada elemento (method
    // reference)… // .collect(Collectors.toList()); // y vuelve a materializar una
    // List.
  }

  /**
   * Convierte una lista de entidades Categoria a una lista de DataCategoria.
   * 
   */
  public static List<DataCategoria> toDTOs(List<? extends Categoria> cats) {
    return cats.stream().peek(Objects::requireNonNull).map(ManejadorCategoria::toData)
        .collect(Collectors.toList());
  }

  static void setServiceForTests(CategoriaService s) {
    service = (s == null) ? new CategoriaService() : s;
  }

}
