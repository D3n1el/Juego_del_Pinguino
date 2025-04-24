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

        String[] columnas = { "ID_JUGADOR", "NICKNAME", "CONTRASENYA", "NUM_PARTIDAS_JUGADAS" };

        System.out.println("Antes del insert:");
        bbdd.print(con, "SELECT * FROM JUGADOR", columnas);

        // Bucle para insertar jugadores
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n¿Cuántos jugadores quieres insertar?: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\nJugador " + (i + 1));

            System.out.print("Nickname: ");
            String nickname = scanner.nextLine();

            System.out.print("Contraseña: ");
            String contrasenya = scanner.nextLine();

            String sql = "INSERT INTO JUGADOR (NICKNAME, CONTRASENYA) " +
                         "VALUES ('" + nickname + "', '" + contrasenya + "')";
            bbdd.insert(con, sql);
        }

        System.out.println("\nDespués del insert:");
        bbdd.print(con, "SELECT * FROM JUGADOR", columnas);

        // SELECT con filtro como ejemplo
        System.out.println("\nSelect con condición:");
        procesamientoSelect(con, "SELECT * FROM JUGADOR WHERE NUM_PARTIDAS_JUGADAS >= 5");

        scanner.close();
    }

    public static void procesamientoSelect(Connection con, String sql) {
        ResultSet rs = bbdd.select(con, sql);
        try {
            if (rs != null && rs.isBeforeFirst()) {
                while (rs.next()) {
                    int idJugador = rs.getInt("ID_JUGADOR");
                    String nickname = rs.getString("NICKNAME");
                    String contrasenya = rs.getString("CONTRASENYA");
                    int partidasJugadas = rs.getInt("NUM_PARTIDAS_JUGADAS");

                    System.out.println("ID_JUGADOR: " + idJugador);
                    System.out.println("NICKNAME: " + nickname);
                    System.out.println("CONTRASENYA: " + contrasenya);
                    System.out.println("PARTIDAS JUGADAS: " + partidasJugadas);
                    System.out.println("------------------------------------");
                }
            } else {
                System.out.println("No se ha encontrado nada");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
