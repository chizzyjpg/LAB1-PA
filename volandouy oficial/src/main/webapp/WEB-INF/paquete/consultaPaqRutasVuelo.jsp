<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
	<jsp:include page="/WEB-INF/template/head.jsp" />
	<title>Consulta de Paquetes de Rutas de Vuelo - Volando.uy</title>
</head>
<body class="consultaPaqRutasVuelo-page">
  <jsp:include page="/WEB-INF/template/header.jsp" />
  <div class="container-fluid">
    <div class="row">
      <jsp:include page="/WEB-INF/template/sidebar.jsp" />
      <main class="col-12 col-lg-9 col-xl-10 py-4">
        <div class="container-fluid">
          <!-- VISTA DE LISTA -->
          <% if ("list".equals(request.getAttribute("view"))) { %>
            <div class="text-center">
              <h1>Paquetes de Rutas de Vuelo</h1>
              <p class="text-muted">Selecciona un paquete para ver sus detalles completos</p>
            </div>
            <div class="row mb-4">
              <div class="col-md-8">
                <div class="alert alert-info" role="alert">
                  <i class="fas fa-info-circle me-2"></i>
                  <span id="paquetesCount">
                    <% java.util.List paquetes = (java.util.List)request.getAttribute("paquetes"); %>
                    <%= paquetes != null ? paquetes.size() : 0 %> paquetes encontrados
                  </span>
                </div>
              </div>
            </div>
            <div class="row" id="paquetesList">
              <% if (paquetes != null && !paquetes.isEmpty()) {
                   for (Object obj : paquetes) {
                     Logica.DataPaquete paq = (Logica.DataPaquete)obj; %>
                <div class="col-md-6 col-lg-4 mb-4">
                  <div class="card h-100">
                    <div class="card-body">
                      <h5 class="card-title"><%= paq.getNombre() %></h5>
                      <p class="card-text"><%= paq.getDescripcion() %></p>
                      <p class="card-text"><strong>Costo:</strong> $<%= paq.getCosto() %></p>
                      <a href="consultaPaqRutasVuelo?paquete=<%= paq.getNombre() %>" class="btn btn-primary">Ver detalles</a>
                    </div>
                  </div>
                </div>
              <%   }
                 } else { %>
                <div class="col-12 text-center py-5">
                  <p class="text-muted mt-2">No hay paquetes de rutas de vuelo registrados.</p>
                </div>
              <% } %>
            </div>
          <% } %>
          <!-- VISTA DE DETALLES -->
          <% if ("detail".equals(request.getAttribute("view"))) { %>
            <div class="d-flex justify-content-between align-items-center mb-4">
              <div>
                <h1 class="h2 mb-1">Detalles del Paquete</h1>
                <p class="text-muted">Información completa del paquete seleccionado</p>
              </div>
              <a href="consultaPaqRutasVuelo" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-2"></i>Volver a la lista
              </a>
            </div>
            <% Logica.DataPaquete paquete = (Logica.DataPaquete)request.getAttribute("paquete"); %>
            <div class="card mb-4">
              <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-info-circle me-2"></i>Información del Paquete</h5>
              </div>
              <div class="card-body">
                <p><strong>Nombre:</strong> <%= paquete.getNombre() %></p>
                <p><strong>Descripción:</strong> <%= paquete.getDescripcion() %></p>
                <p><strong>Costo:</strong> $<%= paquete.getCosto() %></p>
                <p><strong>Validez:</strong> <%= paquete.getValidez() %> días</p>
                <p><strong>Descuento:</strong> <%= paquete.getDescuento() %>%</p>
              </div>
            </div>
            <div class="card mb-4">
              <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-route me-2"></i>Rutas de Vuelo Incluidas</h5>
              </div>
              <div class="card-body">
                <p class="text-muted mb-3">Selecciona una ruta para ver su información detallada:</p>
                <form method="get" action="consultaPaqRutasVuelo">
                  <input type="hidden" name="paquete" value="<%= paquete.getNombre() %>" />
                  <div class="row">
                    <% java.util.Set nombresRutas = (java.util.Set)request.getAttribute("nombresRutas");
                       String rutaParam = request.getParameter("ruta");
                       if (nombresRutas != null && !nombresRutas.isEmpty()) {
                         for (Object nombreRutaObj : nombresRutas) {
                           String nombreRuta = (String)nombreRutaObj; %>
                      <div class="col-md-6 mb-3">
                        <div class="border rounded p-2">
                          <label>
                            <input type="radio" name="ruta" value="<%= nombreRuta %>" <%= (rutaParam != null && rutaParam.equals(nombreRuta)) ? "checked" : "" %> />
                            <strong><%= nombreRuta %></strong>
                          </label>
                        </div>
                      </div>
                    <%   }
                       } else { %>
                      <div class="col-12 text-center py-3">
                        <p class="text-muted">No hay rutas en este paquete.</p>
                      </div>
                    <% } %>
                  </div>
                  <div class="mt-3 text-center">
                    <button type="submit" class="btn btn-outline-primary" id="verDetallesBtn">Ver detalles de ruta</button>
                  </div>
                </form>
              </div>
            </div>
            <% Logica.DataRuta ruta = (Logica.DataRuta)request.getAttribute("ruta"); %>
            <% if (ruta != null) { %>
              <div class="card mb-4">
                <div class="card-header">
                  <h5 class="mb-0"><i class="fas fa-plane me-2"></i>Detalles de la Ruta Seleccionada</h5>
                </div>
                <div class="card-body">
                  <p><strong>Nombre:</strong> <%= ruta.getNombre() %></p>
                  <p><strong>Origen:</strong> <%= ruta.getCiudadOrigen().getNombre() %> - <%= ruta.getCiudadOrigen().getPais() %></p>
                  <p><strong>Destino:</strong> <%= ruta.getCiudadDestino().getNombre() %> - <%= ruta.getCiudadDestino().getPais() %></p>
                  <p><strong>Costo Turista:</strong> $<%= ruta.getCostoTurista() %></p>
                  <p><strong>Costo Ejecutivo:</strong> $<%= ruta.getCostoEjecutivo() %></p>
                  <p><strong>Estado:</strong> <%= ruta.getEstado() %></p>
                </div>
              </div>
            <% } else if (rutaParam != null) { %>
              <div class="alert alert-warning text-center mb-4">
                <i class="fas fa-exclamation-triangle me-2"></i>
                No se encontró la ruta seleccionada.
              </div>
            <% } %>
            <div class="mt-4 text-center">
              <button class="btn btn-success btn-lg me-3" data-roles="Cliente">
                <i class="fas fa-shopping-cart me-2"></i>Comprar Paquete
              </button>
              <a href="consultaPaqRutasVuelo" class="btn btn-outline-primary">
                <i class="fas fa-list me-2"></i>Ver Otros Paquetes
              </a>
            </div>
          <% } %>
        </div>
      </main>
    </div>
  </div>
  <jsp:include page="/WEB-INF/template/footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>