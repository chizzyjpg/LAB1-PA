package Logica;

import java.sql.CallableStatement;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;



import BD.CConexion;
import jakarta.persistence.*;



@Entity
@Table(name = "Categoria")
public class Categoria{
	
	@Id
	private String nombre;

	@OneToMany(mappedBy = "categoria")
	private List<Ruta> rutas;
	
	public Categoria(){}
	
	public Categoria (String nombre){ this.nombre = nombre; }
	
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Categoria [nombre=" + nombre + "]";
	}
/*
 * 
	public void insertartCategoria (JTextField nombre) {
		setNombre(nombre.getText());
		
		CConexion con = new CConexion();
		
		String consulta = "INSERT INTO categoria (nombre) VALUES(?);";
		
		try {
			CallableStatement cs = con.getConexion().prepareCall(consulta) ;
			cs.setString(1, getNombre());
			
			cs.execute();
			
			JOptionPane.showMessageDialog(null,"Se inserto correctamente");
			
		}catch  (Exception e) {
			JOptionPane.showMessageDialog(null,"Error"+ e.toString());
		}
	}
 * */
}