
package Logica;

public class Fabrica {

private static Fabrica instancia = null;
private Fabrica(){};

/*
    @doc La documentacion en general...
*/  
public static Fabrica getInstance(){
    if (instancia == null){
        instancia = new Fabrica();
    }
    return instancia;
}
    
public IControladorUsuario getIControladorUsuario() {
    IControladorUsuario IG = new ControladorUsuario();
    return IG;
}
    
}
