package Vista;

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
	
	public static int mostrarMenu() {
		Scanner s = new Scanner (System.in);
		int opc = 0;
		
		do {
			System.out.println("- ESCOGE UNA OPCIÓN -");
			System.out.println("1. Jugar partida");
			System.out.println("2. Ver tablero");
			System.out.println("3. Ver jugadores");
			System.out.println("4. Salir");
				opc = s.nextInt();
			if (opc < 1 || opc > 4) {System.out.println("¡Numero incorrecto! Escribe uno de la lista");}
		} while (opc < 1 || opc > 4);
			return opc;
	}
	
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
		int elecc = 0;
		
		do {
			elecc = mostrarMenu();
			
			switch(elecc) {
			case 1:
				
				break;
			case 2:
				
				break;
			case 3:
				
				break;
			}
			
		} while (elecc == 4);
	
	
	
	}

}
