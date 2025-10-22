// src/main/java/presentacion/AceptarRechazarRuta.java
package Presentacion;

import Logica.DataAerolinea;
import Logica.EstadoRuta;
import Logica.ISistema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AceptarRechazarRuta extends JInternalFrame {

    private final ISistema sistema;

    // UI
    private final JComboBox<String> cboAerolinea = new JComboBox<>();
    private final JButton btnListar = new JButton("Listar rutas ingresadas");

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Origen", "Destino"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) { return (c == 0) ? Integer.class : String.class; }
    };
    private final JTable tbl = new JTable(model);

    private final JButton btnConfirmar = new JButton("Confirmar");
    private final JButton btnRechazar  = new JButton("Rechazar");
    private final JLabel  lblEstado    = new JLabel("Seleccione una aerolínea y presione “Listar rutas ingresadas”.", JLabel.LEFT);

    public AceptarRechazarRuta(ISistema sistema) {
        super("Aceptar / Rechazar Ruta de Vuelo", true, true, true, true);
        this.sistema = sistema;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(760, 480));

        JPanel content = buildUI();
        applyComicSans(content);
        setContentPane(content);

        cargarAerolineasDesdeSistema();
        hookActions();
        pack();
    }

    // =========================
    //         UI
    // =========================
    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        // TOP: Aerolínea + Listar
        JPanel top = new JPanel(new GridBagLayout());

        GridBagConstraints gcLabel = new GridBagConstraints();
        gcLabel.insets = new Insets(4,4,4,4);
        gcLabel.gridx = 0; gcLabel.gridy = 0;
        gcLabel.anchor = GridBagConstraints.WEST;
        top.add(new JLabel("Aerolínea:"), gcLabel);

        GridBagConstraints gcCombo = new GridBagConstraints();
        gcCombo.insets = new Insets(4,4,4,4);
        gcCombo.gridx = 1; gcCombo.gridy = 0;
        gcCombo.weightx = 1.0;
        gcCombo.fill = GridBagConstraints.HORIZONTAL;
        cboAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        top.add(cboAerolinea, gcCombo);

        GridBagConstraints gcBtn = new GridBagConstraints();
        gcBtn.insets = new Insets(4,4,4,4);
        gcBtn.gridx = 2; gcBtn.gridy = 0;
        gcBtn.anchor = GridBagConstraints.EAST;
        top.add(btnListar, gcBtn);

        root.add(top, BorderLayout.NORTH);

        // CENTER: Tabla
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setRowHeight(24);
        JScrollPane sp = new JScrollPane(tbl);
        root.add(sp, BorderLayout.CENTER);

        // BOTTOM: Estado + Acciones
        JPanel bottom = new JPanel(new BorderLayout());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));

        btnConfirmar.setEnabled(false);
        btnRechazar.setEnabled(false);

        actions.add(btnRechazar);
        actions.add(btnConfirmar);

        bottom.add(lblEstado, BorderLayout.WEST);
        bottom.add(actions, BorderLayout.EAST);

        root.add(bottom, BorderLayout.SOUTH);

        return root;
    }

    private void hookActions() {
        // Selección de fila -> habilitar acciones
        tbl.getSelectionModel().addListSelectionListener(e -> {
            boolean sel = tbl.getSelectedRow() >= 0;
            btnConfirmar.setEnabled(sel);
            btnRechazar.setEnabled(sel);
        });

        // Listar rutas ingresadas
        btnListar.addActionListener(this::onListar);

        // Confirmar y Rechazar
        btnConfirmar.addActionListener(e -> onCambiarEstado(EstadoRuta.CONFIRMADA));
        btnRechazar.addActionListener(e -> onCambiarEstado(EstadoRuta.RECHAZADA));
    }

    // =========================
    //     Acciones (Swing)
    // =========================
    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void onListar(ActionEvent e) {
        String aero = (String) cboAerolinea.getSelectedItem();
        if (aero == null || aero.isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleccione una aerolínea.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
        	// Trae TODAS las rutas de esa aerolínea
            List<DataRuta> rutas = sistema.listarPorAerolinea(aero);

            model.setRowCount(0);
            int count = 0;
            if (rutas != null) {
                for (DataRuta r : rutas) {
                    // Mostrar solo las INGRESADAS (si viene null por datos viejos, las tratamos como INGRESADAS)
                    EstadoRuta est = r.getEstado();
                    if (est == null || est == EstadoRuta.INGRESADA) {
                        String origen = (r.getCiudadOrigen() != null)
                                ? r.getCiudadOrigen().getNombre() + " - " + r.getCiudadOrigen().getPais() : "—";
                        String destino = (r.getCiudadDestino() != null)
                                ? r.getCiudadDestino().getNombre() + " - " + r.getCiudadDestino().getPais() : "—";
                        model.addRow(new Object[]{ r.getIdRuta(), r.getNombre(), origen, destino });
                        count++;
                    }
                }
            }
            
            lblEstado.setText(count == 0
                    ? "No hay rutas en estado “Ingresada” para la aerolínea seleccionada."
                    : "Seleccione una ruta y confirme o rechace.");
            tbl.clearSelection();
            btnConfirmar.setEnabled(false);
            btnRechazar.setEnabled(false);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al listar rutas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCambiarEstado(EstadoRuta nuevo) {
        int row = tbl.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una ruta.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) model.getValueAt(row, 0);
        String nombre = String.valueOf(model.getValueAt(row, 1));

        String verbo = (nuevo == EstadoRuta.CONFIRMADA) ? "confirmar" : "rechazar";
        Object[] opciones = {"Aceptar", "Cancelar"};
        int ok = JOptionPane.showOptionDialog(
        	    this,
        	    "¿Desea " + verbo + " la ruta con ID " + id + " y nombre \"" + nombre + "\"?",
        	    "Confirmación",
        	    JOptionPane.OK_CANCEL_OPTION,
        	    JOptionPane.QUESTION_MESSAGE,
        	    null,
        	    opciones,
        	    opciones[0]
        	);
        	if (ok != JOptionPane.OK_OPTION) return;

        try {
            // Llamada solicitada al sistema
            sistema.cambiarEstadoRuta(id, nuevo);

            // Mensaje requerido
            String hecho = (nuevo == EstadoRuta.CONFIRMADA) ? "confirmado" : "rechazado";
            JOptionPane.showMessageDialog(this,
                    "Se ha " + hecho + " la ruta de vuelo con ID " + id + " y nombre \"" + nombre + "\".");

            // Como esta vista muestra "Ingresadas", quitamos la fila (ya no debería permanecer en la lista)
            model.removeRow(row);
            actualizarLblEstado();
            btnConfirmar.setEnabled(false);
            btnRechazar.setEnabled(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================
    //   Helpers/estética
    // =========================
    private void cargarAerolineasDesdeSistema() {
        try {
            cboAerolinea.removeAllItems();
            List<DataAerolinea> aeros = sistema.listarAerolineas();
            if (aeros != null) {
                for (DataAerolinea a : aeros) {
                    // Se muestra el nickname; si preferís nombre visible, cambiá aquí
                    cboAerolinea.addItem(a.getNickname());
                }
            }
            if (cboAerolinea.getItemCount() > 0) cboAerolinea.setSelectedIndex(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar las aerolíneas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarLblEstado() {
        boolean vacia = model.getRowCount() == 0;
        lblEstado.setText(vacia
                ? "No hay rutas en estado “Ingresada” para la aerolínea seleccionada."
                : "Seleccione una ruta y confirme o rechace.");
    }

    private void applyComicSans(Component root) {
        Font f = new Font("Comic Sans MS", Font.PLAIN, 14);
        applyFontRec(root, f);
        if (tbl.getTableHeader() != null) {
            tbl.getTableHeader().setFont(f.deriveFont(Font.PLAIN));
        }
        setFont(f);
    }

    private static void applyFontRec(Component c, Font f) {
        c.setFont(f);
        if (c instanceof Container cont) {
            for (Component child : cont.getComponents()) {
                applyFontRec(child, f);
            }
        }
    }
}