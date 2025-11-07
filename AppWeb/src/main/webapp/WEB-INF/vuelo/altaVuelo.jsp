<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="Logica.DataAerolinea" %>
<%@page import="Logica.DataRuta" %>
<!DOCTYPE html>
<html lang="es">
<head>
	<jsp:include page="/WEB-INF/template/head.jsp" />
    <title>Registro de Vuelo - Volando.uy</title>
</head>
<body class="regVuelo-page">
    <!-- NAVBAR -->
    <jsp:include page="/WEB-INF/template/header.jsp" />

    <!-- LAYOUT: SIDEBAR + CONTENIDO -->
  <div class="container-fluid">
    <div class="row">
      <!-- SIDEBAR (visible en ≥ lg) -->
      <jsp:include page="/WEB-INF/template/sidebar.jsp" />

      <!-- CONTENIDO -->
      <main class="col-12 col-lg-9 col-xl-10 py-4">
        <div class="container">
            <h1>Registro de Vuelo</h1>
            <form method="post" action="altaVuelo">
            <!-- Aerolínea solo lectura -->
            <div class="mb-3">
                <label class="form-label">Aerolínea</label>
                <input type="text" class="form-control" value="<%= request.getAttribute("aerolineaNombre") %>" readonly>
                <input type="hidden" name="aerolineaId" value="<%= request.getAttribute("aerolineaSeleccionada") %>">
            </div>
            <!-- Rutas de la aerolínea -->
            <div class="mb-3">
                <label for="rutaId" class="form-label">Ruta de Vuelo</label>
                <select class="form-select" id="rutaId" name="rutaId" required>
                    <option value="">Selecciona una ruta...</option>
                    <% List<DataRuta> rutas = (List<DataRuta>)request.getAttribute("rutas");
                       if (rutas != null) {
                         for (DataRuta r : rutas) { %>
                           <option value="<%=r.getIdRuta()%>"><%=r.getNombre()%></option>
                    <%   }
                       } %>
                </select>
            </div>
            <div class="mb-3">
                <label for="nombreVuelo" class="form-label">Nombre del Vuelo</label>
                <input type="text" class="form-control" id="nombreVuelo" name="nombreVuelo" placeholder="Ej: PU1234" required>
            </div>
            <div class="mb-3">
                <label for="fecha" class="form-label">Fecha del Vuelo</label>
                <input type="date" class="form-control" id="fecha" name="fecha" required>
            </div>
            <div class="mb-3">
                <label for="duracion" class="form-label">Duración (minutos)</label>
                <input type="number" class="form-control" id="duracion" name="duracion" placeholder="Ej: 120" required>
            </div>
            <div class="mb-3">
                <label for="cantMaxTuristas" class="form-label">Cantidad Máxima de Turistas</label>
                <input type="number" class="form-control" id="cantMaxTuristas" name="cantMaxTuristas" placeholder="Ej: 180" required>
            </div>
            <div class="mb-3">
                <label for="cantMaxEjecutivos" class="form-label">Cantidad Máxima de Ejecutivos</label>
                <input type="number" class="form-control" id="cantMaxEjecutivos" name="cantMaxEjecutivos" placeholder="Ej: 20" required>
            </div>
            <div class="mb-3">
                <label for="imagenVuelo" class="form-label">URL de la Imagen del Vuelo</label>
                <input type="url" class="form-control" id="imagenVuelo" name="imagenVuelo" placeholder="https://ejemplo.com/imagen.jpg">
            </div>
            <button type="submit" class="btn btn-primary">Registrar Vuelo</button>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Cancelar</a>
            </form>
            <% if (request.getAttribute("errorMsg") != null) { %>
              <div class="alert alert-danger"><%= request.getAttribute("errorMsg") %></div>
            <% } %>
            <% if (request.getAttribute("successMsg") != null) { %>
              <div class="alert alert-success"><%= request.getAttribute("successMsg") %></div>
            <% } %>
        </div>
      </main>
    </div>
  </div>
	<jsp:include page="/WEB-INF/template/footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../assets/js/auth.js"></script>
    <script src="../assets/js/roles.js"></script>
    <script>
    // Eliminar el cambio de aerolínea, ya no es editable
    </script>
</body>
</html>