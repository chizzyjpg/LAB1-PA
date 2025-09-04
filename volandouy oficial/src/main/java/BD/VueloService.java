package BD;

import Logica.Usuario;
import Logica.VueloEspecifico;
import Logica.JPAUtil;
import Logica.ManejadorRuta;
import Logica.Ruta;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import Logica.Aerolinea;
import Logica.Cliente;
import Logica.DataAerolinea;
import Logica.DataCliente;
import Logica.DataRuta;
import Logica.DataUsuario;
import Logica.DataUsuarioAux;
import Logica.DataVueloEspecifico;

public class VueloService {
	
	public void registrarVuelo(VueloEspecifico datos) {
		
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			
			em.persist(datos);
			
			em.getTransaction().commit();
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			throw ex; // propagar para que la UI decida qué mostrar
		} finally {
			em.close();
		}
	}
}
