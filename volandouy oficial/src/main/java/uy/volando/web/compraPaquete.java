package uy.volando.web;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import Logica.DataCliente;
import BD.PaqueteService;
import BD.UsuarioService;
import Logica.Paquete;

@WebServlet("/compraPaquete")
public class compraPaquete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public compraPaquete() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    Object usuario = session.getAttribute("usuario_logueado");
	    request.setAttribute("usuario", usuario);
		// Obtener la lista de paquetes disponibles desde el servicio
	    PaqueteService paqueteService = new PaqueteService();
	    List<Paquete> paquetesDisponibles = paqueteService.listarPaquetes();
	    request.setAttribute("paquetesDisponibles", paquetesDisponibles);

	    // Redirigir al JSP para mostrar los paquetes
	    request.getRequestDispatcher("/WEB-INF/paquete/compraPaquete.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String nombrePaquete = request.getParameter("paqueteId");
	    HttpSession session = request.getSession();
	    Object usuarioObj = session.getAttribute("usuario_logueado");
	    request.setAttribute("usuario", usuarioObj);
	    if (usuarioObj == null) {
	        request.setAttribute("mensaje", "Debes iniciar sesión para comprar un paquete.");
	        doGet(request, response);
	        return;
	    }
	    DataCliente dataCliente = (DataCliente) usuarioObj;
	    String nickname = dataCliente.getNickname();

	    PaqueteService paqueteService = new PaqueteService();
	    Paquete paquete = paqueteService.existePaquete(nombrePaquete);
	    if (paquete == null) {
	        request.setAttribute("mensaje", "El paquete seleccionado no existe.");
	        doGet(request, response);
	        return;
	    }
	    UsuarioService usuarioService = new UsuarioService();
	    boolean yaComprado = usuarioService.clienteYaComproPaquete(nickname, nombrePaquete);
	    if (yaComprado) {
	        request.setAttribute("mensaje", "Ya has comprado este paquete. Elige otro o cancela.");
	        doGet(request, response);
	        return;
	    }
	    Logica.Cliente cliente = usuarioService.obtenerClientePorNickname(nickname);
	    java.util.Date fechaCompra = new java.util.Date();
	    java.util.Calendar cal = java.util.Calendar.getInstance();
	    cal.setTime(fechaCompra);
	    cal.add(java.util.Calendar.DATE, paquete.getValidez());
	    java.util.Date fechaVencimiento = cal.getTime();
	    Logica.CompraPaquete compra = new Logica.CompraPaquete(
	        cliente,
	        paquete,
	        fechaCompra,
	        fechaVencimiento,
	        paquete.getCosto()
	    );
	    paqueteService.comprarPaquete(compra, cliente, paquete);
	    request.setAttribute("mensaje", "¡Compra realizada con éxito!");
	    doGet(request, response);
	}

}