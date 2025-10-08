package uy.volando.web;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import BD.UsuarioService;
import Logica.Usuario;

import uy.volando.exceptions.UsuarioNoEncontrado;
import uy.volando.model.EstadoSesion;

@WebServlet("/iniciar-sesion")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession objSesion = request.getSession();
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		EstadoSesion nuevoEstado;

		DataUsuario usr = Sistema.loguearUsuario(login, password);
		
		System.out.println("Login: " + login + " Pass: " + password);
		
		if (usr == null) {
			nuevoEstado = EstadoSesion.LOGIN_INCORRECTO;
		} else {
			nuevoEstado = EstadoSesion.LOGIN_CORRECTO;
			// setea el usuario logueado
			request.getSession().setAttribute("usuario_logueado", usr);
		}
		
		objSesion.setAttribute("estado_sesion", nuevoEstado);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/home");
		dispatcher.forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    // 🔸 Simplemente redirigir a login.jsp
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/home/login.jsp");
	    dispatcher.forward(request, response);
	}
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);	
	}		
}