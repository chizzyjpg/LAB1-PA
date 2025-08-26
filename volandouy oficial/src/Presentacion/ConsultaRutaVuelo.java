package Presentacion;

import Logica.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

import Logica.ISistema;

public class ConsultaRutaVuelo extends JInternalFrame {

    private final JComboBox<DataAerolinea> comboAerolineas = new JComboBox<>();
    private final JList<DataRuta> listRutas = new JList<>(new DefaultListModel<>());
    private final JList<DataVueloEspecifico> listVuelos = new JList<>(new DefaultListModel<>());
    private final JTextArea txtDetallesRuta = new JTextArea(12, 40);
    private final JButton btnVerVuelo = new JButton("Ver vuelo…");
    private final ISistema sistema; // referencia al sistema

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

    public ConsultaRutaVuelo(ISistema sistema) {
        super("Consulta de Ruta de Vuelo", true, true, true, true);
    	this.sistema = sistema;
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(root);

        // TOP: aerolíneas
        JPanel top = new JPanel(new BorderLayout(8,8));
        top.add(new JLabel("Aerolínea:"), BorderLayout.WEST);
        comboAerolineas.setRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataAerolinea a) setText(a.getNickname() + " — " + a.getNombre());
                return this;
            }
        });
        top.add(comboAerolineas, BorderLayout.CENTER);
        root.add(top, BorderLayout.NORTH);

        // CENTER: rutas + detalles
        JSplitPane center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        center.setResizeWeight(0.33);

        // panel de rutas
        JPanel left = new JPanel(new BorderLayout(6,6));
        left.add(new JLabel("Rutas de la aerolínea"), BorderLayout.NORTH);
        listRutas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listRutas.setCellRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataRuta r) setText(r.getNombre());
                return this;
            }
        });
        left.add(new JScrollPane(listRutas), BorderLayout.CENTER);
        center.setLeftComponent(left);

        // panel detalles + vuelos
        JPanel right = new JPanel(new BorderLayout(8,8));
        JPanel detailsPanel = new JPanel(new BorderLayout(6,6));
        detailsPanel.add(new JLabel("Detalles de la ruta"), BorderLayout.NORTH);
        txtDetallesRuta.setEditable(false);
        txtDetallesRuta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        detailsPanel.add(new JScrollPane(txtDetallesRuta), BorderLayout.CENTER);
        right.add(detailsPanel, BorderLayout.CENTER);

        JPanel flightsPanel = new JPanel(new BorderLayout(6,6));
        flightsPanel.add(new JLabel("Vuelos asociados"), BorderLayout.NORTH);
        listVuelos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listVuelos.setCellRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataVueloEspecifico v) {
                    String f = v.getFecha() != null ? sdf.format(v.getFecha()) : "-";
                    setText(v.getNombre() + "  (" + f + ")");
                }
                return this;
            }
        });
        flightsPanel.add(new JScrollPane(listVuelos), BorderLayout.CENTER);

        JPanel flightsButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        flightsButtons.add(btnVerVuelo);
        flightsPanel.add(flightsButtons, BorderLayout.SOUTH);

        right.add(flightsPanel, BorderLayout.SOUTH);
        center.setRightComponent(right);

        root.add(center, BorderLayout.CENTER);

        // Listeners
        comboAerolineas.addActionListener(e -> cargarRutasDeSeleccion());
        listRutas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarDetalleRutaYVuelos(listRutas.getSelectedValue());
        });
        btnVerVuelo.addActionListener(e -> verVueloSeleccionado());

        // Cargar aerolíneas al iniciar
        cargarAerolineas();
    }

    // ==== DATA FLOW ====

    private void cargarAerolineas() {
        comboAerolineas.removeAllItems();
        try {
            List<DataAerolinea> aas = sistema.listarAerolineas(); // <-- ajustá si tu método se llama distinto
            for (DataAerolinea a : aas) comboAerolineas.addItem(a);
            if (!aas.isEmpty()) comboAerolineas.setSelectedIndex(0);
        } catch (Exception ex) {
            mostrarError("Error cargando aerolíneas: " + ex.getMessage());
        }
    }

    private void cargarRutasDeSeleccion() {
        DataAerolinea sel = (DataAerolinea) comboAerolineas.getSelectedItem();
        DefaultListModel<DataRuta> model = (DefaultListModel<DataRuta>) listRutas.getModel();
        model.clear();
        ((DefaultListModel<DataVueloEspecifico>) listVuelos.getModel()).clear();
        txtDetallesRuta.setText("");

        if (sel == null) return;
        try {
            List<DataRuta> rutas = sistema.listarPorAerolinea(sel.getNickname()); // <-- implementa esto en tu manejador
            for (DataRuta r : rutas) model.addElement(r);
            if (!rutas.isEmpty()) listRutas.setSelectedIndex(0);
        } catch (Exception ex) {
            mostrarError("Error cargando rutas: " + ex.getMessage());
        }
    }

    private void mostrarDetalleRutaYVuelos(DataRuta r) {
        if (r == null) {
            txtDetallesRuta.setText("");
            ((DefaultListModel<DataVueloEspecifico>) listVuelos.getModel()).clear();
            return;
        }
        txtDetallesRuta.setText(formatRuta(r));
        cargarVuelosDeRuta(r);
    }

    private void cargarVuelosDeRuta(DataRuta r) {
        DefaultListModel<DataVueloEspecifico> m = (DefaultListModel<DataVueloEspecifico>) listVuelos.getModel();
        m.clear();
        try {
            List<DataVueloEspecifico> vuelos = ManejadorVuelo.get().listarPorRuta(r.getNombre()); // <-- implementa en manejador
            for (DataVueloEspecifico v : vuelos) m.addElement(v);
        } catch (Exception ex) {
            mostrarError("Error cargando vuelos: " + ex.getMessage());
        }
    }

    private void verVueloSeleccionado() {
    	DataVueloEspecifico v = listVuelos.getSelectedValue();
        if (v == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná un vuelo", "Atención", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // TODO: abrir tu "Consulta de Vuelo", pasando el identificador del vuelo
        // new ConsultaVueloInternalFrame(v.getNombre()).setVisible(true);
        JOptionPane.showMessageDialog(this, "Abrir detalle de vuelo: " + v.getNombre());
    }

    // ==== HELPERS ====

    private String formatRuta(DataRuta r) {
        String o = r.getCiudadOrigen()  != null ? r.getCiudadOrigen().getNombre()  + ", " + r.getCiudadOrigen().getPais()  : "-";
        String d = r.getCiudadDestino() != null ? r.getCiudadDestino().getNombre() + ", " + r.getCiudadDestino().getPais() : "-";
        String f = r.getFechaAlta() != null ? sdf.format(r.getFechaAlta()) : "-";
        String h = String.format("%02d:00", r.getHora()); // si guardás HH solamente

        return """
               Nombre: %s
               Descripción: %s
               Origen: %s
               Destino: %s
               Hora: %s
               Fecha alta: %s
               Costo base: %d
               Costo equipaje extra: %d
               """.formatted(
                r.getNombre(),
                nullToDash(r.getDescripcion()),
                o, d, h, f,
                r.getCostoBase(),
                r.getCostoEquipajeExtra()
        );
    }

    private String nullToDash(String s){ return (s==null || s.isBlank()) ? "-" : s; }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
