package Presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.toedter.calendar.JDateChooser;

import Logica.ISistema;
import Logica.DataAerolinea;
import Logica.DataRuta;
import Logica.DataVueloEspecifico;
import Logica.DataCliente;
import Logica.DataPasaje;
import Logica.DataReserva;
import Logica.TipoAsiento;
import Logica.Equipaje;
import java.awt.Font;
import java.awt.Color;

@SuppressWarnings("serial")
public class RegistrarReservaVuelo extends JInternalFrame {

    // --------- Dependencia ---------
    private final ISistema sistema;

    // --------- Controles ---------
    private final JComboBox<DataAerolinea> comboAerolinea = new JComboBox<>();
    private final JComboBox<DataRuta> comboRuta = new JComboBox<>();
    private final JComboBox<DataVueloEspecifico> comboVuelo = new JComboBox<>();

    private final JComboBox<DataCliente> comboCliente = new JComboBox<>(); // DataCliente (DTO)
    private final JDateChooser dateChooser = new JDateChooser();

    private final JComboBox<TipoAsiento> comboTipoAsiento =
            new JComboBox<>(TipoAsiento.values()); // usar enum directo

    private final JComboBox<Equipaje> comboEquipaje =
            new JComboBox<>(Equipaje.values());    // usar enum directo

    private final JSpinner spinnerPasajes = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private final JSpinner spinnerEquipajeExtra = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

    private final JButton btnAceptar = new JButton("Registrar");
    private final JButton btnCancelar = new JButton("Cancelar");
    private final JTextArea areaDetalleVuelo = new JTextArea();

    // --------- Ctor ---------
    public RegistrarReservaVuelo(ISistema sistema) {
        super("Registrar Reserva de Vuelo", true, true, true, true);
        this.sistema = sistema;

        setSize(800, 543);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        armarUI();
        wireEvents();

        cargarAerolineas();
        cargarClientes();
    }

    // --------- UI ---------
    private void armarUI() {
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Selección de vuelo
        JLabel label = new JLabel("Aerolínea:");
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label);
        comboAerolinea.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboAerolinea);

        JLabel label_1 = new JLabel("Ruta:");
        label_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_1);
        comboRuta.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboRuta);

        JLabel label_2 = new JLabel("Vuelo:");
        label_2.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_2);
        comboVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboVuelo);

        // Datos de la reserva
        JLabel label_3 = new JLabel("Cliente:");
        label_3.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_3);
        comboCliente.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboCliente);

        JLabel label_4 = new JLabel("Fecha de reserva:");
        label_4.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_4);
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");
        form.add(dateChooser);

        JLabel label_5 = new JLabel("Tipo de asiento:");
        label_5.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_5);
        comboTipoAsiento.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboTipoAsiento);

        JLabel label_6 = new JLabel("Tipo de equipaje:");
        label_6.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_6);
        comboEquipaje.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(comboEquipaje);

        JLabel label_7 = new JLabel("Cantidad de pasajes:");
        label_7.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_7);
        spinnerPasajes.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(spinnerPasajes);

        JLabel label_8 = new JLabel("Equipaje extra (unid.):");
        label_8.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(label_8);
        spinnerEquipajeExtra.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
        form.add(spinnerEquipajeExtra);
        
     // === Panel DETALLE DE VUELO ===
        JPanel detalle = new JPanel(new BorderLayout(8, 8));
        detalle.setBorder(BorderFactory.createTitledBorder("Detalle del vuelo seleccionado"));

        areaDetalleVuelo.setEditable(false);
        areaDetalleVuelo.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        areaDetalleVuelo.setRows(8);
        detalle.add(new JScrollPane(areaDetalleVuelo), BorderLayout.CENTER);

        // Centro compuesto: form arriba, detalle abajo
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(form, BorderLayout.NORTH);
        center.add(detalle, BorderLayout.CENTER);

        // Botonera
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        south.add(btnCancelar);
        btnAceptar.setBackground(Color.GREEN);
        btnAceptar.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        south.add(btnAceptar);

        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);
    }
    
    

    // --------- Eventos ---------
    private void wireEvents() {
    	  comboAerolinea.addActionListener(e -> { cargarRutas();  actualizarDetalleVuelo(); });
    	  comboRuta.addActionListener(e -> { cargarVuelos();     actualizarDetalleVuelo(); });
    	  comboVuelo.addActionListener(e ->  actualizarDetalleVuelo());
    	  
    	  comboTipoAsiento.addActionListener(e -> actualizarDetalleVuelo());
    	  spinnerPasajes.addChangeListener(e -> actualizarDetalleVuelo());
    	  spinnerEquipajeExtra.addChangeListener(e -> actualizarDetalleVuelo());

        btnCancelar.addActionListener(e -> dispose());
        btnAceptar.addActionListener(e -> registrar());
    }

    // --------- Carga de datos ---------
    private void cargarAerolineas() {
        comboAerolinea.removeAllItems();
        for (DataAerolinea a : sistema.listarAerolineas()) {
            comboAerolinea.addItem(a);
        }
        cargarRutas();
    }

    private void cargarRutas() {
        comboRuta.removeAllItems();
        DataAerolinea a = (DataAerolinea) comboAerolinea.getSelectedItem();
        if (a == null) return;
        for (DataRuta r : sistema.listarPorAerolinea(a.getNickname())) {
            comboRuta.addItem(r);
        }
        cargarVuelos();
    }

    private void cargarVuelos() {
        comboVuelo.removeAllItems();
        DataAerolinea a = (DataAerolinea) comboAerolinea.getSelectedItem();
        DataRuta r = (DataRuta) comboRuta.getSelectedItem();
        if (a == null || r == null) return;
        for (DataVueloEspecifico v : sistema.listarVuelos(a.getNickname(), r.getNombre())) {
            comboVuelo.addItem(v);
        }
        if (comboVuelo.getItemCount() > 0) comboVuelo.setSelectedIndex(0);
        actualizarDetalleVuelo();
    }

    private void cargarClientes() {
        comboCliente.removeAllItems();
        java.util.List<DataCliente> clientes = sistema.listarClientes();
        System.out.println("Cantidad de clientes encontrados: " + (clientes != null ? clientes.size() : 0));
        if (clientes != null) {
            for (DataCliente dc : clientes) {
                System.out.println("Cliente: " + dc);
                comboCliente.addItem(dc);
            }
        }
    }

    // --------- Registrar ---------
    private void registrar() {
    	DataAerolinea a = (DataAerolinea) comboAerolinea.getSelectedItem();
        DataRuta r = (DataRuta) comboRuta.getSelectedItem();
        DataVueloEspecifico v = (DataVueloEspecifico) comboVuelo.getSelectedItem();
        DataCliente dc = (DataCliente) comboCliente.getSelectedItem();
        Date fecha = dateChooser.getDate();

        if (a == null || r == null || v == null || dc == null || fecha == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná Aerolínea, Ruta, Vuelo, Cliente y Fecha.",
                    "Faltan datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoAsiento tipoAsiento = (TipoAsiento) comboTipoAsiento.getSelectedItem();
        Equipaje equipaje = (Equipaje) comboEquipaje.getSelectedItem();
        int cantPasajes = (int) spinnerPasajes.getValue();
        int equipajeExtra = (int) spinnerEquipajeExtra.getValue();
        
     // Validar cupos disponibles antes de registrar
        int cuposDisponibles = 0;
        if (tipoAsiento == TipoAsiento.TURISTA) {
            cuposDisponibles = v.getMaxAsientosTur();
        } else if (tipoAsiento == TipoAsiento.EJECUTIVO) {
            cuposDisponibles = v.getMaxAsientosEjec();
        }
        // Sumar pasajes ya reservados para este vuelo y tipo de asiento
        int pasajesReservados = 0;
        java.util.List<DataReserva> reservasExistentes = sistema.listarReservas(a.getNickname(), r.getNombre(), v.getNombre());
        for (DataReserva res : reservasExistentes) {
            if (res.getTipoAsiento() == tipoAsiento && res.getPasajes() != null) {
                pasajesReservados += res.getPasajes().size();
            }
        }
        if (pasajesReservados + cantPasajes > cuposDisponibles) {
            JOptionPane.showMessageDialog(this,
                "No hay cupos suficientes para este tipo de asiento. Disponibles: " + (cuposDisponibles - pasajesReservados),
                "Cupos insuficientes", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cantPasajes < 1) {
            JOptionPane.showMessageDialog(this, "La cantidad de pasajes debe ser al menos 1.",
                    "Dato inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Calcular costo estimado
            float costoTotal = 0f;
            DataRuta ruta = r;
            if (ruta != null && tipoAsiento != null) {
                java.math.BigDecimal precioTur = safeBD(ruta.getCostoTurista());
                java.math.BigDecimal precioEje = safeBD(ruta.getCostoEjecutivo());
                java.math.BigDecimal unitExtra = java.math.BigDecimal.valueOf(ruta.getCostoEquipajeExtra());
                java.math.BigDecimal precioAsiento = (tipoAsiento == TipoAsiento.EJECUTIVO ? precioEje : precioTur);
                if (precioAsiento != null) {
                    java.math.BigDecimal subAsientos = precioAsiento.multiply(java.math.BigDecimal.valueOf(cantPasajes));
                    java.math.BigDecimal subExtra = unitExtra.multiply(java.math.BigDecimal.valueOf(equipajeExtra));
                    java.math.BigDecimal total = subAsientos.add(subExtra);
                    costoTotal = total.floatValue();
                }
            }

            // 1) armamos la reserva "base"
            DataReserva datos = new DataReserva(
                0,             // idReserva (lo asigna backend)
                fecha,
                tipoAsiento,
                equipaje,
                equipajeExtra,
                costoTotal,     // costo total calculado
                dc
            );
            
            String nomCliente = (dc != null) ? /* dc.getNombre() */ "" : "";
            String apeCliente = (dc != null) ? /* dc.getApellido() */ "" : "";
 
         // 2) pedir pasajeros
            PasajerosDialog dlg = new PasajerosDialog(
                    SwingUtilities.getWindowAncestor(this),
                    cantPasajes,
                    dc // DataCliente: se usa para prellenar la fila 0, pero es editable
            );
            dlg.setVisible(true);
            if (!dlg.isAccepted()) return;

            java.util.List<DataPasaje> pasajeros = dlg.getPasajeros();
            if (pasajeros.size() != cantPasajes) {
                JOptionPane.showMessageDialog(this,
                        "Se esperaban " + cantPasajes + " pasajeros pero se ingresaron " + pasajeros.size(),
                        "Cantidad inconsistente", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3) guardar la lista dentro del DTO
            datos.setPasajes(pasajeros);

            // registrar con TU método existente
            sistema.registrarReserva(
                    a.getNickname(),
                    r.getNombre(),
                    v.getNombre(),   // si usás código distinto, cambialo
                    datos
            );

            JOptionPane.showMessageDialog(this, "Reserva registrada con éxito");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar la reserva: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
 // =================== Diálogo para ingresar pasajeros (Nombre/Apellido) ===================
    private static final class PasajerosDialog extends JDialog {
        private final JTable table;
        private boolean accepted = false;

        PasajerosDialog(Window owner, int cantidad, Logica.DataCliente cliente) {
            super(owner, "Pasajeros (" + cantidad + ")", ModalityType.APPLICATION_MODAL);
            setSize(520, 300);
            setLocationRelativeTo(owner);

            String[] cols = {"Nombre", "Apellido"};
            Object[][] data = new Object[cantidad][cols.length];

            // prefill fila 0 con el cliente (editable)
            String nomCliente = (cliente != null && cliente.getNombre() != null)   ? cliente.getNombre().trim()   : "";
            String apeCliente = (cliente != null && cliente.getApellido() != null) ? cliente.getApellido().trim() : "";
            if (cantidad > 0) {
                data[0][0] = nomCliente;
                data[0][1] = apeCliente;
            }

            javax.swing.table.DefaultTableModel model =
                    new javax.swing.table.DefaultTableModel(data, cols) {
                        @Override public boolean isCellEditable(int r, int c) { return r != 0; }
                    };

            table = new JTable(model);
            // commit automático si se pierde el foco
            table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
            getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnCancel = new JButton("Cancelar");
            JButton btnOk     = new JButton("Aceptar");
            south.add(btnCancel); south.add(btnOk);
            getContentPane().add(south, BorderLayout.SOUTH);

            btnCancel.addActionListener(e -> { accepted = false; dispose(); });

            btnOk.addActionListener(e -> {
                // 1) Forzar commit de la celda en edición
                if (table.isEditing()) {
                    javax.swing.table.TableCellEditor ed = table.getCellEditor();
                    if (ed != null && !ed.stopCellEditing()) return; // si el editor no puede cerrar, aborta
                }

                // 2) Validación: nombre+apellido en todas las filas
                for (int i = 0; i < model.getRowCount(); i++) {
                    String nom = str(model.getValueAt(i, 0));
                    String ape = str(model.getValueAt(i, 1));
                    if (nom.isEmpty() || ape.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Completá nombre y apellido en la fila " + (i + 1),
                                "Faltan datos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                accepted = true;
                dispose();
            });
        }

        boolean isAccepted() { return accepted; }

        java.util.List<Logica.DataPasaje> getPasajeros() {
            java.util.List<Logica.DataPasaje> out = new java.util.ArrayList<>();
            javax.swing.table.TableModel m = table.getModel();
            for (int i = 0; i < m.getRowCount(); i++) {
                String nom = str(m.getValueAt(i, 0));
                String ape = str(m.getValueAt(i, 1));
                out.add(new Logica.DataPasaje(nom, ape));
            }
            return out;
        }

        private static String str(Object o){ return (o == null) ? "" : o.toString().trim(); }
    }
    
    private void actualizarDetalleVuelo() {
        DataAerolinea a = (DataAerolinea) comboAerolinea.getSelectedItem();
        DataRuta r      = (DataRuta) comboRuta.getSelectedItem();
        DataVueloEspecifico v = (DataVueloEspecifico) comboVuelo.getSelectedItem();

        StringBuilder sb = new StringBuilder();
        if (v != null) {
            sb.append(formatearVuelo(v)).append("\n");
        } else {
            sb.append("(Seleccione un vuelo)").append("\n");
        }

        if (r != null) {
            sb.append("\nRuta:\n").append(formatearRuta(r));
        }
        if (a != null) {
            String nom = safe(call(a, "getNombre"));
            String nick = safe(call(a, "getNickname"));
            sb.append("\nAerolínea: ").append(!nom.isEmpty()? nom : nick);
        }
        
        if (r != null) {
            TipoAsiento ta = (TipoAsiento) comboTipoAsiento.getSelectedItem();
            int nPas = (int) spinnerPasajes.getValue();
            int uExtra = (int) spinnerEquipajeExtra.getValue();

            String costoTxt = calcularCostoEstimado(r, ta, nPas, uExtra);
            if (!costoTxt.isBlank()) {
                sb.append("\n\n").append(costoTxt);
            }
        }

        areaDetalleVuelo.setText(sb.toString().trim());
        areaDetalleVuelo.setCaretPosition(0); // scrollear arriba
    }

    private String formatearVuelo(Object v) {
        // intentamos campos típicos; si no existen, toString()
        String nombre = safe(call(v, "getNombre"));
        String fecha  = safe(formatFecha(callObj(v, "getFecha")));
        String hora   = safe(formatHora(callObj(v, "getHora"), callObj(v, "getHoraSalida")));
        String cupos  = safe(numerito(call(v, "getCuposDisponibles"), call(v, "getCupos")));
        String avion  = numerito(call(v, "getAvion"), call(v, "getAeronave"));


        StringBuilder sb = new StringBuilder("Vuelo:\n");
        if (!nombre.isEmpty()) sb.append("  Nombre: ").append(nombre).append("\n");
        if (!fecha.isEmpty())  sb.append("  Fecha:  ").append(fecha).append("\n");
        if (!hora.isEmpty())   sb.append("  Hora:   ").append(hora).append("\n");
        if (!cupos.isEmpty())  sb.append("  Cupos:  ").append(cupos).append("\n");
        if (!avion.isEmpty())  sb.append("  Avión:  ").append(avion).append("\n");

        if (sb.toString().equals("Vuelo:\n")) {
            // no encontramos getters conocidos → fallback
            return String.valueOf(v);
        }
        return sb.toString().trim();
    }

    private String formatearRuta(Object r) {
        String nombre   = safe(call(r, "getNombre"));
        String desc     = safe(call(r, "getDescripcion"));
        String origen   = ciudad(safeObj(callObj(r, "getOrigen")));
        String destino  = ciudad(safeObj(callObj(r, "getDestino")));
        String hora     = safe(formatHora(callObj(r, "getHora"), null));
        String cTur     = safe(monetario(call(r, "getCostoTurista")));
        String cEje     = safe(monetario(call(r, "getCostoEjecutivo")));
        String equipEx  = safe(numerito(call(r, "getCostoEquipajeExtra")));

        StringBuilder sb = new StringBuilder();
        if (!nombre.isEmpty())  sb.append("  Nombre:   ").append(nombre).append("\n");
        if (!desc.isEmpty())    sb.append("  Descr.:   ").append(desc).append("\n");
        if (!origen.isEmpty())  sb.append("  Origen:   ").append(origen).append("\n");
        if (!destino.isEmpty()) sb.append("  Destino:  ").append(destino).append("\n");
        if (!hora.isEmpty())    sb.append("  Hora:     ").append(hora).append("\n");
        if (!cTur.isEmpty())    sb.append("  Turista:  ").append(cTur).append("\n");
        if (!cEje.isEmpty())    sb.append("  Ejecutivo:").append(cEje).append("\n");
        if (!equipEx.isEmpty()) sb.append("  Equipaje extra (U): ").append(equipEx).append("\n");

        String out = sb.toString().trim();
        return out.isEmpty() ? String.valueOf(r) : out;
    }

    // ---------- utilidades de reflexión/formatos ----------
    private static String call(Object obj, String method) {
        if (obj == null || method == null) return "";
        try {
            var m = obj.getClass().getMethod(method);
            Object val = m.invoke(obj);
            return val == null ? "" : val.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static Object callObj(Object obj, String method) {
        if (obj == null || method == null) return null;
        try {
            var m = obj.getClass().getMethod(method);
            return m.invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private static String safe(String s) { return (s == null) ? "" : s.trim(); }
    private static Object safeObj(Object o) { return o == null ? "" : o; }

    private static String ciudad(Object c) {
        if (c == null || c.toString().isBlank()) return "";
        String nom = call(c, "getNombre");
        String pais = call(c, "getPais");
        if (!nom.isBlank() || !pais.isBlank()) return (nom + (pais.isBlank() ? "" : ", " + pais)).trim();
        return c.toString();
    }

    private static String formatFecha(Object o) {
        if (!(o instanceof java.util.Date d)) return "";
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return f.format(d);
    }

    // acepta int HH o Integer HH o Date/LocalTime
    private static String formatHora(Object h1, Object h2) {
        Object h = (h1 != null) ? h1 : h2;
        if (h == null) return "";
        try {
            if (h instanceof Number n) {
                int hh = n.intValue();
                if (hh >= 0 && hh <= 23) return String.format("%02d:00", hh);
            }
            if (h instanceof java.util.Date d) {
                java.util.Calendar c = java.util.Calendar.getInstance();
                c.setTime(d);
                return String.format("%02d:%02d", c.get(java.util.Calendar.HOUR_OF_DAY), c.get(java.util.Calendar.MINUTE));
            }
            if (h.getClass().getName().equals("java.time.LocalTime")) {
                return h.toString(); //  HH:mm:ss
            }
        } catch (Exception ignored) {}
        return h.toString();
    }

    private static String numerito(String... vals) {
        for (String s : vals) if (s != null && !s.isBlank()) return s.trim();
        return "";
    }

    private static String monetario(String s) {
        if (s == null || s.isBlank()) return "";
        try {
            java.math.BigDecimal bd = new java.math.BigDecimal(s.trim());
            return bd.toPlainString();
        } catch (Exception e) {
            return s.trim();
        }
    }
    
    private String calcularCostoEstimado(DataRuta r, TipoAsiento tipo, int cantPasajes, int unidadesExtra) {
        // Precios base (pueden venir null, cuidamos eso)
        java.math.BigDecimal precioTur = safeBD(r.getCostoTurista());
        java.math.BigDecimal precioEje = safeBD(r.getCostoEjecutivo());
        java.math.BigDecimal unitExtra = java.math.BigDecimal.valueOf(r.getCostoEquipajeExtra());

        java.math.BigDecimal precioAsiento =
                (tipo == TipoAsiento.EJECUTIVO ? precioEje : precioTur);

        if (precioAsiento == null) return ""; // sin base no mostramos costo

        java.math.BigDecimal subAsientos = precioAsiento.multiply(java.math.BigDecimal.valueOf(cantPasajes));
        java.math.BigDecimal subExtra    = unitExtra.multiply(java.math.BigDecimal.valueOf(unidadesExtra));
        java.math.BigDecimal total       = subAsientos.add(subExtra);

        StringBuilder sb = new StringBuilder("Costo estimado:");
        sb.append("\n  Asiento (").append(tipo).append("): ").append(money(precioAsiento));
        sb.append("\n  Pasajes: ").append(cantPasajes);
        sb.append("\n  Subtotal asientos: ").append(money(subAsientos));
        sb.append("\n  Equipaje extra: ").append(unidadesExtra)
          .append("  x ").append(money(unitExtra))
          .append(" = ").append(money(subExtra));
        sb.append("\n  TOTAL: ").append(money(total));
        return sb.toString();
    }

    private static java.math.BigDecimal safeBD(Object o) {
        if (o == null) return null;
        if (o instanceof java.math.BigDecimal bd) return bd;
        try { return new java.math.BigDecimal(o.toString().trim()); }
        catch (Exception e) { return null; }
    }

    private static String money(java.math.BigDecimal bd) {
        if (bd == null) return "";
        // Formato simple, sin símbolo; cambialo si querés usar NumberFormat
        return bd.toPlainString();
    }


}