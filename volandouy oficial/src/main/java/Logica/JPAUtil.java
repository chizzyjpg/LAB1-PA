package Logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase utilitaria para manejar la fábrica de EntityManager.
 */
public class JPAUtil {
  static {
    System.err.println(">>> JPAUtil <clinit> (clase cargada) — ¿quién me trajo?");
    new RuntimeException("TRACE JPAUtil <clinit>").printStackTrace();
    
    System.out.println("Probe=" + Thread.currentThread()
    .getContextClassLoader()
    .getResource("META-INF/persistence.xml"));

  }
  
  

  private static final EntityManagerFactory emf = Persistence
      .createEntityManagerFactory("persistencia");

  public static EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  /**
   * Cierra la fábrica de EntityManager.
   */
  public static void close() {
    emf.close();
  }
}
