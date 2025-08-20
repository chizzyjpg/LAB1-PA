package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;
import javax.swing.SpinnerNumberModel;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistrarRutaVuelo extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldNombre;
	private JTextField textFieldTurista;
	private JTextField textFieldEjecutivo;
	private JTextField textFieldEquipajeExtra;
	private JTextField textFieldCiudadOrigen;
	private JTextField textFieldCiudadDestino;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrarRutaVuelo frame = new RegistrarRutaVuelo();
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
	public RegistrarRutaVuelo() {
		setTitle("Registrar Ruta de Vuelo");
		setBounds(100, 100, 560, 523);
		getContentPane().setLayout(null);
		
		JLabel lblAerolinea = new JLabel("Aerolínea:");
		lblAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblAerolinea.setBounds(8, 25, 72, 14);
		getContentPane().add(lblAerolinea);
		
		JComboBox comboBoxAerolinea = new JComboBox();
		comboBoxAerolinea.setBounds(155, 25, 145, 22);
		getContentPane().add(comboBoxAerolinea);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNombre.setBounds(8, 57, 72, 14);
		getContentPane().add(lblNombre);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(155, 58, 145, 20);
		getContentPane().add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		JLabel lblDesc = new JLabel("Descripción:");
		lblDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblDesc.setBounds(8, 89, 87, 20);
		getContentPane().add(lblDesc);
		
		JTextArea textAreaDesc = new JTextArea();
		textAreaDesc.setBounds(155, 91, 379, 78);
		getContentPane().add(textAreaDesc);
		
		JLabel lblHora = new JLabel("Hora:");
		lblHora.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblHora.setBounds(8, 183, 72, 14);
		getContentPane().add(lblHora);
		
		SpinnerDateModel model = new SpinnerDateModel();
		JSpinner spinnerHora = new JSpinner(new SpinnerDateModel(new Date(1755715138092L), null, null, Calendar.DAY_OF_MONTH));

		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerHora, "HH:mm");
		spinnerHora.setEditor(editor);

		spinnerHora.setBounds(155, 180, 62, 25);
		getContentPane().add(spinnerHora);
		
		JLabel lblCostoTurista = new JLabel("Costo Turista:");
		lblCostoTurista.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCostoTurista.setBounds(8, 219, 102, 14);
		getContentPane().add(lblCostoTurista);
		
		textFieldTurista = new JTextField();
		textFieldTurista.setColumns(10);
		textFieldTurista.setBounds(155, 216, 145, 20);
		getContentPane().add(textFieldTurista);
		
		JLabel lblCostoEjecutivo = new JLabel("Costo Ejecutivo:");
		lblCostoEjecutivo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCostoEjecutivo.setBounds(8, 244, 116, 25);
		getContentPane().add(lblCostoEjecutivo);
		
		textFieldEjecutivo = new JTextField();
		textFieldEjecutivo.setColumns(10);
		textFieldEjecutivo.setBounds(155, 244, 145, 20);
		getContentPane().add(textFieldEjecutivo);
		
		JLabel lblCostoEquipajeExtra = new JLabel("Costo Equipaje \r\nExtra:");
		lblCostoEquipajeExtra.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCostoEquipajeExtra.setBounds(8, 272, 145, 22);
		getContentPane().add(lblCostoEquipajeExtra);
		
		textFieldEquipajeExtra = new JTextField();
		textFieldEquipajeExtra.setColumns(10);
		textFieldEquipajeExtra.setBounds(155, 275, 145, 20);
		getContentPane().add(textFieldEquipajeExtra);
		
		JLabel lblCiudadOrigen = new JLabel("Ciudad Origen:");
		lblCiudadOrigen.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCiudadOrigen.setBounds(8, 305, 102, 22);
		getContentPane().add(lblCiudadOrigen);
		
		textFieldCiudadOrigen = new JTextField();
		textFieldCiudadOrigen.setColumns(10);
		textFieldCiudadOrigen.setBounds(155, 305, 145, 20);
		getContentPane().add(textFieldCiudadOrigen);
		
		JLabel lblCiudadDestino = new JLabel("Ciudad Destino:");
		lblCiudadDestino.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCiudadDestino.setBounds(8, 341, 102, 22);
		getContentPane().add(lblCiudadDestino);
		
		textFieldCiudadDestino = new JTextField();
		textFieldCiudadDestino.setColumns(10);
		textFieldCiudadDestino.setBounds(155, 341, 145, 20);
		getContentPane().add(textFieldCiudadDestino);
		
		JLabel lblFechaAlta = new JLabel("Fecha Alta:");
		lblFechaAlta.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblFechaAlta.setBounds(8, 380, 79, 14);
		getContentPane().add(lblFechaAlta);
		
		JDateChooser fechaAlta = new JDateChooser();
		fechaAlta.setBounds(155, 374, 145, 20);
		getContentPane().add(fechaAlta);
		
		JComboBox comboBoxCategoria = new JComboBox();
		comboBoxCategoria.setBounds(155, 405, 145, 22);
		getContentPane().add(comboBoxCategoria);
		
		JLabel lblCategoria = new JLabel("Categoría:");
		lblCategoria.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCategoria.setBounds(8, 405, 72, 22);
		getContentPane().add(lblCategoria);
		
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.setBounds(155, 451, 100, 23);
		getContentPane().add(btnCancelar);
		
		JButton btnAceptar = new JButton("ACEPTAR");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//String aerolinea = comboBoxAerolinea.getToolTipText().trim();
				String nombre = textFieldNombre.getText().trim();
				String Descripcion = textAreaDesc.getText().trim();
				String turista = textFieldTurista.getText().trim();
				String ejecutivo = textFieldEjecutivo.getText().trim();
				String costo = textFieldEquipajeExtra.getText().trim();
				String ciudadO = textFieldCiudadOrigen.getText().trim();
				String ciudadD = textFieldCiudadDestino.getText().trim();
				java.util.Date fecha = fechaAlta.getDate();
				//String cat = comboBoxCategoria.getToolTipText().trim();
				
				/*if(aerolinea.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Aerolínea NO puede estar vacía", "Error Aerolínea" , JOptionPane.ERROR_MESSAGE);
					return;
				}*/
				
				if(nombre.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "El Nombre NO puede estar vacío", "Error Nombre" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(Descripcion.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Descripción NO puede estar vacía", "Error Descripción" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(turista.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "El Costo Turista NO puede estar vacío", "Error Costo" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(ejecutivo.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "El Costo Ejecutivo NO puede estar vacío", "Error Costo" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(costo.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "El Costo de Equipaje NO puede estar vacío", "Error Costo" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(ciudadO.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Ciudad de Origen NO puede estar vacía", "Error Origen" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(ciudadD.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Ciudad de Destino NO puede estar vacía", "Error Destino" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(fecha == null) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Fecha NO puede estar vacía", "Error Fecha" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				/*if(cat.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Categoría NO puede estar vacía", "Error Categoría" , JOptionPane.ERROR_MESSAGE);
					return;
				}*/
				
				JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "Ruta de Vuelo registrada correctamente!\nNombre: " + nombre, "ÉXITO!" , JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAceptar.setBackground(new Color(5, 250, 79));
		btnAceptar.setBounds(265, 451, 89, 23);
		getContentPane().add(btnAceptar);

	}
}
