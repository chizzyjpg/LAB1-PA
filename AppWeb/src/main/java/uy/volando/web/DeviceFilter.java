package uy.volando.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class DeviceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        if (session.getAttribute("deviceType") == null) {
            DeviceUtils.DeviceType device =
                    DeviceUtils.detectDevice(req);
            session.setAttribute("deviceType", device);
        }

        chain.doFilter(request, response);
    }
}
