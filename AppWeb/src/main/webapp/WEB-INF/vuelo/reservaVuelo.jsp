<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="uy.volando.publicar.DataAerolinea" %>
<%@page import="uy.volando.publicar.DataRuta" %>
<%@page import="uy.volando.publicar.DataVueloEspecifico" %>
<%@page import="uy.volando.publicar.DataReserva" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <jsp:include page="/WEB-INF/template/head.jsp" />
  <title>Reserva de Vuelo - Volando.uy</title>
</head>
<body class="consultaUsuario-page">
  <!-- NAVBAR -->
  <jsp:include page="/WEB-INF/template/header.jsp" />
  
  <div class="container-fluid mt-5">
    <div class="row">
      <jsp:include page="/WEB-INF/template/sidebar.jsp" />
      <main class="col-12 col-lg-9 col-xl-10 py-4">
        <div class="container-fluid text-center">
       		<% String flashErr = (String) session.getAttribute("flash_error");
  			if (flashErr != null) { %>
 			<div class="alert alert-danger alert-dismissible fade show" role="alert">
   			<%= flashErr %>
   			<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
 			</div>
 			<% session.removeAttribute("flash_error"); } %>
          <h1 class="mb-2">Reservar vuelo</h1>
          <p class="text-muted">Elegí aerolínea y ruta; luego seleccioná tu vuelo.</p>

          <!-- Formulario de selección progresiva -->
          <form method="get" action="reservaVuelo" class="row g-3 align-items-end mb-3">
            <div class="col-12 col-md-4">
              <label for="selAerolinea" class="form-label">Aerolínea</label>
              <select id="selAerolinea" name="aerolinea" class="form-select" required onchange="document.getElementById('selRuta').selectedIndex=0;
                  document.getElementById('selVuelo').selectedIndex=0;
                  this.form.submit()">
                <option value="" selected disabled>Seleccionar…</option>
                <% List<DataAerolinea> aerolineas = (List<DataAerolinea>) request.getAttribute("aerolineas");
                   String aerolineaSel = request.getParameter("aerolinea");
                   if (aerolineas != null) {
                     for (DataAerolinea a : aerolineas) {
                %>
                  <option value="<%= a.getNickname() %>" <%= a.getNickname().equals(aerolineaSel) ? "selected" : "" %>><%= a.getNombre() %></option>
                <%   }
                   }
                %>
              </select>
            </div>
            <div class="col-12 col-md-5">
              <label for="selRuta" class="form-label">Ruta</label>
              <select id="selRuta" name="ruta" class="form-select" <%= (aerolineaSel == null || aerolineaSel.isEmpty()) ? "disabled" : "" %> onchange="document.getElementById('selVuelo').selectedIndex=0; this.form.submit()">
                <option value="" selected disabled>Seleccionar…</option>
                <% List<DataRuta> rutas = (List<DataRuta>) request.getAttribute("rutas");
                   String rutaSel = request.getParameter("ruta");
                   if (rutas != null) {
                     for (DataRuta r : rutas) {
                %>
                  <option value="<%= r.getNombre() %>" <%= r.getNombre().equals(rutaSel) ? "selected" : "" %>><%= r.getNombre() %></option>
                <%   }
                   }
                %>
              </select>
            </div>
            <div class="col-12 col-md-3">
              <label for="selVuelo" class="form-label">Vuelo</label>
              <!-- DEBUG: Mostrar valores de aerolineaSel y rutaSel -->
              <p style="color:blue;">DEBUG: aerolineaSel: <%= aerolineaSel %> | rutaSel: <%= rutaSel %></p>
              <!-- DEBUG: Mostrar cantidad de vuelos recibidos -->
              <% List<DataVueloEspecifico> vuelos = (List<DataVueloEspecifico>) request.getAttribute("vuelos");
                 String vueloSel = request.getParameter("vuelo");
              %>
              <p style="color:red;">DEBUG: Vuelos recibidos: <%= (vuelos != null ? vuelos.size() : 0) %></p>
              <% if (vuelos != null) {
                   for (DataVueloEspecifico v : vuelos) {
                       String fechaStr = "";
                       Object fechaObj = v.getFecha();
                       if (fechaObj != null) {
                           try {
                               if (fechaObj instanceof javax.xml.datatype.XMLGregorianCalendar) {
                                   java.util.Date fechaDate = ((javax.xml.datatype.XMLGregorianCalendar) fechaObj).toGregorianCalendar().getTime();
                                   java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                                   fechaStr = sdf.format(fechaDate);
                               } else if (fechaObj instanceof java.util.Date) {
                                   java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                                   fechaStr = sdf.format((java.util.Date) fechaObj);
                               } else {
                                   fechaStr = fechaObj.toString();
                               }
                           } catch (Exception e) {
                               fechaStr = fechaObj.toString();
                           }
                       }
              %>
                <p style="color:red;">DEBUG: Vuelo: <%= v.getNombre() %> - Fecha: <%= fechaStr %></p>
              <%   }
                 }
              %>
              <select id="selVuelo" name="vuelo" class="form-select" <%= rutaSel == null || rutaSel.isEmpty() ? "disabled" : "" %> onchange="this.form.submit()">
                <option value="" selected disabled>Seleccionar…</option>
                <% if (vuelos != null) {
                     for (DataVueloEspecifico v : vuelos) {
                         String fechaStr = "";
                         Object fechaObj = v.getFecha();
                         if (fechaObj != null) {
                             try {
                                 if (fechaObj instanceof javax.xml.datatype.XMLGregorianCalendar) {
                                     java.util.Date fechaDate = ((javax.xml.datatype.XMLGregorianCalendar) fechaObj).toGregorianCalendar().getTime();
                                     java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                                     fechaStr = sdf.format(fechaDate);
                                 } else if (fechaObj instanceof java.util.Date) {
                                     java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                                     fechaStr = sdf.format((java.util.Date) fechaObj);
                                 } else {
                                     fechaStr = fechaObj.toString();
                                 }
                             } catch (Exception e) {
                                 fechaStr = fechaObj.toString();
                             }
                         }
                %>
                  <option value="<%= v.getNombre() %>" <%= v.getNombre().equals(vueloSel) ? "selected" : "" %>><%= v.getNombre() %> - <%= fechaStr %></option>
                <%   }
                   }
                %>
              </select>
            </div>
          </form>

          <hr>

          <!-- Detalle del vuelo seleccionado -->
          <% DataVueloEspecifico vueloDetalle = (DataVueloEspecifico) request.getAttribute("vueloDetalle");
             if (vueloDetalle != null) { 
               String fechaStr = "";
               Object fechaObj = vueloDetalle.getFecha();
               if (fechaObj != null) {
                   try {
                       if (fechaObj instanceof javax.xml.datatype.XMLGregorianCalendar) {
                           java.util.Date fechaDate = ((javax.xml.datatype.XMLGregorianCalendar) fechaObj).toGregorianCalendar().getTime();
                           java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                           fechaStr = sdf.format(fechaDate);
                       } else if (fechaObj instanceof java.util.Date) {
                           java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                           fechaStr = sdf.format((java.util.Date) fechaObj);
                       } else {
                           fechaStr = fechaObj.toString();
                       }
                   } catch (Exception e) {
                       fechaStr = fechaObj.toString();
                   }
               }
               DataRuta rutaDetalle = vueloDetalle.getDruta();
               String costoTurista = rutaDetalle != null && rutaDetalle.getCostoTurista() != null ? rutaDetalle.getCostoTurista().toString() : "N/A";
               String costoEjecutivo = rutaDetalle != null && rutaDetalle.getCostoEjecutivo() != null ? rutaDetalle.getCostoEjecutivo().toString() : "N/A";
          %>
            <div class="card mb-4 mx-auto" style="max-width: 600px;">
              <div class="card-header">Detalle del vuelo</div>
              <div class="card-body">
                <p><strong>Vuelo:</strong> <%= vueloDetalle.getNombre() %></p>
                <p><strong>Fecha salida:</strong> <%= fechaStr %></p>
                <p><strong>Duración:</strong> <%= vueloDetalle.getDuracion() %> hs</p>
                <p><strong>Asientos turista:</strong> <%= vueloDetalle.getMaxAsientosTur() %> | <strong>Ejecutivo:</strong> <%= vueloDetalle.getMaxAsientosEjec() %></p>
                <p><strong>Precio turista:</strong> $<%= costoTurista %> | <strong>Ejecutivo:</strong> $<%= costoEjecutivo %></p>
              </div>
            </div>

            <!-- Formulario de reserva -->
            <form method="post" action="reservaVuelo" class="row g-3 justify-content-center">
              <input type="hidden" name="aerolinea" value="<%= aerolineaSel %>" />
              <input type="hidden" name="ruta" value="<%= rutaSel %>" />
              <input type="hidden" name="vuelo" value="<%= vueloSel %>" />
              <div class="col-12 col-md-4">
                <label class="form-label" for="tipoAsiento">Tipo de asiento</label>
                <select id="tipoAsiento" name="tipoAsiento" class="form-select" required>
                  <option value="TURISTA">Turista</option>
                  <option value="EJECUTIVO">Ejecutivo</option>
                </select>
              </div>
              <div class="col-6 col-md-4">
                <label class="form-label" for="cantidadPasajes">Cantidad de pasajes</label>
                <input id="cantidadPasajes" name="cantidadPasajes" class="form-control" type="number" min="1" value="1" required>
              </div>
              <div class="col-6 col-md-4">
                <label class="form-label" for="equipajeExtra">Unidades de equipaje extra</label>
                <input id="equipajeExtra" name="equipajeExtra" class="form-control" type="number" min="0" value="0">
              </div>
              <div class="col-6 col-md-4">
                <label class="form-label" for="equipajeTipo">Tipo de equipaje</label>
                <select id="equipajeTipo" name="equipajeTipo" class="form-select" required>
                  <option value="BOLSO">Bolso</option>
                  <option value="MOCHILAYCARRYON">Mochila y Carry-on</option>
                </select>
              </div>
              <div class="col-12">
                <label class="form-label">Pasajeros (nombre y apellido)</label>
                <div id="pasajerosFields"></div>
                <small class="text-muted">Ingresá nombre y apellido para cada pasajero.</small>
              </div>
              <div class="col-12">
                <label class="form-label d-block">Forma de pago</label>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="formaPago" id="pagoGeneral" value="general" checked>
                  <label class="form-check-label" for="pagoGeneral">Pago general</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="formaPago" id="pagoPaquete" value="paquete">
                  <label class="form-check-label" for="pagoPaquete">Usar paquete</label>
                </div>
                <div id="paqueteSelectContainer" style="display:none; margin-top:10px;">
                  <label for="paqueteSelect" class="form-label">Seleccionar paquete</label>
                  <select id="paqueteSelect" name="paquete" class="form-select">
                    <option value="" selected disabled>Seleccionar paquete…</option>
                    <% List<uy.volando.publicar.DataPaquete> paquetes = (List<uy.volando.publicar.DataPaquete>) request.getAttribute("paquetesDisponibles");
                       if (paquetes != null) {
                         for (uy.volando.publicar.DataPaquete p : paquetes) {
                    %>
                      <option value="<%= p.getNombre() %>"><%= p.getNombre() %> (Rutas: <%= p.getCantRutas() %>)</option>
                    <%   }
                       }
                    %>
                  </select>
                </div>
              </div>
              <div class="col-12">
                <button type="submit" class="btn btn-primary">Confirmar reserva</button>
              </div>
            </form>
          <% } %>

          <!-- Mensajes de éxito y error -->
          <% if (request.getAttribute("exito") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("exito") %></div>
          <% } %>
          <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
          <% } %>
        </div>
      </main>
    </div>
  </div>
  <jsp:include page="/WEB-INF/template/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script>
document.addEventListener('DOMContentLoaded', function() {
  var cantidadInput = document.getElementById('cantidadPasajes');
  var pasajerosFields = document.getElementById('pasajerosFields');
  function renderPasajerosFields() {
    var cantidad = parseInt(cantidadInput.value) || 1;
    pasajerosFields.innerHTML = '';
    for (var i = 1; i <= cantidad; i++) {
      var div = document.createElement('div');
      div.className = 'input-group mb-2';
      div.innerHTML = '<span class="input-group-text">Pasajero ' + i + '</span>' +
        '<input type="text" name="nombrePasajero' + i + '" class="form-control" placeholder="Nombre" required>' +
        '<input type="text" name="apellidoPasajero' + i + '" class="form-control" placeholder="Apellido" required>';
      pasajerosFields.appendChild(div);
    }
  }
  cantidadInput.addEventListener('input', renderPasajerosFields);
  renderPasajerosFields(); // inicializa al cargar

  var pagoGeneral = document.getElementById('pagoGeneral');
  var pagoPaquete = document.getElementById('pagoPaquete');
  var paqueteSelectContainer = document.getElementById('paqueteSelectContainer');
  function togglePaqueteSelect() {
    paqueteSelectContainer.style.display = pagoPaquete.checked ? 'block' : 'none';
  }
  pagoGeneral.addEventListener('change', togglePaqueteSelect);
  pagoPaquete.addEventListener('change', togglePaqueteSelect);
  togglePaqueteSelect();
});
</script>
</body>
</html>