package uy.volando.web;


import Logica.DataAerolinea;
import Logica.DataRuta;
import Logica.DataVueloEspecifico;
import Logica.ISistema;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet para el alta de vuelos por parte de una aerolínea.
 */
@WebServlet("/altaVuelo")
public class altaVuelo extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor del servlet.
   */
  public altaVuelo() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    request.setAttribute("usuario", usuario);

    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
    if (usuario == null || !(usuario instanceof DataAerolinea)) {
      request.setAttribute("errorMsg",
          "Debes iniciar sesión como aerolínea para registrar un vuelo.");
      request.getRequestDispatcher("/WEB-INF/vuelo/altaVuelo.jsp").forward(request, response);
      return;
    }
    DataAerolinea aerolinea = (DataAerolinea) usuario;
    String aerolineaId = aerolinea.getNickname();
    request.setAttribute("aerolineaSeleccionada", aerolineaId);
    request.setAttribute("aerolineaNombre", aerolinea.getNombre());

    List<DataRuta> rutas = sistema.listarPorAerolinea(aerolineaId);
    request.setAttribute("rutas", rutas);
    request.getRequestDispatcher("/WEB-INF/vuelo/altaVuelo.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    request.setAttribute("usuario", usuario);

    if (usuario == null || !(usuario instanceof DataAerolinea)) {
      request.setAttribute("errorMsg",
          "Debes iniciar sesión como aerolínea para registrar un vuelo.");
      doGet(request, response);
      return;
    }
    DataAerolinea aerolinea = (DataAerolinea) usuario;
    String aerolineaId = aerolinea.getNickname();

    String rutaId = request.getParameter("rutaId");
    String nombreVuelo = request.getParameter("nombreVuelo");
    String fechaStr = request.getParameter("fecha");
    String duracionStr = request.getParameter("duracion");
    String cantMaxTuristasStr = request.getParameter("cantMaxTuristas");
    String cantMaxEjecutivosStr = request.getParameter("cantMaxEjecutivos");
    // String imagenVuelo = request.getParameter("imagenVuelo");

    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
    if (sistema == null) {
      request.setAttribute("errorMsg", "Error interno: sistema no disponible.");
      doGet(request, response);
      return;
    }

    // Obtener el nombre de la ruta a partir del id
    List<DataRuta> rutas = sistema.listarPorAerolinea(aerolineaId);
    String nombreRuta = rutas.stream().filter(r -> Integer.toString(r.getIdRuta()).equals(rutaId))
        .map(DataRuta::getNombre).findFirst().orElse(null);
    if (nombreRuta == null) {
      request.setAttribute("errorMsg", "Ruta seleccionada no válida.");
      doGet(request, response);
      return;
    }

    // Validar nombre único en la ruta de la aerolínea
    List<DataVueloEspecifico> vuelos = sistema.listarVuelos(aerolineaId, nombreRuta);
    boolean existeVuelo = vuelos.stream()
        .anyMatch(v -> v.getNombre().equalsIgnoreCase(nombreVuelo));
    if (existeVuelo) {
      request.setAttribute("errorMsg",
          "Ya existe un vuelo con ese nombre en la ruta seleccionada.");
      doGet(request, response);
      return;
    }

    // Convertir tipos
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.util.Date fecha = null;
    try {
      fecha = sdf.parse(fechaStr);
    } catch (Exception e) {
      request.setAttribute("errorMsg", "Fecha inválida.");
      doGet(request, response);
      return;
    }
    int duracion;
    int cantMaxTuristas;
    int cantMaxEjecutivos;
    try {
      duracion = Integer.parseInt(duracionStr);
      cantMaxTuristas = Integer.parseInt(cantMaxTuristasStr);
      cantMaxEjecutivos = Integer.parseInt(cantMaxEjecutivosStr);
    } catch (Exception e) {
      request.setAttribute("errorMsg", "Duración o cantidad inválida.");
      doGet(request, response);
      return;
    }

    java.util.Date fechaAlta = new java.util.Date();
    // Crear DTO de vuelo
    DataVueloEspecifico dataVuelo = new DataVueloEspecifico(nombreVuelo, fecha, duracion,
        cantMaxTuristas, cantMaxEjecutivos, fechaAlta);

    try {
      sistema.registrarVuelo(aerolineaId, rutaId, dataVuelo);
      request.setAttribute("successMsg", "Vuelo registrado exitosamente.");
    } catch (Exception e) {
      request.setAttribute("errorMsg", e.getMessage());
    }
    doGet(request, response);
  }
}