package Presentacion;

import Logica.ISistema;
import Logica.DataPaquete;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ConsultaPaqRutasVuelo extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    // --- IMPORTANTE: para WindowBuilder, tener un ctor sin args ---
    /** Constructor solo para el diseñador. No usar en runtime. */
    public ConsultaPaqRutasVuelo() {
        this(null);
    }

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
        // Render para que muestre el nombre y no la dirección de memoria
        comboPaquetes.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value instanceof DataPaquete d ? d.getNombre() : "");
                return this;
            }
        });
        getContentPane().add(comboPaquetes);

        JButton btnAceptar = new JButton("ACEPTAR");
        btnAceptar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        btnAceptar.setBackground(new Color(5, 250, 79));
        btnAceptar.setBounds(560, 15, 110, 26);
        btnAceptar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                mostrarDetalleSeleccionado();
            }
        });
        getContentPane().add(btnAceptar);

        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        btnCancelar.setBackground(new Color(241, 43, 14));
        btnCancelar.setBounds(680, 15, 110, 26);
        btnCancelar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { dispose(); }
        });
        getContentPane().add(btnCancelar);

        /* ----------- Datos del paquete ----------- */
        int y = 60;
        txtNombre      = roField("Nombre:",          10, y, 180, 28, 260); y += 34;
        txtCosto       = roField("Costo:",           10, y, 180, 28, 160);
        txtValidez     = roField("Validez (días):", 380, y, 150, 28, 120); y += 34;
        txtCantRutas   = roField("Cant. Rutas:",     10, y, 180, 28, 120);
     // Cant. Rutas (queda igual)
        txtCantRutas = roField("Cant. Rutas:", 10, y, 180, 28, 120);

        // -------- Tipo de Asiento (label + campo) --------
        JLabel lblTipoAsiento = new JLabel("Tipo de Asiento:");
        lblTipoAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        // Lo movemos después del campo de Cant. Rutas (que termina en x=318)
        lblTipoAsiento.setBounds(330, y, 140, 28);
        getContentPane().add(lblTipoAsiento);

        txtTipoAsiento = new JTextField();
        txtTipoAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        txtTipoAsiento.setEditable(false);
        // Campo alineado a continuación del label (330 + 140 + 8 = 478)
        txtTipoAsiento.setBounds(478, y, 150, 28); // ocupa 478..628
        getContentPane().add(txtTipoAsiento);

        // -------- Descuento (label + campo) --------
        // Lo corremos más a la derecha para que no quede debajo del campo anterior
        JLabel lblDescuento = new JLabel("Descuento (%):");
        lblDescuento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        lblDescuento.setBounds(650, y, 120, 28);
        getContentPane().add(lblDescuento);

        txtDescuento = new JTextField();
        txtDescuento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        txtDescuento.setEditable(false);
        // Campo a continuación del label (650 + 120 + 8 = 778)
        txtDescuento.setBounds(778, y, 80, 28); // 80 px para que entre en el ancho del frame
        getContentPane().add(txtDescuento);

        y += 40; // mantener tu incremento de fila y += 40;

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

        /* ----------- Tabla de rutas ----------- */
        rutasModel = new DefaultTableModel(new Object[]{"Ruta", "Cupos"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) { return c == 1 ? Integer.class : String.class; }
        };
        tblRutas = new JTable(rutasModel);
        JScrollPane spRutas = new JScrollPane(tblRutas);
        spRutas.setBorder(BorderFactory.createTitledBorder("Rutas que integran el paquete"));
        // Reducimos altura para que el botón entre en el alto total del frame
        spRutas.setBounds(10, y + 120, 780, 250);
        getContentPane().add(spRutas);

        JButton btnVerRuta = new JButton("Ver ruta…");
        btnVerRuta.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        // Ubicación visible dentro de la ventana
        btnVerRuta.setBounds(690, y + 380, 100, 28);
        btnVerRuta.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                abrirConsultaRutaVuelo();
            }
        });
        getContentPane().add(btnVerRuta);

        // Carga de datos (salteada si sistema==null para que WindowBuilder no falle)
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
        comboPaquetes.removeAllItems();
        List<DataPaquete> lista = sistema.listarPaquetes();
        if (lista != null) {
            lista.stream()
                 .sorted((a,b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
                 .forEach(comboPaquetes::addItem);
        }
    }

    private void seleccionarPrimero() {
        if (comboPaquetes.getItemCount() > 0) {
            comboPaquetes.setSelectedIndex(0);
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

        rutasModel.setRowCount(0);
        Map<String, Integer> cupos = d.getCuposPorRuta(); // ya lo agregamos al DataPaquete
        if (cupos != null && !cupos.isEmpty()) {
            cupos.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                    .forEach(e -> rutasModel.addRow(new Object[]{ e.getKey(), e.getValue() }));
        }
    }

    private void abrirConsultaRutaVuelo() {
        JDesktopPane dp = getDesktopPane();
        if (dp == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el escritorio para abrir la ventana.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            ConsultaRutaVuelo frame = new ConsultaRutaVuelo(sistema); // usa tu constructor real
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
