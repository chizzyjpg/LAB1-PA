package uy.volando.web;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import uy.volando.publicar.WebServices;
import uy.volando.publicar.DataPaquete;
import uy.volando.publicar.DataCompraPaquete;
import uy.volando.publicar.DataCliente;

@WebServlet("/compraPaquete")
public class compraPaquete extends HttpServlet {
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
  // ============================
  // muestra la lista de paquetes y mensajes flash
  // ============================
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();
    Object usuario = session.getAttribute("usuario_logueado");
    request.setAttribute("usuario", usuario);

    List<DataPaquete> paquetes = port.listarPaquetes().getItem();
    request.setAttribute("paquetesDisponibles", paquetes);

    request.getRequestDispatcher("/WEB-INF/paquete/compraPaquete.jsp").forward(request, response);
  }

  // ============================
  // procesa la compra
  // ============================
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();

    try {
      // ---- Login y parámetros ----
      Object usuarioObj = session.getAttribute("usuario_logueado");
      if (!(usuarioObj instanceof DataCliente dc)) {
        session.setAttribute("flash_error",
            "Debes iniciar sesión como cliente para comprar un paquete.");
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;
      }
      String nombrePaquete = trimOrNull(request.getParameter("paqueteId"));
      if (nombrePaquete == null) {
        session.setAttribute("flash_error", "Debes seleccionar un paquete válido.");
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;
      }

      // ---- Validaciones previas ----
      if (!port.existePaquete(nombrePaquete)) {
        session.setAttribute("flash_error", "El paquete seleccionado no existe.");
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;
      }

      String nickname = dc.getNickname();
      if (port.clienteYaComproPaquete(nickname, nombrePaquete)) {
        session.setAttribute("flash_info", "Ya has comprado este paquete anteriormente.");
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;
      }

      DataPaquete dto = null;
      List<DataPaquete> todos = port.listarPaquetes().getItem();
      if (todos != null) {
        for (DataPaquete p : todos) {
          if (p != null && nombrePaquete.equals(p.getNombre())) {
            dto = p;
            break;
          }
        }
      }
      if (dto == null) {
        session.setAttribute("flash_error", "No fue posible cargar el detalle del paquete.");
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;
      }

      boolean sinRutas = false;
      try {
        if (dto.getRutasIncluidas() != null && dto.getRutasIncluidas().isEmpty())
          sinRutas = true;
        Integer cantRutas = dto.getCantRutas();
        if (cantRutas != null && cantRutas == 0)
          sinRutas = true;
      } catch (Throwable ignored) {
      }
      if (sinRutas) {
        session.setAttribute("flash_error", "Este paquete no posee rutas disponibles.");
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;
      }

      Integer cupos = null;
      try {
        var m = dto.getClass().getMethod("getCuposDisponibles");
        Object v = m.invoke(dto);
        if (v instanceof Number n)
          cupos = n.intValue();
      } catch (NoSuchMethodException ignored) {

      } catch (Throwable ignored) {
      }
      if (cupos != null && cupos <= 0) {
        session.setAttribute("flash_error", "No hay cupos disponibles para este paquete.");
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;
      }

      // Ejecutar compra ----
      javax.xml.datatype.DatatypeFactory dtFactory = javax.xml.datatype.DatatypeFactory.newInstance();
      java.util.Date fechaActual = new java.util.Date();
      java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
      cal.setTime(fechaActual);
      jakarta.xml.bind.annotation.XmlSchemaType schemaType = null; // No se usa directamente, solo para claridad
      javax.xml.datatype.XMLGregorianCalendar xmlFecha = dtFactory.newXMLGregorianCalendar(cal);

      DataCompraPaquete compra = new DataCompraPaquete();
      compra.setNombrePaquete(nombrePaquete);
      compra.setNicknameCliente(nickname);
      compra.setFechaCompra(xmlFecha);
      // Si tienes vencimiento y costo, asígnalos aquí:
      // compra.setVencimiento(...);
      // compra.setCosto(...);

      try {
        port.comprarPaquete(compra);
        session.setAttribute("flash_success", "¡Compra realizada con éxito!");
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;

      } catch (IllegalStateException e) {
        // CAPTURAMOS LA EXCEPCIÓN QUE TE ESTÁ ROMPIENDO
        String msg = (e.getMessage() != null) ? e.getMessage()
            : "No hay cupos disponibles para este paquete.";
        session.setAttribute("flash_error", msg);
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;

      } catch (Exception e) {
        String msg = e.getMessage();
        if (msg != null && msg.contains("No hay cupos disponibles para este paquete")) {
          msg = "No hay cupos disponibles para este paquete.";
        } else {
          msg = "No se pudo completar la compra. Intenta nuevamente.";
        }
        session.setAttribute("flash_error", msg);
        response.sendRedirect(request.getContextPath() + "/compraPaquete");
        return;
      }

    } catch (Throwable fatal) {
      // Paracaídas final: nunca permitir que burbujee al contenedor
      session.setAttribute("flash_error", "No se pudo procesar la solicitud: "
          + (fatal.getMessage() != null ? fatal.getMessage() : "error inesperado"));
      response.sendRedirect(request.getContextPath() + "/compraPaquete");
    }
  }

  // ============================
  // Helpers
  // ============================
  private static String trimOrNull(String s) {
    if (s == null)
      return null;
    String t = s.trim();
    return t.isEmpty() ? null : t;
  }
}
