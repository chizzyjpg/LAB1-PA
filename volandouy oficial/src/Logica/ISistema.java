package Logica;

import java.time.LocalDate;
import java.util.List;

public interface ISistema {
    // Lo que ya tenías
    public abstract void registrarUsuario(String n, String ap, String ci);
    public abstract DataUsuario verInfoUsuario(String ci);

       
    
}
