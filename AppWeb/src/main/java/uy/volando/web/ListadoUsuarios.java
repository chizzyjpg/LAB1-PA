package uy.volando.web;

import uy.volando.publicar.DataUsuario;
import uy.volando.publicar.DataUsuarioMuestraWeb;
import uy.volando.publicar.WebServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/listado-usuarios")
public class ListadoUsuarios extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private WebServices port;

    @Override
    public void init() throws ServletException {
        super.init();
        this.port = (WebServices) getServletContext().getAttribute("volandoPort");
        if (this.port == null) {
            throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        DataUsuario logueado = (session != null)
                ? (DataUsuario) session.getAttribute("usuario_logueado")
                : null;

        if (logueado == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        String nickLogueado = logueado.getNickname();

        List<DataUsuarioMuestraWeb> usuarios = port.listarUsuariosWeb(nickLogueado).getItem();

        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/WEB-INF/consulta/listadoUsuarios.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        DataUsuario logueado = (session != null)
                ? (DataUsuario) session.getAttribute("usuario_logueado")
                : null;

        if (logueado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String miNick = logueado.getNickname();
        String nickObjetivo = request.getParameter("nickObjetivo");
        String accion = request.getParameter("accion"); // "seguir" o "dejar"

        if (nickObjetivo != null && !nickObjetivo.isBlank() && accion != null) {
            if ("seguir".equals(accion)) {
                port.seguirUsuario(miNick, nickObjetivo);
            } else if ("dejar".equals(accion)) {
                port.dejarDeSeguirUsuario(miNick, nickObjetivo);
            }
        }

        // PRG: volvemos al GET para evitar re-POST con F5
        response.sendRedirect(request.getContextPath() + "/listado-usuarios");
    }
}
