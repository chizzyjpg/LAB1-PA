package uy.volando.web;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import uy.volando.publicar.WebServices;
import uy.volando.publicar.DataAerolinea;
import uy.volando.publicar.DataRuta;
import uy.volando.publicar.DataVueloEspecifico;


@WebServlet("/altaVuelo")
public class altaVuelo extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private WebServices port;

  @Override
  public void init() throws ServletException {
      super.init();
      this.port = (WebServices) getServletContext().getAttribute("volandoPort");
      if(this.port == null) {
          throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
      }
  }

  public altaVuelo() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    request.setAttribute("usuario", usuario);

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

    List<DataRuta> rutas = port.listarPorAerolinea(aerolineaId).getItem();
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

    if (port == null) {
      request.setAttribute("errorMsg", "Error interno: sistema no disponible.");
      doGet(request, response);
      return;
    }

    // Obtener el nombre de la ruta a partir del id
    List<DataRuta> rutas = port.listarPorAerolinea(aerolineaId).getItem();
    String nombreRuta = rutas.stream().filter(r -> Integer.toString(r.getIdRuta()).equals(rutaId))
        .map(DataRuta::getNombre).findFirst().orElse(null);
    if (nombreRuta == null) {
      request.setAttribute("errorMsg", "Ruta seleccionada no válida.");
      doGet(request, response);
      return;
    }

    // Validar nombre único en la ruta de la aerolínea
    List<DataVueloEspecifico> vuelos = port.listarVuelos(aerolineaId, nombreRuta).getItem();
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
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    cal.set(java.util.Calendar.MILLISECOND, 0);
    java.util.Date startOfToday = cal.getTime();
    if (fecha.before(startOfToday)) {
        request.setAttribute("errorMsg", "La fecha no puede ser anterior a hoy.");
        doGet(request, response);
        return;
    }
    int duracion, cantMaxTuristas, cantMaxEjecutivos;
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
    // Crear DTO de vuelo para el WebService
    DataVueloEspecifico dataVuelo = new DataVueloEspecifico();
    dataVuelo.setNombre(nombreVuelo);
    // Convertir java.util.Date a XMLGregorianCalendar
    javax.xml.datatype.DatatypeFactory df = null;
    try {
      df = javax.xml.datatype.DatatypeFactory.newInstance();
    } catch (Exception e) {
      request.setAttribute("errorMsg", "Error interno al convertir fechas.");
      doGet(request, response);
      return;
    }
    java.util.Calendar calFecha = java.util.Calendar.getInstance();
    calFecha.setTime(fecha);
    javax.xml.datatype.XMLGregorianCalendar xmlFecha = df.newXMLGregorianCalendar(
      calFecha.get(java.util.Calendar.YEAR),
      calFecha.get(java.util.Calendar.MONTH) + 1,
      calFecha.get(java.util.Calendar.DAY_OF_MONTH),
      calFecha.get(java.util.Calendar.HOUR_OF_DAY),
      calFecha.get(java.util.Calendar.MINUTE),
      calFecha.get(java.util.Calendar.SECOND),
      calFecha.get(java.util.Calendar.MILLISECOND),
      java.util.TimeZone.getDefault().getRawOffset() / (60 * 1000)
    );
    dataVuelo.setFecha(xmlFecha);
    dataVuelo.setDuracion(duracion);
    dataVuelo.setMaxAsientosTur(cantMaxTuristas);
    dataVuelo.setMaxAsientosEjec(cantMaxEjecutivos);
    // Fecha alta
    java.util.Calendar calAlta = java.util.Calendar.getInstance();
    calAlta.setTime(fechaAlta);
    javax.xml.datatype.XMLGregorianCalendar xmlFechaAlta = df.newXMLGregorianCalendar(
      calAlta.get(java.util.Calendar.YEAR),
      calAlta.get(java.util.Calendar.MONTH) + 1,
      calAlta.get(java.util.Calendar.DAY_OF_MONTH),
      calAlta.get(java.util.Calendar.HOUR_OF_DAY),
      calAlta.get(java.util.Calendar.MINUTE),
      calAlta.get(java.util.Calendar.SECOND),
      calAlta.get(java.util.Calendar.MILLISECOND),
      java.util.TimeZone.getDefault().getRawOffset() / (60 * 1000)
    );
    dataVuelo.setFechaAlta(xmlFechaAlta);
    // Si necesitas setear la ruta, puedes buscarla y setearla:
    DataRuta rutaSeleccionada = rutas.stream().filter(r -> Integer.toString(r.getIdRuta()).equals(rutaId)).findFirst().orElse(null);
    if (rutaSeleccionada != null) {
      dataVuelo.setDruta(rutaSeleccionada);
    }

    try {
      port.registrarVuelo(aerolineaId, nombreRuta, dataVuelo);
      request.setAttribute("successMsg", "Vuelo registrado exitosamente.");
    } catch (Exception e) {
      request.setAttribute("errorMsg", e.getMessage());
    }
    doGet(request, response);
  }
}