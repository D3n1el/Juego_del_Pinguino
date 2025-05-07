package Controlador;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class saveCon {

	private static Connection connection;
    private static final String URL = "jdbc:oracle:thin:@oracle.ilerna.com:1521/XEPDB2";
    private static final String USER = "DW2425_PIN_GRUP03";
    private static final String PASS = "ACGMP03";

    public static Connection getConexion() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Configuración adicional para evitar el cierre prematuro
            connection = DriverManager.getConnection(URL, USER, PASS);
            connection.setAutoCommit(false); // Desactivar auto-commit por defecto
        }
        return connection;
    }

    public static void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit(); // Asegurar commit pendiente
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	
	
	
	
	/*private static Connection Conexion;

	public static Connection getConexion() {
		return Conexion;
	}

	public static void setConexion(Connection conexion) {
		Conexion = conexion;
	}
	*/

	
}
