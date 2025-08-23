package Logica;

import java.util.*;

public class ManejadorCiudad {
    private static final ManejadorCiudad I = new ManejadorCiudad();
    public static ManejadorCiudad get() { return I; }

    private final Map<String, Ciudad> ciudades = new HashMap<>();
    private ManejadorCiudad() {}

    public void agregarCiudad(Ciudad c) {
        Objects.requireNonNull(c, "ciudad");
        validar(c.getNombre(), c.getPais(), c.getNombreAeropuerto(), c.getFechaAlta(), c.getSitioWeb());
        String key = key(c.getNombre(), c.getPais());
        if (ciudades.containsKey(key))
            throw new IllegalArgumentException("Ya existe la ciudad " + c.getNombre() + " (" + c.getPais() + ")");
        ciudades.put(key, c);
    }

    public void agregarCiudad(DataCiudad d) {
        Objects.requireNonNull(d, "dataCiudad");
        validar(d.getNombre(), d.getPais(), d.getNombreAeropuerto(), d.getFechaAlta(), d.getSitioWeb());
        Ciudad c = new Ciudad(
                d.getNombre(),
                d.getPais(),
                d.getNombreAeropuerto(),
                d.getDescripcionAeropuerto(),
                d.getFechaAlta(),
                d.getSitioWeb()
        );
        agregarCiudad(c);
    }

    public Ciudad obtener(String nombre, String pais) {
        return ciudades.get(key(nombre, pais));
    }

    public DataCiudad obtenerData(String nombre, String pais) {
        Ciudad c = obtener(nombre, pais);
        return (c == null) ? null : toData(c);
    }

    public List<Ciudad> listar() {
        return new ArrayList<>(ciudades.values());
    }

    public List<DataCiudad> listarData() {
        List<DataCiudad> out = new ArrayList<>();
        for (Ciudad c : ciudades.values()) out.add(toData(c));
        return out;
    }

    public void eliminar(String nombre, String pais) {
        ciudades.remove(key(nombre, pais));
    }

    private String key(String nombre, String pais) {
        return (nombre == null ? "" : nombre.trim().toLowerCase()) + "|" +
               (pais   == null ? "" : pais.trim().toLowerCase());
    }

    // si el sitioWeb es obligatorio por letra, exigilo no-vacío aquí
    private void validar(String nombre, String pais, String nombreAeropuerto, Date fechaAlta, String sitioWeb) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre obligatorio");
        if (pais == null || pais.isBlank()) throw new IllegalArgumentException("País obligatorio");
        if (nombreAeropuerto == null || nombreAeropuerto.isBlank()) throw new IllegalArgumentException("Nombre de aeropuerto obligatorio");
        if (fechaAlta == null) throw new IllegalArgumentException("Fecha de alta obligatoria");
        // Si tu letra exige web obligatoria:
        // if (sitioWeb == null || sitioWeb.isBlank()) throw new IllegalArgumentException("Sitio web obligatorio");
    }

    private DataCiudad toData(Ciudad c) {
        return new DataCiudad(
                c.getNombre(),
                c.getPais(),
                c.getNombreAeropuerto(),
                c.getDescripcionAeropuerto(),
                c.getFechaAlta(),
                c.getSitioWeb()
        );
    }
}
