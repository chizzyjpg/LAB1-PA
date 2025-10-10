package uy.volando.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import BD.CiudadService;
import BD.CategoriaService;
import BD.RutaVueloService;
import Logica.Ciudad;
import Logica.Categoria;
import Logica.Ruta;
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
        // Obtener ciudades y categorías desde la base de datos
        CiudadService ciudadService = new CiudadService();
        CategoriaService categoriaService = new CategoriaService();
        List<Ciudad> ciudades = ciudadService.listarCiudades();
        List<Categoria> categorias = categoriaService.listarCategorias();
        request.setAttribute("ciudades", ciudades);
        request.setAttribute("categorias", categorias);
        // Mostrar el formulario de alta de ruta de vuelo
        request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Procesar los datos enviados desde el formulario
        String nombre = request.getParameter("nombreRutaVuelo");
        String descripcionCorta = request.getParameter("descCorta");
        String descripcion = request.getParameter("descLarga");
        String horaStr = request.getParameter("hora");
        int hora = 0;
        if (horaStr != null && horaStr.contains(":")) {
            // Si el formato es "HH:mm", tomar solo la hora
            hora = Integer.parseInt(horaStr.split(":")[0]);
        } else if (horaStr != null) {
            hora = Integer.parseInt(horaStr);
        }
        java.math.BigDecimal costoTurista = new java.math.BigDecimal(request.getParameter("costoTurista"));
        java.math.BigDecimal costoEjecutivo = new java.math.BigDecimal(request.getParameter("costoEjecutivo"));
        int costoEquipajeExtra = Integer.parseInt(request.getParameter("costoEquipajeExtra"));
        String ciudadOrigenNombre = request.getParameter("ciudadOrigen");
        String ciudadDestinoNombre = request.getParameter("ciudadDestino");
        String categoriaNombre = request.getParameter("categorias"); // Si solo se permite una categoría
        String imagen = request.getParameter("imagen");
        java.util.Date fechaAlta = new java.util.Date();
        String nicknameAerolinea = (String) request.getSession().getAttribute("nicknameAerolinea");

        RutaVueloService rutaService = new RutaVueloService();
        Ruta rutaExistente = rutaService.buscarRutaPorNombre(nombre);
        if (rutaExistente != null) {
            // Recargar listas para el JSP
            CiudadService ciudadService = new CiudadService();
            CategoriaService categoriaService = new CategoriaService();
            List<Ciudad> ciudades = ciudadService.listarCiudades();
            List<Categoria> categoriasList = categoriaService.listarCategorias();
            request.setAttribute("ciudades", ciudades);
            request.setAttribute("categorias", categoriasList);
            request.setAttribute("error", "Ya existe una ruta de vuelo con ese nombre. Por favor, elija otro nombre.");
            request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
            return;
        }
        // Buscar objetos Ciudad y Categoria
        CiudadService ciudadService = new CiudadService();
        CategoriaService categoriaService = new CategoriaService();
        Ciudad origen = ciudadService.listarCiudades().stream().filter(c -> c.getNombre().equals(ciudadOrigenNombre)).findFirst().orElse(null);
        Ciudad destino = ciudadService.listarCiudades().stream().filter(c -> c.getNombre().equals(ciudadDestinoNombre)).findFirst().orElse(null);
        Categoria categoria = categoriaService.listarCategorias().stream().filter(cat -> cat.getNombre().equals(categoriaNombre)).findFirst().orElse(null);
        // Validar que los objetos existan
        if (origen == null || destino == null || categoria == null) {
            List<Ciudad> ciudades = ciudadService.listarCiudades();
            List<Categoria> categoriasList = categoriaService.listarCategorias();
            request.setAttribute("ciudades", ciudades);
            request.setAttribute("categorias", categoriasList);
            request.setAttribute("error", "Error al seleccionar ciudad o categoría. Verifique los datos e intente nuevamente.");
            request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
            return;
        }
        // Crear la ruta
        Ruta nuevaRuta = new Ruta(nombre, descripcion, origen, destino, hora, fechaAlta, costoTurista, costoEquipajeExtra, costoEjecutivo, categoria, descripcionCorta);
        rutaService.crearRutaVuelo(nuevaRuta, nicknameAerolinea);
        // Recargar listas para el JSP
        List<Ciudad> ciudades = ciudadService.listarCiudades();
        List<Categoria> categoriasList = categoriaService.listarCategorias();
        request.setAttribute("ciudades", ciudades);
        request.setAttribute("categorias", categoriasList);
        request.setAttribute("exito", "Ruta de vuelo registrada exitosamente.");
        request.getRequestDispatcher("/WEB-INF/vuelo/regRutaVuelo.jsp").forward(request, response);
    }
}