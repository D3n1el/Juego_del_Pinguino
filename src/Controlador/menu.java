package Controlador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class menu {
    public static Connection con;
    
    public static void main(String[] args) {
        con = bbdd.conectarBaseDatos();
        
        if (con == null) {
            System.out.println("No se pudo establecer conexión con la base de datos. Finalizando programa.");
            return;
        }
        
        String[] columnas = { "ID_JUGADOR", "NICKNAME", "CONTRASENYA" };
        
        System.out.println("Antes del insert:");
        bbdd.print(con, "SELECT ID_JUGADOR, NICKNAME, CONTRASENYA FROM JUGADOR", columnas);
        System.out.println(" ");
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Inserta un nuevo usuario");
        
            System.out.print("Nickname: ");
            String nickname = scanner.nextLine();

            System.out.print("Contraseña: ");
            String contrasenya = scanner.nextLine();

            String sql = "INSERT INTO JUGADOR (ID_JUGADOR, NICKNAME, CONTRASENYA) " +
                    "VALUES (ID_JUGADOR_AUTO.NEXTVAL, '" + nickname + "', '" + contrasenya + "')";
            bbdd.insert(con, sql);
        
        System.out.println("\nDespués del insert:");
        bbdd.print(con, "SELECT ID_JUGADOR, NICKNAME, CONTRASENYA FROM JUGADOR", columnas);

        scanner.close();
    }
}
