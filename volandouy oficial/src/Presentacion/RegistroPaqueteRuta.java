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

public class RegistroPaqueteRuta extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_1;
	private final JButton btnNewButton = new JButton("ACEPTAR");

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
		getContentPane().setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setTitle("Registro Paquete Vuelo Ruta");
		setBounds(100, 100, 450, 452);
		getContentPane().setLayout(new MigLayout("", "[][grow][][grow]", "[][][grow][][][grow][]"));
		setBounds(100, 100, 450, 374);
		getContentPane().setLayout(new MigLayout("", "[][][][grow]", "[][][grow][][]"));
		
		JLabel lblNewLabel = new JLabel("Nombre:");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(lblNewLabel, "cell 1 1");
		
		textField = new JTextField();
		lblNewLabel.setLabelFor(textField);
		getContentPane().add(textField, "cell 3 1,growx");
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Descripción:");
		getContentPane().add(lblNewLabel_1, "cell 1 2");
		
		JTextArea textArea = new JTextArea();
		lblNewLabel_1.setLabelFor(textArea);
		getContentPane().add(textArea, "cell 3 2,grow");
		
		JLabel lblNewLabel_2 = new JLabel("Validez:");
		lblNewLabel_2.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(lblNewLabel_2, "cell 1 3");
		
		JSpinner spinner = new JSpinner();
		lblNewLabel_2.setLabelFor(spinner);
		getContentPane().add(spinner, "cell 3 3");
		
		JLabel lblNewLabel_3 = new JLabel("Descuento:");
		lblNewLabel_3.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(lblNewLabel_3, "cell 1 4");
		
		textField_1 = new JTextField();
		lblNewLabel_3.setLabelFor(textField_1);
		getContentPane().add(textField_1, "cell 3 4,growx");
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Fecha de Alta");
		lblNewLabel_4.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		getContentPane().add(lblNewLabel_4, "cell 1 5");
		
		JDateChooser dateChooser = new JDateChooser();
		lblNewLabel_4.setLabelFor(dateChooser);
		getContentPane().add(dateChooser, "cell 3 5,grow");
		btnNewButton.setBackground(Color.GREEN);
		btnNewButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnNewButton.setForeground(Color.BLACK);
		getContentPane().add(btnNewButton, "flowx,cell 3 6");
		
		JButton btnNewButton_1 = new JButton("CERRAR");
		btnNewButton_1.setBackground(Color.RED);
		btnNewButton_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnNewButton_1.setForeground(Color.BLACK);
		getContentPane().add(btnNewButton_1, "cell 3 6");

	}

}
