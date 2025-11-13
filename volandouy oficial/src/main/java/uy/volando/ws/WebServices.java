package uy.volando.ws;

import Logica.*;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;
import java.util.Date;
import java.util.List;

@WebService(serviceName = "VolandoWS")
@SOAPBinding(style = Style.RPC)
public class WebServices {

    // --- Campos internos (no se exponen como operaciones) ---
    private Endpoint endpoint;
    private ISistema sistema;

    // Constructor: inicializa el sistema
    public WebServices() {
        this.sistema = obtenerSistema();
    }

    // ========= Métodos auxiliares, NO publicados =========

    @WebMethod(exclude = true)
    private ISistema obtenerSistema() {
        return Fabrica.getInstance().getSistema();
    }

    @WebMethod(exclude = true)
    public void publicar() {
        String url = "http://localhost:9128/volando";
        endpoint = Endpoint.publish(url, this);

        System.out.println("VolandoWS publicado en: " + url);
        System.out.println("WSDL disponible en: " + url + "?wsdl");
    }

    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
        return endpoint;
    }

    // ========= OPERACIONES =========
    //
    // Ejemplos sumando métodos:
    //
    // @WebMethod
    // public String ping() {
    //     return "VolandoWS activo";
    // }
    //
    //
    //
    // (Todo lo que NO tenga @WebMethod(exclude = true) se publica en el WSDL)

    @WebMethod
    public DataUsuario loguearUsuario(String username, String password) {
        return sistema.loguearUsuario(username, password);
    }


    @WebMethod
    public String ping(@WebParam(name = "nombre") String nombre) {
        return "Hola " + nombre + ", VolandoWS está activo";
    }

    @WebMethod
    public DataCliente verInfoCliente(String nickname){
        return sistema.verInfoCliente(nickname);
    }

    @WebMethod
    public DataAerolinea verInfoAerolinea(String nickname){
        return sistema.verInfoAerolinea(nickname);
    }

    @WebMethod
    public DataCliente actualizarPerfilCliente(
            @WebParam(name = "nickSesion") String nickSesion,
            @WebParam(name = "emailSesion") String emailSesion,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "apellido") String apellido,
            @WebParam(name = "nacionalidad") String nacionalidad,
            @WebParam(name = "tipoDocumento") TipoDocumento tipoDocumento,
            @WebParam(name = "numDocumento") String numDocumento,
            @WebParam(name = "fechaNac") Date fechaNac,
            @WebParam(name = "avatarBytes") byte[] avatarBytes,
            @WebParam(name = "clearPhoto") boolean clearPhoto
    ) {
        PerfilClienteUpdate upd = new PerfilClienteUpdate(
                nickSesion, emailSesion, nombre, apellido,
                nacionalidad, tipoDocumento, numDocumento,
                fechaNac, avatarBytes, clearPhoto
        );
        return sistema.actualizarPerfilCliente(upd);
    }

    @WebMethod
    public DataAerolinea actualizarPerfilAerolinea(
            @WebParam(name = "nickSesion") String nickSesion,
            @WebParam(name = "emailSesion") String emailSesion,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "descripcion") String descripcion,
            @WebParam(name = "sitioWeb") String sitioWeb,
            @WebParam(name = "avatarBytes") byte[] avatarBytes,
            @WebParam(name = "clearPhoto") boolean clearPhoto
    ) {
        PerfilAerolineaUpdate upd = new PerfilAerolineaUpdate(
                nickSesion, emailSesion, nombre,
                descripcion, sitioWeb, avatarBytes, clearPhoto
        );
        return sistema.actualizarPerfilAerolinea(upd);
    }

    @WebMethod
    public DataPaquete[] listarPaquetes() {
        List<DataPaquete> lista = sistema.listarPaquetes();
        return lista.toArray(new DataPaquete[0]);
    }

    @WebMethod
    public DataPaquete verPaquete(@WebParam(name = "nombrePaquete") String nombrePaquete) {
        return sistema.verPaquete(nombrePaquete);
    }

    @WebMethod
    public DataAerolinea[] listarAerolineas() {
        List<DataAerolinea> lista = sistema.listarAerolineas();
        return lista.toArray(new DataAerolinea[0]);
    }

    @WebMethod
    public DataCategoria[] listarCategorias() {
        List<DataCategoria> lista = sistema.listarCategorias();
        return lista.toArray(new DataCategoria[0]);
    }

    @WebMethod
    public DataRuta[] listarPorAerolinea(@WebParam(name = "nicknameAerolinea") String nicknameAerolinea) {
        List<DataRuta> lista = sistema.listarPorAerolinea(nicknameAerolinea);
        return lista.toArray(new DataRuta[0]);
    }

    @WebMethod
    public DataVueloEspecifico[] listarVuelos(@WebParam(name = "nickname") String nickname, @WebParam(name ="nombre") String nombre) {
        List<DataVueloEspecifico> lista = sistema.listarVuelos(nickname, nombre);
        return lista.toArray(new DataVueloEspecifico[0]);
    }

    @WebMethod
    public DataReserva buscarReserva(
            @WebParam(name = "nicknameAerolinea") String nicknameAerolinea,
            @WebParam(name = "rutaSel") String rutaSel,
            @WebParam(name = "vueloSel") String vueloSel,
            @WebParam(name = "idReserva") int idReserva
    ) {
        return sistema.buscarReserva(nicknameAerolinea, rutaSel, vueloSel, idReserva);
    }

    @WebMethod
    public DataReserva[] listarReservas(
            @WebParam(name = "nicknameAerolinea") String nicknameAerolinea,
            @WebParam(name = "rutaSel") String rutaSel,
            @WebParam(name = "vueloSel") String vueloSel
    ) {
        List<DataReserva> lista = sistema.listarReservas(nicknameAerolinea, rutaSel, vueloSel);
        return lista.toArray(new DataReserva[0]);
    }

    @WebMethod
    public void registrarVuelo(
            @WebParam(name = "nicknameAerolinea") String nicknameAerolinea,
            @WebParam(name = "nombreRuta") String nombreRuta,
            DataVueloEspecifico datosVuelo
    ) {
        sistema.registrarVuelo(nicknameAerolinea, nombreRuta, datosVuelo);
    }

    @WebMethod
    public byte[] obtenerAvatar(@WebParam(name = "nickname") String nickname) {
        return sistema.obtenerAvatar(nickname);
    }

   @WebMethod
    public void cambiarPassword(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "pwdCurrent") String pwdCurrent,
            @WebParam(name = "pwdNew") String  pwdNew
    ) {
        sistema.cambiarPassword(nickname,  pwdCurrent,  pwdNew);
    }

    @WebMethod
    public DataCiudad[] listarCiudades() {
        List<DataCiudad> lista = sistema.listarCiudades();
        return lista.toArray(new DataCiudad[0]);
    }

    @WebMethod
    public void registrarRuta(DataRuta ruta) {
        sistema.registrarRuta(ruta);
    }

    @WebMethod
    public boolean existeNickname(@WebParam(name = "nickname") String nickname) {
        return sistema.existeNickname(nickname);
    }

    @WebMethod
    public boolean existeEmail(@WebParam(name = "email") String email) {
        return sistema.existeEmail(email);
    }

    @WebMethod
    public void altaAerolinea(DataAerolinea aerolinea) {
        sistema.altaAerolinea(aerolinea);
    }

    @WebMethod
    public void altaCliente(DataCliente cliente, byte[] avatarBytes) {
        sistema.altaCliente(cliente, avatarBytes);
    }
}
