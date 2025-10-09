package BD;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class CConexion {

	static Connection con = null;
	
	static String usuario = "postgres";
	static String password = "admin";
	static String bd = "pa2025";
	static String ip = "localhost";
	static String puerto = "5432";
	
	static String cadena = "jdbc:postgresql://"+ip+":"+puerto+"/"+bd;
	
	public static Connection getConexion() {
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(cadena, usuario, password);
			JOptionPane.showMessageDialog(null, "Conexion Exitosa");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error de Conexion "+e.getMessage());
		}
		
		return con;
	}
	
}
