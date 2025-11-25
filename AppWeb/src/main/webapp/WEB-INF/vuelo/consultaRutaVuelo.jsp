<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="uy.volando.publicar.DataRuta" %>
<%@page import="uy.volando.publicar.DataAerolinea" %>
<%@page import="uy.volando.publicar.DataCategoria" %>
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
             List<uy.volando.publicar.DataVueloEspecifico> vuelos = (List<uy.volando.publicar.DataVueloEspecifico>) request.getAttribute("vuelos");
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
              <% /* if (rutaSeleccionada.getImagen() != null && !rutaSeleccionada.getImagen().isBlank()) { */%>
                <!--<img src="<%/*= rutaSeleccionada.getImagen()*/ %>" alt="Imagen de la ruta" style="max-width:300px;max-height:200px;">-->
              <% /*} */ %>

              <%-- BLOQUE NUEVO: Video asociado a la ruta (si existe URL) --%>
              <%
                String videoUrl = null;
                try {
                    videoUrl = rutaSeleccionada.getVideoUrl();
                } catch (Exception e) {
                    videoUrl = null; // por si el stub aún no tiene el método, no romper la página
                }
                if (videoUrl != null) {
                    videoUrl = videoUrl.trim();
                }
                if (videoUrl != null && !videoUrl.isEmpty()) {
              %>
                <div class="mt-3">
                  <strong>Video asociado:</strong><br>
                  <% if (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be")) { %>
                    <div class="ratio ratio-16x9" style="max-width: 640px;">
                      <iframe
                        src="<%= videoUrl %>"
                        title="Video de la ruta"
                        frameborder="0"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                        allowfullscreen></iframe>
                    </div>
                  <% } else { %>
                    <video class="w-100" style="max-width:640px;" controls>
                      <source src="<%= videoUrl %>" type="video/mp4" />
                      Tu navegador no soporta video HTML5.
                    </video>
                  <% } %>
                </div>
              <%
                }
              %>
            </div>
            <h3>Vuelos asociados</h3>
            <% if (vuelos != null && !vuelos.isEmpty()) { %>
              <ul class="list-group mt-3">
                <% for (uy.volando.publicar.DataVueloEspecifico vuelo : vuelos) { %>
                  <li class="list-group-item">
                    <form method="post" style="display:inline;">
                      <input type="hidden" name="aerolinea" value="<%= rutaSeleccionada.getNicknameAerolinea() %>" />
                      <input type="hidden" name="ruta" value="<%= rutaSeleccionada.getNombre() %>" />
                      <input type="hidden" name="vuelo" value="<%= vuelo.getNombre() %>" />
                      <button type="submit" class="btn btn-link p-0" style="text-decoration:underline;">
                        <strong>Código:</strong> <%= vuelo.getNombre() %> |
                        <strong>Fecha:</strong>
                        <%
                          java.text.SimpleDateFormat sdfLista = new java.text.SimpleDateFormat("dd/MM/yyyy");
                          String fechaListaStr = "";
                          // vuelo.getFecha() es XMLGregorianCalendar, hay que pasar a java.util.Date de forma segura
                          if (vuelo.getFecha() != null) {
                              try {
                                  javax.xml.datatype.XMLGregorianCalendar xc = vuelo.getFecha();
                                  java.util.Date fechaJava = xc.toGregorianCalendar().getTime();
                                  fechaListaStr = sdfLista.format(fechaJava);
                              } catch (Exception e) {
                                  fechaListaStr = ""; // si algo raro viene en la fecha, no romper la página
                              }
                          }
                        %>
                        <%= fechaListaStr %>
                      </button>
                    </form>
                  </li>
                <% } %>
              </ul>
            <% } else { %>
              <p class="mt-3">No hay vuelos para esta ruta.</p>
            <% } %>
          <% } %>
        </section>
        <% uy.volando.publicar.DataVueloEspecifico vueloSeleccionado = (uy.volando.publicar.DataVueloEspecifico) request.getAttribute("vueloSeleccionado");
           if (vueloSeleccionado != null) { %>
        <section class="mt-4">
          <h3>Detalle del Vuelo Seleccionado</h3>
          <ul class="list-group">
            <li class="list-group-item"><strong>Código:</strong> <%= vueloSeleccionado.getNombre() %></li>
            <li class="list-group-item"><strong>Fecha:</strong>
              <%
                java.text.SimpleDateFormat sdfDet = new java.text.SimpleDateFormat("dd/MM/yyyy");
                String fechaDetStr = "";
                if (vueloSeleccionado.getFecha() != null) {
                    try {
                        javax.xml.datatype.XMLGregorianCalendar xcSel = vueloSeleccionado.getFecha();
                        java.util.Date fechaJavaSel = xcSel.toGregorianCalendar().getTime();
                        fechaDetStr = sdfDet.format(fechaJavaSel);
                    } catch (Exception e) {
                        fechaDetStr = "";
                    }
                }
              %>
              <%= fechaDetStr %>
            </li>
            <li class="list-group-item"><strong>Duración:</strong> <%= vueloSeleccionado.getDuracion() %> minutos</li>
            <li class="list-group-item"><strong>Max. Asientos Turista:</strong> <%= vueloSeleccionado.getMaxAsientosTur() %></li>
            <li class="list-group-item"><strong>Max. Asientos Ejecutivo:</strong> <%= vueloSeleccionado.getMaxAsientosEjec() %></li>
            <li class="list-group-item"><strong>Fecha de Alta:</strong>
              <%
                java.text.SimpleDateFormat sdfAlta = new java.text.SimpleDateFormat("dd/MM/yyyy");
                String fechaAltaStr = "";
                if (vueloSeleccionado.getFechaAlta() != null) {
                    try {
                        javax.xml.datatype.XMLGregorianCalendar xcAlta = vueloSeleccionado.getFechaAlta();
                        java.util.Date fechaAltaJava = xcAlta.toGregorianCalendar().getTime();
                        fechaAltaStr = sdfAlta.format(fechaAltaJava);
                    } catch (Exception e) {
                        fechaAltaStr = "";
                    }
                }
              %>
              <%= fechaAltaStr %>
            </li>
            <!-- Agrega aquí más campos si es necesario -->
          </ul>
        </section>
        <% } %>
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