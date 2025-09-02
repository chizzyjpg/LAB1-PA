
package Logica;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ISistema {
	
    boolean existeNickname(String nickname);
    boolean existeEmail(String email);
    boolean existeCategoria(String nombre);
    boolean clienteYaComproPaquete(String nicknameCliente, String nombrePaquete);
    boolean existePaquete(String nombre); 
      
    DataCliente verInfoCliente(String nickname);      // null si no existe o no es cliente
    DataAerolinea verInfoAerolinea(String nickname); // null si no existe o no es aerolínea
    DataPaquete verPaquete(String nombre);  
    DataVueloEspecifico buscarVuelo(String nickname, String nombre, String codigoVuelo);
    DataReserva buscarReserva(String nickname, String nombre, String codigoVuelo, int idReserva);

    List<DataUsuario> listarUsuarios();
    List<DataAerolinea> listarAerolineas();
    List<DataCliente> listarClientes();
    List<DataCategoria> listarCategorias();
    List<DataRuta> listarPorAerolinea(String nicknameAerolinea);
    List<DataCiudad> listarCiudades();
    List<DataPaquete> listarPaquetesDisponiblesParaCompra();
    List<DataCliente> listarClientesParaCompra();
    List<DataPaquete> listarPaquetes();  
    List<DataPaquete> listarPaquetesSinCompras();
    List<DataVueloEspecifico> listarVuelos(String nickname, String nombre);
    List<DataReserva> listarReservas(String nickname, String nombre, String codigoVuelo);
   
    void registrarVuelo(String nickname, String nombre, DataVueloEspecifico datos); 
    void registrarUsuario(DataUsuario data); // DataCliente o DataAerolinea
    void modificarCliente(String nickname, DataCliente nuevosDatos);
    void modificarAerolinea(String nickname, DataAerolinea nuevosDatos);
    void registrarCategoria(DataCategoria Data);
    void registrarRuta(DataRuta datos);
	void registrarCiudad(DataCiudad data);
	void comprarPaquete(DataCompraPaquete compra);
	void registrarPaquete(DataPaqueteAlta data);  
	void agregarRutaAPaquete(String nombrePaquete,String nicknameAerolinea,String nombreRuta, TipoAsiento tipo, int cantidad);
	void registrarReserva(String nickname, String nombre, String codigoVuelo, DataReserva datos);

	void precargaDemo();	
	
	
	Ciudad buscarCiudad(String nombre, String pais);	
}
