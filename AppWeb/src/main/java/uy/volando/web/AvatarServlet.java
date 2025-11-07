package uy.volando.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import Logica.Fabrica;
import Logica.ISistema;

@WebServlet("/avatar")
public class AvatarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nickname = req.getParameter("nickname");
        if (nickname == null || nickname.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ISistema sistema = Fabrica.getInstance().getSistema();
        byte[] imagen = sistema.obtenerAvatar(nickname); // Debe devolver los bytes o null

        if (imagen == null) {
        	System.out.println("No se encontró avatar para el nickname: " + nickname);
            // una imagen por defecto
            resp.sendRedirect(req.getContextPath() + "/media/default-avatar.png");
            return;
        }

        resp.setContentType("image/jpeg");
        resp.getOutputStream().write(imagen);
    }
}
