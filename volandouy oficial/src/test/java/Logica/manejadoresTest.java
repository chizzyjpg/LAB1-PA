package Logica;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.Test;

public class manejadoresTest {

  // ====== Helpers ======
  private static Date date(int y, int m, int d) {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    cal.clear();
    cal.set(y, m - 1, d, 0, 0, 0);
    return cal.getTime();
  }

  // ====== toDTO: Cliente ======
  @Test
  void toDTO_cliente_ok() {
    Date nac = date(2000, 1, 2);
    Cliente cli = new Cliente(
        "Eze", "mmarce", "em@ex.com", "abc",
        "Marcenal", nac, "Uruguay",
        TipoDocumento.CEDULA, "5.555.555-5"
    );

    DataUsuario dtoGen = ManejadorUsuario.toDTO(cli);
    assertTrue(dtoGen instanceof DataCliente, "Debe retornar DataCliente");

    DataCliente dto = (DataCliente) dtoGen;
    assertEquals("Eze", dto.getNombre());
    assertEquals("mmarce", dto.getNickname());
    assertEquals("em@ex.com", dto.getEmail());
    assertEquals("abc", dto.getContrasenia());
    assertEquals("Marcenal", dto.getApellido());
    assertEquals("Uruguay", dto.getNacionalidad());
    assertEquals(TipoDocumento.CEDULA, dto.getTipoDocumento());
    assertEquals("5.555.555-5", dto.getNumDocumento());

    // Copia defensiva: misma fecha en valor, distinto objeto
    assertEquals(nac, dto.getFechaNac(), "La fecha debe copiarse en valor");
    assertNotSame(nac, dto.getFechaNac(), "La fecha debe ser una COPIA");

    // Si mutamos la fecha original, el DTO NO debe cambiar
    nac.setTime(date(1999, 12, 31).getTime());
    assertEquals(date(2000, 1, 2), dto.getFechaNac(), "El DTO no debe verse afectado por mutaciones externas");
  }

  // ====== toDTO: Aerolinea ======
  @Test
  void toDTO_aerolinea_ok() {
    // Ajustá si tu constructor difiere
    Aerolinea a = new Aerolinea(
        "JetAir", "jetair", "info@jetair.com", "secret",
        "Somos JetAir", "https://jetair.test"
    );

    DataUsuario dtoGen = ManejadorUsuario.toDTO(a);
    assertTrue(dtoGen instanceof DataAerolinea, "Debe retornar DataAerolinea");

    DataAerolinea dto = (DataAerolinea) dtoGen;
    assertEquals("JetAir", dto.getNombre());
    assertEquals("jetair", dto.getNickname());
    assertEquals("info@jetair.com", dto.getEmail());
    assertEquals("secret", dto.getContrasenia());
    assertEquals("Somos JetAir", dto.getDescripcion());
    assertEquals("https://jetair.test", dto.getSitioWeb());
  }

  // ====== toDTO: null ======
  @Test
  void toDTO_null() {
    NullPointerException ex = assertThrows(
        NullPointerException.class,
        () -> ManejadorUsuario.toDTO(null)
    );
    assertTrue(
        ex.getMessage() != null && ex.getMessage().contains("Usuario no puede ser null"),
        "Mensaje debe mencionar 'Usuario no puede ser null'"
    );
  }

  // ====== toDTO: tipo no soportado ======
  static class UsuarioRaro extends Usuario {
    UsuarioRaro(String nombre, String nick, String email, String pass) {
      super(nombre, nick, email, pass);
    }
  }

  @Test
  void toDTO_tipo_no_soportado() {
    Usuario u = new UsuarioRaro("X", "x", "x@x.com", "p");
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> ManejadorUsuario.toDTO(u)
    );
    assertTrue(
        ex.getMessage() != null && ex.getMessage().contains("Tipo de Usuario no soportado"),
        "Mensaje debe indicar tipo no soportado"
    );
  }

  // ====== toDTOs(List<? extends Usuario>) ======
  @Test
  void toDTOs_lista_ok() {
    Cliente c = new Cliente(
        "Ana", "ana", "ana@mail.com", "pwd",
        "López", date(1995, 5, 20), "UY",
        TipoDocumento.CEDULA, "4.444.444-4"
    );
    Aerolinea a = new Aerolinea(
        "Sky", "sky", "info@sky.com", "s3cr3t",
        "Sky desc", "https://sky.test"
    );

    List<DataUsuario> dtos = ManejadorUsuario.toDTOs(Arrays.asList(c, a));
    assertEquals(2, dtos.size());
    assertInstanceOf(DataCliente.class, dtos.get(0));
    assertInstanceOf(DataAerolinea.class, dtos.get(1));
  }

  // ====== toEntities(List<? extends DataUsuario>) ======
  @Test
  void toEntities_lista_ok() {
    DataCliente dc = new DataCliente(
        "Luis", "luis", "luis@mail.com", "pwd",
        "Pérez", date(1990, 10, 1), "UY",
        TipoDocumento.CEDULA, "3.333.333-3"
    );
    DataAerolinea da = new DataAerolinea(
        "AeroMax", "amax", "info@amax.com", "pass",
        "Desc AM", "https://amax.test"
    );

    List<Usuario> entidades = ManejadorUsuario.toEntities(Arrays.asList(dc, da));
    assertEquals(2, entidades.size());
    assertInstanceOf(Cliente.class, entidades.get(0));
    assertInstanceOf(Aerolinea.class, entidades.get(1));

    Cliente cli = (Cliente) entidades.get(0);
    assertEquals("Luis", cli.getNombre());
    assertEquals("luis", cli.getNickname());
    assertEquals("luis@mail.com", cli.getEmail());
    assertEquals(TipoDocumento.CEDULA, cli.getTipoDocumento());
    assertEquals("3.333.333-3", cli.getNumDocumento());

    Aerolinea aer = (Aerolinea) entidades.get(1);
    assertEquals("AeroMax", aer.getNombre());
    assertEquals("amax", aer.getNickname());
    assertEquals("info@amax.com", aer.getEmail());
  }
  
  //==========toDto CompraPaquete ========
  @Test
  void toDtoCompraPaquete_ok() {
	  Date nac = date(2000, 1, 2);
	  Cliente cli = new Cliente(
        "Eze", "mmarce", "em@ex.com", "abc",
        "Marcenal", nac, "Uruguay",
        TipoDocumento.CEDULA, "5.555.555-5"
    );
	  Paquete pac = new Paquete();
	  
	  CompraPaquete compra = new CompraPaquete(cli, pac, new Date(), new Date(), new BigDecimal(100));
	  
	  ManejadorCompraPaquete.toDTO(compra);
  }
  
  // ========== Categorias ===============
  

  // -------- toEntities (List<? extends DataCategoria>) --------

  @Test
  void toEntities_lista_cate_ok() {
    List<DataCategoria> dtos = Arrays.asList(
        new DataCategoria("Promos"),
        new DataCategoria("Internacionales"),
        new DataCategoria("LowCost")
    );

    List<Categoria> entidades = ManejadorCategoria.toEntities(dtos);

    assertEquals(3, entidades.size());
    assertEquals("Promos", entidades.get(0).getNombre());
    assertEquals("Internacionales", entidades.get(1).getNombre());
    assertEquals("LowCost", entidades.get(2).getNombre());
  }

  @Test
  void toEntities_listaVacia_ok() {
    List<Categoria> entidades = ManejadorCategoria.toEntities(Collections.emptyList());
    assertNotNull(entidades);
    assertTrue(entidades.isEmpty());
  }

  @Test
  void toEntities_elementoNull_lanzaNPE() {
    List<DataCategoria> dtosConNull = Arrays.asList(
        new DataCategoria("A"),
        null,
        new DataCategoria("B")
    );

    assertThrows(NullPointerException.class, () -> ManejadorCategoria.toEntities(dtosConNull));
  }

  // Nota: Si llamás con la lista en sí = null, el NPE ocurre antes del stream.
  @Test
  void toEntities_listaNull_lanzaNPE() {
    assertThrows(NullPointerException.class, () -> ManejadorCategoria.toEntities(null));
  }

  // -------- toDTOs (List<? extends Categoria>) --------

  @Test
  void toDTOs_cate_lista_ok() {
    List<Categoria> cats = Arrays.asList(
        new Categoria("Nacionales"),
        new Categoria("Regional"),
        new Categoria("Charters")
    );

    List<DataCategoria> dtos = ManejadorCategoria.toDTOs(cats);

    assertEquals(3, dtos.size());
    assertEquals("Nacionales", dtos.get(0).getNombre());
    assertEquals("Regional", dtos.get(1).getNombre());
    assertEquals("Charters", dtos.get(2).getNombre());
  }

  @Test
  void toDTOs_listaVacia_ok() {
    List<DataCategoria> dtos = ManejadorCategoria.toDTOs(Collections.emptyList());
    assertNotNull(dtos);
    assertTrue(dtos.isEmpty());
  }

  @Test
  void toDTOs_elementoNull() {
    List<Categoria> catsConNull = Arrays.asList(
        new Categoria("A"),
        null,
        new Categoria("B")
    );

    assertThrows(NullPointerException.class, () -> ManejadorCategoria.toDTOs(catsConNull));
  }

  @Test
  void toDTOs_listaNull() {
    assertThrows(NullPointerException.class, () -> ManejadorCategoria.toDTOs(null));
  }
  
  //================== Pasaje =============
  
  @Test
  void toEntity_ok() {
    DataPasaje dto = new DataPasaje("Ana", "López");
    Pasaje ent = ManejadorPasaje.toEntity(dto);
    assertNotNull(ent);
    assertEquals("Ana", ent.getNombre());
    assertEquals("López", ent.getApellido());
  }

  @Test
  void toEntity_null_lanzaNPE() {
    NullPointerException ex = assertThrows(
        NullPointerException.class,
        () -> ManejadorPasaje.toEntity(null)
    );
    assertTrue(ex.getMessage() != null && ex.getMessage().contains("Los datos no pueden ser nulos"));
  }

  @Test
  void toData_ok() {
    Pasaje ent = new Pasaje("Bruno", "Pérez");
    DataPasaje dto = ManejadorPasaje.toData(ent);
    assertNotNull(dto);
    assertEquals("Bruno", dto.getNombre());
    assertEquals("Pérez", dto.getApellido());
  }

  @Test
  void toData_null_lanzaNPE() {
    NullPointerException ex = assertThrows(
        NullPointerException.class,
        () -> ManejadorPasaje.toData(null)
    );
    assertTrue(ex.getMessage() != null && ex.getMessage().contains("El pasaje no puede ser nulo"));
  }

  @Test
  void toEntities_listaNull_devuelveVacia() {
    List<Pasaje> res = ManejadorPasaje.toEntities(null);
    assertNotNull(res);
    assertTrue(res.isEmpty());
  }

  @Test
  void toEntities_listaVacia_devuelveVacia() {
    List<Pasaje> res = ManejadorPasaje.toEntities(Collections.emptyList());
    assertNotNull(res);
    assertTrue(res.isEmpty());
  }

  @Test
  void toEntities_filtraNulls_y_mapeaEnOrden() {
    List<DataPasaje> dtos = Arrays.asList(
        new DataPasaje("Ana", "López"),
        null,
        new DataPasaje("Carlos", "García"),
        null
    );
    List<Pasaje> res = ManejadorPasaje.toEntities(dtos);
    assertEquals(2, res.size());
    assertEquals("Ana", res.get(0).getNombre());
    assertEquals("López", res.get(0).getApellido());
    assertEquals("Carlos", res.get(1).getNombre());
    assertEquals("García", res.get(1).getApellido());
  }

  @Test
  void toDatas_listaNull_devuelveVacia() {
    List<DataPasaje> res = ManejadorPasaje.toDatas(null);
    assertNotNull(res);
    assertTrue(res.isEmpty());
  }

  @Test
  void toDatas_listaVacia_devuelveVacia() {
    List<DataPasaje> res = ManejadorPasaje.toDatas(Collections.emptyList());
    assertNotNull(res);
    assertTrue(res.isEmpty());
  }

  @Test
  void toDatas_filtraNulls_y_mapeaEnOrden() {
    List<Pasaje> ents = Arrays.asList(
        new Pasaje("Diego", "Suárez"),
        null,
        new Pasaje("Elena", "Martínez")
    );
    List<DataPasaje> res = ManejadorPasaje.toDatas(ents);
    assertEquals(2, res.size());
    assertEquals("Diego", res.get(0).getNombre());
    assertEquals("Suárez", res.get(0).getApellido());
    assertEquals("Elena", res.get(1).getNombre());
    assertEquals("Martínez", res.get(1).getApellido());
  }
  
  // ================ Manejador Reservas ============
  
  @Test
  void toEntities_listaNull_devuelveVaciaRes() {
    List<Reserva> res = ManejadorReserva.toEntities(null);
    assertNotNull(res);
    assertTrue(res.isEmpty());
  }

  @Test
  void toEntities_listaVacia_devuelveVaciaRes() {
    List<Reserva> res = ManejadorReserva.toEntities(Collections.emptyList());
    assertNotNull(res);
    assertTrue(res.isEmpty());
  }

  @Test
  void toData_conPasajes_copiaListaDePasajes() {
    Cliente cli = new Cliente("Ana","ana","ana@mail.com","pwd","López", new Date(), "UY", TipoDocumento.CEDULA, "4.444.444-4");
    Reserva res = new Reserva(new Date(), TipoAsiento.TURISTA, Equipaje.BOLSO, 1, (float) 100.5, cli);

    Pasaje p1 = new Pasaje("Juan","Pérez");  p1.setReserva(res);
    Pasaje p2 = new Pasaje("María","García"); p2.setReserva(res);
    res.getPasajes().add(p1);
    res.getPasajes().add(p2);

    DataReserva dr = ManejadorReserva.toData(res);

    assertNotNull(dr);
    assertNotNull(dr.getPasajes());
    assertEquals(2, dr.getPasajes().size());
    assertEquals("Juan",  dr.getPasajes().get(0).getNombre());
    assertEquals("Pérez", dr.getPasajes().get(0).getApellido());
    assertEquals("María", dr.getPasajes().get(1).getNombre());
    assertEquals("García",dr.getPasajes().get(1).getApellido());
  }

  @Test
  void toData_sinPasajes_noSeteaLista() {
    Cliente cli = new Cliente("Eze","eze","eze@mail.com","pwd","Marcenal", new Date(), "UY",
                              TipoDocumento.CEDULA, "5.555.555-5");
    Reserva res = new Reserva(new Date(), TipoAsiento.TURISTA, Equipaje.BOLSO, 1, 100.5f, cli);

    DataReserva dr = ManejadorReserva.toData(res);

    assertNotNull(dr.getPasajes());
    assertTrue(dr.getPasajes().isEmpty());
  }  

}
