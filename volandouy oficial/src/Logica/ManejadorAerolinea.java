package Logica;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ManejadorAerolinea {

    private static class Holder { private static final ManejadorAerolinea I = new ManejadorAerolinea(); }
    public static ManejadorAerolinea get() { return Holder.I; }

    private final Map<String, Aerolinea> porNick = new ConcurrentHashMap<>();

    private ManejadorAerolinea() {}

    // Alta
    public void altaAerolinea(Aerolinea a) {
        Objects.requireNonNull(a, "aerolinea");
        if (porNick.containsKey(a.getNickname()))
            throw new IllegalArgumentException("Ya existe aerolínea con nickname: " + a.getNickname());
        porNick.put(a.getNickname(), a);
    }

    public void altaAerolinea(DataAerolinea d) {
        Aerolinea a = new Aerolinea(d.getNickname(), d.getNombre(), d.getEmail(),
                                    d.getDescripcion(), d.getSitioWeb());
        altaAerolinea(a);
    }

    // Obtener
    public Aerolinea obtenerEntidad(String nickname) {
        Aerolinea a = porNick.get(nickname);
        if (a == null) throw new NoSuchElementException("No existe la aerolínea: " + nickname);
        return a;
    }

    public DataAerolinea obtenerData(String nickname) {
        return toData(obtenerEntidad(nickname));
    }

    // Listar
    public List<DataAerolinea> listarData() {
        return porNick.values().stream().map(this::toData).collect(Collectors.toList());
    }

    // Eliminar
    public void eliminar(String nickname) {
        if (porNick.remove(nickname) == null)
            throw new NoSuchElementException("No existe la aerolínea: " + nickname);
    }

    private DataAerolinea toData(Aerolinea a) {
        return new DataAerolinea(a.getNickname(), a.getNombre(), a.getEmail(),
                                 a.getDescGeneral(), a.getLinkWeb());
    }

}
