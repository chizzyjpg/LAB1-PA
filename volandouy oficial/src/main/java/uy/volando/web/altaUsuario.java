package uy.volando.web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import Logica.ISistema;
import Logica.DataAerolinea;
import Logica.DataCliente;
import Logica.TipoDocumento;
import Logica.PerfilAerolineaUpdate;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet ("/altaUsuario")
@MultipartConfig
public class altaUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

	public altaUsuario() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    Object usuario = session.getAttribute("usuario_logueado");
	    request.setAttribute("usuario", usuario);
	    request.getRequestDispatcher("/WEB-INF/registro/altaUsuario.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    Object usuario = session.getAttribute("usuario_logueado");
	    request.setAttribute("usuario", usuario);

	    ISistema sistema = (ISistema) getServletContext().getAttribute("sistema");

	    String tipoUsuario = request.getParameter("tipoUsuario");
	    String nickname = request.getParameter("nickname");
	    String nombre = request.getParameter("nombre");
	    String apellido = request.getParameter("apellido");
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");
	    

	    String errorMsg = null;
	    boolean exito = false;
	    
	 // Imagen de perfil (opcional)
        byte[] avatarBytes = null;
        try {
          Part avatarPart = request.getPart("avatarFile");
          if (avatarPart != null && avatarPart.getSize() > 0) {
            try (InputStream is = avatarPart.getInputStream()) {
              avatarBytes = is.readAllBytes();
            }
          }
        } catch (Exception ignore) {
          // Si falla la lectura del archivo, se mantiene avatarBytes = null (no cambia).
        }
        
	    if (nickname == null || nickname.isEmpty() ||
	        email == null || email.isEmpty() ||
	        password == null || password.isEmpty() ||
	        tipoUsuario == null || tipoUsuario.isEmpty()) {
	        errorMsg = "Todos los campos son obligatorios.";
	    } else if (sistema.existeNickname(nickname)) {
	        errorMsg = "El nickname ya está en uso.";
	    } else if (sistema.existeEmail(email)) {
	        errorMsg = "El email ya está en uso.";
	    } else {
	        try {
	            switch (tipoUsuario) {
	                case "Aerolinea": {
	                    String nombreAerolinea = request.getParameter("nombreAerolinea");
	                    String descripcion = request.getParameter("descripcion");
	                    String sitioWeb = request.getParameter("sitioWeb");
	                    if (nombreAerolinea == null || nombreAerolinea.isEmpty() || descripcion == null || descripcion.isEmpty()) {
	                        errorMsg = "Nombre de aerolínea y descripción son obligatorios para Aerolínea.";
	                        break;
	                    }
	                    DataAerolinea aerolinea = new DataAerolinea(nombreAerolinea, nickname, email, password, descripcion, sitioWeb);
	                    sistema.altaAerolinea(aerolinea);
	                    
	                    if (avatarBytes != null && avatarBytes.length > 0) {
	                        PerfilAerolineaUpdate perfil = new PerfilAerolineaUpdate(nickname, email, nombreAerolinea, descripcion, sitioWeb, avatarBytes, false);
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
	                    if (nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty() || tipoDocumentoStr == null || tipoDocumentoStr.isEmpty() || numeroDocumento == null || numeroDocumento.isEmpty() || fechaNacimientoStr == null || fechaNacimientoStr.isEmpty() || nacionalidad == null || nacionalidad.isEmpty()) {
	                        errorMsg = "Todos los campos de cliente son obligatorios.";
	                        break;
	                    }
	                    TipoDocumento tipoDocumento = null;
	                    if ("cedula".equalsIgnoreCase(tipoDocumentoStr)) {
	                        tipoDocumento = TipoDocumento.CEDULA;
	                    } else if ("pasaporte".equalsIgnoreCase(tipoDocumentoStr)) {
	                        tipoDocumento = TipoDocumento.PASAPORTE;
	                    }
	                    Date fechaNacimiento = null;
	                    try {
	                        fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimientoStr);
	                    } catch (Exception e) {
	                        errorMsg = "Fecha de nacimiento inválida.";
	                        break;
	                    }
	                    DataCliente cliente = new DataCliente(nombre, nickname, email, password, apellido, fechaNacimiento, nacionalidad, tipoDocumento, numeroDocumento);
	                    if(avatarBytes == null) {
	                    	System.out.println("Avatar nulo en altaUsuario");
	                    }
	                    sistema.altaCliente(cliente, avatarBytes);
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
	    }

	    if (exito) {
	        response.sendRedirect(request.getContextPath() + "/home"); // Redirige a la lista de usuarios
	    } else {
	        request.setAttribute("errorMsg", errorMsg);
	        request.getRequestDispatcher("/WEB-INF/registro/altaUsuario.jsp").forward(request, response);
	    }
	}
}