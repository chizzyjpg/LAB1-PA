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
import java.util.Date;
import java.util.GregorianCalendar;

import uy.volando.model.EstadoSesion;


import uy.volando.publicar.WebServices;
import uy.volando.publicar.DataUsuario;
import uy.volando.publicar.DataCliente;
import uy.volando.publicar.DataAerolinea;
import uy.volando.publicar.TipoDocumento;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
//import uy.volando.publicar.PerfilClienteUpdate;
//import uy.volando.publicar.PerfilAerolineaUpdate;

@WebServlet("/perfil")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5 MB Para avatarFile
public class Perfil extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // En vez de ISistema, ahora usamos el port del WebService
    private WebServices port;

    @Override
    public void init() throws ServletException {
        super.init();
        this.port = (WebServices) getServletContext().getAttribute("volandoPort");
        if (this.port == null) {
            throw new ServletException("El cliente SOAP (WebServices) no fue inicializado por InicioServlet.");
        }
    }

    public Perfil() {
        super();
    }

    // Helpers de sesión (quedan igual)
    public static void initSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("estado_sesion") == null) {
            session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
        }
    }

    public static EstadoSesion getEstado(HttpServletRequest request) {
        return (EstadoSesion) request.getSession().getAttribute("estado_sesion");
    }

    // =====================  GET  =====================

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (getEstado(request) != EstadoSesion.LOGIN_CORRECTO) {
            request.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").forward(request, response);
            return;
        }

        Object u = request.getSession().getAttribute("usuario_logueado");
        if (u == null) {
            request.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").forward(request, response);
            return;
        }

        // Mover flash de sesión
        HttpSession sess = request.getSession(false);
        if (sess != null) {
            Object ok  = sess.getAttribute("flash_ok");
            Object err = sess.getAttribute("flash_error");
            if (ok != null) {
                request.setAttribute("flash_ok", ok);
                sess.removeAttribute("flash_ok");
            }
            if (err != null) {
                request.setAttribute("flash_error", err);
                sess.removeAttribute("flash_error");
            }
        }

        // IMPORTANTE: ahora comparamos con los DTO del cliente SOAP
        if (u instanceof DataCliente dc) {
            request.setAttribute("tipo", "DataCliente");

            // básicos
            request.setAttribute("nickname", dc.getNickname());
            request.setAttribute("email", dc.getEmail());
            request.setAttribute("nombre", dc.getNombre());
            request.setAttribute("apellido", dc.getApellido());

            // fechas (ISO para input date + bonita para vista)
            Date fn = dc.getFechaNac().toGregorianCalendar().getTime();
            request.setAttribute("fechaNacimientoISO",
                    fn != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(fn) : "");
            request.setAttribute("fechaNacimientoView",
                    fn != null ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(fn) : "—");

            // otros
            request.setAttribute("nacionalidad", dc.getNacionalidad());
            request.setAttribute("numeroDocumento", dc.getNumDocumento());

            // tipo doc (enum -> strings)
            TipoDocumento tdoc = dc.getTipoDocumento();
            request.setAttribute("tipoDocumentoValue", tdoc != null ? tdoc.name() : "");

            // opciones del select
            request.setAttribute("tipoDocOptions", new String[] { "CEDULA", "PASAPORTE", "OTRO" });
            request.setAttribute("tipoDocLabels",  new String[] { "Cedula", "Pasaporte", "Otro" });

        } else if (u instanceof DataAerolinea da) {
            request.setAttribute("tipo", "DataAerolinea");
            request.setAttribute("nickname", da.getNickname());
            request.setAttribute("email",   da.getEmail());
            request.setAttribute("nombre",  da.getNombre());
            request.setAttribute("sitioWeb",    da.getSitioWeb());
            request.setAttribute("descripcion", da.getDescGeneral());
        } else {
            request.getRequestDispatcher("/WEB-INF/home/iniciar.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/perfil/perfil.jsp").forward(request, response);
    }

    // =====================  POST  =====================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Requiere sesión válida
        initSession(req);
        if (getEstado(req) != EstadoSesion.LOGIN_CORRECTO) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Usuario en sesión (DTO del cliente SOAP)
        Object u = req.getSession().getAttribute("usuario_logueado");
        String nickSesion = null;
        String emailSesion = null;

        if (u instanceof DataCliente dc) {
            nickSesion = dc.getNickname();
            emailSesion = dc.getEmail();
        } else if (u instanceof DataAerolinea da) {
            nickSesion = da.getNickname();
            emailSesion = da.getEmail();
        }

        // Operación esperada
        if (!"update".equals(req.getParameter("op"))) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Parámetros comunes
        final String tipo     = req.getParameter("tipo"); // "DataCliente" | "DataAerolinea"
        final String nickname = req.getParameter("nickname");
        final String email    = req.getParameter("email");

        // Imagen de perfil (3 estados: borrar | reemplazar | mantener)
        byte[] avatarBytes = null;
        boolean clearPhoto = false;
        try {
            String p = req.getParameter("clearPhoto");
            clearPhoto = "1".equals(p) || "true".equalsIgnoreCase(p) || "on".equalsIgnoreCase(p);

            Part avatarPart = null;
            try {
                avatarPart = req.getPart("avatarFile");
            } catch (IllegalStateException ise) {
                throw ise;
            }

            boolean hayArchivoNuevo = (avatarPart != null && avatarPart.getSize() > 0);

            if (!clearPhoto && hayArchivoNuevo) {
                try (InputStream is = avatarPart.getInputStream()) {
                    avatarBytes = is.readAllBytes();
                }
            }
        } catch (Exception ex) {
            throw new ServletException("Error al procesar la imagen de perfil.", ex);
        }

        // Contraseña
        final String pwdCurrent = req.getParameter("pwdCurrent") != null
                ? req.getParameter("pwdCurrent").trim()
                : "";
        final String pwdNew  = req.getParameter("pwdNew")  != null ? req.getParameter("pwdNew").trim()  : "";
        final String pwdNew2 = req.getParameter("pwdNew2") != null ? req.getParameter("pwdNew2").trim() : "";
        final boolean wantsPwdChange = !pwdCurrent.isBlank() || !pwdNew.isBlank() || !pwdNew2.isBlank();

        try {

            DataUsuario usuarioActualizado = null;

            if ("DataCliente".equals(tipo)) {
                final String nombre   = req.getParameter("nombre");
                final String apellido = req.getParameter("apellido");
                final String nac      = req.getParameter("nacionalidad");
                final String ndoc     = req.getParameter("numeroDocumento");

                Date fnac = null;
                final String fnacStr = req.getParameter("fechaNacimiento"); // "2025-10-13" p.ej.

                if (fnacStr != null && !fnacStr.isBlank()) {
                    try {
                        fnac = java.sql.Date.valueOf(fnacStr);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException(
                                "Fecha de nacimiento inválida. Formato esperado: yyyy-MM-dd");
                    }
                }

                final String tdocParam = req.getParameter("tipoDocumento");

                TipoDocumento tipoDoc = null;
                if (tdocParam != null && !tdocParam.isBlank()) {
                    try {
                        tipoDoc = TipoDocumento.valueOf(tdocParam);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Tipo de documento inválido.");
                    }
                }

                // ====== ARMAMOS EL DTO DE UPDATE PARA SOAP ======
                port.actualizarPerfilCliente(
                nickSesion,   // NOMBRES DE CAMPOS A AJUSTAR
                emailSesion,
                nombre,
                apellido,
                nac,
                tipoDoc,
                ndoc,
                // OJO con la fecha: fijate qué tipo espera PerfilClienteUpdate (Date, XMLGregorianCalendar...)
                toXMLGregorianCalendar(fnac),
                avatarBytes,
                clearPhoto
                );

                req.getSession().setAttribute("usuario_logueado", usuarioActualizado);

            } else if ("DataAerolinea".equals(tipo)) {
                final String nombre = req.getParameter("nombre");
                final String sitio  = req.getParameter("sitioWeb");
                final String desc   = req.getParameter("descripcion");

                port.actualizarPerfilAerolinea(
                nickSesion,  // ajustar nombres según el DTO generado
                emailSesion,
                nombre,
                desc,
                sitio,
                avatarBytes,
                clearPhoto
                );
                req.getSession().setAttribute("usuario_logueado", usuarioActualizado);

            } else {
                throw new IllegalArgumentException("Tipo de usuario no soportado: " + tipo);
            }

            // Cambio de contraseña vía SOAP
            if (wantsPwdChange) {
                if (pwdCurrent.isBlank()) {
                    throw new IllegalArgumentException("Ingresá tu contraseña actual.");
                }
                if (pwdNew.length() < 3) {
                    throw new IllegalArgumentException(
                            "La nueva contraseña debe tener al menos 3 caracteres.");
                }
                if (!pwdNew.equals(pwdNew2)) {
                    throw new IllegalArgumentException("Las contraseñas no coinciden.");
                }
                if (pwdNew.equals(pwdCurrent)) {
                    throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual.");
                }
                // Cambiar sistema.cambiarPassword por el método SOAP equivalente
                //port.cambiarPassword(nickname, pwdCurrent, pwdNew);
            }

            req.getSession().setAttribute("flash_ok", "Perfil actualizado correctamente.");
            resp.sendRedirect(req.getContextPath() + "/perfil");

        } catch (Exception ex) {
            String msg = (ex.getMessage() != null && !ex.getMessage().isBlank())
                    ? ex.getMessage()
                    : "Error al actualizar el perfil.";
            req.getSession().setAttribute("flash_error", msg);
            resp.sendRedirect(req.getContextPath() + "/perfil");
        }
    }

    private XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        if (date == null) {
            return null;
        }
        try {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Error convirtiendo Date a XMLGregorianCalendar", e);
        }
    }
}
