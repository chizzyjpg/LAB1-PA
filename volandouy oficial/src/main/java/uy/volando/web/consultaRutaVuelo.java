package uy.volando.web;


import Logica.DataAerolinea;
import Logica.DataCategoria;
import Logica.DataRuta;
import Logica.DataVueloEspecifico;
import Logica.ISistema;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class consultaRutaVuelo.
 */
@WebServlet("/consultaRutaVuelo")
public class consultaRutaVuelo extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   */
  public consultaRutaVuelo() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
    List<DataAerolinea> aerolineas = sistema.listarAerolineas();
    List<DataCategoria> categorias = sistema.listarCategorias();
    request.setAttribute("aerolineas", aerolineas);
    request.setAttribute("categorias", categorias);

    String aerolineaSel = request.getParameter("aerolinea");
    System.out.println("[DEBUG] Valor de aerolineaSel: " + aerolineaSel);
    List<DataRuta> rutas = null;
    if (aerolineaSel != null && !aerolineaSel.isEmpty()) {
      rutas = sistema.listarPorAerolinea(aerolineaSel).stream()
          .filter(r -> r.getEstado() != null && r.getEstado().name().equalsIgnoreCase("CONFIRMADA"))
          .toList();
    }
    request.setAttribute("rutas", rutas);

    request.getRequestDispatcher("/WEB-INF/vuelo/consultaRutaVuelo.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
    String aerolineaSel = request.getParameter("aerolinea");
    System.out.println("[DEBUG] doPost - aerolineaSel inicial: " + aerolineaSel);
    if (aerolineaSel != null) {
      aerolineaSel = aerolineaSel.trim();
    }
    String categoriaSel = request.getParameter("categoria");
    String rutaSel = request.getParameter("ruta");

    List<DataAerolinea> aerolineas = sistema.listarAerolineas();
    List<DataCategoria> categorias = sistema.listarCategorias();
    request.setAttribute("aerolineas", aerolineas);
    request.setAttribute("categorias", categorias);

    List<DataRuta> rutas = null;
    if (aerolineaSel != null && !aerolineaSel.isEmpty()) {
      rutas = sistema.listarPorAerolinea(aerolineaSel).stream()
          .filter(r -> r.getEstado() != null && r.getEstado().name().equalsIgnoreCase("CONFIRMADA"))
          .toList();
    }
    request.setAttribute("rutas", rutas);

    if (aerolineaSel == null || aerolineaSel.isEmpty()) {
      request.setAttribute("errorMsg", "Debe seleccionar una aerolínea.");
    }

    // Validar que el nickname no se pierda antes de consultar vuelos
    System.out.println("[DEBUG] doPost - aerolineaSel antes de consultar vuelos: " + aerolineaSel);
    if (rutaSel != null && !rutaSel.isEmpty() && rutas != null) {
      DataRuta ruta = rutas.stream().filter(r -> r.getNombre().equals(rutaSel)).findFirst()
          .orElse(null);
      if (ruta != null) {
        System.out
            .println("[DEBUG] doPost - nickname para listarVuelos: " + ruta.getNicknameAerolinea());
        List<DataVueloEspecifico> vuelos = sistema.listarVuelos(ruta.getNicknameAerolinea(),
            ruta.getNombre());
        request.setAttribute("rutaSeleccionada", ruta);
        request.setAttribute("vuelos", vuelos);
      }
    }
    request.getRequestDispatcher("/WEB-INF/vuelo/consultaRutaVuelo.jsp").forward(request, response);
  }
}