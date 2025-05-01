package Controlador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class menu {
    public static Connection con;
    
    //METODO PARA CREAR CUENTA EN SQL
    public static void crearCuenta() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserta un nuevo usuario");

        System.out.print("Nickname: ");
        String nickname = scanner.nextLine();

        System.out.print("Contraseña: ");
        String contrasenya = scanner.nextLine();
        
        System.out.print("Color: ");
        String color = scanner.nextLine();
        
        String sql = "INSERT INTO JUGADOR (ID_JUGADOR, NICKNAME, CONTRASENYA, COLOR) " +
                     "VALUES (ID_JUGADOR_AUTO.NEXTVAL, '" + nickname + "', '" + contrasenya + "', '" + color + "')";

        bbdd.insert(con, sql);
        System.out.println("Cuenta creada correctamente.");
    }

    //METODO PARA INICIAR SESIÓN EN SQL
    public static boolean iniciarSesion(Connection con, String nickname, String contrasenya) {
        String sql = "SELECT * FROM JUGADOR WHERE NICKNAME = '" + nickname + "' AND CONTRASENYA = '" + contrasenya + "'";
        ResultSet rs = bbdd.select(con, sql);

        try {
            if (rs != null && rs.next()) {
                System.out.println("Inicio de sesión correcto. ¡Bienvenido, " + nickname + "!");
                return true;
            } else {
                System.out.println("Nombre de usuario o contraseña incorrectos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error durante el inicio de sesión: " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args) {
    	//saveCon.setConexion(con);
        con = bbdd.conectarBaseDatos();
        if (con == null) { //COMPOBAR LA CONEXION CON SQL 
            System.out.println("No se pudo establecer conexión con la base de datos. Finalizando programa.");
            return;
        }
    }
}
