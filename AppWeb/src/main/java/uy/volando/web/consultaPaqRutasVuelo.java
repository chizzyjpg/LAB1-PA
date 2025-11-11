package uy.volando.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uy.volando.publicar.WebServices;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Servlet implementation class consultaPaqRutasVuelo.
 */
@WebServlet("/consultaPaqRutasVuelo")
public class consultaPaqRutasVuelo extends HttpServlet {
  private static final long serialVersionUID = 1L;
    private WebServices port;

    public consultaPaqRutasVuelo() {
    super();
  }

  @Override
  public void init() throws ServletException {
      super.init();
      this.port = (WebServices) getServletContext().getAttribute("volandoPort");
      if (this.port == null) {
          throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
      }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String nombrePaquete = request.getParameter("paquete");
    String nombreRuta = request.getParameter("ruta");

    if (nombrePaquete == null || nombrePaquete.isEmpty()) {
      // Mostrar lista de paquetes (convertir wrapper a List)
      List<uy.volando.publicar.DataPaquete> paquetes = safeGetListFromWrapper(port.listarPaquetes());
      request.setAttribute("paquetes", paquetes);
      request.setAttribute("view", "list");
    } else {
      // Mostrar detalles del paquete
      uy.volando.publicar.DataPaquete paquete = port.verPaquete(nombrePaquete);
      request.setAttribute("paquete", paquete);
      request.setAttribute("view", "detail");

      // Mostrar rutas del paquete (usando nombres) - convertir a Set<String> de forma segura
      Object rutasObj = paquete.getRutasIncluidas();
      List<String> rutasAsList = safeGetListFromWrapper(rutasObj);
      Set<String> nombresRutas = new LinkedHashSet<>(rutasAsList);
      request.setAttribute("nombresRutas", nombresRutas);

      // Si se seleccionó una ruta, buscar el objeto DataRuta recorriendo todas las aerolíneas
      if (nombreRuta != null && !nombreRuta.isEmpty()) {
        uy.volando.publicar.DataRuta rutaSeleccionada = null;
        String nombreRutaTrim = nombreRuta.trim();
        List<uy.volando.publicar.DataAerolinea> aerolineas = safeGetListFromWrapper(port.listarAerolineas());
        for (uy.volando.publicar.DataAerolinea aerolinea : aerolineas) {
          List<uy.volando.publicar.DataRuta> rutas = safeGetListFromWrapper(port.listarPorAerolinea(aerolinea.getNickname()));
          for (uy.volando.publicar.DataRuta r : rutas) {
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

  @SuppressWarnings("unchecked")
  private <T> List<T> safeGetListFromWrapper(Object wrapper) {
    if (wrapper == null) return Collections.emptyList();
    if (wrapper instanceof List) {
      return (List<T>) wrapper;
    }
    // Algunos generadores usan getItem() que devuelve List<T>
    try {
      Method m = wrapper.getClass().getMethod("getItem");
      Object items = m.invoke(wrapper);
      if (items instanceof List) return (List<T>) items;
      if (items instanceof Object[]) return Arrays.asList((T[]) items);
    } catch (Exception ignored) {
    }
    // Otros wrappers exponen un arreglo directamente, p.ej. getDataAerolinea() no conocido
    try {
      // intentar cualquier método que empiece por 'getData' y devuelva un array
      for (Method mm : wrapper.getClass().getMethods()) {
        if (mm.getName().startsWith("getData") && mm.getReturnType().isArray()) {
          Object arr = mm.invoke(wrapper);
          if (arr instanceof Object[]) return Arrays.asList((T[]) arr);
        }
      }
    } catch (Exception ignored) {
    }
    // Si wrapper es un array
    if (wrapper.getClass().isArray()) {
      return Arrays.asList((T[]) wrapper);
    }
    // Fallback: tratar wrapper como un único elemento
    List<T> single = new ArrayList<>();
    single.add((T) wrapper);
    return single;
  }
}