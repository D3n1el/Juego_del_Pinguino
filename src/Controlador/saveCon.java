package Controlador;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class saveCon {
	
	private static Connection Conexion;
	private static String user;
	
	
	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		saveCon.user = user;
	}

	public static Connection getConexion() {
		return Conexion;
	}

	public static void setConexion(Connection conexion) {
		Conexion = conexion;
	}
	
}
