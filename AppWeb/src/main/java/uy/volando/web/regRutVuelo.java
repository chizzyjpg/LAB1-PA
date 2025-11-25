package uy.volando.web;

import uy.volando.publicar.DataAerolinea;
import uy.volando.publicar.DataCategoria;
import uy.volando.publicar.DataCiudad;
import uy.volando.publicar.DataRuta;
import uy.volando.publicar.WebServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import uy.volando.model.EstadoSesion;
import uy.volando.publicar.WebServices;

/**
 * Servlet implementation class regRutVuelo.
 */
@WebServlet("/regRutVuelo")
public class regRutVuelo extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private WebServices port;
  /**
   * Default constructor.
   */
  public regRutVuelo() {
    super();
  }

  @Override
  public void init() throws ServletException {
      super.init();
      // Recuperamos el port que dejó InicioServlet en el contexto
      this.port = (WebServices) getServletContext().getAttribute("volandoPort");
      if (this.port == null) {
          throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
      }
    }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();

    if (getEstado(request) != EstadoSesion.LOGIN_CORRECTO) {
      request.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").forward(request, response);
      return;
    }
    Object usuario = session.getAttribute("usuario_logueado");
    request.setAttribute("usuario", usuario);

    List<uy.volando.publicar.DataCiudad> ciudades = port.listarCiudades().getItem();
    List<uy.volando.publicar.DataCategoria> categorias = port.listarCategorias().getItem();
    request.setAttribute("ciudades", ciudades);
    request.setAttribute("categorias", categorias);
    request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

      HttpSession session = request.getSession();
      Object usuario = session.getAttribute("usuario_logueado");
      request.setAttribute("usuario", usuario);

      String nicknameAerolinea = null;
      if (usuario instanceof DataAerolinea da) {
          nicknameAerolinea = da.getNickname();
      }

      String nombre = request.getParameter("nombreRutaVuelo");
      String descripcionCorta = request.getParameter("descCorta");
      String descripcion = request.getParameter("descLarga");
      String horaStr = request.getParameter("hora");
      int hora = 0;
      if (horaStr != null && horaStr.contains(":")) hora = Integer.parseInt(horaStr.split(":")[0]);
      else if (horaStr != null) hora = Integer.parseInt(horaStr);

      java.math.BigDecimal costoTurista   = new java.math.BigDecimal(request.getParameter("costoTurista"));
      java.math.BigDecimal costoEjecutivo = new java.math.BigDecimal(request.getParameter("costoEjecutivo"));
      int costoEquipajeExtra = Integer.parseInt(request.getParameter("costoEquipajeExtra"));

      String ciudadOrigenNombre  = request.getParameter("ciudadOrigen");
      String ciudadDestinoNombre = request.getParameter("ciudadDestino");
      String[] categoriasSeleccionadas = request.getParameterValues("categorias");

      // NUEVO: leer URL de video opcional
      String videoUrl = request.getParameter("videoUrl");
      if (videoUrl != null) {
          videoUrl = videoUrl.trim();
          if (videoUrl.isEmpty()) {
              videoUrl = null;
          }
      }

      java.util.Date fechaAlta = new java.util.Date();

      if (nombre == null || nombre.isBlank()
              || descripcionCorta == null || descripcionCorta.isBlank()
              || descripcion == null || descripcion.isBlank()
              || ciudadOrigenNombre == null || ciudadDestinoNombre == null
              || categoriasSeleccionadas == null || categoriasSeleccionadas.length == 0) {
          recargarListasYError(request, response, "Todos los campos obligatorios deben estar completos.");
          return;
      }

      // Listas desde SOAP
      List<DataCiudad> ciudades       = port.listarCiudades().getItem();
      List<DataCategoria> categorias  = port.listarCategorias().getItem();

      // Filtros
      DataCiudad ciudadOrigen = ciudades.stream()
              .filter(c -> ciudadOrigenNombre.equals(c.getNombre()))
              .findFirst().orElse(null);

      DataCiudad ciudadDestino = ciudades.stream()
              .filter(c -> ciudadDestinoNombre.equals(c.getNombre()))
              .findFirst().orElse(null);

      DataCategoria categoria = categorias.stream()
              .filter(cat -> categoriasSeleccionadas[0].equals(cat.getNombre()))
              .findFirst().orElse(null);

      if (ciudadOrigen == null || ciudadDestino == null || categoria == null) {
          recargarListasYError(request, response, "Ciudad o categoría seleccionada no válida.");
          return;
      }

      // Construir DTO SOAP con setters
      DataRuta dataRuta = new DataRuta();
      dataRuta.setNombre(nombre);
      dataRuta.setDescripcion(descripcion);
      dataRuta.setCiudadOrigen(ciudadOrigen);
      dataRuta.setCiudadDestino(ciudadDestino);
      dataRuta.setHora(hora);

      // convertir fechaAlta -> XMLGregorianCalendar
      dataRuta.setFechaAlta(toXMLGregorianCalendar(fechaAlta));

      dataRuta.setCostoTurista(costoTurista);
      dataRuta.setCostoEjecutivo(costoEjecutivo);
      dataRuta.setCostoEquipajeExtra(costoEquipajeExtra);
      dataRuta.setCategoria(categoria);
      dataRuta.setNicknameAerolinea(nicknameAerolinea);
      dataRuta.setDescripcionCorta(descripcionCorta);
      // NUEVO: enviar videoUrl al servidor central (puede ser null si no se ingresó)
      dataRuta.setVideoUrl(videoUrl);

      try {
          // Llamada SOAP
          port.registrarRuta(dataRuta);
          recargarListasYExito(request, response, "Ruta de vuelo registrada exitosamente.");
      } catch (Exception ex) {
          recargarListasYError(request, response, ex.getMessage());
      }
  }
    /**
   * Recarga las listas de ciudades y categorías, y muestra un mensaje de error.
   * 
   */
  private void recargarListasYError(HttpServletRequest request, HttpServletResponse response,
      String errorMsg) throws ServletException, IOException {

    List<uy.volando.publicar.DataCiudad> ciudades = port.listarCiudades().getItem();
    List<uy.volando.publicar.DataCategoria> categoriasList = port.listarCategorias().getItem();
    request.setAttribute("ciudades", ciudades);
    request.setAttribute("categorias", categoriasList);
    request.setAttribute("error", errorMsg);
    request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
  }

  /**
   * Recarga las listas de ciudades y categorías, y muestra un mensaje de éxito.
   * 
   */
  private void recargarListasYExito(HttpServletRequest request, HttpServletResponse response,
      String exitoMsg) throws ServletException, IOException {

    List<uy.volando.publicar.DataCiudad> ciudades = port.listarCiudades().getItem();
    List<uy.volando.publicar.DataCategoria> categoriasList = port.listarCategorias().getItem();
    request.setAttribute("ciudades", ciudades);
    request.setAttribute("categorias", categoriasList);
    request.setAttribute("exito", exitoMsg);
    request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
  }

  /**
   * Obtiene el estado de la sesión del usuario.
   */
  public static EstadoSesion getEstado(HttpServletRequest request) {
    return (EstadoSesion) request.getSession().getAttribute("estado_sesion");
  }

  /**
   * Inicializa la sesión del usuario si no está ya inicializada.
   */
  public static void initSession(HttpServletRequest request) {
    HttpSession session = request.getSession();
    if (session.getAttribute("estado_sesion") == null) {
      session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
    }
  }
    private static javax.xml.datatype.XMLGregorianCalendar toXMLGregorianCalendar(java.util.Date date) {
        if (date == null) return null;
        try {
            javax.xml.datatype.DatatypeFactory df = javax.xml.datatype.DatatypeFactory.newInstance();
            java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
            cal.setTime(date);
            return df.newXMLGregorianCalendar(cal);
        } catch (javax.xml.datatype.DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

}