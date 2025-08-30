package Presentacion;

import Logica.*;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

public class CompraPaqueteIF extends JInternalFrame {

    private final ISistema sistema;

    private JComboBox<ClienteItem> cboClientes;
    private JDateChooser dcFechaCompra;
    private JTextField txtCosto;

    private JTable tblPaquetes;
    private DefaultTableModel paquetesModel;

    private JButton btnComprar, btnCancelar, btnRefrescar;

    public CompraPaqueteIF(ISistema sistema) {
        super("Compra de paquete", true, true, true, true);
        this.sistema = sistema;

        setSize(760, 540);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new MigLayout("insets 10, fill, gap 10", "[grow]", "[][][grow][]"));

        construirHeader();     // fila 0-1
        construirTabla();      // fila 2
        construirFooter();     // fila 3

        cargarClientes();
        cargarPaquetes();
    }

    // ------------------- UI -------------------

    private void construirHeader() {
        JPanel header = new JPanel(new MigLayout("insets 0, fillx, gapx 10, wrap 2", "[right][grow]"));

        JLabel label = new JLabel("Cliente:");
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        header.add(label);
        cboClientes = new JComboBox<>();
        cboClientes.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        cboClientes.setPrototypeDisplayValue(new ClienteItem("xxxxxxxxxx", "Nombre Apellido <email@dominio>"));
        header.add(cboClientes, "growx");

        JLabel label_1 = new JLabel("Fecha de compra:");
        label_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        header.add(label_1);
        dcFechaCompra = new JDateChooser();
        dcFechaCompra.setDate(new java.util.Date());
        header.add(dcFechaCompra, "growx");

        JLabel label_2 = new JLabel("Costo (opcional):");
        label_2.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        header.add(label_2);
        txtCosto = new JTextField();
        txtCosto.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        txtCosto.setToolTipText("Dejar vacío para usar el costo del paquete");
        header.add(txtCosto, "growx");

        btnRefrescar = new JButton("Refrescar listas");
        btnRefrescar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        btnRefrescar.addActionListener(e -> {
            cargarClientes();
            cargarPaquetes();
        });
        header.add(new JLabel());              // celda vacía para alinear
        header.add(btnRefrescar, "right");     // botón a la derecha

        getContentPane().add(header, "growx, span, wrap");
    }

    private void construirTabla() {
        paquetesModel = new DefaultTableModel(
                new Object[]{"Nombre", "Descripción", "Tipo asiento", "Cant. rutas", "Validez (días)", "Costo"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblPaquetes = new JTable(paquetesModel);
        tblPaquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPaquetes.setRowHeight(22);
        JScrollPane sp = new JScrollPane(tblPaquetes);

        getContentPane().add(sp, "grow, span, wrap");
    }

    private void construirFooter() {
        JPanel footer = new JPanel(new MigLayout("insets 0, fillx", "[grow][right]"));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        btnCancelar.addActionListener(e -> dispose());

        btnComprar = new JButton("Comprar");
        btnComprar.setBackground(Color.GREEN);
        btnComprar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        btnComprar.addActionListener(e -> onComprar());

        footer.add(btnCancelar, "left");
        footer.add(btnComprar, "right");

        getContentPane().add(footer, "growx, span");
    }

    // ------------------- DATA -------------------

    private void cargarClientes() {
        try {
            cboClientes.removeAllItems();
            List<DataCliente> clientes = sistema.listarClientesParaCompra();
            for (DataCliente dc : clientes) {
                String display = dc.getNombre() + " " + safe(dc.getApellido()) + " <" + safe(dc.getEmail()) + ">";
                cboClientes.addItem(new ClienteItem(dc.getNickname(), display));
            }
        } catch (Exception ex) {
            showError("No se pudieron cargar los clientes: " + ex.getMessage());
        }
    }

    private void cargarPaquetes() {
        try {
            paquetesModel.setRowCount(0);
            List<DataPaquete> paquetes = sistema.listarPaquetesDisponiblesParaCompra();
            for (DataPaquete p : paquetes) {
                paquetesModel.addRow(new Object[]{
                        p.getNombre(),
                        p.getDescripcion(),
                        (p.getTipoAsiento() == null ? "-" : p.getTipoAsiento().name()),
                        p.getCantRutas(),
                        p.getValidez(),
                        formatoMoneda(p.getCosto())
                });
            }
        } catch (Exception ex) {
            showError("No se pudieron cargar los paquetes: " + ex.getMessage());
        }
    }

    // ------------------- ACTION -------------------

    private void onComprar() {
        try {
            int row = tblPaquetes.getSelectedRow();
            if (row < 0) { showWarn("Seleccione un paquete de la tabla."); return; }

            ClienteItem selCliente = (ClienteItem) cboClientes.getSelectedItem();
            if (selCliente == null) { showWarn("Seleccione un cliente."); return; }

            java.util.Date fechaCompra = dcFechaCompra.getDate();
            if (fechaCompra == null) { showWarn("Seleccione la fecha de compra."); return; }

            String nombrePaquete = String.valueOf(paquetesModel.getValueAt(row, 0));
            String nickCliente   = selCliente.nickname();

            // Validación rápida (el Sistema igual valida)
            if (sistema.clienteYaComproPaquete(nickCliente, nombrePaquete)) {
                showWarn("Ese cliente ya compró este paquete.");
                return;
            }

            BigDecimal costoOverride = parseBigDecimalNullable(txtCosto.getText().trim());

            DataCompraPaquete dto = new DataCompraPaquete(
                    nombrePaquete,
                    nickCliente,
                    fechaCompra,
                    costoOverride,   // null => usa costo del paquete
                    null             // vencimiento => que lo calcule el manejador/sistema
            );

            sistema.comprarPaquete(dto);
            JOptionPane.showMessageDialog(this, "Compra registrada con éxito.", "OK", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (IllegalArgumentException ex) {
            showWarn(ex.getMessage());
        } catch (Exception ex) {
            showError("Error al registrar la compra: " + ex.getMessage());
        }
    }

    // ------------------- UTILS -------------------

    private String safe(String s) { return (s == null) ? "" : s; }

    private String formatoMoneda(java.math.BigDecimal bd) {
        if (bd == null) return "-";
        return NumberFormat.getNumberInstance().format(bd);
    }

    private BigDecimal parseBigDecimalNullable(String raw) {
        if (raw == null || raw.isBlank()) return null; // vacío = sin override

        String s = raw.trim()
                      .replace(" ", "")   // quita espacios
                      .replace(".", "")   // quita separador de miles
                      .replace(",", "."); // coma -> punto

        // permite números con hasta 2 decimales (ajustá si querés más)
        if (!s.matches("-?\\d+(\\.\\d{1,2})?")) {
            throw new IllegalArgumentException(
                "Costo inválido. Solo números (hasta 2 decimales). Ejemplos: 1500, 1500.50, 1.500,50"
            );
        }
        return new BigDecimal(s);
    }

    private void showWarn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Item del combo que guarda el nickname y muestra un texto lindo
    private record ClienteItem(String nickname, String display) {
        @Override public String toString() { return display; }
    }
}
