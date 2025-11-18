<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uy.volando.web.DeviceUtils" %>


<%
    DeviceUtils.DeviceType deviceType =
            (DeviceUtils.DeviceType) session.getAttribute("deviceType");

    boolean esMobile = (deviceType != null
            && deviceType == DeviceUtils.DeviceType.MOBILE);
%>

<!doctype html>
<html>
<head>
<jsp:include page="/WEB-INF/template/head.jsp" />
<title>Iniciar Sesión :: VolandoUy</title>
</head>
<body class="index-page">
<jsp:include page="/WEB-INF/template/headerBasico.jsp" />
  <main class="portada d-flex align-items-start py-5">
    <div class="container">
      <div class="row justify-content-center mt-4 mt-md-5">
        <div class="col-11 col-sm-10 col-md-8 col-lg-6">
          <div class="d-grid gap-3 btn-panel">
            <a href="iniciar-sesion" class="btn btn-primary">Iniciar sesión</a>
            <a href="altaUsuario" class="btn btn-primary">Registrarse</a>
              <% if (!esMobile) { %>
                  <a href="visitante"
                     class="btn btn-outline-secondary">
                      Entrar como visitante
                  </a>
              <% } %>
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
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>