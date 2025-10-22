package uy.volando.web;

import java.io.IOException;
import java.util.List;

import Logica.DataAerolinea;
import Logica.DataCliente;
import Logica.DataReserva;
import Logica.DataRuta;
import Logica.DataVueloEspecifico;
import Logica.EstadoRuta;
import Logica.ISistema;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class consultaVuelo.
 */
@WebServlet("/consultaVuelo")
public class consultaVuelo extends HttpServlet {
  public static final long serialVersionUID = 1L;

  public consultaVuelo() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");

    // Usuario
    HttpSession session = req.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    req.setAttribute("usuario", usuario);

    // Aerolíneas
    List<DataAerolinea> aerolineas = sistema.listarAerolineas();
    req.setAttribute("aerolineas", aerolineas);

    // Parametros
    String nicknameAerolinea = req.getParameter("fltAerolinea");
    String nombreRuta = req.getParameter("fltRuta");
    String codigoVuelo = req.getParameter("codigoVuelo");

    // Rutas confirmadas para la aerolínea (si hay aerolínea)
    List<DataRuta> rutasConfirmadas = null;
    if (nicknameAerolinea != null && !nicknameAerolinea.isBlank()) {
      List<DataRuta> rutas = sistema.listarPorAerolinea(nicknameAerolinea);
      rutasConfirmadas = rutas.stream().filter(r -> r.getEstado() == EstadoRuta.CONFIRMADA)
          .toList();
      req.setAttribute("rutas", rutasConfirmadas);
    }

    // ==== VALIDACIÓN COHERENCIA AEROLÍNEA–RUTA ====
    if (nicknameAerolinea != null && !nicknameAerolinea.isBlank() && nombreRuta != null
        && !nombreRuta.isBlank()) {

      boolean rutaPertenece = (rutasConfirmadas != null)
          && rutasConfirmadas.stream().anyMatch(r -> r.getNombre().equals(nombreRuta));

      if (!rutaPertenece) {
        // hacemos el flash error
        session.setAttribute("flash_error",
            "La aerolínea seleccionada no posee la ruta indicada. Vuelva a elegir la ruta.");
        resp.sendRedirect(
            req.getContextPath() + "/consultaVuelo?fltAerolinea=" + nicknameAerolinea);
        return;
      }
    }

    // Vuelos (solo si hay aerolínea y ruta válidas)
    if (nicknameAerolinea != null && !nicknameAerolinea.isBlank() && nombreRuta != null
        && !nombreRuta.isBlank()) {

      List<DataVueloEspecifico> vuelos = sistema.listarVuelos(nicknameAerolinea, nombreRuta);
      req.setAttribute("vuelos", vuelos);
    }

    // Detalle de vuelo y reservas (solo si hay aerolínea y ruta válidas)
    if (nicknameAerolinea != null && !nicknameAerolinea.isBlank() && nombreRuta != null
        && !nombreRuta.isBlank() && codigoVuelo != null && !codigoVuelo.isBlank()) {

      DataVueloEspecifico vuelo = sistema.buscarVuelo(nicknameAerolinea, nombreRuta, codigoVuelo);

      if (vuelo == null) {
        // Vuelo no pertenece a esa aerolínea/ruta → flash + redirect dejando filtros
        session.setAttribute("flash_error",
            "No se encontró el vuelo para la aerolínea y ruta seleccionadas.");
        String qs = "fltAerolinea=" + nicknameAerolinea + "&fltRuta=" + nombreRuta;
        resp.sendRedirect(req.getContextPath() + "/consultaVuelo?" + qs);
        return;
      }

      req.setAttribute("detalleVuelo", vuelo);

      // Reservas

      if (usuario != null) {
        String usuarioNickname = null;
        boolean esAerolinea = false, esCliente = false;

        if (usuario instanceof DataAerolinea da) {
          usuarioNickname = da.getNickname();
          esAerolinea = true;
        } else if (usuario instanceof DataCliente dc) {
          usuarioNickname = dc.getNickname();
          esCliente = true;
        }

        if (esAerolinea && vuelo.getDRuta() != null && usuarioNickname != null
            && usuarioNickname.equals(vuelo.getDRuta().getNicknameAerolinea())) {

          List<DataReserva> reservas = sistema.listarReservas(nicknameAerolinea, nombreRuta,
              codigoVuelo);
          req.setAttribute("reservasVuelo", reservas);

        } else if (esCliente && usuarioNickname != null) {
          List<DataReserva> reservas = sistema.listarReservas(nicknameAerolinea, nombreRuta,
              codigoVuelo);
          for (DataReserva r : reservas) {
            if (r.getNickCliente() != null
                && usuarioNickname.equals(r.getNickCliente().getNickname())) {
              req.setAttribute("reservaCliente", r);
              break;
            }
          }
        }
      }

      String idReserva = req.getParameter("idReserva");
      if (idReserva != null && !idReserva.isBlank()) {
        try {
          int idRes = Integer.parseInt(idReserva);
          DataReserva detalleReserva = sistema.buscarReserva(nicknameAerolinea, nombreRuta,
              codigoVuelo, idRes);
          req.setAttribute("detalleReserva", detalleReserva);
        } catch (NumberFormatException ignored) {
        }
      }
    }

    // Render final
    req.getRequestDispatcher("/WEB-INF/vuelo/consultaVuelo.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // No se usa en este caso de uso
    resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
  }
}