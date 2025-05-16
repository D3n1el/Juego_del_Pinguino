package Controlador;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que gestiona y almacena información global en el juego.
 * Esta información es la conexión a la BD, los datos de cada usuario y las partidas que hayan en curso.
 * @author DAVO
 * @version 16/05/2025
 */
public class saveCon {
	
	private static Connection Conexion; 
	private static String user;
	private static int numPartida;
	
	/**
	 * @param numPartida Almacena el ID de la partida que se está ejecutando
	 */
	public static void setNumPartida(int numPartida) {
		saveCon.numPartida = numPartida;
	}
	
	public static int getNumPartida() {
		return numPartida;
	}
	
	/**
	 * @param user Guarda el nombre del usuario
	 */
	public static void setUser(String user) {
		saveCon.user = user;
	}
	
	public static String getUser() {
		return user;
	}
	
	/**
	 * @param Conexion Almacena la conexión activa a la base de datos
	 */
	public static void setConexion(Connection conexion) {
		Conexion = conexion;
	}
	
	public static Connection getConexion() {
		return Conexion;
	}
	
}
