package Presentacion;

import Logica.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConsultaVuelo extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private final ISistema sistema;

    private final JComboBox<DataAerolinea> comboAerolineas = new JComboBox<>();
    private final JComboBox<DataRuta> comboRutas = new JComboBox<>();

    private final JList<DataVueloEspecifico> listVuelos = new JList<>(new DefaultListModel<>());
    private final JTextArea txtDetalleVuelo = new JTextArea(10, 40);

    private final JList<DataReserva> listReservas = new JList<>(new DefaultListModel<>());
    private final JButton btnVerReserva = new JButton("Ver reserva…");

    private final SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

    // ==== Constructores ====
    public ConsultaVuelo(ISistema sistema) {
        super("Consulta de Vuelo", true, true, true, true);
        this.sistema = sistema;
        setSize(1000, 620);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        armarUI();
        wireEvents();
        cargarAerolineas();
    }

    // (si querés mantener main para probar standalone)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // TODO: pasá una implementación real de ISistema
            ISistema sistema = null;
            ConsultaVuelo frame = new ConsultaVuelo(sistema);
            frame.setVisible(true);
        });
    }

    // ==== UI ====
    private void armarUI() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(root);

        // TOP: Aerolínea + Ruta
        JPanel top = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4,4,4,4);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0; top.add(new JLabel("Aerolínea:"), gc);
        comboAerolineas.setRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataAerolinea a) setText(a.getNickname() + " — " + a.getNombre());
                return this;
            }
        });
        gc.gridx = 1; gc.gridy = 0; gc.weightx = 1; top.add(comboAerolineas, gc);
        gc.weightx = 0;

        gc.gridx = 0; gc.gridy = 1; top.add(new JLabel("Ruta:"), gc);
        comboRutas.setRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataRuta r) setText(r.getNombre());
                return this;
            }
        });
        gc.gridx = 1; gc.gridy = 1; gc.weightx = 1; top.add(comboRutas, gc);

        root.add(top, BorderLayout.NORTH);

        // CENTER: Vuelos (izquierda) + Detalle y Reservas (derecha)
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.30);

        // Izquierda: lista de vuelos
        JPanel left = new JPanel(new BorderLayout(6,6));
        left.add(new JLabel("Vuelos asociados a la ruta"), BorderLayout.NORTH);
        listVuelos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listVuelos.setCellRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataVueloEspecifico v) {
                    String f = (v.getFecha()!=null) ? sdfFecha.format(v.getFecha()) : "-";
                    setText(v.getNombre() + "  (" + f + ")");
                }
                return this;
            }
        });
        left.add(new JScrollPane(listVuelos), BorderLayout.CENTER);
        split.setLeftComponent(left);

        // Derecha: detalle del vuelo + reservas
        JPanel right = new JPanel(new BorderLayout(8,8));

        JPanel detallePanel = new JPanel(new BorderLayout(6,6));
        detallePanel.add(new JLabel("Detalle del vuelo"), BorderLayout.NORTH);
        txtDetalleVuelo.setEditable(false);
        txtDetalleVuelo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        detallePanel.add(new JScrollPane(txtDetalleVuelo), BorderLayout.CENTER);

        JPanel reservasPanel = new JPanel(new BorderLayout(6,6));
        reservasPanel.add(new JLabel("Reservas asociadas"), BorderLayout.NORTH);
        listReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listReservas.setCellRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataReserva r) {
                    r.toString();
                }
                return this;
            }
        });
        reservasPanel.add(new JScrollPane(listReservas), BorderLayout.CENTER);
        JPanel southRes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southRes.add(btnVerReserva);
        reservasPanel.add(southRes, BorderLayout.SOUTH);

        // apilo detalle arriba y reservas abajo
        right.add(detallePanel, BorderLayout.CENTER);
        right.add(reservasPanel, BorderLayout.SOUTH);

        split.setRightComponent(right);
        root.add(split, BorderLayout.CENTER);
    }

    private void wireEvents() {
        comboAerolineas.addActionListener(e -> cargarRutas());
        comboRutas.addActionListener(e -> cargarVuelos());
        listVuelos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarDetalleYReservas();
        });

        btnVerReserva.addActionListener(e -> {
            DataReserva r = listReservas.getSelectedValue();
            if (r == null) {
                JOptionPane.showMessageDialog(this, "Seleccioná una reserva", "Atención", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // TODO: abrir tu frame de "Consulta de Reserva" si existe
            JOptionPane.showMessageDialog(this, "Abrir detalle de reserva: " +
					r.toString(), "Ver Reserva", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // ==== Carga de datos ====
    private void cargarAerolineas() {
        comboAerolineas.removeAllItems();
        try {
            List<DataAerolinea> aas = sistema.listarAerolineas();
            for (DataAerolinea a : aas) comboAerolineas.addItem(a);
            if (!aas.isEmpty()) comboAerolineas.setSelectedIndex(0); // dispara cargarRutas()
        } catch (Exception ex) {
            error("Error cargando aerolíneas: " + ex.getMessage());
        }
    }

    private void cargarRutas() {
        comboRutas.removeAllItems();
        limpiarVuelosYDetalle();

        DataAerolinea a = (DataAerolinea) comboAerolineas.getSelectedItem();
        if (a == null) return;
        try {
            List<DataRuta> rutas = sistema.listarPorAerolinea(a.getNickname());
            for (DataRuta r : rutas) comboRutas.addItem(r);
            if (!rutas.isEmpty()) comboRutas.setSelectedIndex(0); // dispara cargarVuelos()
        } catch (Exception ex) {
            error("Error cargando rutas: " + ex.getMessage());
        }
    }

    private void cargarVuelos() {
        DefaultListModel<DataVueloEspecifico> m = (DefaultListModel<DataVueloEspecifico>) listVuelos.getModel();
        m.clear();
        txtDetalleVuelo.setText("");
        ((DefaultListModel<DataReserva>) listReservas.getModel()).clear();

        DataAerolinea a = (DataAerolinea) comboAerolineas.getSelectedItem();
        DataRuta r = (DataRuta) comboRutas.getSelectedItem();
        if (a == null || r == null) return;

        try {
            List<DataVueloEspecifico> vuelos = sistema.listarVuelos(a.getNickname(), r.getNombre());
            for (DataVueloEspecifico v : vuelos) m.addElement(v);
            if (!vuelos.isEmpty()) listVuelos.setSelectedIndex(0); // dispara mostrarDetalleYReservas()
        } catch (Exception ex) {
            error("Error cargando vuelos: " + ex.getMessage());
        }
    }

    private void mostrarDetalleYReservas() {
        DataVueloEspecifico v = listVuelos.getSelectedValue();
        DataAerolinea a = (DataAerolinea) comboAerolineas.getSelectedItem();
        DataRuta r = (DataRuta) comboRutas.getSelectedItem();

        if (v == null || a == null || r == null) {
            txtDetalleVuelo.setText("");
            ((DefaultListModel<DataReserva>) listReservas.getModel()).clear();
            return;
        }

        txtDetalleVuelo.setText(v.toString());

        DefaultListModel<DataReserva> mr = (DefaultListModel<DataReserva>) listReservas.getModel();
        mr.clear();
        try {
            List<DataReserva> reservas = sistema.listarReservas(a.getNickname(), r.getNombre(), v.getNombre());
            for (DataReserva res : reservas) mr.addElement(res);
        } catch (Exception ex) {
            error("Error cargando reservas: " + ex.getMessage());
        }
    }

    // ==== Helpers ====
    private void limpiarVuelosYDetalle() {
        ((DefaultListModel<DataVueloEspecifico>) listVuelos.getModel()).clear();
        ((DefaultListModel<DataReserva>) listReservas.getModel()).clear();
        txtDetalleVuelo.setText("");
    }

    private String safe(String s){ return (s==null?"-":s); }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
