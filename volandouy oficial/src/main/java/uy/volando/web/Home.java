package uy.volando.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import uy.volando.model.EstadoSesion;
import Logica.ISistema;
import Logica.Sistema;
/**
 * Servlet implementation class Home
 */

@WebServlet ("/home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ISistema sistema;	   
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Home() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * inicializa la sesión si no estaba creada 
	 * @param request 
	 */
	
	public static void initSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("estado_sesion") == null) {
			session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
		}
	}
	
	/**
	 * Devuelve el estado de la sesión
	 * @param request
	 * @return 
	 */
	public static EstadoSesion getEstado(HttpServletRequest request) {
		return (EstadoSesion) request.getSession().getAttribute("estado_sesion");
	}
	
	private void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("[HOME] Entrando a processRequest");
		initSession(req);
		EstadoSesion estado = getEstado(req);
		System.out.println("[HOME] Estado de sesión: " + estado);
		switch(estado){
			case NO_LOGIN:
				System.out.println("[HOME] Estado NO_LOGIN, mostrando iniciar.jsp");
				// hace que se ejecute el jsp sin cambiar la url
				req.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").
						forward(req, resp);
				break;
			case LOGIN_INCORRECTO:
				System.out.println("[HOME] Estado LOGIN_INCORRECTO, mostrando iniciar.jsp con error");
				req.setAttribute("error", "Login o contraseña incorrectos");
				req.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").
						forward(req, resp);
				break;
			case LOGIN_CORRECTO:
				System.out.println("[HOME] Estado LOGIN_CORRECTO, mostrando iniciado.jsp");
				// hace que se ejecute el jsp sin cambiar la url
				req.getRequestDispatcher("/WEB-INF/home/iniciado.jsp").
						forward(req, resp);
				break;
			default:
				System.out.println("[HOME] Estado inválido, enviando error 500");
				// debería ser imposible llegar acá
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
						"Estado de sesión inválido");
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		this.sistema = (ISistema) getServletContext().getAttribute("sistema");
	     if (this.sistema == null) {
	        throw new ServletException("El sistema no fue inicializado por InicioServlet.");
	    }
	}
}