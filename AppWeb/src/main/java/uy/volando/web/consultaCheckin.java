package uy.volando.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.volando.publicar.*;
import java.io.IOException;

@WebServlet("/consultaCheckin")
public class consultaCheckin extends HttpServlet {
    public static final long serialVersionUID = 1L;
    private WebServices port;

    @Override
    public void init() throws ServletException {
        super.init();
        this.port = (WebServices) getServletContext().getAttribute("volandoPort");
        if(this.port == null) {
            throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object usuario = session.getAttribute("usuario_logueado");
        request.setAttribute("usuario", usuario);
        if (usuario == null) {
            response.sendRedirect("iniciar.jsp");
            return;
        }

        String reservaSel = request.getParameter("reserva");
        String aerolineaSel = request.getParameter("aerolinea");
        String rutaSel = request.getParameter("ruta");
        String vueloSel = request.getParameter("vuelo");

        // Si no se seleccionó una reserva, mostrar la lista de reservas con check-in realizado
        java.util.List<DataReserva> reservasCheckin = null;
        if (usuario instanceof DataCliente) {
            String nickCliente = ((DataCliente) usuario).getNickname();
            DataReservaArray dataReservaArray = port.listarReservasConCheckin(nickCliente);
            reservasCheckin = dataReservaArray != null ? dataReservaArray.getItem() : null;
        }
        if (reservaSel == null) {
            request.setAttribute("reservasCheckin", reservasCheckin);
            request.getRequestDispatcher("/WEB-INF/vuelo/consultaCheckin.jsp").forward(request, response);
            return;
        }

        // Si se seleccionó una reserva, buscar el detalle en la lista de reservas con check-in
        if (reservasCheckin != null) {
            try {
                int idReserva = Integer.parseInt(reservaSel);
                DataReserva detalle = null;
                for (DataReserva r : reservasCheckin) {
                    if (r.getIdReserva() == idReserva) {
                        detalle = r;
                        break;
                    }
                }
                if (detalle != null) {
                    request.setAttribute("detalleReserva", detalle);
                } else {
                    request.setAttribute("error", "No se pudo encontrar el check-in seleccionado.");
                }
            } catch (Exception e) {
                request.setAttribute("error", "No se pudo obtener el detalle del check-in: " + e.getMessage());
            }
        }
        request.getRequestDispatcher("/WEB-INF/vuelo/consultaCheckin.jsp").forward(request, response);
    }
}
