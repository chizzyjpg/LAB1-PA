package Presentacion;

import Logica.DataAerolinea;
import Logica.DataCliente;
import Logica.ISistema;
import Logica.TipoDocumento;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;


/**
 * Clase para el registro de usuarios (Clientes y Aerolíneas).
 */
public class RegistroUsuario extends JInternalFrame {

  private static final long serialVersionUID = 1L;
  private JPanel contentPane;
  private final ButtonGroup buttonGroup = new ButtonGroup();
  private JTextField textFieldNombre;
  private JTextField textFieldNickname;
  private JTextField textFieldEmail;
  private JTextField textFieldApellido;
  private JTextField textFieldNacionalidad;
  private JTextField textFieldNumeroDocumento;
  private JTextField textFieldSitioWeb;
  private JDateChooser dcFechaNac;
  private JRadioButton rdbtnCliente;
  private JRadioButton rdbtnAerolinea;
  private JComboBox<TipoDocumento> comboBox;
  private JPanel panelCliente;
  private JPanel panelAerolinea;
  private JTextArea textAreaDescripcion;
  private JPasswordField passwordField;
  private JPasswordField confirmPasswordField;
  // private final ISistema sistema;

  /**
   * Create the frame.
   */
  public RegistroUsuario(ISistema sistema) {
    super("Registro de Usuario", true, true, true, false);
    setSize(620, 500);
    setVisible(true);
    setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
    // this.sistema = sistema;

    setTitle("Registrar Usuario");
    setBounds(100, 100, 609, 497);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(new BorderLayout(0, 0));

    JPanel panelUsuario = new JPanel();
    contentPane.add(panelUsuario, BorderLayout.NORTH);
    panelUsuario.setLayout(new MigLayout("", "[][grow][]", "[][][][][][]")); // se agregan dos filas
                                                                             // para contraseñas

    JLabel lblTipo = new JLabel("Tipo:");
    lblTipo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelUsuario.add(lblTipo, "cell 0 0");

    rdbtnCliente = new JRadioButton("Cliente");
    buttonGroup.add(rdbtnCliente);
    rdbtnCliente.setSelected(true);
    rdbtnCliente.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelUsuario.add(rdbtnCliente, "cell 1 0,alignx center");

    rdbtnAerolinea = new JRadioButton("Aerolínea");
    buttonGroup.add(rdbtnAerolinea);
    rdbtnAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelUsuario.add(rdbtnAerolinea, "cell 2 0,alignx center");

    JLabel lblNombre = new JLabel("Nombre");
    lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
    lblNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelUsuario.add(lblNombre, "cell 0 1,alignx trailing");

    textFieldNombre = new JTextField();
    textFieldNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    panelUsuario.add(textFieldNombre, "cell 1 1,growx");
    textFieldNombre.setColumns(10);

    JLabel lblNickname = new JLabel("Nickname");
    lblNickname.setHorizontalAlignment(SwingConstants.CENTER);
    lblNickname.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelUsuario.add(lblNickname, "cell 0 2,alignx trailing");

    textFieldNickname = new JTextField();
    textFieldNickname.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldNickname.setColumns(10);
    panelUsuario.add(textFieldNickname, "cell 1 2,growx");

    JLabel lblEmail = new JLabel("Email");
    lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
    lblEmail.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelUsuario.add(lblEmail, "cell 0 3,alignx trailing");

    textFieldEmail = new JTextField();
    textFieldEmail.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldEmail.setColumns(10);
    panelUsuario.add(textFieldEmail, "cell 1 3,growx");

    // Campo Contraseña
    JLabel lblPassword = new JLabel("Contraseña");
    lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
    lblPassword.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelUsuario.add(lblPassword, "cell 0 4,alignx trailing");

    passwordField = new JPasswordField();
    passwordField.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    passwordField.setColumns(10);
    panelUsuario.add(passwordField, "cell 1 4,growx");

    // Campo Confirmar Contraseña
    JLabel lblConfirmPassword = new JLabel("Confirmar Contraseña");
    lblConfirmPassword.setHorizontalAlignment(SwingConstants.CENTER);
    lblConfirmPassword.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelUsuario.add(lblConfirmPassword, "cell 0 5,alignx trailing");

    confirmPasswordField = new JPasswordField();
    confirmPasswordField.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    confirmPasswordField.setColumns(10);
    panelUsuario.add(confirmPasswordField, "cell 1 5,growx");

    JPanel panelCards = new JPanel();
    contentPane.add(panelCards, BorderLayout.CENTER);
    panelCards.setLayout(new CardLayout(0, 0));

    panelCliente = new JPanel();
    panelCards.add(panelCliente, "CLIENTE");
    panelCliente.setLayout(new MigLayout("", "[][grow]", "[][grow][][][]"));

    JLabel lblApellido = new JLabel("Apellido");
    lblApellido.setHorizontalAlignment(SwingConstants.CENTER);
    lblApellido.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelCliente.add(lblApellido, "cell 0 0,alignx trailing");

    textFieldApellido = new JTextField();
    textFieldApellido.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldApellido.setColumns(10);
    panelCliente.add(textFieldApellido, "cell 1 0,growx");

    JLabel lblFechaDeNacimiento = new JLabel("Fecha de Nacimiento");
    lblFechaDeNacimiento.setHorizontalAlignment(SwingConstants.CENTER);
    lblFechaDeNacimiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelCliente.add(lblFechaDeNacimiento, "cell 0 1,alignx trailing");

    dcFechaNac = new JDateChooser();
    dcFechaNac.getCalendarButton().addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });
    panelCliente.add(dcFechaNac, "cell 1 1,grow");

    JLabel lblNacionalidad = new JLabel("País de Nacimiento");
    lblNacionalidad.setHorizontalAlignment(SwingConstants.CENTER);
    lblNacionalidad.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelCliente.add(lblNacionalidad, "cell 0 2,alignx trailing");

    textFieldNacionalidad = new JTextField();
    textFieldNacionalidad.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldNacionalidad.setColumns(10);
    panelCliente.add(textFieldNacionalidad, "cell 1 2,growx");

    JLabel lblTipoDeDocumento = new JLabel("Tipo de Documento");
    lblTipoDeDocumento.setHorizontalAlignment(SwingConstants.CENTER);
    lblTipoDeDocumento.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelCliente.add(lblTipoDeDocumento, "cell 0 3,alignx trailing");

    comboBox = new JComboBox<>(TipoDocumento.values());
    comboBox.setModel(new DefaultComboBoxModel(TipoDocumento.values()));
    comboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    panelCliente.add(comboBox, "cell 1 3,growx");

    JLabel lblNmeroDeDocumento = new JLabel("Número de Documento");
    lblNmeroDeDocumento.setHorizontalAlignment(SwingConstants.CENTER);
    lblNmeroDeDocumento.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelCliente.add(lblNmeroDeDocumento, "cell 0 4,alignx trailing,aligny center");

    textFieldNumeroDocumento = new JTextField();
    textFieldNumeroDocumento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldNumeroDocumento.setColumns(10);
    panelCliente.add(textFieldNumeroDocumento, "cell 1 4,growx");

    panelAerolinea = new JPanel();
    panelCards.add(panelAerolinea, "AEROLINEA");
    panelAerolinea.setLayout(new MigLayout("", "[][grow]", "[grow][]"));

    JLabel lblDescripcin = new JLabel("Descripción:");
    lblDescripcin.setHorizontalAlignment(SwingConstants.CENTER);
    lblDescripcin.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelAerolinea.add(lblDescripcin, "cell 0 0,alignx trailing");

    JScrollPane scrollPane = new JScrollPane();
    panelAerolinea.add(scrollPane, "cell 1 0,grow");

    textAreaDescripcion = new JTextArea();
    scrollPane.setViewportView(textAreaDescripcion);

    JLabel lblSitioWeb = new JLabel("Sitio Web");
    lblSitioWeb.setHorizontalAlignment(SwingConstants.CENTER);
    lblSitioWeb.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    panelAerolinea.add(lblSitioWeb, "cell 0 1,alignx trailing");

    textFieldSitioWeb = new JTextField();
    textFieldSitioWeb.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldSitioWeb.setColumns(10);
    panelAerolinea.add(textFieldSitioWeb, "cell 1 1,growx");

    JPanel panel = new JPanel();
    contentPane.add(panel, BorderLayout.SOUTH);
    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    JButton btnCancelar = new JButton("CANCELAR");
    btnCancelar.setBackground(new Color(241, 43, 14));
    btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    panel.add(btnCancelar);
    btnCancelar.addActionListener(e -> {
      dispose();
    });

    JButton btnGuardar = new JButton("ACEPTAR");
    btnGuardar.setBackground(new Color(5, 250, 79));
    btnGuardar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));

    panel.add(btnGuardar);
    btnGuardar.addActionListener(e -> {
      String nickname = textFieldNickname.getText().trim();
      String nombre = textFieldNombre.getText().trim();
      String email = textFieldEmail.getText().trim();
      String password = new String(passwordField.getPassword()).trim();
      String confirm = new String(confirmPasswordField.getPassword()).trim();

      // Validaciones básicas
      if (nickname.isEmpty() || nombre.isEmpty() || email.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Validación",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Validación contraseña (se exige pero NO se pasa al DTO)
      if (password.isEmpty() || confirm.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese y confirme la contraseña.", "Validación",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (!password.equals(confirm)) {
        JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Validación",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Validación email
      if (!emailValido(email)) {
        JOptionPane.showMessageDialog(this, "Email inválido. Ejemplo: nombre@dominio.com",
            "Validación", JOptionPane.ERROR_MESSAGE);
        textFieldEmail.requestFocus();
        return;
      }

      if (rdbtnCliente.isSelected()) {
        String apellido = textFieldApellido.getText().trim();
        Date fUtil = dcFechaNac.getDate();
        String nac = textFieldNacionalidad.getText().trim();
        TipoDocumento tipo = (TipoDocumento) comboBox.getSelectedItem();
        String nDoc = textFieldNumeroDocumento.getText().trim();

        if (apellido.isEmpty() || fUtil == null || nac.isEmpty() || tipo == null
            || nDoc.isEmpty()) {
          JOptionPane.showMessageDialog(this, "Complete todos los campos de Cliente.", "Validación",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate minFecha = hoy.minusYears(120);
        LocalDate fechaNacimiento = fUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (fechaNacimiento.isAfter(hoy) || fechaNacimiento.isBefore(minFecha)) {
          JOptionPane.showMessageDialog(this, "Fecha de Nacimiento no válida.", "Validación",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        String docLimpio = nDoc.replaceAll("[.\\-\\s]", "");
        if (!docLimpio.matches("\\d+")) {
          JOptionPane.showMessageDialog(this, "Número de Documento no válido.", "Validación",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        // Crear DTO SIN contraseña
        DataCliente data = new DataCliente(nombre, nickname, email, password, // Se pasa la
                                                                              // contraseña al
                                                                              // constructor del DTO
            apellido, fUtil, nac, tipo, docLimpio);

        try {
          sistema.registrarUsuario(data);
          JOptionPane.showMessageDialog(this, "Cliente registrado con éxito");
          limpiarFormulario();
        } catch (IllegalArgumentException ex) {
          JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación",
              JOptionPane.ERROR_MESSAGE);
        }

      } else { // Aerolínea
        String descripcion = textAreaDescripcion.getText().trim();
        String sitio = textFieldSitioWeb.getText().trim();

        if (descripcion.isEmpty()) {
          JOptionPane.showMessageDialog(this, "La descripción de la aerolínea es obligatoria.",
              "Validación", JOptionPane.ERROR_MESSAGE);
          return;
        }

        DataAerolinea data = new DataAerolinea(nombre, nickname, email, password, // Se pasa la
                                                                                  // contraseña al
                                                                                  // constructor del
                                                                                  // DTO
            descripcion, sitio);

        try {
          sistema.registrarUsuario(data);
          JOptionPane.showMessageDialog(this, "Aerolínea registrada con éxito");
          limpiarFormulario();
        } catch (IllegalArgumentException ex) {
          JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // Cambiar tarjeta según radio seleccionado
    rdbtnCliente.addActionListener(e -> {
      ((CardLayout) panelCards.getLayout()).show(panelCards, "CLIENTE");
    });

    rdbtnAerolinea.addActionListener(e -> {
      ((CardLayout) panelCards.getLayout()).show(panelCards, "AEROLINEA");
    });

    // Mostrar CLIENTE por defecto al abrir
    ((CardLayout) panelCards.getLayout()).show(panelCards, "CLIENTE");

  }

  private void limpiarFormulario() {
    // TODO Auto-generated method stub
    textFieldNickname.setText("");
    textFieldNombre.setText("");
    textFieldEmail.setText("");

    if (rdbtnCliente.isSelected()) {
      // Cliente
      textFieldApellido.setText("");
      dcFechaNac.setDate(null); // ← JDateChooser
      textFieldNacionalidad.setText("");
      comboBox.setSelectedIndex(0); // ← JComboBox enum
      textFieldNumeroDocumento.setText("");
    } else {
      // Aerolínea
      textAreaDescripcion.setText("");
      textFieldSitioWeb.setText("");
    }

    if (passwordField != null) {
      passwordField.setText("");      
    }
    if (confirmPasswordField != null) {
      confirmPasswordField.setText("");      
    }
  }

  private static final java.util.regex.Pattern EMAIL_RX = java.util.regex.Pattern
      .compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  private boolean emailValido(String emailRaw) {
    String email = emailRaw == null ? "" : emailRaw.trim();
    return EMAIL_RX.matcher(email).matches();
  }

}