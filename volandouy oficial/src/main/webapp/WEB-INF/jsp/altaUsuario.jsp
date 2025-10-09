<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Alta de Usuario</title>
  <script>
    function onTipoChange() {
      const tipo = document.getElementById('tipo').value;
      document.getElementById('bloqueCliente').style.display = (tipo==='CLIENTE')?'block':'none';
      document.getElementById('bloqueAero').style.display    = (tipo==='AEROLINEA')?'block':'none';
    }
    window.addEventListener('DOMContentLoaded', onTipoChange);
  </script>
</head>
<body>
  <h2>Alta de Usuario</h2>

  <form action="${pageContext.request.contextPath}/alta-usuario" method="post"
        enctype="multipart/form-data" accept-charset="UTF-8">
    <fieldset>
      <legend>Datos básicos</legend>
      <label>Nickname* <input type="text" name="nickname" required></label><br>
      <label>Nombre* <input type="text" name="nombre" required></label><br>
      <label>Email* <input type="email" name="email" required></label><br>
      <label>Contraseña* <input type="password" name="password" required></label><br>
      <label>Confirmar contraseña* <input type="password" name="password2" required></label><br>
      <label>Imagen* <input type="file" name="imagen" accept="image/*" required></label><br>
      <label>Tipo*
        <select id="tipo" name="tipo" onchange="onTipoChange()" required>
          <option value="CLIENTE">Cliente</option>
          <option value="AEROLINEA">Aerolínea</option>
        </select>
      </label>
    </fieldset>

    <fieldset id="bloqueCliente" style="display:none">
      <legend>Cliente</legend>
      <label>Apellido* <input type="text" name="apellido"></label><br>
      <label>Fecha Nac.* <input type="date" name="fecha_nac"></label><br>
      <label>Nacionalidad* <input type="text" name="nacionalidad"></label><br>
      <label>Tipo Doc.* <input type="text" name="tipo_doc"></label><br>
      <label>Nº Doc.* <input type="text" name="num_doc"></label><br>
    </fieldset>

    <fieldset id="bloqueAero" style="display:none">
      <legend>Aerolínea</legend>
      <label>Descripción <textarea name="descripcion" rows="3"></textarea></label><br>
      <label>Sitio Web (opcional) <input type="url" name="sitio_web" placeholder="https://..."></label><br>
    </fieldset>

    <button type="submit">Crear usuario</button>
  </form>

  <p style="color:red">${requestScope.error}</p>
  <p style="color:green">${requestScope.ok}</p>
</body>
</html>
