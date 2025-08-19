
package Logica;


public class Usuario {
    
    private String nombre;
    private String apellido;
    private String cedulaIdentidad;
    
    public Usuario(String n, String ap, String ci){
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
    
    // Setters
    
    public void setNombre(String n){
        nombre = n;
    }
    public void setApellido(String ap){
        apellido = ap;
    }
    public void setCI(String ci){
        cedulaIdentidad = ci;
    }
   
    
}
