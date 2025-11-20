package uy.volando.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import uy.volando.publicar.DataBusquedaItemArray;
import uy.volando.publicar.WebServices;
import uy.volando.publicar.DataBusquedaItem;

@WebServlet("/buscar")
public class BusquedaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Cliente SOAP (se crea una vez)
    private WebServices port;

    @Override
    public void init() throws ServletException {
        super.init();
        this.port = (WebServices) getServletContext().getAttribute("volandoPort");
        if(this.port == null) {
            throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Leer parámetros
        String q = request.getParameter("q");
        String orden = request.getParameter("orden");

        //Normalizar texto
        if (q != null) {
            q = q.trim();
        }

        //Normalizar orden (default si viene vacío o null)
        if (orden == null || orden.isBlank()) {
            orden = "default";
        }

        // El WS devuelve array, lo paso a lista
        DataBusquedaItemArray array = port.buscarRutasYPaquetes(q);
        List<DataBusquedaItem> resultados = new ArrayList<>();

        if (array != null && array.getItem() != null) {
            resultados.addAll(array.getItem());
        }

        // Si pidieron orden alfabético, reordenar por nombre ASC
        if ("alpha".equalsIgnoreCase(orden)) {
            resultados.sort(
                    Comparator.comparing(
                            DataBusquedaItem::getNombre,
                            String.CASE_INSENSITIVE_ORDER
                    )
            );
        }
        // Si es "default", NO tocamos el orden porque ya viene por fecha DESC desde BD

        // Mandar datos al JSP
        request.setAttribute("resultados", resultados);
        request.setAttribute("q", q);
        request.setAttribute("orden", orden);

        //Forward a la página de resultados
        request.getRequestDispatcher("/WEB-INF/busqueda/resultadoBusqueda.jsp")
                .forward(request, response);
    }
}
