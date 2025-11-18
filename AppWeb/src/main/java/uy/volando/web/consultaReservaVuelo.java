package uy.volando.web;
/*
import Logica.DataAerolinea;
import Logica.DataReserva;
import Logica.DataRuta;
import Logica.DataUsuario;
import Logica.DataVueloEspecifico;
 */
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import uy.volando.publicar.*;


import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class consultaReservaVuelo.
 */
@WebServlet("/consultaReservaVuelo")
public class consultaReservaVuelo extends HttpServlet {
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

  /**
   * Constructor de la clase consultaReservaVuelo.
   */
  public consultaReservaVuelo() {
    super();
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

    String rol = (usuario instanceof DataAerolinea) ? "aerolinea" : "cliente";
    request.setAttribute("rol", rol);

    String aerolineaSel = request.getParameter("aerolinea");
    String rutaSel = request.getParameter("ruta");
    String vueloSel = request.getParameter("vuelo");
    String reservaSel = request.getParameter("reserva");

    String nickAerolinea = rol.equals("aerolinea") ? ((DataUsuario) usuario).getNickname()
        : aerolineaSel;

    // Si no hay aerolínea seleccionada (cliente), mostrar selección de aerolínea
    if (rol.equals("cliente") && aerolineaSel == null) {
    DataAerolineaArray dataAerolineaArray = port.listarAerolineas();
    List<DataAerolinea> aerolineas = dataAerolineaArray != null ? dataAerolineaArray.getItem() : null;
    request.setAttribute("aerolineas", aerolineas);
      request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request,
          response);
      return;
    }

    // Mostrar selección de ruta y vuelo en la misma pantalla
    DataRutaArray dataRutaArray = port.listarPorAerolinea(nickAerolinea);
    List<DataRuta> rutas = dataRutaArray != null ? dataRutaArray.getItem() : null;
    request.setAttribute("aerolineaSeleccionada", nickAerolinea);
    request.setAttribute("rutas", rutas);

    List<DataVueloEspecifico> vuelos = null;
    if (rutaSel != null && !rutaSel.isEmpty()) {
        DataVueloEspecificoArray dataVueloEspecificoArray = port.listarVuelos(nickAerolinea, rutaSel);
        if (dataVueloEspecificoArray != null && dataVueloEspecificoArray.getItem() != null) {
            vuelos = dataVueloEspecificoArray.getItem();
        }
        request.setAttribute("rutaSeleccionada", rutaSel);
        request.setAttribute("vuelos", vuelos);

        // Si ya se seleccionó vuelo, continuar con el flujo normal
        if (vueloSel != null && !vueloSel.isEmpty()) {
            if (reservaSel != null) {
                try {
                    int idReserva = Integer.parseInt(reservaSel);
                    DataReserva detalle = port.buscarReserva(nickAerolinea, rutaSel, vueloSel, idReserva);
                    request.setAttribute("detalleReserva", detalle);
                } catch (Exception e) {
                    request.setAttribute("error",
                            "No se pudo obtener el detalle de la reserva: " + e.getMessage());
                }
                request.setAttribute("vueloSeleccionado", vueloSel);
                request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request,
                        response);
                return;
            }

            if (rol.equals("aerolinea")) {
                DataReservaArray dataReservaArray = port.listarReservas(nickAerolinea, rutaSel, vueloSel);
                List<DataReserva> reservas = dataReservaArray != null ? dataReservaArray.getItem() : null;
                request.setAttribute("vueloSeleccionado", vueloSel);
                request.setAttribute("reservas", reservas);
                request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request,
                        response);
                return;
            } else {
                DataReservaArray dataReservaArray = port.listarReservas(nickAerolinea, rutaSel, vueloSel);
                List<DataReserva> reservas = dataReservaArray != null ? dataReservaArray.getItem() : null;
                DataReserva miReserva = null;
                assert reservas != null;
                for (DataReserva r : reservas) {
                    if (r.getNickCliente().getNickname().equals(((DataUsuario) usuario).getNickname())) {
                        miReserva = r;
                        break;
                    }
                }
                request.setAttribute("vueloSeleccionado", vueloSel);
                request.setAttribute("miReserva", miReserva);
                // Asegurarse de que siempre se setea el atributo, aunque la lista esté vacía
                request.setAttribute("reservasCliente", reservas);
                request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request,
                        response);
                return;
            }
        }
    }

    // Si no se seleccionó vuelo, mostrar solo selección de ruta y vuelo
    request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request,
        response);
  }
}