package Presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Locale;


public class ConsultaVuelo extends JInternalFrame {
	private Logica.Sistema sistema;

	public ConsultaVuelo() {
		this.sistema = new Logica.Sistema();
	}

    // Dependencia a tu fachada/lógica

    // Controles
    private final JComboBox<Logica.DataAerolinea> cbAerolinea = new JComboBox<>();
    private final JComboBox<Logica.Rutas> cbRuta = new JComboBox<>();
    private final JComboBox<Logica.VueloEspecifico> cbVuelo = new JComboBox<>();

    private final JTextArea areaDatosVuelo = new JTextArea(6, 60);
    private final DefaultTableModel reservasModel = new DefaultTableModel(
            new String[]{"Pasajero", "Tipo asiento", "Equipaje extra", "Total"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tablaReservas = new JTable(reservasModel);

    public void ConsultaVuelo(Logica.Sistema sistema) {
        super("Consulta de Vuelo", true, true, true, true);
        this.sistema = sistema;

        setSize(860, 520);
        getContentPane().setLayout(new BorderLayout(8, 8));

        // ---------- TOP: selección en cascada ----------
        JPanel filtros = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4,4,4,4);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int colLabel = 0, colField = 1;

        gc.gridx = colLabel; gc.gridy = 0; filtros.add(new JLabel("Aerolínea:"), gc);
        gc.gridx = colField; gc.gridy = 0; cbAerolinea.setPrototypeDisplayValue(dummyText("Aerolínea largaaaaa"));
        filtros.add(cbAerolinea, gc);

        gc.gridx = colLabel; gc.gridy = 1; filtros.add(new JLabel("Ruta:"), gc);
        gc.gridx = colField; gc.gridy = 1; cbRuta.setPrototypeDisplayValue(dummyText("Ruta largaaaaa MVD-MAD"));
        filtros.add(cbRuta, gc);

        gc.gridx = colLabel; gc.gridy = 2; filtros.add(new JLabel("Vuelo:"), gc);
        gc.gridx = colField; gc.gridy = 2; cbVuelo.setPrototypeDisplayValue(dummyText("LAT1234 2025-10-01"));
        filtros.add(cbVuelo, gc);

        getContentPane().add(filtros, BorderLayout.NORTH);

        // ---------- CENTER: datos del vuelo + reservas ----------
        areaDatosVuelo.setEditable(false);
        areaDatosVuelo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaDatosVuelo.setLineWrap(true);
        areaDatosVuelo.setWrapStyleWord(true);

        tablaReservas.setAutoCreateRowSorter(true);
        tablaReservas.setRowHeight(22);
        JScrollPane spTabla = new JScrollPane(tablaReservas);

        JPanel centro = new JPanel(new BorderLayout(8,8));
        centro.add(new JScrollPane(areaDatosVuelo), BorderLayout.NORTH);
        centro.add(spTabla, BorderLayout.CENTER);

        getContentPane().add(centro, BorderLayout.CENTER);

        // ---------- Eventos en cascada ----------
        cbAerolinea.addActionListener(e -> cargarRutas());
        cbRuta.addActionListener(e -> cargarVuelos());
        cbVuelo.addActionListener(e -> mostrarVueloYReservas());

        // ---------- Carga inicial ----------
        cargarAerolineas();
    }

    // ================== CARGAS ==================
    private void cargarAerolineas() {
        try {
            List<Logica.DataAerolinea> aerolineas = sistema.listarAerolineas();
            cbAerolinea.setModel(new DefaultComboBoxModel<>(aerolineas.toArray(new Logica.DataAerolinea[0])));
            cbRuta.setModel(new DefaultComboBoxModel<>());
            cbVuelo.setModel(new DefaultComboBoxModel<>());
            areaDatosVuelo.setText("");
            limpiarReservas();
        } catch (Exception ex) {
            error("No se pudieron obtener las aerolíneas", ex);
        }
    }

    private void cargarRutas() {
        Logica.DataAerolinea a = (Logica.DataAerolinea) cbAerolinea.getSelectedItem();
        if (a == null) {
            cbRuta.setModel(new DefaultComboBoxModel<>());
            cbVuelo.setModel(new DefaultComboBoxModel<>());
            areaDatosVuelo.setText("");
            limpiarReservas();
            return;
        }
        try {
            List<Logica.DataRuta> rutas = sistema.listarRutasDeAerolinea(a.getNickname());
            cbRuta.setModel(new DefaultComboBoxModel<>(rutas.toArray(new Logica.DataRuta[0])));
            cbVuelo.setModel(new DefaultComboBoxModel<>());
            areaDatosVuelo.setText("");
            limpiarReservas();
        } catch (Exception ex) {
            error("No se pudieron obtener las rutas de la aerolínea " + a.getNickname(), ex);
        }
    }

    private void cargarVuelos() {
        Logica.DataRuta r = (Logica.DataRuta) cbRuta.getSelectedItem();
        if (r == null) {
            cbVuelo.setModel(new DefaultComboBoxModel<>());
            areaDatosVuelo.setText("");
            limpiarReservas();
            return;
        }
        try {
            List<Logica.DataVueloEspecifico> vuelos = sistema.listarVuelosDeRuta(r.getNombre());
            cbVuelo.setModel(new DefaultComboBoxModel<>(vuelos.toArray(new Logica.DataVueloEspecifico[0])));
            areaDatosVuelo.setText("");
            limpiarReservas();
        } catch (Exception ex) {
            error("No se pudieron obtener los vuelos de la ruta " + r.getNombre(), ex);
        }
    }

    private void mostrarVueloYReservas() {
        Logica.DataVueloEspecifico v = (Logica.DataVueloEspecifico) cbVuelo.getSelectedItem();
        if (v == null) {
            areaDatosVuelo.setText("");
            limpiarReservas();
            return;
        }
        // Datos del vuelo
        String info = String.format(Locale.US,
                "Vuelo: %s%nRuta: %s%nFecha: %s%nDuración: %d min%nAsientos (Tur/Ejec): %d / %d%nFecha alta: %s",
                nvl(v.getNombre()),
                nvl(v.getRutaNombre()),
                nvl(v.getFecha()),
                v.getDuracionMin(),
                v.getMaxAsientosTur(),
                v.getMaxAsientosEjec(),
                nvl(v.getFechaAlta())
        );
        areaDatosVuelo.setText(info);

        // Reservas asociadas
        try {
            List<Logica.DataReserva> reservas = sistema.listarReservasPorVuelo(v.getNombre());
            cargarReservasEnTabla(reservas);
        } catch (Exception ex) {
            error("No se pudieron obtener las reservas del vuelo " + v.getNombre(), ex);
        }
    }

    // ================== TABLA RESERVAS ==================
    private void limpiarReservas() {
        reservasModel.setRowCount(0);
    }

    private void cargarReservasEnTabla(List<Logica.DataReserva> reservas) {
        limpiarReservas();
        if (reservas == null || reservas.isEmpty()) return;

        for (Logica.DataReserva r : reservas) {
            String pasajero = r.getPasajeroNombre() + " " + r.getPasajeroApellido();
            reservasModel.addRow(new Object[]{
                    pasajero.trim(),
                    nvl(r.getTipoAsiento()),
                    r.getCantEquipajeExtra(),
                    String.format(Locale.US, "$ %.2f", r.getCostoTotal())
            });
        }
    }

    // ================== Utils ==================
    private static String nvl(Object o) { return o == null ? "" : o.toString(); }
    private static String dummyText(String s) { return s; }
    private void error(String msg, Exception ex) {
        JOptionPane.showMessageDialog(this, msg + ":\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}