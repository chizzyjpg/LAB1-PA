<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="uy.volando.publicar.DataRuta" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
    <title>Finalizar Ruta de Vuelo - Volando.uy</title>
</head>
<body class="finRutaVuelo-page">
    <jsp:include page="/WEB-INF/template/header.jsp" />
    <div class="container mt-5">
        <h1 class="mb-4">Finalizar Ruta de Vuelo</h1>
        <% String mensaje = (String) request.getAttribute("mensaje");
           Boolean exito = (Boolean) request.getAttribute("exito");
           if (mensaje != null) { %>
            <div class="alert <%= (exito != null && exito) ? "alert-success" : "alert-danger" %>" role="alert">
                <%= mensaje %>
            </div>
        <% } %>
        <form method="get" class="mb-4">
            <div class="mb-3">
                <label for="nomRuta" class="form-label">Selecciona una ruta confirmada</label>
                <select id="nomRuta" name="nomRuta" class="form-select" onchange="this.form.submit()">
                    <option value="">-- Selecciona una ruta --</option>
                    <% List<DataRuta> rutasConfirmadas = (List<DataRuta>) request.getAttribute("rutasConfirmadas");
                       String nomRutaSel = request.getParameter("nomRuta");
                       if (rutasConfirmadas != null) {
                           for (DataRuta ruta : rutasConfirmadas) { %>
                        <option value="<%= ruta.getNombre() %>" <%= (ruta.getNombre().equals(nomRutaSel)) ? "selected" : "" %>>
                            <%= ruta.getNombre() %> - <%= ruta.getCiudadOrigen().getNombre() %> a <%= ruta.getCiudadDestino().getNombre() %>
                        </option>
                    <%   }
                       } %>
                </select>
            </div>
        </form>
        <% DataRuta rutaSeleccionada = (DataRuta) request.getAttribute("rutaSeleccionada");
           if (rutaSeleccionada != null) { %>
            <div class="card mb-4">
                <div class="card-body">
                    <h5 class="card-title">Datos de la ruta</h5>
                    <p><strong>Nombre:</strong> <%= rutaSeleccionada.getNombre() %></p>
                    <p><strong>Origen:</strong> <%= rutaSeleccionada.getCiudadOrigen().getNombre() %></p>
                    <p><strong>Destino:</strong> <%= rutaSeleccionada.getCiudadDestino().getNombre() %></p>
                    <p><strong>Descripción:</strong> <%= rutaSeleccionada.getDescripcion() %></p>
                    <p><strong>Categoría:</strong> <%= rutaSeleccionada.getCategoria() != null ? rutaSeleccionada.getCategoria().getNombre() : "" %></p>
                    <p><strong>Costo Turista:</strong> <%= rutaSeleccionada.getCostoTurista() %></p>
                    <p><strong>Costo Ejecutivo:</strong> <%= rutaSeleccionada.getCostoEjecutivo() %></p>
                    <p><strong>Estado:</strong> <%= rutaSeleccionada.getEstado() != null ? rutaSeleccionada.getEstado().name() : "" %></p>
                </div>
            </div>
            <form method="post">
                <input type="hidden" name="nicknameAerolinea" value="<%= rutaSeleccionada.getNicknameAerolinea() %>" />
                <input type="hidden" name="nomRuta" value="<%= rutaSeleccionada.getNombre() %>" />
                <button type="submit" class="btn btn-danger" onclick="return confirm('¿Está seguro que desea finalizar esta ruta? Esta acción no se puede deshacer.');">Finalizar Ruta</button>
            </form>
        <% } %>
    </div>
    <jsp:include page="/WEB-INF/template/footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
