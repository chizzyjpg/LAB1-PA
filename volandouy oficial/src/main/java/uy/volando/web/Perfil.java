package uy.volando.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import Logica.ISistema;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import uy.volando.model.EstadoSesion;

@WebServlet ("/perfil")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5 MB Para avatarFile
public class Perfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ISistema sistema;
	   
	 @Override
	 public void init() throws ServletException { 
		 super.init();
		 this.sistema = (ISistema) getServletContext().getAttribute("sistema");
	     if (this.sistema == null) {
	        throw new ServletException("El sistema no fue inicializado por InicioServlet.");
	    }
	 }
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Perfil() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static void initSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("estado_sesion") == null) {
			session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
		}
	}
	
	public static EstadoSesion getEstado(HttpServletRequest request) {
		return (EstadoSesion) request.getSession().getAttribute("estado_sesion");
	}
	
    private void showView(HttpServletRequest req, HttpServletResponse resp)
	         throws ServletException, IOException {
	      Object usuario = req.getSession().getAttribute("usuario_logueado");
	      if (usuario != null) req.setAttribute("usuario", usuario);
	       req.getRequestDispatcher("/WEB-INF/perfil/perfil.jsp").forward(req, resp);
	    }
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		 initSession(request);
		  if (getEstado(request) != EstadoSesion.LOGIN_CORRECTO) {
		    request.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").forward(request, response);
		    return;
		  }

		  Object u = request.getSession().getAttribute("usuario_logueado");
		  if (u == null) {
		    request.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").forward(request, response);
		    return;
		  }

		  // Mover flash de sesión -> request (opcional)
		  HttpSession sess = request.getSession(false);
		  if (sess != null) {
		    Object ok = sess.getAttribute("flash_ok");
		    Object err = sess.getAttribute("flash_error");
		    if (ok != null) { request.setAttribute("flash_ok", ok);   sess.removeAttribute("flash_ok"); }
		    if (err != null) { request.setAttribute("flash_error", err); sess.removeAttribute("flash_error"); }
		  }

		  if (u instanceof Logica.DataCliente dc) {
		    request.setAttribute("tipo", "DataCliente");

		    // básicos
		    request.setAttribute("nickname", dc.getNickname());
		    request.setAttribute("email",    dc.getEmail());
		    request.setAttribute("nombre",   dc.getNombre());
		    request.setAttribute("apellido", dc.getApellido());

		    // fechas (ISO para input date + bonita para vista)
		    java.util.Date fn = dc.getFechaNac();
		    request.setAttribute("fechaNacimientoISO",
		        fn != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(fn) : "");
		    request.setAttribute("fechaNacimientoView",
		        fn != null ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(fn) : "—");

		    // otros
		    request.setAttribute("nacionalidad", dc.getNacionalidad());
		    // OJO: cambia este getter si tu clase lo nombra distinto
		    request.setAttribute("numeroDocumento", dc.getNumDocumento());

		    // tipo doc (enum -> strings)
		    Logica.TipoDocumento tdoc = dc.getTipoDocumento();
		    request.setAttribute("tipoDocumentoValue", tdoc != null ? tdoc.name() : "");

		    // opciones del select (par valor/etiqueta)
		    request.setAttribute("tipoDocOptions", new String[]{"CEDULA","PASAPORTE","OTRO"});
		    request.setAttribute("tipoDocLabels",  new String[]{"Cedula","Pasaporte","Otro"});

		  } else if (u instanceof Logica.DataAerolinea da) {
		    request.setAttribute("tipo", "DataAerolinea");
		    request.setAttribute("nickname",  da.getNickname());
		    request.setAttribute("email",     da.getEmail());
		    request.setAttribute("nombre",    da.getNombre());
		    request.setAttribute("sitioWeb",  da.getSitioWeb());
		    request.setAttribute("descripcion", da.getDescripcion());
		  } else {
		    request.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").forward(request, response);
		    return;
		  }

		  request.getRequestDispatcher("/WEB-INF/perfil/perfil.jsp").forward(request, response);
		}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		  // Requiere sesión válida
		  initSession(req);
		  if (getEstado(req) != EstadoSesion.LOGIN_CORRECTO) {
		    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
		    return;
		  }

		  // Operación esperada
		  if (!"update".equals(req.getParameter("op"))) {
		    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		    return;
		  }

		  // Parámetros comunes
		  final String tipo     = req.getParameter("tipo");       // "DataCliente" | "DataAerolinea"
		  final String nickname = req.getParameter("nickname");   // PK lógica
		  final String email    = req.getParameter("email");

		  // Imagen de perfil (opcional)
		  byte[] avatarBytes = null;
		  boolean clearPhoto = "1".equals(req.getParameter("clearPhoto"));
		  try {
		    Part avatarPart = req.getPart("avatarFile");
		    if (!clearPhoto && avatarPart != null && avatarPart.getSize() > 0) {
		      try (InputStream is = avatarPart.getInputStream()) {
		        avatarBytes = is.readAllBytes();
		      }
		    }
		  } catch (Exception ignore) {
		    // Si falla la lectura del archivo, se mantiene avatarBytes = null (no cambia).
		  }

		  // Contraseña 
		  final String pwdCurrent = req.getParameter("pwdCurrent") != null ? req.getParameter("pwdCurrent").trim() : "";
		  final String pwdNew     = req.getParameter("pwdNew")     != null ? req.getParameter("pwdNew").trim()     : "";
		  final String pwdNew2    = req.getParameter("pwdNew2")    != null ? req.getParameter("pwdNew2").trim()    : "";
		  final boolean wantsPwdChange = !pwdCurrent.isBlank() || !pwdNew.isBlank() || !pwdNew2.isBlank();

		  try {
			  
			  Object usuarioActualizado = null;
		    
		    if ("DataCliente".equals(tipo)) {
		    	final String nombre   = req.getParameter("nombre");
		    	final String apellido = req.getParameter("apellido");
		    	final String nac      = req.getParameter("nacionalidad");
		    	final String ndoc     = req.getParameter("numeroDocumento");

		    	java.util.Date fnac = null;
		    	final String fnacStr  = req.getParameter("fechaNacimiento"); // "2025-10-13" p.ej.

		    	if (fnacStr != null && !fnacStr.isBlank()) {
		    	  try {
		    	    fnac = java.sql.Date.valueOf(fnacStr); // parsea yyyy-MM-dd
		    	  } catch (IllegalArgumentException e) {
		    	    throw new IllegalArgumentException("Fecha de nacimiento inválida. Formato esperado: yyyy-MM-dd");
		    	  }
		    	}
		    	
		    	final String tdocParam = req.getParameter("tipoDocumento"); // "CEDULA" | "PASAPORTE" | "OTRO"
		        Logica.TipoDocumento tipoDoc = null;
		        if (tdocParam != null && !tdocParam.isBlank()) {
		          try {
		            tipoDoc = Logica.TipoDocumento.valueOf(tdocParam);
		          } catch (IllegalArgumentException e) {
		            throw new IllegalArgumentException("Tipo de documento inválido.");
		          }
		        }

		    	var upd = new Logica.PerfilClienteUpdate(
		    	  nickname, email, nombre, apellido, nac, tipoDoc, ndoc, fnac,
		    	  avatarBytes, clearPhoto
		    	);
		    	  sistema.actualizarPerfilCliente(upd);

		    } else if ("DataAerolinea".equals(tipo)) {
		      final String nombre = req.getParameter("nombre");
		      final String sitio  = req.getParameter("sitioWeb");
		      final String desc   = req.getParameter("descripcion");

		      var upd = new Logica.PerfilAerolineaUpdate(
		          nickname, email, nombre, desc, sitio, avatarBytes, clearPhoto
		      );
		      
		      usuarioActualizado = sistema.actualizarPerfilAerolinea(upd);
		      req.getSession().setAttribute("usuario_logueado", usuarioActualizado);

		    } else {
		      throw new IllegalArgumentException("Tipo de usuario no soportado: " + tipo);
		    }

		    // Cambio de contraseña (validaciones mínimas de forma; la verificación real la hace ISistema)
		    if (wantsPwdChange) {
		      if (pwdCurrent.isBlank())             throw new IllegalArgumentException("Ingresá tu contraseña actual.");
		      if (pwdNew.length() < 3)              throw new IllegalArgumentException("La nueva contraseña debe tener al menos 3 caracteres.");
		      if (!pwdNew.equals(pwdNew2))          throw new IllegalArgumentException("Las contraseñas no coinciden.");
		      if (pwdNew.equals(pwdCurrent))        throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual.");
		      sistema.cambiarPassword(nickname, pwdCurrent, pwdNew);
		    }

		    // Refrescar sesión y feedback
		    req.getSession().setAttribute("flash_ok", "Perfil actualizado correctamente.");

		    // PRG para evitar reenvío de formularios
		    resp.sendRedirect(req.getContextPath() + "/perfil");

		  } catch (Exception ex) {
		    // Mensaje de error para mostrar en la vista
		    String msg = (ex.getMessage() != null && !ex.getMessage().isBlank())
		        ? ex.getMessage() : "Error al actualizar el perfil.";
		    req.getSession().setAttribute("flash_error", msg);

		    // Redirige al GET del perfil (mostrará el flash)
		    resp.sendRedirect(req.getContextPath() + "/perfil");
		  }
		}
}