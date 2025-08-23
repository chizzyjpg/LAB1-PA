package Logica;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ManejadorRuta {

    private static class Holder { private static final ManejadorRuta I = new ManejadorRuta(); }
    public static ManejadorRuta get() { return Holder.I; }

    private final Map<String, Ruta> porNombre = new ConcurrentHashMap<>();

    private ManejadorRuta() {}

    // ===== Altas =====
    public void altaRuta(Ruta r) {
        Objects.requireNonNull(r, "ruta");
        validarBasico(r);
        if (porNombre.putIfAbsent(r.getNombre(), r) != null)
            throw new IllegalArgumentException("Ya existe una ruta con nombre: " + r.getNombre());
    }

    // Alta desde DTO + entidades de ciudad ya resueltas
    public void altaRutaDesdeData(DataRuta d, Ciudad origen, Ciudad destino) {
        Objects.requireNonNull(d, "dataRuta");
        Ruta r = new Ruta(
            d.getNombre(), d.getDescripcion(),
            origen, destino,
            d.getHora(), d.getFechaAlta(),
            d.getCostoBase(), d.getCostoEquipajeExtra()
        );
        altaRuta(r);
    }

    // Alta “desde formulario” resolviendo ciudades por nombre+pais
    public void altaRutaDesdeFormulario(String nombre, String descripcion,
                                        String origenNombre, String origenPais,
                                        String destinoNombre, String destinoPais,
                                        int hora, Date fechaAlta,
                                        int costoBase, int costoEquipajeExtra,
                                        Aerolinea aerolinea) {
        Ciudad origen = ManejadorCiudad.get().obtener(origenNombre, origenPais);
        if (origen == null) throw new IllegalArgumentException("No existe la ciudad de origen: " + origenNombre + " (" + origenPais + ")");
        Ciudad destino = ManejadorCiudad.get().obtener(destinoNombre, destinoPais);
        if (destino == null) throw new IllegalArgumentException("No existe la ciudad de destino: " + destinoNombre + " (" + destinoPais + ")");

        Ruta r = new Ruta(nombre, descripcion, origen, destino, hora, fechaAlta, costoBase, costoEquipajeExtra);
        altaRuta(r);
    }

    // ===== Obtener / Listar =====
    public Ruta obtenerEntidad(String nombre) {
        Ruta r = porNombre.get(nombre);
        if (r == null) throw new NoSuchElementException("No existe la ruta: " + nombre);
        return r;
    }

    public DataRuta obtenerData(String nombre) { return toData(obtenerEntidad(nombre)); }

    public List<DataRuta> listarTodas() {
        return porNombre.values().stream().map(this::toData).collect(Collectors.toList());
    }

    // ===== Búsquedas =====
    public List<DataRuta> buscarPorNombreParcial(String patron) {
        if (patron == null || patron.isBlank()) return listarTodas();
        String p = patron.toLowerCase(Locale.ROOT);
        return porNombre.values().stream()
            .filter(r -> r.getNombre()!=null && r.getNombre().toLowerCase(Locale.ROOT).contains(p))
            .map(this::toData)
            .collect(Collectors.toList());
    }

    public List<DataRuta> buscarPorOrigenDestino(String origenNombre, String origenPais,
                                                 String destinoNombre, String destinoPais) {
        return porNombre.values().stream()
            .filter(r -> ciudadMatch(r.getOrigen(),  origenNombre,  origenPais))
            .filter(r -> ciudadMatch(r.getDestino(), destinoNombre, destinoPais))
            .map(this::toData)
            .collect(Collectors.toList());
    }

    // ===== Actualizar / Eliminar =====
    public void actualizar(String nombreViejo,
                           String nuevoNombre,
                           String nuevaDescripcion,
                           Ciudad nuevoOrigen,
                           Ciudad nuevoDestino,
                           Integer nuevaHora,
                           Date nuevaFechaAlta,
                           Integer nuevoCostoBase,
                           Integer nuevoCostoEquipajeExtra) {
        Ruta r = obtenerEntidad(nombreViejo);
        if (nuevaDescripcion != null)      r.setDescripcion(nuevaDescripcion);
        if (nuevoOrigen != null)           r.setOrigen(nuevoOrigen);
        if (nuevoDestino != null)          r.setDestino(nuevoDestino);
        if (nuevaHora != null)             r.setHora(nuevaHora);
        if (nuevaFechaAlta != null)        r.setFechaAlta(nuevaFechaAlta);
        if (nuevoCostoBase != null)        r.setCostoBase(nuevoCostoBase);
        if (nuevoCostoEquipajeExtra != null) r.setCostoEquipajeExtra(nuevoCostoEquipajeExtra);

        if (nuevoNombre != null && !nuevoNombre.equals(nombreViejo)) {
            if (porNombre.containsKey(nuevoNombre))
                throw new IllegalArgumentException("Ya existe una ruta con nombre: " + nuevoNombre);
            porNombre.remove(nombreViejo);
            r.setNombre(nuevoNombre);
            porNombre.put(nuevoNombre, r);
        }
    }

    public void eliminar(String nombre) {
        if (porNombre.remove(nombre) == null)
            throw new NoSuchElementException("No existe la ruta: " + nombre);
    }

    // ===== Helpers =====
    private boolean ciudadMatch(Ciudad c, String nombre, String pais) {
        if (nombre == null && pais == null) return true;
        if (c == null) return false;
        boolean okNombre = (nombre == null) || (c.getNombre()!=null && c.getNombre().equalsIgnoreCase(nombre));
        boolean okPais   = (pais   == null) || (c.getPais()!=null   && c.getPais().equalsIgnoreCase(pais));
        return okNombre && okPais;
    }

    private void validarBasico(Ruta r) {
        if (r.getNombre()==null || r.getNombre().isBlank()) throw new IllegalArgumentException("nombre obligatorio");
        if (r.getDescripcion()==null || r.getDescripcion().isBlank()) throw new IllegalArgumentException("descripcion obligatoria");
        if (r.getOrigen()==null)  throw new IllegalArgumentException("ciudad origen obligatoria");
        if (r.getDestino()==null) throw new IllegalArgumentException("ciudad destino obligatoria");
        if (r.getFechaAlta()==null) throw new IllegalArgumentException("fecha de alta obligatoria");
    }
    
    public List<DataRuta> listarPorAerolinea(String nicknameAerolinea) {
        if (nicknameAerolinea == null || nicknameAerolinea.isBlank()) {
            return Collections.emptyList();
        }

        return porNombre.values().stream()
                .filter(r -> r.getAerolinea() != null &&
                             nicknameAerolinea.equals(r.getAerolinea().getNickname()))
                .map(this::toData)
                .collect(Collectors.toList());
    }


    private DataRuta toData(Ruta r) {
        DataCiudad origen = (r.getOrigen()==null) ? null :
            new DataCiudad(r.getOrigen().getNombre(), r.getOrigen().getPais(),
                           r.getOrigen().getNombreAeropuerto(), r.getOrigen().getDescripcionAeropuerto(),
                           r.getOrigen().getFechaAlta(), r.getOrigen().getSitioWeb());
        DataCiudad destino = (r.getDestino()==null) ? null :
            new DataCiudad(r.getDestino().getNombre(), r.getDestino().getPais(),
                           r.getDestino().getNombreAeropuerto(), r.getDestino().getDescripcionAeropuerto(),
                           r.getDestino().getFechaAlta(), r.getDestino().getSitioWeb());
        return new DataRuta(
            r.getNombre(), r.getDescripcion(),
            origen, destino,
            r.getHora(), r.getFechaAlta(),
            r.getCostoBase(), r.getCostoEquipajeExtra(),
            r.getAerolinea()
        );
    }
}
