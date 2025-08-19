
package Logica;


import java.util.HashMap;
import java.util.Map;



/*
public class ManejadorUsuario {
    //Clase que conserva la colección global de los Usuarios del Sistema
	private Map<String, Usuario> usuariosCI;
    public static ManejadorUsuario instancia=null;
    
    
    private ManejadorUsuario(){
        usuariosCI= new HashMap<String, Usuario>();
    }
    
    public static ManejadorUsuario getinstance(){
        if (instancia==null)
            instancia = new ManejadorUsuario();
        return instancia;
    }
    
    public void addUsuario(Usuario usu){
        String ci = usu.getCI();
        if (this.obtenerUsuario(ci) == null) {
            usuariosCI.put(ci,usu);
        }
    }
    
    public Usuario obtenerUsuario(String ci){
        return ((Usuario) usuariosCI.get(ci));
    }
    
}
*/