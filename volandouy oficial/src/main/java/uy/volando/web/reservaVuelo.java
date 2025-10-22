package uy.volando.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import Logica.ISistema;

import Logica.DataRuta;
import Logica.DataAerolinea;


@WebServlet("/reservaVuelo")
public class reservaVuelo extends HttpServlet {
	public static final long serialVersionUID = 1L;
	
	public reservaVuelo() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    HttpSession session = request.getSession();
	    Object usuario = session.getAttribute("usuario_logueado");
	    request.setAttribute("usuario", usuario);

	    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");

	    // ===== 1) Aerolíneas (siempre) =====
	    List<Logica.DataAerolinea> aerolineas = sistema.listarAerolineas();
	    request.setAttribute("aerolineas", aerolineas);

	    // ===== 2) Parámetros saneados =====
	    String aerolineaSel = trimOrNull(request.getParameter("aerolinea"));
	    String rutaSel      = trimOrNull(request.getParameter("ruta"));
	    String vueloSel     = trimOrNull(request.getParameter("vuelo"));

	    // ===== 3) Rutas confirmadas de la aerolínea (si hay aerolínea) =====
	    List<Logica.DataRuta> rutas = null;
	    if (aerolineaSel != null) {
	        List<Logica.DataRuta> todas = sistema.listarPorAerolinea(aerolineaSel);
	        rutas = (todas == null ? List.of() :
	                 todas.stream()
	                      .filter(r -> r != null && r.getEstado() == Logica.EstadoRuta.CONFIRMADA)
	                      .toList());
	    }
	    request.setAttribute("rutas", rutas);

	    // ===== 4) Validación Aerolínea–Ruta (si hay ambas) =====
	    if (aerolineaSel != null && rutaSel != null) {
	        boolean pertenece = rutas != null && rutas.stream()
	                .anyMatch(r -> r.getNombre() != null && r.getNombre().equals(rutaSel));
	        if (!pertenece) {
	            session.setAttribute("flash_error", "La aerolínea seleccionada no posee esa ruta. Vuelva a elegir la ruta.");
	            response.sendRedirect(request.getContextPath() + "/reservaVuelo?aerolinea=" + url(aerolineaSel));
	            return;
	        }
	    }

	    // ===== 5) Vuelos (solo si aerolínea y ruta son válidas) =====
	    List<Logica.DataVueloEspecifico> vuelos = null;
	    if (aerolineaSel != null && rutaSel != null) {
	        vuelos = sistema.listarVuelos(aerolineaSel, rutaSel);
	    }
	    request.setAttribute("vuelos", vuelos);

	    // ===== 6) Detalle de vuelo (y validación Ruta–Vuelo) =====
	    Logica.DataVueloEspecifico vueloDetalle = null;
	    if (aerolineaSel != null && rutaSel != null && vueloSel != null) {
	        vueloDetalle = sistema.buscarVuelo(aerolineaSel, rutaSel, vueloSel);
	        if (vueloDetalle == null) {
	            session.setAttribute("flash_error", "El vuelo no corresponde a la aerolínea y ruta seleccionadas.");
	            response.sendRedirect(request.getContextPath() + "/reservaVuelo?aerolinea=" + url(aerolineaSel) + "&ruta=" + url(rutaSel));
	            return;
	        }

	        // ===== 7) Si hay cliente y vuelo, cargar paquetes disponibles filtrados por ruta =====
	        if (usuario instanceof Logica.DataCliente dc) {
	            List<Logica.DataPaquete> paquetesDisponibles = sistema.listarPaquetesDisponiblesParaCompra();
	            int cantidadPasajes = 1;
	            try {
	                String cantStr = request.getParameter("cantidadPasajes");
	                if (cantStr != null) cantidadPasajes = Integer.parseInt(cantStr);
	            } catch (NumberFormatException ignored) {}

	            // Filtramos por que el paquete incluya la ruta seleccionada (por nombre)
	            List<Logica.DataPaquete> paquetesFiltrados =
	                    (paquetesDisponibles == null ? List.<Logica.DataPaquete>of() :
	                     paquetesDisponibles.stream()
	                         .filter(p -> p != null && p.getCantRutas() > 0 && p.getRutasIncluidas() != null)
	                         .filter(p -> p.getRutasIncluidas().stream()
	                                 .filter(ri -> ri != null)
	                                 .anyMatch(ri -> ri.trim().equalsIgnoreCase(rutaSel)))
	                         .toList());

	            request.setAttribute("paquetesDisponibles", paquetesFiltrados);
	        }
	    }
	    request.setAttribute("vueloDetalle", vueloDetalle);

	    // ===== 8) Forward final =====
	    request.getRequestDispatcher("/WEB-INF/vuelo/reservaVuelo.jsp").forward(request, response);
	}

	/* === Helpers usados por doGet === */
	private static String trimOrNull(String s) {
	    if (s == null) return null;
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    Object usuario = session.getAttribute("usuario_logueado");
	    Logica.ISistema sistema = (Logica.ISistema) getServletContext().getAttribute("sistema");
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
	        nombres[i] = request.getParameter("nombrePasajero" + (i+1));
	        apellidos[i] = request.getParameter("apellidoPasajero" + (i+1));
	    }
	    // Validar usuario logueado y tipo cliente
	    if (!(usuario instanceof Logica.DataCliente)) {
	        request.setAttribute("error", "Debes estar logueado como cliente para reservar.");
	        doGet(request, response);
	        return;
	    }
	    String nicknameCliente = ((Logica.DataCliente) usuario).getNickname();
	    // Validar reserva duplicada
	    List<Logica.DataReserva> reservasExistentes = sistema.listarReservas(aerolinea, ruta, vuelo);
	    boolean yaReservo = reservasExistentes.stream().anyMatch(r -> r.getNickCliente().getNickname().equalsIgnoreCase(nicknameCliente));
	    if (yaReservo) {
	        request.setAttribute("error", "Ya existe una reserva para este cliente en este vuelo.");
	        doGet(request, response);
	        return;
	    }
	    // Construir lista de pasajes
	    java.util.List<Logica.DataPasaje> pasajes = new java.util.ArrayList<>();
	    for (int i = 0; i < cantidadPasajes; i++) {
	        pasajes.add(new Logica.DataPasaje(nombres[i], apellidos[i]));
	    }
	    // Tipo de asiento
	    Logica.TipoAsiento tipoAsiento = Logica.TipoAsiento.valueOf(tipoAsientoStr);
	    String equipajeTipoStr = request.getParameter("equipajeTipo");
	    Logica.Equipaje equipaje = Logica.Equipaje.BOLSO;
	    if (equipajeTipoStr != null) {
	        equipaje = Logica.Equipaje.valueOf(equipajeTipoStr);
	    }
	    // Calcular costo (simplificado, deberías ajustar según lógica de negocio)
	    float costoTotal = 0f;
	    Logica.DataVueloEspecifico vueloDetalle = sistema.buscarVuelo(aerolinea, ruta, vuelo);
	    if (vueloDetalle != null && vueloDetalle.getDRuta() != null) {
	        if (tipoAsiento == Logica.TipoAsiento.TURISTA && vueloDetalle.getDRuta().getCostoTurista() != null) {
	            costoTotal = vueloDetalle.getDRuta().getCostoTurista().floatValue() * cantidadPasajes;
	        } else if (tipoAsiento == Logica.TipoAsiento.EJECUTIVO && vueloDetalle.getDRuta().getCostoEjecutivo() != null) {
	            costoTotal = vueloDetalle.getDRuta().getCostoEjecutivo().floatValue() * cantidadPasajes;
	        }
	    }
	    // Crear DataReserva
	    Logica.DataReserva reserva = new Logica.DataReserva(0, new java.util.Date(), tipoAsiento, equipaje, equipajeExtra, costoTotal, (Logica.DataCliente) usuario);
	    reserva.setPasajes(pasajes);
	    // Registrar reserva
	    try {
	        sistema.registrarReserva(aerolinea, ruta, vuelo, reserva);
	        request.setAttribute("exito", "Reserva registrada correctamente.");
	    } catch (Exception ex) {
	        request.setAttribute("error", ex.getMessage());
	    }
	    doGet(request, response);
	}
	    
}