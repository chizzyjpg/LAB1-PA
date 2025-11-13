package uy.volando.web;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uy.volando.publicar.WebServices;

@WebServlet("/avatar")
public class AvatarServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nickname = req.getParameter("nickname");
        if (nickname == null || nickname.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro nickname");
            return;
        }

        byte[] imagen = null;
        try {
            imagen = port.obtenerAvatar(nickname);
        } catch (Exception e) {
            // Logueamos y devolvemos default
            System.err.println("[AvatarServlet] Error llamando al WS: " + e.getMessage());
        }
        if (imagen == null || imagen.length == 0) {
            resp.sendRedirect(req.getContextPath() + "/media/default-avatar.png");
            return;
        }
        // Heurística simple para content-type
        String contentType = "image/jpeg";
        // Para detectar PNG, se mira los primeros bytes (signature 89 50 4E 47)
        if (imagen.length >= 4 &&
                (imagen[0] & 0xFF) == 0x89 &&
                (imagen[1] & 0xFF) == 0x50 &&
                (imagen[2] & 0xFF) == 0x4E &&
                (imagen[3] & 0xFF) == 0x47) {
            contentType = "image/png";
        }

        resp.setContentType(contentType);
        resp.setContentLength(imagen.length);

        //permitir cachear 10 minutos para reducir carga
        resp.setHeader("Cache-Control", "public, max-age=600");

        try (OutputStream os = resp.getOutputStream()) {
            os.write(imagen);
        }
    }
}
