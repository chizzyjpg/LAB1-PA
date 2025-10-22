<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,Logica.DataAerolinea,Logica.DataRuta,Logica.DataVueloEspecifico" %>
<!DOCTYPE html>
<html lang="es">
<head>
	<jsp:include page="/WEB-INF/template/head.jsp" />
	<title>Consulta de Usuario - Volando.uy</title>
</head>
<body class="consulta-vuelo">
  <jsp:include page="/WEB-INF/template/header.jsp" />
  <div class="container-fluid mt-5">
    <div class="row">
      <jsp:include page="/WEB-INF/template/sidebar.jsp" />
      <main class="col-12 col-lg-9 col-xl-10 py-4">
        <% String flashErr = (String) session.getAttribute("flash_error");
		   if (flashErr != null) { %>
		  <div class="alert alert-danger alert-dismissible fade show" role="alert">
		    <%= flashErr %>
		    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		  </div>
		<% session.removeAttribute("flash_error"); } %>
        <div class="container">
        <h1 class="text-center mb-4">Consulta de Vuelo</h1>
        <!-- Filtros -->
		<form id="flightFilters" class="row g-2 align-items-end mb-3" method="get" action="consultaVuelo">
		  <div class="col-md-4">
		    <label for="fltAerolinea" class="form-label">Aerolínea</label>
		    <select id="fltAerolinea" name="fltAerolinea" class="form-select" required
		            onchange="document.getElementById('fltRuta').selectedIndex=0; this.form.submit()">
		      <option value="" selected>Elegir…</option>
		      <% List<DataAerolinea> aerolineas = (List<DataAerolinea>) request.getAttribute("aerolineas");
		         String selAer = request.getParameter("fltAerolinea");
		         if (aerolineas != null) {
		           for (DataAerolinea a : aerolineas) { %>
		             <option value="<%= a.getNickname() %>" <%= a.getNickname().equals(selAer) ? "selected" : "" %>><%= a.getNombre() %></option>
		      <%   }
		         } %>
		    </select>
		  </div>
		
		  <div class="col-md-5">
		    <label for="fltRuta" class="form-label">Ruta (Confirmada)</label>
		
		    <% List<DataRuta> rutas = (List<DataRuta>) request.getAttribute("rutas");
		       String selRuta = request.getParameter("fltRuta"); %>
		
		    <select id="fltRuta" name="fltRuta" class="form-select"
		            <%= (selAer == null || selAer.isBlank()) ? "disabled" : "" %>
		            required onchange="this.form.submit()">
		
		      <% if (selAer == null || selAer.isBlank()) { %>
		        <option value="" selected>Elegir aerolínea primero…</option>
		      <% } else { %>
		        <option value="" <%= (selRuta == null || selRuta.isBlank()) ? "selected" : "" %> >-- Seleccione ruta --</option>
		      <% } %>
		
		      <% if (rutas != null) {
		           for (DataRuta r : rutas) { %>
		        <option value="<%= r.getNombre() %>" <%= (r.getNombre().equals(selRuta) ? "selected" : "") %>><%= r.getNombre() %></option>
		      <% } } %>
		    </select>
		  </div>		
		</form>
        <!-- Lista de vuelos -->
        <div id="flightList" class="list-group">
        <% List<DataVueloEspecifico> vuelos = (List<DataVueloEspecifico>) request.getAttribute("vuelos");
           String selVuelo = request.getParameter("codigoVuelo");
           if (vuelos != null && !vuelos.isEmpty()) {
             for (DataVueloEspecifico v : vuelos) {
               // Filtro por texto si corresponde
               String filtro = request.getParameter("fltTexto");
               boolean mostrar = true;
               if (filtro != null && !filtro.isBlank()) {
                 mostrar = v.getNombre().toLowerCase().contains(filtro.toLowerCase()) ||
                           (v.getDRuta() != null && (
                             v.getDRuta().getCiudadOrigen().getNombre().toLowerCase().contains(filtro.toLowerCase()) ||
                             v.getDRuta().getCiudadDestino().getNombre().toLowerCase().contains(filtro.toLowerCase())
                           ));
               }
               if (mostrar) { %>
                 <form method="get" action="consultaVuelo" style="display:inline;">
                   <input type="hidden" name="fltAerolinea" value="<%= selAer %>">
                   <input type="hidden" name="fltRuta" value="<%= selRuta %>">
                   <input type="hidden" name="codigoVuelo" value="<%= v.getNombre() %>">
                   <button type="submit" class="list-group-item list-group-item-action <%= v.getNombre().equals(selVuelo) ? "active" : "" %>">
                     <%= v.getNombre() %> - <%= v.getDRuta() != null ? v.getDRuta().getCiudadOrigen().getNombre() : "?" %> → <%= v.getDRuta() != null ? v.getDRuta().getCiudadDestino().getNombre() : "?" %> (<%= v.getFecha() %>)
                   </button>
                 </form>
        <%     }
             }
           } else if (vuelos != null) { %>
             <div class="list-group-item">No hay vuelos para esta ruta.</div>
        <% } %>
        </div>
        <!-- Detalle de vuelo -->
        <% DataVueloEspecifico detalle = (DataVueloEspecifico) request.getAttribute("detalleVuelo");
           List reservasVuelo = (List) request.getAttribute("reservasVuelo");
           Object reservaCliente = request.getAttribute("reservaCliente");
           Object detalleReserva = request.getAttribute("detalleReserva");
           if (detalle != null) { %>
           <div class="card mt-4">
             <div class="card-header">Detalle de vuelo</div>
             <div class="card-body">
               <h5 class="card-title"><%= detalle.getNombre() %></h5>
               <p class="card-text">
                 <strong>Origen:</strong> <%= detalle.getDRuta() != null && detalle.getDRuta().getCiudadOrigen() != null ? detalle.getDRuta().getCiudadOrigen().getNombre() : "No disponible" %><br>
                 <strong>Destino:</strong> <%= detalle.getDRuta() != null && detalle.getDRuta().getCiudadDestino() != null ? detalle.getDRuta().getCiudadDestino().getNombre() : "No disponible" %><br>
                 <strong>Fecha salida:</strong> <%= detalle.getFecha() %><br>
                 <strong>Estado:</strong> <%= detalle.getDRuta() != null && detalle.getDRuta().getEstado() != null ? detalle.getDRuta().getEstado() : "No disponible" %>
               </p>
               <% if (reservasVuelo != null) { %>
                 <div style="color: red; font-weight: bold;">Reservas recibidas: <%= reservasVuelo.size() %></div>
               <% } %>
               <%-- Mostrar reservas si el usuario es aerolínea dueña --%>
               <% if (reservasVuelo != null && !reservasVuelo.isEmpty()) { %>
                 <h6>Reservas del vuelo:</h6>
                 <ul class="list-group mb-3">
                 <% for (Object obj : reservasVuelo) {
                      Logica.DataReserva r = (Logica.DataReserva) obj; %>
                   <li class="list-group-item">
                     <form method="get" action="consultaVuelo" style="display:inline;">
                       <input type="hidden" name="fltAerolinea" value="<%= request.getParameter("fltAerolinea") %>">
                       <input type="hidden" name="fltRuta" value="<%= request.getParameter("fltRuta") %>">
                       <input type="hidden" name="codigoVuelo" value="<%= detalle.getNombre() %>">
                       <input type="hidden" name="idReserva" value="<%= r.getIdReserva() %>">
                       <button type="submit" class="btn btn-link">Reserva #<%= r.getIdReserva() %> - <%= r.getNickCliente() != null ? r.getNickCliente().getNombre() : "" %></button>
                     </form>
                   </li>
                 <% } %>
                 </ul>
               <% } %>
               <%-- Mostrar reserva del cliente si corresponde --%>
               <% if (reservaCliente != null) {
                    Logica.DataReserva r = (Logica.DataReserva) reservaCliente; %>
                 <h6>Tu reserva en este vuelo:</h6>
                 <form method="get" action="consultaVuelo">
                   <input type="hidden" name="fltAerolinea" value="<%= request.getParameter("fltAerolinea") %>">
                   <input type="hidden" name="fltRuta" value="<%= request.getParameter("fltRuta") %>">
                   <input type="hidden" name="codigoVuelo" value="<%= detalle.getNombre() %>">
                   <input type="hidden" name="idReserva" value="<%= r.getIdReserva() %>">
                   <button type="submit" class="btn btn-link">Reserva #<%= r.getIdReserva() %> - <%= r.getNickCliente() != null ? r.getNickCliente().getNombre() : "" %></button>
                 </form>
               <% } %>
               <%-- Mostrar detalle de reserva si corresponde --%>
               <% if (detalleReserva != null) {
                    Logica.DataReserva r = (Logica.DataReserva) detalleReserva; %>
                 <div class="card mt-3">
                   <div class="card-header">Detalle de Reserva</div>
                   <div class="card-body">
                     <p>
                       <strong>ID:</strong> <%= r.getIdReserva() %><br>
                       <strong>Cliente:</strong> <%= r.getNickCliente() != null ? r.getNickCliente().getNombre() : "" %> (<%= r.getNickCliente() != null ? r.getNickCliente().getNickname() : "" %>)<br>
                       <strong>Fecha:</strong> <%= r.getFechaReserva() %>
                     </p>
                   </div>
                 </div>
               <% } %>
             </div>
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