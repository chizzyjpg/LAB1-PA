<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Logica.DataUsuario, java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Listado de Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h2>Listado de Usuarios</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Nickname</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Tipo</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="usuario" items="${usuarios}">
                    <tr>
                        <td>${usuario.nickname}</td>
                        <td>${usuario.nombre}</td>
                        <td>${usuario.email}</td>
                        <td>${usuario.tipoUsuario}</td>
                        <td>
                            <a href="consulta-perfil-usuario?nickname=${usuario.nickname}" class="btn btn-primary btn-sm">Ver Perfil</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>