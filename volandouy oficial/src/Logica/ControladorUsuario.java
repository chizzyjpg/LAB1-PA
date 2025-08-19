
package Logica;



public class ControladorUsuario implements IControladorUsuario {
    
    public ControladorUsuario(){
        
    }

    /**
     *
     * @param n, nombre de...
     * @param ap
     * @param ci
     * 
     * @see HOLA ESTO ESTÁ EN UN SEE
     * BUENO ESTARÍAMOS FUERA DEL SEE PERO DENTRO DE LOS COMENTARIOS
     */
    @Override
    public void registrarUsuario(String n, String ap, String ci){
        //Instancio al manejador
        
        //Creo el usuario
        Usuario u = new Usuario(n,ap,ci);
        ManejadorUsuario.getinstance().addUsuario(u);
        //Lo agrego a la colección global
        
    }
    
    
    
    public DataUsuario verInfoUsuario(String ci){
        ManejadorUsuario mu = ManejadorUsuario.getinstance();
        Usuario u = mu.obtenerUsuario(ci);
        if (u!= null)
            return new DataUsuario(u.getNombre(),u.getApellido(),u.getCI());
        else
            return new DataUsuario();
        
    }
}
