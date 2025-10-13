<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Logica.DataCliente, Logica.DataAerolinea" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<c:set var="rol" value="${sessionScope.rol}" />


<%
    String rolCalc = (String) pageContext.findAttribute("rol"); // puede venir null
    if (rolCalc == null) {
        Object u = session.getAttribute("usuario_logueado");
        if (u instanceof DataCliente) {
            rolCalc = "Cliente";
        } else if (u instanceof DataAerolinea) {
            rolCalc = "Aerolínea";
        } else {
            rolCalc = null; // sigue siendo visitante
        }
        request.setAttribute("rol", rolCalc);
    }
%>


<c:set var="isCliente"   value="${rol == 'Cliente'}" />
<c:set var="isAerolinea" value="${rol == 'Aerolínea'}" />

<aside class="col-lg-3 col-xl-2 d-none d-lg-block border-end bg-body-tertiary">
  <nav class="sidebar position-sticky sticky-top">
    <div class="p-3">

      <c:if test="${isAerolinea}">
        <h6 class="text-uppercase text-muted mb-3">Registros</h6>
        <div class="list-group list-group-flush">
          <a href="${pageContext.request.contextPath}/altaVuelo"
             class="list-group-item list-group-item-action">Nuevo Vuelo</a>
          <a href="${pageContext.request.contextPath}/regRutVuelo"
             class="list-group-item list-group-item-action">Nueva Ruta de Vuelo</a>
        </div>
        <br/>
      </c:if>


      <h6 class="text-uppercase text-muted mb-3">Consultas</h6>
      <div class="list-group list-group-flush">
        <a href="${pageContext.request.contextPath}/consultaUsuario.jsp"
           class="list-group-item list-group-item-action">Usuario</a>

        <a href="${pageContext.request.contextPath}/consultaRutaVuelo"
           class="list-group-item list-group-item-action">Ruta de Vuelo</a>

        <a href="${pageContext.request.contextPath}/consultaVuelo.jsp"
           class="list-group-item list-group-item-action">Vuelo</a>

        <a href="${pageContext.request.contextPath}/consultaPaqRutasVuelo.jsp"
           class="list-group-item list-group-item-action">Paquete de Rutas de Vuelo</a>


        <c:if test="${isCliente}">
          <a href="${pageContext.request.contextPath}/consultaReservas.jsp"
             class="list-group-item list-group-item-action">Mis Reservas de Vuelo</a>
        </c:if>

        <c:if test="${isAerolinea}">
          <a href="${pageContext.request.contextPath}/consultaReservas.jsp"
             class="list-group-item list-group-item-action">Reservas de Nuestros Vuelos</a>
        </c:if>
      </div>
      <br/>


      <c:if test="${isCliente || isAerolinea}">
        <h6 class="text-uppercase text-muted mb-3">Modificaciones</h6>
        <div class="list-group list-group-flush mb-4">
          <a href="${pageContext.request.contextPath}/perfil"
             class="list-group-item list-group-item-action">Modificar mis datos</a>
        </div>
      </c:if>


      <c:if test="${isCliente}">
        <h6 class="text-uppercase text-muted mb-3">Reserva / Compra</h6>
        <div class="list-group list-group-flush">
          <a href="${pageContext.request.contextPath}/reservarVuelo.jsp"
             class="list-group-item list-group-item-action">Reservar Vuelo</a>
          <a href="${pageContext.request.contextPath}/compraPaquete"
             class="list-group-item list-group-item-action">Comprar Paquete</a>
        </div>
      </c:if>


    </div>
  </nav>
</aside>
