package Modelo;

import java.util.ArrayList;

public class CasillaAgujero extends Casilla{

	
	//Constructor
	public CasillaAgujero(int tipo, int posicion) {
	super(tipo, posicion);
	
	}

	public void enviarJugadorAgujero(Jugador jugador, Tablero tablero) {
	    int posicionActual = jugador.getPosicionActual();
	    Casilla[][] casillas = tablero.getCasillas();

	    // Buscar la casilla de agujero anterior (Tipo 1)
	    for (int i = casillas.length - 1; i >= 0; i--) {
	    	
	     for (int j = casillas[i].length - 1; j >= 0; j--) {
	       if (casillas[i][j].getPosicion() < posicionActual && casillas[i][j].getTipo() == 1) {
	                
	         jugador.setPosicionActual(casillas[i][j].getPosicion());
	         System.out.println(jugador.getNombre() + " ha retrocedido al agujero en la posicion: " + casillas[i][j].getPosicion());
	         return;
	     }
	  }
	}

	    // Si no encontró ningún agujero atrás
	    System.out.println(jugador.getNombre() + " no encontro otro agujero atras, se queda en la misma posición.");
	}
}
