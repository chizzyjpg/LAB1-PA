package Logica;

//import jakarta.persistence.*;

/**
 * Clase singleton que provee acceso a la instancia del sistema.
 */
public final class Fabrica {

  private static Fabrica instancia = null;
  private final ISistema sistema;

  private Fabrica() {

    // 1) En memoria:
    this.sistema = new Sistema();

    // 2) Con JPA:

    /*
     * EntityManagerFactory emf =
     * Persistence.createEntityManagerFactory("volandoUy"); EntityManager em =
     * emf.createEntityManager(); this.sistema = new SistemaJpa(em);
     */
  }

  public static synchronized Fabrica getInstance() { // synchronized es una palabra clave que
                                                     // controla el acceso a recursos compartidos
                                                     // entre múltiples hilos, garantizando que solo
                                                     // un hilo pueda ejecutar un método o bloque de
                                                     // código a la vez.
    if (instancia == null) {
      instancia = new Fabrica();
    }
    return instancia;
  }

  public ISistema getSistema() {
    return sistema;
  }

}
