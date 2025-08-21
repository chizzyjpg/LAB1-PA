
package Logica;


public interface ISistema {
	void registrarUsuario(DataUsuario data); // DataCliente o DataAerolinea
    boolean existeNickname(String nickname);
    boolean existeNombre(String nombre);

    DataCliente verInfoCliente(String nickname);      // null si no existe o no es cliente
    DataAerolinea verInfoAerolinea(String nickname); // null si no existe o no es aerolínea
}
