
package Logica;

import java.util.List;

public interface ISistema {
	void registrarUsuario(DataUsuario data); // DataCliente o DataAerolinea
    boolean existeNickname(String nickname);
    boolean existeEmail(String email);
    boolean existeCategoria(String nombre);
    
    List<DataUsuario> listarUsuarios();

    DataCliente verInfoCliente(String nickname);      // null si no existe o no es cliente
    DataAerolinea verInfoAerolinea(String nickname); // null si no existe o no es aerolínea
    List<DataCategoria> listarCategorias();
    void modificarCliente(String nickname, DataCliente nuevosDatos);
    void modificarAerolinea(String nickname, DataAerolinea nuevosDatos);
    void registrarCategoria(DataCategoria Data);
    
    /*List<DataRutaResumen> listarRutasDeAerolinea(String nickAerolinea);
    List<DataReservaResumen> listarReservasDeCliente(String nickCliente);
    List<DataPaqueteResumen> listarPaquetesDeCliente(String nickCliente);
    
    DataRuta verRuta(long idRuta);
    DataVuelo verVuelo(long idVuelo);
    DataPaquete verPaquete(long idPaquete);*/
}
