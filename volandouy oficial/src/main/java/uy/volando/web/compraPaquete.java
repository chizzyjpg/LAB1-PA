package uy.volando.web;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import Logica.DataCliente;
import Logica.DataCompraPaquete;
import Logica.DataPaquete;
import Logica.ISistema;

@WebServlet("/compraPaquete")
public class compraPaquete extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ============================
    // muestra la lista de paquetes y mensajes flash
    // ============================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object usuario = session.getAttribute("usuario_logueado");
        request.setAttribute("usuario", usuario);

        ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
        List<DataPaquete> paquetes = sistema.listarPaquetes();
        request.setAttribute("paquetesDisponibles", paquetes);

        request.getRequestDispatcher("/WEB-INF/paquete/compraPaquete.jsp").forward(request, response);
    }

    // ============================
    // procesa la compra
    // ============================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");

        try {
            // ----  Login y parámetros ----
            Object usuarioObj = session.getAttribute("usuario_logueado");
            if (!(usuarioObj instanceof DataCliente dc)) {
                session.setAttribute("flash_error", "Debes iniciar sesión como cliente para comprar un paquete.");
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;
            }
            String nombrePaquete = trimOrNull(request.getParameter("paqueteId"));
            if (nombrePaquete == null) {
                session.setAttribute("flash_error", "Debes seleccionar un paquete válido.");
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;
            }

            // ----  Validaciones previas ----
            if (!sistema.existePaquete(nombrePaquete)) {
                session.setAttribute("flash_error", "El paquete seleccionado no existe.");
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;
            }

            String nickname = dc.getNickname();
            if (sistema.clienteYaComproPaquete(nickname, nombrePaquete)) {
                session.setAttribute("flash_info", "Ya has comprado este paquete anteriormente.");
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;
            }

            DataPaquete dto = null;
            List<DataPaquete> todos = sistema.listarPaquetes();
            if (todos != null) {
                for (DataPaquete p : todos) {
                    if (p != null && nombrePaquete.equals(p.getNombre())) { dto = p; break; }
                }
            }
            if (dto == null) {
                session.setAttribute("flash_error", "No fue posible cargar el detalle del paquete.");
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;
            }
 
            boolean sinRutas = false;
            try {
                if (dto.getRutasIncluidas() != null && dto.getRutasIncluidas().isEmpty()) sinRutas = true;
                Integer cantRutas = dto.getCantRutas();
                if (cantRutas != null && cantRutas == 0) sinRutas = true;
            } catch (Throwable ignored) {}
            if (sinRutas) {
                session.setAttribute("flash_error", "Este paquete no posee rutas disponibles.");
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;
            }

            Integer cupos = null;
            try {
                var m = dto.getClass().getMethod("getCuposDisponibles");
                Object v = m.invoke(dto);
                if (v instanceof Number n) cupos = n.intValue();
            } catch (NoSuchMethodException ignored) {

            } catch (Throwable ignored) {}
            if (cupos != null && cupos <= 0) {
                session.setAttribute("flash_error", "No hay cupos disponibles para este paquete.");
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;
            }

            // Ejecutar compra ----
            DataCompraPaquete compra = new DataCompraPaquete(
                    nombrePaquete, nickname, new java.util.Date(), null, null);

            try {
                sistema.comprarPaquete(compra);
                session.setAttribute("flash_success", "¡Compra realizada con éxito!");
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;

            } catch (IllegalStateException e) {
                // CAPTURAMOS LA EXCEPCIÓN QUE TE ESTÁ ROMPIENDO
                String msg = (e.getMessage() != null) ? e.getMessage() : "No hay cupos disponibles para este paquete.";
                session.setAttribute("flash_error", msg);
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;

            } catch (Exception e) {
                session.setAttribute("flash_error", "No se pudo completar la compra: " +
                        (e.getMessage() != null ? e.getMessage() : "error inesperado"));
                response.sendRedirect(request.getContextPath() + "/compraPaquete");
                return;
            }

        } catch (Throwable fatal) {
            // Paracaídas final: nunca permitir que burbujee al contenedor
            session.setAttribute("flash_error", "No se pudo procesar la solicitud: " +
                    (fatal.getMessage() != null ? fatal.getMessage() : "error inesperado"));
            response.sendRedirect(request.getContextPath() + "/compraPaquete");
        }
    }

    // ============================
    // Helpers
    // ============================
    private static String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
