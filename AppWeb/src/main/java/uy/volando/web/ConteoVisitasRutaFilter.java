package uy.volando.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

import uy.volando.publicar.WebServices;
import uy.volando.publicar.DataRuta;
import uy.volando.publicar.DataRutaArray;

@WebFilter("/consultaRutaVuelo")
public class ConteoVisitasRutaFilter implements Filter {

    private WebServices port;
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();

        Object portObj = servletContext.getAttribute("volandoPort");
        if (portObj instanceof WebServices) {
            this.port = (WebServices) portObj;
        } else {
            throw new ServletException(
                    "El cliente SOAP (WebServices) no fue inicializado en el contexto (atributo 'volandoPort')."
            );
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpReq
                && "POST".equalsIgnoreCase(httpReq.getMethod())) {

            String aerolineaSel = httpReq.getParameter("aerolinea");
            String categoriaSel = httpReq.getParameter("categoria");
            String rutaSel = httpReq.getParameter("ruta");

            if (aerolineaSel != null) aerolineaSel = aerolineaSel.trim();
            if (categoriaSel != null) categoriaSel = categoriaSel.trim();
            if (rutaSel != null) rutaSel = rutaSel.trim();

            final String aerolineaFinal = aerolineaSel;
            final String categoriaFinal = categoriaSel;
            final String rutaFinal = rutaSel;

            // Solo intentamos registrar visita si hay aerolínea y ruta
            if (port != null
                    && aerolineaFinal != null && !aerolineaFinal.isEmpty()
                    && rutaFinal != null && !rutaFinal.isEmpty()) {

                try {
                    // 1) Rutas por aerolínea
                    DataRutaArray dataRutaArray = port.listarPorAerolinea(aerolineaFinal);
                    if (dataRutaArray != null && dataRutaArray.getItem() != null) {
                        List<DataRuta> rutas = dataRutaArray.getItem().stream()
                                //solo CONFIRMADAS
                                .filter(r -> r.getEstado() != null
                                        && r.getEstado().name().equalsIgnoreCase("CONFIRMADA"))
                                .toList();

                        // 2) Aplicar filtro por categoría si corresponde (igual que doPost)
                        if (rutas != null
                                && categoriaFinal != null && !categoriaFinal.isEmpty()) {
                            rutas = rutas.stream()
                                    .filter(r -> r.getCategoria() != null
                                            && categoriaFinal.equals(
                                            r.getCategoria().getNombre()))
                                    .toList();
                        }

                        // 3) Buscar la ruta por nombre dentro de ese conjunto filtrado
                        DataRuta rutaEncontrada = rutas.stream()
                                .filter(r -> rutaFinal.equals(r.getNombre()))
                                .findFirst()
                                .orElse(null);

                        // 4) Solo si la ruta es válida para esa aerolínea+categoría, contamos visita
                        if (rutaEncontrada != null) {
                            int idRuta = rutaEncontrada.getIdRuta();
                            servletContext.log("[DEBUG] ConteoVisitasRutaFilter: registrando visita para ruta id=" + idRuta);
                            port.registrarVisitaRuta(idRuta);
                        } else {
                            servletContext.log("[DEBUG] ConteoVisitasRutaFilter: ruta '"
                                    + rutaFinal + "' no corresponde a la categoría seleccionada; no se registra visita.");
                        }
                    }
                } catch (Exception e) {
                    // Logueamos pero NO cortamos la navegación del usuario
                    servletContext.log("[WARN] Error al registrar visita de ruta en ConteoVisitasRutaFilter", e);
                }
            }
        }

        // Siempre dejamos continuar al siguiente filtro / servlet
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
