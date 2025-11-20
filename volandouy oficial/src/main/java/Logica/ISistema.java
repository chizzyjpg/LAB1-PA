package Logica;

//import java.math.BigDecimal;
//import java.util.Date;
import java.util.List;

public interface ISistema {
	
    boolean existeCategoria(String nombre);
    boolean clienteYaComproPaquete(String nicknameCliente, String nombrePaquete);
    boolean existePaquete(String nombre); 
    boolean existeNickname(String nickname);
    boolean existeEmail(String email);
      
    DataCliente verInfoCliente(String nickname);      // null si no existe o no es cliente
    DataAerolinea verInfoAerolinea(String nickname); // null si no existe o no es aerolínea
    DataPaquete verPaquete(String nombre);  
    DataVueloEspecifico buscarVuelo(String nickname, String nombre, String codigoVuelo);
    DataReserva buscarReserva(String nickname, String nombre, String codigoVuelo, int idReserva);
    DataUsuario loguearUsuario(String nickname, String password); // null si no existe o password incorrecta
	DataCliente actualizarPerfilCliente(PerfilClienteUpdate datos);
    DataAerolinea actualizarPerfilAerolinea(PerfilAerolineaUpdate datos);

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
    List<DataRutaMasVisitada> obtener5RutasMasVisitadas();
    List<DataBusquedaItem> buscarRutasYPaquetes(String texto);
   
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
	void cambiarEstadoRuta(int idRuta, EstadoRuta nuevoEstado);
	void cambiarPassword(String nickname, String pwdCurrent, String pwdNew);
	void altaCliente(DataCliente cliente, byte[] avatar);
    void altaAerolinea(DataAerolinea aerolinea);
    void  registrarVisitaRuta(int codigoRuta);
    void finalizarRutaDeVuelo(String nicknameAerolinea, String nomRuta);

    byte[] obtenerAvatar(String nickname);
	Ciudad buscarCiudad(String nombre, String pais);    

}
