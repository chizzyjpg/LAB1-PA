<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/media/styles/styles.css" rel="stylesheet">
    <title>Registro - Volando.uy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
    <body class="registro-page">
    <nav class="navbar navbar-dark bg-dark fixed-top navbar-compact">
        <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">Volando.uy</a>
        </div>
    </nav>
    <main class="portada d-flex align-items-start py-5">
        <div class="container">
            <div class="row justify-content-center mt-4 mt-md-5 register-stack">
                <div class="col-11 col-sm-10 col-md-8 col-lg-6">
                    <div class="card p-4">
                        <h2 class="mb-4 text-center">Registro</h2>
                        <% String errorMsg = (String) request.getAttribute("errorMsg");
                       if (errorMsg != null) { %>
                        <div class="alert alert-danger"><%= errorMsg %></div>
                    <% } %>
                    	 <div id="clientError" class="alert alert-danger" style="display:none;"></div>
                        <form class="registro-form" action="${pageContext.request.contextPath}/altaUsuario" method="post" id="registroForm" enctype="multipart/form-data">
                            <div class="mb-3">
                                <label for="userType" class="form-label">Tipo de Usuario</label>
                                <select class="form-control" id="userType" name="tipoUsuario" required onchange="toggleFields()">
                                    <option value="">Selecciona el tipo de usuario</option>
                                    <option value="Cliente" <%= "Cliente".equals(request.getParameter("tipoUsuario")) ? "selected" : "" %>>Cliente</option>
                                    <option value="Aerolinea" <%= "Aerolinea".equals(request.getParameter("tipoUsuario")) ? "selected" : "" %>>Aerolinea</option>
                                </select>
                            </div>
                            
                            <!-- Campos comunes -->
                            <div class="mb-3">
                                <label for="username" class="form-label">Nickname</label>
                                <input type="text" class="form-control" id="username" name="nickname" required>
                                <div id="nickStatus" class="form-text"></div>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Correo electrónico</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                                <div id="emailStatus" class="form-text"></div>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Contraseña</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Confirmar Contraseña</label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                            </div>
                            <div class="mb-3">
                                <label for="image" class="form-label">Imagen de Perfil</label>
                                <input type="file" accept="image/*" id="avatarFile" class="form-control form-control-sm" name="avatarFile" />
                                
                            </div>
                            <!-- Campos especificos para Cliente -->
                            
                            <div id="clienteFields" style="display:none;">
                                <div class="mb-3">
                                    <label for="nombre" class="form-label">Nombre</label>
                                    <input type="text" class="form-control" id="nombre" name="nombre">
                                </div>
                                <div class="mb-3">
                                    <label for="apellido" class="form-label">Apellido</label>
                                    <input type="text" class="form-control" id="apellido" name="apellido">
                                </div>
                                <div class="mb-3">
                                    <label for="tipoDocumento" class="form-label">Tipo de Documento</label>
                                    <select class="form-control" id="tipoDocumento" name="tipoDocumento">
                                        <option value="">Selecciona tipo de documento</option>
                                        <option value="CEDULA">Cédula</option>
                                        <option value="PASAPORTE">Pasaporte</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="numeroDocumento" class="form-label">Número de Documento</label>
                                    <input type="text" class="form-control" id="numeroDocumento" name="numeroDocumento">
                                </div>
                                <div class="mb-3">
                                    <label for="fechaNacimiento" class="form-label">Fecha de Nacimiento</label>
                                    <input type="date" class="form-control" id="fechaNacimiento" name="fechaNacimiento">
                                </div>
                                <div class="mb-3">
                                    <label for="nacionalidad" class="form-label">Nacionalidad</label>
                                    <input type="text" class="form-control" id="nacionalidad" name="nacionalidad">
                                </div>
                            </div>
                            
                            <div id="aerolineaFields" style="display:none;">
                                <div class="mb-3">
                                    <label for="nombreAerolinea" class="form-label">Nombre de la Aerolínea</label>
                                    <input type="text" class="form-control" id="nombreAerolinea" name="nombreAerolinea">
                                </div>
                                
                                <div class="mb-3">
                                    <label for="descripcion" class="form-label">Descripción</label>
                                    <textarea class="form-control" id="descripcion" name="descripcion" rows="3" placeholder="Descripcion de la aerolinea"></textarea>
                                </div>
                                <div class="mb-3">
                                    <label for="sitioWeb" class="form-label">Sitio Web</label>
                                    <input type="url" class="form-control" id="sitioWeb" name="sitioWeb" placeholder="https://www.ejemplo.com">
                                </div>
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">Registrarse</button>
                                <a href="index.html" class="btn btn-outline-secondary mt-2">Cancelar</a>
                            </div>
                        </form>
                    </div>    
                </div>
            </div>
        </div>
        
        <!-- Footer dentro de la portada -->
        <footer class="mt-auto py-3 bg-transparent">
            <div class="container text-center">
                <small class="text-white-50">2025 Volando.uy</small>
            </div>
        </footer>
    </main>

    <!-- Scripts -->
    <script>
        function toggleFields() {
            var tipo = document.getElementById('userType').value;
            document.getElementById('clienteFields').style.display = (tipo === 'Cliente') ? 'block' : 'none';
            document.getElementById('aerolineaFields').style.display = (tipo === 'Aerolinea') ? 'block' : 'none';
        }
        window.onload = function() {
            var tipo = document.getElementById('userType').value;
            document.getElementById('clienteFields').style.display = (tipo === 'Cliente') ? 'block' : 'none';
            document.getElementById('aerolineaFields').style.display = (tipo === 'Aerolinea') ? 'block' : 'none';
        };
        document.addEventListener('DOMContentLoaded', function() {
            var form = document.getElementById('registroForm');
            var pwd = document.getElementById('password');
            var pwd2 = document.getElementById('confirmPassword');
            var clientError = document.getElementById('clientError');

            function checkPasswords() {
                if (!pwd || !pwd2) return true;
                if (pwd.value !== pwd2.value) {
                    clientError.textContent = 'Las contraseñas no coinciden. Por favor ingresa la misma contraseña en ambos campos.';
                    clientError.style.display = 'block';
                    return false;
                }
                clientError.style.display = 'none';
                return true;
            }

            // Comprobar al enviar
            form.addEventListener('submit', function(e) {
                if (!checkPasswords()) {
                    e.preventDefault();
                    e.stopPropagation();
                }
            });

            // Comprobacion en tiempo real para mejor experiencia
            pwd.addEventListener('input', checkPasswords);
            pwd2.addEventListener('input', checkPasswords);

            // ==============================
            // verificación AJAX nick/email
            // ==============================

            // Context path de la app (ej: /volandouy-oficial)
            const CONTEXT_PATH = '${pageContext.request.contextPath}';

            const nicknameInput = document.getElementById('username');
            const emailInput    = document.getElementById('email');
            const nickStatus    = document.getElementById('nickStatus');
            const emailStatus   = document.getElementById('emailStatus');

            let nickTimer = null;
            let emailTimer = null;

            // Helper para mostrar mensajes con color
            function setStatus(element, message, state) {
                if (!element) return;
                element.textContent = message || '';
                element.classList.remove('text-success', 'text-danger');

                if (state === 'ok') {
                    element.classList.add('text-success');
                } else if (state === 'error') {
                    element.classList.add('text-danger');
                }
            }

            // Llamada asíncrona para verificar nickname
            async function checkNickname() {
                const value = nicknameInput.value.trim();
                if (!value) {
                    setStatus(nickStatus, '');
                    return;
                }

                setStatus(nickStatus, 'Verificando...', null);

                try {
                    const resp = await fetch(
                        CONTEXT_PATH + '/api/verificarNickname?nickname=' + encodeURIComponent(value)
                    );

                    if (!resp.ok) {
                        setStatus(nickStatus, 'No se pudo verificar el nickname.', 'error');
                        return;
                    }

                    const data = await resp.json(); // esperamos JSON: {disponible: true/false}

                    if (data.disponible) {
                        setStatus(nickStatus, 'Nickname disponible.', 'ok');
                    } else {
                        setStatus(nickStatus, 'Nickname no disponible.', 'error');
                    }
                } catch (err) {
                    console.error(err);
                    setStatus(nickStatus, 'Error al conectar con el servidor.', 'error');
                }
            }

            // Llamada asíncrona para verificar email
            async function checkEmail() {
                const value = emailInput.value.trim();
                if (!value) {
                    setStatus(emailStatus, '');
                    return;
                }

                setStatus(emailStatus, 'Verificando...', null);

                try {
                    const resp = await fetch(
                        CONTEXT_PATH + '/api/verificarEmail?email=' + encodeURIComponent(value)
                    );

                    if (!resp.ok) {
                        setStatus(emailStatus, 'No se pudo verificar el correo.', 'error');
                        return;
                    }

                    const data = await resp.json();

                    if (data.disponible) {
                        setStatus(emailStatus, 'Correo disponible.', 'ok');
                    } else {
                        setStatus(emailStatus, 'Ya existe un usuario con este correo.', 'error');
                    }
                } catch (err) {
                    console.error(err);
                    setStatus(emailStatus, 'Error al conectar con el servidor.', 'error');
                }
            }

            // Debounce: espera unos ms desde la última tecla antes de consultar
            function debounceNick() {
                clearTimeout(nickTimer);
                nickTimer = setTimeout(checkNickname, 400);
            }

            function debounceEmail() {
                clearTimeout(emailTimer);
                emailTimer = setTimeout(checkEmail, 400);
            }

            if (nicknameInput) {
                nicknameInput.addEventListener('input', debounceNick);
            }

            if (emailInput) {
                emailInput.addEventListener('input', debounceEmail);
            }
        });
    </script>

    </body>
</html>