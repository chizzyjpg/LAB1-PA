package uy.volando.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import uy.volando.model.EstadoSesion;

@WebServlet ("/perfil")
public class Perfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	   
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Perfil() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static void initSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("estado_sesion") == null) {
			session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
		}
	}
	
	public static EstadoSesion getEstado(HttpServletRequest request) {
		return (EstadoSesion) request.getSession().getAttribute("estado_sesion");
	}
	
	private void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("[PERFIL] Entrando a processRequest");
		initSession(req);
		EstadoSesion estado = getEstado(req);
		System.out.println("[PERFIL] Estado de sesión: " + estado);
		switch(estado){
			case NO_LOGIN:
				System.out.println("[PERFIL] Estado NO_LOGIN, mostrando iniciar.jsp");
				req.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").
				forward(req, resp);
				break;
			case LOGIN_CORRECTO:
				System.out.println("[PERFIL] Estado LOGIN_CORRECTO, mostrando perfil_cliente.jsp");
				Object usuario = req.getSession().getAttribute("usuario_logueado");
				if (usuario != null) {
					req.setAttribute("usuario", usuario);
				}
				req.getRequestDispatcher("/WEB-INF/perfil/perfil.jsp").forward(req, resp);
				break;
			/*case LOGIN_ADMIN:
				System.out.println("[PERFIL] Estado LOGIN_ADMIN, mostrando perfil_admin.jsp");
				req.getRequestDispatcher("/WEB-INF/perfil/perfil_admin.jsp").
				forward(req, resp);
				break;*/
			default:
				System.out.println("[PERFIL] Estado desconocido, mostrando iniciar.jsp");
				req.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").
				forward(req, resp);
				break;
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		processRequest(request, response);
	}
}