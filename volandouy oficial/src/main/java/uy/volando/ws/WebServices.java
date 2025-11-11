package uy.volando.ws;

import Logica.*;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;
import java.util.Date;

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
}
