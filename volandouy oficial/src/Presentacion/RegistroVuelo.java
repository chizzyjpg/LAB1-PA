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

import Logica.DataAerolinea;
import Logica.DataRuta;
import Logica.DataVueloEspecifico;
import Logica.ISistema;



public class RegistroVuelo extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textNombreVuelo;
	private JTextField textDuracion;
	private JTextField textCantTurista;
	private JTextField textMaxEjecutivo;
	private JComboBox<DataAerolinea> comboAerolinea;
	private final static ISistema sistema = Logica.Fabrica.getInstance().getSistema();     // trabajá contra la interfaz
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroVuelo frame = new RegistroVuelo(sistema);
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
	public RegistroVuelo(ISistema sistema) {
		getContentPane().setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setTitle("Registro de Vuelo");
		setBounds(100, 100, 629, 567);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabelAerolinea = new JLabel("Aerolínea");
		lblNewLabelAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelAerolinea.setBounds(291, 0, 69, 14);
		getContentPane().add(lblNewLabelAerolinea);
		
		comboAerolinea = new JComboBox<>();
		comboAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboAerolinea.setBounds(147, 22, 339, 22);

		// cargar
		for (DataAerolinea da : sistema.listarAerolineas()) {
		    comboAerolinea.addItem(da);
		}
		getContentPane().add(comboAerolinea);

		
		JLabel lblNewLabelRutaVuelo = new JLabel("Ruta de Vuelo");
		lblNewLabelRutaVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelRutaVuelo.setBounds(280, 58, 69, 14);
		getContentPane().add(lblNewLabelRutaVuelo);
		
		JComboBox comboBoxRutaVuelo = new JComboBox();
		comboBoxRutaVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBoxRutaVuelo.setBounds(147, 83, 339, 22);
		for (DataRuta da : sistema.listarPorAerolinea(((DataAerolinea)comboAerolinea.getSelectedItem()).getNickname())) {
		    comboBoxRutaVuelo.addItem(da);
		}
		lblNewLabelRutaVuelo.setLabelFor(comboBoxRutaVuelo);
		getContentPane().add(comboBoxRutaVuelo);
		
		JLabel lblNewLabelVuelo = new JLabel("Nombre del Vuelo");
		lblNewLabelVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelVuelo.setBounds(271, 116, 89, 14);
		getContentPane().add(lblNewLabelVuelo);
		
		textNombreVuelo = new JTextField();
		textNombreVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textNombreVuelo.setBounds(147, 141, 339, 20);
		lblNewLabelVuelo.setLabelFor(textNombreVuelo);
		getContentPane().add(textNombreVuelo);
		textNombreVuelo.setColumns(10);
		
		JLabel lblNewLabelFechaVuelo = new JLabel("Fecha de Vuelo");
		lblNewLabelFechaVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelFechaVuelo.setBounds(280, 172, 83, 14);
		getContentPane().add(lblNewLabelFechaVuelo);
		
		JDateChooser dateChooserFechaVuelo = new JDateChooser();
		dateChooserFechaVuelo.setBounds(147, 197, 339, 57);
		getContentPane().add(dateChooserFechaVuelo);
		
		JLabel lblNewLabelDuracion = new JLabel("Duración");
		lblNewLabelDuracion.setBounds(123, 283, 62, 17);
		getContentPane().add(lblNewLabelDuracion);
		lblNewLabelDuracion.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		
		textDuracion = new JTextField();
		textDuracion.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelDuracion.setLabelFor(textDuracion);
		textDuracion.setBounds(257, 281, 86, 20);
		getContentPane().add(textDuracion);
		textDuracion.setColumns(10);
		
		JLabel lblNewLabelCantTurista = new JLabel("cant.Turista");
		lblNewLabelCantTurista.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelCantTurista.setBounds(123, 323, 62, 14);
		getContentPane().add(lblNewLabelCantTurista);
		
		textCantTurista = new JTextField();
		textCantTurista.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelCantTurista.setLabelFor(textCantTurista);
		textCantTurista.setBounds(257, 321, 86, 20);
		getContentPane().add(textCantTurista);
		textCantTurista.setColumns(10);
		
		JLabel lblNewLabelMaxEjecutivos = new JLabel("Max. ejecutivos");
		lblNewLabelMaxEjecutivos.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelMaxEjecutivos.setBounds(123, 371, 83, 14);
		getContentPane().add(lblNewLabelMaxEjecutivos);
		
		textMaxEjecutivo = new JTextField();
		textMaxEjecutivo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelMaxEjecutivos.setLabelFor(textMaxEjecutivo);
		textMaxEjecutivo.setBounds(257, 369, 86, 20);
		getContentPane().add(textMaxEjecutivo);
		textMaxEjecutivo.setColumns(10);
		
		JLabel lblNewLabelFechaAlta = new JLabel("Fecha de Alta");
		lblNewLabelFechaAlta.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		lblNewLabelFechaAlta.setBounds(123, 426, 69, 14);
		getContentPane().add(lblNewLabelFechaAlta);
		
		JDateChooser dateChooserFechaAlta = new JDateChooser();
		lblNewLabelFechaAlta.setLabelFor(dateChooserFechaAlta);
		dateChooserFechaAlta.setBounds(273, 420, 70, 20);
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
		        textNombreVuelo.setText("");
		        textDuracion.setText("");
		        textCantTurista.setText("");
		        textMaxEjecutivo.setText("");

		        // cerrar la ventana
		        dispose();
		    }
		});

		getContentPane().add(btnCancelar);


		
		getContentPane().add(dateChooserFechaAlta);
		
		JButton btnNewButtonAceptar = new JButton("ACEPTAR");
		btnNewButtonAceptar.setBackground(Color.GREEN);
		btnNewButtonAceptar.setForeground(Color.BLACK);
		btnNewButtonAceptar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnNewButtonAceptar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Validar que todos los campos tengan texto
		        if (textNombreVuelo.getText().trim().isEmpty() ||
		            textDuracion.getText().trim().isEmpty() ||
		            textCantTurista.getText().trim().isEmpty() ||
		            textMaxEjecutivo.getText().trim().isEmpty()) {
		            
		            JOptionPane.showMessageDialog(null,
		                "Debe completar todos los campos.",
		                "Error",
		                JOptionPane.ERROR_MESSAGE);
		        } else {
		        	try {
		                // Intentar convertir los campos numéricos
		                int duracion = Integer.parseInt(textDuracion.getText().trim());
		                int cantTurista = Integer.parseInt(textCantTurista.getText().trim());
		                int maxEjecutivo = Integer.parseInt(textMaxEjecutivo.getText().trim());

		                // Validar que los números sean positivos
		                if (duracion <= 0 || cantTurista <= 0 || maxEjecutivo <= 0) {
		                    throw new NumberFormatException();
		                }

		                // Validar que las fechas no sean nulas
		                if (dateChooserFechaVuelo.getDate() == null || dateChooserFechaAlta.getDate() == null) {
		                    JOptionPane.showMessageDialog(null,
		                        "Debe seleccionar ambas fechas.",
		                        "Error",
		                        JOptionPane.ERROR_MESSAGE);
		                    return;
		                }
		                
		                // Registrar el vuelo
		                DataVueloEspecifico vueloData = new DataVueloEspecifico(textNombreVuelo.getText().trim(),
		                        dateChooserFechaVuelo.getDate(),
		                        duracion,
		                        cantTurista,
		                        maxEjecutivo,
		                        dateChooserFechaAlta.getDate());
		                DataRuta rutaSeleccionada = (DataRuta) comboBoxRutaVuelo.getSelectedItem();
		                DataAerolinea aerolineaSeleccionada = (DataAerolinea) comboAerolinea.getSelectedItem();

		                 sistema.registrarVuelo(aerolineaSeleccionada.getNickname(), rutaSeleccionada.getNombre(), vueloData);
		                 
		                 JOptionPane.showMessageDialog(null,
		                         "¡Vuelo registrado con éxito!",
		                         "Éxito",
		                         JOptionPane.INFORMATION_MESSAGE);

		            } catch (NumberFormatException ex) {
		                JOptionPane.showMessageDialog(null,
		                    "Los campos de duración, cantidad de turista y máximo ejecutivo deben ser números enteros positivos.",
		                    "Error",
		                    JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            JOptionPane.showMessageDialog(null,
		                    "¡Vuelo registrado con éxito!",
		                    "Éxito",
		                    JOptionPane.INFORMATION_MESSAGE);

		                // Limpiar campos
		                textNombreVuelo.setText("");
		                textDuracion.setText("");
		                textCantTurista.setText("");
		                textMaxEjecutivo.setText("");
		                dateChooserFechaVuelo.setDate(null);
		                dateChooserFechaAlta.setDate(null);
		            }

		    }
		});

		btnNewButtonAceptar.setBounds(490, 491, 89, 23);
		getContentPane().add(btnNewButtonAceptar);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(RegistroVuelo.class.getResource("/imagenes/logoV.png")));
		lblNewLabel.setBounds(353, 245, 290, 268);
		getContentPane().add(lblNewLabel);

		
		comboAerolinea.setRenderer(new javax.swing.DefaultListCellRenderer() {
		    @Override
		    public java.awt.Component getListCellRendererComponent(
		            javax.swing.JList<?> list, Object value, int index,
		            boolean isSelected, boolean cellHasFocus) {
		        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        if (value instanceof DataAerolinea da) {
		            // Cambiá getNombre()/getNickname() por los getters reales que tengas
		            String nombre = da.getNombre();
		            String nick   = da.getNickname();
		            setText((nombre != null && !nombre.isBlank() ? nombre : nick) +
		                    (nick != null && !nick.isBlank() ? " (" + nick + ")" : ""));
		        }
		        return this;
		    }
		});

	}
}
