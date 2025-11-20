<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ page import="uy.volando.publicar.DataBusquedaItem" %>
<%@ page import="uy.volando.publicar.TipoResultado" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
    <title>Resultados de búsqueda - Volando.uy</title>
</head>

<body>
<jsp:include page="/WEB-INF/template/header.jsp" />

<%
    // Traigo atributos del servlet
    List<DataBusquedaItem> resultados = (List<DataBusquedaItem>) request.getAttribute("resultados");

    String q = (String) request.getAttribute("q");
    String orden = (String) request.getAttribute("orden");

    if (resultados == null) resultados = new ArrayList<>();

    if (orden == null || orden.isBlank()) orden = "default";

    // Formateador, fechaAlta es Date
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

<main class="container py-4">

    <!-- TÍTULO -->
    <div class="mb-3">
        <% if (q == null || q.trim().isEmpty()) { %>
        <h3 class="fw-bold">Mostrando todas las rutas y paquetes</h3>
        <% } else { %>
        <h3 class="fw-bold">Resultados para “<%= q %>”</h3>
        <% } %>
    </div>

    <!-- BARRA DE CONTROLES -->
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4 p-3 bg-light rounded-3">
        <div class="text-muted">
            Se encontraron <strong><%= resultados.size() %></strong> resultados
        </div>

        <!-- Selector de orden -->
        <form method="get" action="<%= request.getContextPath() %>/buscar"
              class="d-flex align-items-center gap-2">
            <input type="hidden" name="q" value="<%= (q == null ? "" : q) %>" />
            <label for="orden" class="text-muted small">Ordenar por:</label>
            <select id="orden" name="orden" class="form-select form-select-sm"
                    onchange="this.form.submit()">
                <option value="default" <%= "default".equals(orden) ? "selected" : "" %>>
                    Más recientes
                </option>
                <option value="alpha" <%= "alpha".equals(orden) ? "selected" : "" %>>
                    Alfabético (A-Z)
                </option>
            </select>
        </form>
    </div>

    <!-- SIN RESULTADOS -->
    <% if (resultados.isEmpty()) { %>
    <div class="alert alert-warning">
        No se encontraron resultados para tu búsqueda.
    </div>
    <% } %>

    <!-- LISTA DE RESULTADOS -->
    <div class="row g-3">
        <%
            for (DataBusquedaItem item : resultados) {

                // Tipo (puede venir como enum o String según SOAP)
                String tipoStr = String.valueOf(item.getTipo()); // "RUTA" o "PAQUETE"
                boolean esRuta = "RUTA".equals(tipoStr);

                // Fecha alta: si es Date la formateo, sino toString
                String fechaFormateada = "";
                Object fechaObj = item.getFechaAlta();
                if (fechaObj != null) {
                    if (fechaObj instanceof Date) {
                        fechaFormateada = sdf.format((Date) fechaObj);
                    } else {
                        fechaFormateada = fechaObj.toString();
                    }
                } else {
                    fechaFormateada = "-";
                }

                // Links de detalle (ajustá rutas reales)
                String hrefDetalle;

                if (esRuta) {
                    hrefDetalle = request.getContextPath() + "/consultaRuta?id=" + item.getIdentificador();

                } else {
                    hrefDetalle = request.getContextPath() + "/consultaPaquete?nombre=" + item.getIdentificador();

                }
        %>

        <div class="col-12">
            <div class="card shadow-sm">
                <div class="card-body">

                    <!-- BADGE TIPO -->
                    <% if (esRuta) { %>
                    <span class="badge bg-primary mb-2">Ruta de vuelo</span>
                    <% } else { %>
                    <span class="badge bg-success mb-2">Paquete</span>
                    <% } %>

                    <!-- NOMBRE -->
                    <h5 class="card-title mb-1"><%= item.getNombre() %></h5>

                    <!-- DESCRIPCIÓN -->
                    <p class="card-text text-muted mb-2">
                        <%= item.getDescripcion() %>
                    </p>

                    <!-- FECHA -->
                    <p class="small text-secondary mb-3">
                        Alta: <%= fechaFormateada %>
                    </p>

                </div>
            </div>
        </div>

        <%
            } // fin for
        %>
    </div>

</main>

<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>
</html>
