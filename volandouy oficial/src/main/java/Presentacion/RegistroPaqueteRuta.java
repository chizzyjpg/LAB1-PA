package Presentacion;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.util.Date;

import Logica.DataPaqueteAlta;
import Logica.ISistema;

public class RegistroPaqueteRuta extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private final ISistema sistema;

    // Campos UI
    private JTextField txtNombre;
    private JTextArea  taDescripcion;
    private JSpinner   spValidez;     // días
    private JSpinner   spDescuento;   // %
    private JDateChooser dcFechaAlta;

    private JButton btnAceptar;
    private JButton btnCerrar;

    public RegistroPaqueteRuta(ISistema sistema) {
        super("Registro Paquete de Rutas de Vuelo", true, true, true, true);
        this.sistema = sistema;

        setBounds(100, 100, 520, 420);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        // Layout general: 2 columnas (label a la derecha, campo que crece)
        getContentPane().setLayout(new MigLayout(
                "insets 12, fillx, gap 10",
                "[right][grow]",
                "[][][grow][][::40][]"
        ));

        construirFormulario();
        construirBotonera();
    }

    private void construirFormulario() {
        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        getContentPane().add(lblNombre, "cell 0 0");

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        txtNombre.setColumns(25);
        getContentPane().add(txtNombre, "cell 1 0, growx");

        // Descripción
        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        getContentPane().add(lblDesc, "cell 0 1, top");

        taDescripcion = new JTextArea(5, 30);
        taDescripcion.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        taDescripcion.setLineWrap(true);
        taDescripcion.setWrapStyleWord(true);
        JScrollPane spDesc = new JScrollPane(taDescripcion,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(spDesc, "cell 1 1, growx, h 110!");

        // Validez (días)
        JLabel lblValidez = new JLabel("Validez (días):");
        lblValidez.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        getContentPane().add(lblValidez, "cell 0 2");

        spValidez = new JSpinner(new SpinnerNumberModel(30, 1, 3650, 1));
        ((JSpinner.DefaultEditor) spValidez.getEditor()).getTextField().setColumns(6);
        getContentPane().add(spValidez, "cell 1 2, w 120!");

        // Descuento (%)
        JLabel lblDescPct = new JLabel("Descuento (%):");
        lblDescPct.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        getContentPane().add(lblDescPct, "cell 0 3");

        spDescuento = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        ((JSpinner.DefaultEditor) spDescuento.getEditor()).getTextField().setColumns(6);
        getContentPane().add(spDescuento, "cell 1 3, w 120!");

        // Fecha de alta
        JLabel lblFecha = new JLabel("Fecha de alta:");
        lblFecha.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        getContentPane().add(lblFecha, "cell 0 4");

        dcFechaAlta = new JDateChooser(new Date());
        getContentPane().add(dcFechaAlta, "cell 1 4, w 180!");
    }

    private void construirBotonera() {
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        btnCerrar = new JButton("CERRAR");
        btnCerrar.setBackground(new Color(241, 43, 14));
        btnCerrar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        btnCerrar.addActionListener(e -> dispose());

        btnAceptar = new JButton("ACEPTAR");
        btnAceptar.setBackground(new Color(5, 250, 79));
        btnAceptar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        btnAceptar.addActionListener(e -> onAceptar());

        south.add(btnCerrar);
        south.add(btnAceptar);

        getContentPane().add(south, "cell 0 5, span 2, growx");
    }

    // Acción principal
    private void onAceptar() {
        try {
            String nombre = safe(txtNombre.getText());
            String desc   = safe(taDescripcion.getText());
            int validez   = (Integer) spValidez.getValue();
            int dto       = (Integer) spDescuento.getValue();
            Date fecha    = dcFechaAlta.getDate();

            // Validaciones de UI (la lógica también valida)
            if (nombre.isBlank()) { warn("El nombre es obligatorio."); txtNombre.requestFocus(); return; }
            if (desc.isBlank())   { warn("La descripción es obligatoria."); taDescripcion.requestFocus(); return; }
            if (fecha == null)    { warn("La fecha de alta es obligatoria."); dcFechaAlta.requestFocus(); return; }
            if (validez <= 0)     { warn("La validez debe ser mayor a 0."); spValidez.requestFocus(); return; }
            if (dto < 0 || dto > 100) { warn("El descuento debe estar entre 0 y 100."); spDescuento.requestFocus(); return; }

            // Unicidad inmediata
            if (sistema.existePaquete(nombre)) {
                warn("Ya existe un paquete con ese nombre.");
                txtNombre.requestFocus();
                return;
            }

            // DTO y llamada a lógica
            DataPaqueteAlta dtoAlta = new DataPaqueteAlta(nombre, desc, validez, dto, fecha);
            sistema.registrarPaquete(dtoAlta);

            JOptionPane.showMessageDialog(this, "Paquete creado correctamente.", "OK", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (IllegalArgumentException ex) {
            warn(ex.getMessage());
        } catch (Exception ex) {
            error("Error creando el paquete: " + ex.getMessage());
        }
    }

    // Helpers
    private String safe(String s) { return (s == null) ? "" : s.trim(); }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
    }
    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
