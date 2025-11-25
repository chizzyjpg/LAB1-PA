package Logica;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import BD.CategoriaService;
import BD.CiudadService;
import BD.ClienteService;
import BD.PaqueteService;
import BD.ReservaService;
import BD.RutaVueloService;
import BD.UsuarioService;

public class Sistema implements ISistema {

  // Helpers
  private final Map<Long, Ciudad> CiudadPorHash = new HashMap<>(); // guardamos ENTIDADES (dominio),
                                                                   // indexadas por hashcode
  private final List<CompraPaquete> compras = new ArrayList<>();

  private final UsuarioService usuarioService;
  private final CategoriaService categoriaService;
  private final CiudadService ciudadService;
  private final PaqueteService paqueteService;
  private final ClienteService clienteService;
  private final ReservaService reservaService;
  private final RutaVueloService rutaService;

  public Sistema(UsuarioService usuarioService, CategoriaService categoriaService,
      CiudadService ciudadService, PaqueteService paqueteService, ClienteService clienteService,
      ReservaService reservaService, RutaVueloService rutaService) {
    this.usuarioService = usuarioService;
    this.categoriaService = categoriaService;
    this.ciudadService = ciudadService;
    this.paqueteService = paqueteService;
    this.clienteService = clienteService;
    this.reservaService = reservaService;
    this.rutaService = rutaService;
  }

  public Sistema() {

    this.usuarioService = new UsuarioService();
    this.categoriaService = new CategoriaService();
    this.ciudadService = new CiudadService();
    this.paqueteService = new PaqueteService();
    this.clienteService = new ClienteService();
    this.reservaService = new ReservaService();
    this.rutaService = new RutaVueloService();
  }

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

  // ======================
  // REGISTRAR USUARIOS
  // ======================

  @Override
  public void registrarUsuario(DataUsuario data) {
    if (data == null) throw new IllegalArgumentException("Los datos no pueden ser nulos");

    String nickCanon = canonical(data.getNickname());
    String emailCanon = canonical(data.getEmail());

    if (usuarioService.existeNickname(nickCanon)) {
      throw new IllegalArgumentException("El nickname ya está en uso");
    }
    if (usuarioService.existeEmail(emailCanon)) {
      throw new IllegalArgumentException("El email ya está en uso");
    }

    ManejadorUsuario.toEntity(data);
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
    return usuarioService.listarUsuarios().stream().sorted(Comparator
        .comparing(DataUsuario::getNickname, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)))
        .collect(Collectors.toList());
  }

  @Override
  public List<DataAerolinea> listarAerolineas() {
    return usuarioService.listarUsuarios().stream().filter(DataAerolinea.class::isInstance)
        .map(DataAerolinea.class::cast).sorted(Comparator.comparing(DataAerolinea::getNickname,
            Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)))
        .collect(Collectors.toList());
  }

  public List<DataCliente> listarClientes() {
    return clienteService.listarClientes().stream().sorted(Comparator
        .comparing(DataCliente::getNickname, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)))
        .collect(Collectors.toList());
  }

  @Override
  public DataUsuario loguearUsuario(String nickname, String password) {
    if (nickname == null || password == null) {
      throw new IllegalArgumentException("Nickname y contraseña no pueden ser nulos");
    }

    // Autenticar SOLO por nickname (plano) para la prueba
    String nickCanon = canonical(nickname);
    Usuario u = usuarioService.autenticarUsuario(nickCanon, password);
    if (u == null) {
      return null; // falla autenticación
    }

    // Traer el DTO como ya lo haces
    return usuarioService.verInfoUsuario(u.getNickname());

  }

  @Override
  public byte[] obtenerAvatar(String nickname) {
    if (nickname == null || nickname.isBlank()) {
      return null;
    }

    Usuario u = usuarioService.obtenerClientePorNickname(canonical(nickname));
    if (u == null) {
      u = usuarioService.obtenerAerolineaPorNickname(canonical(nickname));
    }

    if (u == null || u.getAvatar() == null) {
      return null;
    }
    return Arrays.copyOf(u.getAvatar(), u.getAvatar().length);
  }

  // ======================
  // MODIFICAR USUARIOS
  // ======================

  @Override
  public void modificarCliente(String nickname, DataCliente nuevos) {
    if (nuevos == null)
      throw new IllegalArgumentException("Datos de cliente no pueden ser nulos");
    String key = canonical(nickname);
    Cliente cliente = usuarioService.obtenerClientePorNickname(key);
    if (cliente == null)
      throw new IllegalArgumentException("No existe un cliente con ese nickname");

    // Validar que NO se cambie email ni nickname
    String emailActual = cliente.getEmail();
    String emailNuevo = nuevos.getEmail();
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
    usuarioService.actualizarUsuario(cliente, false);
  }

  @Override
  public DataCliente actualizarPerfilCliente(PerfilClienteUpdate upd) {
    if (upd == null)
      throw new IllegalArgumentException("Datos de actualización nulos");

    // Verifico existencia y email inmutable contra el actual
    String key = canonical(upd.getNickname());
    Cliente existente = usuarioService.obtenerClientePorNickname(key);
    if (existente == null)
      throw new IllegalArgumentException("No existe un cliente con ese nickname");

    String emailActual = existente.getEmail();
    String emailNuevo  = upd.getEmail();
    if (emailNuevo != null && !canonical(emailNuevo).equals(canonical(emailActual))) {
      throw new IllegalArgumentException("No se permite modificar el correo electrónico.");
    }

    // Construyo un "delta" con los campos a actualizar
    Cliente delta = new Cliente();
    delta.setNickname(key);                      // PK para localizar managed en el service
    delta.setNombre(upd.getNombre());
    delta.setApellido(upd.getApellido());
    delta.setNacionalidad(upd.getNacionalidad());
    delta.setTipoDocumento(upd.getTipoDocumento());
    delta.setNumDocumento(upd.getNumDocumento());
    delta.setFechaNac(copia(upd.getFechaNac()));
    delta.setEmail(emailActual);                 // se conserva el email

    // Avatar: SOLO si hay archivo nuevo; si no, lo dejamos NULL para "mantener"
    if (upd.getAvatar() != null && upd.getAvatar().length > 0) {
      delta.setAvatar(upd.getAvatar());
    }

    // El service debe cargar la entidad managed y aplicar:
    // clear -> null, nuevoAvatar -> reemplazar, else -> mantener
    usuarioService.actualizarUsuario(delta,upd.isClearAvatar());

    // Devolver DTO fresco
    DataUsuario dto = usuarioService.verInfoUsuario(upd.getNickname());
    return (dto instanceof DataCliente) ? (DataCliente) dto : null;// Imposible de llegar con
    															   // JUnit, super defensivo
  }


  @Override
  public void modificarAerolinea(String nickname, DataAerolinea nuevos) {
    if (nuevos == null)
      throw new IllegalArgumentException("Datos de aerolínea no pueden ser nulos");

    String key = canonical(nickname);
    Aerolinea aerolinea = usuarioService.obtenerAerolineaPorNickname(key);
    if (aerolinea == null)
      throw new IllegalArgumentException("No existe una aerolínea con ese nickname");

    // Validar que NO se cambie email ni nickname
    String emailActual = aerolinea.getEmail();
    String emailNuevo = nuevos.getEmail();
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

    usuarioService.actualizarUsuario(aerolinea, false);
  }

  @Override
  public DataAerolinea actualizarPerfilAerolinea(PerfilAerolineaUpdate upd) {
    if (upd == null) {
    	throw new IllegalArgumentException("Datos de actualización nulos");
    }

    String key = canonical(upd.getNickname());
    Aerolinea existente = usuarioService.obtenerAerolineaPorNickname(key);
    if (existente == null) {
    	throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
    }

    // email inmutable
    String emailNuevo = upd.getEmail();
    if (emailNuevo != null && !canonical(emailNuevo).equals(canonical(existente.getEmail()))) {
    	throw new IllegalArgumentException("No se permite modificar el correo electrónico.");
    }

    // "delta" con solo lo que quiero cambiar
    Aerolinea delta = new Aerolinea();
    delta.setNickname(key);
    delta.setNombre(upd.getNombre());
    delta.setLinkWeb(upd.getSitioWeb());
    delta.setDescGeneral(upd.getDescGeneral());
    delta.setEmail(existente.getEmail());

    // avatar: SOLO si hay archivo nuevo; si no, dejar null para "mantener" en el service
    if (upd.getAvatar() != null && upd.getAvatar().length > 0) {
    	delta.setAvatar(upd.getAvatar());
    }

    usuarioService.actualizarUsuario(delta, upd.isClearAvatar());

    DataUsuario dto = usuarioService.verInfoUsuario(upd.getNickname());
    return (dto instanceof DataAerolinea) ? (DataAerolinea) dto : null;
  }


  @Override
  public void cambiarPassword(String nickname, String pwdCurrent, String pwdNew) {
    if (nickname == null || pwdCurrent == null || pwdNew == null) {
      throw new IllegalArgumentException("Nickname y contraseñas no pueden ser nulos");
    }
    if (pwdNew.length() < 3) {
      throw new IllegalArgumentException("La nueva contraseña debe tener al menos 3 caracteres");
    }

    Usuario u = usuarioService.autenticarUsuario(nickname, pwdCurrent);
    if (u == null) {
      throw new IllegalArgumentException("No existe un usuario con ese nickname y esa contraseña");
    }

    // Actualizar la contraseña (plana)
    u.setContrasenia(pwdNew);
    usuarioService.actualizarUsuario(u, false);
  }

  // ======================
  // CREAR CATEGORIA
  // ======================

  @Override
  public void registrarCategoria(DataCategoria data) {
    if (existeCategoria(data.getNombre())) {
      throw new IllegalArgumentException("El nombre de la categoría ya está en uso");
    }

    ManejadorCategoria.toEntity(data);
  }

  @Override
  public boolean existeCategoria(String nombre) {
    return categoriaService.existeCategoria(nombre);
  }

  @Override
  public List<DataCategoria> listarCategorias() {
    return categoriaService.listarCategorias().stream().map(ManejadorCategoria::toData)
        .sorted(Comparator.comparing(c -> c.getNombre() == null ? "" : c.getNombre(),
            String.CASE_INSENSITIVE_ORDER))
        .collect(Collectors.toList());
  }

  // =========================
  // REGISTRAR RUTAS DE VUELO
  // =========================

  @Override
  public void registrarRuta(DataRuta datos) {
    if (datos == null)
      throw new IllegalArgumentException("Los datos de la ruta no pueden ser nulos");

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
    boolean existe = aerolinea.getRutas().stream().anyMatch(r -> nombre.equals(r.getNombre()));
    if (existe) {
      throw new IllegalArgumentException("Ya existe una ruta llamada exactamente: " + nombre);
    }

    Ruta ruta = ManejadorRuta.toEntity(datos); // convierte DataRuta a Ruta (ENTIDAD)
    rutaService.crearRutaVuelo(ruta, nickAerolinea);
  }

  public List<DataRuta> listarPorAerolinea(String nicknameAerolinea) {
    DataUsuario usuario = usuarioService.verInfoUsuario(nicknameAerolinea);
    if (!(usuario instanceof DataAerolinea)) {
      throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
    }
    List<DataRuta> rutas = usuarioService.listarRutasPorAerolinea(nicknameAerolinea);
    return rutas.stream().sorted(Comparator
        .comparing(r -> r.getNombre() == null ? "" : r.getNombre(), String.CASE_INSENSITIVE_ORDER))
        .collect(Collectors.toList());
  }

  // =========================
  // CIUDADES
  // =========================

  @Override
  public void registrarCiudad(DataCiudad data) {
    if (data == null)
      throw new IllegalArgumentException("Los datos no pueden ser nulos");

    // Validaciones de unicidad
    if (ciudadService.existeCiudad(data.getNombre(), data.getPais())) {
      throw new IllegalArgumentException("La ciudad ya está registrada");
    }

    ManejadorCiudad.toEntity(data);
  }

  @Override
  public List<DataCiudad> listarCiudades() {
    return ciudadService.listarCiudades().stream().map(ManejadorCiudad::toData).sorted(Comparator
        .comparing(c -> c.getNombre() == null ? "" : c.getNombre(), String.CASE_INSENSITIVE_ORDER))
        .collect(Collectors.toList());
  }

  public Ciudad buscarCiudad(String nombre, String pais) {
    if (nombre == null || nombre.isBlank() || pais == null || pais.isBlank()) {
      return null;
    }
    long hash = nombre.toLowerCase().hashCode() + 31 * pais.toLowerCase().hashCode();
    return CiudadPorHash.get(hash);
  }

  // =========================
  // VUELOS
  // =========================

  @Override
  public void registrarVuelo(String nickname, String nombre, DataVueloEspecifico datos) {
    if (datos == null)
      throw new IllegalArgumentException("Los datos del vuelo no pueden ser nulos");
    // Buscar el id de la ruta persistida por nombre
    Integer idRuta = rutaService.buscarRutaPorNombreYObtenerId(nombre);
    if (idRuta == null) {
      throw new IllegalArgumentException("No existe una ruta con ese nombre en la base de datos");
    }
    // Buscar la entidad Ruta persistida usando el id
    Ruta ruta = rutaService.buscarRutaPorId(idRuta);
    if (ruta == null) {
      throw new IllegalArgumentException("No se encontró la Ruta con id: " + idRuta);
    }
    if (datos.getFecha() == null) {
      throw new IllegalArgumentException("La fecha del vuelo es obligatoria");
    }
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    cal.set(java.util.Calendar.MILLISECOND, 0);
    java.util.Date startOfToday = cal.getTime();
    if (datos.getFecha().before(startOfToday)) {
        throw new IllegalArgumentException("La fecha del vuelo no puede ser anterior a hoy");
    }
    // Inicializar las colecciones para evitar LazyInitializationException
    ruta.getAerolineas().size();
    ruta.getVuelosEspecificos().size();

    ManejadorVueloEspecifico.toEntity(datos, ruta);
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
        .filter(r -> r.getNombre() != null && r.getNombre().equalsIgnoreCase(nombre)).findFirst()
        .orElse(null);
    if (ruta == null) {
      throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
    }
    // Obtener los vuelos específicos de la ruta desde la base de datos
    List<DataVueloEspecifico> vuelos = usuarioService.listarVuelosPorRuta(nickname, nombre);
    return vuelos == null ? Collections.emptyList() : vuelos;
  }

  @Override
  public DataVueloEspecifico buscarVuelo(String nickname, String nombre, String codigoVuelo) {
    // Obtener la aerolínea desde la base de datos
    Usuario u = usuarioService.obtenerAerolineaPorNickname(canonical(nickname));
    if (!(u instanceof Aerolinea)) {
      throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
    }
    // Buscar la ruta por nombre usando el servicio de BD para asegurar la sesión
    // activa
    Integer idRuta = rutaService.buscarRutaPorNombreYObtenerId(nombre);
    if (idRuta == null) {
      throw new IllegalArgumentException("No existe una ruta con ese nombre en la base de datos");
    }
    Ruta r = rutaService.buscarRutaPorId(idRuta);
    if (r == null) {
      throw new IllegalArgumentException("No se encontró la Ruta con id: " + idRuta);
    }
    // Inicializar las colecciones para evitar LazyInitializationException
    r.getAerolineas().size();
    r.getVuelosEspecificos().size();
    // Buscar el vuelo específico en la ruta
    VueloEspecifico v = r.getVuelosEspecificos().stream()
        .filter(ve -> ve.getNombre() != null && ve.getNombre().equalsIgnoreCase(codigoVuelo))
        .findFirst().orElse(null);
    if (v == null) {
      throw new IllegalArgumentException("No existe un vuelo con ese código en la ruta indicada");
    }
    return ManejadorVueloEspecifico.toDTO(v);
  }

  // =========================
  // COMPRA DE PAQUETE
  // =========================

  @Override
  public List<DataPaquete> listarPaquetesDisponiblesParaCompra() {
    return paqueteService.listarPaquetes().stream().filter(p -> p.getCantRutas() > 0)
        .map(ManejadorPaquete::toDTO)
        .sorted(Comparator.comparing(p -> p.getNombre() == null ? "" : p.getNombre(),
            String.CASE_INSENSITIVE_ORDER))
        .collect(Collectors.toList());
  }

  @Override
  public List<DataCliente> listarClientesParaCompra() {
    return clienteService.listarClientes();
  }

  @Override
  public boolean clienteYaComproPaquete(String nicknameCliente, String nombrePaquete) {
    return usuarioService.clienteYaComproPaquete(nicknameCliente, nombrePaquete);
  }

  @Override
  public void comprarPaquete(DataCompraPaquete compra) {
    if (compra == null)
      throw new IllegalArgumentException("Datos de compra nulos");

    // Resuelvo ENTIDADES a partir de los identificadores del DTO
    Paquete paquete = paqueteService.existePaquete(compra.getNombrePaquete());
    if (paquete == null)
      throw new IllegalArgumentException("Paquete inexistente: " + compra.getNombrePaquete());
    if (paquete.getCantRutas() <= 0)
      throw new IllegalStateException("El paquete no tiene rutas");

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
  // ALTA PAQUETE
  // ===============================

  public void registrarPaquete(DataPaqueteAlta data) {
    // Validaciones básicas de entrada
    if (data == null)
      throw new IllegalArgumentException("Datos del paquete nulos");
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
  }

  @Override
  public boolean existePaquete(String nombre) {
    if (nombre == null)
      return false;
    return paqueteService.existePaquete(nombre) != null;
  }

  @Override
  public List<DataPaquete> listarPaquetes() {
    // Devolver DTOs usando tu manejador
    List<Paquete> paquetes = paqueteService.listarPaquetes();
    return ManejadorPaquete.toDTOs(paquetes);
  }

  @Override
  public DataPaquete verPaquete(String nombre) {
    if (nombre == null)
      return null;
    Paquete p = paqueteService.existePaquete(nombre);
    return (p == null) ? null : ManejadorPaquete.toDTO(p);
  }

  // ===============================
  // AGREGAR RUTA DE VUELO A PAQUETE
  // ===============================

  @Override
  public List<DataPaquete> listarPaquetesSinCompras() {
    // Obtener todos los paquetes
    List<Paquete> paquetes = paqueteService.listarPaquetes();
    // Obtener los nombres de los paquetes que han sido comprados (desde la base de
    // datos)
    Set<String> nombresConCompra = paqueteService.listarNombresPaquetesComprados().stream()
        .map(Sistema::canonical).collect(Collectors.toSet());

    // Filtrar los paquetes que no han sido comprados
    return paquetes.stream().filter(p -> !nombresConCompra.contains(canonical(p.getNombre())))
        .sorted(Comparator.comparing(Paquete::getNombre, String.CASE_INSENSITIVE_ORDER))
        .map(ManejadorPaquete::toDTO).collect(Collectors.toList());
  }

  @Override
  public void agregarRutaAPaquete(String nombrePaquete, String nicknameAerolinea, String nombreRuta,
      TipoAsiento tipo, int cantidad) {

    if (nombrePaquete == null || nicknameAerolinea == null || nombreRuta == null || tipo == null
        || cantidad <= 0)
      throw new IllegalArgumentException("Datos incompletos");

    // 1) Paquete (y no permitir si ya tiene compras)
    Paquete p = paqueteService.existePaquete(nombrePaquete);
    if (p == null)
      throw new IllegalArgumentException("No existe un paquete con ese nombre");

    boolean tieneCompras = compras.stream()
        .anyMatch(cp -> canonical(cp.getPaquete().getNombre()).equals(canonical(nombrePaquete)));
    if (tieneCompras)
      throw new IllegalStateException("El paquete ya tiene compras y no admite modificaciones");

    // 2) Aerolínea
    usuarioService.verInfoUsuario(nicknameAerolinea);

    // 3) Ruta de esa aerolínea por NOMBRE
    Ruta r = paqueteService.buscarRutaEnAerolinea(nicknameAerolinea, nombreRuta);

    // validar tipo de asiento único del paquete
    if (p.getTipoAsiento() == null) {
      p.setTipoAsiento(tipo);
    } else if (p.getTipoAsiento() != tipo) {
      throw new IllegalArgumentException("El tipo de asiento del paquete (" + p.getTipoAsiento()
          + ") no coincide con el seleccionado (" + tipo + ").");
    }

    BigDecimal cantidadBD = BigDecimal.valueOf(cantidad);
    BigDecimal descuentoFactor = BigDecimal.valueOf((100 - p.getDescuento()) / 100.0);

    if (p.getCosto() == null) {
      p.setCosto(BigDecimal.ZERO);
    }

    if (tipo == TipoAsiento.TURISTA) {
      p.setCosto(
          p.getCosto().add(r.getCostoTurista().multiply(cantidadBD).multiply(descuentoFactor)));
    } else { // EJECUTIVO
      p.setCosto(
          p.getCosto().add(r.getCostoEjecutivo().multiply(cantidadBD).multiply(descuentoFactor)));
    }
    // agregar ruta por NOMBRE (único)
    p.setCuposDisponibles(cantidad);
    p.setCuposMaximos(cantidad);
    p.addRutaPorNombre(r.getNombre());
    paqueteService.actualizarPaquete(p);
  }

    // ===============================
    // FINALIZAR RUTA DE VUELO
    // ===============================
    @Override
    public void finalizarRutaDeVuelo(String nicknameAerolinea, String nomRuta) {
        // Validar aerolínea
        DataAerolinea aerolinea = verInfoAerolinea(nicknameAerolinea);
        if (aerolinea == null) {
            throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
        }
        // Validar ruta
        int idRuta = rutaService.buscarRutaPorNombreYObtenerId(nomRuta);
        Ruta ruta = rutaService.buscarRutaPorId(idRuta);
        if (ruta == null) {
            throw new IllegalArgumentException("No existe una ruta con ese ID");
        }
        Ruta paquete = paqueteService.buscarRutaEnAerolinea(nicknameAerolinea, nomRuta);
        if (paquete == null) {
            throw new IllegalArgumentException("La ruta no pertenece a la aerolínea indicada");
        }
        if (ruta.getEstado() != EstadoRuta.CONFIRMADA) {
            throw new IllegalArgumentException("Solo se pueden finalizar rutas en estado CONFIRMADA");
        }
        // Validar que no tenga vuelos pendientes
        boolean tieneVuelosPendientes = ruta.getVuelosEspecificos().stream()
                .anyMatch(v -> v.getFecha() != null && v.getFecha().after(new Date()));
        if (tieneVuelosPendientes) {
            throw new IllegalArgumentException("La ruta tiene vuelos pendientes");
        }
        // Validar que no esté en paquetes
        if (rutaService.estaEnPaquete(idRuta)) { // Debes implementar este método
            throw new IllegalArgumentException("La ruta está incluida en un paquete");
        }
        // Cambiar estado de la ruta a FINALIZADA
        ruta.setEstado(EstadoRuta.FINALIZADA);
        rutaService.actualizarRuta(ruta);
    }

    // =========================
  // RESERVAS
  // =========================

  @Override
  public List<DataReserva> listarReservas(String nickname, String nombre, String codigoVuelo) {
    Usuario u = usuarioService.obtenerAerolineaPorNickname(canonical(nickname));
    if (!(u instanceof Aerolinea a)) {
      throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
    }
    Ruta r = a.getRutas().stream()
        .filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre)).findFirst()
        .orElse(null);
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
        .sorted(Comparator.comparingInt(Reserva::getIdReserva)).collect(Collectors.toList());
    return ManejadorReserva.toDatas(new ArrayList<>(reservas));
  }

  public DataReserva buscarReserva(String nickname, String nombre, String codigoVuelo,
      int idReserva) {
    Usuario u = usuarioService.obtenerAerolineaPorNickname(canonical(nickname));
    if (!(u instanceof Aerolinea a)) {
      throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
    }
    Ruta r = a.getRutas().stream()
        .filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre)).findFirst()
        .orElse(null);
    if (r == null) {
      throw new IllegalArgumentException("La aerolínea no tiene una ruta con ese nombre");
    }
    VueloEspecifico v = r.getVuelosEspecificos().stream()
        .filter(ve -> ve.getNombre() != null && ve.getNombre().equalsIgnoreCase(codigoVuelo))
        .findFirst().orElse(null);
    if (v == null) {
      throw new IllegalArgumentException("No existe un vuelo con ese código en la ruta indicada");
    }
    Reserva res = v.getReservas().stream().filter(re -> re.getIdReserva() == idReserva).findFirst()
        .orElse(null);
    if (res == null) {
      throw new IllegalArgumentException("No existe una reserva con ese ID en el vuelo indicado");
    }
    return ManejadorReserva.toData(res);
  }

  @Override
  public void registrarReserva(String nickname, String nombre, String codigoVuelo,
      DataReserva datos) {
    if (datos == null)
      throw new IllegalArgumentException("Los datos de la reserva no pueden ser nulos");
    Usuario u = usuarioService.obtenerAerolineaPorNickname(canonical(nickname));
    if (!(u instanceof Aerolinea a)) {
      throw new IllegalArgumentException("No existe una aerolínea con ese nickname");
    }
    Ruta r = a.getRutas().stream()
        .filter(rt -> rt.getNombre() != null && rt.getNombre().equalsIgnoreCase(nombre)).findFirst()
        .orElse(null);
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
    boolean existe = v.getReservas().stream().anyMatch(rsv -> rsv.getCliente() != null
        && rsv.getCliente().getNickname().equalsIgnoreCase(datos.getNickCliente().getNickname()));
    if (existe) {
      throw new IllegalArgumentException("Ya existe una reserva para este cliente en este vuelo.");
    }

    Reserva res = ManejadorReserva.toEntity(datos);
    int nuevoId = nextReservaId(v.getReservas());
    res.setIdReserva(nuevoId);
    res.setVueloEspecifico(v); // Asociar el vuelo específico a la reserva
    // Asociar el cliente gestionado por la sesión
    Cliente cliente = usuarioService
        .obtenerClientePorNickname(datos.getNickCliente().getNickname());
    if (cliente == null) {
      throw new IllegalArgumentException("No existe el cliente con ese nickname");
    }
    res.setCliente(cliente);
    reservaService.registrarReserva(res);
  }

  @Override
  public DataReserva verDetalleReserva(int idReserva) {
    Reserva reserva = reservaService.buscarReservaPorId(idReserva);
    if (reserva == null) {
      throw new IllegalArgumentException("No existe una reserva con ese ID");
    }
    return ManejadorReserva.toData(reserva);
  }

  @Override
  public List<DataReserva> listarReservasPendientesCheckIn(String nicknameCliente) {
    Cliente cliente = usuarioService.obtenerClientePorNickname(canonical(nicknameCliente));
    if (cliente == null) {
      throw new IllegalArgumentException("No existe un cliente con ese nickname");
    }
    List<Reserva> reservasPendientes = reservaService.listarReservasPendientesCheckIn(cliente);
    return ManejadorReserva.toDatas(reservasPendientes);
  }

  @Override
  public void realizarCheckIn(int idReserva) {
        System.out.println("[realizarCheckIn] idReserva recibido: " + idReserva);
        Reserva reserva = reservaService.buscarReservaPorId(idReserva);
        if (reserva == null) {
            System.out.println("[realizarCheckIn] No existe una reserva con ese ID: " + idReserva);
            throw new IllegalArgumentException("No existe una reserva con ese ID");
        }
        if (reserva.isCheckInRealizado()) {
            System.out.println("[realizarCheckIn] El check-in ya ha sido realizado para la reserva ID: " + idReserva);
            throw new IllegalStateException("El check-in ya ha sido realizado para esta reserva");
        }
        // Obtener asientos ya ocupados en el vuelo
        VueloEspecifico vuelo = reserva.getVueloEspecifico();
        Set<String> asientosOcupados = new HashSet<>();
        for (Reserva r : vuelo.getReservas()) {
            if (r.isCheckInRealizado()) {
                for (Pasaje p : r.getPasajes()) {
                    if (p.getAsientoAsignado() != null) {
                        asientosOcupados.add(p.getAsientoAsignado());
                    }
                }
            }
        }
        // Asignar asientos a cada pasajero evitando repeticiones
        List<Pasaje> pasajes = reserva.getPasajes();
        char letra = 'A';
        int fila = 1;
        for (Pasaje pasaje : pasajes) {
            String asiento;
            do {
                asiento = fila + String.valueOf(letra);
                letra++;
                if (letra > 'F') { // asumiendo 6 asientos por fila
                    letra = 'A';
                    fila++;
                }
            } while (asientosOcupados.contains(asiento));
            pasaje.setAsientoAsignado(asiento);
            asientosOcupados.add(asiento);
            System.out.println("[realizarCheckIn] Asignado asiento " + asiento + " a pasaje: " + pasaje);
        }
        // Descontar asientos disponibles según tipo
        int cantidad = pasajes.size();
        if (reserva.getTipoAsiento() == TipoAsiento.TURISTA) {
            int disponibles = vuelo.getMaxAsientosTur();
            if (disponibles < cantidad) {
                throw new IllegalStateException("No hay suficientes asientos Turista disponibles");
            }
            vuelo.setMaxAsientosTur(disponibles - cantidad);
            System.out.println("[realizarCheckIn] Asientos Turista restantes: " + vuelo.getMaxAsientosTur());
        } else if (reserva.getTipoAsiento() == TipoAsiento.EJECUTIVO) {
            int disponibles = vuelo.getMaxAsientosEjec();
            if (disponibles < cantidad) {
                throw new IllegalStateException("No hay suficientes asientos Ejecutivo disponibles");
            }
            vuelo.setMaxAsientosEjec(disponibles - cantidad);
            System.out.println("[realizarCheckIn] Asientos Ejecutivo restantes: " + vuelo.getMaxAsientosEjec());
        }
        // Registrar hora de embarque
        Date ahora = new Date();
        reserva.setHoraEmbarque(ahora);
        System.out.println("[realizarCheckIn] Hora de embarque registrada: " + ahora);
        reserva.setCheckInRealizado(true);
        System.out.println("[realizarCheckIn] Check-in realizado para reserva ID: " + idReserva);
        reservaService.actualizarReserva(reserva);
    }

    @Override
    public List<DataReserva> listarReservasConCheckin(String nicknameCliente) {
        Cliente cliente = usuarioService.obtenerClientePorNickname(canonical(nicknameCliente));
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con ese nickname");
        }
        List<Reserva> reservas = reservaService.listarReservasPorCliente(cliente);
        List<Reserva> conCheckin = reservas.stream()
                .filter(Reserva::isCheckInRealizado)
                .collect(Collectors.toList());
        return ManejadorReserva.toDatas(conCheckin);
    }

  // ======================
  // CAMBIAR ESTADO RUTA
  // ======================

  @Override
  public void cambiarEstadoRuta(int idRuta, EstadoRuta nuevoEstado) {
    if (nuevoEstado == null)
      throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
    Ruta ruta = rutaService.buscarRutaPorId(idRuta);
    if (ruta == null) {
      throw new IllegalArgumentException("No existe una ruta con ese ID");
    }
    if (ruta.getEstado() == nuevoEstado) {
      throw new IllegalArgumentException("La ruta ya está en el estado indicado");
    }
    if (ruta.getEstado() != EstadoRuta.INGRESADA) {
      throw new IllegalStateException("Solo se pueden cambiar rutas en estado \"Ingresada\"");
    }
    ruta.setEstado(nuevoEstado);
    rutaService.actualizarRuta(ruta);
  }

    // ======================
    // VISITA DE RUTAS
    // ======================


  @Override
  public void registrarVisitaRuta(int idRuta) {
      Ruta ruta = rutaService.buscarRutaPorId(idRuta);
      if (ruta == null) {
          throw new IllegalArgumentException("No existe una ruta con ese ID");
      }
      ruta.setVisitas(ruta.getVisitas() + 1);

      rutaService.actualizarRuta(ruta);
  }

    @Override
    public List<DataRutaMasVisitada> obtener5RutasMasVisitadas() {
        List<Ruta> rutas = rutaService.listar5RutasMasVisitadas();

        List<DataRutaMasVisitada> resultado = rutas.stream()
                .map(r -> {
                    String nickAerolinea = null;
                    if (r.getAerolineas() != null && !r.getAerolineas().isEmpty()) {
                        Aerolinea aerolinea = r.getAerolineas().iterator().next();
                        nickAerolinea = aerolinea.getNickname();
                    }

                    DataRutaMasVisitada dto = new DataRutaMasVisitada(
                            r.getIdRuta(),
                            nickAerolinea,
                            r.getOrigen(),
                            r.getDestino(),
                            r.getVisitas()
                    );

                    return dto;
                })
                .toList();

        return resultado;
    }

    // ======================
    // CAMBIOS ALTAS PARA AVATAR
    // ======================

  @Override
  public boolean existeNickname(String nickname) {
    return usuarioService.existeNickname(nickname);
  }

  @Override
  public boolean existeEmail(String email) {
    return usuarioService.existeEmail(email);
  }

  @Override
  public void altaCliente(DataCliente cliente, byte[] avatar) {
    String nickname = canonical(cliente.getNickname());
    String email = canonical(cliente.getEmail());
    TipoDocumento documento = cliente.getTipoDocumento();
    String numeroDocumento = canonical(cliente.getNumDocumento());
    String nombre = canonical(cliente.getNombre());
    String apellido = canonical(cliente.getApellido());
    String pais = cliente.getNacionalidad();
    Date fechaNac = cliente.getFechaNac();

    // Ajusta la construcción de PerfilClienteUpdate según el constructor disponible
    PerfilClienteUpdate perfil = new PerfilClienteUpdate(nickname, email, nombre, apellido, pais,
        documento, numeroDocumento, fechaNac, avatar, false);
    if (avatar == null) {
    }
    registrarUsuario(cliente);
    if (avatar != null && avatar.length > 0) {
      actualizarPerfilCliente(perfil);
    }

  }

  @Override
  public void altaAerolinea(DataAerolinea aerolinea) {
    registrarUsuario(aerolinea);
  }

    // =============================
    // BUSQUEDAS DE CUADRO DE TEXTO
    // =============================

    @Override
    public List<DataBusquedaItem> buscarRutasYPaquetes(String texto){
      List<Ruta> rutas = rutaService.buscarPorTexto(texto);
      List<Paquete> paquetes = paqueteService.buscarPorTexto(texto);

      List<DataBusquedaItem> resultados = new ArrayList<>();
      List<DataBusquedaItem> rutasDTO = ManejadorRuta.toDataBusquedaItem(rutas);
      List<DataBusquedaItem> paquetesDTO = ManejadorPaquete.toDataBusquedaItem(paquetes);

      resultados.addAll(rutasDTO);
      resultados.addAll(paquetesDTO);

        resultados.sort(
                Comparator.comparing(
                        DataBusquedaItem::getFechaAlta,
                        Comparator.nullsLast(Date::compareTo)
                ).reversed()
        );

      return resultados;
    }
}
