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

public class RegistroPaqueteRuta extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;

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

	}

}
