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

@WebServlet ("/consultaVuelo")
public class consultaVuelo extends HttpServlet {
	public static final long serialVersionUID = 1L;
	//private final Gson gson = new Gson();

	public consultaVuelo() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
		String accion = req.getParameter("accion");
		
		// Obtener usuario desde la sesión
		HttpSession session = req.getSession();
		Object usuario = session.getAttribute("usuario_logueado");
		req.setAttribute("usuario", usuario);

		// Modo tradicional: no JSON, no AJAX
		if (accion == null || accion.isBlank()) {
			// 1. Cargar aerolíneas
			List<DataAerolinea> aerolineas = sistema.listarAerolineas();
			req.setAttribute("aerolineas", aerolineas);

			// 2. Si se seleccionó aerolínea, cargar rutas confirmadas
			String nicknameAerolinea = req.getParameter("fltAerolinea");
			if (nicknameAerolinea != null && !nicknameAerolinea.isBlank()) {
				List<DataRuta> rutas = sistema.listarPorAerolinea(nicknameAerolinea);
				List<DataRuta> rutasConfirmadas = rutas.stream()
					.filter(r -> r.getEstado() == EstadoRuta.CONFIRMADA)
					.toList();
				req.setAttribute("rutas", rutasConfirmadas);
			}

			// 3. Si se seleccionó ruta, cargar vuelos
			String nombreRuta = req.getParameter("fltRuta");
			if (nicknameAerolinea != null && !nicknameAerolinea.isBlank() && nombreRuta != null && !nombreRuta.isBlank()) {
				List<DataVueloEspecifico> vuelos = sistema.listarVuelos(nicknameAerolinea, nombreRuta);
				req.setAttribute("vuelos", vuelos);
			}

			// 4. Si se seleccionó vuelo, mostrar detalle y reservas según usuario
			String codigoVuelo = req.getParameter("codigoVuelo");
			if (nicknameAerolinea != null && !nicknameAerolinea.isBlank() && nombreRuta != null && !nombreRuta.isBlank() && codigoVuelo != null && !codigoVuelo.isBlank()) {
				DataVueloEspecifico vuelo = sistema.buscarVuelo(nicknameAerolinea, nombreRuta, codigoVuelo);
				req.setAttribute("detalleVuelo", vuelo);
				System.out.println("[consultaVuelo] Detalle vuelo: " + (vuelo != null ? vuelo.getNombre() : "null"));

			
				System.out.println("[consultaVuelo] session: " + session);
				System.out.println("[consultaVuelo] usuarioObj: " + usuario);
				if (usuario != null) {
					String usuarioNickname = null;
					boolean esAerolinea = false;
					boolean esCliente = false;
					if (usuario instanceof DataAerolinea) {
						usuarioNickname = ((DataAerolinea) usuario).getNickname();
						esAerolinea = true;
					} else if (usuario instanceof DataCliente) {
						usuarioNickname = ((DataCliente) usuario).getNickname();
						esCliente = true;
					}

					System.out.println("[consultaVuelo] Usuario logueado: " + usuario);
					System.out.println("[consultaVuelo] Nickname usuario: " + usuarioNickname);
					System.out.println("[consultaVuelo] esAerolinea: " + esAerolinea + ", esCliente: " + esCliente);
					System.out.println("[consultaVuelo] Nickname aerolínea de la ruta: " + (vuelo.getDRuta() != null ? vuelo.getDRuta().getNicknameAerolinea() : "null"));
					// Si es aerolínea dueña de la ruta, mostrar todas las reservas
					if (esAerolinea && vuelo.getDRuta() != null && usuarioNickname != null && usuarioNickname.equals(vuelo.getDRuta().getNicknameAerolinea())) {
						List<DataReserva> reservas = sistema.listarReservas(nicknameAerolinea, nombreRuta, codigoVuelo);
						System.out.println("[consultaVuelo] Aerolínea dueña, reservasVuelo size: " + (reservas != null ? reservas.size() : "null"));
						req.setAttribute("reservasVuelo", reservas);
					}
					// Si es cliente con reserva en el vuelo, mostrar solo su reserva
					else if (esCliente && usuarioNickname != null) {
						List<DataReserva> reservas = sistema.listarReservas(nicknameAerolinea, nombreRuta, codigoVuelo);
						System.out.println("[consultaVuelo] Cliente, reservas size: " + (reservas != null ? reservas.size() : "null"));
						for (DataReserva r : reservas) {
							if (r.getNickCliente() != null && usuarioNickname.equals(r.getNickCliente().getNickname())) {
								System.out.println("[consultaVuelo] Reserva encontrada para cliente: " + usuarioNickname);
								req.setAttribute("reservaCliente", r);
								break;
							}
						}
					}
				}
				// Si se seleccionó una reserva, mostrar detalle
				String idReserva = req.getParameter("idReserva");
				if (idReserva != null && !idReserva.isBlank()) {
					try {
						int idRes = Integer.parseInt(idReserva);
						DataReserva detalleReserva = sistema.buscarReserva(nicknameAerolinea, nombreRuta, codigoVuelo, idRes);
						System.out.println("[consultaVuelo] Detalle reserva id: " + idRes + " encontrada: " + (detalleReserva != null));
						req.setAttribute("detalleReserva", detalleReserva);
					} catch (NumberFormatException e) {
						System.out.println("[consultaVuelo] idReserva inválido: " + idReserva);
					}
				}
			}

			// Renderizar JSP
			req.getRequestDispatcher("/WEB-INF/vuelo/consultaVuelo.jsp").forward(req, resp);
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// No se usa en este caso de uso
		resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
}