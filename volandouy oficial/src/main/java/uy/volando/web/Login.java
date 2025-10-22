package uy.volando.web;

import Logica.DataUsuario;
import Logica.ISistema;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import uy.volando.model.EstadoSesion;

/**
 * Servlet implementation class Login.
 */
@WebServlet("/iniciar-sesion")
public class Login extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor of the servlet.
   */
  public Login() {
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
    String login = request.getParameter("login"); // nickname (por ahora)
    String password = request.getParameter("password");
    System.out.println("[DEBUG] login param: " + login);
    System.out.println("[DEBUG] password param: " + password);

    EstadoSesion nuevoEstado;
    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");

    System.out.println("[LOGIN] params -> nick=" + login + " , pass.len="
        + (password != null ? password.length() : null));

    DataUsuario dto = null;
    try {
      System.out.println("[DEBUG] Llamando a loguearUsuario");
      dto = sistema.loguearUsuario(login, password); // nickname + plano
      System.out.println("[DEBUG] Resultado loguearUsuario: " + (dto != null ? "OK" : "NULL"));
    } catch (Exception e) {
      System.out.println("[ERROR] Excepción en loguearUsuario: " + e.getMessage());
      e.printStackTrace();
      objSesion.setAttribute("estado_sesion", EstadoSesion.LOGIN_INCORRECTO);
      request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
      return;
    }

    if (dto == null) {
      System.out.println("[DEBUG] Login incorrecto, dto es null");
      nuevoEstado = EstadoSesion.LOGIN_INCORRECTO;
      objSesion.setAttribute("estado_sesion", nuevoEstado);
      request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
      return;
    }

    System.out.println("[DEBUG] Login correcto, redirigiendo a /home");
    nuevoEstado = EstadoSesion.LOGIN_CORRECTO;
    objSesion.setAttribute("usuario_logueado", dto);
    objSesion.setAttribute("estado_sesion", nuevoEstado);
    String rol = null; // null = visitante
    if (dto instanceof Logica.DataCliente) {
      rol = "Cliente";
    } else if (dto instanceof Logica.DataAerolinea) {
      rol = "Aerolínea";
    }
    objSesion.setAttribute("rol", rol);
    response.sendRedirect(request.getContextPath() + "/home");
  }

  /**
   * Consults the login page.
   * 
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Solo mostrar el formulario de login, no procesar login
    System.out.println("[LOGIN] doGet: mostrando login.jsp");
    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/home/login.jsp");
    dispatcher.forward(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   * 
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("[LOGIN] Entrando a doPost");
    processRequest(request, response);
  }
}