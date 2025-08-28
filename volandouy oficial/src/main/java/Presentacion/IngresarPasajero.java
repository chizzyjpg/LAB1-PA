package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Logica.ISistema;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class IngresarPasajero extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldNombre;
	private JTextField textFieldApellido;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IngresarPasajero frame = new IngresarPasajero();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public IngresarPasajero(ISistema sistema) {
		setIconifiable(true);
		setClosable(true);
		setTitle("Ingresar Pasajero");
		setBounds(100, 100, 450, 196);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Sexo:");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 11, 71, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblFemenino = new JLabel("Nombre:");
		lblFemenino.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblFemenino.setBounds(10, 36, 71, 14);
		getContentPane().add(lblFemenino);
		
		JRadioButton rdbtnMasculino = new JRadioButton("Masculino");
		rdbtnMasculino.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		rdbtnMasculino.setBounds(87, 9, 109, 23);
		getContentPane().add(rdbtnMasculino);
		
		JRadioButton rdbtnFemenino = new JRadioButton("Femenino");
		rdbtnFemenino.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		rdbtnFemenino.setBounds(198, 9, 109, 23);
		getContentPane().add(rdbtnFemenino);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(91, 35, 190, 20);
		getContentPane().add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblApellido.setBounds(10, 62, 71, 23);
		getContentPane().add(lblApellido);
		
		textFieldApellido = new JTextField();
		textFieldApellido.setColumns(10);
		textFieldApellido.setBounds(91, 61, 190, 20);
		getContentPane().add(textFieldApellido);
		
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.setBounds(70, 103, 100, 25);
		getContentPane().add(btnCancelar);
		
		JButton btnGuardar = new JButton("INGRESAR");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textFieldNombre.getText();
				String apellido = textFieldApellido.getText();
				String sexo = rdbtnFemenino.isSelected() ? "Femenino" : rdbtnMasculino.isSelected() ? "Masculino" : "";
				
				//Validaciones
				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(IngresarPasajero.this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				if(apellido.isEmpty()) {
					JOptionPane.showMessageDialog(IngresarPasajero.this, "El apellido no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				if(sexo.isEmpty()) {
					JOptionPane.showMessageDialog(IngresarPasajero.this, "Debe seleccionar un sexo.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				//ACA SE LLAMA A UNA FUNCION DEL SISTEMA PARA QUE VALIDE LOS DATOS y ESTA LLAMA AL MANEJADOR PARA QUE CREE LOS OBJETOS PASAJES, EL SISTEMA CREA LA RESERVA
				// Y GUARDA EN LA RESERVA LA LISTA DE PASAJES, O ALGO POR EL ESTILO
				
				//Si pasa las validaciones, se ingresa el pasajero
				JOptionPane.showMessageDialog(IngresarPasajero.this, "Pasajero ingresado:\nNombre: " + nombre + "\nApellido: " + apellido + "\nSexo: " + sexo, "Éxito", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnGuardar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnGuardar.setBackground(new Color(5, 250, 79));
		btnGuardar.setBounds(181, 103, 100, 25);
		getContentPane().add(btnGuardar);

	}
}
