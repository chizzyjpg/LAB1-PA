package Presentacion;

import Logica.DataAerolinea;
import Logica.DataPaquete;
import Logica.DataRuta;
import Logica.ISistema;
import Logica.TipoAsiento;
import java.awt.Color;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Interfaz gráfica para registrar una ruta de vuelo a un paquete turístico.
 */
public class RegistroRutaVueloaPaquete extends JInternalFrame {

  private static final long serialVersionUID = 1L;

  private final ISistema sistema;

  private JComboBox<Item<String>> comboBoxPaquetes; // value = nombrePaquete
  private JComboBox<Item<String>> comboBoxAerolineas; // value = nicknameAerolinea
  private JComboBox<Item<String>> comboBoxRutas; // value = nombreRuta
  private JComboBox<TipoAsiento> comboBoxAsiento; // enum
  private JSpinner spinnerCupos;

  /**
   * Constructor de la interfaz.
   * 
   */
  public RegistroRutaVueloaPaquete(ISistema sistema) {
    super("Registro Ruta de Vuelo a Paquete", true, true, true, true);
    this.sistema = sistema;

    setBounds(100, 100, 520, 280);
    getContentPane().setLayout(null);

    JLabel lblPaquetes = new JLabel("Paquetes NO comprados:");
    lblPaquetes.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblPaquetes.setBounds(10, 11, 180, 21);
    getContentPane().add(lblPaquetes);

    comboBoxPaquetes = new JComboBox<>();
    comboBoxPaquetes.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    comboBoxPaquetes.setBounds(200, 10, 290, 22);
    getContentPane().add(comboBoxPaquetes);

    JLabel lblAerolneas = new JLabel("Aerolíneas:");
    lblAerolneas.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblAerolneas.setBounds(10, 43, 100, 21);
    getContentPane().add(lblAerolneas);

    comboBoxAerolineas = new JComboBox<>();
    comboBoxAerolineas.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    comboBoxAerolineas.setBounds(200, 44, 290, 22);
    comboBoxAerolineas.addActionListener(e -> cargarRutas());
    getContentPane().add(comboBoxAerolineas);

    JLabel lblRutasDeVuelo = new JLabel("Rutas de vuelo asociadas:");
    lblRutasDeVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblRutasDeVuelo.setBounds(10, 75, 200, 21);
    getContentPane().add(lblRutasDeVuelo);

    comboBoxRutas = new JComboBox<>();
    comboBoxRutas.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    comboBoxRutas.setBounds(200, 76, 290, 22);
    getContentPane().add(comboBoxRutas);

    JLabel lblCantidadDeCupos = new JLabel("Cantidad de cupos:");
    lblCantidadDeCupos.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblCantidadDeCupos.setBounds(10, 107, 160, 21);
    getContentPane().add(lblCantidadDeCupos);

    spinnerCupos = new JSpinner(new SpinnerNumberModel(1, 1, 5000, 1));
    spinnerCupos.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    spinnerCupos.setBounds(200, 109, 80, 20);
    getContentPane().add(spinnerCupos);

    JLabel lblTipoDeAsiento = new JLabel("Tipo de Asiento:");
    lblTipoDeAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblTipoDeAsiento.setBounds(10, 139, 130, 21);
    getContentPane().add(lblTipoDeAsiento);

    comboBoxAsiento = new JComboBox<>(new DefaultComboBoxModel<>(TipoAsiento.values()));
    comboBoxAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    comboBoxAsiento.setBounds(200, 140, 290, 22);
    getContentPane().add(comboBoxAsiento);

    JButton btnCancelar = new JButton("CANCELAR");
    btnCancelar.addActionListener(e -> dispose());
    btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    btnCancelar.setBackground(new Color(241, 43, 14));
    btnCancelar.setBounds(200, 192, 120, 25);
    getContentPane().add(btnCancelar);

    JButton btnGuardar = new JButton("ACEPTAR");
    btnGuardar.addActionListener(e -> onGuardar());
    btnGuardar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    btnGuardar.setBackground(new Color(5, 250, 79));
    btnGuardar.setBounds(370, 192, 120, 25);
    getContentPane().add(btnGuardar);

    // Precarga inicial
    comboBoxAerolineas.addActionListener(e -> cargarRutas());

    cargarPaquetes();
    cargarAerolineas();

    if (comboBoxAerolineas.getItemCount() > 0) {
      comboBoxAerolineas.setSelectedIndex(0);
      cargarRutas();
    }
    cargarRutas(); // en caso de que ya haya una aerolínea seleccionada

  }

  // ------------ CARGA DE DATOS UI ------------

  private void cargarPaquetes() {
    comboBoxPaquetes.removeAllItems();
    try {
      for (DataPaquete p : sistema.listarPaquetesSinCompras()) {
        comboBoxPaquetes
            .addItem(new Item<>(p.getNombre(), p.getNombre() + " — " + p.getDescripcion()));
      }
    } catch (Exception ex) {
      // No alertamos en constructor; si querés, logueá
      // o se puede usar LOGGER
      JOptionPane.showMessageDialog(this, "No se pudieron cargar paquetes: " + ex.getMessage());
    }
  }

  private void cargarAerolineas() {
    comboBoxAerolineas.removeAllItems();
    try {
      for (DataAerolinea a : sistema.listarAerolineas()) {
        comboBoxAerolineas
            .addItem(new Item<>(a.getNickname(), a.getNombre() + " (" + a.getNickname() + ")"));
      }
    } catch (Exception ex) {
      throw new RuntimeException("No se pudieron cargar aerolíneas: " + ex.getMessage());
      // idem, no molestamos al crear
    }
  }

  private void cargarRutas() {
    comboBoxRutas.removeAllItems();
    Item<String> aeroSel = (Item<String>) comboBoxAerolineas.getSelectedItem();
    if (aeroSel == null) {
      return;      
    }
    String nick = aeroSel.value();
    if (nick == null || nick.isBlank()) {
      return;      
    }
    try {
      for (DataRuta r : sistema.listarPorAerolinea(nick)) {
        comboBoxRutas.addItem(new Item<>(r.getNombre(), r.getNombre()));
      }
    } catch (Exception ex) {
      warn("No se pudieron cargar las rutas: " + ex.getMessage());
    }
  }

  // ------------ ACCIÓN GUARDAR ------------

  private void onGuardar() {
    Item<String> pSel = (Item<String>) comboBoxPaquetes.getSelectedItem();
    Item<String> aSel = (Item<String>) comboBoxAerolineas.getSelectedItem();
    Item<String> rSel = (Item<String>) comboBoxRutas.getSelectedItem();
    TipoAsiento tipo = (TipoAsiento) comboBoxAsiento.getSelectedItem();
    int cupos = (Integer) spinnerCupos.getValue();

    // Validaciones UI
    if (pSel == null) {
      warn("Debe seleccionar un paquete.");
      return;
    }
    if (aSel == null) {
      warn("Debe seleccionar una aerolínea.");
      return;
    }
    if (rSel == null) {
      warn("Debe seleccionar una ruta.");
      return;
    }
    if (tipo == null) {
      warn("Debe seleccionar un tipo de asiento.");
      return;
    }
    if (cupos <= 0) {
      warn("La cantidad de cupos debe ser mayor a cero.");
      return;
    }

    try {
      // Llamar a la lógica
      sistema.agregarRutaAPaquete(pSel.value(), // nombre del paquete
          aSel.value(), // nickname de aerolínea
          rSel.value(), // nombre de la ruta
          tipo, cupos // cantidad de cupos
      );

      JOptionPane.showMessageDialog(this,
          "Ruta agregada al paquete:\n" + "Paquete: " + pSel.display() + "\n" + "Aerolínea: "
              + aSel.display() + "\n" + "Ruta: " + rSel.display() + "\n" + "Cupos: " + cupos + "\n"
              + "Tipo de Asiento: " + tipo,
          "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
      dispose();

    } catch (IllegalArgumentException | IllegalStateException ex) {
      warn(ex.getMessage());
    } catch (Exception ex) {
      error("Error al registrar: " + ex.getMessage());
    }
  }

  // ------------ HELPERS ------------

  private void warn(String msg) {
    JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
  }

  private void error(String msg) {
    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /** Wrapper de combo: muestra display y guarda value real. */
  private static final class Item<T> {
    private final T value;
    private final String display;

    Item(T value, String display) {
      this.value = value;
      this.display = display;
    }

    T value() {
      return value;
    }

    String display() {
      return display;
    }

    @Override
    public String toString() {
      return display;
    }
  }
}
