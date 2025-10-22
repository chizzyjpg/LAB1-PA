<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="Logica.DataRuta" %>
<%@page import="Logica.DataAerolinea" %>
<%@page import="Logica.DataCategoria" %>
<!DOCTYPE html>
<html lang="es">
<head>
	<jsp:include page="/WEB-INF/template/head.jsp" />
	<title>Consulta de Ruta de Vuelo - Volando.uy</title>
</head>
<body class="consultaRutasVuelo-page">
  <!-- NAVBAR -->
	<jsp:include page="/WEB-INF/template/header.jsp" />
	
 <!-- LAYOUT: SIDEBAR + CONTENIDO -->
  <div class="container-fluid mt-5">
    <div class="row">
      <!-- SIDEBAR (visible en ≥ lg) -->
 		<jsp:include page="/WEB-INF/template/sidebar.jsp" />
      <!-- CONTENIDO (container interno para centrar/limitar ancho) -->
      <main class="col-12 col-lg-9 col-xl-10 py-4">
        <div class="container">
        <!-- Mensaje de error -->
        <% if (request.getAttribute("errorMsg") != null) { %>
          <div class="alert alert-danger" role="alert">
            <%= request.getAttribute("errorMsg") %>
          </div>
        <% } %>
        <!-- Título -->
        <h1 class="text-center mb-4">Consulta de Ruta de Vuelo</h1>
        <!-- Filtros -->
        <section class="mb-3">
          <form method="post" class="row g-2 align-items-end">
            <div class="col-md-4">
              <label for="aerolinea" class="form-label">Aerolínea</label>
              <% String aerolineaSel = request.getParameter("aerolinea"); %>
              <select id="aerolinea" name="aerolinea" class="form-select">
                <option value="" <%= (aerolineaSel == null || aerolineaSel.isEmpty()) ? "selected" : "" %>>Todas</option>
                <% List<DataAerolinea> aerolineas = (List<DataAerolinea>) request.getAttribute("aerolineas");
                   if (aerolineas != null) {
                     for (DataAerolinea aer : aerolineas) { %>
                       <option value="<%= aer.getNickname() %>" <%= (aer.getNickname().equals(aerolineaSel)) ? "selected" : "" %>><%= aer.getNombre() %></option>
                <%   }
                   } %>
              </select>
            </div>
            <div class="col-md-4">
              <label for="categoria" class="form-label">Categoría</label>
              <select id="categoria" name="categoria" class="form-select">
                <option value="" selected>Todas</option>
                <% List<DataCategoria> categorias = (List<DataCategoria>) request.getAttribute("categorias");
                   if (categorias != null) {
                     for (DataCategoria cat : categorias) { %>
                       <option value="<%= cat.getNombre() %>"><%= cat.getNombre() %></option>
                <%   }
                   } %>
              </select>
            </div>
            <div class="col-md-4">
              <label for="ruta" class="form-label">Ruta</label>
              <select id="ruta" name="ruta" class="form-select">
                <option value="" selected>Seleccione una ruta</option>
                <% List<DataRuta> rutas = (List<DataRuta>) request.getAttribute("rutas");
                   if (rutas != null) {
                     for (DataRuta ruta : rutas) { %>
                       <option value="<%= ruta.getNombre() %>"><%= ruta.getNombre() %> - <%= ruta.getCiudadOrigen().getNombre() %> a <%= ruta.getCiudadDestino().getNombre() %></option>
                <%   }
                   } %>
              </select>
            </div>
            <div class="col-md-12 mt-2">
              <button type="submit" class="btn btn-primary">Consultar Vuelos</button>
            </div>
          </form>
        </section>
        <!-- Lista de vuelos de la ruta seleccionada -->
        <section>
          <% DataRuta rutaSeleccionada = (DataRuta) request.getAttribute("rutaSeleccionada");
             List<Logica.DataVueloEspecifico> vuelos = (List<Logica.DataVueloEspecifico>) request.getAttribute("vuelos");
             if (rutaSeleccionada != null) { %>
            <h2 class="mt-4">Datos de la ruta: <%= rutaSeleccionada.getNombre() %></h2>
            <div class="mb-3">
              <strong>Origen:</strong> <%= rutaSeleccionada.getCiudadOrigen().getNombre() %> <br>
              <strong>Destino:</strong> <%= rutaSeleccionada.getCiudadDestino().getNombre() %> <br>
              <strong>Descripción:</strong> <%= rutaSeleccionada.getDescripcion() %> <br>
              <strong>Categoría:</strong> <%= rutaSeleccionada.getCategoria() != null ? rutaSeleccionada.getCategoria().getNombre() : "" %> <br>
              <strong>Costo Turista:</strong> <%= rutaSeleccionada.getCostoTurista() %> <br>
              <strong>Costo Ejecutivo:</strong> <%= rutaSeleccionada.getCostoEjecutivo() %> <br>
              <strong>Estado:</strong> <%= rutaSeleccionada.getEstado() != null ? rutaSeleccionada.getEstado().name() : "" %> <br>
              <% /* Imagen de la ruta (no implementado) */ %>
              <% /* if (rutaSeleccionada.getImagen() != null && !rutaSeleccionada.getImagen().isBlank()) { %>
                <img src="<%= rutaSeleccionada.getImagen() %>" alt="Imagen de la ruta" style="max-width:300px;max-height:200px;">
              <% } */ %>
            </div>
            <h3>Vuelos asociados</h3>
            <% if (vuelos != null && !vuelos.isEmpty()) { %>
              <ul class="list-group mt-3">
                <% for (Logica.DataVueloEspecifico vuelo : vuelos) { %>
                  <li class="list-group-item">
                    <strong>Código:</strong> <%= vuelo.getNombre() %> |
                    <strong>Fecha:</strong> <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(vuelo.getFecha()) %>
                  </li>
                <% } %>
              </ul>
            <% } else { %>
              <p class="mt-3">No hay vuelos para esta ruta.</p>
            <% } %>
          <% } %>
        </section>
      </div>
      </main>
    </div>
  </div>
  <!-- FOOTER -->
	<jsp:include page="/WEB-INF/template/footer.jsp" />
  <!-- Bootstrap JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    // Enviar el formulario automáticamente al cambiar la aerolínea
    document.getElementById('aerolinea').addEventListener('change', function() {
      this.form.submit();
    });
  </script>
</body>
</html>