package Controlador;

import java.sql.*;

import Modelo.Tablero;

public class GestorPartidas {
	private String urlBDD;
	private String username;
	private String password;
	
	//Metodos
	public void guardarPartida(int idPartida, String estado) throws SQLException {
        Connection con = bbdd.conectarBaseDatos();
        
        if (con != null) { // Verificar que la conexión fue establecida correctamente
            String sql = "INSERT INTO PARTIDA (\"ID_PARTIDA\", \"ESTADO\", \"FECHA_GUARDADO\") VALUES (" 
                         + idPartida + ", '" + estado + "', SYSDATE)";
            bbdd.insert(con, sql);
            
            con.close(); // Cerrar conexión
        }
    }
	
	public String cargarPartida(int idPartida) throws SQLException {
        Connection con = bbdd.conectarBaseDatos();
        String estado = null;

        if (con != null) {
            String sql = "SELECT \"ESTADO\" FROM PARTIDA WHERE \"ID_PARTIDA\" = " + idPartida;
            ResultSet rs = bbdd.select(con, sql);

            if (rs != null && rs.next()) { // Verificar si hay resultados directamente con rs.next()
                estado = rs.getString("ESTADO");
            }

            rs.close(); // Cerrar ResultSet
            con.close(); // Cerrar conexión
        }

        return estado;
    }
	
}
