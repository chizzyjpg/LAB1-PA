package uy.volando.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import Logica.ISistema;
import Logica.Sistema;
import Logica.DataUsuario;
import Logica.DataAerolinea;
import Logica.DataCliente;

@WebServlet("/consulta-perfil-usuario")
public class ConsultaPerfilUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
        if (sistema == null) {
            sistema = new Sistema();
            getServletContext().setAttribute("sistema", sistema);
        }
        DataUsuario usuario = sistema.obtenerUsuario(nickname);
        request.setAttribute("usuario", usuario);
        boolean esPropio = false;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario_logueado") != null) {
            DataUsuario logueado = (DataUsuario) session.getAttribute("usuario_logueado");
            esPropio = logueado.getNickname().equals(nickname);
        }
        request.setAttribute("esPropio", esPropio);
        if (usuario instanceof DataAerolinea) {
            request.setAttribute("rutasConfirmadas", sistema.obtenerRutasAerolinea(nickname, "Confirmada"));
            if (esPropio) {
                request.setAttribute("rutasIngresadas", sistema.obtenerRutasAerolinea(nickname, "Ingresada"));
                request.setAttribute("rutasRechazadas", sistema.obtenerRutasAerolinea(nickname, "Rechazada"));
            }
        } else if (usuario instanceof DataCliente && esPropio) {
            request.setAttribute("reservas", sistema.obtenerReservasCliente(nickname));
            request.setAttribute("paquetes", sistema.obtenerPaquetesCliente(nickname));
        }
        request.getRequestDispatcher("/WEB-INF/consulta/perfilUsuario.jsp").forward(request, response);
    }
}
