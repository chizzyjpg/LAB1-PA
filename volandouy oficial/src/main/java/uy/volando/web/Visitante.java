package uy.volando.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet ("/visitante")
public class Visitante extends HttpServlet {
	private static final long serialVersionUID = 1L;

		   
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Visitante() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * inicializa la sesión si no estaba creada 
	 * @param request 
	 */
	
	private void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/home/iniciado.jsp").forward(req, resp);
		}
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
	
