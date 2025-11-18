<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="es">
<head>
	<jsp:include page="/WEB-INF/template/head.jsp" />
  <title>Volando.uy :: Iniciar Sesión</title>
</head>
<body class="index-page">
    <jsp:include page="/WEB-INF/template/headerBasico.jsp" />
  <!-- SecciÃ³n con fondo -->
  <main class="portada d-flex align-items-center py-5">
    <div class="container">
      <!-- bloque de botones centrado -->
      <div class="row justify-content-center mt-4 mt-md-5">
        <div class="col-11 col-sm-10 col-md-8 col-lg-6">
          <div class="d-grid gap-3 btn-panel">
            <form id="loginForm" action="${pageContext.request.contextPath}/iniciar-sesion" method="POST">
                <div class="mb-3">
                    <label for="loginId" class="form-label label-login">Nickname o email</label>
                    <input type="text" class="form-control" id="loginId" name="login" placeholder="ej: nickname o correo@dominio.com" required pattern="(^[a-zA-Z0-9._-]{3,30}$)|(^\S+@\S+\.\S+$)" autocomplete="username"> 
                </div>

                <div class="mb-3">
                    <label for="loginPass" class="form-label label-login">Contraseña</label>
                    <input type="password" class="form-control" id="loginPass" name="password" placeholder="****" minlength="3" required autocomplete="current-password"> 
                </div>
	
				<%
				uy.volando.model.EstadoSesion estado = (uy.volando.model.EstadoSesion) session.getAttribute("estado_sesion");
				boolean loginIncorrecto = (estado == uy.volando.model.EstadoSesion.LOGIN_INCORRECTO);

                String mensajeError = (String) request.getAttribute("mensaje_error");
				%>
				
				<div class="mb-3" id="loginError" style="<%= loginIncorrecto ? "" : "display:none;" %>">
				    <div class="alert alert-danger" role="alert">
                        <%= (mensajeError != null
                                ? mensajeError
                                : "Usuario o contraseña incorrectos") %>
				    </div>
				</div>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">Iniciar sesión</button>
                    <a href="index.html" class="btn btn-outline-secondary">Cancelar</a>
                </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <footer class="mt-auto py-3 bg-transparent">
            <div class="container text-center">
                <small class="text-white-50">2025 Volando.uy</small>
            </div>
        </footer>
  </main>

  
</body>
</html>
