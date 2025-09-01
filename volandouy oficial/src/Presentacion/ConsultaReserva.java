package Presentacion;

import Logica.DataReserva;

import javax.swing.*;
import java.awt.*;

public class ConsultaReserva extends JInternalFrame {

    private static final long serialVersionUID = 1L;

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
        try { return "Reserva #" + r.getIdReserva(); } catch (Throwable t) { return "Reserva"; }
    }

    private static String formatear(DataReserva r) {
        // Si todavía no quieres mapear campo por campo, mostramos toString()
        // Para mapear, cambia este bloque por getters reales (cliente, fecha, asientos, etc.)
        try {
            return r.toString();
        } catch (Throwable t) {
            return String.valueOf(r);
        }
    }
}