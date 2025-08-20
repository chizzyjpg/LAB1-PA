package Presentacion;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.CardLayout;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import Logica.TipoAsiento;
import Logica.TipoDocumento;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	private JComboBox comboBox;
	private JPanel panelCliente;
	private JPanel panelAerolinea;
	private JTextArea textAreaDescripcion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroUsuario frame = new RegistroUsuario();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegistroUsuario() {
		super("Registro de Usuario", true, true, true, true);
		 setSize(620, 500);
		 setVisible(true);
		
		setTitle("RegistrarUsuario");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 609, 497);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelUsuario = new JPanel();
		contentPane.add(panelUsuario, BorderLayout.NORTH);
		panelUsuario.setLayout(new MigLayout("", "[][grow][]", "[][][][]"));
		
		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelUsuario.add(lblTipo, "cell 0 0");
		
		rdbtnCliente = new JRadioButton("Cliente");
		buttonGroup.add(rdbtnCliente);
		rdbtnCliente.setSelected(true);
		rdbtnCliente.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelUsuario.add(rdbtnCliente, "cell 1 0,alignx center");
		
		rdbtnAerolinea = new JRadioButton("Aerolínea");
		buttonGroup.add(rdbtnAerolinea);
		rdbtnAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelUsuario.add(rdbtnAerolinea, "cell 2 0,alignx center");
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelUsuario.add(lblNombre, "cell 0 1,alignx trailing");
		
		textFieldNombre = new JTextField();
		textFieldNombre.setEnabled(false);
		textFieldNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelUsuario.add(textFieldNombre, "cell 1 1,growx");
		textFieldNombre.setColumns(10);
		
		JLabel lblNickname = new JLabel("Nickname");
		lblNickname.setHorizontalAlignment(SwingConstants.CENTER);
		lblNickname.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelUsuario.add(lblNickname, "cell 0 2,alignx trailing");
		
		textFieldNickname = new JTextField();
		textFieldNickname.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textFieldNickname.setColumns(10);
		panelUsuario.add(textFieldNickname, "cell 1 2,growx");
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelUsuario.add(lblEmail, "cell 0 3,alignx trailing");
		
		textFieldEmail = new JTextField();
		textFieldEmail.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textFieldEmail.setColumns(10);
		panelUsuario.add(textFieldEmail, "cell 1 3,growx");
		
		JPanel panelCards = new JPanel();
		contentPane.add(panelCards, BorderLayout.CENTER);
		panelCards.setLayout(new CardLayout(0, 0));
		
		panelCliente = new JPanel();
		panelCards.add(panelCliente, "CLIENTE");
		panelCliente.setLayout(new MigLayout("", "[][grow]", "[][grow][][][]"));
		
		JLabel lblApellido = new JLabel("Apellido");
		lblApellido.setHorizontalAlignment(SwingConstants.CENTER);
		lblApellido.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelCliente.add(lblApellido, "cell 0 0,alignx trailing");
		
		textFieldApellido = new JTextField();
		textFieldApellido.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textFieldApellido.setColumns(10);
		panelCliente.add(textFieldApellido, "cell 1 0,growx");
		
		JLabel lblFechaDeNacimiento = new JLabel("Fecha de Nacimiento");
		lblFechaDeNacimiento.setHorizontalAlignment(SwingConstants.CENTER);
		lblFechaDeNacimiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelCliente.add(lblFechaDeNacimiento, "cell 0 1,alignx trailing");
		
		dcFechaNac = new JDateChooser();
		dcFechaNac.getCalendarButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panelCliente.add(dcFechaNac, "cell 1 1,grow");
		
		JLabel lblNacionalidad = new JLabel("País de Nacimiento");
		lblNacionalidad.setHorizontalAlignment(SwingConstants.CENTER);
		lblNacionalidad.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelCliente.add(lblNacionalidad, "cell 0 2,alignx trailing");
		
		textFieldNacionalidad = new JTextField();
		textFieldNacionalidad.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textFieldNacionalidad.setColumns(10);
		panelCliente.add(textFieldNacionalidad, "cell 1 2,growx");
		
		JLabel lblTipoDeDocumento = new JLabel("Tipo de Documento");
		lblTipoDeDocumento.setHorizontalAlignment(SwingConstants.CENTER);
		lblTipoDeDocumento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelCliente.add(lblTipoDeDocumento, "cell 0 3,alignx trailing");
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(TipoDocumento.values()));
		comboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelCliente.add(comboBox, "cell 1 3,growx");
		
		JLabel lblNmeroDeDocumento = new JLabel("Número de Documento");
		lblNmeroDeDocumento.setHorizontalAlignment(SwingConstants.CENTER);
		lblNmeroDeDocumento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
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
		lblDescripcin.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelAerolinea.add(lblDescripcin, "cell 0 0,alignx trailing");
		
		JScrollPane scrollPane = new JScrollPane();
		panelAerolinea.add(scrollPane, "cell 1 0,grow");
		
		textAreaDescripcion = new JTextArea();
		scrollPane.setViewportView(textAreaDescripcion);
		
		JLabel lblSitioWeb = new JLabel("Sitio Web");
		lblSitioWeb.setHorizontalAlignment(SwingConstants.CENTER);
		lblSitioWeb.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panelAerolinea.add(lblSitioWeb, "cell 0 1,alignx trailing");
		
		textFieldSitioWeb = new JTextField();
		textFieldSitioWeb.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textFieldSitioWeb.setColumns(10);
		panelAerolinea.add(textFieldSitioWeb, "cell 1 1,growx");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		panel.add(btnCancelar);
		btnCancelar.addActionListener(e -> {
			dispose();
		});
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.setBackground(new Color(5, 250, 79));
		btnGuardar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		
		panel.add(btnGuardar);
		btnGuardar.addActionListener(e -> {
		    String nickname = textFieldNickname.getText().trim();
		    String nombre   = textFieldNombre.getText().trim();
		    String email    = textFieldEmail.getText().trim();

		    if (rdbtnCliente.isSelected()) {
		        String apellido = textFieldApellido.getText().trim();
		        java.util.Date fUtil = dcFechaNac.getDate();
		        String nac      = textFieldNacionalidad.getText().trim();
		        TipoDocumento tipo   = (TipoDocumento) comboBox.getSelectedItem();
		        String nDoc     = textFieldNumeroDocumento.getText().trim();
		        
		        if (nickname.isEmpty() || nombre.isEmpty() || email.isEmpty() || apellido.isEmpty() || fUtil == null || nac.isEmpty() || tipo == null || nDoc.isEmpty()) {
		            JOptionPane.showMessageDialog(this, "Complete todos los campos de Cliente.", "Validación", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
		        
		        java.time.LocalDate fechaNac = fUtil.toInstant()
		                .atZone(java.time.ZoneId.systemDefault())
		                .toLocalDate();
		        
		        // TODO: guardar en tu servicio/DAO.
		        // usuarioService.registrarCliente(nick, nombre, email, apellido, fechaNac, nacionalidad, tipo.name(), nroDoc);
		        
		        JOptionPane.showMessageDialog(this, "Cliente guardado.");
		        limpiarFormulario(); // ⬅ limpiar al final
		            
		            
		        // validar y registrar Cliente...
		    } else { // Aerolínea
		        String descripcion = textAreaDescripcion.getText().trim();
		        String sitio       = textFieldSitioWeb.getText().trim(); // opcional
		        // validar y registrar Aerolínea...
		        
		        if (descripcion.isEmpty()) {
		            JOptionPane.showMessageDialog(this, "La descripción de la aerolínea es obligatoria.", "Validación", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
		            
		         // TODO: guardar en tu servicio/DAO.
		         // usuarioService.registrarAerolinea(nick, nombre, email, descripcion, sitioWeb.isEmpty()? null : sitioWeb);
		        
		        JOptionPane.showMessageDialog(this, "Aerolínea guardada.");
		        limpiarFormulario(); // ⬅ limpiar al final

		        }
		    
		    
		    
		});
		
		// Cambiar tarjeta según radio seleccionado
		rdbtnCliente.addActionListener(e -> {
		    ((java.awt.CardLayout) panelCards.getLayout()).show(panelCards, "CLIENTE");
		});

		rdbtnAerolinea.addActionListener(e -> {
		    ((java.awt.CardLayout) panelCards.getLayout()).show(panelCards, "AEROLINEA");
		});

		// Mostrar CLIENTE por defecto al abrir
		((java.awt.CardLayout) panelCards.getLayout()).show(panelCards, "CLIENTE");

	}

	private void limpiarFormulario() {
		// TODO Auto-generated method stub
		textFieldNickname.setText("");
	    textFieldNombre.setText("");
	    textFieldEmail.setText("");

	    if (rdbtnCliente.isSelected()) {
	        // Cliente
	        textFieldApellido.setText("");
	        dcFechaNac.setDate(null);         // ← JDateChooser
	        textFieldNacionalidad.setText("");
	        comboBox.setSelectedIndex(0);    // ← JComboBox enum
	        textFieldNumeroDocumento.setText("");
	    } else {
	        // Aerolínea
	    	textAreaDescripcion.setText("");
	        textFieldSitioWeb.setText("");
	    }

	}
	
	
}
