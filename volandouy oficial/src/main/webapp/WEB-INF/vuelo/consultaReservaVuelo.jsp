<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.*" %>
<%@page import="Logica.*" %>
<!DOCTYPE html>
<html lang="es">
<head>
	<jsp:include page="/WEB-INF/template/head.jsp" />
	<title>Consulta de Reserva de Vuelo - Volando.uy</title>
</head>
<body class="consultaUsuario-page">
  <jsp:include page="/WEB-INF/template/header.jsp" />
  <div class="container-fluid mt-5">
    <div class="row">
      <jsp:include page="/WEB-INF/template/sidebar.jsp" />
      <main class="col-12 col-lg-9 col-xl-10 py-4">
        <div class="container-fluid text-center">
          <h2>Consulta de Reserva de Vuelo</h2>
          <hr>

<%
String rol = (String) request.getAttribute("rol");
String aerolineaSeleccionada = (String) request.getAttribute("aerolineaSeleccionada");
String rutaSeleccionada = (String) request.getAttribute("rutaSeleccionada");
String vueloSeleccionado = (String) request.getAttribute("vueloSeleccionado");
%>

<%-- Paso 1: Selección de Aerolínea (solo cliente) --%>
<% if ("cliente".equals(rol) && request.getAttribute("aerolineas") != null) { %>
  <form method="get" class="mb-4">
    <div class="row justify-content-center align-items-end">
      <div class="col-md-4 mb-2">
        <label for="aerolinea">Seleccione una aerolínea:</label>
        <select name="aerolinea" id="aerolinea" class="form-select" required>
          <option value="" disabled selected>Seleccionar…</option>
          <% for (DataAerolinea a : (List<DataAerolinea>)request.getAttribute("aerolineas")) { %>
            <option value="<%= a.getNickname() %>"><%= a.getNombre() %></option>
          <% } %>
        </select>
      </div>
      <div class="col-md-2 mb-2 d-flex align-items-end">
        <button type="submit" class="btn btn-primary w-100">Siguiente</button>
      </div>
    </div>
  </form>
<% } %>

<%-- Paso 2 y 3: Selección de Ruta y Vuelo en la misma pantalla --%>
<% if (request.getAttribute("rutas") != null) { %>
  <form method="get" class="mb-4">
    <% if (aerolineaSeleccionada != null) { %>
      <input type="hidden" name="aerolinea" value="<%= aerolineaSeleccionada %>" />
    <% } %>
    <div class="row justify-content-center align-items-end">
      <div class="col-md-4 mb-2">
        <label for="ruta">Seleccione una ruta:</label>
        <select name="ruta" id="ruta" class="form-select" required>
          <option value="" disabled <%= (rutaSeleccionada == null) ? "selected" : "" %>>Seleccionar…</option>
          <% for (DataRuta r : (List<DataRuta>)request.getAttribute("rutas")) { %>
            <option value="<%= r.getNombre() %>" <%= (rutaSeleccionada != null && rutaSeleccionada.equals(r.getNombre())) ? "selected" : "" %>><%= r.getNombre() %></option>
          <% } %>
        </select>
      </div>
      <% if (request.getAttribute("vuelos") != null) { %>
      <div class="col-md-4 mb-2">
        <label for="vuelo">Seleccione un vuelo:</label>
        <select name="vuelo" id="vuelo" class="form-select" required>
          <option value="" disabled <%= (vueloSeleccionado == null) ? "selected" : "" %>>Seleccionar…</option>
          <% for (DataVueloEspecifico v : (List<DataVueloEspecifico>)request.getAttribute("vuelos")) { %>
            <option value="<%= v.getNombre() %>" <%= (vueloSeleccionado != null && vueloSeleccionado.equals(v.getNombre())) ? "selected" : "" %>><%= v.getNombre() %> - <%= v.getFecha() %></option>
          <% } %>
        </select>
      </div>
      <div class="col-md-2 mb-2 d-flex align-items-end">
        <button type="submit" class="btn btn-primary w-100">Siguiente</button>
      </div>
      <% } %>
    </div>
  </form>
<% } %>

<%-- Paso 4: Mostrar reservas (aerolínea) o mi reserva (cliente) --%>
<% if ("aerolinea".equals(rol) && request.getAttribute("reservas") != null) { %>
  <h4>Reservas del vuelo</h4>
  <% List<DataReserva> reservas = (List<DataReserva>)request.getAttribute("reservas"); %>
  <% if (reservas.isEmpty()) { %>
    <div class="alert alert-warning">No hay reservas para este vuelo.</div>
  <% } else { %>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>ID</th>
        <th>Cliente</th>
        <th>Fecha</th>
        <th>Tipo Asiento</th>
        <th>Costo Total</th>
        <th>Detalle</th>
      </tr>
    </thead>
    <tbody>
      <% for (DataReserva r : reservas) { %>
        <tr>
          <td><%= r.getIdReserva() %></td>
          <td><%= r.getNickCliente().getNombre() %> <%= r.getNickCliente().getApellido() %></td>
          <td><%= r.getFechaReserva() %></td>
          <td><%= r.getTipoAsiento() %></td>
          <td><%= r.getCostoTotal() %></td>
          <td>
            <form method="get" style="display:inline">
              <input type="hidden" name="aerolinea" value="<%= aerolineaSeleccionada %>" />
              <input type="hidden" name="ruta" value="<%= rutaSeleccionada %>" />
              <input type="hidden" name="vuelo" value="<%= vueloSeleccionado %>" />
              <input type="hidden" name="reserva" value="<%= r.getIdReserva() %>" />
              <button type="submit" class="btn btn-sm btn-info">Ver detalle</button>
            </form>
          </td>
        </tr>
      <% } %>
    </tbody>
  </table>
  <% } %>
<% } %>

<% if ("cliente".equals(rol) && vueloSeleccionado != null) { 
     DataReserva miReserva = (DataReserva)request.getAttribute("miReserva");
     List<DataReserva> reservasCliente = (List<DataReserva>)request.getAttribute("reservasCliente");
     if (miReserva != null) { %>
  <h4>Mi reserva</h4>
  <div class="card mx-auto" style="max-width: 500px;">
    <div class="card-body">
      <h5 class="card-title">Reserva #<%= miReserva.getIdReserva() %></h5>
      <p class="card-text">
        Fecha: <%= miReserva.getFechaReserva() %><br>
        Tipo Asiento: <%= miReserva.getTipoAsiento() %><br>
        Costo Total: <%= miReserva.getCostoTotal() %>
      </p>
      <form method="get">
        <input type="hidden" name="aerolinea" value="<%= aerolineaSeleccionada %>" />
        <input type="hidden" name="ruta" value="<%= rutaSeleccionada %>" />
        <input type="hidden" name="vuelo" value="<%= vueloSeleccionado %>" />
        <input type="hidden" name="reserva" value="<%= miReserva.getIdReserva() %>" />
        <button type="submit" class="btn btn-info">Ver detalle</button>
      </form>
    </div>
  </div>
<%   } else if (reservasCliente != null && reservasCliente.isEmpty()) { %>
  <div class="alert alert-warning">No hay reservas para este vuelo.</div>
<%   } else if (reservasCliente != null) { %>
  <div class="alert alert-warning">No tienes reserva para este vuelo.</div>
<%   } 
   } %>

<%-- Paso 5: Detalle de reserva --%>
<% if (request.getAttribute("detalleReserva") != null) { 
     DataReserva det = (DataReserva)request.getAttribute("detalleReserva"); %>
  <h4>Detalle de la Reserva #<%= det.getIdReserva() %></h4>
  <ul class="list-group mx-auto" style="max-width: 500px;">
    <li class="list-group-item"><b>Cliente:</b> <%= det.getNickCliente().getNombre() %> <%= det.getNickCliente().getApellido() %></li>
    <li class="list-group-item"><b>Fecha:</b> <%= det.getFechaReserva() %></li>
    <li class="list-group-item"><b>Tipo Asiento:</b> <%= det.getTipoAsiento() %></li>
    <li class="list-group-item"><b>Costo Total:</b> <%= det.getCostoTotal() %></li>
    <li class="list-group-item"><b>Pasajeros:</b>
      <ul>
        <% for (DataPasaje p : det.getPasajes()) { %>
          <li><%= p.getNombre() %> <%= p.getApellido() %></li>
        <% } %>
      </ul>
    </li>
  </ul>
  <form method="get" class="mt-3">
    <% if (aerolineaSeleccionada != null) { %>
      <input type="hidden" name="aerolinea" value="<%= aerolineaSeleccionada %>" />
    <% } %>
    <% if (rutaSeleccionada != null) { %>
      <input type="hidden" name="ruta" value="<%= rutaSeleccionada %>" />
    <% } %>
    <% if (vueloSeleccionado != null) { %>
      <input type="hidden" name="vuelo" value="<%= vueloSeleccionado %>" />
    <% } %>
    <button type="submit" class="btn btn-secondary">Volver a reservas</button>
  </form>
<% } %>

        </div>
      </main>
    </div>
  </div>
  <jsp:include page="/WEB-INF/template/footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    // Al cambiar la ruta, limpia el select de vuelo (si existe), lo deshabilita y envía el formulario
    document.addEventListener('DOMContentLoaded', function() {
      var rutaSelect = document.getElementById('ruta');
      if (rutaSelect) {
        rutaSelect.addEventListener('change', function() {
          var form = rutaSelect.form;
          var vueloSelect = document.getElementById('vuelo');
          if (vueloSelect) {
            vueloSelect.selectedIndex = 0;
            vueloSelect.disabled = true; // Así no se envía el valor anterior
          }
          form.submit();
        });
      }
    });
  </script>
</body>
</html>