
package Logica;


public interface ISistema {
    public abstract void registrarUsuario(String n, String ap, String ci);
    public abstract DataUsuario verInfoUsuario(String ci);
}
