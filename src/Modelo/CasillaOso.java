package Modelo;

import java.util.ArrayList;
import java.util.Scanner;

public class CasillaOso extends Casilla { //Subclase que extiende de la clase "Casilla"
	
	//Constructor
	public CasillaOso(int tipo, int posicion) {
	super(tipo, posicion);
	}

	public void regresarInicio(Jugador jugador) {
		Inventario inventarioJugador = jugador.getInventario();  
		Scanner sc = new Scanner(System.in);

		System.out.println("Un oso ha aparecido");

		if (inventarioJugador.getPeces() > 0) {
			System.out.println("¿Quieres usar un pez para calmar al oso? (s/n)");
			String respuesta = sc.nextLine();

			if (respuesta.equalsIgnoreCase("s")) {
				inventarioJugador.quitarPez();
				System.out.println("Has calmado al oso con un pez.");
			} else {
				System.out.println("El oso te ha mandado al inicio del tablero.");
				jugador.setPosActual(0);
			}
		} else {
			System.out.println("No tienes peces. El oso te manda al inicio del tablero.");
			jugador.setPosActual(0);
		}
	}

	

}
