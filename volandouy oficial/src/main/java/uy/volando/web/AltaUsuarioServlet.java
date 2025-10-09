package uy.volando.web;

import dao.UsuarioDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.sql.Date;
import java.util.UUID;

@WebServlet("/alta-usuario")
@MultipartConfig(fileSizeThreshold = 1024*1024,
                 maxFileSize = 5L*1024*1024,
                 maxRequestSize = 6L*1024*1024)
public class AltaUsuarioServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Cambiá esto si querés otro lugar (p.ej. /var/app/uploads o C:\\app\\uploads)
    private Path uploadsRoot;

    @Override
    public void init() throws ServletException {
        String configured = System.getProperty("UPLOAD_DIR");
        if (configured != null && !configured.isBlank()) {
            uploadsRoot = Paths.get(configured);
        } else {
            // fallback: dentro del contenedor (se pierde al redeploy)
            uploadsRoot = Paths.get(getServletContext().getRealPath("/uploads"));
        }
        try { Files.createDirectories(uploadsRoot); }
        catch (IOException e) { throw new ServletException("No se pudo crear directorio de uploads", e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String nickname = t(req.getParameter("nickname"));
        String nombre   = t(req.getParameter("nombre"));
        String email    = t(req.getParameter("email"));
        String pass1    = req.getParameter("password");
        String pass2    = req.getParameter("password2");
        String tipo     = req.getParameter("tipo"); // CLIENTE | AEROLINEA

        Part imagenPart = req.getPart("imagen");

        if (blank(nickname) || blank(nombre) || blank(email) || blank(pass1) || blank(pass2)
                || blank(tipo) || imagenPart==null || imagenPart.getSize()==0) {
            fail(req, resp, "Faltan datos obligatorios.");
            return;
        }
        if (!pass1.equals(pass2)) {
            fail(req, resp, "Las contraseñas no coinciden.");
            return;
        }
        if (!"CLIENTE".equals(tipo) && !"AEROLINEA".equals(tipo)) {
            fail(req, resp, "Tipo inválido.");
            return;
        }

        // Campos según tipo
        String apellido=null, nacionalidad=null, tipoDoc=null, numDoc=null, descripcion=null, sitio=null;
        Date fechaNac=null;

        if ("CLIENTE".equals(tipo)) {
            apellido     = t(req.getParameter("apellido"));
            nacionalidad = t(req.getParameter("nacionalidad"));
            tipoDoc      = t(req.getParameter("tipo_doc"));
            numDoc       = t(req.getParameter("num_doc"));
            String f     = t(req.getParameter("fecha_nac"));
            if (blank(apellido) || blank(nacionalidad) || blank(tipoDoc) || blank(numDoc) || blank(f)) {
                fail(req, resp, "Faltan datos de Cliente.");
                return;
            }
            try { fechaNac = Date.valueOf(f); }
            catch (IllegalArgumentException e) { fail(req, resp, "Fecha de nacimiento inválida."); return; }
        } else { // AEROLINEA
            descripcion = t(req.getParameter("descripcion"));
            sitio       = t(req.getParameter("sitio_web")); // opcional
        }

        try {
            if (usuarioDAO.existeNickname(nickname)) { fail(req, resp, "El nickname ya está en uso."); return; }
            if (usuarioDAO.existeEmail(email))      { fail(req, resp, "El email ya está en uso.");    return; }
        } catch (Exception e) {
            fail(req, resp, "Error validando unicidad: " + e.getMessage());
            return;
        }

        String imagenPath;
        try {
            imagenPath = saveImage(imagenPart);
        } catch (Exception e) {
            fail(req, resp, "No se pudo guardar la imagen: " + e.getMessage());
            return;
        }

        String passwordHash = sha256(pass1); // luego cambiamos a BCrypt

        try {
            long userId = usuarioDAO.crearUsuario(nickname, nombre, email, passwordHash, imagenPath, tipo);
            if ("CLIENTE".equals(tipo)) {
                usuarioDAO.crearCliente(userId, apellido, fechaNac, nacionalidad, tipoDoc, numDoc);
            } else {
                usuarioDAO.crearAerolinea(userId, descripcion, sitio);
            }
            req.setAttribute("ok", "¡Usuario creado (ID " + userId + ")!");
        } catch (Exception e) {
            fail(req, resp, "No se pudo crear el usuario: " + e.getMessage());
            return;
        }

        req.getRequestDispatcher("/altaUsuario.jsp").forward(req, resp);
    }

    private String saveImage(Part part) throws IOException {
        String original = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String safe = UUID.randomUUID() + "_" + original.replaceAll("[^A-Za-z0-9._-]", "_");
        Path target = uploadsRoot.resolve(safe);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        // Guardamos la ruta absoluta; si preferís relativa, ajustamos aquí:
        return target.toString();
    }

    private static boolean blank(String s){ return s==null || s.isBlank(); }
    private static String t(String s){ return s==null ? null : s.trim(); }

    private static String sha256(String s) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            var dig = md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            var sb = new StringBuilder();
            for (byte b: dig) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private void fail(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/altaUsuario.jsp").forward(req, resp);
    }
}
