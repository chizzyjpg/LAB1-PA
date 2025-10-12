<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <jsp:include page="/WEB-INF/template/head.jsp" />
  <title>Home - Volando.uy</title>
</head>
<body class="home-page">
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
              
              <a href="${pageContext.request.contextPath}/altaVuelo" class="list-group-item list-group-item-action">Nuevo Vuelo</a>
              <a href="${pageContext.request.contextPath}/regRutVuelo" class="list-group-item list-group-item-action">Nueva Ruta de Vuelo</a>

            </div><br>
            <h6 class="text-uppercase text-muted mb-3">Consultas</h6>
            <div class="list-group list-group-flush">
              <a href="front/consultaUsuario.html" class="list-group-item list-group-item-action">Usuario</a>
              <a href="front/consultaRutasVuelo.html" class="list-group-item list-group-item-action">Ruta de Vuelo</a>
              <a href="front/consultaVuelo.html" class="list-group-item list-group-item-action">Vuelo</a>
              <a href="front/consultaPaqRutasVuelo.html" class="list-group-item list-group-item-action">Paquete de Rutas de Vuelo</a>
              <a href="front/consultaReservas.html" data-roles="Cliente" class="list-group-item list-group-item-action">Mis Reservas de Vuelo</a>
              <a href="front/consultaReservas.html" data-roles="Aerolínea" class="list-group-item list-group-item-action">Reservas de Nuestros Vuelos</a>
            </div><br>
            <h6 class="text-uppercase text-muted mb-3" data-roles="Aerolínea,Cliente">Modificaciones</h6>
            <div class="list-group list-group-flush mb-4" data-roles="Aerolínea,Cliente">
              <a href="front/perfil.html" class="list-group-item list-group-item-action">Modificar mis datos</a>
            </div>
            <h6 class="text-uppercase text-muted mb-3" data-roles="Cliente">Reserva / Compra</h6>
            <div class="list-group list-group-flush" data-roles="Cliente">
              <a href="front/reservarVuelo.html" class="list-group-item list-group-item-action">Reservar Vuelo</a>
              <a href="${pageContext.request.contextPath}/compraPaquete" class="list-group-item list-group-item-action">Comprar Paquete</a>
            </div>
          </div>
        </nav>
      </aside>

      <!-- CONTENIDO -->
      <main class="col-12 col-lg-9 col-xl-10 py-4">
        <div class="container">
          <h1>Bienvenid@ a Volando.uy</h1>

          <!--imagen de volando en la izquierda 
          <div class="text-center">
            <img src="assets/img/ChatGPT Image 24 sept 2025, 10_28_06 a.m..png" alt="Avión volando" class="img-fluid my-4" style="max-height: 120px;">
          </div>
          -->
          <section class="mt-4">
            <div class="row justify-content-center g-3">
              <!-- Card 1 -->
              <div class="col-12 col-md-10 col-lg-8">
                <div class="card h-100 shadow-sm">
                  <div class="row g-0 align-items-center">
                    <div class="col-12 col-sm-4">
                    <!-- Arreglar la ruta de la imagen-->
                      <img src="${pageContext.request.contextPath}/media/images/madrid.jpg" alt="Madrid" class="img-fluid card-img-top rounded-start">
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
                      <img src="${pageContext.request.contextPath}/media/images/nyc.jpg" alt="Nueva York" class="img-fluid card-img-top rounded-start">
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
                      <img src="${pageContext.request.contextPath}/media/images/rio.jpg" alt="Río de Janeiro" class="img-fluid card-img-top rounded-start">
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
        </div>
      </main>
    </div>
  </div>
  

  <!-- FOOTER -->
	<jsp:include page="/WEB-INF/template/footer.jsp" />

  <!-- Bootstrap JS 
  <script src="assets/js/auth.js"></script>-->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="assets/js/consulta-usuario.js"></script>
  <script src="assets/js/roles.js"></script>

  <div class="modal fade" id="loginModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
      <form class="modal-content" id="loginForm">
      <div class="modal-header">
          <h5 class="modal-title">Iniciar sesión</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body">
          <div class="mb-3">
          <label class="form-label">Nickname</label>
          <input name="nickname" class="form-control" placeholder="p. ej., maria23" required>
          </div>
          <div class="mb-2">
          <label class="form-label">Contraseña</label>
          <input name="pass" type="password" class="form-control" placeholder="1235" required>
          </div>
          <div id="loginError" class="text-danger small d-none">Credenciales inválidas.</div>
      </div>
      <div class="modal-footer">
          <button class="btn btn-secondary" data-bs-dismiss="modal" type="button">Cancelar</button>
          <button class="btn btn-primary" type="submit">Entrar</button>
      </div>
      </form>
  </div>
  </div>

  <% System.out.println("DEBUG iniciado.jsp usuario_logueado=" + session.getAttribute("usuario_logueado")); %>
</body>
<!-- Modal Login (Front-only, mock) -->

</html>