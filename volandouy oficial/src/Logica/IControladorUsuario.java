
package Logica;


public interface IControladorUsuario {
    public abstract void registrarUsuario(String n, String ap, String ci);
    public abstract DataUsuario verInfoUsuario(String ci);
}
