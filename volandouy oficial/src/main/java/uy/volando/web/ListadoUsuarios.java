package uy.volando.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import Logica.ISistema;
import Logica.Sistema;
import Logica.DataUsuario;

@WebServlet("/listado-usuarios")
public class ListadoUsuarios extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
        if (sistema == null) {
            sistema = new Sistema();
            getServletContext().setAttribute("sistema", sistema);
        }
        List<DataUsuario> usuarios = sistema.listarUsuarios();
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/WEB-INF/consulta/listadoUsuarios.jsp").forward(request, response);
    }
}
