package uy.volando.config;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Properties;

public final class ServidorConfig {
    private static final Properties props = new Properties();
    private static boolean loaded = false;

    private ServidorConfig() {}

    private static synchronized void load() {
        if (loaded) return;

        Path file = Paths.get(System.getProperty("user.home"), "volandouy", "servidor.properties");

        try (InputStream in = Files.newInputStream(file)) {
            props.load(in);
            loaded = true;
            System.out.println("[ServidorConfig] Config cargada desde: " + file);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar servidor.properties desde " + file, e);
        }
    }

    public static String get(String key) {
        if (!loaded) load();
        return props.getProperty(key);
    }
}
