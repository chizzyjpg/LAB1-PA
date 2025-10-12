package uy.volando.web; 

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import BD.AerolineaService;
import Logica.Aerolinea;
import Logica.DataAerolinea;
import Logica.DataRuta;
import BD.RutaVueloService;
import Logica.Ruta;
import BD.VueloService;

@WebServlet ("/altaVuelo")
public class altaVuelo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public altaVuelo() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    Object usuario = session.getAttribute("usuario_logueado");
	    request.setAttribute("usuario", usuario);

		AerolineaService aerolineaService = new AerolineaService();
		RutaVueloService rutaVueloService = new RutaVueloService();
		List<DataAerolinea> aerolineas = aerolineaService.listarAerolineas();
		request.setAttribute("aerolineas", aerolineas);

		String aerolineaId = request.getParameter("aerolineaId");
		List<DataRuta> rutas;
		if (aerolineaId != null && !aerolineaId.isEmpty()) {
			rutas = rutaVueloService.listarRutasPorAerolinea(aerolineaId);
			request.setAttribute("aerolineaSeleccionada", aerolineaId);
		} else {
			rutas = rutaVueloService.listarRutas();
			request.setAttribute("aerolineaSeleccionada", "");
		}
		request.setAttribute("rutas", rutas);
		request.getRequestDispatcher("/WEB-INF/vuelo/altaVuelo.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    Object usuario = session.getAttribute("usuario_logueado");
	    request.setAttribute("usuario", usuario);

	    String aerolineaId = request.getParameter("aerolineaId");
	    String rutaId = request.getParameter("rutaId");
	    String nombreVuelo = request.getParameter("nombreVuelo");
	    String fechaStr = request.getParameter("fecha");
	    String duracionStr = request.getParameter("duracion");
	    String cantMaxTuristasStr = request.getParameter("cantMaxTuristas");
	    String cantMaxEjecutivosStr = request.getParameter("cantMaxEjecutivos");

	    VueloService vueloService = new VueloService();
	    RutaVueloService rutaVueloService = new RutaVueloService();

	    // Validar nombre único
	    if (vueloService.existeVueloConNombre(nombreVuelo)) {
	        request.setAttribute("errorMsg", "Ya existe un vuelo con ese nombre. Por favor, elija otro nombre.");
	        doGet(request, response);
	        return;
	    }

	    // Convertir tipos
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
	    java.util.Date fecha = null;
	    try {
	        fecha = sdf.parse(fechaStr);
	    } catch (Exception e) {
	        request.setAttribute("errorMsg", "Fecha inválida.");
	        doGet(request, response);
	        return;
	    }
	    int duracion = Integer.parseInt(duracionStr);
	    int cantMaxTuristas = Integer.parseInt(cantMaxTuristasStr);
	    int cantMaxEjecutivos = Integer.parseInt(cantMaxEjecutivosStr);

	    // Buscar la entidad Ruta
	    Ruta ruta = null;
	    try {
	        int idRuta = Integer.parseInt(rutaId);
	        ruta = rutaVueloService.buscarRutaPorId(idRuta);
	    } catch (Exception e) {
	        request.setAttribute("errorMsg", "Ruta de vuelo inválida.");
	        doGet(request, response);
	        return;
	    }
	    if (ruta == null) {
	        request.setAttribute("errorMsg", "Ruta de vuelo no encontrada.");
	        doGet(request, response);
	        return;
	    }

	    // Crear el VueloEspecifico
	    java.util.Date fechaAlta = new java.util.Date();
	    Logica.VueloEspecifico vuelo = new Logica.VueloEspecifico(
	        nombreVuelo, fecha, duracion, cantMaxTuristas, cantMaxEjecutivos, fechaAlta, ruta
	    );

	    // Registrar el vuelo
	    vueloService.registrarVuelo(vuelo);
	    request.setAttribute("successMsg", "Vuelo registrado exitosamente.");
	    doGet(request, response);
	}
}