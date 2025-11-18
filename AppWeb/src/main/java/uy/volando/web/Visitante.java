package uy.volando.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.volando.model.EstadoSesion;
import java.io.IOException;

/**
 * Servlet implementation class Visitante.
 */
@WebServlet("/visitante")
public class Visitante extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor of the object.
   */
  public Visitante() {
    super();
  }

  /**
   * inicializa la sesión si no estaba creada.
   * 
   */
  private void processRequest(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    HttpSession objSesion = req.getSession();
    DeviceUtils.DeviceType deviceType = (DeviceUtils.DeviceType) objSesion.getAttribute("deviceType");

    if (deviceType == DeviceUtils.DeviceType.MOBILE) {
        objSesion.setAttribute("estado_sesion", EstadoSesion.LOGIN_INCORRECTO);
        resp.sendRedirect(req.getContextPath() + "/home");
    }
    else {
        req.getRequestDispatcher("/WEB-INF/home/iniciado.jsp").forward(req, resp);
    }
  }
  /**
   * Configura la sesión del visitante.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Consfigura la sesión del visitante.
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

}
