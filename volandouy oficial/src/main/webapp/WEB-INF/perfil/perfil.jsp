<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
    <title>Mi Perfil - Volando.uy</title>
</head>
<body class="perfil-page">
    <!-- NAVBAR -->
	<jsp:include page="/WEB-INF/template/header.jsp" />

    <!-- LAYOUT: SIDEBAR + CONTENIDO -->
    <div class="container-fluid">
        <div class="row">
            <aside class="col-lg-3 col-xl-2 d-none d-lg-block border-end bg-body-tertiary">
                <nav class="sidebar position-sticky sticky-top">
                    <div class="p-3">
                        <h6 class="text-uppercase text-muted mb-3">Mi Perfil</h6>
                        <div class="list-group list-group-flush" data-roles="Cliente">
                            <a href="reservarVuelo.html" data-roles="Cliente" class="list-group-item list-group-item-action">Reservar Vuelo</a>
                            <a href="comprarPaquete.html" data-roles="Cliente" class="list-group-item list-group-item-action">Comprar Paquete</a>
                        </div>
                        <div class="list-group list-group-flush" data-roles="Aerolínea">
                        <a href="registroVuelo.html" data-roles="Aerolínea" class="list-group-item list-group-item-action">Nuevo Vuelo</a>
                            <a href="registroRutaVuelo.html" data-roles="Aerolínea" class="list-group-item list-group-item-action">Nueva Ruta</a>
                        </div>
                    </div>
                </nav>
            </aside>
            <main class="col-lg-9 col-xl-10">
                <div class="profile-container p-4">
                    <% Object usuario = request.getAttribute("usuario"); 
                    String tipo = usuario.getClass().getSimpleName(); 
                    String nombre = ""; 
                    String email = ""; 
                    if ("DataCliente".equals(tipo)) { 
	                    nombre = ((Logica.DataCliente)usuario).getNombre(); 
	                    email = ((Logica.DataCliente)usuario).getEmail(); 
                    } else if ("DataAerolinea".equals(tipo)) { 
                    	nombre = ((Logica.DataAerolinea)usuario).getNombre(); 
                    	email = ((Logica.DataAerolinea)usuario).getEmail(); 
                    } %>
					<h1 id="profileName" class="display-5"><%= nombre %></h1>
					<p id="profileEmail" class="text-muted"><%= email %></p>
					<img id="profileAvatar" class="avatar-lg rounded-circle" src="" alt="Avatar del usuario">
                        <!-- DETALLE + EDICIÓN -->
                    <section class="container p-4">
                    <div class="card shadow-sm">
                        <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h2 class="h5 mb-0">Datos personales</h2>
                            <button id="btnEditProfile" class="btn btn-outline-primary btn-sm">Editar</button>
                        </div>

                        <!-- Vista (solo lectura) -->
                        <div id="profileView">
						<dl class="row mb-0">
						<% if ("DataCliente".equals(tipo)) { %>
						    <dt class="col-sm-3">Nombre</dt>
						    <dd class="col-sm-9" id="viewNombre"><%= ((Logica.DataCliente)usuario).getNombre() %></dd>
						    <dt class="col-sm-3">Apellido</dt>
						    <dd class="col-sm-9" id="viewApellido"><%= ((Logica.DataCliente)usuario).getApellido() %></dd>
						    <dt class="col-sm-3">Nickname</dt>
						    <dd class="col-sm-9" id="viewNickname"><%= ((Logica.DataCliente)usuario).getNickname() %></dd>
						    <dt class="col-sm-3">Email</dt>
						    <dd class="col-sm-9" id="viewEmail"><%= ((Logica.DataCliente)usuario).getEmail() %></dd>
						    <dt class="col-sm-3">Rol</dt>
						    <dd class="col-sm-9" id="viewRol">Cliente</dd>
						<% } else if ("DataAerolinea".equals(tipo)) { %>
						    <dt class="col-sm-3">Nombre</dt>
						    <dd class="col-sm-9" id="viewNombre"><%= ((Logica.DataAerolinea)usuario).getNombre() %></dd>
						    <dt class="col-sm-3">Descripcion</dt>
						    <dd class="col-sm-9" id="viewDesc"><%= ((Logica.DataAerolinea)usuario).getDescripcion() %></dd>
						    <dt class="col-sm-3">Nickname</dt>
						    <dd class="col-sm-9" id="viewNickname"><%= ((Logica.DataAerolinea)usuario).getNickname() %></dd>
						    <dt class="col-sm-3">Email</dt>
						    <dd class="col-sm-9" id="viewEmail"><%= ((Logica.DataAerolinea)usuario).getEmail() %></dd>
						    <dt class="col-sm-3">Sitio Web</dt>
						    <dd class="col-sm-9" id="viewSitio"><%= ((Logica.DataAerolinea)usuario).getSitioWeb() %></dd>
						    <dt class="col-sm-3">Rol</dt>
						    <dd class="col-sm-9" id="viewRol">Aerolínea</dd>
						<% } else { %>
						    <dt class="col-sm-3">Tipo</dt>
						    <dd class="col-sm-9">No soportado: <%= tipo %></dd>
						<% } %>
						</dl>
						</div>

                        <!-- Formulario de edición -->
                        <form id="profileForm" class="row g-3 d-none">
                            <div class="col-md-6">
                            <label class="form-label">Nombre</label>
                            <input type="text" class="form-control" id="inpNombre" required>
                            </div>
                            <div class="col-md-6">
                            <label class="form-label">Apellido</label>
                            <input type="text" class="form-control" id="inpApellido">
                            </div>

                            <!-- Campos no editables-->
                            <div class="col-md-6">
                            <label class="form-label">Nickname (no editable)</label>
                            <input type="text" class="form-control" id="inpNickname" disabled>
                            </div>
                            <div class="col-md-6">
                            <label class="form-label">Email (no editable)</label>
                            <input type="email" class="form-control" id="inpEmail" disabled>
                            </div>

                            <!-- Imagen  -->
                            <div class="col-12">
                                <label class="form-label mb-2">Imagen de perfil</label>

                                <div class="d-flex gap-3 align-items-center">
                                    <img id="avatarPreview" class="rounded-circle object-fit-cover" width="72" height="72" alt="">
                                    <div class="d-flex flex-column gap-2">
                                        <input type="file" accept="image/*" id="avatarFile" class="form-control form-control-sm" />
                                        <div class="form-text">
                                        Se guarda localmente (demo sin backend). Podés quitar la foto con el botón.
                                        </div>
                                    </div>
                                    <button type="button" id="btnClearPhoto" class="btn btn-outline-danger btn-sm">Quitar foto</button>
                                </div>
                            </div>

                            <!-- Cambio de contraseña -->
                            <div class="col-12">
                            <h3 class="h6 mb-2">Cambiar contraseña (mock Parte 1)</h3>
                            </div>
                            <div class="col-md-4">
                            <label class="form-label">Contraseña actual</label>
                            <input type="password" class="form-control" id="pwdCurrent" autocomplete="current-password">
                            </div>
                            <div class="col-md-4">
                            <label class="form-label">Nueva contraseña</label>
                            <input type="password" class="form-control" id="pwdNew" autocomplete="new-password">
                            </div>
                            <div class="col-md-4">
                            <label class="form-label">Repetir nueva contraseña</label>
                            <input type="password" class="form-control" id="pwdNew2" autocomplete="new-password">
                            </div>

                            <div class="col-12 d-flex gap-2">
                            <button type="submit" class="btn btn-primary">Guardar</button>
                            <button type="button" id="btnCancelEdit" class="btn btn-outline-secondary">Cancelar</button>
                            </div>
                        </form>
                        </div>
                    </div>
                    </section>
                </div>
            </main>
        </div>
    </div>
        <!-- FOOTER -->
	<jsp:include page="/WEB-INF/template/footer.jsp" />
	
	
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <!--
    <script src="../assets/js/auth.js"></script>
    <script src="../assets/js/roles.js"></script>
    <script src="../assets/js/consulta-usuario.js"></script>
     -->
    <script src="../assets/js/perfil-edit.js"></script>
	
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
</body>
</html>