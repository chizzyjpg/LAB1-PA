package Logica;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

import java.util.List;

/**
 * Clase que representa los datos comunes de un usuario que se muestran en la web.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "dataUsuarioMuestraWeb",
        propOrder = { "nombre", "nickname", "email", "siguiendo", "tipoUsuario" }
)
@XmlSeeAlso({ DataCliente.class, DataAerolinea.class })
public class DataUsuarioMuestraWeb {

    private String nombre;
    private String nickname;
    private String email;
    private boolean siguiendo;
    private TipoUsuario tipoUsuario;

    public DataUsuarioMuestraWeb(){
        super();
    }
    /**
     * Constructor de la clase DataUsuarioMuestraWeb.
     *
     */
    public DataUsuarioMuestraWeb(String nombre, String nickname, String email, boolean siguiendo, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.nickname = nickname;
        this.email = email;
        this.siguiendo = siguiendo;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters

    public String getNombre() {

        return nombre;
    }

    public String getNickname() {

        return nickname;
    }

    public String getEmail() {

        return email;
    }

    public boolean isSiguiendo() {
        return siguiendo;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
}