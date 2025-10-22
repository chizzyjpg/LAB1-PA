package Presentacion;

import Logica.DataAerolinea;
import Logica.DataCliente;
import Logica.DataUsuario;
import Logica.DataUsuarioAux;
import Logica.ISistema;
import Logica.TipoDocumento;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Ventana de modificación de usuarios (clientes y aerolíneas).
 */
public class ModificarUsuario extends JInternalFrame {

  private final ISistema sistema;

  // top
  private JComboBox<DataUsuarioAux> comboUsuarios;
  private JButton btnGuardar;
  private JButton btnCerrar;

  // comunes
  private JTextField txtNick;
  private JTextField txtEmail;
  private JTextField txtNombre;

  // cliente
  private JTextField txtApellido;
  private JTextField txtNacionalidad;
  private JTextField txtNumDoc;
  private JComboBox<TipoDocumento> cmbTipoDoc;
  private JDateChooser dcFechaNac;

  // aerolínea
  private JTextArea areaDesc;
  private JTextField txtSitio;

  // cards
  private JPanel cards; // "CLIENTE" / "AEROLINEA"
  private static final String CARD_CLIENTE = "CLIENTE";
  private static final String CARD_AERO = "AEROLINEA";

  /**
   * Constructor.
   *
   */
  public ModificarUsuario(ISistema sistema) {
    super("Modificar Usuario", true, true, true, true);
    this.sistema = sistema;
    setSize(650, 520);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    initUI();
    cargarUsuarios();
  }

  private void initUI() {
    getContentPane().setLayout(new BorderLayout(8, 8));

    // ——— TOP: combo + botones ———
    JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel label = new JLabel("Usuario:");
    label.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    top.add(label);

    comboUsuarios = new JComboBox<>();
    comboUsuarios.setPreferredSize(new Dimension(300, 28));
    for (DataUsuario da : sistema.listarUsuarios()) {
      comboUsuarios.addItem(new DataUsuarioAux(da));
    }
    comboUsuarios.setRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof DataUsuarioAux du) {
          setText("%s: %s — %s".formatted(du.getTipoUsuario(), nvl(du.getNickname()),
              nvl(du.getNombre())));
          setToolTipText(du.getEmail());
        }
        return this;
      }
    });
    comboUsuarios.addActionListener(e -> onSeleccionarUsuario());
    top.add(comboUsuarios);

    btnGuardar = new JButton("Guardar cambios");
    btnGuardar.setBackground(Color.GREEN);
    btnGuardar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    btnCerrar = new JButton("Cerrar");
    btnCerrar.setBackground(Color.RED);
    btnCerrar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    btnCerrar.addActionListener(e -> dispose());
    top.add(btnGuardar);
    top.add(btnCerrar);

    getContentPane().add(top, BorderLayout.NORTH);

    // ——— CENTER: formulario ———

    // comunes
    txtNick = new JTextField();
    txtNick.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    txtEmail = new JTextField();
    txtEmail.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    txtNombre = new JTextField();
    txtNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));

    JPanel formCommon = new JPanel(new GridLayout(0, 2, 6, 8));
    JLabel lblNickname = new JLabel("Nickname:");
    lblNickname.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    formCommon.add(lblNickname);
    formCommon.add(txtNick);
    JLabel lblEmail = new JLabel("Email:");
    lblEmail.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    formCommon.add(lblEmail);
    formCommon.add(txtEmail);
    JLabel lblNombre = new JLabel("Nombre:");
    lblNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    formCommon.add(lblNombre);
    formCommon.add(txtNombre);

    txtNick.setEnabled(false); // no editable
    txtEmail.setEnabled(false); // no editable

    // cliente
    txtApellido = new JTextField();
    dcFechaNac = new JDateChooser();
    txtNacionalidad = new JTextField();
    cmbTipoDoc = new JComboBox<>(TipoDocumento.values());
    txtNumDoc = new JTextField();

    JPanel pnlCliente = new JPanel(new GridLayout(0, 2, 6, 8));
    pnlCliente.add(new JLabel("Apellido:"));
    pnlCliente.add(txtApellido);
    pnlCliente.add(new JLabel("Fecha de nacimiento:"));
    pnlCliente.add(dcFechaNac);
    pnlCliente.add(new JLabel("País de nacimiento:"));
    pnlCliente.add(txtNacionalidad);
    pnlCliente.add(new JLabel("Tipo de documento:"));
    pnlCliente.add(cmbTipoDoc);
    pnlCliente.add(new JLabel("Número de documento:"));
    pnlCliente.add(txtNumDoc);

    // aerolínea
    areaDesc = new JTextArea(5, 30);
    areaDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
    txtSitio = new JTextField();
    txtSitio.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));

    JPanel pnlAero = new JPanel(new GridBagLayout());
    Insets PAD = new Insets(4, 4, 4, 4);
    GridBagConstraints c;

    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.insets = PAD;
    c.anchor = GridBagConstraints.EAST;
    JLabel lblDescripcion = new JLabel("Descripción:");
    lblDescripcion.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    pnlAero.add(lblDescripcion, c);

    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 0;
    c.insets = PAD;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1;
    c.weighty = 1;
    pnlAero.add(new JScrollPane(areaDesc), c);

    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 1;
    c.insets = PAD;
    c.anchor = GridBagConstraints.EAST;
    JLabel lblSitioWeb = new JLabel("Sitio web:");
    lblSitioWeb.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    pnlAero.add(lblSitioWeb, c);

    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = 1;
    c.insets = PAD;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1;
    pnlAero.add(txtSitio, c);

    // — cards (CAMPO, NO LOCAL)
    cards = new JPanel(new CardLayout(0, 0));
    cards.add(pnlCliente, CARD_CLIENTE);
    cards.add(pnlAero, CARD_AERO);

    JPanel center = new JPanel(new BorderLayout(8, 8));
    center.add(formCommon, BorderLayout.NORTH);
    center.add(cards, BorderLayout.CENTER);

    getContentPane().add(center, BorderLayout.CENTER);

    // acción guardar
    btnGuardar.addActionListener(e -> guardar());
  }

  private void cargarUsuarios() {
    try {
      bloquearTodo(true);
      comboUsuarios.removeAllItems();
      List<DataUsuario> usuarios = sistema.listarUsuarios();
      usuarios.stream().map(DataUsuarioAux::new) // convertir a DataUsuarioAux
          .sorted(Comparator.comparing(DataUsuarioAux::getTipoUsuario) // primero por tipo
              .thenComparing(DataUsuarioAux::getNickname, String.CASE_INSENSITIVE_ORDER)) // luego
                                                                                          // por
                                                                                          // nickname
          .forEach(comboUsuarios::addItem);
      comboUsuarios.setSelectedIndex(-1); // nada seleccionado al inicio
    } finally {
      bloquearTodo(false);
      limpiar();
      mostrarCard(CARD_CLIENTE); // por defecto
    }
  }

  private void onSeleccionarUsuario() {
    DataUsuarioAux du = (DataUsuarioAux) comboUsuarios.getSelectedItem();
    if (du == null) {
      limpiar();
      return;
    }
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
    JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Aviso",
        JOptionPane.WARNING_MESSAGE);
  }

  private void guardar() {
    DataUsuarioAux du = (DataUsuarioAux) comboUsuarios.getSelectedItem();
    if (du == null) {
      return;      
    }
    String nick = du.getNickname();
    // ¿cliente?
    DataCliente dcActual = sistema.verInfoCliente(nick);
    if (dcActual != null) {
      try {
        DataCliente dto = new DataCliente(txtNombre.getText().trim(), dcActual.getNickname(), // nickname
                                                                                              // NO
                                                                                              // cambia
            dcActual.getEmail(), // email NO cambia
            dcActual.getContrasenia(), // contraseña NO cambia
            txtApellido.getText().trim(), dcFechaNac.getDate(), txtNacionalidad.getText().trim(),
            (TipoDocumento) cmbTipoDoc.getSelectedItem(), txtNumDoc.getText().trim());
        sistema.modificarCliente(nick, dto);
        JOptionPane.showMessageDialog(this, "Cliente modificado con éxito.");
      } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación",
            JOptionPane.ERROR_MESSAGE);
      }
      return;
    }
    // ¿aerolínea?
    DataAerolinea daActual = sistema.verInfoAerolinea(nick);
    if (daActual != null) {
      try {
        DataAerolinea dto = new DataAerolinea(txtNombre.getText().trim(), daActual.getNickname(), // nickname
                                                                                                  // NO
                                                                                                  // cambia
            daActual.getEmail(), // email NO cambia
            daActual.getContrasenia(), // contraseña NO cambia
            areaDesc.getText().trim(), txtSitio.getText().trim());
        sistema.modificarAerolinea(nick, dto);
        JOptionPane.showMessageDialog(this, "Aerolínea modificada con éxito.");
      } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void mostrarCard(String card) {
    if (cards == null) {
      return; // o lanza IllegalStateException con un mensaje claro      
    }
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

  private static String nvl(String s) {
    return s == null ? "" : s;
  }
}