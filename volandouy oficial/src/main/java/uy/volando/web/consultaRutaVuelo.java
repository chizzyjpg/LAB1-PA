package uy.volando.web; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import Logica.DataAerolinea;
import Logica.DataCategoria;
import Logica.DataRuta;
import Logica.DataVueloEspecifico;
import Logica.ISistema;

@WebServlet ("/consultaRutaVuelo")
public class consultaRutaVuelo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public consultaRutaVuelo() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
		List<DataAerolinea> aerolineas = sistema.listarAerolineas();
		List<DataCategoria> categorias = sistema.listarCategorias();
		request.setAttribute("aerolineas", aerolineas);
		request.setAttribute("categorias", categorias);

		String aerolineaSel = request.getParameter("aerolinea");
		String categoriaSel = request.getParameter("categoria");
		if (aerolineaSel != null) aerolineaSel = aerolineaSel.trim();
		if (categoriaSel != null) categoriaSel = categoriaSel.trim();
		System.out.println("[DEBUG] doGet - aerolineaSel: " + aerolineaSel + " categoriaSel: " + categoriaSel);

		List<DataRuta> rutas = Collections.emptyList();
		try {
			if (aerolineaSel != null && !aerolineaSel.isEmpty()) {
				rutas = sistema.listarPorAerolinea(aerolineaSel).stream()
					.filter(r -> r.getEstado() != null && r.getEstado().name().equalsIgnoreCase("CONFIRMADA"))
					.toList();
			} else if (categoriaSel != null && !categoriaSel.isEmpty()) {
				// Buscar rutas en todas las aerolíneas que pertenezcan a la categoría seleccionada
				List<DataRuta> acumulador = new ArrayList<>();
				for (DataAerolinea a : aerolineas) {
					List<DataRuta> rutasA = sistema.listarPorAerolinea(a.getNickname());
					for (DataRuta r : rutasA) {
						if (r.getEstado() != null && r.getEstado().name().equalsIgnoreCase("CONFIRMADA")
							&& r.getCategoria() != null
							&& r.getCategoria().getNombre() != null
							&& r.getCategoria().getNombre().trim().equalsIgnoreCase(categoriaSel)) {
							acumulador.add(r);
							System.out.println("[DEBUG] doGet - matched route '" + r.getNombre() + "' for category '" + categoriaSel + "'");
						}
					}
				}
				rutas = acumulador;
			}
		} catch (Exception ex) {
			request.setAttribute("errorMsg", ex.getMessage());
			rutas = Collections.emptyList();
		}

		request.setAttribute("rutas", rutas);
		// Preserve selected values for the view
		request.setAttribute("aerolineaSel", aerolineaSel);
		request.setAttribute("categoriaSel", categoriaSel);

		request.getRequestDispatcher("/WEB-INF/vuelo/consultaRutaVuelo.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
		String aerolineaSel = request.getParameter("aerolinea");
		if (aerolineaSel != null) {
			aerolineaSel = aerolineaSel.trim();
		}
		String categoriaSel = request.getParameter("categoria");
		if (categoriaSel != null) {
			categoriaSel = categoriaSel.trim();
		}
		String rutaParam = request.getParameter("ruta");
		final String rutaSel = (rutaParam == null || rutaParam.isEmpty()) ? null : rutaParam.trim();

		List<DataAerolinea> aerolineas = sistema.listarAerolineas();
		List<DataCategoria> categorias = sistema.listarCategorias();
		request.setAttribute("aerolineas", aerolineas);
		request.setAttribute("categorias", categorias);

		List<DataRuta> rutas = Collections.emptyList();
		try {
			if (aerolineaSel != null && !aerolineaSel.isEmpty()) {
				rutas = sistema.listarPorAerolinea(aerolineaSel).stream()
					.filter(r -> r.getEstado() != null && r.getEstado().name().equalsIgnoreCase("CONFIRMADA"))
					.toList();
			} else if (categoriaSel != null && !categoriaSel.isEmpty()) {
				List<DataRuta> acumulador = new ArrayList<>();
				for (DataAerolinea a : aerolineas) {
					List<DataRuta> rutasA = sistema.listarPorAerolinea(a.getNickname());
					for (DataRuta r : rutasA) {
						if (r.getEstado() != null && r.getEstado().name().equalsIgnoreCase("CONFIRMADA")
							&& r.getCategoria() != null
							&& r.getCategoria().getNombre() != null
							&& r.getCategoria().getNombre().trim().equalsIgnoreCase(categoriaSel)) {
							acumulador.add(r);
							System.out.println("[DEBUG] doPost - matched route '" + r.getNombre() + "' for category '" + categoriaSel + "'");
						}
					}
				}
				rutas = acumulador;
			}
		} catch (Exception ex) {
			request.setAttribute("errorMsg", ex.getMessage());
			rutas = Collections.emptyList();
		}

		request.setAttribute("rutas", rutas);
		// Require at least an aerolinea or a category
		if ((aerolineaSel == null || aerolineaSel.isEmpty()) && (categoriaSel == null || categoriaSel.isEmpty())) {
			request.setAttribute("errorMsg", "Debe seleccionar una aerolínea o una categoría.");
		}

		// Preserve selected values for the view
		request.setAttribute("aerolineaSel", aerolineaSel);
		request.setAttribute("categoriaSel", categoriaSel);
		request.setAttribute("rutaSel", rutaSel);

		// If a route was selected, find it in the filtered list and load vuelos
		if (rutaSel != null && !rutaSel.isEmpty() && rutas != null && !rutas.isEmpty()) {
			DataRuta ruta = rutas.stream().filter(r -> r.getNombre() != null && r.getNombre().equalsIgnoreCase(rutaSel)).findFirst().orElse(null);
			if (ruta != null) {
				System.out.println("[DEBUG] doPost - nickname para listarVuelos: " + ruta.getNicknameAerolinea());
				List<DataVueloEspecifico> vuelos = sistema.listarVuelos(ruta.getNicknameAerolinea(), ruta.getNombre());
				request.setAttribute("rutaSeleccionada", ruta);
				request.setAttribute("vuelos", vuelos);
			}
		}

		request.getRequestDispatcher("/WEB-INF/vuelo/consultaRutaVuelo.jsp").forward(request, response);
	}
}