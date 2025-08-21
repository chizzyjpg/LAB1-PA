
package Logica;

import java.util.List;

public interface ISistema {
	void registrarUsuario(DataUsuario data); // DataCliente o DataAerolinea
    boolean existeNickname(String nickname);
    boolean existeEmail(String email);
    List<DataUsuario> listarUsuarios();

    DataCliente verInfoCliente(String nickname);      // null si no existe o no es cliente
    DataAerolinea verInfoAerolinea(String nickname); // null si no existe o no es aerolínea
}
