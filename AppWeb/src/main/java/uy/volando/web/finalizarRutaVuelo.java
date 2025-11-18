package uy.volando.web;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uy.volando.publicar.DataAerolinea;
import uy.volando.publicar.DataAerolineaArray;
import uy.volando.publicar.WebServices;

import java.util.List;

@WebServlet("/finalizarRutaVuelo")
public class finalizarRutaVuelo extends HttpServlet {
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
    public finalizarRutaVuelo() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {

        HttpSession session = request.getSession();
        Object usuario = session.getAttribute("usuario_logueado");
        request.setAttribute("usuario", usuario);

        // Obtener nickname de la aerolínea logueada
        String nicknameAerolinea = null;
        if (usuario instanceof uy.volando.publicar.DataAerolinea) {
            nicknameAerolinea = ((uy.volando.publicar.DataAerolinea) usuario).getNickname();
        }

        // Si no hay aerolínea logueada, redirigir o mostrar error
        if (nicknameAerolinea == null) {
            request.setAttribute("mensaje", "Debe iniciar sesión como aerolínea.");
            request.getRequestDispatcher("/WEB-INF/perfil/perfilAerolinea.jsp").forward(request, response);
            return;
        }

        // Obtener rutas de la aerolínea
        var rutasArray = port.listarPorAerolinea(nicknameAerolinea);
        var rutas = rutasArray != null ? rutasArray.getItem() : null;
        // Filtrar solo rutas en estado CONFIRMADA
        java.util.List<uy.volando.publicar.DataRuta> rutasConfirmadas = new java.util.ArrayList<>();
        if (rutas != null) {
            for (var r : rutas) {
                if (r.getEstado() != null && r.getEstado().toString().equals("CONFIRMADA")) {
                    rutasConfirmadas.add(r);
                }
            }
        }
        request.setAttribute("rutasConfirmadas", rutasConfirmadas);

        // Si se seleccionó una ruta, obtener sus datos y vuelos asociados
        String nomRuta = request.getParameter("nomRuta");
        uy.volando.publicar.DataRuta rutaSeleccionada = null;
        if (nomRuta != null && rutas != null) {
            for (var r : rutas) {
                if (nomRuta.equals(r.getNombre())) {
                    rutaSeleccionada = r;
                    break;
                }
            }
        }
        request.setAttribute("rutaSeleccionada", rutaSeleccionada);

        // Aquí podrías obtener los vuelos asociados si tienes un método para eso
        // request.setAttribute("vuelosAsociados", vuelosAsociados);

        request.getRequestDispatcher("/WEB-INF/vuelo/finRutaVuelo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws ServletException, java.io.IOException {
        String nicknameAerolinea = request.getParameter("nicknameAerolinea");
        String nomRuta = request.getParameter("nomRuta");
        String mensaje = null;
        boolean exito = false;

        if (nicknameAerolinea == null || nomRuta == null) {
            mensaje = "Faltan datos requeridos.";
        } else {
            try {
                port.finalizarRutaDeVuelo(nicknameAerolinea, nomRuta);
                mensaje = "Ruta finalizada correctamente.";
                exito = true;
            } catch (Exception e) {
                mensaje = e.getMessage();
            }
        }

        request.setAttribute("mensaje", mensaje);
        request.setAttribute("exito", exito);
        // Puedes reenviar a una JSP de resultado o al perfil de la aerolínea
        request.getRequestDispatcher("/WEB-INF/home/iniciado.jsp").forward(request, response);
    }
}
