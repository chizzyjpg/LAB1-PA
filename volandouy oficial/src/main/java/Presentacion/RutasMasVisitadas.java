package Presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Logica.Ciudad;
import Logica.DataRutaMasVisitada;
import Logica.ISistema;

public class RutasMasVisitadas extends JInternalFrame {

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public RutasMasVisitadas(ISistema sistema) {
        // Título y propiedades de la ventana interna
        super("Rutas más visitadas", true, true, true, true);

        inicializarComponentes(sistema);
        cargarDatos(sistema);

        setSize(700, 300);
        setVisible(true);
    }

    private void inicializarComponentes(ISistema sistema) {
        // Modelo de la tabla: columnas fijas, filas se agregan en cargarDatos()
        modeloTabla = new DefaultTableModel(
                new Object[]{"#", "Aerolínea", "Origen", "Destino", "Visitas"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabla solo de lectura
            }
        };

        tabla = new JTable(modeloTabla);

        //tabla en el centro, botones abajo
        setLayout(new BorderLayout());
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botón para refrescar los datos
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos(sistema));

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.add(btnActualizar);

        add(panelSur, BorderLayout.SOUTH);
    }

    private void cargarDatos(ISistema sistema) {
        modeloTabla.setRowCount(0);

        try {
            List<DataRutaMasVisitada> lista = sistema.obtener5RutasMasVisitadas();
            int rank = 1;

            for (DataRutaMasVisitada d : lista) {
                String nickAerolinea = d.getNickAerolinea() != null ? d.getNickAerolinea() : "-";

                Ciudad origen = d.getCiudadOrigen();
                Ciudad destino = d.getCiudadDestino();

                String nombreOrigen = (origen != null) ? origen.getNombre() : "-";
                String nombreDestino = (destino != null) ? destino.getNombre() : "-";

                modeloTabla.addRow(new Object[]{
                        rank++,               // #
                        nickAerolinea,        // Aerolínea (nickname)
                        nombreOrigen,         // Origen
                        nombreDestino,        // Destino
                        d.getVisitas()        // Visitas
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
