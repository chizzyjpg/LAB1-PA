<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
    <title>Check-in de Reserva de Vuelo - Volando.uy</title>
</head>
<body class="checkinReservaVuelo-page">
    <!-- NAVBAR -->
    <jsp:include page="/WEB-INF/template/header.jsp" />
    <div class="container-fluid mt-5">
        <div class="row">
            <!-- SIDEBAR (visible en ≥ lg) -->
            <jsp:include page="/WEB-INF/template/sidebar.jsp" />
            <!-- CONTENIDO PRINCIPAL -->
            <main class="col-12 col-lg-9 col-xl-10 py-4">
                <div class="container">
                    <!-- Mensaje de feedback -->
                    <% if (request.getAttribute("mensaje") != null) { %>
                        <div class="alert <%= (Boolean.TRUE.equals(request.getAttribute("exito"))) ? "alert-success" : "alert-danger" %>" role="alert">
                            <%= request.getAttribute("mensaje") %>
                        </div>
                    <% } %>
                    <h1 class="text-center mb-4">Check-in de Reserva de Vuelo</h1>
                    <!-- Listado de reservas pendientes de check-in -->
                    <section class="mb-4">
                        <%
                        java.util.List<uy.volando.publicar.DataReserva> reservas = (java.util.List<uy.volando.publicar.DataReserva>) request.getAttribute("reservasPendientesCheckIn");
                        String reservaSeleccionada = request.getParameter("reservaSeleccionada");
                        uy.volando.publicar.DataReserva detalleReserva = (uy.volando.publicar.DataReserva) request.getAttribute("detalleReserva");
                        System.out.println("DEBUG JSP reservas size: " + (reservas != null ? reservas.size() : "null"));
                        if (reservas != null) {
                            for (uy.volando.publicar.DataReserva res : reservas) {
                                System.out.println("DEBUG JSP reserva id: " + res.getIdReserva() + ", fecha: " + res.getFechaReserva());
                            }
                        }
                        if (reservas != null && !reservas.isEmpty()) { %>
                            <form method="get" class="row g-3">
                                <div class="col-12 mb-3">
                                    <label for="reservaSeleccionada" class="form-label">Seleccione una reserva para ver el detalle y hacer check-in:</label>
                                    <select id="reservaSeleccionada" name="reservaSeleccionada" class="form-select" required onchange="this.form.submit()">
                                        <option value="" <%= (reservaSeleccionada == null || reservaSeleccionada.isEmpty()) ? "selected" : "" %> disabled>Seleccione una reserva</option>
                                        <% for (uy.volando.publicar.DataReserva res : reservas) { %>
                                            <option value="<%= res.getIdReserva() %>" <%= (reservaSeleccionada != null && reservaSeleccionada.equals(String.valueOf(res.getIdReserva()))) ? "selected" : "" %>>
                                                Reserva #<%= res.getIdReserva() %> - Fecha: <%= res.getFechaReserva() != null ? res.getFechaReserva().toString() : "" %>
                                            </option>
                                        <% } %>
                                    </select>
                                </div>
                            </form>
                            <% if (detalleReserva != null) { %>
                                <h4>Detalle de la Reserva #<%= detalleReserva.getIdReserva() %></h4>
                                <ul class="list-group mx-auto" style="max-width: 500px;">
                                    <li class="list-group-item"><b>Cliente:</b> <%= detalleReserva.getNickCliente() != null ? detalleReserva.getNickCliente().getNombre() : "" %> <%= detalleReserva.getNickCliente() != null ? detalleReserva.getNickCliente().getApellido() : "" %></li>
                                    <li class="list-group-item"><b>Fecha:</b> <%= detalleReserva.getFechaReserva() %></li>
                                    <li class="list-group-item"><b>Tipo Asiento:</b> <%= detalleReserva.getTipoAsiento() %></li>
                                    <li class="list-group-item"><b>Costo Total:</b> <%= detalleReserva.getCostoTotal() %></li>
                                    <li class="list-group-item"><b>Pasajeros:</b>
                                        <ul>
                                            <% if (detalleReserva.getPasajes() != null) {
                                                for (uy.volando.publicar.DataPasaje p : detalleReserva.getPasajes()) { %>
                                                    <li><%= p.getNombre() %> <%= p.getApellido() %></li>
                                            <%   }
                                               } %>
                                        </ul>
                                    </li>
                                </ul>
                                <form method="post" class="mt-3">
                                    <input type="hidden" name="reservaSeleccionada" value="<%= detalleReserva.getIdReserva() %>" />
                                    <button type="submit" class="btn btn-primary">Realizar Check-in</button>
                                </form>
                            <% } %>
                        <% } else { %>
                            <div class="alert alert-info">No tiene reservas pendientes de check-in.</div>
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
</body>
</html>
