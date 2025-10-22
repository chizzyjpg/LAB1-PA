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
 * Ventana principal del sistema VolandoUy. Permite acceder a los registros,
 * consultas, modificaciones y compras del sistema.
 */
public class Menu extends JFrame {

  private static final long serialVersionUID = 1L;
  private JPanel contentPane;
  private JDesktopPane desktopPane;
  private final ISistema sistema = Fabrica.getInstance().getSistema(); // trabajá contra la interfaz

  /**
   * Método principal para lanzar la aplicación.
   * 
   * 
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
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
   * Crea la ventana principal y sus componentes.
   */
  public Menu() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1200, 900);

    CConexion con = new CConexion();
    con.getConexion();

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnNewMenu = new JMenu("Registros");
    menuBar.add(mnNewMenu);

    JMenuItem mntmNewMenuItem = new JMenuItem("Registar Usuario");
    mntmNewMenuItem.addActionListener(new ActionListener() {
      @Override
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

    JMenuItem mntmRegistrarCategoria = new JMenuItem("Registrar Categoría");
    mntmRegistrarCategoria.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        RegistroCategoria cat = new RegistroCategoria(sistema);
        desktopPane.add(cat);
        cat.setVisible(true);
        cat.toFront();
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(mntmRegistrarCategoria);

    JMenuItem mntmRegistrarCiudad = new JMenuItem("Registrar Ciudad");
    mntmRegistrarCiudad.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        RegistroCiudad nuevaCiudad = new RegistroCiudad(sistema);
        nuevaCiudad.setVisible(true);
        desktopPane.add(nuevaCiudad);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(mntmRegistrarCiudad);

    JMenuItem mntmRegistrarVuelo = new JMenuItem("Registrar Vuelo");
    mntmRegistrarVuelo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        RegistroVuelo nuevoVuelo = new RegistroVuelo(sistema);
        desktopPane.add(nuevoVuelo);
        desktopPane.revalidate();
        desktopPane.repaint();
        nuevoVuelo.setVisible(true);
      }
    });
    mnNewMenu.add(mntmRegistrarVuelo);

    JMenuItem mntmRegistrarRutaVuelo = new JMenuItem("Registrar Ruta de Vuelo");
    mntmRegistrarRutaVuelo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        RegistrarRutaVuelo rutaNueva = new RegistrarRutaVuelo(sistema);
        rutaNueva.setVisible(true);
        desktopPane.add(rutaNueva);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(mntmRegistrarRutaVuelo);

    JMenuItem mntmCrearPaqueteRutasVuelo = new JMenuItem("Crear Paquetes de Rutas de Vuelo");
    mntmCrearPaqueteRutasVuelo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        RegistroPaqueteRuta nuevoPaqRutasDeVuelo = new RegistroPaqueteRuta(sistema);
        nuevoPaqRutasDeVuelo.setVisible(true);
        desktopPane.add(nuevoPaqRutasDeVuelo);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(mntmCrearPaqueteRutasVuelo);

    JMenuItem mntmRegistrarRutasVueloEnPaquete = new JMenuItem("Registrar Rutas de Vuelo en Paquete");
    mntmRegistrarRutasVueloEnPaquete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        RegistroRutaVueloaPaquete nuevoRegRutasVueloaPaq = new RegistroRutaVueloaPaquete(sistema);
        nuevoRegRutasVueloaPaq.setVisible(true);
        desktopPane.add(nuevoRegRutasVueloaPaq);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnNewMenu.add(mntmRegistrarRutasVueloEnPaquete);

    JMenu mnConsultas = new JMenu("Consultas");
    menuBar.add(mnConsultas);

    JMenuItem mntmConsultaUsuario = new JMenuItem("Usuario");
    mntmConsultaUsuario.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ConsultaUsuario conUsu = new ConsultaUsuario(sistema);
        desktopPane.add(conUsu);
        desktopPane.revalidate();
        desktopPane.repaint();
        conUsu.setVisible(true);
      }
    });
    mnConsultas.add(mntmConsultaUsuario);

    JMenuItem mntmConsultaRutaVuelo = new JMenuItem("Ruta de Vuelo");
    mntmConsultaRutaVuelo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ConsultaRutaVuelo conRut = new ConsultaRutaVuelo(sistema);
        desktopPane.add(conRut);
        desktopPane.revalidate();
        desktopPane.repaint();
        conRut.setVisible(true);
      }
    });
    mnConsultas.add(mntmConsultaRutaVuelo);

    JMenuItem mntmConsultaVuelo = new JMenuItem("Vuelo");
    mntmConsultaVuelo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ConsultaVuelo conVuel = new ConsultaVuelo(sistema);
        desktopPane.add(conVuel);
        desktopPane.revalidate();
        desktopPane.repaint();
        conVuel.setVisible(true);
      }
    });
    mnConsultas.add(mntmConsultaVuelo);

    JMenuItem mntmConsultaPaquetesRutasVuelo = new JMenuItem("Paquetes de Rutas de Vuelo");
    mntmConsultaPaquetesRutasVuelo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ConsultaPaqRutasVuelo conPaq = new ConsultaPaqRutasVuelo(sistema);
        desktopPane.add(conPaq);
        desktopPane.revalidate();
        desktopPane.repaint();
        conPaq.setVisible(true);
      }
    });
    mnConsultas.add(mntmConsultaPaquetesRutasVuelo);

    JMenu mnModificaciones = new JMenu("Modificaciones");
    menuBar.add(mnModificaciones);

    JMenuItem mntmModificarDatosUsuario = new JMenuItem("Modificar Datos de Usuario");
    mnModificaciones.add(mntmModificarDatosUsuario);
    mntmModificarDatosUsuario.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ModificarUsuario frm = new ModificarUsuario(sistema);
        desktopPane.add(frm);
        desktopPane.revalidate();
        desktopPane.repaint();
        frm.setVisible(true);
        frm.toFront();
      }
    });

    JMenu mnReservasCompra = new JMenu("Reservas / Compra");
    menuBar.add(mnReservasCompra);

    JMenuItem mntmReservaVuelo = new JMenuItem("Reserva de Vuelo");
    mntmReservaVuelo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        RegistrarReservaVuelo nuevaResVuelo = new RegistrarReservaVuelo(sistema);
        nuevaResVuelo.setVisible(true);
        desktopPane.add(nuevaResVuelo);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnReservasCompra.add(mntmReservaVuelo);

    JMenuItem mntmCompraPaquete = new JMenuItem("Compra de Paquete");
    mntmCompraPaquete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CompraPaqueteIF compraPaquete = new CompraPaqueteIF(sistema);
        desktopPane.add(compraPaquete);
        desktopPane.revalidate();
        desktopPane.repaint();
        compraPaquete.setVisible(true);
        compraPaquete.toFront();
      }
    });
    mnReservasCompra.add(mntmCompraPaquete);

    JMenu mnAceptarRechazar = new JMenu("Aceptar / Rechazar");
    menuBar.add(mnAceptarRechazar);

    JMenuItem mntmAceptarRechazarRutaVuelo = new JMenuItem("Ruta de Vuelo");
    mntmAceptarRechazarRutaVuelo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AceptarRechazarRuta nuevaResVuelo = new AceptarRechazarRuta(sistema);
        nuevaResVuelo.setVisible(true);
        desktopPane.add(nuevaResVuelo);
        desktopPane.revalidate();
        desktopPane.repaint();
      }
    });
    mnAceptarRechazar.add(mntmAceptarRechazarRutaVuelo);

    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    desktopPane = new JDesktopPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(desktopPane, BorderLayout.CENTER);
    desktopPane.revalidate();
    desktopPane.repaint();
  }
}