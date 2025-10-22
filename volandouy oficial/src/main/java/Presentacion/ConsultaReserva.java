package Presentacion;

import Logica.DataReserva;
import java.awt.Font;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Ventana para consultar los detalles de una reserva.
 */
public class ConsultaReserva extends JInternalFrame {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor de la ventana de consulta de reserva.
   * 
   */
  public ConsultaReserva(DataReserva r) {
    super(titulo(r), true, true, true, true);
    setSize(520, 420);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    JTextArea area = new JTextArea();
    area.setEditable(false);
    area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    area.setText(formatear(r));

    setContentPane(new JScrollPane(area));
  }

  private static String titulo(DataReserva r) {
    try {
      return "Reserva #" + r.getIdReserva();
    } catch (Throwable t) {
      return "Reserva";
    }
  }

  private static String formatear(DataReserva r) {
    StringBuilder sb = new StringBuilder();
    try {
      sb.append(r.toString());
    } catch (Throwable t) {
      sb.append(String.valueOf(r));
    }
    // Mostrar costo total
    try {
      Float costo = r.getCostoTotal();
      if (costo != null) {
        sb.append("\n\nCosto total: $").append(String.format("%.2f", costo));
      }
    } catch (Throwable t) {
      JOptionPane.showMessageDialog(null,
          "Error al obtener el costo de la reserva: " + t.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
      // Ignorar si no se puede obtener el costo
    }
    // Pasajes (como texto)
    sb.append("\n\nPasajes:\n");
    sb.append(formatearPasajes(r));
    return sb.toString();
  }

  private static String formatearPasajes(DataReserva r) {
    try {
      // Busca un método getPasajes() en DataReserva
      java.lang.reflect.Method m = r.getClass().getMethod("getPasajes");
      Object value = m.invoke(r);

      if (value instanceof java.util.Collection<?> col) {
        if (col.isEmpty()) {
          return "(sin pasajes)";          
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Object p : col) {
          sb.append("  ").append(i++).append(") ").append(describirPasaje(p)).append("\n");
        }
        return sb.toString();
      }
      return "(pasajes no disponibles)";
    } catch (Exception e) {
      return "(pasajes no disponibles)";
    }
  }

  private static String describirPasaje(Object p) {
    // Intenta usar getNombre() + getApellido(); si no, usa toString()
    try {
      java.lang.reflect.Method gn = p.getClass().getMethod("getNombre");
      java.lang.reflect.Method ga = p.getClass().getMethod("getApellido");
      Object nom = gn.invoke(p);
      Object ape = ga.invoke(p);
      String n = nom == null ? "" : nom.toString().trim();
      String a = ape == null ? "" : ape.toString().trim();
      String full = (n + " " + a).trim();
      return full.isEmpty() ? String.valueOf(p) : full;
    } catch (Exception e) {
      return String.valueOf(p);
    }
  }

}