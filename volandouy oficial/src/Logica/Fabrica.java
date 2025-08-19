
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
    
public ISistema getISistema() {
    ISistema sis = new Sistema();
    return sis;
}
    
}
