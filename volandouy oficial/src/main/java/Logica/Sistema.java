package Logica;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import BD.AerolineaService;
import BD.CategoriaService;
import BD.CiudadService;
import BD.ClienteService;
import BD.PaqueteService;
import BD.ReservaService;
import BD.RutaVueloService;
import BD.UsuarioService;
import BD.VueloService;


//import javax.swing.JOptionPane;


public class Sistema implements ISistema {
    

    // “Persistencia” en memoria por ahora.
    private final Map<String, Usuario> usuariosPorNickname = new HashMap<>(); // guardamos ENTIDADES (dominio), indexadas por nickname
    private final Map<Long, Ciudad> CiudadPorHash = new HashMap<>(); // guardamos ENTIDADES (dominio), indexadas por hashcode
    private final Map<String, Categoria> categoriasPorNombre = new LinkedHashMap<>();
    private final Map<String, Paquete> paquetesPorNombre = new HashMap<>(); // clave: nombre canónico
    private final List<CompraPaquete> compras = new ArrayList<>();
    private int compraIdSeq = 1;  // Contador de IDs para compras (auto incremental en memoria)
    
    private final UsuarioService usuarioService = new UsuarioService();
    private final CategoriaService categoriaService = new CategoriaService();
    private final CiudadService ciudadService = new CiudadService();
    private final PaqueteService paqueteService = new PaqueteService();
    private final VueloService vueloService = new VueloService();
    private final ClienteService clienteService = new ClienteService();
    private final ReservaService reservaService = new ReservaService();
    public Sistema() {}
    
    
    // ======================
    //  REGISTRAR USUARIOS
    // ======================
    
    
 // Normaliza claves para que "Juan", "juAN" y "juan" choquen correctamente ////// HELPERS
    
 // Helper en Sistema
    private static int nextReservaId(Set<Reserva> reservas) {
        int max = 0;
        for (Reserva r : reservas) {
            if (r.getIdReserva() > max) {
                max = r.getIdReserva();
            }
        }
        return max + 1;
    }
    
    
    private static String canonical(String s) {
        return (s == null) ? null : s.trim().toLowerCase(Locale.ROOT);
    }
    
    private static Date copia(java.util.Date d) {
        return (d == null) ? null : new java.util.Date(d.getTime());
    }

    @Override
    public void registrarUsuario(DataUsuario data) {
        if (data == null) throw new IllegalArgumentException("Los datos no pueden ser nulos");
        ManejadorUsuario.toEntity(data);

        /*
         
        // Validaciones de unicidad
        if (existeNickname(data.getNickname())) {
            throw new IllegalArgumentException("El nickname ya está en uso");
        }
        if (existeEmail(data.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
         */
        
        
        
        // DTO -> Entidad (polimórfico)
        		//Usuario entity = 
        
        //System.out.println(entity.toString());
        

        // Clave canónica
        //String key = canonical(entity.getNickname());
        //usuariosPorNickname.put(key, entity);
    }

    @Override
    public boolean existeNickname(String nickname) {
        if (nickname == null) return false;
        return usuariosPorNickname.containsKey(canonical(nickname));
    }

    @Override
    public boolean existeEmail(String email) {
        if (email == null) return false;
        String e = canonical(email);
        return usuariosPorNickname.values().stream()
                .anyMatch(u -> u.getEmail() != null
                        && canonical(u.getEmail()).equals(e));
    }

    @Override
    public DataCliente verInfoCliente(String nickname) {
       DataUsuario usuario = usuarioService.verInfoUsuario(nickname);
       if (usuario instanceof DataCliente) {
		   return (DataCliente) usuario;
	   }
       return null;
    }

    @Override
    public DataAerolinea verInfoAerolinea(String nickname) {
        DataUsuario usuario = usuarioService.verInfoUsuario(nickname);
        if (usuario instanceof DataAerolinea) {
            return (DataAerolinea) usuario;
        }
        return null;
    }
    
    @Override
    public List<DataUsuario> listarUsuarios() {
    	return usuarioService.listarUsuarios().stream()
		        .sorted(Comparator.comparing(
		            u -> u.getNickname() == null ? "" : u.getNickname(),
		            String.CASE_INSENSITIVE_ORDER
		        ))
		        .collect(Collectors.toList());
    }
    
    
    @Override
    public List<DataAerolinea> listarAerolineas() {
    	return usuarioService.listarUsuarios().stream()
    	        .filter(DataAerolinea.class::isInstance)
    	        .map(DataAerolinea.class::cast)
    	        .sorted(Comparator.comparing(
    	            a -> a.getNickname() == null ? "" : a.getNickname(),
    	            String.CASE_INSENSITIVE_ORDER
    	        ))
    	        .collect(Collectors.toList());
    
    	/*
    	UsuarioService aerolinea = new UsuarioService();
    	return aerolinea.listarAerolineas();
    	List<DataAerolinea> aerolineas = new ArrayList<>();
    	for (Usuario u : usuariosPorNickname.values()) {
    		if (u instanceof Aerolinea a) {
    			aerolineas.add(ManejadorAerolinea.toData(a));
    		}
    	}
    	aerolineas.sort(Comparator.comparing(
    			a -> a.getNickname() == null ? "" : a.getNickname(),
    					String.CASE_INSENSITIVE_ORDER));
    	return aerolineas;
		 * */
	}
   
	/*
	 * mirar
	 * 
	 public List<DataCliente> listarClientes() {
		 List<DataCliente> clientes = new ArrayList<>();
		 for (Usuario u : usuariosPorNickname.values()) {
			 if (u instanceof Cliente c) {
				 clientes.add(ManejadorCliente.toData(c));
			 }
		 }
		 clientes.sort(Comparator.comparing(
				 c -> c.getNickname() == null ? "" : c.getNickname(),
				 String.CASE_INSENSITIVE_ORDER));
		 return clientes;
	 }
	 */
 
	 public List<DataCliente> listarClientes() {
		 return clienteService.listarClientes().stream()
			        .sorted(Comparator.comparing(
			            c -> c.getNickname() == null ? "" : c.getNickname(),
			            String.CASE_INSENSITIVE_ORDER
			        ))
			        .collect(Collectors.toList());
		 /*
		 ClienteService clienteService = new ClienteService();
		return clienteService.listarClientes();
		 return usuarioService.listarUsuarios().stream()
			        .filter(DataCliente.class::isInstance)
			        .map(DataCliente.class::cast)
			        .sorted(Comparator.comparing(
			            c -> c.getNickname() == null ? "" : c.getNickname(),
			            String.CASE_INSENSITIVE_ORDER
			        ))
			        .collect(Collectors.toList());*/
	 }
	 
    // ======================
    //  MODIFICAR USUARIOS
    // ======================
    
    @Override
    public void modificarCliente(String nickname, DataCliente nuevos) {
        if (nuevos == null) throw new IllegalArgumentException("Datos de cliente no pueden ser nulos");
        String key = canonical(nickname);
        Cliente cliente = usuarioService.obtenerClientePorNickname(key);
        if (cliente == null)
            throw new IllegalArgumentException("No existe un cliente con ese nickname");

        // Validar que NO se cambie email ni nickname
        String emailActual = cliente.getEmail();
        String emailNuevo  = nuevos.getEmail();
        if (emailNuevo != null && !canonical(emailNuevo).equals(canonical(emailActual))) {
            throw new IllegalArgumentException("No se permite modificar el correo electrónico.");
        }
        if (nuevos.getNickname() != null && !canonical(nuevos.getNickname()).equals(key)) {
            throw new IllegalArgumentException("No se permite modificar el nickname.");
        }
        // Actualizar SOLO campos permitidos usando setters de Cliente
        cliente.setNombre(nuevos.getNombre());
        cliente.setApellido(nuevos.getApellido());
        cliente.setFechaNac(copia(nuevos.getFechaNac()));
        cliente.setNacionalidad(nuevos.getNacionalidad());
        cliente.setTipoDocumento(nuevos.getTipoDocumento());
        cliente.setNumDocumento(nuevos.getNumDocumento());
        // Persistir cambios en la base de datos usando el manejador
        usuarioService.actualizarUsuario(cliente);// ESTO TIENE QUE USAR EL MANEJADOR CREO
    }

    @Override
    public void modificarAerolinea(String nickname, DataAerolinea nuevos) {
        if (nuevos == null) throw new IllegalArgumentException("Datos de aerolínea no pueden ser nulos");

        String key = canonical(nickname);
        Aerolinea aerolinea = usuarioService.obtenerAerolineaPorNickname(key);
        if (aerolinea == null)
            throw new IllegalArgumentException("No existe una aerolínea con ese nickname");

        // Validar que NO se cambie email ni nickname
        String emailActual = aerolinea.getEmail();
        String emailNuevo  = nuevos.getEmail();
        if (emailNuevo != null && !canonical(emailNuevo).equals(canonical(emailActual))) {
            throw new IllegalArgumentException("No se permite modificar el correo electrónico.");
        }
        if (nuevos.getNickname() != null && !canonical(nuevos.getNickname()).equals(key)) {
            throw new IllegalArgumentException("No se permite modificar el nickname.");
        }

        // Actualizar SOLO campos básicos permitidos
        aerolinea.setNombre(nuevos.getNombre());
        aerolinea.setDescGeneral(nuevos.getDescripcion());
        aerolinea.setLinkWeb(nuevos.getSitioWeb());
        
        usuarioService.actualizarUsuario(aerolinea); // ESTO TIENE QUE USAR EL MANEJADOR CREO
    }
    
    // ======================
    //  CREAR CATEGORIA
    // ======================
    
    @Override
    public void registrarCategoria(DataCategoria data) {
    	ManejadorCategoria.toEntity(data);
        /*
    	if (data == null || data.getNombre() == null || data.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        
        String key = canonical(data.getNombre());
        if (categoriasPorNombre.containsKey(key))
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        
        categoriasPorNombre.put(key, ManejadorCategoria.toEntity(data));
        */
    }
    
    @Override
    public boolean existeCategoria(String nombre) {
        return nombre != null && categoriasPorNombre.containsKey(canonical(nombre));
    }
    
    @Override
    public List<DataCategoria> listarCategorias() {
    	return categoriaService.listarCategorias().stream()
		        .map(ManejadorCategoria::toData)
		        .sorted(Comparator.comparing(
		            c -> c.getNombre() == null ? "" : c.getNombre(),
		            String.CASE_INSENSITIVE_ORDER
		        ))
		        .collect(Collectors.toList());
    	/*
    	List<Categoria> categorias = categoriaService.listarCategorias();
    	List<DataCategoria> dataCategorias = new ArrayList<>();
    	for (Categoria c : categorias) {
    		dataCategorias.add(new DataCategoria(c.getNombre()));
    	}
    	return dataCategorias;*/
    } 
    
    // =========================
    // REGISTRAR RUTAS DE VUELO
    // =========================
    
    @Override
    /*
    public void registrarRuta(DataRuta datos) {
        if (datos != null) {
            Ruta ruta = ManejadorRuta.toEntity(datos); // Solo crea la entidad, no la persiste
            String nicknameAerolinea = datos.getNicknameAerolinea();
            new RutaVueloService().crearRutaVuelo(ruta, nicknameAerolinea);
            System.out.println(datos.toString());
        }
    }*/
    public void registrarRuta(DataRuta datos) {
		if (datos == null) throw new IllegalArgumentException("Los datos de la ruta no pueden ser nulos");
		
		String nombre = (datos.getNombre() == null) ? "" : datos.getNombre().trim();
		if (nombre.isEmpty()) {
		    throw new IllegalArgumentException("El nombre de la ruta no puede estar vacío");
		}
		String nickAerolinea = datos.getNicknameAerolinea();
		// Busca en TODAS las rutas de TODAS las aerolíneas (igualdad exacta)
		Aerolinea aerolinea = usuarioService.obtenerAerolineaPorNickname(nickAerolinea);
		if (aerolinea == null) {
		    throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		boolean existe = aerolinea.getRutas().stream()
		    .anyMatch(r -> nombre.equals(r.getNombre()));
		if (existe) {
		    throw new IllegalArgumentException("Ya existe una ruta llamada exactamente: " + nombre);
		}
		
		
		
		/*String nombre = (datos.getNombre() == null) ? "" : datos.getNombre().trim();
		if (nombre.isEmpty()) {
		    throw new IllegalArgumentException("El nombre de la ruta no puede estar vacío");
		}
																										////////////////////////
		Long cnt = em.createQuery(																		//// PARA JPA  /////////
		    "select count(r) from Ruta r where r.nombre = :n", Long.class)								////////////////////////
		    .setParameter("n", nombre)
		    .getSingleResult();

		if (cnt > 0) {
		    throw new IllegalArgumentException("Ya existe una ruta llamada exactamente: " + nombre);

	    Usuario u = usuarioService.obtenerAerolineaPorNickname(nickAerolinea);  
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		}*/
		Ruta ruta = ManejadorRuta.toEntity(datos); // Solo crea la entidad, no la persiste
        //String nicknameAerolinea = datos.getNicknameAerolinea();
        new RutaVueloService().crearRutaVuelo(ruta, nickAerolinea);
	}
    
//    private void validarBasico(Ruta r) {
//        if (r.getNombre()==null || r.getNombre().isBlank()) throw new IllegalArgumentException("nombre obligatorio");
//        if (r.getDescripcion()==null || r.getDescripcion().isBlank()) throw new IllegalArgumentException("descripcion obligatoria");
//        if (r.getOrigen()==null)  throw new IllegalArgumentException("ciudad origen obligatoria");
//        if (r.getDestino()==null) throw new IllegalArgumentException("ciudad destino obligatoria");
//        if (r.getFechaAlta()==null) throw new IllegalArgumentException("fecha de alta obligatoria");
//    }
    
    public List<DataRuta> listarPorAerolinea(String nicknameAerolinea) {
        DataUsuario usuario = usuarioService.verInfoUsuario(nicknameAerolinea);
        if (!(usuario instanceof DataAerolinea)) {
            throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
        }
        List<DataRuta> rutas = usuarioService.listarRutasPorAerolinea(nicknameAerolinea);
        return rutas.stream()
				.sorted(Comparator.comparing(
						r -> r.getNombre() == null ? "" : r.getNombre(),
						String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
    }
    
    
    // =========================
	//  	   CIUDADES
	// =========================
	
	@Override
	public void registrarCiudad(DataCiudad data) {
		if (data == null) throw new IllegalArgumentException("Los datos no pueden ser nulos");

		// Validaciones de unicidad
		long hash = data.getNombre().toLowerCase().hashCode() + 31 * data.getPais().toLowerCase().hashCode();
		if (CiudadPorHash.containsKey(hash)) {
			throw new IllegalArgumentException("La ciudad ya está registrada");
		}

		ManejadorCiudad.toEntity(data);
		// DTO -> Entidad

		//Ciudad entity = 
		//CiudadPorHash.put(hash, entity);
	}

	@Override
	public List<DataCiudad> listarCiudades() {
		return ciudadService.listarCiudades().stream()
		        .map(ManejadorCiudad::toData)
		        .sorted(Comparator.comparing(
		            c -> c.getNombre() == null ? "" : c.getNombre(),
		            String.CASE_INSENSITIVE_ORDER
		        ))
		        .collect(Collectors.toList());
		/*
		List<Ciudad> ciudades = ciudadService.listarCiudades();
		List<DataCiudad> dataCiudades = new ArrayList<>();
		for (Ciudad c : ciudades) {
			dataCiudades.add(new DataCiudad(c.getNombre(), c.getPais(), c.getNombreAeropuerto(), c.getDescripcionAeropuerto(), c.getFechaAlta(), c.getSitioWeb()));
		}
		return dataCiudades;*/
	}
	
	public Ciudad buscarCiudad(String nombre, String pais) {
		if (nombre == null || nombre.isBlank() || pais == null || pais.isBlank()) {
			return null;
		}
		long hash = nombre.toLowerCase().hashCode() + 31 * pais.toLowerCase().hashCode();
		return CiudadPorHash.get(hash);
	}
	
	// =========================
	//  	   VUELOS
	// =========================
	
	@Override
	public void registrarVuelo(String nickname, String nombre, DataVueloEspecifico datos) {
		if (datos == null) throw new IllegalArgumentException("Los datos del vuelo no pueden ser nulos");
		// Buscar el id de la ruta persistida por nombre
		RutaVueloService rutaService = new RutaVueloService();
		Integer idRuta = rutaService.buscarRutaPorNombreYObtenerId(nombre);
		if (idRuta == null) {
			throw new IllegalArgumentException("No existe una ruta con ese nombre en la base de datos");
		}
		// Buscar la entidad Ruta persistida usando el id
		Ruta ruta = rutaService.buscarRutaPorId(idRuta);
		if (ruta == null) {
			throw new IllegalArgumentException("No se encontró la Ruta con id: " + idRuta);
		}
		// Crear el vuelo específico usando el manejador, pero con la ruta persistida
		//ANDA
		/*VueloEspecifico vuelo = new VueloEspecifico(
			    datos.getNombre(),
			    datos.getFecha(),
			    datos.getDuracion(),
			    datos.getMaxAsientosTur(),
			    datos.getMaxAsientosEjec(),
			    datos.getFechaAlta(),
			    ruta // Usa la entidad persistida directamente
			);*/
		
		ManejadorVueloEspecifico.toEntity(datos, ruta);
			//vueloService.registrarVuelo(vuelo);
		/*
		 * NO ANDA
		DataVueloEspecifico datosConRuta = new DataVueloEspecifico(
			datos.getNombre(),
			datos.getFecha(),
			datos.getDuracion(),
			datos.getMaxAsientosTur(),
			datos.getMaxAsientosEjec(),
			datos.getFechaAlta(),
			ManejadorRuta.toData(ruta)
		);
		VueloEspecifico vuelo = ManejadorVueloEspecifico.toEntity(datosConRuta);
		vueloService.registrarVuelo(vuelo);
		*/
		}
	
	@Override
	public List<DataVueloEspecifico> listarVuelos(String nickname, String nombre) {
	    // Obtener la aerolínea desde la base de datos
	    DataAerolinea aerolinea = verInfoAerolinea(nickname);
	    if (aerolinea == null) {
	        throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
	    }
	    // Obtener la ruta desde la base de datos
	    List<DataRuta> rutas = usuarioService.listarRutasPorAerolinea(nickname);
	    DataRuta ruta = rutas.stream()
	        .filter(r -> r.getNombre() != null && r.getNombre().equalsIgnoreCase(nombre))
	        .findFirst().orElse(null);
	    if (ruta == null) {
	        throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
	    }
	    // Obtener los vuelos específicos de la ruta desde la base de datos
	    List<DataVueloEspecifico> vuelos = usuarioService.listarVuelosPorRuta(nickname, nombre);
	    return vuelos == null ? Collections.emptyList() : vuelos;
	}

	@Override
	public DataVueloEspecifico buscarVuelo(String nickname, String nombre, String codigoVuelo) {
		Usuario u = usuariosPorNickname.get(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutas().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		VueloEspecifico v = r.getVuelosEspecificos().stream()
			.filter(ve -> ve.getNombre() != null && ve.getNombre().equalsIgnoreCase(codigoVuelo))
			.findFirst().orElse(null);
		if (v == null) {
			throw new IllegalArgumentException("No existe un vuelo con ese código en la ruta indicada");
		}
		return ManejadorVueloEspecifico.toData(v);
	}
	
		// =========================
		//  	COMPRA DE PAQUETE
		// =========================
    
	@Override
	public List<DataPaquete> listarPaquetesDisponiblesParaCompra() {
		PaqueteService paqueteService = new PaqueteService();
		return paqueteService.listarPaquetes().stream()
		        .filter(p -> p.getCantRutas() > 0)
		        .map(ManejadorPaquete::toDTO)
		        .sorted(Comparator.comparing(
		            p -> p.getNombre() == null ? "" : p.getNombre(),
		            String.CASE_INSENSITIVE_ORDER
		        ))
		        .collect(Collectors.toList());
		/*
		List<Paquete> elegibles = paquetesPorNombre.values().stream()
	            .filter(p -> p.getCantRutas() > 0)
	            .sorted(Comparator.comparing(Paquete::getNombre, String.CASE_INSENSITIVE_ORDER))
	            .collect(Collectors.toList());
		
		//System.out.println(elegibles.toString());
		
	    return ManejadorPaquete.toDTOs(elegibles);
	    */
	}
	

	@Override
	public List<DataCliente> listarClientesParaCompra() {
		ClienteService clienteService = new ClienteService();
		return clienteService.listarClientes();
		// Usamos tu ManejadorUsuario para convertir ENTIDAD -> DTO
		/*
		return usuariosPorNickname.values().stream()
	            .filter(u -> u instanceof Cliente)
	            .map(u -> (Cliente) u)
	            .sorted(Comparator.comparing(Cliente::getNickname, String.CASE_INSENSITIVE_ORDER))
	            .map(c -> (DataCliente) ManejadorUsuario.toDTO(c))
	            .collect(Collectors.toList());
	
		 */
		}
	
	@Override
	public boolean clienteYaComproPaquete(String nicknameCliente, String nombrePaquete) {
	    /*
		String keyNick = canonical(nicknameCliente);
	    String keyPack = canonical(nombrePaquete);
	    return compras.stream().anyMatch(cp ->
	            canonical(cp.getCliente().getNickname()).equals(keyNick) &&
	            canonical(cp.getPaquete().getNombre()).equals(keyPack)
	    );
	    */
	    return usuarioService.clienteYaComproPaquete(nicknameCliente, nombrePaquete);
	}
	
	@Override
	public void comprarPaquete(DataCompraPaquete compra) {
        if (compra == null)
            throw new IllegalArgumentException("Datos de compra nulos");

	    // Resuelvo ENTIDADES a partir de los identificadores del DTO
	    //Paquete paquete = paquetesPorNombre.get(canonical(compra.getNombrePaquete()));
	    Paquete paquete = paqueteService.existePaquete(compra.getNombrePaquete());
	    if (paquete == null) throw new IllegalArgumentException("Paquete inexistente: " + compra.getNombrePaquete());
	    if (paquete.getCantRutas() <= 0) throw new IllegalStateException("El paquete no tiene rutas");
	    
	    //Usuario u = usuariosPorNickname.get(canonical(compra.getNicknameCliente()));
	    Usuario u = usuarioService.obtenerClientePorNickname(compra.getNicknameCliente());
	    if (!(u instanceof Cliente cliente))
	        throw new IllegalArgumentException("Cliente inexistente: " + compra.getNicknameCliente());

        // Verificar cupos disponibles antes de permitir la compra
        if (paquete.getCuposDisponibles() <= 0) {
            throw new IllegalStateException("No hay cupos disponibles para este paquete");
        }

        if (clienteYaComproPaquete(compra.getNicknameCliente(), compra.getNombrePaquete())) {
            throw new IllegalArgumentException("El cliente ya compró este paquete");
        }
        
        if (compra.getFechaCompra() == null)
			throw new IllegalArgumentException("La fecha de compra es obligatoria");

		// Restar un cupo disponible al paquete
		paquete.setCuposDisponibles(paquete.getCuposDisponibles() - 1);
		paqueteService.actualizarPaquete(paquete);

		// Crear la entidad CompraPaquete y persistirla usando el manejador
		ManejadorCompraPaquete.toEntity(compra, cliente, paquete);
	}
	
			// ===============================
			//  PRECARGA CLIENTES Y PAQUETES
			// ===============================
/*
	public void precargaDemo() {
	    registrarUsuario(new DataCliente("Ana","ana01","ana@mail.com","Pérez", new Date(), "UY", TipoDocumento.CEDULA, "52559649"));
	    registrarUsuario(new DataCliente("Bruno","bruno02","bruno@mail.com","López", new Date(), "UY", TipoDocumento.PASAPORTE, "54985693"));
	    
	    registrarUsuario(new DataAerolinea("Copa","copa","copa@mail.com", "DESCcopa", "SITIOcopa"));
	    registrarUsuario(new DataAerolinea("Jet","jet","jet@mail.com", "DESC", "SITIO"));
	    
	    registrarCategoria(new DataCategoria("Económica"));
	    
	    registrarCiudad(new DataCiudad("Montevideo","Uruguay", "Carrasco", "Ciudad capital", null, "sitio"));

	    DataPaquete rp  = new DataPaquete("Promo Río","Paquete con rutas a Río",2,TipoAsiento.TURISTA,20,30, BigDecimal.valueOf(1200));
	    DataPaquete rp2 = new DataPaquete("Europa Express","Rutas a Europa",3,TipoAsiento.EJECUTIVO,15,60, BigDecimal.valueOf(3200));

	    Paquete p1 = ManejadorPaquete.toEntity(rp);
	    Paquete p2 = ManejadorPaquete.toEntity(rp2);

	    // Guardar con la MISMA normalización que usás al buscar
	    paquetesPorNombre.put(canonical(rp.getNombre()),  p1);
	    paquetesPorNombre.put(canonical(rp2.getNombre()), p2);
	}
	*/
			// ===============================
			//  ALTA PAQUETE
			// ===============================
	
	
	public void registrarPaquete(DataPaqueteAlta data) {
        // Validaciones básicas de entrada
        if (data == null) throw new IllegalArgumentException("Datos del paquete nulos");
        if (data.getNombre() == null || data.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del paquete es obligatorio");
        if (data.getDescripcion() == null || data.getDescripcion().isBlank())
            throw new IllegalArgumentException("La descripción es obligatoria");
        if (data.getValidez() <= 0)
            throw new IllegalArgumentException("La validez debe ser mayor a 0");
        if (data.getDescuento() < 0 || data.getDescuento() > 100)
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
        if (data.getFechaAlta() == null)
            throw new IllegalArgumentException("La fecha de alta es obligatoria");
        
        ManejadorPaquete.altaToEntity(data);
        // Unicidad por nombre
        /*
        String key = canonical(data.getNombre());
        if (paquetesPorNombre.containsKey(key))
            throw new IllegalArgumentException("Ya existe un paquete con ese nombre");
        paquetesPorNombre.put(key, entity);
        Paquete entity = 
        */
	}
	
	 @Override
	    public boolean existePaquete(String nombre) {
	        return nombre != null && paquetesPorNombre.containsKey(canonical(nombre));
	    }
	 
	 @Override
	    public List<DataPaquete> listarPaquetes() {
	        // Devolver DTOs usando tu manejador
		 	PaqueteService paqueteService = new PaqueteService();
		 	List<Paquete> paquetes = paqueteService.listarPaquetes();
		 	return ManejadorPaquete.toDTOs(paquetes);
	    }
	 
	 @Override
	 public DataPaquete verPaquete(String nombre) {
	     if (nombre == null) return null;
	     PaqueteService paqueteService = new PaqueteService();
	     Paquete p = paqueteService.existePaquete(nombre);
	     return (p == null) ? null : ManejadorPaquete.toDTO(p);
	 }
	 
	 			// ===============================
				//  AGREGAR RUTA DE VUELO A PAQUETE
				// ===============================
	 	
	 @Override
	 public List<DataPaquete> listarPaquetesSinCompras() {
	     // un paquete “con compras” es aquel que aparece en la lista compras
	     PaqueteService paqueteService = new PaqueteService();
	     List<Paquete> paquetes = paqueteService.listarPaquetes();
		 Set<String> nombresConCompra = compras.stream()
	             .map(cp -> canonical(cp.getPaquete().getNombre()))
	             .collect(Collectors.toSet());
		 
	     return paquetes.stream()
	             .filter(p -> !nombresConCompra.contains(canonical(p.getNombre())))
	             .sorted(Comparator.comparing(Paquete::getNombre, String.CASE_INSENSITIVE_ORDER))
	             .map(ManejadorPaquete::toDTO)
	             .collect(Collectors.toList());
	 }
	 	 
	 @Override
	 public void agregarRutaAPaquete(String nombrePaquete,
	                                 String nicknameAerolinea,
	                                 String nombreRuta,
	                                 TipoAsiento tipo,
	                                 int cantidad
	                                 ) {

	     if (nombrePaquete == null || nicknameAerolinea == null || nombreRuta == null || tipo == null || cantidad <= 0)
	         throw new IllegalArgumentException("Datos incompletos");
	     
	     //System.out.println(nombrePaquete + " " + nicknameAerolinea + " " + nombreRuta + " " +  tipo + " " + cantidad );
	
	     // 1) Paquete (y no permitir si ya tiene compras)
	     PaqueteService paqueteService = new PaqueteService();
	     Paquete p = paqueteService.existePaquete(nombrePaquete);
	     if (p == null) throw new IllegalArgumentException("No existe un paquete con ese nombre");
	
	     boolean tieneCompras = compras.stream()
	             .anyMatch(cp -> canonical(cp.getPaquete().getNombre()).equals(canonical(nombrePaquete)));
	     if (tieneCompras) throw new IllegalStateException("El paquete ya tiene compras y no admite modificaciones");
	
	     // 2) Aerolínea
	     //Usuario u = usuariosPorNickname.get(canonical(nicknameAerolinea));
	     usuarioService.verInfoUsuario(nicknameAerolinea);
	     /*
	     if (!(u instanceof Aerolinea a)) {
	         throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
	     }
	      */
	     // 3) Ruta de esa aerolínea por NOMBRE
	     Ruta r = paqueteService.buscarRutaEnAerolinea(nicknameAerolinea, nombreRuta);
	     //////////////////////////////////////////////////////////////////////////////////////////////////////
	
	     // fijar/validar tipo de asiento único del paquete
	     if (p.getTipoAsiento() == null) {
	         p.setTipoAsiento(tipo);
	     } else if (p.getTipoAsiento() != tipo) {
	         throw new IllegalArgumentException(
	             "El tipo de asiento del paquete (" + p.getTipoAsiento()
	             + ") no coincide con el seleccionado (" + tipo + ")."
	         );
	     }
	     
	     BigDecimal cantidadBD = BigDecimal.valueOf(cantidad);
	     BigDecimal descuentoFactor = BigDecimal.valueOf((100 - p.getDescuento()) / 100.0);
	     
	     if (p.getCosto() == null) {
	         p.setCosto(BigDecimal.ZERO);
	     }
	     
	     if(tipo == TipoAsiento.TURISTA) {
	         p.setCosto(p.getCosto().add(r.getCostoTurista().multiply(cantidadBD).multiply(descuentoFactor)));
	     }
	     else if(tipo == TipoAsiento.EJECUTIVO) {
	         p.setCosto(p.getCosto().add(r.getCostoEjecutivo().multiply(cantidadBD).multiply(descuentoFactor)));
	     }
	     // agregar ruta por NOMBRE (único)
		 p.setCuposDisponibles(cantidad);
         p.setCuposMaximos(cantidad);
         p.addRutaPorNombre(r.getNombre());
	     paqueteService.actualizarPaquete(p);
	     }

	
	// =========================
	//   CONSULTA DE PAQUETE
	// =========================
	 
	 
	
	// =========================
	//         RESERVAS
	// =========================
	
	@Override
	public List<DataReserva> listarReservas(String nickname, String nombre, String codigoVuelo) {
		Usuario u = usuarioService.obtenerAerolineaPorNickname(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutas().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		VueloEspecifico v = r.getVuelosEspecificos().stream()
			.filter(ve -> ve.getNombre() != null && ve.getNombre().equalsIgnoreCase(codigoVuelo))
			.findFirst().orElse(null);
		if (v == null) {
			throw new IllegalArgumentException("No existe un vuelo con ese código en la ruta indicada");
		}
		Collection<Reserva> reservas = v.getReservas().stream()
				.sorted(Comparator.comparingInt(Reserva::getIdReserva))
				.collect(Collectors.toList());
		return ManejadorReserva.toDatas(new ArrayList<>(reservas));
	}
	
	public DataReserva buscarReserva(String nickname, String nombre, String codigoVuelo, int idReserva) {
		Usuario u = usuarioService.obtenerAerolineaPorNickname(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutas().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		VueloEspecifico v = r.getVuelosEspecificos().stream()
			.filter(ve -> ve.getNombre() != null && ve.getNombre().equalsIgnoreCase(codigoVuelo))
			.findFirst().orElse(null);
		if (v == null) {
			throw new IllegalArgumentException("No existe un vuelo con ese código en la ruta indicada");
		}
		Reserva res = v.getReservas().stream()
				.filter(re -> re.getIdReserva() == idReserva)
				.findFirst().orElse(null);
		if (res == null) {
			throw new IllegalArgumentException("No existe una reserva con ese ID en el vuelo indicado");
		}
		return ManejadorReserva.toData(res);
	}
	
	@Override
	public void registrarReserva(String nickname, String nombre, String codigoVuelo, DataReserva datos) {
		if (datos == null) throw new IllegalArgumentException("Los datos de la reserva no pueden ser nulos");
		Usuario u = usuarioService.obtenerAerolineaPorNickname(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutas().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		VueloEspecifico v = r.getVuelosEspecificos().stream()
			.filter(ve -> ve.getNombre() != null && ve.getNombre().equalsIgnoreCase(codigoVuelo))
			.findFirst().orElse(null);
		if (v == null) {
			throw new IllegalArgumentException("No existe un vuelo con ese código en la ruta indicada");
		}
		
		// Validación: no permitir reservas duplicadas para el mismo cliente y vuelo
        boolean existe = v.getReservas().stream()
            .anyMatch(rsv -> rsv.getCliente() != null &&
                rsv.getCliente().getNickname().equalsIgnoreCase(datos.getNickCliente().getNickname()));
        if (existe) {
            throw new IllegalArgumentException("Ya existe una reserva para este cliente en este vuelo.");
        }
		
		Reserva res = ManejadorReserva.toEntity(datos);
		int nuevoId = nextReservaId(v.getReservas());
		res.setIdReserva(nuevoId);
		res.setVueloEspecifico(v); // Asociar el vuelo específico a la reserva
		// Asociar el cliente gestionado por la sesión
		Cliente cliente = usuarioService.obtenerClientePorNickname(datos.getNickCliente().getNickname());
		if (cliente == null) {
		    throw new IllegalArgumentException("No existe el cliente con ese nickname");
		}
		res.setCliente(cliente);
		reservaService.registrarReserva(res);
		//v.getReservas().add(res);
	}
	
	 
	
	
}