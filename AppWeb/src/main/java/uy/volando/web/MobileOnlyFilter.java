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

@WebFilter({"/checkInReservaVuelo"}) //Cuando se haga hay que cambiar aca
public class MobileOnlyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        DeviceUtils.DeviceType deviceType = null;
        if (session != null) {
            deviceType = (DeviceUtils.DeviceType) session.getAttribute("deviceType");
        }

        if (deviceType == DeviceUtils.DeviceType.DESKTOP) {
            System.out.println("[MobileOnlyFilter] Bloqueando acceso de escritorio a " + req.getRequestURI());
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        chain.doFilter(request, response);
    }
}

