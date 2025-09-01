
package Logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//import javax.swing.JOptionPane;


public class Sistema implements ISistema {
    

    // “Persistencia” en memoria por ahora.
    private final Map<String, Usuario> usuariosPorNickname = new HashMap<>(); // guardamos ENTIDADES (dominio), indexadas por nickname
    private final Map<Long, Ciudad> CiudadPorHash = new HashMap<>(); // guardamos ENTIDADES (dominio), indexadas por hashcode
    private final Map<String, Categoria> categoriasPorNombre = new LinkedHashMap<>();
    private final Map<String, Paquete> paquetesPorNombre = new HashMap<>(); // clave: nombre canónico
    private final List<CompraPaquete> compras = new ArrayList<>();
    private int compraIdSeq = 1;  // Contador de IDs para compras (auto incremental en memoria)
           
    public Sistema() {}

    
    
    // ======================
    //  REGISTRAR USUARIOS
    // ======================
    
    
 // Normaliza claves para que "Juan", "juAN" y "juan" choquen correctamente ////// HELPERS
    
    
    private static String canonical(String s) {
        return (s == null) ? null : s.trim().toLowerCase(Locale.ROOT);
    }
    
    private static java.util.Date copia(java.util.Date d) {
        return (d == null) ? null : new java.util.Date(d.getTime());
    }

    @Override
    public void registrarUsuario(DataUsuario data) {
        if (data == null) throw new IllegalArgumentException("Los datos no pueden ser nulos");

        // Validaciones de unicidad
        if (existeNickname(data.getNickname())) {
            throw new IllegalArgumentException("El nickname ya está en uso");
        }
        if (existeEmail(data.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        // DTO -> Entidad (polimórfico)
        Usuario entity = ManejadorUsuario.toEntity(data);

        // Clave canónica
        String key = canonical(entity.getNickname());
        usuariosPorNickname.put(key, entity);
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
        Usuario u = usuariosPorNickname.get(canonical(nickname));
        if (u instanceof Cliente c) {
        	return (DataCliente) ManejadorUsuario.toDTO(c);
        }
        return null;
    }

    @Override
    public DataAerolinea verInfoAerolinea(String nickname) {
        Usuario u = usuariosPorNickname.get(canonical(nickname));
        if (u instanceof Aerolinea a) {
            return (DataAerolinea) ManejadorUsuario.toDTO(a);
        }
        return null;
    }
    
    @Override
    public List<DataUsuario> listarUsuarios() {
        return ManejadorUsuario.toDTOs(new ArrayList<>(usuariosPorNickname.values()));
    }
    
    @Override
    public List<DataAerolinea> listarAerolineas() {
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
	}
    
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

    
    // ======================
    //  MODIFICAR USUARIOS
    // ======================
    
    @Override
    public void modificarCliente(String nickname, DataCliente nuevos) {
        if (nuevos == null) throw new IllegalArgumentException("Datos de cliente no pueden ser nulos");

        String key = canonical(nickname);
        Usuario u = usuariosPorNickname.get(key);
        if (!(u instanceof Cliente c))
            throw new IllegalArgumentException("No existe un cliente con ese nickname");

        // Validar que NO se cambie email ni nickname (según caso de uso)
        String emailActual = c.getEmail();
        String emailNuevo  = nuevos.getEmail();
        if (emailNuevo != null && !canonical(emailNuevo).equals(canonical(emailActual))) {
            throw new IllegalArgumentException("No se permite modificar el correo electrónico.");
        }
        if (nuevos.getNickname() != null && !canonical(nuevos.getNickname()).equals(key)) {
            throw new IllegalArgumentException("No se permite modificar el nickname.");
        }

        // Actualizar SOLO campos básicos permitidos
        c.setNombre(nuevos.getNombre());
        c.setApellido(nuevos.getApellido());
        c.setFechaNac(copia(nuevos.getFechaNac()));
        c.setNacionalidad(nuevos.getNacionalidad());
        c.setTipoDocumento(nuevos.getTipoDocumento());
        c.setNumDocumento(nuevos.getNumDocumento());
    }

    @Override
    public void modificarAerolinea(String nickname, DataAerolinea nuevos) {
        if (nuevos == null) throw new IllegalArgumentException("Datos de aerolínea no pueden ser nulos");

        String key = canonical(nickname);
        Usuario u = usuariosPorNickname.get(key);
        if (!(u instanceof Aerolinea a))
            throw new IllegalArgumentException("No existe una aerolínea con ese nickname");

        // Validar que NO se cambie email ni nickname
        String emailActual = a.getEmail();
        String emailNuevo  = nuevos.getEmail();
        if (emailNuevo != null && !canonical(emailNuevo).equals(canonical(emailActual))) {
            throw new IllegalArgumentException("No se permite modificar el correo electrónico.");
        }
        if (nuevos.getNickname() != null && !canonical(nuevos.getNickname()).equals(key)) {
            throw new IllegalArgumentException("No se permite modificar el nickname.");
        }

        // Actualizar SOLO campos básicos permitidos
        a.setNombre(nuevos.getNombre());
        a.setDescGeneral(nuevos.getDescripcion());
        a.setLinkWeb(nuevos.getSitioWeb());
    }
    
    // ======================
    //  CREAR CATEGORIA
    // ======================
    
    @Override
    public void registrarCategoria(DataCategoria data) {
        if (data == null || data.getNombre() == null || data.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        
        String key = canonical(data.getNombre());
        if (categoriasPorNombre.containsKey(key))
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        
        categoriasPorNombre.put(key, ManejadorCategoria.toEntity(data));
        
    }
    
    @Override
    public boolean existeCategoria(String nombre) {
        return nombre != null && categoriasPorNombre.containsKey(canonical(nombre));
    }
    
    @Override
    public java.util.List<DataCategoria> listarCategorias() {
        return ManejadorCategoria.toDTOs(new java.util.ArrayList<>(categoriasPorNombre.values()));
    } 
    
    // =========================
    // REGISTRAR RUTAS DE VUELO
    // =========================
    
    @Override
    public void registrarRuta(String nickAerolinea, DataRuta datos) {
		if (datos == null) throw new IllegalArgumentException("Los datos de la ruta no pueden ser nulos");

	    Usuario u = usuariosPorNickname.get(canonical(nickAerolinea));	    
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = ManejadorRuta.toEntity(datos);
		a.addRuta(r);
	}
    
//    private void validarBasico(Ruta r) {
//        if (r.getNombre()==null || r.getNombre().isBlank()) throw new IllegalArgumentException("nombre obligatorio");
//        if (r.getDescripcion()==null || r.getDescripcion().isBlank()) throw new IllegalArgumentException("descripcion obligatoria");
//        if (r.getOrigen()==null)  throw new IllegalArgumentException("ciudad origen obligatoria");
//        if (r.getDestino()==null) throw new IllegalArgumentException("ciudad destino obligatoria");
//        if (r.getFechaAlta()==null) throw new IllegalArgumentException("fecha de alta obligatoria");
//    }
    
    public List<DataRuta> listarPorAerolinea(String nicknameAerolinea) {
    	Usuario u = usuariosPorNickname.get(canonical(nicknameAerolinea));
    	if (!(u instanceof Aerolinea a)) {
    		throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
    	}
    	return ManejadorRuta.toDatas(new ArrayList<>(a.getRutaMap().values()));
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

		// DTO -> Entidad
		Ciudad entity = ManejadorCiudad.toEntity(data);

		CiudadPorHash.put(hash, entity);
	}

	@Override
	public List<DataCiudad> listarCiudades() {
		return ManejadorCiudad.toDatas(new ArrayList<>(CiudadPorHash.values()));
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
		
		Usuario u = usuariosPorNickname.get(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutaMap().values().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		VueloEspecifico v = ManejadorVueloEspecifico.toEntity(datos);
		r.addVuelosEspecificos(v);
	}
	
	@Override
	public List<DataVueloEspecifico> listarVuelos(String nickname, String nombre) {
		Usuario u = usuariosPorNickname.get(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutaMap().values().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		Collection<VueloEspecifico> vuelos = r.getVuelosEspecificos().values();
		return ManejadorVueloEspecifico.toDatas(new ArrayList<>(vuelos));
	}

	@Override
	public DataVueloEspecifico buscarVuelo(String nickname, String nombre, String codigoVuelo) {
		Usuario u = usuariosPorNickname.get(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutaMap().values().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		VueloEspecifico v = r.getVuelosEspecificos().get(codigoVuelo);
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
		List<Paquete> elegibles = paquetesPorNombre.values().stream()
	            .filter(p -> p.getCantRutas() > 0)
	            .sorted(Comparator.comparing(Paquete::getNombre, String.CASE_INSENSITIVE_ORDER))
	            .collect(Collectors.toList());

	    return ManejadorPaquete.toDTOs(elegibles);
	}
	

	@Override
	public List<DataCliente> listarClientesParaCompra() {
	    // Usamos tu ManejadorUsuario para convertir ENTIDAD -> DTO
	    return usuariosPorNickname.values().stream()
	            .filter(u -> u instanceof Cliente)
	            .map(u -> (Cliente) u)
	            .sorted(Comparator.comparing(Cliente::getNickname, String.CASE_INSENSITIVE_ORDER))
	            .map(c -> (DataCliente) ManejadorUsuario.toDTO(c))
	            .collect(Collectors.toList());
	}
	
	@Override
	public boolean clienteYaComproPaquete(String nicknameCliente, String nombrePaquete) {
	    String keyNick = canonical(nicknameCliente);
	    String keyPack = canonical(nombrePaquete);
	    return compras.stream().anyMatch(cp ->
	            canonical(cp.getCliente().getNickname()).equals(keyNick) &&
	            canonical(cp.getPaquete().getNombre()).equals(keyPack)
	    );
	}
	
	@Override
	public void comprarPaquete(DataCompraPaquete compra) {
	    if (compra == null)
	        throw new IllegalArgumentException("Datos de compra nulos");

	    // Resuelvo ENTIDADES a partir de los identificadores del DTO
	    Paquete paquete = paquetesPorNombre.get(canonical(compra.getNombrePaquete()));
	    if (paquete == null) throw new IllegalArgumentException("Paquete inexistente: " + compra.getNombrePaquete());
	    if (paquete.getCantRutas() <= 0) throw new IllegalStateException("El paquete no tiene rutas");

	    Usuario u = usuariosPorNickname.get(canonical(compra.getNicknameCliente()));
	    if (!(u instanceof Cliente cliente))
	        throw new IllegalArgumentException("Cliente inexistente: " + compra.getNicknameCliente());

	    if (clienteYaComproPaquete(compra.getNicknameCliente(), compra.getNombrePaquete())) {
	        throw new IllegalArgumentException("El cliente ya compró este paquete");
	    }

	    if (compra.getFechaCompra() == null)
	        throw new IllegalArgumentException("La fecha de compra es obligatoria");

	    // ENTIDAD a partir del DTO usando el Manejador de compras (nada de 'new' acá)
	    CompraPaquete entidad = ManejadorCompraPaquete.toEntity(compra, cliente, paquete);
	    
	    // asignar id secuencial
	    entidad.setId(compraIdSeq++);

	    // Registro en la “BD” en memoria
	    compras.add(entidad);
	}
	
			// ===============================
			//  PRECARGA CLIENTES Y PAQUETES
			// ===============================

	public void precargaDemo() {
	    registrarUsuario(new DataCliente("Ana","ana01","ana@mail.com","Pérez", new Date(), "UY", TipoDocumento.CEDULA, "52559649"));
	    registrarUsuario(new DataCliente("Bruno","bruno02","bruno@mail.com","López", new Date(), "UY", TipoDocumento.PASAPORTE, "54985693"));

	    DataPaquete rp  = new DataPaquete("Promo Río","Paquete con rutas a Río",2,TipoAsiento.TURISTA,20,30, BigDecimal.valueOf(1200));
	    DataPaquete rp2 = new DataPaquete("Europa Express","Rutas a Europa",3,TipoAsiento.EJECUTIVO,15,60, BigDecimal.valueOf(3200));

	    Paquete p1 = ManejadorPaquete.toEntity(rp);
	    Paquete p2 = ManejadorPaquete.toEntity(rp2);

	    // Guardar con la MISMA normalización que usás al buscar
	    paquetesPorNombre.put(canonical(rp.getNombre()),  p1);
	    paquetesPorNombre.put(canonical(rp2.getNombre()), p2);
	}
	
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

        // Unicidad por nombre
        String key = canonical(data.getNombre());
        if (paquetesPorNombre.containsKey(key))
            throw new IllegalArgumentException("Ya existe un paquete con ese nombre");
        
        Paquete entity = ManejadorPaquete.altaToEntity(data);
        paquetesPorNombre.put(key, entity);
	}
	
	 @Override
	    public boolean existePaquete(String nombre) {
	        return nombre != null && paquetesPorNombre.containsKey(canonical(nombre));
	    }
	 
	 @Override
	    public List<DataPaquete> listarPaquetes() {
	        // Devolver DTOs usando tu manejador
	        return paquetesPorNombre.values().stream()
	                .sorted(Comparator.comparing(Paquete::getNombre, String.CASE_INSENSITIVE_ORDER))
	                .map(ManejadorPaquete::toDTO)
	                .collect(Collectors.toList());
	    }
	 
	 @Override
	    public DataPaquete verPaquete(String nombre) {
	        if (nombre == null) return null;
	        Paquete p = paquetesPorNombre.get(canonical(nombre));
	        return (p == null) ? null : ManejadorPaquete.toDTO(p);
	    }
	 
	 			// ===============================
				//  AGREGAR RUTA DE VUELO A PAQUETE
				// ===============================
	 	
	 @Override
	 public List<DataPaquete> listarPaquetesSinCompras() {
	     // un paquete “con compras” es aquel que aparece en la lista compras
	     Set<String> nombresConCompra = compras.stream()
	             .map(cp -> canonical(cp.getPaquete().getNombre()))
	             .collect(Collectors.toSet());

	     return paquetesPorNombre.values().stream()
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
	                                 int cantidad) {

	     if (nombrePaquete == null || nicknameAerolinea == null || nombreRuta == null || tipo == null || cantidad <= 0)
	         throw new IllegalArgumentException("Datos incompletos");

	     // 1) Paquete (y no permitir si ya tiene compras)
	     Paquete p = paquetesPorNombre.get(canonical(nombrePaquete));
	     if (p == null) throw new IllegalArgumentException("No existe un paquete con ese nombre");

	     boolean tieneCompras = compras.stream()
	             .anyMatch(cp -> canonical(cp.getPaquete().getNombre()).equals(canonical(nombrePaquete)));
	     if (tieneCompras) throw new IllegalStateException("El paquete ya tiene compras y no admite modificaciones");

	     // 2) Aerolínea
	     Usuario u = usuariosPorNickname.get(canonical(nicknameAerolinea));
	     if (!(u instanceof Aerolinea a)) {
	         throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
	     }

	     // 3) Ruta de esa aerolínea por NOMBRE
	     Ruta r = a.getRutaMap().values().stream()
	             .filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombreRuta))
	             .findFirst()
	             .orElseThrow(() -> new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre"));
	     
	     //////////////////////////////////////////////////////////////////////////////////////////////////////
	
         /*if (p == null || r == null || tipo == null)
             throw new IllegalArgumentException("Datos inválidos para agregar ruta al paquete");*/

         // fijar/validar tipo de asiento único del paquete
         if (p.getTipoAsiento() == null) {
             p.setTipoAsiento(tipo);
         } else if (p.getTipoAsiento() != tipo) {
             throw new IllegalArgumentException(
                 "El tipo de asiento del paquete (" + p.getTipoAsiento()
                 + ") no coincide con el seleccionado (" + tipo + ")."
             );
         }

         // agregar ruta por NOMBRE (único)
         p.addCuposRuta(r.getNombre(), cantidad);
     }

	
    
	
	// =========================
	//         RESERVAS
	// =========================
	
	@Override
	public List<DataReserva> listarReservas(String nickname, String nombre, String codigoVuelo) {
		Usuario u = usuariosPorNickname.get(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutaMap().values().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		VueloEspecifico v = r.getVuelosEspecificos().get(codigoVuelo);
		if (v == null) {
			throw new IllegalArgumentException("No existe un vuelo con ese código en la ruta indicada");
		}
		Collection<Reserva> reservas = v.getReserva().values();
		return ManejadorReserva.toDatas(new ArrayList<>(reservas));
	}
	
	public DataReserva buscarReserva(String nickname, String nombre, String codigoVuelo, int idReserva) {
		Usuario u = usuariosPorNickname.get(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutaMap().values().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		VueloEspecifico v = r.getVuelosEspecificos().get(codigoVuelo);
		if (v == null) {
			throw new IllegalArgumentException("No existe un vuelo con ese código en la ruta indicada");
		}
		Reserva res = v.getReserva().get(Integer.toString(idReserva));
		if (res == null) {
			throw new IllegalArgumentException("No existe una reserva con ese ID en el vuelo indicado");
		}
		return ManejadorReserva.toData(res);
	}
	
	@Override
	public void registrarReserva(String nickname, String nombre, String codigoVuelo, DataReserva datos) {
		if (datos == null) throw new IllegalArgumentException("Los datos de la reserva no pueden ser nulos");
		
		Usuario u = usuariosPorNickname.get(canonical(nickname));
		if (!(u instanceof Aerolinea a)) {
			throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
		}
		Ruta r = a.getRutaMap().values().stream()
				.filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre))
				.findFirst().orElse(null);
		if (r == null) {
			throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
		}
		VueloEspecifico v = r.getVuelosEspecificos().get(codigoVuelo);
		if (v == null) {
			throw new IllegalArgumentException("No existe un vuelo con ese código en la ruta indicada");
		}
		Reserva res = ManejadorReserva.toEntity(datos);
		v.getReserva().put(Integer.toString(res.getIdReserva()), res);
	}
}