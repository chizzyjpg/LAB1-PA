package Presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.toedter.calendar.JDateChooser;

import Logica.ISistema;
import Logica.DataAerolinea;
import Logica.DataRuta;
import Logica.DataVueloEspecifico;
import Logica.DataCliente;
import Logica.DataReserva;
import Logica.TipoAsiento;
import Logica.Equipaje;

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
        form.add(new JLabel("Aerolínea:"));
        form.add(comboAerolinea);

        form.add(new JLabel("Ruta:"));
        form.add(comboRuta);

        form.add(new JLabel("Vuelo:"));
        form.add(comboVuelo);

        // Datos de la reserva
        form.add(new JLabel("Cliente:"));
        form.add(comboCliente);

        form.add(new JLabel("Fecha de reserva:"));
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");
        form.add(dateChooser);

        form.add(new JLabel("Tipo de asiento:"));
        form.add(comboTipoAsiento);

        form.add(new JLabel("Tipo de equipaje:"));
        form.add(comboEquipaje);

        form.add(new JLabel("Cantidad de pasajes:"));
        form.add(spinnerPasajes);

        form.add(new JLabel("Equipaje extra (unid.):"));
        form.add(spinnerEquipajeExtra);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnCancelar);
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
            JOptionPane.showMessageDialog(this,
                    "La cantidad de pasajes debe ser al menos 1.",
                    "Dato inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Construcción exacta usando DataCliente (no Cliente)
            DataReserva datos = new DataReserva(
                0,             // idReserva (lo asigna backend/persistencia)
                fecha,         // fecha
                tipoAsiento,   // enum TipoAsiento
                equipaje,      // enum Equipaje
                equipajeExtra, // cantidad equipaje extra
                0f,            // costo total (dejalo en 0; que lo calcule backend)
                dc             // >>> DataCliente directamente <<<
            );

            // OJO: usá el *código* del vuelo si tu interfaz lo exige
            sistema.registrarReserva(
                a.getNickname(),
                r.getNombre(),
                v.getNombre(),
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

}