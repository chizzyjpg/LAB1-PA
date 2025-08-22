package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;



public class RegistroVuelo extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textNombre;
	private JTextField textDuracion;
	private JTextField textCantTurista;
	private JTextField textMaxEjecutivo;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroVuelo frame = new RegistroVuelo();
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
	public RegistroVuelo() {
		getContentPane().setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setTitle("Registro de Vuelo");
		setBounds(100, 100, 629, 567);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Aerolínea");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel.setBounds(291, 0, 69, 14);
		getContentPane().add(lblNewLabel);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBox.setBounds(147, 25, 339, 22);
		lblNewLabel.setLabelFor(comboBox);
		getContentPane().add(comboBox);
		
		JLabel lblNewLabel_1 = new JLabel("Ruta de Vuelo");
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_1.setBounds(280, 58, 69, 14);
		getContentPane().add(lblNewLabel_1);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBox_1.setBounds(147, 83, 339, 22);
		lblNewLabel_1.setLabelFor(comboBox_1);
		getContentPane().add(comboBox_1);
		
		JLabel lblNewLabel_2 = new JLabel("Nombre del Vuelo");
		lblNewLabel_2.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_2.setBounds(271, 116, 89, 14);
		getContentPane().add(lblNewLabel_2);
		
		textNombre = new JTextField();
		textNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textNombre.setBounds(147, 141, 339, 20);
		lblNewLabel_2.setLabelFor(textNombre);
		getContentPane().add(textNombre);
		textNombre.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Fecha de Vuelo");
		lblNewLabel_3.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_3.setBounds(280, 172, 83, 14);
		getContentPane().add(lblNewLabel_3);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(147, 197, 339, 57);
		getContentPane().add(dateChooser);
		
		JLabel lblNewLabel_4 = new JLabel("Duración");
		lblNewLabel_4.setBounds(123, 283, 62, 17);
		getContentPane().add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		
		textDuracion = new JTextField();
		textDuracion.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_4.setLabelFor(textDuracion);
		textDuracion.setBounds(257, 281, 86, 20);
		getContentPane().add(textDuracion);
		textDuracion.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("cant.Turista");
		lblNewLabel_5.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_5.setBounds(123, 323, 62, 14);
		getContentPane().add(lblNewLabel_5);
		
		textCantTurista = new JTextField();
		textCantTurista.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_5.setLabelFor(textCantTurista);
		textCantTurista.setBounds(257, 321, 86, 20);
		getContentPane().add(textCantTurista);
		textCantTurista.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("Max. ejecutivos");
		lblNewLabel_6.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_6.setBounds(123, 371, 83, 14);
		getContentPane().add(lblNewLabel_6);
		
		textMaxEjecutivo = new JTextField();
		textMaxEjecutivo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_6.setLabelFor(textMaxEjecutivo);
		textMaxEjecutivo.setBounds(257, 369, 86, 20);
		getContentPane().add(textMaxEjecutivo);
		textMaxEjecutivo.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("Fecha de Alta");
		lblNewLabel_7.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabel_7.setBounds(123, 426, 69, 14);
		getContentPane().add(lblNewLabel_7);
		
		JDateChooser dateChooser_1 = new JDateChooser();
		lblNewLabel_7.setLabelFor(dateChooser_1);
		dateChooser_1.setBounds(273, 420, 70, 20);
		// Botón Cancelar
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.setBackground(Color.RED);
		btnCancelar.setForeground(Color.BLACK);
		btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnCancelar.setBounds(380, 491, 100, 23);

		// Acción al hacer clic
		btnCancelar.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // limpiar los campos
		        textNombre.setText("");
		        textDuracion.setText("");
		        textCantTurista.setText("");
		        textMaxEjecutivo.setText("");

		        // cerrar la ventana
		        dispose();
		    }
		});

		getContentPane().add(btnCancelar);


		
		getContentPane().add(dateChooser_1);
		
		JButton btnNewButton_1 = new JButton("ACEPTAR");
		btnNewButton_1.setBackground(Color.GREEN);
		btnNewButton_1.setForeground(Color.BLACK);
		btnNewButton_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnNewButton_1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Validar que todos los campos tengan texto
		        if (textNombre.getText().trim().isEmpty() ||
		            textDuracion.getText().trim().isEmpty() ||
		            textCantTurista.getText().trim().isEmpty() ||
		            textMaxEjecutivo.getText().trim().isEmpty()) {
		            
		            JOptionPane.showMessageDialog(null,
		                "Debe completar todos los campos.",
		                "Error",
		                JOptionPane.ERROR_MESSAGE);
		        } else {
		            JOptionPane.showMessageDialog(null,
		                    "¡Vuelo registrado con éxito!",
		                    "Éxito",
		                    JOptionPane.INFORMATION_MESSAGE);

		                // Limpiar campos
		                textNombre.setText("");
		                textDuracion.setText("");
		                textCantTurista.setText("");
		                textMaxEjecutivo.setText("");
		                dateChooser.setDate(null);
		                dateChooser_1.setDate(null);
		            }

		    }
		});

		btnNewButton_1.setBounds(490, 491, 89, 23);
		getContentPane().add(btnNewButton_1);
		
		JLabel lblNewLabel_8 = new JLabel("New label");
		lblNewLabel_8.setIcon(new ImageIcon(RegistroVuelo.class.getResource("/imagenes/logoV.png")));
		lblNewLabel_8.setBounds(353, 245, 290, 268);
		getContentPane().add(lblNewLabel_8);


	}
}
