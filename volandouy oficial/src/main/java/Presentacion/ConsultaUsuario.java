package Presentacion;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import Logica.DataAerolinea;
import Logica.DataCliente;
import Logica.DataPaquete;
import Logica.DataReserva;
import Logica.DataRuta;
import Logica.DataUsuario;
import Logica.DataVueloEspecifico;
import Logica.ISistema;

/**
 * Interfaz gráfica para la consulta de información de usuarios (clientes y aerolíneas).
 */
public class ConsultaUsuario extends JInternalFrame {

  private final ISistema sistema;

  private final JComboBox<DataUsuario> comboUsuarios;
  private final JButton btnConsultar;
  private final JButton btnRefrescar;

  private final JTextArea areaDatos;
  private final JScrollPane scrollDatos;

  private final JPanel panelListas;
  private final JPanel panelRutas; // para Aerolínea
  private final JPanel panelCliente; // para Cliente (Reservas + Paquetes)

  private final DefaultListModel<Object> modelRutas = new DefaultListModel<>();
  private final JList<Object> lstRutas = new JList<>(modelRutas);
  private final JButton btnVerRuta = new JButton("Ver detalle");

  private final DefaultListModel<Object> modelReservas = new DefaultListModel<>();
  private final JList<Object> lstReservas = new JList<>(modelReservas);
  private final JButton btnVerVuelo = new JButton("Ver detalle");

  private final DefaultListModel<Object> modelPaquetes = new DefaultListModel<>();
  private final JList<Object> lstPaquetes = new JList<>(modelPaquetes);
  private final JButton btnVerPaquete = new JButton("Ver detalle");

  private boolean expandido = false;

  /** Constructor de la clase ConsultaUsuario.
   * 
   */
  public ConsultaUsuario(ISistema sistema) {
    super("Consulta de Usuario", true, true, true, true);
    this.sistema = sistema;

    setSize(700, 520);
    setMinimumSize(new Dimension(520, 160));
    getContentPane().setLayout(new BorderLayout());

    // --- Top ---
    JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
    JLabel label = new JLabel("Usuario:");
    label.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    panelTop.add(label);

    comboUsuarios = new JComboBox<>();
    comboUsuarios.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    comboUsuarios.setPreferredSize(new Dimension(320, 26));
    comboUsuarios.setRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof DataUsuario du) {
          boolean esCliente = du instanceof DataCliente;
          boolean esAero = du instanceof DataAerolinea;
          String tipo = esCliente ? "Cliente" : esAero ? "Aerolínea" : "Usuario";
          String nick = nvl(du.getNickname());
          String nombre = nvl(du.getNombre());
          String extra = (esCliente && ((DataCliente) du).getApellido() != null)
              ? " " + nvl(((DataCliente) du).getApellido())
              : "";
          setText(String.format("%s: %s — %s%s", tipo, nick, nombre, extra));
          setToolTipText(nvl(du.getEmail()));
        }
        return this;
      }
    });
    panelTop.add(comboUsuarios);

    btnConsultar = new JButton("Consultar");
    btnConsultar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    panelTop.add(btnConsultar);

    btnRefrescar = new JButton("Refrescar");
    btnRefrescar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    panelTop.add(btnRefrescar);

    getContentPane().add(panelTop, BorderLayout.NORTH);

    // --- Centro ---
    JPanel center = new JPanel();
    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

    areaDatos = new JTextArea(8, 60);
    areaDatos.setEditable(false);
    areaDatos.setLineWrap(true);
    areaDatos.setWrapStyleWord(true);
    areaDatos.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));

    scrollDatos = new JScrollPane(areaDatos);
    scrollDatos.setVisible(false);
    center.add(scrollDatos);

    panelListas = new JPanel(new GridLayout(1, 2, 10, 10));
    panelListas.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    // Rutas (aerolínea)
    panelRutas = new JPanel(new BorderLayout(6, 6));
    panelRutas.setBorder(BorderFactory.createTitledBorder("Rutas de vuelo"));
    lstRutas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lstRutas.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof DataRuta dr) {
          setText("Ruta: " + nvl(dr.getNombre()));
        } else {
          setText(String.valueOf(value));
        }
        return this;
      }
    });
    panelRutas.add(new JScrollPane(lstRutas), BorderLayout.CENTER);
    JPanel pnlRutaBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pnlRutaBtn.add(btnVerRuta);
    panelRutas.add(pnlRutaBtn, BorderLayout.SOUTH);
    panelRutas.setVisible(false);

    // Cliente (reservas + paquetes)
    panelCliente = new JPanel(new GridLayout(1, 2, 8, 8));

    JPanel pnlReservas = new JPanel(new BorderLayout(6, 6));
    pnlReservas.setBorder(BorderFactory.createTitledBorder("Vuelos reservados"));
    lstReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lstReservas.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof DataReserva r) {
          String nickCli = (r.getNickCliente() == null) ? ""
              : nvl(r.getNickCliente().getNickname());
          setText("Reserva del cliente: " + nickCli);
        } else {
          setText(String.valueOf(value));
        }
        return this;
      }
    });
    pnlReservas.add(new JScrollPane(lstReservas), BorderLayout.CENTER);
    JPanel pnlResBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pnlResBtn.add(btnVerVuelo);
    pnlReservas.add(pnlResBtn, BorderLayout.SOUTH);

    JPanel pnlPaquetes = new JPanel(new BorderLayout(6, 6));
    pnlPaquetes.setBorder(BorderFactory.createTitledBorder("Paquetes comprados"));
    lstPaquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lstPaquetes.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof DataPaquete p) {
          setText("Paquete: " + nvl(p.getNombre()));
          setToolTipText(describirPaquete(p));
        } else {
          setText(String.valueOf(value));
        }
        return this;
      }
    });
    pnlPaquetes.add(new JScrollPane(lstPaquetes), BorderLayout.CENTER);
    JPanel pnlPaqBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pnlPaqBtn.add(btnVerPaquete);
    pnlPaquetes.add(pnlPaqBtn, BorderLayout.SOUTH);

    panelCliente.add(pnlReservas);
    panelCliente.add(pnlPaquetes);
    panelCliente.setVisible(false);

    panelListas.add(panelRutas);
    panelListas.add(panelCliente);
    panelListas.setVisible(false);

    center.add(panelListas);
    getContentPane().add(center, BorderLayout.CENTER);

    // --- Acciones ---
    btnConsultar.addActionListener(e -> consultarSeleccion());
    btnRefrescar.addActionListener(e -> cargarUsuariosEnCombo());

    lstRutas.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && lstRutas.getSelectedValue() != null) {
          abrirConsultaRuta(lstRutas.getSelectedValue());          
        }
      }
    });
    btnVerRuta.addActionListener(e -> {
      if (lstRutas.getSelectedValue() != null) {
        abrirConsultaRuta(lstRutas.getSelectedValue());
      } else {
        JOptionPane.showMessageDialog(this, "Seleccioná una ruta de vuelo.", "Atención",
            JOptionPane.INFORMATION_MESSAGE);
      }
    });

    lstReservas.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && lstReservas.getSelectedValue() != null) {
          abrirConsultaVuelo(lstReservas.getSelectedValue());          
        }
      }
    });
    btnVerVuelo.addActionListener(e -> {
      if (lstReservas.getSelectedValue() != null) {
        abrirConsultaVuelo(lstReservas.getSelectedValue());        
      } else {        
        JOptionPane.showMessageDialog(this, "Seleccioná una reserva de vuelo.", "Atención",
            JOptionPane.INFORMATION_MESSAGE);
      }
    });

    lstPaquetes.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && lstPaquetes.getSelectedValue() != null) {
          abrirConsultaPaquete(lstPaquetes.getSelectedValue());          
        }
      }
    });
    btnVerPaquete.addActionListener(e -> {
      if (lstPaquetes.getSelectedValue() != null) {
        abrirConsultaPaquete(lstPaquetes.getSelectedValue());        
      } else {
        JOptionPane.showMessageDialog(this, "Seleccioná un paquete comprado.", "Atención",
            JOptionPane.INFORMATION_MESSAGE);
      }
    });

    // --- Carga inicial ---
    cargarUsuariosEnCombo();
    SwingUtilities.invokeLater(() -> comboUsuarios.requestFocusInWindow());
  }

  // ------------------- DATA -------------------

  private void cargarUsuariosEnCombo() {
    try {
      comboUsuarios.removeAllItems();
      List<DataUsuario> usuarios = sistema.listarUsuarios();
      usuarios.sort(Comparator.comparing(u -> nvl(u.getNickname()), String.CASE_INSENSITIVE_ORDER));
      for (DataUsuario du : usuarios) {
        comboUsuarios.addItem(du);
      }

      boolean hay = !usuarios.isEmpty();
      btnConsultar.setEnabled(hay);
      if (!hay) {
        scrollDatos.setVisible(false);
        panelListas.setVisible(false);
        setSize(getWidth(), 180);
        expandido = false;
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "No se pudo listar usuarios:\n" + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  // ------------------- CONSULTAR -------------------

  private void consultarSeleccion() {
    DataUsuario du = (DataUsuario) comboUsuarios.getSelectedItem();
    // if (du == null) return;
    if (du == null) {
      JOptionPane.showMessageDialog(this, "Seleccioná un usuario", "Atención",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    String nick = nvl(du.getNickname());
    StringBuilder sb = new StringBuilder();

    // Cliente
    DataCliente dc = sistema.verInfoCliente(nick);
    if (dc != null) {
      sb.append("[CLIENTE]\n");
      sb.append("Nickname:           ").append(nvl(dc.getNickname())).append('\n');
      sb.append("Nombre:             ").append(nvl(dc.getNombre())).append('\n');
      sb.append("Apellido:           ").append(nvl(dc.getApellido())).append('\n');
      sb.append("Email:              ").append(nvl(dc.getEmail())).append('\n');
      sb.append("Fecha de nacimiento:").append(fmtFecha(dc.getFechaNac())).append('\n');
      sb.append("Nacionalidad:       ").append(nvl(dc.getNacionalidad())).append('\n');
      sb.append("Tipo/N° documento:  ")
          .append(dc.getTipoDocumento() == null ? "-" : dc.getTipoDocumento().name()).append(" ")
          .append(nvl(dc.getNumDocumento())).append('\n');

      mostrarDetalle(sb.toString());

      cargarReservasCliente(dc);
      cargarPaquetesCliente(dc);
      panelRutas.setVisible(false);
      panelCliente.setVisible(true);
      panelListas.setVisible(true);
      return;
    }

    // Aerolínea
    DataAerolinea da = sistema.verInfoAerolinea(nick);
    if (da != null) {
      sb.append("[AEROLÍNEA]\n");
      sb.append("Nickname:     ").append(nvl(da.getNickname())).append('\n');
      sb.append("Nombre:       ").append(nvl(da.getNombre())).append('\n');
      sb.append("Email:        ").append(nvl(da.getEmail())).append('\n');
      sb.append("Descripción:  ").append(nvl(da.getDescripcion())).append('\n');
      sb.append("Sitio web:    ").append(nvl(da.getSitioWeb())).append('\n');

      mostrarDetalle(sb.toString());

      cargarRutasAerol(da);
      panelCliente.setVisible(false);
      panelRutas.setVisible(true);
      panelListas.setVisible(true);
      return;
    }

    JOptionPane.showMessageDialog(this, "No se encontró el usuario.", "Aviso",
        JOptionPane.WARNING_MESSAGE);
  }

  private void mostrarDetalle(String texto) {
    areaDatos.setText(texto);
    scrollDatos.setVisible(true);
    if (!expandido) {
      expandido = true;
      setSize(getWidth(), 520);
      revalidate();
      repaint();
    }
  }

  // ------------------- CARGAS DE LISTAS -------------------

  private void cargarRutasAerol(DataAerolinea da) {
    modelRutas.clear();
    List<?> rutas = obtenerRutasDeAerolinea(da);
    for (Object o : rutas) {
      modelRutas.addElement(o);
    }
  }

  private void cargarReservasCliente(DataCliente dc) {
    modelReservas.clear();
    List<?> reservas = obtenerReservasDeCliente(dc);
    for (Object o : reservas) {
      modelReservas.addElement(o);
    }
  }

  private void cargarPaquetesCliente(DataCliente dc) {
    modelPaquetes.clear();
    List<?> paquetes = obtenerPaquetesDeCliente(dc);
    for (Object o : paquetes) {
      modelPaquetes.addElement(o);
    }
  }

  // ------------------- INTEGRACIONES (usando tus métodos) -------------------

  // Rutas de una aerolínea
  private List<?> obtenerRutasDeAerolinea(DataAerolinea da) {
    try {
      return sistema.listarPorAerolinea(da.getNickname());
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "No se pudieron listar rutas:\n" + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
      return java.util.Collections.emptyList();
    }
  }

  // Reservas del cliente (composición de métodos)
  private List<?> obtenerReservasDeCliente(DataCliente dc) {
    List<Object> result = new ArrayList<>();
    try {
      for (DataAerolinea aero : sistema.listarAerolineas()) {
        String nickAero = aero.getNickname();
        for (DataRuta ruta : (List<DataRuta>) sistema.listarPorAerolinea(nickAero)) {
          String nombreRuta = ruta.getNombre();
          for (DataVueloEspecifico v : sistema.listarVuelos(nickAero, nombreRuta)) {
            String codVuelo = v.getNombre(); // en tu modelo 'nombre' es el código
            for (DataReserva r : sistema.listarReservas(nickAero, nombreRuta, codVuelo)) {
              String nickReserva = (r.getNickCliente() == null) ? null
                  : r.getNickCliente().getNickname();
              if (nickReserva != null && dc.getNickname() != null
                  && nickReserva.equalsIgnoreCase(dc.getNickname())) {
                result.add(r);
              }
            }
          }
        }
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "No se pudieron listar reservas del cliente:\n" + ex.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
    return result;
  }

  // Paquetes comprados por el cliente
  private List<?> obtenerPaquetesDeCliente(DataCliente dc) {
    List<Object> result = new ArrayList<>();
    try {
      for (DataPaquete p : sistema.listarPaquetes()) {
        String nombrePaquete = p.getNombre();
        if (sistema.clienteYaComproPaquete(dc.getNickname(), nombrePaquete)) {
          result.add(p);
        }
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "No se pudieron listar paquetes del cliente:\n" + ex.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
    return result;
  }

  // ------------------- ABRIR FRAMES DE DETALLE -------------------
  @SuppressWarnings("PMD.UnusedFormalParameter")
  private void abrirConsultaRuta(Object dtoRuta) {
    try {
      JInternalFrame jf = new ConsultaRutaVuelo(sistema);
      agregarYMostrar(jf);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "No se pudo abrir Consulta de Ruta de Vuelo:\n" + ex.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  @SuppressWarnings("PMD.UnusedFormalParameter")
  private void abrirConsultaVuelo(Object dtoVuelo) {
    try {
      JInternalFrame jf = new ConsultaVuelo(sistema);
      agregarYMostrar(jf);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "No se pudo abrir Consulta de Vuelo:\n" + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  @SuppressWarnings("PMD.UnusedFormalParameter")
  private void abrirConsultaPaquete(Object dtoCompra) {
    try {
      JInternalFrame jf = new ConsultaPaqRutasVuelo(sistema);
      agregarYMostrar(jf);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "No se pudo abrir Consulta de Paquete de Rutas de Vuelo:\n" + ex.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void agregarYMostrar(JInternalFrame jf) {
    JDesktopPane desk = getDesktopPane();
    if (desk != null) {
      desk.add(jf);
      jf.setVisible(true);
      try {
        jf.setSelected(true);
      } catch (Exception ignored) {
      }
    } else {
      jf.setVisible(true);
    }
  }

  // ------------------- Utils -------------------

  private static String fmtFecha(java.util.Date d) {
    if (d == null) {
      return "-";      
    }
    return new SimpleDateFormat("dd/MM/yyyy").format(d);
  }

  private static String nvl(String s) {
    return s == null ? "" : s;
  }

  private static String describirPaquete(DataPaquete p) {
    String costo = (p.getCosto() == null) ? "-"
        : NumberFormat.getNumberInstance().format(p.getCosto());
    return "Costo: " + costo + " | Validez: " + p.getValidez() + " días";
  }
}