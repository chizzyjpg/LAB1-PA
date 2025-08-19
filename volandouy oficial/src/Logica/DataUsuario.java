
package Logica;


public class DataUsuario {
    
    private String nombre;
    private String apellido;
    private String cedulaIdentidad;
    
    public DataUsuario(){
        this.nombre = "Fulano";
        this.apellido = "Detal";
        this.cedulaIdentidad = "1.111.111-1";
    }
    
    public DataUsuario(String n, String ap, String ci){
        this.nombre = n;
        this.apellido = ap;
        this.cedulaIdentidad = ci;
    }
    
    //Getters
    
    public String getNombre(){
        return nombre;
    }
    public String getApellido(){
        return apellido;
    }
    public String getCI(){
        return cedulaIdentidad;
    }
    
}
