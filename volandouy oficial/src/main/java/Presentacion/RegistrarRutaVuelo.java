package Presentacion;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;
import javax.swing.SpinnerNumberModel;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListCellRenderer;

import Logica.DataAerolinea;
import Logica.DataCategoria;
import Logica.DataCiudad;
import Logica.DataRuta;
import Logica.ISistema;
import BD.CategoriaService;

public class RegistrarRutaVuelo extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JComboBox<DataAerolinea> comboAerolinea;
	private JTextField textFieldNombre;
	private JTextField textFieldTurista;
	private JTextField textFieldEjecutivo;
	private JTextField textFieldEquipajeExtra;
//	private JTextField textFieldCiudadOrigen;
//	private JTextField textFieldCiudadDestino;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					RegistrarRutaVuelo frame = new RegistrarRutaVuelo();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public RegistrarRutaVuelo(ISistema sistema) {
		getContentPane().setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		setClosable(true);
		setTitle("Registrar Ruta de Vuelo");
		setBounds(100, 100, 560, 523);
		getContentPane().setLayout(null);
		
		JLabel lblAerolinea = new JLabel("Aerolínea:");
		lblAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblAerolinea.setBounds(8, 25, 72, 14);
		getContentPane().add(lblAerolinea);
		
		comboAerolinea = new JComboBox<>();
		comboAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboAerolinea.setBounds(155, 22, 240, 22);
		for (DataAerolinea da : sistema.listarAerolineas()) comboAerolinea.addItem(da);
		getContentPane().add(comboAerolinea);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNombre.setBounds(8, 57, 72, 14);
		getContentPane().add(lblNombre);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textFieldNombre.setBounds(155, 58, 145, 20);
		getContentPane().add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		JLabel lblDesc = new JLabel("Descripción:");
		lblDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblDesc.setBounds(8, 89, 87, 20);
		getContentPane().add(lblDesc);
		
		JTextArea textAreaDesc = new JTextArea();
		textAreaDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textAreaDesc.setBounds(155, 91, 379, 78);
		getContentPane().add(textAreaDesc);
		
		JLabel lblHora = new JLabel("Hora:");
		lblHora.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblHora.setBounds(8, 183, 72, 14);
		getContentPane().add(lblHora);
		
		SpinnerDateModel model = new SpinnerDateModel();
		JSpinner spinnerHora = new JSpinner(new SpinnerDateModel(new Date(1755715138092L), null, null, Calendar.DAY_OF_MONTH));

		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerHora, "HH:mm");
		spinnerHora.setEditor(editor);

		spinnerHora.setBounds(155, 180, 62, 25);
		getContentPane().add(spinnerHora);
		
		JLabel lblCostoTurista = new JLabel("Costo Turista:");
		lblCostoTurista.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCostoTurista.setBounds(8, 219, 102, 14);
		getContentPane().add(lblCostoTurista);
		
		textFieldTurista = new JTextField();
		textFieldTurista.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textFieldTurista.setColumns(10);
		textFieldTurista.setBounds(155, 216, 145, 20);
		getContentPane().add(textFieldTurista);
		
		JLabel lblCostoEjecutivo = new JLabel("Costo Ejecutivo:");
		lblCostoEjecutivo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCostoEjecutivo.setBounds(8, 244, 116, 25);
		getContentPane().add(lblCostoEjecutivo);
		
		textFieldEjecutivo = new JTextField();
		textFieldEjecutivo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textFieldEjecutivo.setColumns(10);
		textFieldEjecutivo.setBounds(155, 244, 145, 20);
		getContentPane().add(textFieldEjecutivo);
		
		JLabel lblCostoEquipajeExtra = new JLabel("Costo Equipaje \r\nExtra:");
		lblCostoEquipajeExtra.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCostoEquipajeExtra.setBounds(8, 272, 145, 22);
		getContentPane().add(lblCostoEquipajeExtra);
		
		textFieldEquipajeExtra = new JTextField();
		textFieldEquipajeExtra.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		textFieldEquipajeExtra.setColumns(10);
		textFieldEquipajeExtra.setBounds(155, 275, 145, 20);
		getContentPane().add(textFieldEquipajeExtra);
		
		JLabel lblCiudadOrigen = new JLabel("Ciudad Origen:");
		lblCiudadOrigen.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCiudadOrigen.setBounds(8, 305, 102, 22);
		getContentPane().add(lblCiudadOrigen);
		
		JComboBox<DataCiudad> comboBoxCiudadOrigen = new JComboBox<>();
		comboBoxCiudadOrigen.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBoxCiudadOrigen.setBounds(155, 306, 240, 22);
		for (DataCiudad dcd : sistema.listarCiudades()) comboBoxCiudadOrigen.addItem(dcd);
		comboBoxCiudadOrigen.setRenderer(new DefaultListCellRenderer() {
		    @Override
		    public java.awt.Component getListCellRendererComponent(
		            javax.swing.JList<?> list, Object value, int index,
		            boolean isSelected, boolean cellHasFocus) {
		        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        if (value instanceof DataCiudad c) {
		            // Ajustá getters reales: getNombre(), getPais()
		            setText(c.getNombre() + " — " + c.getPais());
		        }
		        return this;
		    }
		});
		getContentPane().add(comboBoxCiudadOrigen);
		
		JLabel lblCiudadDestino = new JLabel("Ciudad Destino:");
		lblCiudadDestino.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCiudadDestino.setBounds(8, 341, 102, 22);
		getContentPane().add(lblCiudadDestino);
		
		JComboBox<DataCiudad> comboBoxCiudadDestino = new JComboBox<>();
		comboBoxCiudadDestino.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		comboBoxCiudadDestino.setBounds(155, 342, 240, 22);
		for (DataCiudad dcd : sistema.listarCiudades()) comboBoxCiudadDestino.addItem(dcd);
		comboBoxCiudadDestino.setRenderer(new javax.swing.DefaultListCellRenderer() {
		    @Override
		    public java.awt.Component getListCellRendererComponent(
		            javax.swing.JList<?> list, Object value, int index,
		            boolean isSelected, boolean cellHasFocus) {
		        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        if (value instanceof DataCiudad c) {
		            setText(c.getNombre() + " — " + c.getPais());
		        }
		        return this;
		    }
		});
		getContentPane().add(comboBoxCiudadDestino);
		
		JLabel lblFechaAlta = new JLabel("Fecha Alta:");
		lblFechaAlta.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblFechaAlta.setBounds(8, 380, 79, 14);
		getContentPane().add(lblFechaAlta);
		
		JDateChooser fechaAlta = new JDateChooser();
		fechaAlta.setBounds(155, 376, 145, 20);
		getContentPane().add(fechaAlta);
		
		JComboBox<DataCategoria> comboBoxCategoria = new JComboBox();
		comboBoxCategoria.setBounds(155, 405, 145, 22);
		// Cargar categorías desde la base de datos usando CategoriaService
        CategoriaService categoriaService = new CategoriaService();
        for (DataCategoria dc : categoriaService.listarCategorias()) comboBoxCategoria.addItem(dc);
        comboBoxCategoria.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        comboBoxCategoria.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DataCategoria c) {
                    setText(c.getNombre());
                }
                return this;
            }
        });
		
		getContentPane().add(comboBoxCategoria);
		
		JLabel lblCategoria = new JLabel("Categoría:");
		lblCategoria.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblCategoria.setBounds(8, 405, 72, 22);
		getContentPane().add(lblCategoria);
		
		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnCancelar.setBackground(new Color(241, 43, 14));
		btnCancelar.setBounds(155, 451, 100, 23);
		getContentPane().add(btnCancelar);
		
		JButton btnAceptar = new JButton("ACEPTAR");
		btnAceptar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	DataAerolinea aerolinea = (DataAerolinea) comboAerolinea.getSelectedItem();	
		    	String nombreAerolinea = (aerolinea != null) ? aerolinea.getNickname() : null;
		        String nombre = textFieldNombre.getText().trim();
		        String Descripcion = textAreaDesc.getText().trim();
		        String turista = textFieldTurista.getText().trim();
		        String ejecutivo = textFieldEjecutivo.getText().trim(); // hoy no lo usamos porque Ruta tiene un solo costo base
		        String costo = textFieldEquipajeExtra.getText().trim();
		        DataCiudad ciudadO = (DataCiudad) comboBoxCiudadOrigen.getSelectedItem();
		        DataCiudad ciudadD = (DataCiudad) comboBoxCiudadDestino.getSelectedItem();
		        Date fecha = fechaAlta.getDate();

		        // Validaciones mínimas (las tuyas)
		        if(nombre.isEmpty()) { JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "El Nombre NO puede estar vacío", "Error Nombre" , JOptionPane.ERROR_MESSAGE); return; }
		        if(Descripcion.isEmpty()) { JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Descripción NO puede estar vacía", "Error Descripción" , JOptionPane.ERROR_MESSAGE); return; }
		        if(turista.isEmpty()) { JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "El Costo Turista NO puede estar vacío", "Error Costo" , JOptionPane.ERROR_MESSAGE); return; }
		        if(ejecutivo.isEmpty()) { /* por ahora no lo usamos, pero dejo tu check */ JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "El Costo Ejecutivo NO puede estar vacío", "Error Costo" , JOptionPane.ERROR_MESSAGE); return; }
		        if(costo.isEmpty()) { JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "El Costo de Equipaje NO puede estar vacío", "Error Costo" , JOptionPane.ERROR_MESSAGE); return; }
		        if(ciudadO == null) { JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Ciudad Origen NO puede estar vacía", "Error Ciudad Origen" , JOptionPane.ERROR_MESSAGE); return; }
		        if(ciudadD == null) { JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Ciudad Destino NO puede estar vacía", "Error Ciudad Destino" , JOptionPane.ERROR_MESSAGE); return; }
		        if(fecha == null) { JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "La Fecha NO puede estar vacía", "Error Fecha" , JOptionPane.ERROR_MESSAGE); return; }

		        // Pedimos PAÍS (mínimo cambio en UI)
		        
		        // Parseos
		        int costoBase;
		        int costoEquipaje;
		        try {
		            costoBase = Integer.parseInt(turista);       // usamos "Turista" como costo base (tu entidad tiene 1 costo)
		            costoEquipaje = Integer.parseInt(costo);
		        } catch (NumberFormatException nfe) {
		            JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "Costos deben ser números enteros", "Error de formato" , JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        // Hora (tomamos HH:mm del spinner → a int HHmm o solo hora; tu entidad usa 'int hora')
		        Date h = (Date) ((JSpinner.DateEditor) spinnerHora.getEditor()).getModel().getValue();
		        Calendar cal = Calendar.getInstance();
		        cal.setTime(h);
		        int horaInt = cal.get(Calendar.HOUR_OF_DAY); // si querés HHmm, comentalo y armamos HH*100 + mm

		        try {
		            // Alta real usando el manejador (resuelve Ciudad por nombre+país)
		        	
		        	DataRuta datos = new DataRuta(
		        			nombre,
		        			Descripcion,
		        			ciudadO,// <-- fijate que tu manejador resuelve bien la ciudad
		        			ciudadD,// <-- fijate que tu manejador resuelve bien la ciudad
		        			horaInt,
		        			fecha,
		        			costoBase,
		        			costoEquipaje
		        			);
		        	
		        	sistema.RegistrarRuta(nombreAerolinea, datos);

		            JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "Ruta de Vuelo registrada correctamente!\nNombre: " + nombre, "ÉXITO!" , JOptionPane.INFORMATION_MESSAGE);

		            // limpiar
		            textFieldNombre.setText("");
		            textFieldTurista.setText("");
		            textFieldEjecutivo.setText("");
		            textFieldEquipajeExtra.setText("");
		            fechaAlta.setDate(null);

		        } catch (IllegalArgumentException ex2) {
		            JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, ex2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(RegistrarRutaVuelo.this, "Error al registrar ruta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		btnAceptar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		btnAceptar.setBackground(new Color(5, 250, 79));
		btnAceptar.setBounds(265, 451, 89, 23);
		getContentPane().add(btnAceptar);
		
		comboAerolinea.setRenderer(new DefaultListCellRenderer() {
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
