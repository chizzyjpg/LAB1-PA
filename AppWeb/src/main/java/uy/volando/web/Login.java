package uy.volando.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import uy.volando.web.DeviceUtils;

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

        request.setCharacterEncoding("UTF-8");

        HttpSession objSesion = request.getSession();

        DeviceUtils.DeviceType deviceType = (DeviceUtils.DeviceType) objSesion.getAttribute("deviceType"); //Leemos tipo de dispositivo detectado por el filtro
        System.out.println("[DEBUG] deviceType en login: " + deviceType);

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
            // LLAMADA AL WEB SERVICE
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

        nuevoEstado = EstadoSesion.LOGIN_CORRECTO;

        // === Determinar el rol ===
        String rol = null; // null = visitante
        if (dto instanceof uy.volando.publicar.DataCliente) {
            rol = "Cliente";
        }
        else if (dto instanceof uy.volando.publicar.DataAerolinea) {
            rol = "Aerolínea";
        }
        objSesion.setAttribute("rol", rol);

        // === RESTRICCIÓN POR DISPOSITIVO ===
        // Si es MOBILE: solo se permite Cliente.
        // No puede entrar Aerolínea ni Visitante (rol == null).
        if (deviceType == DeviceUtils.DeviceType.MOBILE) {
            if (!"Cliente".equals(rol)) {
                System.out.println("[DEBUG] Login denegado desde móvil para rol: " + rol);
                objSesion.setAttribute("estado_sesion", EstadoSesion.LOGIN_INCORRECTO);
                // Mensaje para la pagina JSP
                request.setAttribute("mensaje_error",
                        "Solo los clientes pueden iniciar sesión desde dispositivos móviles.");
                request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
                return;
            }
        }
        // Si no es MOBILE (DESKTOP o UNKNOWN), dejamos pasar
        System.out.println("[DEBUG] Login correcto, redirigiendo a /home");
        objSesion.setAttribute("usuario_logueado", dto);
        objSesion.setAttribute("estado_sesion", nuevoEstado);

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
