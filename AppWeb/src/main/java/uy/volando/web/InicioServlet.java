package uy.volando.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import Logica.Fabrica;
import Logica.ISistema;

@WebServlet(loadOnStartup = 1, urlPatterns = "/inicio")
public class InicioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();

        // 1) Obtenemos el Sistema desde la Fabrica
        ISistema sistema = Fabrica.getInstance().getSistema();

        // 2) Lo guardamos en el contexto de la aplicación
        ServletContext ctx = getServletContext();
        ctx.setAttribute("sistema", sistema);

        System.out.println("[InicioServlet] Sistema cargado en el contexto de la aplicación.");
    }
}
//TODOS LOS SERVLETS PUEDEN ACCEDER AL SISTEMA ASI:
//@Override
/*public void init() throws ServletException {
	super.init();
	this.sistema = (ISistema) getServletContext().getAttribute("sistema");
     if (this.sistema == null) {
        throw new ServletException("El sistema no fue inicializado por InicioServlet.");
    }
}*/