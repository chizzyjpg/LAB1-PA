package uy.volando.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.xml.ws.BindingProvider;

import uy.volando.publicar.VolandoWS;
import uy.volando.publicar.WebServices;
import uy.volando.web.config.WebConfig;

import java.net.URL;

@WebListener
public class InicioListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        try {
            // Leer endpoint base desde HOME/volandouy/web.properties
            String endpointBase = WebConfig.get("soap.endpoint");
            if (endpointBase == null || endpointBase.isBlank()) {
                throw new RuntimeException("soap.endpoint no definido en web.properties");
            }

            // Construir URL del WSDL (base + ?wsdl)
            URL wsdlURL = new URL(endpointBase + "?wsdl");

            // Crear Service usando ese WSDL configurable (NO localhost)
            VolandoWS service = new VolandoWS(wsdlURL);

            // Obtener port
            WebServices port = service.getWebServicesPort();

            // Asegurar que el endpoint real del port también quede configurable
            BindingProvider bp = (BindingProvider) port;
            bp.getRequestContext().put(
                    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    endpointBase
            );

            ctx.setAttribute("volandoPort", port);

            System.out.println("[InicioListener] Cliente SOAP listo. Endpoint: " + endpointBase);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo inicializar cliente SOAP VolandoWS", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
