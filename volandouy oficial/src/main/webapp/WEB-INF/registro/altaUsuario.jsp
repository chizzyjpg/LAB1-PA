<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- FIX: correct path to the app's stylesheet so background image loads -->
    <link href="${pageContext.request.contextPath}/media/styles/styles.css" rel="stylesheet">
    <title>Registro - Volando.uy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
    <body class="registro-page">
    <nav class="navbar navbar-dark bg-dark fixed-top navbar-compact">
        <div class="container">
        <a class="navbar-brand fw-bold" href="home.html">Volando.uy</a>
        </div>
    </nav>
    <main class="portada d-flex align-items-start py-5">
        <div class="container">
            <div class="row justify-content-center mt-4 mt-md-5">
                <div class="col-11 col-sm-10 col-md-8 col-lg-6">
                    <div class="card p-4">
                        <h2 class="mb-4 text-center">Registro</h2>
                        <% String errorMsg = (String) request.getAttribute("errorMsg");
                       if (errorMsg != null) { %>
                        <div class="alert alert-danger"><%= errorMsg %></div>
                    <% } %>
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
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Correo electronico</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Contrase�a</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Confirmar Contrase�a</label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                            </div>
                            <div class="mb-3">
                                <label for="image" class="form-label">Imagen de Perfil</label>
                                <input type="file" accept="image/*" id="avatarFile" class="form-control form-control-sm" name="avatarFile" />
                                
                            </div>
                            
                            <!-- Campos especÃ­ficos para Cliente -->
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
                                        <option value="cedula">Cedula</option>
                                        <option value="pasaporte">Pasaporte</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="numeroDocumento" class="form-label">Numero de Documento</label>
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
                            
                            <!-- Campos especÃ­ficos para AerolÃ­nea -->
                            <div id="aerolineaFields" style="display:none;">
                                <div class="mb-3">
                                    <label for="nombreAerolinea" class="form-label">Nombre de la Aerolinea</label>
                                    <input type="text" class="form-control" id="nombreAerolinea" name="nombreAerolinea">
                                </div>
                                
                                <div class="mb-3">
                                    <label for="descripcion" class="form-label">Descripcion</label>
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
    </script>

    </body>
</html>