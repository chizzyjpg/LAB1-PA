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

import Logica.DataCliente;
import Logica.DataCompraPaquete;
import Logica.ISistema;

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
	    // Obtener la instancia de ISistema desde el contexto
	    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
	    // Obtener la lista de paquetes disponibles desde ISistema
	    List<Logica.DataPaquete> paquetesDisponibles = sistema.listarPaquetes();
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
	    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
	    boolean existePaquete = sistema.existePaquete(nombrePaquete);
	    if (!existePaquete) {
	        request.setAttribute("mensaje", "El paquete seleccionado no existe.");
	        doGet(request, response);
	        return;
	    }
	    boolean yaComprado = sistema.clienteYaComproPaquete(nickname, nombrePaquete);
	    if (yaComprado) {
	        request.setAttribute("mensaje", "Ya has comprado este paquete. Elige otro o cancela.");
	        doGet(request, response);
	        return;
	    }
	    // Obtener el costo del paquete desde ISistema
	    BigDecimal costo = null;
	    Date fechaCompra = new Date();
	    Date vencimiento = null;
	    DataCompraPaquete compra = new DataCompraPaquete(nombrePaquete, nickname, fechaCompra, costo, vencimiento);
	    sistema.comprarPaquete(compra);
	    request.setAttribute("mensaje", "¡Compra realizada con éxito!");
	    doGet(request, response);
	}

}