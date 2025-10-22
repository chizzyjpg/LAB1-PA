package uy.volando.web;

import java.io.IOException;


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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    Object usuario = session.getAttribute("usuario_logueado");
	    request.setAttribute("usuario", usuario);
	    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
	    List<DataAerolinea> aerolineas = sistema.listarAerolineas();
	    request.setAttribute("aerolineas", aerolineas);
	    String aerolineaSel = request.getParameter("aerolinea");
	    List<DataRuta> rutas = null;
	    if (aerolineaSel != null && !aerolineaSel.isEmpty()) {
	        rutas = sistema.listarPorAerolinea(aerolineaSel).stream()
	            .filter(r -> r.getEstado() != null && r.getEstado().name().equalsIgnoreCase("CONFIRMADA"))
	            .toList();
	    }
	    request.setAttribute("rutas", rutas);
	    String rutaSel = request.getParameter("ruta");
	    List<Logica.DataVueloEspecifico> vuelos = null;
	    if (aerolineaSel != null && !aerolineaSel.isEmpty() && rutaSel != null && !rutaSel.isEmpty()) {
	        vuelos = sistema.listarVuelos(aerolineaSel, rutaSel);
	    }
	    request.setAttribute("vuelos", vuelos);
	    String vueloSel = request.getParameter("vuelo");
	    Logica.DataVueloEspecifico vueloDetalle = null;
	    if (aerolineaSel != null && !aerolineaSel.isEmpty() && rutaSel != null && !rutaSel.isEmpty() && vueloSel != null && !vueloSel.isEmpty()) {
	        vueloDetalle = sistema.buscarVuelo(aerolineaSel, rutaSel, vueloSel);
	        // Obtener paquetes vigentes del cliente si está logueado y hay vuelo seleccionado
	        if (usuario != null) {
	            String nicknameCliente = usuario instanceof Logica.DataCliente ? ((Logica.DataCliente) usuario).getNickname() : null;
	            if (nicknameCliente != null) {
	                List<Logica.DataPaquete> paquetesDisponibles = sistema.listarPaquetesDisponiblesParaCompra();
	                // Filtrar paquetes que tengan disponibilidad para la ruta y cantidad de pasajes
	                String nombreRuta = rutaSel;
	                int cantidadPasajes = 1;
	                try {
	                    cantidadPasajes = Integer.parseInt(request.getParameter("cantidadPasajes"));
	                } catch (Exception e) {
	                	throw new ServletException("Parámetro cantidadPasajes inválido.");
	                }
	                // DEBUG: Mostrar rutas incluidas en cada paquete y la ruta seleccionada
	                for (Logica.DataPaquete p : paquetesDisponibles) {
	                    System.out.println("[DEBUG] Paquete: " + p.getNombre() + ", rutasIncluidas: " + p.getRutasIncluidas() + ", nombreRuta: " + nombreRuta);
	                }
	                List<Logica.DataPaquete> paquetesFiltrados = paquetesDisponibles.stream()
    .filter(p -> p.getCantRutas() > 0)
    .filter(p -> p.getRutasIncluidas().stream()
        .anyMatch(ruta -> ruta.trim().equalsIgnoreCase(nombreRuta.trim())))
    .toList();
	                request.setAttribute("paquetesDisponibles", paquetesFiltrados);
	            }
	        }
	    }
	    request.setAttribute("vueloDetalle", vueloDetalle);
	    request.getRequestDispatcher("/WEB-INF/vuelo/reservaVuelo.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    Object usuario = session.getAttribute("usuario_logueado");
	    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
	    String aerolinea = request.getParameter("aerolinea");
	    String ruta = request.getParameter("ruta");
	    String vuelo = request.getParameter("vuelo");
	    String tipoAsientoStr = request.getParameter("tipoAsiento");
	    int cantidadPasajes = 1;
	    int equipajeExtra = 0;
	    try {
	        cantidadPasajes = Integer.parseInt(request.getParameter("cantidadPasajes"));
	    } catch (Exception e) {
	    	throw new ServletException("Parámetro cantidadPasajes inválido.");
	    }
	    try {
	        equipajeExtra = Integer.parseInt(request.getParameter("equipajeExtra"));
	    } catch (Exception e) {
	    	throw new ServletException("Parámetro equipajeExtra inválido.");
	    }
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
	    List<Logica.DataPasaje> pasajes = new java.util.ArrayList<>();
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