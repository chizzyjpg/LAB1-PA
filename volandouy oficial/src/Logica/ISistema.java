
package Logica;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ISistema {
	
    boolean existeNickname(String nickname);
    boolean existeEmail(String email);
    boolean existeCategoria(String nombre);
    boolean clienteYaComproPaquete(String nicknameCliente, String nombrePaquete);
      
    DataCliente verInfoCliente(String nickname);      // null si no existe o no es cliente
    DataAerolinea verInfoAerolinea(String nickname); // null si no existe o no es aerolínea
    
    List<DataUsuario> listarUsuarios();
    List<DataAerolinea> listarAerolineas();
    List<DataCategoria> listarCategorias();
    List<DataRuta> listarPorAerolinea(String nicknameAerolinea);
    List<DataCiudad> listarCiudades();
    List<DataVueloEspecifico> listarVuelos(String nickname, String nombre);
    List<DataPaquete> listarPaquetesDisponiblesParaCompra();
    List<DataCliente> listarClientesParaCompra();
    
    void registrarUsuario(DataUsuario data); // DataCliente o DataAerolinea
    void modificarCliente(String nickname, DataCliente nuevosDatos);
    void modificarAerolinea(String nickname, DataAerolinea nuevosDatos);
    void registrarCategoria(DataCategoria Data);
    void registrarRuta(String nickAerolinea, DataRuta datos);
    void registrarVuelo(String nickname, String nombre, DataVueloEspecifico datos);
	void registrarCiudad(DataCiudad data);
	void comprarPaquete(DataCompraPaquete compra);
	
	Ciudad buscarCiudad(String nombre, String pais);	
	
}
