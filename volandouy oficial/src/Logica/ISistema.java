
package Logica;

import java.util.Date;
import java.util.List;

public interface ISistema {
	void registrarUsuario(DataUsuario data); // DataCliente o DataAerolinea
    boolean existeNickname(String nickname);
    boolean existeEmail(String email);
    
    List<DataUsuario> listarUsuarios();
    List<DataAerolinea> listarAerolineas();
    
    DataCliente verInfoCliente(String nickname);      // null si no existe o no es cliente
    DataAerolinea verInfoAerolinea(String nickname); // null si no existe o no es aerolínea
    
    void modificarCliente(String nickname, DataCliente nuevosDatos);
    void modificarAerolinea(String nickname, DataAerolinea nuevosDatos);
    
    /*List<DataRutaResumen> listarRutasDeAerolinea(String nickAerolinea);
    List<DataReservaResumen> listarReservasDeCliente(String nickCliente);
    List<DataPaqueteResumen> listarPaquetesDeCliente(String nickCliente);
    
    DataRuta verRuta(long idRuta);
    DataVuelo verVuelo(long idVuelo);
    DataPaquete verPaquete(long idPaquete);*/
    

    void RegistrarRuta(String nickAerolinea, DataRuta datos);
    List<DataRuta> listarPorAerolinea(String nicknameAerolinea);
    
	void registrarCiudad(DataCiudad data);
	List<DataCiudad> listarCiudades();
	Ciudad buscarCiudad(String nombre, String pais);
}
