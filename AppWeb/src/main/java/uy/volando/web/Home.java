package uy.volando.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import uy.volando.model.EstadoSesion;

/**
 * Servlet implementation class Home.
 */

@WebServlet("/home")
public class Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Home() {
        super();
    }

    // inicializa la sesión si no estaba creada.
    public static void initSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("estado_sesion") == null) {
            session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
        }
    }

    // Devuelve el estado de la sesión.
    public static EstadoSesion getEstado(HttpServletRequest request) {
        return (EstadoSesion) request.getSession().getAttribute("estado_sesion");
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("[HOME] Entrando a processRequest");
        initSession(req);
        EstadoSesion estado = getEstado(req);
        System.out.println("[HOME] Estado de sesión: " + estado);
        switch (estado) {
            case NO_LOGIN:
                System.out.println("[HOME] Estado NO_LOGIN, mostrando iniciar.jsp");
                req.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").forward(req, resp);
                break;
            case LOGIN_INCORRECTO:
                System.out.println("[HOME] Estado LOGIN_INCORRECTO, mostrando iniciar.jsp con error");
                req.setAttribute("error", "Login o contraseña incorrectos");
                req.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").forward(req, resp);
                break;
            case LOGIN_CORRECTO:
                System.out.println("[HOME] Estado LOGIN_CORRECTO, mostrando iniciado.jsp");
                req.getRequestDispatcher("/WEB-INF/home/iniciado.jsp").forward(req, resp);
                break;
            default:
                System.out.println("[HOME] Estado inválido, enviando error 500");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Estado de sesión inválido");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
