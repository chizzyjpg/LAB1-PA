<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.*" %>
<%@page import="uy.volando.publicar.*" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
    <title>Consulta de Check-in - Volando.uy</title>
</head>
<body>
<jsp:include page="/WEB-INF/template/header.jsp" />
<div class="container-fluid mt-5">
    <div class="row">
        <jsp:include page="/WEB-INF/template/sidebar.jsp" />
        <main class="col-12 col-lg-9 col-xl-10 py-4">
            <div class="container">
                <h2>Consulta de Check-in</h2>
                <hr>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                <% } else if (request.getAttribute("reservasCheckin") != null && ((List)request.getAttribute("reservasCheckin")).size() > 0) {
                    List<DataReserva> reservas = (List<DataReserva>)request.getAttribute("reservasCheckin"); %>
                    <form id="formCheckin" class="mb-4">
                        <div class="mb-3">
                            <label for="reserva" class="form-label">Seleccione una reserva con check-in realizado:</label>
                            <select class="form-select" id="reserva" name="reserva" required>
                                <option value="" disabled selected>Seleccione...</option>
                                <% for (DataReserva r : reservas) { %>
                                    <option value="<%= r.getIdReserva() %>">Reserva #<%= r.getIdReserva() %> - Fecha: <%= r.getFechaReserva() %></option>
                                <% } %>
                            </select>
                        </div>
                    </form>
                    <%-- Renderiza todos los detalles ocultos --%>
                    <% for (DataReserva det : reservas) { %>
                        <div class="detalle-reserva-checkin" id="detalle-<%= det.getIdReserva() %>" style="display:none;">
                            <h4>Check-in de la Reserva #<%= det.getIdReserva() %></h4>
                            <ul class="list-group mx-auto" style="max-width: 500px;">
                                <li class="list-group-item"><b>Cliente:</b> <%= det.getNickCliente().getNombre() %> <%= det.getNickCliente().getApellido() %></li>
                                <li class="list-group-item"><b>Fecha:</b> <%= det.getFechaReserva() %></li>
                                <li class="list-group-item"><b>Tipo Asiento:</b> <%= det.getTipoAsiento() %></li>
                                <li class="list-group-item"><b>Costo Total:</b> <%= det.getCostoTotal() %></li>
                                <li class="list-group-item"><b>Pasajeros y Asientos:</b>
                                    <ul>
                                    <% for (DataPasaje p : det.getPasajes()) { %>
                                        <li><%= p.getNombre() %> <%= p.getApellido() %> - Asiento: <%= p.getAsientoAsignado() %></li>
                                    <% } %>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    <% } %>
                    <script>
                    document.addEventListener('DOMContentLoaded', function() {
                        var select = document.getElementById('reserva');
                        select.addEventListener('change', function() {
                            // Oculta todos los detalles
                            document.querySelectorAll('.detalle-reserva-checkin').forEach(function(div) {
                                div.style.display = 'none';
                            });
                            // Muestra el detalle seleccionado
                            var id = select.value;
                            if (id) {
                                var detalle = document.getElementById('detalle-' + id);
                                if (detalle) detalle.style.display = 'block';
                            }
                        });
                    });
                    </script>
                <% } else { %>
                    <div class="alert alert-info">No se encontró información de check-in para mostrar.</div>
                <% } %>
            </div>
        </main>
    </div>
</div>
<jsp:include page="/WEB-INF/template/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
