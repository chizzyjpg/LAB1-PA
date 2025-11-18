package uy.volando.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import uy.volando.publicar.DataAerolinea;
import uy.volando.publicar.DataAerolineaArray;
import uy.volando.publicar.DataCliente;
import uy.volando.publicar.DataReserva;
import uy.volando.publicar.DataRuta;
import uy.volando.publicar.DataVueloEspecifico;
import uy.volando.publicar.WebServices;

/**
 * Servlet implementation class consultaVuelo.
 */
@WebServlet("/consultaVuelo")
public class consultaVuelo extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private WebServices port;

  public consultaVuelo() {
    super();
  }

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

    // Usuario
    HttpSession session = req.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    req.setAttribute("usuario", usuario);

    // Aerolíneas
    List<?> aerolineas = unwrapList(port.listarAerolineas());
    req.setAttribute("aerolineas", aerolineas);

    // Parametros
    String nicknameAerolinea = req.getParameter("fltAerolinea");
    String nombreRuta = req.getParameter("fltRuta");
    String codigoVuelo = req.getParameter("codigoVuelo");

    // Rutas confirmadas para la aerolínea (si hay aerolínea)
    List<DataRuta> rutasConfirmadas = null;
    if (nicknameAerolinea != null && !nicknameAerolinea.isBlank()) {
      List<?> rutasWrapper = unwrapList(port.listarPorAerolinea(nicknameAerolinea));
      List<DataRuta> rutas = rutasWrapper.stream()
          .map(o -> (DataRuta) o)
          .collect(Collectors.toList());

      rutasConfirmadas = rutas.stream()
          .filter(r -> {
            try {
              Object estado = r.getEstado();
              return estado != null && "CONFIRMADA".equalsIgnoreCase(estado.toString());
            } catch (Exception e) {
              return false;
            }
          })
          .collect(Collectors.toList());

      req.setAttribute("rutas", rutasConfirmadas);
    }

    // ==== VALIDACIÓN COHERENCIA AEROLÍNEA–RUTA ====
    if (nicknameAerolinea != null && !nicknameAerolinea.isBlank() && nombreRuta != null
        && !nombreRuta.isBlank()) {

      boolean rutaPertenece = (rutasConfirmadas != null)
          && rutasConfirmadas.stream().anyMatch(r -> {
            try {
              return nombreRuta.equals(r.getNombre());
            } catch (Exception e) {
              return false;
            }
          });

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

      List<?> vuelosWrapper = unwrapList(port.listarVuelos(nicknameAerolinea, nombreRuta));
      List<DataVueloEspecifico> vuelos = vuelosWrapper.stream()
          .map(o -> (DataVueloEspecifico) o)
          .collect(Collectors.toList());
      req.setAttribute("vuelos", vuelos);
    }

    // Detalle de vuelo y reservas (solo si hay aerolínea y ruta válidas)
    if (nicknameAerolinea != null && !nicknameAerolinea.isBlank() && nombreRuta != null
        && !nombreRuta.isBlank() && codigoVuelo != null && !codigoVuelo.isBlank()) {

      DataVueloEspecifico vuelo = port.buscarVuelo(nicknameAerolinea, nombreRuta, codigoVuelo);

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

        if (esAerolinea && vuelo.getDruta() != null && usuarioNickname != null
            && usuarioNickname.equals(vuelo.getDruta().getNicknameAerolinea())) {

          List<?> reservasWrapper = unwrapList(port.listarReservas(nicknameAerolinea, nombreRuta,
              codigoVuelo));
          List<DataReserva> reservas = reservasWrapper.stream()
              .map(o -> (DataReserva) o)
              .collect(Collectors.toList());
          req.setAttribute("reservasVuelo", reservas);

        } else if (esCliente && usuarioNickname != null) {
          List<?> reservasWrapper = unwrapList(port.listarReservas(nicknameAerolinea, nombreRuta,
              codigoVuelo));
          List<DataReserva> reservas = reservasWrapper.stream()
              .map(o -> (DataReserva) o)
              .collect(Collectors.toList());
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
          DataReserva detalleReserva = port.buscarReserva(nicknameAerolinea, nombreRuta,
              codigoVuelo, idRes);
          req.setAttribute("detalleReserva", detalleReserva);
        } catch (NumberFormatException ignored) {
        }
      }
    }

    // Render final
    req.getRequestDispatcher("/WEB-INF/vuelo/consultaVuelo.jsp").forward(req, resp);
  }

  private List<?> unwrapList(Object wrapper) {
    if (wrapper == null) {
      return Collections.emptyList();
    }
    try {
      // método más común generado por wsimport
      Method m = wrapper.getClass().getMethod("getItem");
      Object r = m.invoke(wrapper);
      if (r instanceof List) return (List<?>) r;
    } catch (Exception ignored) {
    }
    try {
      // buscar cualquier getter que devuelva una List
      for (Method mm : wrapper.getClass().getMethods()) {
        if (mm.getParameterCount() == 0 && List.class.isAssignableFrom(mm.getReturnType())) {
          Object r = mm.invoke(wrapper);
          if (r instanceof List) return (List<?>) r;
        }
      }
    } catch (Exception ignored) {
    }
    return Collections.emptyList();
  }

    @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // No se usa en este caso de uso
    resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
  }
}