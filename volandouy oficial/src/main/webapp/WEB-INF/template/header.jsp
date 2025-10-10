<!-- NAVBAR -->
    <nav class="navbar navbar-dark bg-dark fixed-top navbar-compact">
        <div class="container">
        <a class="navbar-brand fw-bold" href="#">Volando.uy</a>

      <!-- Buscador centrado -->
      <div class="flex-grow-1 d-none d-md-flex justify-content-center">
        <form class="search-bar" role="search">
          <div class="input-group input-group-sm" style="max-width: 420px;">
            <span class="input-group-text">Buscar</span>
            <input class="form-control form-control-sm" type="search" placeholder="Vuelos, rutas..." aria-label="Buscar">
          </div>
        </form>
      </div>

      <!-- Acciones auth (mock) -->
      <div id="authArea" class="d-flex align-items-center gap-2">
        <!-- Estado: invitado -->
        <% 
          Logica.DataUsuario usuario = (Logica.DataUsuario) session.getAttribute("usuario_logueado");
          if (usuario == null) {
        %>
        <% System.out.println("DEBUG: usuario_logueado=" + usuario); %>
        <div id="guestArea" class="d-flex align-items-center gap-2">
            <button class="btn btn-outline-light btn-sm" data-bs-toggle="modal" data-bs-target="#loginModal">Iniciar sesión</button>
            <a class="btn btn-warning btn-sm" href="registro.html" role="button">Registrarse</a>
        </div>
        <% } else { %>
        <!-- Estado: logueado -->
        <% System.out.println("DEBUG header.jsp getNickname=" + usuario.getNickname()); %>
        <% System.out.println("DEBUG header.jsp getNombre=" + usuario.getNombre()); %>
        <div id="userArea" class="d-flex align-items-center gap-2">
            <div class="auth-user text-light small">
                <span id="avatarWrapper" class="d-flex align-items-center">
                  <span id="avatarFallback" class="avatar-fallback"><%= usuario.getNickname().substring(0,1).toUpperCase() %></span>
                </span>
                <span>Hola, <strong id="nicknameSpan"><%= usuario.getNickname() %></strong></span>
            </div>
            <div class="btn-group">
                <button class="btn btn-outline-light btn-sm dropdown-toggle" data-bs-toggle="dropdown">Cuenta</button>
                <ul class="dropdown-menu dropdown-menu-end">
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/perfil">Mi perfil</a></li>
                  <li><hr class="dropdown-divider" /></li>
                  <li><form action="cerrar-sesion" method="post" style="margin:0;"><button type="submit" class="dropdown-item text-danger" id="btnLogout">Cerrar sesión</button></form></li>
                </ul>
            </div>
        </div>
        <% } %>
      </div>
    </div>
  </nav>