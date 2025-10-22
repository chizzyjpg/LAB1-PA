package Presentacion;

import Logica.DataCiudad;
import Logica.ISistema;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * Clase para el registro de una ciudad en el sistema.
 */
public class RegistroCiudad extends JInternalFrame {

  private static final long serialVersionUID = 1L;
  private JTextField textFieldNomCiudad;
  private JTextField textFieldPais;
  private JTextField textFieldAeropuerto;
  private JTextField textFieldWeb;
  // private static ISistema sistema; // referencia al sistema


  /**
   * Create the frame.
   */
  public RegistroCiudad(ISistema sistema) {
    setClosable(true);
    setResizable(true);
    setMaximizable(true);
    setIconifiable(true);
    setTitle("Registrar Ciudad");
    setBounds(100, 100, 640, 473);
    getContentPane().setLayout(null);

    JLabel lblNombreCiudad = new JLabel("Nombre de la Ciudad:");
    lblNombreCiudad.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblNombreCiudad.setBounds(10, 43, 138, 14);
    getContentPane().add(lblNombreCiudad);

    textFieldNomCiudad = new JTextField();
    textFieldNomCiudad.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldNomCiudad.setBounds(154, 37, 460, 20);
    getContentPane().add(textFieldNomCiudad);
    textFieldNomCiudad.setColumns(10);

    JLabel lblPaisCiudad = new JLabel("País de la Ciudad:");
    lblPaisCiudad.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblPaisCiudad.setBounds(10, 71, 110, 14);
    getContentPane().add(lblPaisCiudad);

    textFieldPais = new JTextField();
    textFieldPais.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldPais.setColumns(10);
    textFieldPais.setBounds(154, 65, 460, 20);
    getContentPane().add(textFieldPais);

    JLabel lblAeropuertoAsociado = new JLabel("Aeropuerto asociado:");
    lblAeropuertoAsociado.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblAeropuertoAsociado.setBounds(10, 97, 138, 19);
    getContentPane().add(lblAeropuertoAsociado);

    textFieldAeropuerto = new JTextField();
    textFieldAeropuerto.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldAeropuerto.setColumns(10);
    textFieldAeropuerto.setBounds(154, 96, 460, 20);
    getContentPane().add(textFieldAeropuerto);

    JLabel lblDescripcion = new JLabel("Descripción:");
    lblDescripcion.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblDescripcion.setBounds(10, 127, 87, 20);
    getContentPane().add(lblDescripcion);

    JTextArea textAreaDesc = new JTextArea();
    textAreaDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textAreaDesc.setBounds(154, 127, 460, 78);
    getContentPane().add(textAreaDesc);

    JLabel lblSitioWeb = new JLabel("Sitio Web:");
    lblSitioWeb.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblSitioWeb.setBounds(10, 222, 70, 14);
    getContentPane().add(lblSitioWeb);

    textFieldWeb = new JTextField();
    textFieldWeb.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    textFieldWeb.setColumns(10);
    textFieldWeb.setBounds(154, 216, 460, 20);
    getContentPane().add(textFieldWeb);

    JLabel lblFechaAlta = new JLabel("Fecha Alta:");
    lblFechaAlta.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    lblFechaAlta.setBounds(10, 253, 79, 14);
    getContentPane().add(lblFechaAlta);

    JDateChooser fechaAlta = new JDateChooser();
    fechaAlta.setBounds(154, 247, 135, 20);
    fechaAlta.setDate(new Date());
    getContentPane().add(fechaAlta);

    JButton btnAceptar = new JButton("ACEPTAR");
    btnAceptar.setBackground(new Color(5, 250, 79));
    btnAceptar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String nomCiudad = textFieldNomCiudad.getText().trim();
        String nomPais = textFieldPais.getText().trim();
        String nomAeropuerto = textFieldAeropuerto.getText().trim();
        String descripcion = textAreaDesc.getText().trim();
        String web = textFieldWeb.getText().trim(); // hoy no se persiste en Ciudad
        Date fecha = fechaAlta.getDate();

        // Validaciones mínimas (dejé las tuyas)
        if (nomCiudad.isEmpty()) {
          JOptionPane.showMessageDialog(RegistroCiudad.this, "La Ciudad NO puede estar vacía",
              "Error Ciudad", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (nomPais.isEmpty()) {
          JOptionPane.showMessageDialog(RegistroCiudad.this, "El País NO puede estar vacío",
              "Error País", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (nomAeropuerto.isEmpty()) {
          JOptionPane.showMessageDialog(RegistroCiudad.this, "El Aeropuerto NO puede estar vacío",
              "Error Aeropuerto", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (descripcion.isEmpty()) {
          JOptionPane.showMessageDialog(RegistroCiudad.this, "La Descripción NO puede estar vacía",
              "Error Descripción", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (web.isEmpty()) {
          JOptionPane.showMessageDialog(RegistroCiudad.this, "El Sitio Web NO puede estar vacío",
              "Error Sitio Web", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (fecha == null) {
          JOptionPane.showMessageDialog(RegistroCiudad.this, "La Fecha NO puede estar vacía",
              "Error Fecha", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // Conectar con el manejador usando DTO
        try {
          DataCiudad dto = new DataCiudad(nomCiudad, nomPais, nomAeropuerto, descripcion, fecha,
              web);

          // Alta en memoria
          sistema.registrarCiudad(dto);
          // LIMPIA CAMPOS
          textFieldNomCiudad.setText("");
          textFieldPais.setText("");
          textFieldAeropuerto.setText("");
          textAreaDesc.setText("");
          textFieldWeb.setText("");
          fechaAlta.setDate(new Date());

        } catch (IllegalArgumentException ex) {
          // Errores de validación/duplicado del manejador
          JOptionPane.showMessageDialog(RegistroCiudad.this, ex.getMessage(), "Datos inválidos",
              JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(RegistroCiudad.this,
              "Error al registrar la ciudad: " + ex.getMessage(), "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    btnAceptar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    btnAceptar.setBounds(299, 409, 89, 23);
    getContentPane().add(btnAceptar);

    JButton btnCancelar = new JButton("CANCELAR");
    btnCancelar.setBackground(new Color(241, 43, 14));
    btnCancelar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    getContentPane().add(btnCancelar);
    setVisible(true);
    btnCancelar.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
    btnCancelar.setBounds(189, 409, 100, 23);
    getContentPane().add(btnCancelar);

  }
}