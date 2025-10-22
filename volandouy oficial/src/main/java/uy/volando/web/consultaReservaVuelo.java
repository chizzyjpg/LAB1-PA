package uy.volando.web; 

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import Logica.*;

@WebServlet ("/consultaReservaVuelo")
public class consultaReservaVuelo extends HttpServlet{
	public static final long serialVersionUID = 1L;
	
	public consultaReservaVuelo() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object usuario = session.getAttribute("usuario_logueado");
		request.setAttribute("usuario", usuario);
		if (usuario == null) {
			response.sendRedirect("iniciar.jsp");
			return;
		}
		ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");

		String rol = (usuario instanceof DataAerolinea) ? "aerolinea" : "cliente";
		request.setAttribute("rol", rol);

		String aerolineaSel = request.getParameter("aerolinea");
		String rutaSel = request.getParameter("ruta");
		String vueloSel = request.getParameter("vuelo");
		String reservaSel = request.getParameter("reserva");

		String nickAerolinea = rol.equals("aerolinea") ? ((DataUsuario)usuario).getNickname() : aerolineaSel;

		// Si no hay aerolínea seleccionada (cliente), mostrar selección de aerolínea
		if (rol.equals("cliente") && aerolineaSel == null) {
			List<DataAerolinea> aerolineas = sistema.listarAerolineas();
			request.setAttribute("aerolineas", aerolineas);
			request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request, response);
			return;
		}

		// Mostrar selección de ruta y vuelo en la misma pantalla
		List<DataRuta> rutas = sistema.listarPorAerolinea(nickAerolinea);
		request.setAttribute("aerolineaSeleccionada", nickAerolinea);
		request.setAttribute("rutas", rutas);

		List<DataVueloEspecifico> vuelos = null;
		if (rutaSel != null && !rutaSel.isEmpty()) {
			vuelos = sistema.listarVuelos(nickAerolinea, rutaSel);
			request.setAttribute("rutaSeleccionada", rutaSel);
			request.setAttribute("vuelos", vuelos);
		}

		// Si ya se seleccionó vuelo, continuar con el flujo normal
		if (vueloSel != null && !vueloSel.isEmpty()) {
			if (reservaSel != null) {
				try {
					int idReserva = Integer.parseInt(reservaSel);
					DataReserva detalle = sistema.buscarReserva(nickAerolinea, rutaSel, vueloSel, idReserva);
					request.setAttribute("detalleReserva", detalle);
				} catch (Exception e) {
					request.setAttribute("error", "No se pudo obtener el detalle de la reserva: " + e.getMessage());
				}
				request.setAttribute("vueloSeleccionado", vueloSel);
				request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request, response);
				return;
			}

			if (rol.equals("aerolinea")) {
				List<DataReserva> reservas = sistema.listarReservas(nickAerolinea, rutaSel, vueloSel);
				request.setAttribute("vueloSeleccionado", vueloSel);
				request.setAttribute("reservas", reservas);
				request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request, response);
				return;
			} else {
				List<DataReserva> reservas = sistema.listarReservas(nickAerolinea, rutaSel, vueloSel);
				DataReserva miReserva = null;
				for (DataReserva r : reservas) {
					if (r.getNickCliente().getNickname().equals(((DataUsuario)usuario).getNickname())) {
						miReserva = r;
						break;
					}
				}
				request.setAttribute("vueloSeleccionado", vueloSel);
				request.setAttribute("miReserva", miReserva);
				// Asegurarse de que siempre se setea el atributo, aunque la lista esté vacía
				request.setAttribute("reservasCliente", reservas);
				request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request, response);
				return;
			}
		}

		// Si no se seleccionó vuelo, mostrar solo selección de ruta y vuelo
		request.getRequestDispatcher("/WEB-INF/vuelo/consultaReservaVuelo.jsp").forward(request, response);
	}
}