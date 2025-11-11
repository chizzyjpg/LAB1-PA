package uy.volando.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import uy.volando.publicar.VolandoWS;
import uy.volando.publicar.WebServices;

@WebServlet(loadOnStartup = 1, urlPatterns = "/inicio")
public class InicioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            // Crear el Service y el Port generados por wsimport
            VolandoWS service = new VolandoWS();
            WebServices port = service.getWebServicesPort();

            // Guardar el port en el contexto de la aplicación
            ServletContext ctx = getServletContext();
            ctx.setAttribute("volandoPort", port);

            System.out.println("[InicioServlet] Cliente SOAP (WebServices) cargado en el contexto.");
        } catch (Exception e) {
            throw new ServletException("No se pudo inicializar el cliente SOAP VolandoWS", e);
        }
    }
}
