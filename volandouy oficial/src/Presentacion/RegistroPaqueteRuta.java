package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import java.awt.GridLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JSpinner;
import java.awt.Font;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistroPaqueteRuta extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_1;
	private final JButton brnAceptar = new JButton("ACEPTAR");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroPaqueteRuta frame = new RegistroPaqueteRuta();
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
	public RegistroPaqueteRuta() {
		setClosable(true);
		getContentPane().setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		setTitle("Registro Paquete de Rutas de Vuelo");
		setBounds(100, 100, 419, 345);
		getContentPane().setLayout(new MigLayout("", "[][grow][][grow]", "[][][grow][][][grow][]"));
		setBounds(100, 100, 450, 374);
		getContentPane().setLayout(new MigLayout("", "[][][][grow]", "[][][grow][][]"));
		
		JLabel lblNewLabel = new JLabel("Nombre:");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(lblNewLabel, "cell 1 1");
		
		textField = new JTextField();
		textField.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel.setLabelFor(textField);
		getContentPane().add(textField, "cell 3 1");
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Descripción:");
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(lblNewLabel_1, "cell 1 2");
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(textArea, "cell 3 2,grow");
		
		JLabel lblNewLabel_2 = new JLabel("Validez en días:");
		lblNewLabel_2.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(lblNewLabel_2, "cell 1 3");
		
		JSpinner spinner = new JSpinner();
		lblNewLabel_2.setLabelFor(spinner);
		getContentPane().add(spinner, "cell 3 3");
		
		JLabel lblNewLabel_3 = new JLabel("Descuento:");
		lblNewLabel_3.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(lblNewLabel_3, "cell 1 4");
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_3.setLabelFor(textField_1);
		getContentPane().add(textField_1, "cell 3 4");
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Fecha de Alta");
		lblNewLabel_4.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(lblNewLabel_4, "cell 1 5");
		
		JDateChooser dateChooser = new JDateChooser();
		lblNewLabel_4.setLabelFor(dateChooser);
		getContentPane().add(dateChooser, "cell 3 5,aligny center");
		
		JButton btnCerrar = new JButton("CERRAR");
		btnCerrar.addActionListener(e -> {
			dispose();
		});
			
		btnCerrar.setBackground(Color.RED);
		btnCerrar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnCerrar.setForeground(Color.BLACK);
		getContentPane().add(btnCerrar, "cell 3 6");
		brnAceptar.setBackground(Color.GREEN);
		brnAceptar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		brnAceptar.setForeground(Color.BLACK);
		getContentPane().add(brnAceptar, "cell 3 6");

	}

}
