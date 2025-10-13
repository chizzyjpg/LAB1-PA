package uy.volando.web;

import java.io.IOException;
import java.util.List;

import Logica.Ciudad;
import Logica.Categoria;
import Logica.DataRuta;
import Logica.DataCiudad;
import Logica.DataCategoria;
import Logica.ISistema;
import Logica.Sistema;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet ("/regRutVuelo")
public class regRutVuelo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public regRutVuelo() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object usuario = session.getAttribute("usuario_logueado");
        request.setAttribute("usuario", usuario);

        ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
        List<DataCiudad> ciudades = sistema.listarCiudades();
        List<DataCategoria> categorias = sistema.listarCategorias();
        request.setAttribute("ciudades", ciudades);
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object usuario = session.getAttribute("usuario_logueado");
        request.setAttribute("usuario", usuario);

        ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
        String nicknameAerolinea = null;
        if (usuario instanceof Logica.DataAerolinea) {
            nicknameAerolinea = ((Logica.DataAerolinea) usuario).getNickname();
        }
        String nombre = request.getParameter("nombreRutaVuelo");
        String descripcionCorta = request.getParameter("descCorta");
        String descripcion = request.getParameter("descLarga");
        String horaStr = request.getParameter("hora");
        int hora = 0;
        if (horaStr != null && horaStr.contains(":")) {
            hora = Integer.parseInt(horaStr.split(":")[0]);
        } else if (horaStr != null) {
            hora = Integer.parseInt(horaStr);
        }
        java.math.BigDecimal costoTurista = new java.math.BigDecimal(request.getParameter("costoTurista"));
        java.math.BigDecimal costoEjecutivo = new java.math.BigDecimal(request.getParameter("costoEjecutivo"));
        int costoEquipajeExtra = Integer.parseInt(request.getParameter("costoEquipajeExtra"));
        String ciudadOrigenNombre = request.getParameter("ciudadOrigen");
        String ciudadDestinoNombre = request.getParameter("ciudadDestino");
        String[] categoriasSeleccionadas = request.getParameterValues("categorias");
        String imagen = request.getParameter("imagen");
        java.util.Date fechaAlta = new java.util.Date();

        if (nombre == null || nombre.isBlank() || descripcionCorta == null || descripcionCorta.isBlank() || descripcion == null || descripcion.isBlank() || ciudadOrigenNombre == null || ciudadDestinoNombre == null || categoriasSeleccionadas == null || categoriasSeleccionadas.length == 0) {
            recargarListasYError(request, response, "Todos los campos obligatorios deben estar completos.");
            return;
        }

        List<DataCiudad> ciudades = sistema.listarCiudades();
        List<DataCategoria> categorias = sistema.listarCategorias();

        DataCiudad ciudadOrigen = ciudades.stream()
            .filter(c -> c.getNombre().equals(ciudadOrigenNombre))
            .findFirst().orElse(null);
        DataCiudad ciudadDestino = ciudades.stream()
            .filter(c -> c.getNombre().equals(ciudadDestinoNombre))
            .findFirst().orElse(null);

        DataCategoria categoria = categorias.stream()
            .filter(cat -> cat.getNombre().equals(categoriasSeleccionadas[0]))
            .findFirst().orElse(null);

        if (ciudadOrigen == null || ciudadDestino == null || categoria == null) {
            recargarListasYError(request, response, "Ciudad o categoría seleccionada no válida.");
            return;
        }

        DataRuta dataRuta = new DataRuta(
            nombre,
            descripcion,
            ciudadOrigen,
            ciudadDestino,
            hora,
            fechaAlta,
            costoTurista,
            costoEquipajeExtra,
            costoEjecutivo,
            categoria,
            nicknameAerolinea,
            null,
            descripcionCorta
        );
        // Si necesitas asignar imagen, usa un setter si existe

        try {
            sistema.registrarRuta(dataRuta);
            recargarListasYExito(request, response, "Ruta de vuelo registrada exitosamente.");
        } catch (Exception ex) {
            recargarListasYError(request, response, ex.getMessage());
        }
    }

    private void recargarListasYError(HttpServletRequest request, HttpServletResponse response, String errorMsg) throws ServletException, IOException {
        ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
        List<DataCiudad> ciudades = sistema.listarCiudades();
        List<DataCategoria> categoriasList = sistema.listarCategorias();
        request.setAttribute("ciudades", ciudades);
        request.setAttribute("categorias", categoriasList);
        request.setAttribute("error", errorMsg);
        request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
    }

    private void recargarListasYExito(HttpServletRequest request, HttpServletResponse response, String exitoMsg) throws ServletException, IOException {
        ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
        List<DataCiudad> ciudades = sistema.listarCiudades();
        List<DataCategoria> categoriasList = sistema.listarCategorias();
        request.setAttribute("ciudades", ciudades);
        request.setAttribute("categorias", categoriasList);
        request.setAttribute("exito", exitoMsg);
        request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
    }
}