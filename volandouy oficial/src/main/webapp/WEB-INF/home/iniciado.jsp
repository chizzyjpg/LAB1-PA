<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <jsp:include page="/WEB-INF/template/head.jsp" />
  <title>Home - Volando.uy</title>
</head>
<body class="home-page">
  <jsp:include page="/WEB-INF/template/header.jsp" />

  <!-- ===== LAYOUT: SIDEBAR + CONTENIDO (Grid) ===== -->
  <div class="container-fluid">       
    <div class="row g-0">

      <%-- 3) columna izquierda: sidebar --%>
      <jsp:include page="/WEB-INF/template/sidebar.jsp" />

     <main class="col-12 col-lg-9 col-xl-10 py-4">
          <h1>Bienvenid@ a Volando.uy</h1>

          <section class="mt-4">
            <div class="row justify-content-center g-3">
              <!-- Card 1 -->
              <div class="col-12 col-md-10 col-lg-8">
                <div class="card h-100 shadow-sm">
                  <div class="row g-0 align-items-center">
                    <div class="col-12 col-sm-4">
                      <img src="${pageContext.request.contextPath}/media/images/madrid.jpg"
                           alt="Madrid" class="img-fluid card-img-top rounded-start">
                    </div>
                    <div class="col-12 col-sm-8">
                      <div class="card-body">
                        <h3 class="h6 mb-2">Montevideo — Madrid <small class="text-muted">(IB6012)</small></h3>
                        <p class="mb-2">
                          <strong>Aerolínea:</strong> Iberia<br>
                          <strong>Duración:</strong> ~12 h<br>
                          Comidas, bebidas y entretenimiento.
                        </p>
                        <a href="#" class="btn btn-sm btn-primary">Ver detalles</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Card 2 -->
              <div class="col-12 col-md-10 col-lg-8">
                <div class="card h-100 shadow-sm">
                  <div class="row g-0 align-items-center">
                    <div class="col-12 col-sm-4">
                      <img src="${pageContext.request.contextPath}/media/images/nyc.jpg"
                           alt="Nueva York" class="img-fluid card-img-top rounded-start">
                    </div>
                    <div class="col-12 col-sm-8">
                      <div class="card-body">
                        <h3 class="h6 mb-2">Ciudad de Panamá — Nueva York <small class="text-muted">(CM804)</small></h3>
                        <p class="mb-2">
                          <strong>Aerolínea:</strong> Copa Airlines<br>
                          <strong>Duración:</strong> 5 h 30 m<br>
                          Servicio a bordo completo.
                        </p>
                        <a href="#" class="btn btn-sm btn-primary">Ver detalles</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Card 3 -->
              <div class="col-12 col-md-10 col-lg-8">
                <div class="card h-100 shadow-sm">
                  <div class="row g-0 align-items-center">
                    <div class="col-12 col-sm-4">
                      <img src="${pageContext.request.contextPath}/media/images/rio.jpg"
                           alt="Río de Janeiro" class="img-fluid card-img-top rounded-start">
                    </div>
                    <div class="col-12 col-sm-8">
                      <div class="card-body">
                        <h3 class="h6 mb-2">Montevideo — Río de Janeiro <small class="text-muted">(ZL1502)</small></h3>
                        <p class="mb-2">
                          <strong>Aerolínea:</strong> ZulyFly<br>
                          <strong>Duración:</strong> 2 h 30 m (directo)<br>
                          Servicio de cortesía incluido.
                        </p>
                        <a href="#" class="btn btn-sm btn-primary">Ver detalles</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

            </div>
          </section>
      </main>

    </div>
  </div>  

  <!-- FOOTER -->
  <jsp:include page="/WEB-INF/template/footer.jsp" />

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

  <% System.out.println("DEBUG iniciado.jsp usuario_logueado=" + session.getAttribute("usuario_logueado")); %>
</body>
</html>
