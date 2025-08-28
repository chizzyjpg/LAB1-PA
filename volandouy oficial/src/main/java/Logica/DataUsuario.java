
package Logica;


public abstract class DataUsuario {
    
    private String nombre;
    private String nickname;
    private String email;
    
    public DataUsuario(String nombre, String nickname, String email){
        this.nombre = nombre;
        this.nickname = nickname;
        this.email = email;
    }
    
    //Getters
    
    public String getNombre(){
        return nombre;
    }
    public String getNickname(){
        return nickname;
    }
    public String getEmail(){
        return email;
    }
    
}
