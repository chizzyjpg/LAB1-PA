package uy.volando.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import uy.volando.model.EstadoSesion;

// IMPORTS DEL CLIENTE SOAP
import uy.volando.publicar.WebServices;
import uy.volando.publicar.DataUsuario;

/**
 * Servlet implementation class Login.
 */
@WebServlet("/iniciar-sesion")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Cliente SOAP (port) que inicializa InicioServlet y se guarda en el contexto
    private WebServices port;

    public Login() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        // Recuperamos el port que dejó InicioServlet en el contexto
        this.port = (WebServices) getServletContext().getAttribute("volandoPort");
        if (this.port == null) {
            throw new ServletException(
                    "El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[DEBUG] Entrando a processRequest");
        request.setCharacterEncoding("UTF-8");

        HttpSession objSesion = request.getSession();
        String login = request.getParameter("login");   // nickname
        String password = request.getParameter("password");
        System.out.println("[DEBUG] login param: " + login);
        System.out.println("[DEBUG] password param: " + password);

        EstadoSesion nuevoEstado;

        System.out.println("[LOGIN] params -> nick=" + login + " , pass.len="
                + (password != null ? password.length() : null));

        DataUsuario dto = null;
        try {
            System.out.println("[DEBUG] Llamando a loguearUsuario (SOAP)");
            // LLAMADA AL WEB SERVICE, ya no al ISistema del core
            dto = port.loguearUsuario(login, password);
            System.out.println("[DEBUG] Resultado loguearUsuario: " + (dto != null ? "OK" : "NULL"));
        } catch (Exception e) {
            System.out.println("[ERROR] Excepción en loguearUsuario (SOAP): " + e.getMessage());
            e.printStackTrace();
            objSesion.setAttribute("estado_sesion", EstadoSesion.LOGIN_INCORRECTO);
            request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
            return;
        }

        if (dto == null) {
            System.out.println("[DEBUG] Login incorrecto, dto es null");
            nuevoEstado = EstadoSesion.LOGIN_INCORRECTO;
            objSesion.setAttribute("estado_sesion", nuevoEstado);
            request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
            return;
        }

        System.out.println("[DEBUG] Login correcto, redirigiendo a /home");
        nuevoEstado = EstadoSesion.LOGIN_CORRECTO;

        // Guardamos el DataUsuario generado por el WS en la sesión
        objSesion.setAttribute("usuario_logueado", dto);
        objSesion.setAttribute("estado_sesion", nuevoEstado);

        // === Determinar el rol ===

        String rol = null; // null = visitante
        if (dto instanceof uy.volando.publicar.DataCliente) {
            rol = "Cliente";
        }
        else if (dto instanceof uy.volando.publicar.DataAerolinea) {
            rol = "Aerolínea";
        }
        objSesion.setAttribute("rol", rol);

        response.sendRedirect(request.getContextPath() + "/home");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[LOGIN] doGet: mostrando login.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/home/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[LOGIN] Entrando a doPost");
        processRequest(request, response);
    }
}
