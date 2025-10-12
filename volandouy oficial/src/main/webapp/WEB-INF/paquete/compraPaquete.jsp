<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="Logica.Paquete" %>
<!DOCTYPE html>
<html lang="es">
<head>
	<jsp:include page="/WEB-INF/template/head.jsp" />
	<title>Compra Paquete - Volando.uy</title>
</head>
<body class="compraPaquete-page">
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
              
              <a href="registroVuelo.html" class="list-group-item list-group-item-action">Nuevo Vuelo</a>
              <a href="registroRutaVuelo.html" class="list-group-item list-group-item-action">Nueva Ruta de Vuelo</a>

            </div><br>
            <h6 class="text-uppercase text-muted mb-3">Consultas</h6>
            <div class="list-group list-group-flush">
              <a href="consultaUsuario.html" class="list-group-item list-group-item-action">Usuario</a>
              <a href="consultaRutasVuelo.html" class="list-group-item list-group-item-action">Ruta de Vuelo</a>
              <a href="consultaVuelo.html" class="list-group-item list-group-item-action">Vuelo</a>
              <a href="consultaPaqRutasVuelo.html" class="list-group-item list-group-item-action">Paquete de Rutas de Vuelo</a>
              <a href="consultaReservas.html" data-roles="Cliente" class="list-group-item list-group-item-action">Mis Reservas de Vuelo</a>
              <a href="consultaReservas.html" data-roles="Aerolínea" class="list-group-item list-group-item-action ">Reservas de Nuestros Vuelos</a>
            </div><br>
            <h6 class="text-uppercase text-muted mb-3" data-roles="Aerolínea,Cliente">Modificaciones</h6>
            <div class="list-group list-group-flush mb-4" data-roles="Aerolínea,Cliente">
              <a href="perfil.html" class="list-group-item list-group-item-action">Modificar mis datos</a>
            </div>
            <h6 class="text-uppercase text-muted mb-3" data-roles="Cliente">Reserva / Compra</h6>
            <div class="list-group list-group-flush" data-roles="Cliente">
              <a href="reservarVuelo.html" class="list-group-item list-group-item-action">Reservar Vuelo</a>
              <a href="comprarPaquete.html" class="list-group-item list-group-item-action active">Comprar Paquete</a>
            </div>
          </div>
        </nav>
      </aside>

      <!-- CONTENIDO PRINCIPAL -->
      <main class="col-lg-9 col-xl-10">
        <div class="container-fluid px-4 py-4">
          <div class="row">
            <div class="col-12">
              <h2>Paquetes disponibles para comprar</h2>
              <form action="compraPaquete" method="post">
                <table class="table table-bordered">
                  <thead>
                    <tr>
                      <th>Nombre</th>
                      <th>Descripción</th>
                      <th>Costo</th>
                      <th>Vencimiento (dias)</th>
                      <th>Acción</th>
                    </tr>
                  </thead>
                  <tbody>
                  <% 
                    List<Paquete> paquetes = (List<Paquete>)request.getAttribute("paquetesDisponibles");
                    if (paquetes != null && !paquetes.isEmpty()) {
                      for (Paquete paquete : paquetes) {
                  %>
                    <tr>
                      <td><%= paquete.getNombre() %></td>
                      <td><%= paquete.getDescripcion() %></td>
                      <td><%= paquete.getCosto() %></td>
                      <td><%= paquete.getValidez() %></td>
                      <td>
                        <button type="submit" name="paqueteId" value="<%= paquete.getNombre() %>" class="btn btn-primary">Comprar</button>
                      </td>
                    </tr>
                  <% 
                      }
                    } else {
                  %>
                    <tr><td colspan="5">No hay paquetes disponibles.</td></tr>
                  <% } %>
                  </tbody>
                </table>
              </form>
              
              <!-- En el JSP, mostrar el mensaje si existe -->
              <% String mensaje = (String) request.getAttribute("mensaje"); %>
              <% if (mensaje != null) { %>
                <div class="alert alert-info" role="alert">
                  <%= mensaje %>
                </div>
              <% } %>
              
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>

  <!-- FOOTER -->
	<jsp:include page="/WEB-INF/template/footer.jsp" />

  <!-- Bootstrap JS 
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


  <script src="../assets/js/consulta-usuario.js"></script>
  <script src="../assets/js/auth.js"></script>
  <script src="../assets/js/roles.js"></script>
  <script src="../assets/js/comprar-paquetes.js"></script>
  -->

</body>
</html>