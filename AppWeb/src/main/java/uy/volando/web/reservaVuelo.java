package uy.volando.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.volando.publicar.WebServices;

import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class reservaVuelo.
 */
@WebServlet("/reservaVuelo")
public class reservaVuelo extends HttpServlet {
  public static final long serialVersionUID = 1L;

  public WebServices port;

  @Override
  public void init() throws ServletException {
      super.init();
      this.port = (WebServices) getServletContext().getAttribute("volandoPort");
      if (this.port == null) {
          throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
      }
  }

  public reservaVuelo() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    request.setAttribute("usuario", usuario);

    // ===== 1) Aerolíneas (siempre) =====
    List<uy.volando.publicar.DataAerolinea> aerolineas = port.listarAerolineas().getItem();
    request.setAttribute("aerolineas", aerolineas);

    // ===== 2) Parámetros saneados =====
    String aerolineaSel = trimOrNull(request.getParameter("aerolinea"));
    String rutaSel = trimOrNull(request.getParameter("ruta"));
    String vueloSel = trimOrNull(request.getParameter("vuelo"));
    System.out.println("DEBUG reservaVuelo.doGet - aerolineaSel: " + aerolineaSel);
    System.out.println("DEBUG reservaVuelo.doGet - rutaSel: " + rutaSel);
    System.out.println("DEBUG reservaVuelo.doGet - vueloSel: " + vueloSel);

    // ===== 3) Rutas confirmadas de la aerolínea (si hay aerolínea) =====
    List<uy.volando.publicar.DataRuta> rutas = null;
    if (aerolineaSel != null) {
      List<uy.volando.publicar.DataRuta> todas = port.listarPorAerolinea(aerolineaSel).getItem();
      rutas = (todas == null ? List.of()
          : todas.stream().filter(r -> r != null && r.getEstado() == uy.volando.publicar.EstadoRuta.CONFIRMADA)
              .toList());
    }
    request.setAttribute("rutas", rutas);

    // ===== 4) Validación Aerolínea–Ruta (si hay ambas) =====
    if (aerolineaSel != null && rutaSel != null) {
      boolean pertenece = rutas != null
          && rutas.stream().anyMatch(r -> r.getNombre() != null && r.getNombre().equals(rutaSel));
      if (!pertenece) {
        session.setAttribute("flash_error",
            "La aerolínea seleccionada no posee esa ruta. Vuelva a elegir la ruta.");
        response.sendRedirect(
            request.getContextPath() + "/reservaVuelo?aerolinea=" + url(aerolineaSel));
        return;
      }
    }

    // ===== 5) Vuelos (solo si aerolínea y ruta son válidas) =====
    List<uy.volando.publicar.DataVueloEspecifico> vuelos = null;
    if (aerolineaSel != null && rutaSel != null) {
      vuelos = port.listarVuelos(aerolineaSel, rutaSel).getItem();
      System.out.println("DEBUG reservaVuelo.doGet - vuelos.size(): " + (vuelos != null ? vuelos.size() : 0));
      if (vuelos != null) {
        for (uy.volando.publicar.DataVueloEspecifico v : vuelos) {
          System.out.println("DEBUG reservaVuelo.doGet - Vuelo: " + v.getNombre() + ", Fecha: " + v.getFecha());
        }
      }
    }
    request.setAttribute("vuelos", vuelos);

    // ===== 6) Detalle de vuelo (y validación Ruta–Vuelo) =====
    uy.volando.publicar.DataVueloEspecifico vueloDetalle = null;
    if (aerolineaSel != null && rutaSel != null && vueloSel != null) {
      vueloDetalle = port.buscarVuelo(aerolineaSel, rutaSel, vueloSel);
      if (vueloDetalle == null) {
        session.setAttribute("flash_error",
            "El vuelo no corresponde a la aerolínea y ruta seleccionadas.");
        response.sendRedirect(request.getContextPath() + "/reservaVuelo?aerolinea="
            + url(aerolineaSel) + "&ruta=" + url(rutaSel));
        return;
      }

      // ===== 7) Si hay cliente y vuelo, cargar paquetes disponibles filtrados por
      // ruta =====
      if (usuario instanceof uy.volando.publicar.DataCliente dc) {
        List<uy.volando.publicar.DataPaquete> paquetesDisponibles = port.listarPaquetesDisponiblesParaCompra().getItem();
        int cantidadPasajes = 1;
        try {
          String cantStr = request.getParameter("cantidadPasajes");
          if (cantStr != null) {
            cantidadPasajes = Integer.parseInt(cantStr);            
          }
        } catch (NumberFormatException ignored) {
        }

        // Filtramos por que el paquete incluya la ruta seleccionada (por nombre)
        List<uy.volando.publicar.DataPaquete> paquetesFiltrados = (paquetesDisponibles == null
            ? List.<uy.volando.publicar.DataPaquete>of()
            : (List<uy.volando.publicar.DataPaquete>) paquetesDisponibles.stream()
                .filter(p -> p != null && p.getCantRutas() > 0 && p.getRutasIncluidas() != null)
                .filter(p -> p.getRutasIncluidas().stream().filter(ri -> ri != null)
                        .anyMatch(ri -> ri.trim().equalsIgnoreCase(rutaSel)))
                .toList());

        request.setAttribute("paquetesDisponibles", paquetesFiltrados);
      }
    }
    request.setAttribute("vueloDetalle", vueloDetalle);

    // ===== 8) Forward final =====
    request.getRequestDispatcher("/WEB-INF/vuelo/reservaVuelo.jsp").forward(request, response);
  }


  private static String trimOrNull(String s) {
    if (s == null) {
      return null;      
    }
    String t = s.trim();
    return t.isEmpty() ? null : t;
  }

  private static String url(String s) {
    try {
      return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    } catch (Exception e) {
      return s;
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    if (!(usuario instanceof uy.volando.publicar.DataCliente dc)) {
      request.setAttribute("error", "Debes estar logueado como cliente para reservar.");
      doGet(request, response);
      return;
    }
    final String nicknameCliente = dc.getNickname();
    String aerolinea = request.getParameter("aerolinea");
    String ruta = request.getParameter("ruta");
    String vuelo = request.getParameter("vuelo");
    String tipoAsientoStr = request.getParameter("tipoAsiento");
    int cantidadPasajes = 1;
    int equipajeExtra = 0;
    try {
      cantidadPasajes = Integer.parseInt(request.getParameter("cantidadPasajes"));
    } catch (Exception e) {}
    try {
      equipajeExtra = Integer.parseInt(request.getParameter("equipajeExtra"));
    } catch (Exception e) {}
    String formaPago = request.getParameter("formaPago");
    String paquete = request.getParameter("paquete");
    // Nombres y apellidos de pasajeros
    String[] nombres = new String[cantidadPasajes];
    String[] apellidos = new String[cantidadPasajes];
    for (int i = 0; i < cantidadPasajes; i++) {
      nombres[i] = request.getParameter("nombrePasajero" + (i + 1));
      apellidos[i] = request.getParameter("apellidoPasajero" + (i + 1));
    }
    // Validar reserva duplicada
    List<uy.volando.publicar.DataReserva> reservasExistentes = port.listarReservas(aerolinea, ruta, vuelo).getItem();
    boolean yaReservo = reservasExistentes.stream()
      .anyMatch(r -> r.getNickCliente().getNickname().equalsIgnoreCase(nicknameCliente));
    if (yaReservo) {
      request.setAttribute("error", "Ya existe una reserva para este cliente en este vuelo.");
      doGet(request, response);
      return;
    }
    uy.volando.publicar.TipoAsiento tipoAsiento = uy.volando.publicar.TipoAsiento.valueOf(tipoAsientoStr);
    String equipajeTipoStr = request.getParameter("equipajeTipo");
    uy.volando.publicar.Equipaje equipaje = uy.volando.publicar.Equipaje.BOLSO;
    if (equipajeTipoStr != null) {
      equipaje = uy.volando.publicar.Equipaje.valueOf(equipajeTipoStr);
    }
    // Calcular costo (simplificado, deberías ajustar según lógica de negocio)
    float costoTotal = 0f;
    uy.volando.publicar.DataVueloEspecifico vueloDetalle = port.buscarVuelo(aerolinea, ruta, vuelo);
    if (vueloDetalle != null && vueloDetalle.getDruta() != null) {
      if (tipoAsiento == uy.volando.publicar.TipoAsiento.TURISTA
          && vueloDetalle.getDruta().getCostoTurista() != null) {
        costoTotal = vueloDetalle.getDruta().getCostoTurista().floatValue() * cantidadPasajes;
      } else if (tipoAsiento == uy.volando.publicar.TipoAsiento.EJECUTIVO
          && vueloDetalle.getDruta().getCostoEjecutivo() != null) {
        costoTotal = vueloDetalle.getDruta().getCostoEjecutivo().floatValue() * cantidadPasajes;
      }
    }
    // Registrar reserva
    try {
          // LOG: Mostrar todos los datos antes de llamar al WebService
      System.out.println("[DEBUG reservaVuelo] --- DATOS PARA registrarReserva ---");
      System.out.println("aerolinea: " + aerolinea);
      System.out.println("ruta: " + ruta);
      System.out.println("vuelo: " + vuelo);
      System.out.println("tipoAsiento: " + tipoAsiento);
      System.out.println("equipaje: " + equipaje);
      System.out.println("equipajeExtra: " + equipajeExtra);
      System.out.println("costoTotal: " + costoTotal);
      System.out.println("usuario: " + usuario);
      System.out.println("nombresArray: " + java.util.Arrays.toString(nombres));
      System.out.println("apellidosArray: " + java.util.Arrays.toString(apellidos));
      // Construir arrays de nombres y apellidos para enviar al WebService
      String[] nombresLista = new String[cantidadPasajes];
      String[] apellidosLista = new String[cantidadPasajes];
      for (int i = 0; i < cantidadPasajes; i++) {
        nombresLista[i] = nombres[i];
        apellidosLista[i] = apellidos[i];
      }
      uy.volando.publicar.StringArray nombresArray = new uy.volando.publicar.StringArray();
      nombresArray.getItem().addAll(java.util.Arrays.asList(nombresLista));
      uy.volando.publicar.StringArray apellidosArray = new uy.volando.publicar.StringArray();
      apellidosArray.getItem().addAll(java.util.Arrays.asList(apellidosLista));
      port.registrarReserva(
        aerolinea,
        ruta,
        vuelo,
        tipoAsiento,
        equipaje,
        equipajeExtra,
        costoTotal,
        (uy.volando.publicar.DataCliente) usuario,
        nombresArray,
        apellidosArray
      );
      System.out.println("[DEBUG reservaVuelo] --- RESERVA ENVIADA CORRECTAMENTE ---");
      request.setAttribute("exito", "Reserva registrada correctamente.");
    } catch (Exception ex) {
      ex.printStackTrace();
      request.setAttribute("error", ex.toString());
    }
    doGet(request, response);
  }

}