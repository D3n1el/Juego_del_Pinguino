package Vista;

import Controlador.menu;
import Controlador.bbdd;

////////////////////////////
//BASE DE DATOS
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
////////////////////////////

import java.util.Scanner;

import Controlador.bbdd;

public class MainMenu {
	////////////////////////////
	//BASE DE DATOS
	public static Connection con;
	////////////////////////////
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//////////////////////////////////////////////////////////////////////
		//BASE DE DATOS
		con = bbdd.conectarBaseDatos();
        
        if (con == null) {
            System.out.println("No se pudo establecer conexión con la base de datos. Finalizando programa.");
            return;
        }
        //////////////////////////////////////////////////////////////////////
        
		Scanner s = new Scanner (System.in);
		
		boolean inicio = true;
		
		while(inicio) {
			System.out.println("\n Bienvenido al Juego del Pingüino \n");
			System.out.println("Iniciar Sesión (1)");
			System.out.println("Crear Cuenta (2)");
			System.out.print("Inserta: ");
			int ini1 = s.nextInt();
			
			while(ini1 < 1 && ini1 > 2) {
				System.out.println("Opcion inexistente...");
				System.out.print("Inserta: ");
				ini1 = s.nextInt();
			}
			
			if(ini1 == 1) {
				System.out.print("Nickname: ");
				String nickname = s.nextLine();
				s.nextLine();
				System.out.print("Contraseña: ");
				String contrasenya = s.nextLine();
				menu.iniciarSesion(con, nickname, contrasenya);
				
			}else if(ini1 == 2) {
				menu.crearCuenta();
				
			}
			
		}
	
	
	}

}
