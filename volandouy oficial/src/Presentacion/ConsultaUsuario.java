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
        getContentPane().setLayout(new BorderLayout());

        // 1) Panel superior (combo + botones)
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Usuario:");
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        panelTop.add(label);

        comboUsuarios = new JComboBox<>();
        comboUsuarios.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
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
        btnConsultar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        panelTop.add(btnConsultar);

        btnRefrescar = new JButton("Refrescar");   // <-- NUEVO
        panelTop.add(btnRefrescar);

        getContentPane().add(panelTop, BorderLayout.NORTH);

        // 2) Área de detalle
        areaDatos = new JTextArea(14, 52);
        areaDatos.setEditable(false);
        areaDatos.setLineWrap(true);
        areaDatos.setWrapStyleWord(true);
        areaDatos.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));

        scrollDatos = new JScrollPane(areaDatos);
        scrollDatos.setVisible(false);
        getContentPane().add(scrollDatos, BorderLayout.CENTER);

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
