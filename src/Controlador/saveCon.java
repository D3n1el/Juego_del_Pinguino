package Controlador;
import java.sql.Connection;
public class saveCon {

	private static Connection Conexion;

	public static Connection getConexion() {
		return Conexion;
	}

	public static void setConexion(Connection conexion) {
		Conexion = conexion;
	}
	

	
}
