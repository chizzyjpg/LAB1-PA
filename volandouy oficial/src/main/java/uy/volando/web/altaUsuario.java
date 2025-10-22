package uy.volando.web;

import Logica.DataAerolinea;
import Logica.DataCliente;
import Logica.ISistema;
import Logica.TipoDocumento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import Logica.PerfilAerolineaUpdate;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet implementation class altaUsuario.
 */
@WebServlet("/altaUsuario")
@MultipartConfig
public class altaUsuario extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor de altaUsuario
   */
  public altaUsuario() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    request.setAttribute("usuario", usuario);
    request.getRequestDispatcher("/WEB-INF/registro/altaUsuario.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    request.setAttribute("usuario", usuario);

    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");
    if (sistema == null) {
      request.setAttribute("errorMsg", "El sistema no está inicializado en el contexto.");
      request.getRequestDispatcher("/WEB-INF/registro/altaUsuario.jsp").forward(request, response);
      return;
    }

    String tipoUsuario = request.getParameter("tipoUsuario"); // "Aerolinea" o "Cliente"
    String nickname = request.getParameter("nickname");
    String nombre = request.getParameter("nombre");
    String apellido = request.getParameter("apellido");
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    String errorMsg = null;
    boolean exito = false;

    // === Validaciones básicas comunes ===
    if (nickname == null || nickname.isBlank()
        || email == null || email.isBlank()
        || password == null || password.isBlank()
        || tipoUsuario == null || tipoUsuario.isBlank()) {
      errorMsg = "Todos los campos son obligatorios.";
    } else if (sistema.existeNickname(nickname)) {
      errorMsg = "El nickname ya está en uso.";
    } else if (sistema.existeEmail(email)) {
      errorMsg = "El email ya está en uso.";
    }

    if (errorMsg != null) {
      request.setAttribute("errorMsg", errorMsg);
      request.getRequestDispatcher("/WEB-INF/registro/altaUsuario.jsp").forward(request, response);
      return;
    }

    try {
      switch (tipoUsuario) {
        case "Aerolinea": {
          String nombreAerolinea = request.getParameter("nombreAerolinea");
          String descripcion = request.getParameter("descripcion");
          String sitioWeb = request.getParameter("sitioWeb");

          if (nombreAerolinea == null || nombreAerolinea.isBlank()
              || descripcion == null || descripcion.isBlank()) {
            errorMsg = "Nombre de aerolínea y descripción son obligatorios para Aerolínea.";
            break;
          }

          // Crear aerolínea
          DataAerolinea aerolinea = new DataAerolinea(
              nombreAerolinea, nickname, email, password, descripcion, sitioWeb);
          sistema.altaAerolinea(aerolinea);

          // Avatar opcional
          byte[] avatarBytes = null;
          Part avatarPart = request.getPart("avatarFile");
          if (avatarPart != null && avatarPart.getSize() > 0) {
            try (InputStream is = avatarPart.getInputStream()) {
              avatarBytes = is.readAllBytes();
            }
          }
          if (avatarBytes != null && avatarBytes.length > 0) {
            PerfilAerolineaUpdate perfil = new PerfilAerolineaUpdate(
                nickname, email, nombreAerolinea, descripcion, sitioWeb, avatarBytes, false);
            sistema.actualizarPerfilAerolinea(perfil);
          }

          exito = true;
          break;
        }

        case "Cliente": {
          String tipoDocumentoStr = request.getParameter("tipoDocumento");
          String numeroDocumento = request.getParameter("numeroDocumento");
          String fechaNacimientoStr = request.getParameter("fechaNacimiento");
          String nacionalidad = request.getParameter("nacionalidad");

          if (nombre == null || nombre.isBlank()
              || apellido == null || apellido.isBlank()
              || tipoDocumentoStr == null || tipoDocumentoStr.isBlank()
              || numeroDocumento == null || numeroDocumento.isBlank()
              || fechaNacimientoStr == null || fechaNacimientoStr.isBlank()
              || nacionalidad == null || nacionalidad.isBlank()) {
            errorMsg = "Todos los campos de cliente son obligatorios.";
            break;
          }

          TipoDocumento tipoDocumento;
          if ("cedula".equalsIgnoreCase(tipoDocumentoStr)) {
            tipoDocumento = TipoDocumento.CEDULA;
          } else if ("pasaporte".equalsIgnoreCase(tipoDocumentoStr)) {
            tipoDocumento = TipoDocumento.PASAPORTE;
          } else {
            // contemplá "otro" si existe en tu enum
            tipoDocumento = TipoDocumento.OTRO; // si tu enum no tiene OTRO, manejalo como null + error
          }

          Date fechaNacimiento;
          try {
            fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimientoStr);
          } catch (Exception e) {
            request.setAttribute("errorMsg", "Fecha de nacimiento inválida.");
            request.getRequestDispatcher("/WEB-INF/registro/altaUsuario.jsp").forward(request, response);
            return;
          }

          // Avatar opcional
          byte[] avatarBytes = null;
          Part avatarPart = request.getPart("avatarFile");
          if (avatarPart != null && avatarPart.getSize() > 0) {
            try (InputStream is = avatarPart.getInputStream()) {
              avatarBytes = is.readAllBytes();
            }
          }

          DataCliente cliente = new DataCliente(
              nombre, nickname, email, password, apellido,
              fechaNacimiento, nacionalidad, tipoDocumento, numeroDocumento);

          sistema.altaCliente(cliente, avatarBytes); // puede ir null si no hay avatar
          exito = true;
          break;
        }

        default:
          errorMsg = "Tipo de usuario inválido.";
          break;
      }
    } catch (Exception e) {
      errorMsg = "Error al dar de alta el usuario: " + e.getMessage();
    }

    if (exito) {
      response.sendRedirect(request.getContextPath() + "/home");
    } else {
      request.setAttribute("errorMsg", errorMsg);
      request.getRequestDispatcher("/WEB-INF/registro/altaUsuario.jsp").forward(request, response);
    }
  }
 }
  