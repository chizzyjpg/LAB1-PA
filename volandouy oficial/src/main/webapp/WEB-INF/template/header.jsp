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
        <div id="guestArea" class="d-flex align-items-center gap-2">
            <button class="btn btn-outline-light btn-sm" data-bs-toggle="modal" data-bs-target="#loginModal">Iniciar sesión</button>
            <a class="btn btn-warning btn-sm" href="registro.html" role="button">Registrarse</a>
        </div>
        <!-- Estado: logueado -->
        <div id="userArea" class="d-none align-items-center gap-2">
            <div class="auth-user text-light small">
                <span id="avatarWrapper" class="d-flex align-items-center">
                    <img id="avatarImg" src="" alt="avatar" class="avatar-sm d-none">
                    <span id="avatarFallback" class="avatar-fallback d-none">U</span>
                </span>
                <span>Hola, <strong id="nicknameSpan">usuario</strong></span>
            </div>
            <div class="btn-group">
                <button class="btn btn-outline-light btn-sm dropdown-toggle" data-bs-toggle="dropdown">Cuenta</button>
                <ul class="dropdown-menu dropdown-menu-end">
                  <li><a class="dropdown-item" href="front/perfil.html">Mi perfil</a></li>
                  <li><hr class="dropdown-divider" /></li>
                  <li><button class="dropdown-item text-danger" id="btnLogout">Cerrar sesión</button></li>
                </ul>
            </div>
        </div>
        </div>
    </div>
  </nav>