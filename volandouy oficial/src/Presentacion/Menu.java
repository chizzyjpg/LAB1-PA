package Presentacion;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDesktopPane;
import java.awt.BorderLayout;

import Logica.Fabrica;
import Logica.ISistema;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JDesktopPane desktopPane;
    private final ISistema sistema = Fabrica.getInstance().getSistema();     // trabajá contra la interfaz
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
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
	public Menu() {
        //this.sistema = new Sistema();   // una sola instancia para toda la app
        //initComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 907, 719);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Registros");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Reg. Usuario");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistroUsuario internal = new RegistroUsuario(sistema);
	            desktopPane.add(internal);
	            desktopPane.revalidate();
	            desktopPane.repaint();
	            internal.setVisible(true);
	            try {
	                internal.setSelected(true);
	            } catch (java.beans.PropertyVetoException ex) {
	                ex.printStackTrace();
	            }
	            
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Categoría ");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistroCategoria cat = new RegistroCategoria();
				cat.setVisible(true);
				desktopPane.add(cat);
				desktopPane.revalidate();
				desktopPane.repaint();

			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Reg. Ciudad");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistroCiudad nuevaCiudad = new RegistroCiudad();
				nuevaCiudad.setVisible(true);
				desktopPane.add(nuevaCiudad);
				desktopPane.revalidate();
				desktopPane.repaint();

			}
		});
		mnNewMenu.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Reg. Vuelo");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistroVuelo nuevoVuelo = new RegistroVuelo();
				nuevoVuelo.setVisible(true);
				desktopPane.add(nuevoVuelo);
				
			}
		});
		mnNewMenu.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Reg. Ruta de Vuelo");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrarRutaVuelo rutaNueva = new RegistrarRutaVuelo();
				rutaNueva.setVisible(true);
				desktopPane.add(rutaNueva);
				desktopPane.revalidate();
				desktopPane.repaint();

			}
		});
		mnNewMenu.add(mntmNewMenuItem_4);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Reg. Paq. Rutas de Vuelo");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // instanciás tu InternalFrame correcto
		        RegistroPaqueteRuta nuevoPaqRutasDeVuelo = new RegistroPaqueteRuta();
		        nuevoPaqRutasDeVuelo.setVisible(true);
		        desktopPane.add(nuevoPaqRutasDeVuelo);
		    }
		});
		mnNewMenu.add(mntmNewMenuItem_5);
		
		JMenuItem mntmNewMenuItem_13 = new JMenuItem("Reg. Rutas de Vuelo a Paq.");
		mntmNewMenuItem_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistroRutaVueloaPaquete nuevoRegRutasVueloaPaq = new RegistroRutaVueloaPaquete();
				nuevoRegRutasVueloaPaq.setVisible(true);
				desktopPane.add(nuevoRegRutasVueloaPaq);
				desktopPane.revalidate();
				desktopPane.repaint();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_13);
		
		JMenu mnNewMenu_1 = new JMenu("Consultas");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_6 = new JMenuItem("Usuario");
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultaUsuario ConUsu = new ConsultaUsuario(sistema);
				desktopPane.add(ConUsu);
				desktopPane.revalidate();
				desktopPane.repaint();
				ConUsu.setVisible(true);

			} 
		});
		
		mnNewMenu_1.add(mntmNewMenuItem_6);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Ruta de Vuelo");
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultaRutaVuelo ConRut = new ConsultaRutaVuelo();
				desktopPane.add(ConRut);
				desktopPane.revalidate();
				desktopPane.repaint();
				ConRut.setVisible(true);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_7);
//		mntmNewMenuItem_7.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				ConsultaVuelo ConVue = new ConsultaVuelo();
//				ConVue.setVisible(true);
//				desktopPane.add(ConVue);
//				desktopPane.revalidate();
//				desktopPane.repaint();
//
//			}
//		});
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("Vuelo");
		mnNewMenu_1.add(mntmNewMenuItem_8);
		
		JMenuItem mntmNewMenuItem_9 = new JMenuItem("Paq. de Rutas de Vuelo");
		mntmNewMenuItem_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultaPaqRutasVuelo ConPaq = new ConsultaPaqRutasVuelo();
				desktopPane.add(ConPaq);
				desktopPane.revalidate();
				desktopPane.repaint();
				ConPaq.setVisible(true);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_9);
		
		JMenu mnNewMenu_2 = new JMenu("Modificaciones");
		menuBar.add(mnNewMenu_2);
		
		JMenuItem mntmNewMenuItem_10 = new JMenuItem("Modificar Datos de Usuario");
		mnNewMenu_2.add(mntmNewMenuItem_10);
		mnNewMenu_2.add(mntmNewMenuItem_10);
		mntmNewMenuItem_10.addActionListener(e -> {
		    ModificarUsuario frm = new ModificarUsuario(sistema);
		    desktopPane.add(frm);
		    frm.setVisible(true);
		    frm.toFront();
		});
		
		JMenu mnNewMenu_3 = new JMenu("Reservas");
		menuBar.add(mnNewMenu_3);
		
		JMenuItem mntmNewMenuItem_11 = new JMenuItem("Vuelo");
		mnNewMenu_3.add(mntmNewMenuItem_11);
		
		JMenuItem mntmNewMenuItem_12 = new JMenuItem("Paquete");
		mnNewMenu_3.add(mntmNewMenuItem_12);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		desktopPane = new JDesktopPane();
		contentPane.setLayout(new BorderLayout()); //aseguro BorderLayout
		contentPane.add(desktopPane, BorderLayout.CENTER);
		desktopPane.revalidate();
		desktopPane.repaint();
	
	}

}
