package Presentacion;

import Logica.ISistema;
import Logica.DataPaquete;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConsultaPaqRutasVuelo extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    public ConsultaPaqRutasVuelo() { this(null); }

    private boolean cargandoPaquetes = false;
    private final ISistema sistema;

    private JComboBox<DataPaquete> comboPaquetes;
    private JTextField txtNombre, txtCosto, txtValidez, txtCantRutas, txtTipoAsiento, txtDescuento;
    private JTextArea txtDescripcion;

    private JTable tblRutas;
    private DefaultTableModel rutasModel;

    public ConsultaPaqRutasVuelo(ISistema sistema) {
        this.sistema = sistema;

        setTitle("Consulta de Paquete de Rutas de Vuelo");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setBounds(100, 100, 875, 608);
        getContentPane().setLayout(null);

        /* ----------- Barra superior ----------- */
        JLabel lblPaquetesRegistrados = new JLabel("Paquetes Registrados:");
        lblPaquetesRegistrados.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        lblPaquetesRegistrados.setBounds(10, 15, 180, 24);
        getContentPane().add(lblPaquetesRegistrados);

        comboPaquetes = new JComboBox<>();
        comboPaquetes.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        comboPaquetes.setBounds(190, 16, 350, 24);
        comboPaquetes.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value instanceof DataPaquete d ? d.getNombre() : "");
                return this;
            }
        });
        comboPaquetes.addActionListener(e -> { if (!cargandoPaquetes) mostrarDetalleSeleccionado(); });
        getContentPane().add(comboPaquetes);

        JButton btnCancelar = new JButton("CERRAR");
        btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        btnCancelar.setBackground(new Color(241, 43, 14));
        btnCancelar.setBounds(680, 15, 110, 26);
        btnCancelar.addActionListener(e -> dispose());
        getContentPane().add(btnCancelar);

        /* ----------- Datos del paquete ----------- */
        int y = 60;
        txtNombre      = roField("Nombre:",          10, y, 180, 28, 260); y += 34;
        txtCosto       = roField("Costo:",           10, y, 180, 28, 160);
        txtValidez     = roField("Validez (días):", 380, y, 150, 28, 120); y += 34;
        txtCantRutas   = roField("Cant. Rutas:",     10, y, 180, 28, 120);

        JLabel lblTipoAsiento = new JLabel("Tipo de Asiento:");
        lblTipoAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        lblTipoAsiento.setBounds(330, y, 140, 28);
        getContentPane().add(lblTipoAsiento);

        txtTipoAsiento = new JTextField();
        txtTipoAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        txtTipoAsiento.setEditable(false);
        txtTipoAsiento.setBounds(478, y, 150, 28);
        getContentPane().add(txtTipoAsiento);

        JLabel lblDescuento = new JLabel("Descuento (%):");
        lblDescuento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        lblDescuento.setBounds(650, y, 120, 28);
        getContentPane().add(lblDescuento);

        txtDescuento = new JTextField();
        txtDescuento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        txtDescuento.setEditable(false);
        txtDescuento.setBounds(778, y, 80, 28);
        getContentPane().add(txtDescuento);

        y += 40;

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        lblDesc.setBounds(10, y, 100, 20);
        getContentPane().add(lblDesc);

        txtDescripcion = new JTextArea();
        txtDescripcion.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane spDesc = new JScrollPane(txtDescripcion);
        spDesc.setBounds(10, y + 20, 780, 90);
        getContentPane().add(spDesc);

        /* ----------- Tabla de rutas (solo nombre) ----------- */
        rutasModel = new DefaultTableModel(new Object[]{"Ruta"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) { return String.class; }
        };
        tblRutas = new JTable(rutasModel);
        JScrollPane spRutas = new JScrollPane(tblRutas);
        spRutas.setBorder(BorderFactory.createTitledBorder("Rutas que integran el paquete"));
        spRutas.setBounds(10, y + 120, 780, 250);
        getContentPane().add(spRutas);

        JButton btnVerRuta = new JButton("Ver ruta…");
        btnVerRuta.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        btnVerRuta.setBounds(690, y + 380, 100, 28);
        btnVerRuta.addActionListener(e -> abrirConsultaRutaVuelo());
        getContentPane().add(btnVerRuta);

        if (this.sistema != null) {
            cargarPaquetes();
            seleccionarPrimero();
        }
    }

    /* ---------------- Helpers ---------------- */

    private JTextField roField(String label, int xLbl, int y, int wLbl, int h, int wField) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        l.setBounds(xLbl, y, wLbl, h);
        getContentPane().add(l);

        JTextField t = new JTextField();
        t.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        t.setEditable(false);
        t.setBounds(xLbl + wLbl + 8, y, wField, h);
        getContentPane().add(t);
        return t;
    }

    private void cargarPaquetes() {
        cargandoPaquetes = true;
        try {
            comboPaquetes.removeAllItems();
            List<DataPaquete> lista = sistema.listarPaquetes();
            if (lista != null) {
                lista.stream()
                        .sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
                        .forEach(comboPaquetes::addItem);
            }
        } finally {
            cargandoPaquetes = false;
        }
    }

    private void seleccionarPrimero() {
        if (comboPaquetes.getItemCount() > 0) {
            try {
                comboPaquetes.setSelectedIndex(0);
            } finally {
                cargandoPaquetes = false;
            }
            mostrarDetalleSeleccionado();
        }
    }

    private void mostrarDetalleSeleccionado() {
        DataPaquete d = (DataPaquete) comboPaquetes.getSelectedItem();
        if (d == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un paquete de rutas de vuelo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        txtNombre.setText(safe(d.getNombre()));
        txtCosto.setText(d.getCosto() != null ? d.getCosto().toPlainString() : "");
        txtValidez.setText(String.valueOf(d.getValidez()));
        txtCantRutas.setText(String.valueOf(d.getCantRutas()));
        txtTipoAsiento.setText(d.getTipoAsiento() != null ? d.getTipoAsiento().name() : "");
        txtDescuento.setText(String.valueOf(d.getDescuento()));
        txtDescripcion.setText(safe(d.getDescripcion()));

        // Cargar solo nombres de ruta
        rutasModel.setRowCount(0);
        Set<String> rutas = d.getRutasIncluidas(); // ahora es un Set<String>
        if (rutas != null && !rutas.isEmpty()) {
            rutas.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .forEach(nombreRuta -> rutasModel.addRow(new Object[]{ nombreRuta }));
        }
    }

    private void abrirConsultaRutaVuelo() {
        int row = tblRutas.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná una ruta en la tabla antes de continuar.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombreRuta = (String) rutasModel.getValueAt(row, 0);

        JDesktopPane dp = getDesktopPane();
        if (dp == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el escritorio para abrir la ventana.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Si tu frame de consulta de ruta admite el nombre de ruta:
            // ConsultaRutaVuelo frame = new ConsultaRutaVuelo(sistema, nombreRuta);
            // Si no, usá el constructor actual y hacé la selección dentro de ese frame.
            ConsultaRutaVuelo frame = new ConsultaRutaVuelo(sistema); // <-- ajustá si tenés sobrecarga con nombreRuta
            dp.add(frame);
            frame.setVisible(true);
            frame.toFront();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir Consulta de Ruta de Vuelo:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String safe(String s) { return (s == null) ? "" : s; }
}