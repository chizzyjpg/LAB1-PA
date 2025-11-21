<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="uy.volando.publicar.DataCliente, uy.volando.publicar.DataAerolinea, uy.volando.web.DeviceUtils" %>
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

    // Detectar si es MOBILE según DeviceUtils
    DeviceUtils.DeviceType deviceType =
            (DeviceUtils.DeviceType) session.getAttribute("deviceType");
    boolean esMobile = (deviceType != null
            && deviceType == DeviceUtils.DeviceType.MOBILE);
    // lo exponemos a JSTL
    request.setAttribute("esMobile", esMobile);
%>

<c:set var="isCliente"   value="${rol == 'Cliente'}" />
<c:set var="isAerolinea" value="${rol == 'Aerolínea'}" />
<c:set var="isOffcanvas" value="${param.context == 'offcanvas' or requestScope.offcanvas == true}" />

<c:choose>
    <c:when test="${!isOffcanvas}">
        <!-- SIDEBAR LATERAL (desktop) -->
        <aside class="col-lg-3 col-xl-2 d-none d-lg-block border-end bg-body-tertiary">
            <nav class="sidebar position-sticky sticky-top">
                <div class="p-3">

                    <!-- REGISTROS: solo escritorio -->
                    <c:if test="${isAerolinea and not esMobile}">
                        <h6 class="text-uppercase text-muted mb-3">Registros</h6>
                        <div class="list-group list-group-flush">
                            <a href="${pageContext.request.contextPath}/altaVuelo"
                               class="list-group-item list-group-item-action">Nuevo Vuelo</a>
                            <a href="${pageContext.request.contextPath}/regRutVuelo"
                               class="list-group-item list-group-item-action">Nueva Ruta de Vuelo</a>
                            <a href="${pageContext.request.contextPath}/finalizarRutaVuelo"
                                class="list-group-item list-group-item-action">Finalizar Ruta de Vuelo</a>
                        </div>
                        <br/>
                    </c:if>

                    <!-- CONSULTAS -->
                    <h6 class="text-uppercase text-muted mb-3">Consultas</h6>
                    <div class="list-group list-group-flush">
                        <!-- Usuario: solo escritorio -->
                        <c:if test="${not esMobile}">
                            <a href="${pageContext.request.contextPath}/listado-usuarios"
                               class="list-group-item list-group-item-action">Usuario</a>
                        </c:if>

                        <!-- Estas dos SIEMPRE disponibles (también en móvil) -->
                        <a href="${pageContext.request.contextPath}/consultaRutaVuelo"
                           class="list-group-item list-group-item-action">Ruta de Vuelo</a>

                        <a href="${pageContext.request.contextPath}/consultaVuelo"
                           class="list-group-item list-group-item-action">Vuelo</a>

                        <!-- Paquetes: solo escritorio -->
                        <c:if test="${not esMobile}">
                            <a href="${pageContext.request.contextPath}/consultaPaqRutasVuelo"
                               class="list-group-item list-group-item-action">Paquete de Rutas de Vuelo</a>
                        </c:if>

                        <!-- Mis reservas / Reservas de nuestros vuelos -->
                        <c:if test="${isCliente}">
                            <a href="${pageContext.request.contextPath}/consultaReservaVuelo"
                               class="list-group-item list-group-item-action">Mis Reservas de Vuelo</a>
                        </c:if>

                        <c:if test="${isAerolinea and not esMobile}">
                            <!-- aerolínea no entra desde móvil, pero por las dudas lo ocultamos igual -->
                            <a href="${pageContext.request.contextPath}/consultaReservaVuelo"
                               class="list-group-item list-group-item-action">Reservas de Nuestros Vuelos</a>
                        </c:if>

                        <!-- Check-in: visible en móvil para cliente -->
                        <c:if test="${isCliente and not esMobile}">
                            <a href="${pageContext.request.contextPath}/consultaCheckin"
                               class="list-group-item list-group-item-action"> Consulta Check-in de Reserva de Vuelo</a>
                        </c:if>
                    </div>
                    <br/>

                    <!-- MODIFICACIONES: solo escritorio -->
                    <c:if test="${(isCliente || isAerolinea) and not esMobile}">
                        <h6 class="text-uppercase text-muted mb-3">Modificaciones</h6>
                        <div class="list-group list-group-flush mb-4">
                            <a href="${pageContext.request.contextPath}/perfil"
                               class="list-group-item list-group-item-action">Modificar mis datos</a>
                        </div>
                    </c:if>

                    <!-- RESERVA / COMPRA: solo escritorio -->
                    <c:if test="${isCliente and not esMobile}">
                        <h6 class="text-uppercase text-muted mb-3">Reserva / Compra</h6>
                        <div class="list-group list-group-flush">
                            <a href="${pageContext.request.contextPath}/reservaVuelo"
                               class="list-group-item list-group-item-action">Reservar Vuelo</a>
                            <a href="${pageContext.request.contextPath}/compraPaquete"
                               class="list-group-item list-group-item-action">Comprar Paquete</a>
                        </div>
                    </c:if>

                </div>
            </nav>
        </aside>
    </c:when>

    <c:otherwise>
        <!-- OFFCANVAS (mobile / colapsable) -->
        <nav class="sidebar">
            <div class="p-3">

                <!-- REGISTROS: solo escritorio, así que no se muestran en offcanvas móvil -->
                <c:if test="${isAerolinea and not esMobile}">
                    <h6 class="text-uppercase text-muted mb-3">Registros</h6>
                    <div class="list-group list-group-flush">
                        <a href="${pageContext.request.contextPath}/altaVuelo"
                           class="list-group-item list-group-item-action">Nuevo Vuelo</a>
                        <a href="${pageContext.request.contextPath}/regRutVuelo"
                           class="list-group-item list-group-item-action">Nueva Ruta de Vuelo</a>
                    </div>
                    <br/>
                </c:if>

                <!-- CONSULTAS -->
                <h6 class="text-uppercase text-muted mb-3">Consultas</h6>
                <div class="list-group list-group-flush">
                    <!-- Usuario: solo escritorio -->
                    <c:if test="${not esMobile}">
                        <a href="${pageContext.request.contextPath}/listado-usuarios"
                           class="list-group-item list-group-item-action">Usuario</a>
                    </c:if>

                    <!-- Estas dos SIEMPRE disponibles (también en móvil) -->
                    <a href="${pageContext.request.contextPath}/consultaRutaVuelo"
                       class="list-group-item list-group-item-action">Ruta de Vuelo</a>
                    <a href="${pageContext.request.contextPath}/consultaVuelo"
                       class="list-group-item list-group-item-action">Vuelo</a>

                    <!-- Paquetes: solo escritorio -->
                    <c:if test="${not esMobile}">
                        <a href="${pageContext.request.contextPath}/consultaPaqRutasVuelo"
                           class="list-group-item list-group-item-action">Paquete de Rutas de Vuelo</a>
                    </c:if>

                    <!-- Mis reservas / Reservas de nuestros vuelos -->
                    <c:if test="${isCliente}">
                        <a href="${pageContext.request.contextPath}/consultaReservaVuelo"
                           class="list-group-item list-group-item-action">Mis Reservas de Vuelo</a>
                    </c:if>
                    <c:if test="${isAerolinea and not esMobile}">
                        <a href="${pageContext.request.contextPath}/consultaReservaVuelo"
                           class="list-group-item list-group-item-action">Reservas de Nuestros Vuelos</a>
                    </c:if>

                    <!-- Check-in: visible en móvil para cliente -->
                    <c:if test="${isCliente and esMobile}">
                        <a href="${pageContext.request.contextPath}/checkInReservaVuelo"
                           class="list-group-item list-group-item-action">Check-in de Reserva de Vuelo</a>
                    </c:if>
                </div>
                <br/>

                <!-- MODIFICACIONES: solo escritorio -->
                <c:if test="${(isCliente || isAerolinea) and not esMobile}">
                    <h6 class="text-uppercase text-muted mb-3">Modificaciones</h6>
                    <div class="list-group list-group-flush mb-4">
                        <a href="${pageContext.request.contextPath}/perfil"
                           class="list-group-item list-group-item-action">Modificar mis datos</a>
                    </div>
                </c:if>

                <!-- RESERVA / COMPRA: solo escritorio -->
                <c:if test="${isCliente and not esMobile}">
                    <h6 class="text-uppercase text-muted mb-3">Reserva / Compra</h6>
                    <div class="list-group list-group-flush">
                        <a href="${pageContext.request.contextPath}/reservaVuelo"
                           class="list-group-item list-group-item-action">Reservar Vuelo</a>
                        <a href="${pageContext.request.contextPath}/compraPaquete"
                           class="list-group-item list-group-item-action">Comprar Paquete</a>
                    </div>
                </c:if>

            </div>
        </nav>
    </c:otherwise>
</c:choose>
