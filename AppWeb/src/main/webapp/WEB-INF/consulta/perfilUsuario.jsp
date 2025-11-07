<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Logica.DataUsuario, Logica.DataAerolinea, Logica.DataCliente, java.util.List" %>
<html>
<head>
    <title>Perfil de Usuario</title>
</head>
<body>
    <h2>Perfil de Usuario</h2>
    <c:if test="${not empty usuario}">
        <p><strong>Nickname:</strong> ${usuario.nickname}</p>
        <p><strong>Nombre:</strong> ${usuario.nombre}</p>
        <p><strong>Email:</strong> ${usuario.email}</p>
        <p><strong>Tipo:</strong> ${usuario.tipo}</p>
        <c:if test="${not empty usuario.imagenURL}">
            <img src="${usuario.imagenURL}" alt="Imagen de usuario" width="150" />
        </c:if>
        <c:if test="${usuario.tipo eq 'Aerolinea'}">
            <h3>Rutas de Vuelo Confirmadas</h3>
            <c:forEach var="ruta" items="${rutasConfirmadas}">
                <p>
                    <a href="consultaRutaVuelo?id=${ruta.id}">${ruta.infoBasica}</a>
                </p>
            </c:forEach>
            <c:if test="${esPropio}">
                <h3>Rutas de Vuelo Ingresadas</h3>
                <c:forEach var="ruta" items="${rutasIngresadas}">
                    <p>
                        <a href="consultaRutaVuelo?id=${ruta.id}">${ruta.infoBasica}</a>
                    </p>
                </c:forEach>
                <h3>Rutas de Vuelo Rechazadas</h3>
                <c:forEach var="ruta" items="${rutasRechazadas}">
                    <p>
                        <a href="consultaRutaVuelo?id=${ruta.id}">${ruta.infoBasica}</a>
                    </p>
                </c:forEach>
            </c:if>
        </c:if>
        <c:if test="${usuario.tipo eq 'Cliente' && esPropio}">
            <h3>Reservas de Vuelo</h3>
            <c:forEach var="reserva" items="${reservas}">
                <p>
                    <a href="consultaReservaVuelo?id=${reserva.id}">${reserva.infoBasica}</a>
                </p>
            </c:forEach>
            <h3>Paquetes Comprados</h3>
            <c:forEach var="paquete" items="${paquetes}">
                <p>
                    <a href="consultaPaquete?id=${paquete.id}">${paquete.infoBasica}</a>
                </p>
            </c:forEach>
        </c:if>
    </c:if>
    <c:if test="${empty usuario}">
        <p>No se encontró el usuario.</p>
    </c:if>
    <a href="listaUsuarios">Volver a la lista de usuarios</a>
</body>
</html>
