package Presentacion;

import Logica.DataAerolinea;
import Logica.DataReserva;
import Logica.DataRuta;
import Logica.DataVueloEspecifico;
import Logica.ISistema;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.List;
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
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

/**
 * Interfaz gráfica para la consulta de vuelos, sus detalles y reservas asociadas.
 */
public class ConsultaVuelo extends JInternalFrame {

  private static final long serialVersionUID = 1L;

  // --- ctor sin args para WindowBuilder ---
  /*
   * Constructor por defecto utilizado por el diseñador de interfaces
   * 
   */
  public ConsultaVuelo() {
    this(null);
  }

  private final ISistema sistema;

  private final JComboBox<DataAerolinea> comboAerolineas = new JComboBox<>();
  private final JComboBox<DataRuta> comboRutas = new JComboBox<>();

  private final JList<DataVueloEspecifico> listVuelos = new JList<>(new DefaultListModel<>());
  private final JTextArea txtDetalleVuelo = new JTextArea(10, 40);

  private final JList<DataReserva> listReservas = new JList<>(new DefaultListModel<>());
  private final JButton btnVerReserva = new JButton("Ver reserva…");

  private final SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");

  // ==== Constructores ====
  /*
   * Constructor principal de la interfaz de consulta de vuelos.
   */
  public ConsultaVuelo(ISistema sistema) {
    super("Consulta de Vuelo", true, true, true, true);
    this.sistema = sistema;
    setSize(1000, 620);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    armarUI();
    wireEvents();

    if (this.sistema != null) { // evita NPE en el diseñador
      cargarAerolineas();
    }
  }

  // ==== UI ====
  private void armarUI() {
    JPanel root = new JPanel(new BorderLayout(10, 10));
    root.setBorder(new EmptyBorder(10, 10, 10, 10));
    setContentPane(root);

    // TOP: Aerolínea + Ruta (sin GridBag para evitar el warning de WB)
    JPanel top = new JPanel(new BorderLayout(8, 8));
    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));

    JLabel label = new JLabel("Aerolínea:");
    label.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    row.add(label);
    comboAerolineas.setRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof DataAerolinea a) {
          setText(a.getNickname() + " — " + a.getNombre());          
        }
        return this;
      }
    });
    row.add(comboAerolineas);

    JLabel lblRuta = new JLabel("Ruta:");
    lblRuta.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    row.add(lblRuta);
    comboRutas.setRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof DataRuta r) {
          setText(r.getNombre());          
        }
        return this;
      }
    });
    row.add(comboRutas);

    top.add(row, BorderLayout.CENTER);
    root.add(top, BorderLayout.NORTH);

    // CENTER: Vuelos (izq) + Detalle y Reservas (der)
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    split.setResizeWeight(0.30);

    // Izquierda: lista de vuelos
    JPanel left = new JPanel(new BorderLayout(6, 6));
    JLabel lblVuelos = new JLabel("Vuelos asociados a la ruta");
    lblVuelos.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    left.add(lblVuelos, BorderLayout.NORTH);
    listVuelos.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    listVuelos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listVuelos.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof DataVueloEspecifico v) {
          String f = (v.getFecha() != null) ? sdfFecha.format(v.getFecha()) : "-";
          setText(v.getNombre() + "  (" + f + ")");
        }
        return this;
      }
    });
    left.add(new JScrollPane(listVuelos), BorderLayout.CENTER);
    split.setLeftComponent(left);

    // Derecha: detalle del vuelo + reservas
    JPanel right = new JPanel(new BorderLayout(8, 8));

    JPanel detallePanel = new JPanel(new BorderLayout(6, 6));
    JLabel lblDetalle = new JLabel("Detalle del vuelo");
    lblDetalle.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    detallePanel.add(lblDetalle, BorderLayout.NORTH);
    txtDetalleVuelo.setEditable(false);
    txtDetalleVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
    detallePanel.add(new JScrollPane(txtDetalleVuelo), BorderLayout.CENTER);

    JPanel reservasPanel = new JPanel(new BorderLayout(6, 6));
    JLabel lblReservas = new JLabel("Reservas asociadas");
    lblReservas.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    reservasPanel.add(lblReservas, BorderLayout.NORTH);
    listReservas.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    listReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listReservas.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof DataReserva r) {
          String costo = (r.getCostoTotal() != null)
              ? String.format(" | Costo: $%.2f", r.getCostoTotal())
              : "";
          setText(r.toString() + costo);
        }
        return this;
      }
    });
    reservasPanel.add(new JScrollPane(listReservas), BorderLayout.CENTER);
    JPanel southRes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnVerReserva.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    southRes.add(btnVerReserva);
    reservasPanel.add(southRes, BorderLayout.SOUTH);

    right.add(detallePanel, BorderLayout.CENTER);
    right.add(reservasPanel, BorderLayout.SOUTH);

    split.setRightComponent(right);
    root.add(split, BorderLayout.CENTER);
  }

  private void wireEvents() {
    comboAerolineas.addActionListener(e -> cargarRutas());
    comboRutas.addActionListener(e -> cargarVuelos());
    listVuelos.addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        mostrarDetalleYReservas();        
      }
    });

    btnVerReserva.addActionListener(e -> verReservaSeleccionada());
  }

  // ==== Carga de datos ====
  private void cargarAerolineas() {
    comboAerolineas.removeAllItems();
    try {
      List<DataAerolinea> aas = sistema.listarAerolineas();
      for (DataAerolinea a : aas) {
        comboAerolineas.addItem(a);        
      }
      if (!aas.isEmpty()) {
        comboAerolineas.setSelectedIndex(0); // dispara cargarRutas()        
      }
    } catch (Exception ex) {
      error("Error cargando aerolíneas: " + ex.getMessage());
    }
  }

  private void cargarRutas() {
    comboRutas.removeAllItems();
    limpiarVuelosYDetalle();

    DataAerolinea a = (DataAerolinea) comboAerolineas.getSelectedItem();
    if (a == null) {
      return;      
    }
    try {
      List<DataRuta> rutas = sistema.listarPorAerolinea(a.getNickname());
      for (DataRuta r : rutas) {
        comboRutas.addItem(r);        
      }
      if (!rutas.isEmpty()) {
        comboRutas.setSelectedIndex(0); // dispara cargarVuelos()        
      }
    } catch (Exception ex) {
      error("Error cargando rutas: " + ex.getMessage());
    }
  }

  private void cargarVuelos() {
    DefaultListModel<DataVueloEspecifico> m = (DefaultListModel<DataVueloEspecifico>) listVuelos
        .getModel();
    m.clear();
    txtDetalleVuelo.setText("");
    ((DefaultListModel<DataReserva>) listReservas.getModel()).clear();

    DataAerolinea a = (DataAerolinea) comboAerolineas.getSelectedItem();
    DataRuta r = (DataRuta) comboRutas.getSelectedItem();
    if (a == null || r == null) {
      return;      
    }

    try {
      List<DataVueloEspecifico> vuelos = sistema.listarVuelos(a.getNickname(), r.getNombre());
      for (DataVueloEspecifico v : vuelos) {
        m.addElement(v);        
      }
      if (!vuelos.isEmpty()) {
        listVuelos.setSelectedIndex(0); // dispara mostrarDetalleYReservas()        
      }
    } catch (Exception ex) {
      error("Error cargando vuelos: " + ex.getMessage());
    }
  }

  private void mostrarDetalleYReservas() {
    DataVueloEspecifico v = listVuelos.getSelectedValue();
    DataAerolinea a = (DataAerolinea) comboAerolineas.getSelectedItem();
    DataRuta r = (DataRuta) comboRutas.getSelectedItem();

    if (v == null || a == null || r == null) {
      txtDetalleVuelo.setText("");
      ((DefaultListModel<DataReserva>) listReservas.getModel()).clear();
      return;
    }

    txtDetalleVuelo.setText(formatVuelo(v));

    DefaultListModel<DataReserva> mr = (DefaultListModel<DataReserva>) listReservas.getModel();
    mr.clear();
    try {
      List<DataReserva> reservas = sistema.listarReservas(a.getNickname(), r.getNombre(),
          v.getNombre());
      for (DataReserva res : reservas) {
        mr.addElement(res);        
      }
    } catch (Exception ex) {
      error("Error cargando reservas: " + ex.getMessage());
    }
  }

  // ==== Helpers ====
  private void limpiarVuelosYDetalle() {
    ((DefaultListModel<DataVueloEspecifico>) listVuelos.getModel()).clear();
    ((DefaultListModel<DataReserva>) listReservas.getModel()).clear();
    txtDetalleVuelo.setText("");
  }

  private String errorSafe(String s) {
    return (s == null || s.isEmpty()) ? "-" : s;
  }

  private void error(String msg) {
    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
  }

  private String formatVuelo(DataVueloEspecifico v) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String fVuelo = v.getFecha() != null ? sdf.format(v.getFecha()) : "-";
    String fAlta = v.getFechaAlta() != null ? sdf.format(v.getFechaAlta()) : "-";

    return """
        Nombre: %s
        Fecha: %s
        Duración: %d min
        Asientos Turista: %d
        Asientos Ejecutivo: %d
        Fecha alta: %s
        """.formatted(errorSafe(v.getNombre()), fVuelo, v.getDuracion(), v.getMaxAsientosTur(),
        v.getMaxAsientosEjec(), fAlta);
  }

  private void verReservaSeleccionada() {
    DataReserva sel = listReservas.getSelectedValue();
    if (sel == null) {
      JOptionPane.showMessageDialog(this, "Seleccioná una reserva", "Atención",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    DataAerolinea a = (DataAerolinea) comboAerolineas.getSelectedItem();
    DataRuta r = (DataRuta) comboRutas.getSelectedItem();
    DataVueloEspecifico v = listVuelos.getSelectedValue();

    if (a == null || r == null || v == null) {
      JOptionPane.showMessageDialog(this, "Falta aerolínea/ruta/vuelo seleccionado.", "Atención",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // Si DataReserva tiene getIdReserva(), usamos el ID para traer el detalle
    // actualizado
    DataReserva detalle;
    try {
      int id = sel.getIdReserva();
      detalle = sistema.buscarReserva(a.getNickname(), r.getNombre(), v.getNombre(), id);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "No se pudo cargar la reserva:\n" + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Abrir el IF de detalle
    JDesktopPane dp = getDesktopPane();
    if (dp == null) {
      JOptionPane.showMessageDialog(this, "No se encontró el escritorio (JDesktopPane).", "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    ConsultaReserva frame = new ConsultaReserva(detalle); // ver clase abajo
    dp.add(frame);
    frame.setVisible(true);
    frame.toFront();
  }
}