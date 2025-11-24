package uy.volando.web.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class WebConfig {
    private static final Properties props = new Properties();
    private static boolean loaded = false;

    private WebConfig() {}

    private static synchronized void load() {
        if (loaded) return;

        Path file = Paths.get(System.getProperty("user.home"), "volandouy", "web.properties");

        try (InputStream in = Files.newInputStream(file)) {
            props.load(in);
            loaded = true;
            System.out.println("[WebConfig] Config cargada desde: " + file);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar web.properties desde " + file, e);
        }
    }

    public static String get(String key) {
        if (!loaded) load();
        return props.getProperty(key);
    }
}
