<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="uy.volando.publicar.DataPaquete" %>
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
	<jsp:include page="/WEB-INF/template/sidebar.jsp" />

      <!-- CONTENIDO PRINCIPAL -->
      <main class="col-lg-9 col-xl-10">
        <div class="container-fluid px-4 py-4">
          <div class="row">
            <div class="col-12">
            	<% String flashErr = (String) session.getAttribute("flash_error");
				   String flashOk  = (String) session.getAttribute("flash_success");
				   String flashInfo= (String) session.getAttribute("flash_info");
				   if (flashErr != null) { %>
				  <div class="alert alert-danger alert-dismissible fade show" role="alert">
				    <%= flashErr %>
				    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				  </div>
				  <% session.removeAttribute("flash_error"); }
				   if (flashOk != null) { %>
				  <div class="alert alert-success alert-dismissible fade show" role="alert">
				    <%= flashOk %>
				    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				  </div>
				  <% session.removeAttribute("flash_success"); }
				   if (flashInfo != null) { %>
				  <div class="alert alert-info alert-dismissible fade show" role="alert">
				    <%= flashInfo %>
				    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				  </div>
				  <% session.removeAttribute("flash_info"); } %>
				  
              <h2>Paquetes disponibles para comprar</h2>
              <form action="<%= request.getContextPath() %>/compraPaquete" method="post">
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
                    List<DataPaquete> paquetes = (List<DataPaquete>)request.getAttribute("paquetesDisponibles");
                    if (paquetes != null && !paquetes.isEmpty()) {
                      for (DataPaquete paquete : paquetes) {
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
              
              <% String mensaje = (String) request.getAttribute("mensaje"); %>
              <% if (mensaje != null) { %>
                <div class="alert alert-info" role="alert">
                  <%= mensaje %>
                </div>
              <% } %>
              
            </div>
          </div>
        </div>
		<jsp:include page="/WEB-INF/template/footer.jsp" />
      </main>
    </div>
  </div>

  <!-- FOOTER -->


  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

	<!--
  <script src="../assets/js/consulta-usuario.js"></script>
  <script src="../assets/js/auth.js"></script>
  <script src="../assets/js/roles.js"></script>
  <script src="../assets/js/comprar-paquetes.js"></script>
  -->

</body>
</html>