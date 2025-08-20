package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistroCategoria extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldCategoria;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroCategoria frame = new RegistroCategoria();
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
	public RegistroCategoria() {
		setTitle("Registrar Categoría");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100, 450, 171);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nombre de la Categoría:");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 28, 169, 21);
		getContentPane().add(lblNewLabel);
		
		textFieldCategoria = new JTextField();
		textFieldCategoria.setBounds(175, 29, 235, 20);
		getContentPane().add(textFieldCategoria);
		textFieldCategoria.setColumns(10);
		
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.setBounds(90, 80, 100, 23);
		getContentPane().add(btnCancelar);
		
		JButton btnAceptar = new JButton("ACEPTAR");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cat = textFieldCategoria.getText().trim();
				
				if(cat.isEmpty()) {
					JOptionPane.showMessageDialog(RegistroCategoria.this, "La Categoría NO puede estar vacía", "Error Categoría" , JOptionPane.ERROR_MESSAGE);
					return;
				}
			
				JOptionPane.showMessageDialog(RegistroCategoria.this, "Categoría registrada correctamente!\nNombre: " + cat, "ÉXITO!" , JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAceptar.setBackground(new Color(5, 250, 79));
		btnAceptar.setBounds(200, 80, 89, 23);
		getContentPane().add(btnAceptar);

	}
}
