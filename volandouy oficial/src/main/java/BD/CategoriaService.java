package BD;

import Logica.Categoria;
import Logica.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import Logica.DataCategoria;


public class CategoriaService {

    public List<DataCategoria> listarCategorias() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            var categorias = em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
            em.getTransaction().commit();
            // Convertir Categoria a DataCategoria
            return categorias.stream()
                .map(c -> new DataCategoria(c.getNombre()))
                .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
	
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