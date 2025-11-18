package Logica;

public class DataRutaMasVisitada {

    private int idRuta;
    private String nickAerolinea;
    private Ciudad ciudadOrigen;
    private Ciudad ciudadDestino;
    private int visitas;

    /**
     * Constructor for DataRutaMasVisitada.
     *
     */
    public DataRutaMasVisitada(int idRuta, String nickAerolinea, Ciudad ciudadOrigen, Ciudad ciudadDestino, int visitas) {
        this.idRuta = idRuta;
        this.nickAerolinea = nickAerolinea;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.visitas = visitas;
    }

    public int getIdRuta() {
        return idRuta;
    }
    public String getNickAerolinea() {
        return nickAerolinea;
    }
    public Ciudad getCiudadOrigen() {
        return ciudadOrigen;
    }
    public Ciudad getCiudadDestino() {
        return ciudadDestino;
    }

    public int getVisitas() {
        return visitas;
    }
}
