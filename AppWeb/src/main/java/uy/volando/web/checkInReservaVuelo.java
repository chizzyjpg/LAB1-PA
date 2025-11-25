package uy.volando.web;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.volando.publicar.DataReserva;
import uy.volando.publicar.WebServices;

import java.io.IOException;
import java.util.List;

@WebServlet ("/checkInReservaVuelo")
public class checkInReservaVuelo extends HttpServlet {
    private static  final long serialVersionUID = 1L;

    private WebServices port;

    public checkInReservaVuelo() { super();}

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

        HttpSession session = request.getSession();
        Object usuario = session.getAttribute("usuario_logueado");
        request.setAttribute("usuario", usuario);

        uy.volando.publicar.DataCliente cliente = (uy.volando.publicar.DataCliente) session.getAttribute("usuario_logueado");
        String nickCliente = cliente != null ? cliente.getNickname() : null;
        System.out.println("[checkInReservaVuelo][doGet] Nickname enviado: " + nickCliente);
        List<DataReserva> pendientes = port.listarReservasPendientesCheckIn(nickCliente).getItem();
        System.out.println("[checkInReservaVuelo][doGet] Reservas pendientes recibidas: " + (pendientes != null ? pendientes.size() : "null"));
        request.setAttribute("reservasPendientesCheckIn", pendientes);

        // Mostrar detalle si se seleccionó una reserva
        String reservaSel = request.getParameter("reservaSeleccionada");
        if (reservaSel != null && !reservaSel.isEmpty()) {
            try {
                int idReserva = Integer.parseInt(reservaSel);
                DataReserva detalle = null;
                if (pendientes != null) {
                    for (DataReserva r : pendientes) {
                        if (r.getIdReserva() == idReserva) {
                            detalle = r;
                            break;
                        }
                    }
                }
                request.setAttribute("detalleReserva", detalle);
            } catch (Exception e) {
                System.out.println("[checkInReservaVuelo][doGet] Error obteniendo detalle: " + e.getMessage());
            }
        }
        request.getRequestDispatcher("/WEB-INF/vuelo/checkInReservaVuelo.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object usuario = session.getAttribute("usuario_logueado");
        request.setAttribute("usuario", usuario);

        uy.volando.publicar.DataCliente cliente = (uy.volando.publicar.DataCliente) session.getAttribute("usuario_logueado");
        String nickCliente = cliente != null ? cliente.getNickname() : null;
        System.out.println("[checkInReservaVuelo][doPost] Nickname enviado: " + nickCliente);
        String idReservaStr = request.getParameter("reservaSeleccionada");
        String mensaje = null;
        boolean exito = false;

        if (nickCliente == null || idReservaStr == null || idReservaStr.isEmpty()) {
            mensaje = "Debe seleccionar una reserva válida.";
        } else {
            try {
                int idReserva = Integer.parseInt(idReservaStr);
                // Llamada al método del WebService para realizar el check-in
                port.realizarCheckIn(idReserva);
                mensaje = "Check-in realizado con éxito.";
                exito = true;
            } catch (NumberFormatException e) {
                mensaje = "ID de reserva inválido.";
            } catch (Exception e) {
                mensaje = "Error al realizar el check-in: " + e.getMessage();
            }
        }

        // Recargar la lista de reservas pendientes para mostrar feedback actualizado
        List<DataReserva> pendientes = port.listarReservasPendientesCheckIn(nickCliente).getItem();
        System.out.println("[checkInReservaVuelo][doPost] Reservas pendientes recibidas: " + (pendientes != null ? pendientes.size() : "null"));
        request.setAttribute("reservasPendientesCheckIn", pendientes);
        request.setAttribute("mensaje", mensaje);
        request.setAttribute("exito", exito);
        request.getRequestDispatcher("/WEB-INF/vuelo/checkInReservaVuelo.jsp").forward(request, response);
    }
}
