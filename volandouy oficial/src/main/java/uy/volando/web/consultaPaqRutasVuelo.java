package uy.volando.web;


import Logica.DataAerolinea;
import Logica.DataPaquete;
import Logica.DataRuta;
import Logica.ISistema;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class consultaPaqRutasVuelo.
 */
@WebServlet("/consultaPaqRutasVuelo")
public class consultaPaqRutasVuelo extends HttpServlet {
  public static final long serialVersionUID = 1L;

  public consultaPaqRutasVuelo() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
    String nombrePaquete = request.getParameter("paquete");
    String nombreRuta = request.getParameter("ruta");

    if (nombrePaquete == null || nombrePaquete.isEmpty()) {
      // Mostrar lista de paquetes
      List<DataPaquete> paquetes = sistema.listarPaquetes();
      request.setAttribute("paquetes", paquetes);
      request.setAttribute("view", "list");
    } else {
      // Mostrar detalles del paquete
      DataPaquete paquete = sistema.verPaquete(nombrePaquete);
      request.setAttribute("paquete", paquete);
      request.setAttribute("view", "detail");
      // Mostrar rutas del paquete (usando nombres)
      java.util.Set<String> nombresRutas = paquete.getRutasIncluidas();
      request.setAttribute("nombresRutas", nombresRutas);
      // Si se seleccionó una ruta, buscar el objeto DataRuta recorriendo todas las
      // aerolíneas
      if (nombreRuta != null && !nombreRuta.isEmpty()) {
        DataRuta rutaSeleccionada = null;
        String nombreRutaTrim = nombreRuta.trim();
        for (DataAerolinea aerolinea : sistema.listarAerolineas()) {
          for (DataRuta r : sistema.listarPorAerolinea(aerolinea.getNickname())) {
            if (r.getNombre() != null && r.getNombre().trim().equalsIgnoreCase(nombreRutaTrim)) {
              rutaSeleccionada = r;
              break;
            }
          }
          if (rutaSeleccionada != null)
            break;
        }
        request.setAttribute("ruta", rutaSeleccionada);
      }
    }
    // Forward a la JSP
    RequestDispatcher dispatcher = request
        .getRequestDispatcher("/WEB-INF/paquete/consultaPaqRutasVuelo.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }
}