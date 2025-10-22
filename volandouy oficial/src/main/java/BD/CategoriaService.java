package BD;

import Logica.Categoria;
import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Categoría.
 */
public class CategoriaService {

  // ===============================
  // CREAR
  // ===============================
  /**
   * Crea una nueva categoría en la base de datos.
   *
   * 
   */
  public void crearCategoria(Categoria c) {

    EntityManager em = JPAUtil.getEntityManager();

    try {
      em.getTransaction().begin();
      /*
       * // Evitar duplicados (como el @Id es 'nombre') Categoria existente =
       * em.find(Categoria.class, nombre); if (existente != null) { throw new
       * IllegalStateException("Ya existe una categoría con ese nombre."); }
       */
      em.persist(c);

      em.getTransaction().commit();
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();        
      }
      throw ex; // propagar para que la UI decida qué mostrar
    } finally {
      em.close();
    }
  }

  // ===============================
  // LISTAR / BUSCAR / EXISTE
  // ===============================
  /**
   * Lista todas las categorías en la base de datos.
   *
   * 
   */
  public List<Categoria> listarCategorias() {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      var categorias = em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
      em.getTransaction().commit();
      return categorias;
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();        
      }
      throw ex;
    } finally {
      em.close();
    }
  }

  /**
   * Busca una categoría por su nombre.
   *
   * 
   */
  public Categoria buscarPorNombre(String nombre) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      List<Categoria> categorias = em
          .createQuery("from Categoria c where c.nombre = :nombre", Categoria.class)
          .setParameter("nombre", nombre).getResultList();
      em.getTransaction().commit();
      return categorias.isEmpty() ? null : categorias.get(0);
    } catch (RuntimeException ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();        
      }
      throw ex;
    } finally {
      em.close();
    }
  }

  /**
   * Verifica si una categoría existe por su nombre.
   *
   * 
   */
  public boolean existeCategoria(String nombre) {
    return buscarPorNombre(nombre) != null;
  }
}