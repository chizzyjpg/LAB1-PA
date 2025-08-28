package BD;

import java.sql.Connection;

import javax.swing.JOptionPane;

import Logica.JPAUtil;

public class CConexion {

	Connection con = null;
	
	String usuario = "postgres";
	String password = "1234";
	String bd = "pa2025";
	String ip = "postgres";
	String puerto = "5432";
	
	String cadena = "jdbc:postgresql://"+ip+":"+puerto+"/"+bd;
	
	public Connection getConexion() {
		try {
			Class.forName("org.postgresql.Driver");
			con = java.sql.DriverManager.getConnection(cadena, usuario, password);
			JOptionPane.showMessageDialog(null, "Conexion Exitosa");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error de Conexion "+e.getMessage());
		}
		
		return con;
	}
	
}
