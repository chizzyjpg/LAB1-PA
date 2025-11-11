package uy.volando.web;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.volando.publicar.WebServices;

import java.io.IOException;
import java.util.Collections;
import java.util.List;



import uy.volando.publicar.WebServices;
import uy.volando.publicar.DataAerolineaArray;
import uy.volando.publicar.DataCategoriaArray;
import uy.volando.publicar.DataVueloEspecificoArray;
import uy.volando.publicar.DataRutaArray;

import uy.volando.publicar.DataRuta;
import uy.volando.publicar.DataAerolinea;
import uy.volando.publicar.DataCategoria;
import uy.volando.publicar.DataVueloEspecifico;

/**
 * Servlet implementation class consultaRutaVuelo.
 */
@WebServlet("/consultaRutaVuelo")
public class consultaRutaVuelo extends HttpServlet {
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
  /**
   * Default constructor.
   */
  public consultaRutaVuelo() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

      HttpSession session = request.getSession();
      Object usuario = session.getAttribute("usuario_logueado");
      request.setAttribute("usuario", usuario);

      DataAerolineaArray dataAerolineaArray = port.listarAerolineas();
      System.out.println("[DEBUG] dataAerolineaArray: " + dataAerolineaArray);
      List<DataAerolinea> aerolineas = dataAerolineaArray != null ? dataAerolineaArray.getItem() : null;
      System.out.println("[DEBUG] aerolineas: " + aerolineas);
      request.setAttribute("aerolineas", aerolineas);
      if (aerolineas != null) {
          for (DataAerolinea a : aerolineas) {
              System.out.println("[DEBUG] Aerolínea: " + a.getNombre() + " (" + a.getNickname() + ")");
          }
      }

      DataCategoriaArray dataCategoriaArray = port.listarCategorias();
      List<DataCategoria> categorias = dataCategoriaArray != null ? dataCategoriaArray.getItem() : null;
      request.setAttribute("categorias", categorias);


      String aerolineaSel = request.getParameter("aerolinea");
    System.out.println("[DEBUG] Valor de aerolineaSel: " + aerolineaSel);
      List<DataRuta> rutas = null;
      if (aerolineaSel != null && !aerolineaSel.isEmpty()) {
          DataRutaArray dataRutaArray = port.listarPorAerolinea(aerolineaSel);
          if (dataRutaArray != null && dataRutaArray.getItem() != null) {
              rutas = dataRutaArray.getItem().stream()
                      .filter(r -> r.getEstado() != null && r.getEstado().name().equalsIgnoreCase("CONFIRMADA"))
                      .toList();
          }
      }
      request.setAttribute("rutas", rutas);


      request.getRequestDispatcher("/WEB-INF/vuelo/consultaRutaVuelo.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

      HttpSession session = request.getSession();
      Object usuario = session.getAttribute("usuario_logueado");
      request.setAttribute("usuario", usuario);

    String aerolineaSel = request.getParameter("aerolinea");
    System.out.println("[DEBUG] doPost - aerolineaSel inicial: " + aerolineaSel);
    if (aerolineaSel != null) {
      aerolineaSel = aerolineaSel.trim();
    }
    String categoriaSel = request.getParameter("categoria");
    String rutaSel = request.getParameter("ruta");

  DataAerolineaArray dataAerolineaArray = port.listarAerolineas();
  System.out.println("[DEBUG] dataAerolineaArray: " + dataAerolineaArray);
  List<DataAerolinea> aerolineas = dataAerolineaArray != null ? dataAerolineaArray.getItem() : null;
  System.out.println("[DEBUG] aerolineas: " + aerolineas);
  request.setAttribute("aerolineas", aerolineas);
      if (aerolineas != null) {
          for (DataAerolinea a : aerolineas) {
              System.out.println("[DEBUG] Aerolínea: " + a.getNombre() + " (" + a.getNickname() + ")");
          }
      }

  DataCategoriaArray dataCategoriaArray = port.listarCategorias();
  List<DataCategoria> categorias = dataCategoriaArray != null ? dataCategoriaArray.getItem() : null;
  request.setAttribute("categorias", categorias);


      List<DataRuta> rutas = null;
      DataRutaArray dataRutaArray = port.listarPorAerolinea(aerolineaSel);
      if (dataRutaArray != null && dataRutaArray.getItem() != null) {
          rutas = dataRutaArray.getItem().stream()
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
        List<DataVueloEspecifico> vuelos = port.listarVuelos(ruta.getNicknameAerolinea(),
            ruta.getNombre()).getItem();
        request.setAttribute("rutaSeleccionada", ruta);
        request.setAttribute("vuelos", vuelos);
        // Si se seleccionó un vuelo específico, buscarlo y mostrar detalles
        String vueloSel = request.getParameter("vuelo");
        if (vueloSel != null && !vueloSel.isEmpty()) {
          DataVueloEspecifico vueloSeleccionado = vuelos.stream()
            .filter(v -> v.getNombre().equals(vueloSel))
            .findFirst().orElse(null);
          request.setAttribute("vueloSeleccionado", vueloSeleccionado);
        }
      }
    }
    request.getRequestDispatcher("/WEB-INF/vuelo/consultaRutaVuelo.jsp").forward(request, response);
  }
}