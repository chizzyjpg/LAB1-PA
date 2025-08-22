package Presentacion;

import Logica.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ModificarUsuario extends JInternalFrame {

    private final ISistema sistema;

    // top
    private JComboBox<DataUsuario> comboUsuarios;
    private JButton btnGuardar, btnCerrar;

    // comunes
    private JTextField txtNick, txtEmail, txtNombre;

    // cliente
    private JTextField txtApellido, txtNacionalidad, txtNumDoc;
    private JComboBox<TipoDocumento> cmbTipoDoc;
    private JDateChooser dcFechaNac;

    // aerolínea
    private JTextArea areaDesc;
    private JTextField txtSitio;

    // cards
    private JPanel cards; // "CLIENTE" / "AEROLINEA"
    private static final String CARD_CLIENTE = "CLIENTE";
    private static final String CARD_AERO = "AEROLINEA";

    public ModificarUsuario(ISistema sistema) {
        super("Modificar Usuario", true, true, true, true);
        this.sistema = sistema;
        setSize(650, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        cargarUsuarios();
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));

        // ——— TOP: combo + botones ———
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Usuario:"));

        comboUsuarios = new JComboBox<>();
        comboUsuarios.setPreferredSize(new Dimension(300, 28));
        comboUsuarios.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataUsuario du) {
                    String tipo = (du instanceof DataCliente) ? "Cliente" :
                                  (du instanceof DataAerolinea) ? "Aerolínea" : "Usuario";
                    setText("%s: %s — %s".formatted(tipo, nvl(du.getNickname()), nvl(du.getNombre())));
                    setToolTipText(du.getEmail());
                }
                return this;
            }
        });
        comboUsuarios.addActionListener(e -> onSeleccionarUsuario());
        top.add(comboUsuarios);

        btnGuardar = new JButton("Guardar cambios");
        btnCerrar  = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        top.add(btnGuardar);
        top.add(btnCerrar);

        add(top, BorderLayout.NORTH);

        // ——— CENTER: formulario ———

        // comunes
        txtNick   = new JTextField();
        txtEmail  = new JTextField();
        txtNombre = new JTextField();

        JPanel formCommon = new JPanel(new GridLayout(0, 2, 6, 8));
        formCommon.add(new JLabel("Nickname:")); formCommon.add(txtNick);
        formCommon.add(new JLabel("Email:"));    formCommon.add(txtEmail);
        formCommon.add(new JLabel("Nombre:"));   formCommon.add(txtNombre);

        txtNick.setEnabled(false);   // no editable
        txtEmail.setEnabled(false);  // no editable

        // cliente
        txtApellido     = new JTextField();
        dcFechaNac      = new com.toedter.calendar.JDateChooser();
        txtNacionalidad = new JTextField();
        cmbTipoDoc      = new JComboBox<>(TipoDocumento.values());
        txtNumDoc       = new JTextField();

        JPanel pnlCliente = new JPanel(new GridLayout(0, 2, 6, 8));
        pnlCliente.add(new JLabel("Apellido:"));              pnlCliente.add(txtApellido);
        pnlCliente.add(new JLabel("Fecha de nacimiento:"));   pnlCliente.add(dcFechaNac);
        pnlCliente.add(new JLabel("País de nacimiento:"));    pnlCliente.add(txtNacionalidad);
        pnlCliente.add(new JLabel("Tipo de documento:"));     pnlCliente.add(cmbTipoDoc);
        pnlCliente.add(new JLabel("Número de documento:"));   pnlCliente.add(txtNumDoc);

        // aerolínea
        areaDesc = new JTextArea(5, 30);
        txtSitio = new JTextField();

        JPanel pnlAero = new JPanel(new GridBagLayout());
        Insets PAD = new Insets(4,4,4,4);
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx=0; c.gridy=0; c.insets=PAD; c.anchor=GridBagConstraints.EAST;
        pnlAero.add(new JLabel("Descripción:"), c);

        c = new GridBagConstraints();
        c.gridx=1; c.gridy=0; c.insets=PAD; c.fill=GridBagConstraints.BOTH; c.weightx=1; c.weighty=1;
        pnlAero.add(new JScrollPane(areaDesc), c);

        c = new GridBagConstraints();
        c.gridx=0; c.gridy=1; c.insets=PAD; c.anchor=GridBagConstraints.EAST;
        pnlAero.add(new JLabel("Sitio web:"), c);

        c = new GridBagConstraints();
        c.gridx=1; c.gridy=1; c.insets=PAD; c.fill=GridBagConstraints.HORIZONTAL; c.weightx=1;
        pnlAero.add(txtSitio, c);

        // — cards (CAMPO, NO LOCAL)
        cards = new JPanel(new CardLayout(0, 0));
        cards.add(pnlCliente, CARD_CLIENTE);
        cards.add(pnlAero,    CARD_AERO);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.add(formCommon, BorderLayout.NORTH);
        center.add(cards,      BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        // acción guardar
        btnGuardar.addActionListener(e -> guardar());
    }

    private void cargarUsuarios() {
        try {
            List<DataUsuario> lista = sistema.listarUsuarios();
            lista.sort(Comparator.comparing(u -> nvl(u.getNickname()), String.CASE_INSENSITIVE_ORDER));
            comboUsuarios.setModel(new DefaultComboBoxModel<>(lista.toArray(new DataUsuario[0])));
            if (!lista.isEmpty()) {
                comboUsuarios.setSelectedIndex(0);
                onSeleccionarUsuario(); // carga el primero
            } else {
                bloquearTodo(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo listar usuarios:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            bloquearTodo(true);
        }
    }

    private void onSeleccionarUsuario() {
        DataUsuario du = (DataUsuario) comboUsuarios.getSelectedItem();
        if (du == null) { limpiar(); return; }

        // refrescar desde sistema por nickname
        String nick = du.getNickname();
        DataCliente dc = sistema.verInfoCliente(nick);
        if (dc != null) {
            mostrarCard(CARD_CLIENTE);
            // comunes
            txtNick.setText(dc.getNickname());
            txtEmail.setText(dc.getEmail());
            txtNombre.setText(nvl(dc.getNombre()));
            // específicos
            txtApellido.setText(nvl(dc.getApellido()));
            dcFechaNac.setDate(dc.getFechaNac());
            txtNacionalidad.setText(nvl(dc.getNacionalidad()));
            cmbTipoDoc.setSelectedItem(dc.getTipoDocumento());
            txtNumDoc.setText(nvl(dc.getNumDocumento()));
            return;
        }

        DataAerolinea da = sistema.verInfoAerolinea(nick);
        if (da != null) {
            mostrarCard(CARD_AERO);
            // comunes
            txtNick.setText(da.getNickname());
            txtEmail.setText(da.getEmail());
            txtNombre.setText(nvl(da.getNombre()));
            // específicos
            areaDesc.setText(nvl(da.getDescripcion()));
            txtSitio.setText(nvl(da.getSitioWeb()));
            return;
        }

        // si no existe
        limpiar();
        JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void guardar() {
        DataUsuario du = (DataUsuario) comboUsuarios.getSelectedItem();
        if (du == null) return;
        String nick = du.getNickname();

        // ¿cliente?
        DataCliente dcActual = sistema.verInfoCliente(nick);
        if (dcActual != null) {
            try {
                DataCliente dto = new DataCliente(
                        txtNombre.getText().trim(),
                        dcActual.getNickname(),        // nickname NO cambia
                        dcActual.getEmail(),           // email NO cambia
                        txtApellido.getText().trim(),
                        dcFechaNac.getDate(),
                        txtNacionalidad.getText().trim(),
                        (TipoDocumento) cmbTipoDoc.getSelectedItem(),
                        txtNumDoc.getText().trim()
                );
                sistema.modificarCliente(nick, dto);
                JOptionPane.showMessageDialog(this, "Cliente modificado con éxito.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        // ¿aerolínea?
        DataAerolinea daActual = sistema.verInfoAerolinea(nick);
        if (daActual != null) {
            try {
                DataAerolinea dto = new DataAerolinea(
                        txtNombre.getText().trim(),
                        daActual.getNickname(),        // nickname NO cambia
                        daActual.getEmail(),           // email NO cambia
                        areaDesc.getText().trim(),
                        txtSitio.getText().trim()
                );
                sistema.modificarAerolinea(nick, dto);
                JOptionPane.showMessageDialog(this, "Aerolínea modificada con éxito.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarCard(String card) {
        if (cards == null) return;  // o lanza IllegalStateException con un mensaje claro
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, card);
    }

    private void bloquearTodo(boolean bloquear) {
        comboUsuarios.setEnabled(!bloquear);
        btnGuardar.setEnabled(!bloquear);
    }

    private void limpiar() {
        txtNick.setText("");
        txtEmail.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        dcFechaNac.setDate(null);
        txtNacionalidad.setText("");
        cmbTipoDoc.setSelectedIndex(0);
        txtNumDoc.setText("");
        areaDesc.setText("");
        txtSitio.setText("");
    }

    private static String nvl(String s){ return s == null ? "" : s; }
}
