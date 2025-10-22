<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="es">
<head>
  <jsp:include page="/WEB-INF/template/head.jsp" />
  <title>Mi Perfil - Volando.uy</title>
</head>
<body class="perfil-page">
  <!-- NAVBAR -->
  <jsp:include page="/WEB-INF/template/header.jsp" />

  <!-- Derivar banderas de tipo -->
  <c:set var="isCliente"   value="${tipo == 'DataCliente'}"/>
  <c:set var="isAerolinea" value="${tipo == 'DataAerolinea'}"/>

  <!-- Mensajes flash -->
  <c:if test="${not empty flash_ok}"> <!-- Existe la variable flash_ok y tiene contenido?-->
    <div class="alert alert-success m-3" role="alert">${flash_ok}</div>
  </c:if>
  <c:if test="${not empty flash_error}">
    <div class="alert alert-danger m-3" role="alert">${flash_error}</div>
  </c:if>

  <!-- LAYOUT -->
  <div class="container-fluid">
    <div class="row">
      <aside class="col-lg-3 col-xl-2 d-none d-lg-block border-end bg-body-tertiary">
        <nav class="sidebar position-sticky sticky-top">
          <div class="p-3">
            <h6 class="text-uppercase text-muted mb-3">Mi Perfil</h6>
            <c:if test="${isCliente}">
              <div class="list-group list-group-flush">
                <a href="${pageContext.request.contextPath}/reservaVuelo" class="list-group-item list-group-item-action">Reservar Vuelo</a>
                <a href="${pageContext.request.contextPath}/compraPaquete" class="list-group-item list-group-item-action">Comprar Paquete</a>
              </div>
            </c:if>
            <c:if test="${isAerolinea}">
              <div class="list-group list-group-flush">
                <a href="${pageContext.request.contextPath}/altaVuelo" class="list-group-item list-group-item-action">Nuevo Vuelo</a>
                <a href="${pageContext.request.contextPath}/regRutVuelo" class="list-group-item list-group-item-action">Nueva Ruta</a>
              </div>
            </c:if>
          </div>
        </nav>
      </aside>

      <main class="col-lg-9 col-xl-10">
        <div class="profile-container p-4">
          <!-- Encabezado -->
          <h1 id="profileName" class="display-5">
            <c:out value="${nombre}"/>
          </h1>
          <p id="profileEmail" class="text-muted">
            <c:out value="${email}"/>
          </p>
          <img id="profileAvatar" class="avatar-lg rounded-circle" src="${pageContext.request.contextPath}/avatar?nickname=${nickname}" alt="Avatar del usuario">

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
                    <dt class="col-sm-3">Nombre</dt>
                    <dd class="col-sm-9" id="viewNombre"><c:out value="${nombre}"/></dd>

                    <c:if test="${isCliente}">
                      <dt class="col-sm-3">Apellido</dt>
                      <dd class="col-sm-9" id="viewApellido"><c:out value="${apellido}"/></dd>
                    </c:if>

                    <dt class="col-sm-3">Nickname</dt>
                    <dd class="col-sm-9" id="viewNickname"><c:out value="${nickname}"/></dd>

                    <dt class="col-sm-3">Email</dt>
                    <dd class="col-sm-9" id="viewEmail"><c:out value="${email}"/></dd>

                    <c:if test="${isCliente}">
                      <dt class="col-sm-3">F. Nac.</dt>
                      <dd class="col-sm-9" id="viewFnac">
                        <c:out value="${fechaNacimientoView != null ? fechaNacimientoView : '—'}"/>
                      </dd>

                      <dt class="col-sm-3">Nacionalidad</dt>
                      <dd class="col-sm-9" id="viewNac"><c:out value="${nacionalidad}"/></dd>

                      <dt class="col-sm-3">Tipo doc.</dt>
                      <dd class="col-sm-9" id="viewTdoc"><c:out value="${tipoDocumentoLabel}"/></dd>

                      <dt class="col-sm-3">Nº doc.</dt>
                      <dd class="col-sm-9" id="viewNdoc"><c:out value="${numeroDocumento}"/></dd>
                    </c:if>

                    <c:if test="${isAerolinea}">
                      <dt class="col-sm-3">Descripción</dt>
                      <dd class="col-sm-9" id="viewDesc"><c:out value="${descripcion}"/></dd>

                      <dt class="col-sm-3">Sitio Web</dt>
                      <dd class="col-sm-9" id="viewSitio"><c:out value="${sitioWeb}"/></dd>
                    </c:if>

                    <dt class="col-sm-3">Rol</dt>
                    <dd class="col-sm-9" id="viewRol">
                      <c:out value="${isCliente ? 'Cliente' : (isAerolinea ? 'Aerolínea' : '—')}"/>
                    </dd>
                  </dl>
                </div>

                <!-- Formulario de edición -->
                <form id="profileForm" class="row g-3 d-none"
                      method="post"
                      action="${pageContext.request.contextPath}/perfil"
                      enctype="multipart/form-data">

                  <input type="hidden" name="op" value="update">
                  <input type="hidden" name="tipo" value="${tipo}">
                  <input type="hidden" name="nickname" value="${nickname}">
                  <input type="hidden" name="email" value="${email}">

                  <!-- NO editables (visibles) -->
                  <div class="col-md-6">
                    <label class="form-label">Nickname (no editable)</label>
                    <input type="text" class="form-control" value="${nickname}" disabled>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Email (no editable)</label>
                    <input type="email" class="form-control" value="${email}" disabled>
                  </div>

                  <!-- EDITABLES -->
                  <c:if test="${isCliente}">
                    <div class="col-md-6">
                      <label class="form-label">Nombre</label>
                      <input type="text" class="form-control" id="inpNombre" name="nombre" value="${nombre}" required>
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">Apellido</label>
                      <input type="text" class="form-control" id="inpApellido" name="apellido" value="${apellido}">
                    </div>

                    <div class="col-md-4">
                      <label class="form-label">Fecha de nacimiento</label>
                      <input type="date" class="form-control" id="inpFechaNac" name="fechaNacimiento" value="${fechaNacimientoISO}">
                    </div>
                    <div class="col-md-4">
                      <label class="form-label">Nacionalidad</label>
                      <input type="text" class="form-control" id="inpNacionalidad" name="nacionalidad" value="${nacionalidad}">
                    </div>
                    <div class="col-md-4">
                      <label class="form-label">Tipo de documento</label>
                      <select class="form-select" id="inpTipoDoc" name="tipoDocumento" required>
						  <c:forEach var="val" items="${tipoDocOptions}" varStatus="st">
						    <c:set var="lab" value="${tipoDocLabels[st.index]}"/>
						    <option value="${val}" <c:if test="${val == tipoDocumentoValue}">selected</c:if>>
						      ${lab}
						    </option>
						  </c:forEach>
					</select>
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">Número de documento</label>
                      <input type="text" class="form-control" id="inpNumDoc" name="numeroDocumento" value="${numeroDocumento}">
                    </div>
                  </c:if>

                  <c:if test="${isAerolinea}">
                    <div class="col-md-6">
                      <label class="form-label">Nombre</label>
                      <input type="text" class="form-control" id="inpNombre" name="nombre" value="${nombre}" required>
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">Sitio Web</label>
                      <input type="url" class="form-control" id="inpSitio" name="sitioWeb" value="${sitioWeb}">
                    </div>
                    <div class="col-12">
                      <label class="form-label">Descripción</label>
                      <textarea class="form-control" id="inpDesc" name="descripcion" rows="2">${descripcion}</textarea>
                    </div>
                  </c:if>

                  <!-- Imagen -->
                  <div class="col-12">
                    <label class="form-label mb-2">Imagen de perfil</label>
                    <div class="d-flex gap-3 align-items-center">
                      <img id="avatarPreview" class="rounded-circle object-fit-cover" width="72" height="72" alt="Avatar" src="${pageContext.request.contextPath}/avatar?nickname=${nickname}">
                      <div class="d-flex flex-column gap-2">
                        <input type="file" accept="image/*" id="avatarFile" class="form-control form-control-sm" name="avatarFile" />
                        <div class="form-text">Opcional. Si no subís nada, se mantiene la actual.</div>
                      </div>
                      <button type="button" id="btnClearPhoto" class="btn btn-outline-danger btn-sm">Quitar foto</button>
                      <input type="hidden" name="clearPhoto" id="clearPhoto" value="0">
                    </div>
                  </div>

                  <!-- Cambio de contraseña (opcional) -->
                  <div class="col-12"><h3 class="h6 mb-2">Cambiar contraseña</h3></div>
                  <div class="col-md-4">
                    <label class="form-label">Contraseña actual</label>
                    <input type="password" class="form-control" name="pwdCurrent" id="pwdCurrent" autocomplete="current-password">
                  </div>
                  <div class="col-md-4">
                    <label class="form-label">Nueva contraseña</label>
                    <input type="password" class="form-control" name="pwdNew" id="pwdNew" autocomplete="new-password">
                  </div>
                  <div class="col-md-4">
                    <label class="form-label">Repetir nueva contraseña</label>
                    <input type="password" class="form-control" name="pwdNew2" id="pwdNew2" autocomplete="new-password">
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
  <script src="<c:url value='/assets/js/perfil-edit.js'/>"></script>
</body>
</html>
