<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
    <title>Listado de Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<jsp:include page="/WEB-INF/template/header.jsp" />

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/template/sidebar.jsp" />

        <main class="col-12 col-lg-9 col-xl-10 py-4">
            <div class="container-fluid">
                <h2>Listado de Usuarios</h2>

                <table class="table table-striped align-middle">
                    <thead>
                    <tr>
                        <th>Nickname</th>
                        <th>Nombre</th>
                        <th>Email</th>
                        <th>Tipo de Usuario</th>
                        <c:if test="${mostrarAccion}">
                            <th>Seguir/Dejar de seguir</th>
                        </c:if>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="usuario" items="${usuarios}">
                        <tr>
                            <td>${usuario.nickname}</td>
                            <td>${usuario.nombre}</td>
                            <td>${usuario.email}</td>
                            <td>${usuario.tipoUsuario}</td>
                            <c:if test="${mostrarAccion}">
                                <td>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/listado-usuarios"
                                          class="d-inline">
                                        <input type="hidden" name="nickObjetivo" value="${usuario.nickname}" />

                                        <c:choose>
                                            <c:when test="${usuario.siguiendo}">
                                                <button type="submit"
                                                        class="btn btn-secondary btn-sm"
                                                        name="accion"
                                                        value="dejar">
                                                    Dejar de seguir
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="submit"
                                                        class="btn btn-primary btn-sm"
                                                        name="accion"
                                                        value="seguir">
                                                    Seguir
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

            </div>
        </main>
    </div>
</div>

<jsp:include page="/WEB-INF/template/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
