<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,uy.volando.publicar.DataCiudad,uy.volando.publicar.DataCategoria" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
    <title>Registro de Ruta de Vuelo - Volando.uy</title>
</head>
<body class="regRutaVuelo-page">
    <jsp:include page="/WEB-INF/template/header.jsp" />

    <div class="container-fluid">
        <div class="row">
            <!-- SIDEBAR -->
            <jsp:include page="/WEB-INF/template/sidebar.jsp" />

            <!-- CONTENIDO -->
            <main class="col-12 col-lg-9 col-xl-10 py-4">
                <div class="container">
                    <h1>Registro de Ruta de Vuelo</h1>

                    <%-- mensajes --%>
                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger" role="alert"><%= request.getAttribute("error") %></div>
                    <% } %>
                    <% if (request.getAttribute("exito") != null) { %>
                    <div class="alert alert-success" role="alert"><%= request.getAttribute("exito") %></div>
                    <% } %>

                    <form method="post" action="<%= request.getContextPath() %>/regRutVuelo">
                        <div class="mb-3">
                            <label for="nombreRutaVuelo" class="form-label">Nombre de la Ruta de Vuelo</label>
                            <input type="text" class="form-control" id="nombreRutaVuelo" name="nombreRutaVuelo" required>
                        </div>

                        <div class="mb-3">
                            <label for="descCorta" class="form-label">Descripción Corta</label>
                            <input type="text" class="form-control" id="descCorta" name="descCorta" required
                                   placeholder="Ej: Ruta directa Montevideo - Buenos Aires">
                        </div>

                        <div class="mb-3">
                            <label for="descLarga" class="form-label">Descripción</label>
                            <textarea class="form-control" id="descLarga" name="descLarga" rows="3" required
                                      placeholder="Ej: Esta ruta conecta las ciudades de ..."></textarea>
                        </div>

                        <div class="mb-3">
                            <label for="hora" class="form-label">Hora de Salida</label>
                            <input type="time" class="form-control" id="hora" name="hora" required>
                        </div>

                        <div class="mb-3">
                            <label for="costoTurista" class="form-label">Costo por Pasajero - Turista (USD)</label>
                            <input type="number" class="form-control" id="costoTurista" name="costoTurista" min="0" step="0.01" required>
                        </div>

                        <div class="mb-3">
                            <label for="costoEjecutivo" class="form-label">Costo por Pasajero - Ejecutivo (USD)</label>
                            <input type="number" class="form-control" id="costoEjecutivo" name="costoEjecutivo" min="0" step="0.01" required>
                        </div>

                        <div class="mb-3">
                            <label for="costoEquipajeExtra" class="form-label">Costo por Pasajero - Equipaje Extra (USD)</label>
                            <input type="number" class="form-control" id="costoEquipajeExtra" name="costoEquipajeExtra" min="0" step="0.01" required>
                        </div>

                        <div class="mb-3">
                            <label for="ciudadOrigen" class="form-label">Ciudad de Origen</label>
                            <select class="form-select" id="ciudadOrigen" name="ciudadOrigen" required>
                                <%
                                    List<DataCiudad> ciudades = (List<DataCiudad>) request.getAttribute("ciudades");
                                    if (ciudades != null) {
                                        for (DataCiudad ciudad : ciudades) {
                                %>
                                <option value="<%= ciudad.getNombre() %>"><%= ciudad.getNombre() %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="ciudadDestino" class="form-label">Ciudad de Destino</label>
                            <select class="form-select" id="ciudadDestino" name="ciudadDestino" required>
                                <%
                                    if (ciudades != null) {
                                        for (DataCiudad ciudad : ciudades) {
                                %>
                                <option value="<%= ciudad.getNombre() %>"><%= ciudad.getNombre() %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="categorias" class="form-label">Categorías Disponibles</label>
                            <select class="form-select" id="categorias" name="categorias" required multiple>
                                <%
                                    List<DataCategoria> categorias = (List<DataCategoria>) request.getAttribute("categorias");
                                    if (categorias != null) {
                                        for (DataCategoria cat : categorias) {
                                %>
                                <option value="<%= cat.getNombre() %>"><%= cat.getNombre() %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="imagen" class="form-label">Imagen Opcional (URL)</label>
                            <input type="url" class="form-control" id="imagen" name="imagen" placeholder="https://ejemplo.com/mi-imagen.jpg">
                        </div>

                        <!-- NUEVO: URL de video opcional para la ruta -->
                        <div class="mb-3">
                            <label for="videoUrl" class="form-label">URL de video asociada (opcional)</label>
                            <input type="url" class="form-control" id="videoUrl" name="videoUrl"
                                   placeholder="https://www.youtube.com/embed/ID_DEL_VIDEO">
                            <div class="form-text">
                                Podés pegar una URL de YouTube en formato de "embed" (por ejemplo,
                                <code>https://www.youtube.com/embed/c6-RSKqUbFU</code>) u otra URL de video válida.
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary">Registrar Ruta de Vuelo</button>
                        <a href="<%= request.getContextPath() %>/home" class="btn btn-secondary">Cancelar</a>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <jsp:include page="/WEB-INF/template/footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
