package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistroRutaVueloaPaquete extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroRutaVueloaPaquete frame = new RegistroRutaVueloaPaquete();
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
	public RegistroRutaVueloaPaquete() {
		setClosable(true);
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setTitle("Registro Ruta de Vuelo a Paquete");
		setBounds(100, 100, 465, 267);
		getContentPane().setLayout(null);
		
		JLabel lblPaquetes = new JLabel("Paquetes NO comprados:");
		lblPaquetes.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblPaquetes.setBounds(10, 11, 166, 21);
		getContentPane().add(lblPaquetes);
		
		JComboBox comboBoxPaquetes = new JComboBox();
		comboBoxPaquetes.setBounds(186, 10, 166, 22);
		getContentPane().add(comboBoxPaquetes);
		
		JLabel lblAerolneas = new JLabel("Aerolíneas:");
		lblAerolneas.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblAerolneas.setBounds(10, 43, 81, 21);
		getContentPane().add(lblAerolneas);
		
		JComboBox comboBoxAerolineas = new JComboBox();
		comboBoxAerolineas.setBounds(186, 44, 166, 22);
		getContentPane().add(comboBoxAerolineas);
		
		JLabel lblRutasDeVuelo = new JLabel("Rutas de vuelo asociadas:");
		lblRutasDeVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblRutasDeVuelo.setBounds(10, 75, 166, 21);
		getContentPane().add(lblRutasDeVuelo);
		
		JComboBox comboBoxRutas = new JComboBox();
		comboBoxRutas.setBounds(186, 76, 166, 22);
		getContentPane().add(comboBoxRutas);
		
		JLabel lblCantidadDeCupos = new JLabel("Cantidad de cupos:");
		lblCantidadDeCupos.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCantidadDeCupos.setBounds(10, 107, 166, 21);
		getContentPane().add(lblCantidadDeCupos);
		
		JSpinner spinnerCupos = new JSpinner();
		spinnerCupos.setBounds(186, 109, 30, 20);
		getContentPane().add(spinnerCupos);
		
		JLabel lblTipoDeAsiento = new JLabel("Tipo de Asiento:");
		lblTipoDeAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblTipoDeAsiento.setBounds(10, 139, 111, 21);
		getContentPane().add(lblTipoDeAsiento);
		
		JComboBox comboBoxAsiento = new JComboBox();
		comboBoxAsiento.setBounds(186, 140, 166, 22);
		getContentPane().add(comboBoxAsiento);
		comboBoxAsiento.addItem("Turista");
		comboBoxAsiento.addItem("Ejecutivo");
		
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.setBounds(109, 192, 100, 25);
		getContentPane().add(btnCancelar);
		
		JButton btnGuardar = new JButton("ACEPTAR");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Validaciones
				String paqueteSeleccionado = (String) comboBoxPaquetes.getSelectedItem();
				String aerolineaSeleccionada = (String) comboBoxAerolineas.getSelectedItem();
				String rutaSeleccionada = (String) comboBoxRutas.getSelectedItem();
				int cantidadCupos = (Integer) spinnerCupos.getValue();
				String tipoAsientoSeleccionado = (String) comboBoxAsiento.getSelectedItem();
				
				if (paqueteSeleccionado == null || paqueteSeleccionado.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroRutaVueloaPaquete.this, "Debe seleccionar un paquete.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (aerolineaSeleccionada == null || aerolineaSeleccionada.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroRutaVueloaPaquete.this, "Debe seleccionar una aerolínea.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (rutaSeleccionada == null || rutaSeleccionada.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroRutaVueloaPaquete.this, "Debe seleccionar una ruta de vuelo.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (cantidadCupos <= 0) {
					JOptionPane.showMessageDialog(RegistroRutaVueloaPaquete.this, "La cantidad de cupos debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (tipoAsientoSeleccionado == null || tipoAsientoSeleccionado.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroRutaVueloaPaquete.this, "Debe seleccionar un tipo de asiento.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// Si todas las validaciones pasan, se puede proceder a guardar la información
				JOptionPane.showMessageDialog(RegistroRutaVueloaPaquete.this, "Ruta de vuelo a paquete registrada correctamente:\nPaquete: " + paqueteSeleccionado + "\nAerolínea: " + aerolineaSeleccionada + "\nRuta: " + rutaSeleccionada + "\nCupos: " + cantidadCupos + "\nTipo de Asiento: " + tipoAsientoSeleccionado, "ÉXITO!", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		btnGuardar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnGuardar.setBackground(new Color(5, 250, 79));
		btnGuardar.setBounds(220, 192, 100, 25);
		getContentPane().add(btnGuardar);

	}
}
