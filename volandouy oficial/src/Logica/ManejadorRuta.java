package Logica;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ManejadorRuta {

    // ===== Singleton =====
    private static class Holder { private static final ManejadorRuta I = new ManejadorRuta(); }
    public static ManejadorRuta get() { return Holder.I; }

    // Índice por nombre (asumo nombre único)
    private final Map<String, Ruta> porNombre = new ConcurrentHashMap<>();

    private ManejadorRuta() {}

    // ===== Altas =====
    public void altaRuta(Ruta r) {
        Objects.requireNonNull(r, "ruta");
        String nombre = Objects.requireNonNull(r.getNombre(), "nombre");
        if (porNombre.putIfAbsent(nombre, r) != null) {
            throw new IllegalArgumentException("Ya existe ruta con nombre: " + nombre);
        }
    }

    public void altaRutaDesdeData(DataRuta d) {
        Objects.requireNonNull(d, "data ruta");
        Ruta r = new Ruta();
        r.setNombre(d.getNombre());
        r.setDescripcion(d.getDescripcion());
        r.setCiudadOrigen(d.getCiudadOrigen());
        r.setCiudadDestino(d.getCiudadDestino());
        r.setHora(d.getHora());
        r.setFechaAlta(d.getFechaAlta());
        r.setCostoBase(d.getCostoBase());
        r.setCostoEquipajeExtra(d.getCostoEquipajeExtra());
        altaRuta(r);
    }

    // ===== Obtener =====
    public Ruta obtenerPorNombre(String nombre) {
        Ruta r = porNombre.get(nombre);
        if (r == null) throw new NoSuchElementException("No existe ruta: " + nombre);
        return r;
    }

    public DataRuta obtenerDataPorNombre(String nombre) {
        return toData(obtenerPorNombre(nombre));
    }

    // ===== Listar / Buscar =====
    public List<DataRuta> listarTodas() {
        return porNombre.values().stream()
                .map(this::toData)
                .collect(Collectors.toList());
    }

    public List<DataRuta> buscarPorOrigenDestino(String origen, String destino) {
        return porNombre.values().stream()
                .filter(r -> origen  == null || origen.equalsIgnoreCase(r.getCiudadOrigen()))
                .filter(r -> destino == null || destino.equalsIgnoreCase(r.getCiudadDestino()))
                .map(this::toData)
                .collect(Collectors.toList());
    }

    public List<DataRuta> buscarPorNombreParcial(String patron) {
        if (patron == null || patron.isBlank()) return listarTodas();
        String p = patron.toLowerCase(Locale.ROOT);
        return porNombre.values().stream()
                .filter(r -> r.getNombre() != null && r.getNombre().toLowerCase(Locale.ROOT).contains(p))
                .map(this::toData)
                .collect(Collectors.toList());
    }

    // ===== Actualizar =====
    public void actualizar(String nombreViejo, DataRuta cambios) {
        Ruta r = obtenerPorNombre(nombreViejo);

        if (cambios.getDescripcion() != null)      r.setDescripcion(cambios.getDescripcion());
        if (cambios.getCiudadOrigen() != null)     r.setCiudadOrigen(cambios.getCiudadOrigen());
        if (cambios.getCiudadDestino() != null)    r.setCiudadDestino(cambios.getCiudadDestino());
        if (cambios.getHora() != 0)                r.setHora(cambios.getHora());
        if (cambios.getFechaAlta() != null)        r.setFechaAlta(cambios.getFechaAlta());
        if (cambios.getCostoBase() != 0)           r.setCostoBase(cambios.getCostoBase());
        if (cambios.getCostoEquipajeExtra() != 0)  r.setCostoEquipajeExtra(cambios.getCostoEquipajeExtra());

        if (cambios.getNombre() != null && !cambios.getNombre().equals(nombreViejo)) {
            String nombreNuevo = cambios.getNombre();
            if (porNombre.containsKey(nombreNuevo)) {
                throw new IllegalArgumentException("Ya existe ruta: " + nombreNuevo);
            }
            porNombre.remove(nombreViejo);
            r.setNombre(nombreNuevo);
            porNombre.put(nombreNuevo, r);
        }
    }

    // ===== Eliminar =====
    public void eliminarPorNombre(String nombre) {
        Ruta r = porNombre.remove(nombre);
        if (r == null) throw new NoSuchElementException("No existe ruta: " + nombre);
    }

    // ===== Conversión =====
    private DataRuta toData(Ruta r) {
        return new DataRuta(
            r.getNombre(),
            r.getDescripcion(),
            r.getCiudadOrigen(),
            r.getCiudadDestino(),
            r.getHora(),
            r.getFechaAlta(),
            r.getCostoBase(),
            r.getCostoEquipajeExtra()
        );
    }
}
