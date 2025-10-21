package Logica;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class JPAUtil {
    static {
        System.err.println(">>> JPAUtil <clinit> (clase cargada) — ¿quién me trajo?");
        new RuntimeException("TRACE JPAUtil <clinit>").printStackTrace();
    }

    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("persistencia");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
