package Logica;

import BD.CategoriaService;
import BD.CiudadService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ManejadorRuta es una clase que proporciona métodos estáticos para convertir entre
 * entidades Ruta y sus correspondientes objetos DataRuta (DTOs).
 */
public class ManejadorRuta {

  private ManejadorRuta() {
  }

  private static CiudadService ciudadService = new CiudadService();
  private static CategoriaService categoriaService = new CategoriaService();

  // ===== Obtener / Listar =====
  /**
   * Convierte un objeto DataRuta en una entidad Ruta.
   * 
   */
  public static Ruta toEntity(DataRuta data) {
    Objects.requireNonNull(data, "Los datos no pueden ser nulos");
    // Buscar Ciudad y Categoria reales a partir de los DTOs
    Ciudad origen = ciudadService.buscarPorNombre(data.getCiudadOrigen().getNombre());
    Ciudad destino = ciudadService.buscarPorNombre(data.getCiudadDestino().getNombre());
    Categoria categoria = categoriaService.buscarPorNombre(data.getCategoria().getNombre());
    Ruta r = new Ruta(data.getNombre(), data.getDescripcion(), origen, destino, data.getHora(),
        data.getFechaAlta(), data.getCostoTurista(), data.getCostoEquipajeExtra(),
        data.getCostoEjecutivo(), categoria, data.getDescripcionCorta());
    // Propagar también la URL de video (opcional) desde el DTO a la entidad
    if (data.getVideoUrl() != null && !data.getVideoUrl().isBlank()) {
      r.setVideoUrl(data.getVideoUrl());
    }
    // Ya no se persiste aquí, solo se crea la entidad
    return r;
  }

  /**
   * Convierte una entidad Ruta en un objeto DataRuta.
   * 
   */
  public static DataRuta toData(Ruta r) {
    Objects.requireNonNull(r, "La ruta no puede ser nula");
    DataCiudad origen = new DataCiudad(r.getOrigen().getNombre(), r.getOrigen().getPais(),
        r.getOrigen().getNombreAeropuerto(), r.getOrigen().getDescripcionAeropuerto(),
        r.getOrigen().getFechaAlta(), r.getOrigen().getSitioWeb());
    DataCiudad destino = new DataCiudad(r.getDestino().getNombre(), r.getDestino().getPais(),
        r.getDestino().getNombreAeropuerto(), r.getDestino().getDescripcionAeropuerto(),
        r.getDestino().getFechaAlta(), r.getDestino().getSitioWeb());
    DataCategoria categoria = new DataCategoria(r.getCategoriaR().getNombre());
    // Extraer el nickname de la aerolínea desde la relación (Set<Aerolinea>)
    String nicknameAerolinea = "";
    if (r.getAerolineas() != null && !r.getAerolineas().isEmpty()) {
      Aerolinea aerolinea = r.getAerolineas().iterator().next();
      nicknameAerolinea = aerolinea.getNickname();
    }
    DataRuta dto = new DataRuta(
        r.getNombre(),
        r.getDescripcion(),
        origen,
        destino,
        r.getHora(),
        r.getFechaAlta(),
        r.getCostoTurista(),
        r.getCostoEquipajeExtra(),
        r.getCostoEjecutivo(),
        categoria,
        nicknameAerolinea,
        r.getEstado(),
        r.getDescripcionCorta(),
        r.getVideoUrl()    // <-- nuevo campo propagado
    );
    dto.setIdRuta(r.getIdRuta());
    return dto;
  }

  // HELLPERS
  /**
   * Convierte una lista de objetos DataRuta en una lista de entidades Ruta.
   * 
   */
  public static List<Ruta> toEntities(List<? extends DataRuta> dtos) {
    if (dtos == null) {
      return Collections.emptyList();      
    }
    return dtos.stream().filter(Objects::nonNull).map(ManejadorRuta::toEntity)
        .collect(Collectors.toList());
  }

  /**
   * Convierte una lista de entidades Ruta en una lista de objetos DataRuta.
   * 
   */
  public static List<DataRuta> toDatas(List<? extends Ruta> entities) {
    if (entities == null) {
      return Collections.emptyList();      
    }
    return entities.stream().filter(Objects::nonNull).map(ManejadorRuta::toData)
        .collect(Collectors.toList());
  }

  // SOLO PARA TESTS
  static void setCiudadServiceForTests(BD.CiudadService cs) {
    ciudadService = cs;
  }

  static void setCategoriaServiceForTests(BD.CategoriaService cs) {
    categoriaService = cs;
  }

/**
 * Convierte una entidad Ruta en un objeto DataBusquedaItem.
 *
 */

    public static DataBusquedaItem toDataBusquedaItem(Ruta r) {
        Objects.requireNonNull(r, "La ruta no puede ser nula");

        if(r.getFechaAlta() == null) {
            r.setFechaAlta(new java.util.Date(0));
        }

        DataBusquedaItem dto = new DataBusquedaItem(String.valueOf(r.getIdRuta()),r.getNombre(), r.getDescripcion(), r.getFechaAlta(), TipoResultado.RUTA);
        return dto;
    }
/**
 * Convierte una lista de Rutas una lista de DataBusquedaItem.
 *
 *
 */
    public static List<DataBusquedaItem> toDataBusquedaItem(List<Ruta> rutas) {
        return rutas.stream().map(ManejadorRuta::toDataBusquedaItem).collect(Collectors.toList());
    }
}