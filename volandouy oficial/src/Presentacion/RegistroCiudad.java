package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;

public class RegistroCiudad extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldNomCiudad;
	private JTextField textFieldPais;
	private JTextField textFieldAeropuerto;
	private JTextField textFieldWeb;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroCiudad frame = new RegistroCiudad();
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
	public RegistroCiudad() {
		setClosable(true);
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setTitle("Registrar Ciudad");
		setBounds(100, 100, 640, 473);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nombre de la Ciudad:");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 43, 138, 14);
		getContentPane().add(lblNewLabel);
		
		textFieldNomCiudad = new JTextField();
		textFieldNomCiudad.setBounds(154, 37, 460, 20);
		getContentPane().add(textFieldNomCiudad);
		textFieldNomCiudad.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("País de la Ciudad:");
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 71, 110, 14);
		getContentPane().add(lblNewLabel_1);
		
		textFieldPais = new JTextField();
		textFieldPais.setColumns(10);
		textFieldPais.setBounds(154, 65, 460, 20);
		getContentPane().add(textFieldPais);
		
		JLabel lblNewLabel_2 = new JLabel("Aeropuerto asociado:");
		lblNewLabel_2.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(10, 97, 138, 19);
		getContentPane().add(lblNewLabel_2);
		
		textFieldAeropuerto = new JTextField();
		textFieldAeropuerto.setColumns(10);
		textFieldAeropuerto.setBounds(154, 96, 460, 20);
		getContentPane().add(textFieldAeropuerto);
		
		JLabel lblNewLabel_3 = new JLabel("Descripción:");
		lblNewLabel_3.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(10, 127, 87, 20);
		getContentPane().add(lblNewLabel_3);
		
		JTextArea textAreaDesc = new JTextArea();
		textAreaDesc.setBounds(154, 127, 460, 78);
		getContentPane().add(textAreaDesc);
		
		JLabel lblNewLabel_4 = new JLabel("Sitio Web:");
		lblNewLabel_4.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel_4.setBounds(10, 222, 70, 14);
		getContentPane().add(lblNewLabel_4);
		
		textFieldWeb = new JTextField();
		textFieldWeb.setColumns(10);
		textFieldWeb.setBounds(154, 216, 460, 20);
		getContentPane().add(textFieldWeb);
		
		JLabel lblNewLabel_4_1 = new JLabel("Fecha Alta:");
		lblNewLabel_4_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel_4_1.setBounds(10, 253, 79, 14);
		getContentPane().add(lblNewLabel_4_1);
		
		JDateChooser fechaAlta = new JDateChooser();
		fechaAlta.setBounds(154, 247, 135, 20);
		getContentPane().add(fechaAlta);

		JButton btnAceptar = new JButton("ACEPTAR");
		btnAceptar.setBackground(new Color(5, 250, 79));
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nomCiudad = textFieldNomCiudad.getText().trim();
				String nomPais = textFieldPais.getText().trim();
				String nomAeropuerto = textFieldAeropuerto.getText().trim();
				String Descripcion = textAreaDesc.getText().trim();
				String web = textFieldWeb.getText().trim();
				java.util.Date fecha = fechaAlta.getDate();
				
				if(nomCiudad.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroCiudad.this, "La Ciudad NO puede estar vacía", "Error Ciudad" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(nomPais.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroCiudad.this, "El País NO puede estar vacío", "Error País" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(nomAeropuerto.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroCiudad.this, "El Aeropuerto NO puede estar vacío", "Error Aeropuerto" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(Descripcion.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroCiudad.this, "La Descripción NO puede estar vacía", "Error Descripción" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(web.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroCiudad.this, "El Sitio Web NO puede estar vacío", "Error Sitio Web" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(fecha == null) {
					JOptionPane.showMessageDialog(RegistroCiudad.this, "La Fecha NO puede estar vacía", "Error Fecha" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				JOptionPane.showMessageDialog(RegistroCiudad.this, "Ciudad registrada correctamente!\nNombre: " + nomCiudad + "\nPaís: " + nomPais, "ÉXITO!" , JOptionPane.INFORMATION_MESSAGE);
				
				
				//LIMPIA CAMPOS
				textFieldNomCiudad.setText("");
				textFieldPais.setText("");
				textFieldAeropuerto.setText("");
				textAreaDesc.setText("");
				textFieldWeb.setText("");
				
			}
		});
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAceptar.setBounds(299, 409, 89, 23);
		getContentPane().add(btnAceptar);
		
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		getContentPane().add(btnCancelar);
		setVisible(true);
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancelar.setBounds(189, 409, 100, 23);
		getContentPane().add(btnCancelar);
		

	}
}
