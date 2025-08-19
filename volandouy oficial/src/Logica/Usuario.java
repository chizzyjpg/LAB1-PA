
package Logica;


public abstract class Usuario {
    
    private String nombre;
    private String nickname;
    private String email;
    
    public Usuario(String n, String nick, String email){
        this.nombre = n;
        this.nickname = nick;
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
    
    // Setters
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public void setEmail(String email){
        this.email = email;
    }
   
    
}
