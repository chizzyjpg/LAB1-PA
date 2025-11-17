package uy.volando.web;

import jakarta.servlet.http.HttpServletRequest;

public class DeviceUtils {

    public enum DeviceType {
        MOBILE, DESKTOP, UNKNOWN
    }

    public static DeviceType detectDevice(HttpServletRequest req) {
        String ua = req.getHeader("User-Agent");
        if (ua == null) {
            return DeviceType.UNKNOWN;
        }
        ua = ua.toLowerCase();

        // Muy simple, pero suficiente para la materia
        if (ua.contains("android") || ua.contains("iphone") ||
                ua.contains("ipad")   || ua.contains("ipod")) {
            return DeviceType.MOBILE;
        }

        return DeviceType.DESKTOP;
    }
}
