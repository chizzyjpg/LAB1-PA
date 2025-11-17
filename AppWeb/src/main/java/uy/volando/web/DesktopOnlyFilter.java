package uy.volando.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter({
        "/reservaVuelo",
        "/compraPaquete",
        "/altaVuelo",
        "/regRutVuelo",
        "/consultaPaqRutasVuelo",
        "/listado-usuarios",
        "/regRutVuelo",

})
public class DesktopOnlyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        DeviceUtils.DeviceType deviceType = null;
        if (session != null) {
            deviceType = (DeviceUtils.DeviceType) session.getAttribute("deviceType");
        }

        // Si es móvil, NO dejamos entrar a estas URLs
        if (deviceType == DeviceUtils.DeviceType.MOBILE) {
            System.out.println("[DesktopOnlyFilter] Bloqueando acceso móvil a " + req.getRequestURI());
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // Si no es móvil, dejamos pasar
        chain.doFilter(request, response);
    }
}
