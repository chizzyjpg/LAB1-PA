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
      <aside class="col-lg-3 col-xl-2 d-none d-lg-block border-end bg-body-tertiary">
        <nav class="sidebar position-sticky sticky-top">
          <div class="p-3">
            <h6 class="text-uppercase text-muted mb-3" data-roles="Aerolínea">Registros</h6>
            <div class="list-group list-group-flush" data-roles="Aerolínea">
              
              <a href="registroVuelo.html" class="list-group-item list-group-item-action active">Nuevo Vuelo</a>
              <a href="registroRutaVuelo.html" class="list-group-item list-group-item-action">Nueva Ruta de Vuelo</a>

            </div><br>
            <h6 class="text-uppercase text-muted mb-3">Consultas</h6>
            <div class="list-group list-group-flush">
              <a href="consultaUsuario.html" class="list-group-item list-group-item-action">Usuario</a>
              <a href="consultaRutasVuelo.html" class="list-group-item list-group-item-action">Ruta de Vuelo</a>
              <a href="consultaVuelo.html" class="list-group-item list-group-item-action">Vuelo</a>
              <a href="consultaPaqRutasVuelo.html" class="list-group-item list-group-item-action">Paquete de Rutas de Vuelo</a>
              <a href="consultaReservas.html" data-roles="Cliente" class="list-group-item list-group-item-action">Mis Reservas de Vuelo</a>
              <a href="consultaReservas.html" data-roles="Aerolínea" class="list-group-item list-group-item-action">Reservas de Nuestros Vuelos</a>
            </div><br>
            <h6 class="text-uppercase text-muted mb-3" data-roles="Aerolínea,Cliente">Modificaciones</h6>
            <div class="list-group list-group-flush mb-4" data-roles="Aerolínea,Cliente">
              <a href="perfil.html" class="list-group-item list-group-item-action">Modificar mis datos</a>
            </div>
            <h6 class="text-uppercase text-muted mb-3" data-roles="Cliente">Reserva / Compra</h6>
            <div class="list-group list-group-flush" data-roles="Cliente">
              <a href="reservarVuelo.html" class="list-group-item list-group-item-action">Reservar Vuelo</a>
              <a href="comprarPaquete.html" class="list-group-item list-group-item-action">Comprar Paquete</a>
            </div>
          </div>
        </nav>
      </aside>

      <!-- CONTENIDO -->
      <main class="col-12 col-lg-9 col-xl-10 py-4">
        <div class="container">
            <h1>Registro de Vuelo</h1>
            <form method="post" action="altaVuelo">
            <!-- Primero selecciona la aerolínea -->
            <div class="mb-3">
                <label for="aerolineaId" class="form-label">Aerolínea</label>
                <select class="form-select" id="aerolineaId" name="aerolineaId" required>
                    <option value="">Selecciona una aerolínea...</option>
                    <% String aerolineaSeleccionada = (String)request.getAttribute("aerolineaSeleccionada");
                       List<DataAerolinea> aerolineas = (List<DataAerolinea>)request.getAttribute("aerolineas");
                       if (aerolineas != null) {
                         for (DataAerolinea a : aerolineas) { %>
                           <option value="<%=a.getNickname()%>" <%=a.getNickname().equals(aerolineaSeleccionada) ? "selected" : ""%>><%=a.getNombre()%></option>
                    <%   }
                       } %>
                </select>
            </div>
            <!-- Luego se llenan automáticamente las rutas de esa aerolínea -->
            <div class="mb-3">
                <label for="rutaId" class="form-label">Ruta de Vuelo</label>
                <select class="form-select" id="rutaId" name="rutaId" required>
                    <option value="">Selecciona una ruta...</option>
                    <% List<DataRuta> rutas = (List<DataRuta>)request.getAttribute("rutas");
                       if (rutas != null) {
                         for (DataRuta r : rutas) {
                           String aerolineaNick = r.getNicknameAerolinea();
                    %>
                           <option value="<%=r.getIdRuta()%>" data-aerolinea="<%=aerolineaNick%>"><%=r.getNombre()%></option>
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
            <a href="../home.html" class="btn btn-secondary">Cancelar</a>
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
    // Recargar la página enviando el parámetro aerolineaId al cambiar la aerolínea
      document.getElementById('aerolineaId').addEventListener('change', function() {
        var aerolineaId = this.value;
        window.location.href = 'altaVuelo?aerolineaId=' + encodeURIComponent(aerolineaId);
      });
    </script>
</body>
</html>