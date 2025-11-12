package uy.volando.web;

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
import uy.volando.publicar.WebServices;
import uy.volando.publicar.DataAerolinea;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet implementation class altaUsuario.
 */
@WebServlet("/altaUsuario")
@MultipartConfig
public class altaUsuario extends HttpServlet {
  private static final long serialVersionUID = 1L;

    private WebServices port;

    @Override
    public void init() throws ServletException {
        super.init();
        this.port = (WebServices) getServletContext().getAttribute("volandoPort");
        if(this.port == null) {
            throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
        }
    }
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

    String tipoUsuario = request.getParameter("tipoUsuario");
    String nickname    = request.getParameter("nickname");
    String nombre      = request.getParameter("nombre");
    String apellido    = request.getParameter("apellido");
    String email       = request.getParameter("email");
    String password    = request.getParameter("password");

    String errorMsg = null;
    boolean exito = false;

    // === Avatar OPCIONAL (no condiciona el flujo) ===
    byte[] avatarBytes = new byte[0];
    try {
      Part avatarPart = request.getPart("avatarFile");
      if (avatarPart != null && avatarPart.getSize() > 0) {
        try (InputStream is = avatarPart.getInputStream()) {
          avatarBytes = is.readAllBytes();
        }
      }
    } catch (Exception ignore) {
      // si falla leer el avatar, seguimos con array vacío
    }

    // === Validaciones comunes ===
    if (nickname == null || nickname.isBlank()
        || email == null || email.isBlank()
        || password == null || password.isBlank()
        || tipoUsuario == null || tipoUsuario.isBlank()) {
      errorMsg = "Todos los campos son obligatorios.";
    } else if (port.existeNickname(nickname)) {
      errorMsg = "El nickname ya está en uso.";
    } else if (port.existeEmail(email)) {
      errorMsg = "El email ya está en uso.";
    }

    if (errorMsg == null) {
      try {
        switch (tipoUsuario) {
          case "Aerolinea": {
            String nombreAerolinea = request.getParameter("nombreAerolinea");
            String descripcion     = request.getParameter("descripcion");
            String sitioWeb        = request.getParameter("sitioWeb");

            if (nombreAerolinea == null || nombreAerolinea.isBlank()
                || descripcion == null || descripcion.isBlank()) {
              errorMsg = "Nombre de aerolínea y descripción son obligatorios para Aerolínea.";
              break;
            }

            DataAerolinea a = new DataAerolinea();
            a.setNombre(nombreAerolinea);
            a.setNickname(nickname);
            a.setEmail(email);
            a.setContrasenia(password);
            a.setDescGeneral(descripcion);
            a.setSitioWeb(sitioWeb);
            port.altaAerolinea(a);

            // Si subieron avatar, lo actualizo
            if (avatarBytes != null && avatarBytes.length > 0) {
              port.actualizarPerfilAerolinea(nickname, email, nombreAerolinea, descripcion, sitioWeb, avatarBytes, false);
            }

            exito = true;
            break;
          }

          case "Cliente": {
            String tipoDocumentoStr   = request.getParameter("tipoDocumento");
            String numeroDocumento    = request.getParameter("numeroDocumento");
            String fechaNacimientoStr = request.getParameter("fechaNacimiento");
            String nacionalidad       = request.getParameter("nacionalidad");

            if (nombre == null || nombre.isBlank()
                || apellido == null || apellido.isBlank()
                || tipoDocumentoStr == null || tipoDocumentoStr.isBlank()
                || numeroDocumento == null || numeroDocumento.isBlank()
                || fechaNacimientoStr == null || fechaNacimientoStr.isBlank()
                || nacionalidad == null || nacionalidad.isBlank()) {
              errorMsg = "Todos los campos de cliente son obligatorios.";
              break;
            }

            uy.volando.publicar.TipoDocumento tipoDocEnum;
            if ("CEDULA".equalsIgnoreCase(tipoDocumentoStr)) {
                tipoDocEnum = uy.volando.publicar.TipoDocumento.CEDULA;
            } else if ("PASAPORTE".equalsIgnoreCase(tipoDocumentoStr)) {
                tipoDocEnum = uy.volando.publicar.TipoDocumento.PASAPORTE;
            } else {
                errorMsg = "Tipo de documento inválido. Debe ser 'CEDULA' o 'PASAPORTE'.";
                break;
            }

            Date fechaNacimiento;
            javax.xml.datatype.XMLGregorianCalendar xmlFecha;
            try {
              fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimientoStr);
              // Validar que la fecha no sea futura
              if (fechaNacimiento.after(new Date())) {
                errorMsg = "La fecha de nacimiento no puede ser futura.";
                break;
              }
              javax.xml.datatype.DatatypeFactory df = javax.xml.datatype.DatatypeFactory.newInstance();
              java.util.Calendar calFecha = java.util.Calendar.getInstance();
              calFecha.setTime(fechaNacimiento);
              xmlFecha = df.newXMLGregorianCalendar(
                calFecha.get(java.util.Calendar.YEAR),
                calFecha.get(java.util.Calendar.MONTH) + 1,
                calFecha.get(java.util.Calendar.DAY_OF_MONTH),
                0, 0, 0, 0, 0
              );
            } catch (Exception e) {
              errorMsg = "Fecha de nacimiento inválida.";
              break;
            }

            uy.volando.publicar.DataCliente c = new uy.volando.publicar.DataCliente();
            c.setNombre(nombre);
            c.setNickname(nickname);
            c.setEmail(email);
            c.setContrasenia(password);
            c.setApellido(apellido);
            c.setNacionalidad(nacionalidad);
            c.setTipoDocumento(tipoDocEnum);
            c.setNumDocumento(numeroDocumento);
            c.setFechaNac(xmlFecha);

            // DEBUG: Mostrar todos los valores del objeto DataCliente
            System.out.println("[DEBUG altaUsuario] nickname=" + c.getNickname());
            System.out.println("[DEBUG altaUsuario] nombre=" + c.getNombre());
            System.out.println("[DEBUG altaUsuario] apellido=" + c.getApellido());
            System.out.println("[DEBUG altaUsuario] email=" + c.getEmail());
            System.out.println("[DEBUG altaUsuario] password=" + c.getContrasenia());
            System.out.println("[DEBUG altaUsuario] nacionalidad=" + c.getNacionalidad());
            System.out.println("[DEBUG altaUsuario] tipoDocumento=" + c.getTipoDocumento());
            System.out.println("[DEBUG altaUsuario] numDocumento=" + c.getNumDocumento());
            System.out.println("[DEBUG altaUsuario] fechaNac=" + c.getFechaNac());

            port.altaCliente(c, avatarBytes);
            exito = true;
            break;
          }

          default:
            errorMsg = "Tipo de usuario inválido.";
        }
      } catch (Exception e) {
        errorMsg = "Error al dar de alta el usuario: " + e.getMessage();
      }
    }

    if (exito) {
      response.sendRedirect(request.getContextPath() + "/home");
    } else {
      request.setAttribute("errorMsg", errorMsg);
      request.getRequestDispatcher("/WEB-INF/registro/altaUsuario.jsp").forward(request, response);
    }
  }

 }
