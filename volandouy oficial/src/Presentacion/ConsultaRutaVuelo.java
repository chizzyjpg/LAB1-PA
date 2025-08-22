package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConsultaRutaVuelo extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsultaRutaVuelo frame = new ConsultaRutaVuelo();
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
	public ConsultaRutaVuelo() {
		setResizable(true);
		setIconifiable(true);
		setMaximizable(true);
		setClosable(true);
		setTitle("Consulta de Ruta de Vuelo");
		setBounds(100, 100, 415, 227);
		getContentPane().setLayout(null);
		
		JLabel lblAerolneas = new JLabel("Aerolíneas:");
		lblAerolneas.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblAerolneas.setBounds(10, 27, 81, 21);
		getContentPane().add(lblAerolneas);
		
		JComboBox comboBoxAerolineas = new JComboBox();
		comboBoxAerolineas.setBounds(186, 28, 166, 22);
		getContentPane().add(comboBoxAerolineas);
		
		JLabel lblRutasDeVuelo = new JLabel("Rutas de vuelo asociadas:");
		lblRutasDeVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblRutasDeVuelo.setBounds(10, 59, 166, 21);
		getContentPane().add(lblRutasDeVuelo);
		
		JComboBox comboBoxRutas = new JComboBox();
		comboBoxRutas.setBounds(186, 60, 166, 22);
		getContentPane().add(comboBoxRutas);
		
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.setBounds(85, 126, 100, 25);
		getContentPane().add(btnCancelar);
		
		JButton btnGuardar = new JButton("ACEPTAR");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Validaciones
				String aerolinea = (String) comboBoxAerolineas.getSelectedItem();
				String ruta = (String) comboBoxRutas.getSelectedItem();
				
				if(aerolinea == null || aerolinea.isEmpty()) {
					JOptionPane.showMessageDialog(ConsultaRutaVuelo.this, "Por favor, seleccione una aerolínea.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(ruta == null || ruta.isEmpty()) {
					JOptionPane.showMessageDialog(ConsultaRutaVuelo.this, "Por favor, seleccione una ruta de vuelo.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// Si todas las validaciones pasan, proceder con la acción
				System.out.println("Aerolínea seleccionada: " + aerolinea + ", Ruta de vuelo seleccionada: " + ruta);
				
			}
		});
		btnGuardar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnGuardar.setBackground(new Color(5, 250, 79));
		btnGuardar.setBounds(196, 126, 100, 25);
		getContentPane().add(btnGuardar);

	}

}
