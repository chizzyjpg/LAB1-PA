package uy.volando.web;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class Logout.
 */
@WebServlet("/cerrar-sesion")
public class Logout extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor of the servlet.
   * 
   */
  public Logout() {
    super();
  }
  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
   * 
   */

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    System.out.println("[DEBUG] Entrando a processRequest");
    request.setCharacterEncoding("UTF-8");

    HttpSession objSesion = request.getSession();
    objSesion.invalidate();

    RequestDispatcher rd = request.getRequestDispatcher("/index.html");
    rd.forward(request, response);

  }

  /**
   * Consults the servlet for the HTTP <code>GET</code> method.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Consults the servlet for the HTTP <code>POST</code> method.
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }
}