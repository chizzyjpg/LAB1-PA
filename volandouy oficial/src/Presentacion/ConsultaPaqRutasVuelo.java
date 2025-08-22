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

public class ConsultaPaqRutasVuelo extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsultaPaqRutasVuelo frame = new ConsultaPaqRutasVuelo();
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
	public ConsultaPaqRutasVuelo() {
		setTitle("Consulta de Paquete de Rutas de Vuelo");
		setBounds(100, 100, 450, 158);
		getContentPane().setLayout(null);
		
		JLabel lblPaquetesRegistrados = new JLabel("Paquetes Registrados:");
		lblPaquetesRegistrados.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblPaquetesRegistrados.setBounds(10, 22, 144, 21);
		getContentPane().add(lblPaquetesRegistrados);
		
		JComboBox comboBoxAerolineas = new JComboBox();
		comboBoxAerolineas.setBounds(186, 23, 166, 22);
		getContentPane().add(comboBoxAerolineas);
		
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();  
			}
		});
		btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.setBounds(80, 73, 100, 25);
		getContentPane().add(btnCancelar);
		
		JButton btnGuardar = new JButton("ACEPTAR");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String paqueteSeleccionado = (String) comboBoxAerolineas.getSelectedItem();
				
				if (paqueteSeleccionado == null || paqueteSeleccionado.isEmpty()) {
					JOptionPane.showMessageDialog(ConsultaPaqRutasVuelo.this, "Debe seleccionar un paquete de rutas de vuelo.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//Pasar validacion
				JOptionPane.showMessageDialog(ConsultaPaqRutasVuelo.this, "Paquete de rutas de vuelo seleccionado: " + paqueteSeleccionado, "Información", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnGuardar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnGuardar.setBackground(new Color(5, 250, 79));
		btnGuardar.setBounds(191, 73, 100, 25);
		getContentPane().add(btnGuardar);

	}

}
