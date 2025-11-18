package Presentacion;

import BD.CConexion;
import Logica.Fabrica;
import Logica.ISistema;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


/**
 * Clase principal que representa el menú de la aplicación.
 */
public class Menu extends JFrame {

  private static final long serialVersionUID = 1L;
  private JPanel contentPane;
  private JDesktopPane desktopPane;
  private final ISistema sistema = Fabrica.getInstance().getSistema(); // trabajá contra la interfaz

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
    // initComponents();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1200, 900);

    CConexion con = new CConexion();
    con.getConexion();

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnNewMenu = new JMenu("Registros");
    menuBar.add(mnNewMenu);

    JMenuItem menuItemRegistrarUsuario = new JMenuItem("Registar Usuario");
    menuItemRegistrarUsuario.addActionListener(new ActionListener() {
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
    mnNewMenu.add(menuItemRegistrarUsuario);

    JMenuItem menuItemRegistrarCategoria = new JMenuItem("Registrar Categoría");
    menuItemRegistrarCategoria.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RegistroCategoria cat = new RegistroCategoria(sistema); // usa el campo del menú
        desktopPane.add(cat);
        cat.setVisible(true);
        cat.toFront();
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(menuItemRegistrarCategoria);

    JMenuItem menuItemRegistrarCiudad = new JMenuItem("Registrar Ciudad");
    menuItemRegistrarCiudad.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RegistroCiudad nuevaCiudad = new RegistroCiudad(sistema);
        nuevaCiudad.setVisible(true);
        desktopPane.add(nuevaCiudad);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(menuItemRegistrarCiudad);

    JMenuItem menuItemRegistrarVuelo = new JMenuItem("Registrar Vuelo");
    menuItemRegistrarVuelo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RegistroVuelo nuevoVuelo = new RegistroVuelo(sistema);
        desktopPane.add(nuevoVuelo);
        desktopPane.revalidate();
        desktopPane.repaint();
        nuevoVuelo.setVisible(true);
      }
    });
    mnNewMenu.add(menuItemRegistrarVuelo);

    JMenuItem menuItemRegistrarRutaVuelo = new JMenuItem("Registrar Ruta de Vuelo");
    menuItemRegistrarRutaVuelo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RegistrarRutaVuelo rutaNueva = new RegistrarRutaVuelo(sistema);
        rutaNueva.setVisible(true);
        desktopPane.add(rutaNueva);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(menuItemRegistrarRutaVuelo);

    JMenuItem menuItemCrearPaqueteRutas = new JMenuItem("Crear Paquetes de Rutas de Vuelo");
    menuItemCrearPaqueteRutas.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RegistroPaqueteRuta nuevoPaqRutasDeVuelo = new RegistroPaqueteRuta(sistema);
        nuevoPaqRutasDeVuelo.setVisible(true);
        desktopPane.add(nuevoPaqRutasDeVuelo);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(menuItemCrearPaqueteRutas);

    JMenuItem menuItemRegistrarRutasPaquete = new JMenuItem("Registrar Rutas de Vuelo en Paquete");
    menuItemRegistrarRutasPaquete.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RegistroRutaVueloaPaquete nuevoRegRutasVueloaPaq = new RegistroRutaVueloaPaquete(sistema);
        nuevoRegRutasVueloaPaq.setVisible(true);
        desktopPane.add(nuevoRegRutasVueloaPaq);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(menuItemRegistrarRutasPaquete);

    JMenu menuConsultas = new JMenu("Consultas");
    menuBar.add(menuConsultas);

    JMenuItem menuItemConsultaUsuario = new JMenuItem("Usuario");
    menuItemConsultaUsuario.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ConsultaUsuario conUsu = new ConsultaUsuario(sistema);
        desktopPane.add(conUsu);
        desktopPane.revalidate();
        desktopPane.repaint();
        conUsu.setVisible(true);
      }
    });
    menuConsultas.add(menuItemConsultaUsuario);

    JMenuItem menuItemConsultaRutaVuelo = new JMenuItem("Ruta de Vuelo");
    menuItemConsultaRutaVuelo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ConsultaRutaVuelo conRut = new ConsultaRutaVuelo(sistema);
        desktopPane.add(conRut);
        desktopPane.revalidate();
        desktopPane.repaint();
        conRut.setVisible(true);
      }
    });
    menuConsultas.add(menuItemConsultaRutaVuelo);

    JMenuItem menuItemConsultaVuelo = new JMenuItem("Vuelo");
    menuItemConsultaVuelo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ConsultaVuelo conVuel = new ConsultaVuelo(sistema);
        desktopPane.add(conVuel);
        desktopPane.revalidate();
        desktopPane.repaint();
        conVuel.setVisible(true);
      }
    });
    menuConsultas.add(menuItemConsultaVuelo);

    JMenuItem menuItemConsultaPaqueteRutas = new JMenuItem("Paquetes de Rutas de Vuelo");
    menuItemConsultaPaqueteRutas.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ConsultaPaqRutasVuelo conPaq = new ConsultaPaqRutasVuelo(sistema);
        desktopPane.add(conPaq);
        desktopPane.revalidate();
        desktopPane.repaint();
        conPaq.setVisible(true);
      }
    });
    menuConsultas.add(menuItemConsultaPaqueteRutas);

    JMenu menuModificaciones = new JMenu("Modificaciones");
    menuBar.add(menuModificaciones);

    JMenuItem menuItemModificarUsuario = new JMenuItem("Modificar Datos de Usuario");
    menuModificaciones.add(menuItemModificarUsuario);
    menuItemModificarUsuario.addActionListener(e -> {
      ModificarUsuario frm = new ModificarUsuario(sistema);
      desktopPane.add(frm);
      desktopPane.revalidate();
      desktopPane.repaint();
      frm.setVisible(true);
      frm.toFront();
    });

    JMenu menuReservasCompra = new JMenu("Reservas / Compra");
    menuBar.add(menuReservasCompra);

    JMenuItem menuItemReservaVuelo = new JMenuItem("Reserva de Vuelo");
    menuItemReservaVuelo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RegistrarReservaVuelo nuevaResVuelo = new RegistrarReservaVuelo(sistema);
        nuevaResVuelo.setVisible(true);
        desktopPane.add(nuevaResVuelo);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    menuReservasCompra.add(menuItemReservaVuelo);

    JMenuItem menuItemCompraPaquete = new JMenuItem("Compra de Paquete");
    menuReservasCompra.add(menuItemCompraPaquete);
    menuItemCompraPaquete.addActionListener(e -> {
      CompraPaqueteIF compraPaquete = new CompraPaqueteIF(sistema);
      desktopPane.add(compraPaquete);
      desktopPane.revalidate();
      desktopPane.repaint();
      compraPaquete.setVisible(true);
      compraPaquete.toFront();
    });

    JMenu menuAceptarRechazar = new JMenu("Aceptar / Rechazar");
    menuBar.add(menuAceptarRechazar);

    JMenuItem menuItemAceptarRechazarRuta = new JMenuItem("Ruta de Vuelo");
    menuItemAceptarRechazarRuta.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        AceptarRechazarRuta nuevaResVuelo = new AceptarRechazarRuta(sistema);
        nuevaResVuelo.setVisible(true);
        desktopPane.add(nuevaResVuelo);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    menuAceptarRechazar.add(menuItemAceptarRechazarRuta);

    JMenu menuVisitas = new JMenu("Visitas");
    menuBar.add(menuVisitas);

    JMenuItem menuItemVisitasRuta = new JMenuItem("Ruta de Vuelo");
    menuItemVisitasRuta.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            RutasMasVisitadas visitas = new RutasMasVisitadas(sistema);
            visitas.setVisible(true);
            desktopPane.add(visitas);
            desktopPane.revalidate();
            desktopPane.repaint();
        }
    });
    menuVisitas.add(menuItemVisitasRuta);

    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    desktopPane = new JDesktopPane();
    contentPane.setLayout(new BorderLayout()); // aseguro BorderLayout
    contentPane.add(desktopPane, BorderLayout.CENTER);
    desktopPane.revalidate();
    desktopPane.repaint();

  }
}