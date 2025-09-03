package Presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.toedter.calendar.JDateChooser;

import Logica.ISistema;
import Logica.DataAerolinea;
import Logica.DataRuta;
import Logica.DataVueloEspecifico;
import Logica.DataCliente;
import Logica.DataPasaje;
import Logica.DataReserva;
import Logica.TipoAsiento;
import Logica.Equipaje;
import java.awt.Font;
import java.awt.Color;

@SuppressWarnings("serial")
public class RegistrarReservaVuelo extends JInternalFrame {

    // --------- Dependencia ---------
    private final ISistema sistema;

    // --------- Controles ---------
    private final JComboBox<DataAerolinea> comboAerolinea = new JComboBox<>();
    private final JComboBox<DataRuta> comboRuta = new JComboBox<>();
    private final JComboBox<DataVueloEspecifico> comboVuelo = new JComboBox<>();

    private final JComboBox<DataCliente> comboCliente = new JComboBox<>(); // DataCliente (DTO)
    private final JDateChooser dateChooser = new JDateChooser();

    private final JComboBox<TipoAsiento> comboTipoAsiento =
            new JComboBox<>(TipoAsiento.values()); // usar enum directo

    private final JComboBox<Equipaje> comboEquipaje =
            new JComboBox<>(Equipaje.values());    // usar enum directo

    private final JSpinner spinnerPasajes = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private final JSpinner spinnerEquipajeExtra = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

    private final JButton btnAceptar = new JButton("Registrar");
    private final JButton btnCancelar = new JButton("Cancelar");

    // --------- Ctor ---------
    public RegistrarReservaVuelo(ISistema sistema) {
        super("Registrar Reserva de Vuelo", true, true, true, true);
        this.sistema = sistema;

        setSize(740, 490);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        armarUI();
        wireEvents();

        cargarAerolineas();
        cargarClientes();
    }

    // --------- UI ---------
    private void armarUI() {
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Selección de vuelo
        JLabel label = new JLabel("Aerolínea:");
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label);
        comboAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboAerolinea);

        JLabel label_1 = new JLabel("Ruta:");
        label_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_1);
        comboRuta.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboRuta);

        JLabel label_2 = new JLabel("Vuelo:");
        label_2.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_2);
        comboVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboVuelo);

        // Datos de la reserva
        JLabel label_3 = new JLabel("Cliente:");
        label_3.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_3);
        comboCliente.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboCliente);

        JLabel label_4 = new JLabel("Fecha de reserva:");
        label_4.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_4);
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");
        form.add(dateChooser);

        JLabel label_5 = new JLabel("Tipo de asiento:");
        label_5.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_5);
        comboTipoAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboTipoAsiento);

        JLabel label_6 = new JLabel("Tipo de equipaje:");
        label_6.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_6);
        comboEquipaje.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboEquipaje);

        JLabel label_7 = new JLabel("Cantidad de pasajes:");
        label_7.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_7);
        spinnerPasajes.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(spinnerPasajes);

        JLabel label_8 = new JLabel("Equipaje extra (unid.):");
        label_8.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_8);
        spinnerEquipajeExtra.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(spinnerEquipajeExtra);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        south.add(btnCancelar);
        btnAceptar.setBackground(Color.GREEN);
        btnAceptar.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        south.add(btnAceptar);

        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);
    }

    // --------- Eventos ---------
    private void wireEvents() {
        comboAerolinea.addActionListener(e -> cargarRutas());
        comboRuta.addActionListener(e -> cargarVuelos());

        btnCancelar.addActionListener(e -> dispose());
        btnAceptar.addActionListener(e -> registrar());
    }

    // --------- Carga de datos ---------
    private void cargarAerolineas() {
        comboAerolinea.removeAllItems();
        for (DataAerolinea a : sistema.listarAerolineas()) {
            comboAerolinea.addItem(a);
        }
        cargarRutas();
    }

    private void cargarRutas() {
        comboRuta.removeAllItems();
        DataAerolinea a = (DataAerolinea) comboAerolinea.getSelectedItem();
        if (a == null) return;
        for (DataRuta r : sistema.listarPorAerolinea(a.getNickname())) {
            comboRuta.addItem(r);
        }
        cargarVuelos();
    }

    private void cargarVuelos() {
        comboVuelo.removeAllItems();
        DataAerolinea a = (DataAerolinea) comboAerolinea.getSelectedItem();
        DataRuta r = (DataRuta) comboRuta.getSelectedItem();
        if (a == null || r == null) return;
        for (DataVueloEspecifico v : sistema.listarVuelos(a.getNickname(), r.getNombre())) {
            comboVuelo.addItem(v);
        }
    }

    private void cargarClientes() {
        comboCliente.removeAllItems();
        for (DataCliente dc : sistema.listarClientes()) {
            comboCliente.addItem(dc);
        }
    }

    // --------- Registrar ---------
    private void registrar() {
    	DataAerolinea a = (DataAerolinea) comboAerolinea.getSelectedItem();
        DataRuta r = (DataRuta) comboRuta.getSelectedItem();
        DataVueloEspecifico v = (DataVueloEspecifico) comboVuelo.getSelectedItem();
        DataCliente dc = (DataCliente) comboCliente.getSelectedItem();
        Date fecha = dateChooser.getDate();

        if (a == null || r == null || v == null || dc == null || fecha == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná Aerolínea, Ruta, Vuelo, Cliente y Fecha.",
                    "Faltan datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoAsiento tipoAsiento = (TipoAsiento) comboTipoAsiento.getSelectedItem();
        Equipaje equipaje = (Equipaje) comboEquipaje.getSelectedItem();
        int cantPasajes = (int) spinnerPasajes.getValue();
        int equipajeExtra = (int) spinnerEquipajeExtra.getValue();

        if (cantPasajes < 1) {
            JOptionPane.showMessageDialog(this, "La cantidad de pasajes debe ser al menos 1.",
                    "Dato inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 1) armamos la reserva "base"
            DataReserva datos = new DataReserva(
                0,             // idReserva (lo asigna backend)
                fecha,
                tipoAsiento,
                equipaje,
                equipajeExtra,
                0f,            // costo total (que lo calcule backend)
                dc
            );
            
            String nomCliente = (dc != null) ? /* dc.getNombre() */ "" : "";
            String apeCliente = (dc != null) ? /* dc.getApellido() */ "" : "";
            
            // 2) pedimos los pasajeros
            //java.util.List<DataPasaje> pasajeros = java.util.Collections.emptyList();
            
            
         // 2) pedir pasajeros
            //boolean exigirPrimeroIgualCliente = true; // <- ponelo en false si tu backend NO lo exige
            
            PasajerosDialog dlg = new PasajerosDialog(
                    SwingUtilities.getWindowAncestor(this),
                    cantPasajes,
                    dc // DataCliente: se usa para prellenar la fila 0, pero es editable
            );
            dlg.setVisible(true);
            if (!dlg.isAccepted()) return;

            java.util.List<DataPasaje> pasajeros = dlg.getPasajeros();
            if (pasajeros.size() != cantPasajes) {
                JOptionPane.showMessageDialog(this,
                        "Se esperaban " + cantPasajes + " pasajeros pero se ingresaron " + pasajeros.size(),
                        "Cantidad inconsistente", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3) guardar la lista dentro del DTO
            datos.setPasajes(pasajeros);

            // registrar con TU método existente
            sistema.registrarReserva(
                    a.getNickname(),
                    r.getNombre(),
                    v.getNombre(),   // si usás código distinto, cambialo
                    datos
            );

            JOptionPane.showMessageDialog(this, "Reserva registrada con éxito");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar la reserva: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
 // =================== Diálogo para ingresar pasajeros (Nombre/Apellido) ===================
    private static final class PasajerosDialog extends JDialog {
        private final JTable table;
        private boolean accepted = false;

        PasajerosDialog(Window owner, int cantidad, Logica.DataCliente cliente) {
            super(owner, "Pasajeros (" + cantidad + ")", ModalityType.APPLICATION_MODAL);
            setSize(520, 300);
            setLocationRelativeTo(owner);

            String[] cols = {"Nombre", "Apellido"};
            Object[][] data = new Object[cantidad][cols.length];

            // prefill fila 0 con el cliente (editable)
            String nomCliente = (cliente != null && cliente.getNombre() != null)   ? cliente.getNombre().trim()   : "";
            String apeCliente = (cliente != null && cliente.getApellido() != null) ? cliente.getApellido().trim() : "";
            if (cantidad > 0) {
                data[0][0] = nomCliente;
                data[0][1] = apeCliente;
            }

            javax.swing.table.DefaultTableModel model =
                    new javax.swing.table.DefaultTableModel(data, cols) {
                        @Override public boolean isCellEditable(int r, int c) { return r != 0; }
                    };

            table = new JTable(model);
            // commit automático si se pierde el foco
            table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
            getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnCancel = new JButton("Cancelar");
            JButton btnOk     = new JButton("Aceptar");
            south.add(btnCancel); south.add(btnOk);
            getContentPane().add(south, BorderLayout.SOUTH);

            btnCancel.addActionListener(e -> { accepted = false; dispose(); });

            btnOk.addActionListener(e -> {
                // 1) Forzar commit de la celda en edición
                if (table.isEditing()) {
                    javax.swing.table.TableCellEditor ed = table.getCellEditor();
                    if (ed != null && !ed.stopCellEditing()) return; // si el editor no puede cerrar, aborta
                }

                // 2) Validación: nombre+apellido en todas las filas
                for (int i = 0; i < model.getRowCount(); i++) {
                    String nom = str(model.getValueAt(i, 0));
                    String ape = str(model.getValueAt(i, 1));
                    if (nom.isEmpty() || ape.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Completá nombre y apellido en la fila " + (i + 1),
                                "Faltan datos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                accepted = true;
                dispose();
            });
        }

        boolean isAccepted() { return accepted; }

        java.util.List<Logica.DataPasaje> getPasajeros() {
            java.util.List<Logica.DataPasaje> out = new java.util.ArrayList<>();
            javax.swing.table.TableModel m = table.getModel();
            for (int i = 0; i < m.getRowCount(); i++) {
                String nom = str(m.getValueAt(i, 0));
                String ape = str(m.getValueAt(i, 1));
                out.add(new Logica.DataPasaje(nom, ape));
            }
            return out;
        }

        private static String str(Object o){ return (o == null) ? "" : o.toString().trim(); }
    }
}