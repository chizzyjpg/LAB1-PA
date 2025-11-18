package uy.volando.web;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uy.volando.publicar.WebServices;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet que expone dos endpoints para verificar disponibilidad
 * de nickname y email vía AJAX/Fetch desde el frontend.
 *
 * - /api/verificarNickname?nickname=...
 * - /api/verificarEmail?email=...
 *
 * Responde JSON: {"disponible": true} o {"disponible": false}
 */
@WebServlet(urlPatterns = {"/api/verificarNickname", "/api/verificarEmail"})
public class VerificarUsuario extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private WebServices port;

    @Override
    public void init() throws ServletException {
        super.init();
        this.port = (WebServices) getServletContext().getAttribute("volandoPort");
        if(this.port == null) {
            throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
        }
    }

    public VerificarUsuario() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Aseguramos UTF-8
        request.setCharacterEncoding("UTF-8");

        // Preparamos la respuesta como JSON
        response.setContentType("application/json;charset=UTF-8");

        // ServletPath nos dice cuál de los dos endpoints se llamó
        String servletPath = request.getServletPath();

        boolean disponible;

        try (PrintWriter out = response.getWriter()) {

            if (servletPath.endsWith("/verificarNickname")) {
                // --- CASO NICKNAME ---
                String nickname = request.getParameter("nickname");

                if (nickname == null || nickname.isBlank()) {
                    // Parámetro inválido → 400
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Falta el parámetro 'nickname'\"}");
                    return;
                }

                // Llamás al WS: existeNickname → true si YA está usado
                boolean existe = port.existeNickname(nickname);
                disponible = !existe;

                // Respondés JSON
                out.print("{\"disponible\": " + disponible + "}");

            } else if (servletPath.endsWith("/verificarEmail")) {
                // --- CASO EMAIL ---
                String email = request.getParameter("email");

                if (email == null || email.isBlank()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Falta el parámetro 'email'\"}");
                    return;
                }

                boolean existe = port.existeEmail(email);
                disponible = !existe;

                out.print("{\"disponible\": " + disponible + "}");

            } else {
                // Esto no debería pasar, pero por las dudas
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Endpoint no encontrado\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
