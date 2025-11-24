package uy.volando.main;

import uy.volando.ws.WebServices;
import Presentacion.Menu;

public class MainServidor {
    public static void main(String[] args) {

        // 1) Publicar WebServices
        WebServices ws = new WebServices();
        ws.publicar();

        // Levantar Swing
        Menu.main(args); // o new Menu().setVisible(true); según tu diseño
    }
}
