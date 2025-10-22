package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 * Clase para manejar la conexion a la base de datos PostgreSQL
 */
public class CConexion {

  Connection con = null;

  String usuario = "postgres";
  String password = "admin";
  String bd = "pa2025";
  String ip = "localhost";
  String puerto = "5432";

  String cadena = "jdbc:postgresql://" + ip + ":" + puerto + "/" + bd;
  
  /**
   * Metodo para obtener la conexion a la base de datos.
   *
   */
  public Connection getConexion() {
    try {
      Class.forName("org.postgresql.Driver");
      con = DriverManager.getConnection(cadena, usuario, password);
      JOptionPane.showMessageDialog(null, "Conexion Exitosa");
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Error de Conexion " + e.getMessage());
    }

    return con;
  }

}
