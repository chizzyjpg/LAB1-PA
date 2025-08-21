package Presentacion;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

// importá tus DTO/Interfaces reales:
import Logica.ISistema;
import Logica.DataUsuario;
import Logica.DataCliente;
import Logica.DataAerolinea;

//// ================= ENUMS (UML) =================
//enum TipoAsiento { TURISTA, EJECUTIVO }
//enum Equipaje { BOLSO, MOCHILA_CARRYON }
//
//// ================= MODELO (UML) =================
//// Usuario (abstracto) -> Cliente / Aerolinea
//abstract class Usuario {
//    private final String nickname;
//    private final String nombre;
//    private final String email;
//
//    public Usuario(String nickname, String nombre, String email) {
//        this.nickname = nickname;
//        this.nombre = nombre;
//        this.email = email;
//    }
//    public String getNickname() { return nickname; }
//    public String getNombre()   { return nombre; }
//    public String getEmail()    { return email; }
//
//    @Override public String toString() { return nickname; }
//
//    public String cabeceraUsuario() {
//        return  "Nickname: " + nickname + "\n" +
//                "Nombre:   " + nombre   + "\n" +
//                "Email:    " + email    + "\n";
//    }
//}
//
//class Cliente extends Usuario {
//    private final String apellido;
//    private final LocalDate fechaNac;
//    private final String nacionalidad;
//    private final String tipoDocumento;
//    private final String numDocumento;
//
//    // Asociación 1..* con Reserva (el cliente "tiene" reservas)
//    private final List<Reserva> reservas = new ArrayList<>();
//
//    public Cliente(String nick, String nombre, String email,
//                   String apellido, LocalDate fechaNac, String nacionalidad,
//                   String tipoDocumento, String numDocumento) {
//        super(nick, nombre, email);
//        this.apellido = apellido;
//        this.fechaNac = fechaNac;
//        this.nacionalidad = nacionalidad;
//        this.tipoDocumento = tipoDocumento;
//        this.numDocumento = numDocumento;
//    }
//
//    public void agregarReserva(Reserva r) { reservas.add(r); }
//    public List<Reserva> getReservas() { return Collections.unmodifiableList(reservas); }
//
//    @Override public String toString() { return getNickname() + " (Cliente)"; }
//
//    public String mostrarDatos() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(cabeceraUsuario());
//        sb.append("\n[CLIENTE]\n");
//        sb.append("Apellido:        ").append(apellido).append('\n');
//        sb.append("Fecha Nac.:      ").append(fechaNac).append('\n');
//        sb.append("Nacionalidad:    ").append(nacionalidad).append('\n');
//        sb.append("Tipo Documento:  ").append(tipoDocumento).append('\n');
//        sb.append("N° Documento:    ").append(numDocumento).append('\n');
//
//        sb.append("\nReservas (").append(reservas.size()).append("):\n");
//        for (Reserva r : reservas) {
//            sb.append("  - ").append(r.resumen()).append('\n');
//        }
//        return sb.toString();
//    }
//}
//
//class Aerolinea extends Usuario {
//    private final String descGeneral;
//    private final String linkWeb;
//
//    // Asociaciones con Ruta y VueloEspecifico
//    private final List<Ruta> rutas = new ArrayList<>();
//    private final List<VueloEspecifico> vuelos = new ArrayList<>();
//
//    public Aerolinea(String nick, String nombre, String email, String descGeneral, String linkWeb) {
//        super(nick, nombre, email);
//        this.descGeneral = descGeneral;
//        this.linkWeb = linkWeb;
//    }
//
//    public void agregarRuta(Ruta r) { rutas.add(r); }
//    public void agregarVuelo(VueloEspecifico v) { vuelos.add(v); }
//
//    public List<Ruta> getRutas() { return Collections.unmodifiableList(rutas); }
//    public List<VueloEspecifico> getVuelos() { return Collections.unmodifiableList(vuelos); }
//
//    @Override public String toString() { return getNickname() + " (Aerolínea)"; }
//
//    public String mostrarDatos() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(cabeceraUsuario());
//        sb.append("\n[AEROLÍNEA]\n");
//        sb.append("Descripción: ").append(descGeneral).append('\n');
//        sb.append("Web:         ").append(linkWeb).append('\n');
//
//        sb.append("\nRutas (").append(rutas.size()).append("):\n");
//        for (Ruta r : rutas) {
//            sb.append("  - ").append(r.resumen()).append('\n');
//        }
//
//        sb.append("\nVuelos Específicos (").append(vuelos.size()).append("):\n");
//        for (VueloEspecifico v : vuelos) {
//            sb.append("  - ").append(v.resumen()).append('\n');
//        }
//        return sb.toString();
//    }
//}
//
//// ----- Ruta -----
//class Ruta {
//    private final String nombre;         // único
//    private final String descripcion;
//    private final String ciudadOrigen;
//    private final String ciudadDestino;
//    private final LocalTime hora;
//    private final LocalDate fechaAlta;
//    private final double costoBaseTurista;
//    private final double costoBaseEjecutivo;
//    private final double costoEquipajeExtra; // por unidad
//    private final List<String> categorias;   // simplificado
//
//    public Ruta(String nombre, String descripcion, String ciudadOrigen, String ciudadDestino,
//                LocalTime hora, LocalDate fechaAlta,
//                double costoBaseTurista, double costoBaseEjecutivo, double costoEquipajeExtra,
//                List<String> categorias) {
//        this.nombre = nombre;
//        this.descripcion = descripcion;
//        this.ciudadOrigen = ciudadOrigen;
//        this.ciudadDestino = ciudadDestino;
//        this.hora = hora;
//        this.fechaAlta = fechaAlta;
//        this.costoBaseTurista = costoBaseTurista;
//        this.costoBaseEjecutivo = costoBaseEjecutivo;
//        this.costoEquipajeExtra = costoEquipajeExtra;
//        this.categorias = categorias;
//    }
//
//    public String getNombre() { return nombre; }
//    public LocalTime getHora() { return hora; }
//    public double getCostoBase(TipoAsiento asiento) {
//        return (asiento == TipoAsiento.TURISTA) ? costoBaseTurista : costoBaseEjecutivo;
//    }
//    public double getCostoEquipajeExtra() { return costoEquipajeExtra; }
//
//    public String resumen() {
//        return nombre + " [" + ciudadOrigen + " → " + ciudadDestino + " " + hora + "]";
//    }
//}
//
//// ----- Vuelo Específico -----
//class VueloEspecifico {
//    private final String nombre;
//    private final LocalDate fecha;
//    private final int duracionMin;     // minutos
//    private final int maxAsientosTur;
//    private final int maxAsientosEjec;
//    private final LocalDate fechaAlta;
//    private final Ruta ruta;           // 1..* Ruta ↔ VueloEspecifico (cada vuelo pertenece a una ruta)
//
//    public VueloEspecifico(String nombre, LocalDate fecha, int duracionMin,
//                           int maxAsientosTur, int maxAsientosEjec, LocalDate fechaAlta, Ruta ruta) {
//        this.nombre = nombre;
//        this.fecha = fecha;
//        this.duracionMin = duracionMin;
//        this.maxAsientosTur = maxAsientosTur;
//        this.maxAsientosEjec = maxAsientosEjec;
//        this.fechaAlta = fechaAlta;
//        this.ruta = ruta;
//    }
//
//    public Ruta getRuta() { return ruta; }
//
//    public String resumen() {
//        return nombre + " / " + ruta.getNombre() + " (" + fecha + ", " + duracionMin + "min)";
//    }
//}
//
//// ----- Paquete -----
//class Paquete {
//    private final String nombre;       // único
//    private final String descripcion;
//    private final Map<Ruta, Integer> cantPorRuta = new LinkedHashMap<>();
//    private final TipoAsiento tipoAsientoSeleccionado;
//    private final LocalDate fechaCompra;
//    private final int validezDias;
//    private final double costoAsociado;   // precio promocional
//
//    public Paquete(String nombre, String descripcion,
//                   Map<Ruta,Integer> cantPorRuta,
//                   TipoAsiento tipoAsientoSeleccionado,
//                   LocalDate fechaCompra, int validezDias, double costoAsociado) {
//        this.nombre = nombre;
//        this.descripcion = descripcion;
//        this.cantPorRuta.putAll(cantPorRuta);
//        this.tipoAsientoSeleccionado = tipoAsientoSeleccionado;
//        this.fechaCompra = fechaCompra;
//        this.validezDias = validezDias;
//        this.costoAsociado = costoAsociado;
//    }
//
//    public String getNombre() { return nombre; }
//    public TipoAsiento getTipoAsientoSeleccionado() { return tipoAsientoSeleccionado; }
//    public double getCostoAsociado() { return costoAsociado; }
//
//    public String resumen() {
//        return nombre + " (" + tipoAsientoSeleccionado + ", $" + costoAsociado + ")";
//    }
//}
//
//// ----- Pasaje -----
//class Pasaje {
//    private final String nombre;
//    private final String apellido;
//
//    public Pasaje(String nombre, String apellido) {
//        this.nombre = nombre;
//        this.apellido = apellido;
//    }
//    @Override public String toString() { return nombre + " " + apellido; }
//}
//
//// ----- Reserva (RNE: o vuelo o paquete) -----
//class Reserva {
//    private final LocalDate fechaReserva;
//    private final TipoAsiento tipoAsiento;
//    private final Equipaje equipajeBasico;
//    private final int cantEquipajeExtra;
//
//    private final VueloEspecifico vuelo;  // exclusivo
//    private final Paquete paquete;        // exclusivo
//
//    private final List<Pasaje> pasajes;   // 1..*
//
//    public Reserva(LocalDate fechaReserva, TipoAsiento tipoAsiento,
//                   Equipaje equipajeBasico, int cantEquipajeExtra,
//                   VueloEspecifico vuelo, Paquete paquete,
//                   List<Pasaje> pasajes) {
//        // RNE: no pueden ser ambos a la vez ni ambos nulos
//        if ((vuelo == null) == (paquete == null)) {
//            throw new IllegalArgumentException("La reserva debe referir SOLO a un vuelo específico O a un paquete.");
//        }
//        if (pasajes == null || pasajes.isEmpty()) {
//            throw new IllegalArgumentException("La reserva debe tener al menos un pasaje (1..*).");
//        }
//        this.fechaReserva = fechaReserva;
//        this.tipoAsiento = tipoAsiento;
//        this.equipajeBasico = equipajeBasico;
//        this.cantEquipajeExtra = cantEquipajeExtra;
//        this.vuelo = vuelo;
//        this.paquete = paquete;
//        this.pasajes = new ArrayList<>(pasajes);
//    }
//
//    public double getCostoTotal() {
//        if (vuelo != null) {
//            Ruta r = vuelo.getRuta();
//            double base = r.getCostoBase(tipoAsiento);
//            double extra = cantEquipajeExtra * r.getCostoEquipajeExtra();
//            return base + extra;
//        } else {
//            // simplificación: si viene de paquete, el costo es el del paquete (promocional)
//            return paquete.getCostoAsociado();
//        }
//    }
//
//    public String resumen() {
//        String destino = (vuelo != null) ? ("Vuelo: " + vuelo.resumen())
//                                         : ("Paquete: " + paquete.resumen());
//        return destino +
//               " | Asiento: " + tipoAsiento +
//               " | Equipaje extra: " + cantEquipajeExtra +
//               " | Pasajeros: " + pasajes +
//               " | Total: $" + String.format(Locale.US,"%.2f", getCostoTotal());
//    }
//}

// ================= UI (igual a tu enfoque) =================


public class ConsultaUsuario extends JInternalFrame {

    private final ISistema sistema;                 // <-- NUEVO: guardamos la referencia
    private final JComboBox<DataUsuario> comboUsuarios;
    private final JButton btnConsultar;
    private final JButton btnRefrescar;            // <-- NUEVO
    private final JTextArea areaDatos;
    private final JScrollPane scrollDatos;
    private boolean expandido = false;

    public ConsultaUsuario(ISistema sistema) {
        super("Consulta de Usuario", true, true, true, true);
        this.sistema = sistema;                    // <-- NUEVO
        setSize(455, 140);
        setMinimumSize(new Dimension(455, 140));
        setLayout(new BorderLayout());

        // 1) Panel superior (combo + botones)
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTop.add(new JLabel("Usuario:"));

        comboUsuarios = new JComboBox<>();
        comboUsuarios.setPreferredSize(new Dimension(260, 26));
        comboUsuarios.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataUsuario du) {
                    boolean esCliente   = du instanceof DataCliente;
                    boolean esAero      = du instanceof DataAerolinea;
                    String  tipo        = esCliente ? "Cliente" : esAero ? "Aerolínea" : "Usuario";
                    String  nick        = nvl(du.getNickname());
                    String  nombre      = nvl(du.getNombre());
                    String  email       = nvl(du.getEmail());
                    String  extra       = "";
                    if (esCliente) {
                        DataCliente dc = (DataCliente) du;
                        extra = " | " + nvl(dc.getApellido());
                    }
                    setText(String.format("%s: %s — %s%s", tipo, nick, nombre, extra));
                    setToolTipText(email.isEmpty() ? null : email);
                }
                return this;
            }
        });
        panelTop.add(comboUsuarios);

        btnConsultar = new JButton("Consultar");
        panelTop.add(btnConsultar);

        btnRefrescar = new JButton("Refrescar");   // <-- NUEVO
        panelTop.add(btnRefrescar);

        add(panelTop, BorderLayout.NORTH);

        // 2) Área de detalle
        areaDatos = new JTextArea(14, 52);
        areaDatos.setEditable(false);
        areaDatos.setLineWrap(true);
        areaDatos.setWrapStyleWord(true);
        areaDatos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        scrollDatos = new JScrollPane(areaDatos);
        scrollDatos.setVisible(false);
        add(scrollDatos, BorderLayout.CENTER);

        // 3) Acciones
        btnConsultar.addActionListener(e -> consultarSeleccion());
        btnRefrescar.addActionListener(e -> cargarUsuariosEnCombo()); // <-- NUEVO

        // 4) Cargar al iniciar
        cargarUsuariosEnCombo();
        SwingUtilities.invokeLater(() -> comboUsuarios.requestFocusInWindow());
    }

    // Carga/recarga el combo desde el sistema (ordenado por nickname)
    private void cargarUsuariosEnCombo() {
        try {
            List<DataUsuario> usuarios = sistema.listarUsuarios();
            usuarios.sort(Comparator.comparing(
                    u -> nvl(u.getNickname()), String.CASE_INSENSITIVE_ORDER));

            comboUsuarios.setModel(new DefaultComboBoxModel<>(
                    usuarios.toArray(new DataUsuario[0])));

            boolean hay = !usuarios.isEmpty();
            btnConsultar.setEnabled(hay);
            if (!hay) {
                scrollDatos.setVisible(false);
                setSize(getWidth(), 140);
                expandido = false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo listar usuarios:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Usa nickname para traer el DTO actualizado desde el Sistema
    private void consultarSeleccion() {
    DataUsuario du = (DataUsuario) comboUsuarios.getSelectedItem();
    if (du == null) return;

    String nick = nvl(du.getNickname());
    StringBuilder sb = new StringBuilder();

    // Intento como Cliente
    DataCliente dc = sistema.verInfoCliente(nick);
    if (dc != null) {
        sb.append("[CLIENTE]\n");
        sb.append("Nickname:           ").append(nvl(dc.getNickname())).append('\n');
        sb.append("Nombre:             ").append(nvl(dc.getNombre())).append('\n');
        sb.append("Apellido:           ").append(nvl(dc.getApellido())).append('\n');
        sb.append("Email:              ").append(nvl(dc.getEmail())).append('\n');
        sb.append("Fecha de nacimiento:").append(fmtFecha(dc.getFechaNac())).append('\n');
        sb.append("Nacionalidad:       ").append(nvl(dc.getNacionalidad())).append('\n');
        sb.append("Tipo/N° documento:  ")
          .append(dc.getTipoDocumento() == null ? "-" : dc.getTipoDocumento().name())
          .append(" ")
          .append(nvl(dc.getNumDocumento()))
          .append('\n');

        mostrarDetalle(sb.toString());
        return;
    }

    // Si no era cliente, pruebo como Aerolínea
    DataAerolinea da = sistema.verInfoAerolinea(nick);
    if (da != null) {
        sb.append("[AEROLÍNEA]\n");
        sb.append("Nickname:     ").append(nvl(da.getNickname())).append('\n');
        sb.append("Nombre:       ").append(nvl(da.getNombre())).append('\n');
        sb.append("Email:        ").append(nvl(da.getEmail())).append('\n');
        sb.append("Descripción:  ").append(nvl(da.getDescripcion())).append('\n');
        sb.append("Sitio web:    ").append(nvl(da.getSitioWeb())).append('\n');

        mostrarDetalle(sb.toString());
        return;
    }

    JOptionPane.showMessageDialog(this, "No se encontró el usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
}
    private void mostrarDetalle(String texto) {
        areaDatos.setText(texto);
        scrollDatos.setVisible(true);
        if (!expandido) {
            expandido = true;
            setSize(getWidth(), 460);
            revalidate();
            repaint();
        }
    }
private static String fmtFecha(java.util.Date d) {
    if (d == null) return "-";
    return new java.text.SimpleDateFormat("dd/MM/yyyy").format(d);
}
    private static String nvl(String s) { return s == null ? "" : s; }
}
