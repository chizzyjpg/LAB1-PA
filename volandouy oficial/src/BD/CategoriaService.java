package BD;

import Logica.Categoria;
import Logica.JPAUtil;
import jakarta.persistence.EntityManager;


public class CategoriaService {

    public void crearCategoria(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // Evitar duplicados (como el @Id es 'nombre')
            Categoria existente = em.find(Categoria.class, nombre);
            if (existente != null) {
                throw new IllegalStateException("Ya existe una categoría con ese nombre.");
            }

            Categoria c = new Categoria(nombre.trim());
            em.persist(c);

            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex; // propagar para que la UI decida qué mostrar
        } finally {
            em.close();
        }
    }
}
