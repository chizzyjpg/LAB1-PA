package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JButton;
import com.toedter.calendar.JDateChooser;

import Logica.ISistema;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ReservaVuelo extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField textField;

	/**
	 * Launch the application.
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReservaVuelo frame = new ReservaVuelo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	 */
	/**
	 * Create the frame.
	 */
	public ReservaVuelo(ISistema sistema) {
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setTitle("Reserva de Vuelo");
		setBounds(100, 100, 500, 440);
		getContentPane().setLayout(null);
		
		JLabel lblAerolneas = new JLabel("Aerolíneas:");
		lblAerolneas.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblAerolneas.setBounds(10, 26, 81, 21);
		getContentPane().add(lblAerolneas);
		
		JLabel lblRutasDeVuelo = new JLabel("Rutas de vuelo asociadas:");
		lblRutasDeVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblRutasDeVuelo.setBounds(10, 58, 166, 21);
		getContentPane().add(lblRutasDeVuelo);
		
		JComboBox comboBoxRutas = new JComboBox();
		comboBoxRutas.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBoxRutas.setBounds(186, 59, 166, 22);
		getContentPane().add(comboBoxRutas);
		
		JComboBox comboBoxAerolineas = new JComboBox();
		comboBoxAerolineas.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBoxAerolineas.setBounds(186, 27, 166, 22);
		getContentPane().add(comboBoxAerolineas);
		
		JLabel lblVuelosAsociados = new JLabel("Vuelos Asociados:");
		lblVuelosAsociados.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblVuelosAsociados.setBounds(10, 90, 113, 21);
		getContentPane().add(lblVuelosAsociados);
		
		JLabel lblCliente = new JLabel("Cliente:");
		lblCliente.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCliente.setBounds(10, 124, 166, 21);
		getContentPane().add(lblCliente);
		
		JComboBox comboBoxRutas_1 = new JComboBox();
		comboBoxRutas_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBoxRutas_1.setBounds(186, 123, 166, 22);
		getContentPane().add(comboBoxRutas_1);
		
		JComboBox comboBoxAerolineas_1 = new JComboBox();
		comboBoxAerolineas_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBoxAerolineas_1.setBounds(186, 91, 166, 22);
		getContentPane().add(comboBoxAerolineas_1);
		
		JLabel lblTipoDeAsiento = new JLabel("Tipo de Asiento:");
		lblTipoDeAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblTipoDeAsiento.setBounds(10, 161, 111, 21);
		getContentPane().add(lblTipoDeAsiento);
		
		JComboBox comboBoxAsiento = new JComboBox();
		comboBoxAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBoxAsiento.setBounds(186, 162, 166, 22);
		getContentPane().add(comboBoxAsiento);
		comboBoxAsiento.addItem("Turista");
		comboBoxAsiento.addItem("Ejecutivo");
		
		JLabel lblCantPasajes = new JLabel("Cant. Pasajes:");
		lblCantPasajes.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCantPasajes.setBounds(10, 193, 166, 21);
		getContentPane().add(lblCantPasajes);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(186, 195, 43, 20);
		getContentPane().add(spinner);
		
		JLabel lblEquipExtra = new JLabel("Equip. Extra:");
		lblEquipExtra.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblEquipExtra.setBounds(10, 225, 166, 21);
		getContentPane().add(lblEquipExtra);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setBounds(186, 227, 43, 20);
		getContentPane().add(spinner_1);
		
		JLabel lblIngresarPasajeros = new JLabel("Pasajeros:");
		lblIngresarPasajeros.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblIngresarPasajeros.setBounds(10, 257, 166, 21);
		getContentPane().add(lblIngresarPasajeros);
		
		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IngresarPasajero ingresarPasajero = new IngresarPasajero();
				ingresarPasajero.setVisible(true);
			}
		});
		btnIngresar.setBounds(186, 255, 89, 23);
		getContentPane().add(btnIngresar);
		
		JLabel lblFechaDeReserva = new JLabel("Fecha de Reserva:");
		lblFechaDeReserva.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblFechaDeReserva.setBounds(10, 289, 166, 21);
		getContentPane().add(lblFechaDeReserva);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(186, 290, 70, 20);
		getContentPane().add(dateChooser);
		
		JLabel lblCostoTotal = new JLabel("Costo Total:");
		lblCostoTotal.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCostoTotal.setBounds(10, 321, 166, 21);
		getContentPane().add(lblCostoTotal);
		
		textField = new JTextField();
		textField.setBounds(186, 321, 86, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnGuardar = new JButton("ACEPTAR");
		btnGuardar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnGuardar.setBackground(new Color(5, 250, 79));
		btnGuardar.setBounds(252, 353, 100, 25);
		getContentPane().add(btnGuardar);
		
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.setBounds(141, 353, 100, 25);
		getContentPane().add(btnCancelar);

	}
}
