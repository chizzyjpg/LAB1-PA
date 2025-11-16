package uy.volando.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import uy.volando.publicar.VolandoWS;
import uy.volando.publicar.WebServices;

///**
/// SOLUCIONA PROBLEMA DE INICALIZACION DEL CLIENTE SOAP EN EL CONTEXTO DE LA APLICACION CON FILTROS
/// **//

@WebListener
public class InicioListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        try {
            VolandoWS service = new VolandoWS();
            WebServices port = service.getWebServicesPort();

            ctx.setAttribute("volandoPort", port);

            System.out.println("[InicioListener] Cliente SOAP (WebServices) cargado en el contexto.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo inicializar el cliente SOAP VolandoWS", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // si querés limpiar algo al apagar la app
    }
}
