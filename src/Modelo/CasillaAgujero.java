package Modelo;

import java.util.ArrayList;

public class CasillaAgujero extends Casilla{

	
	//Constructor
	public CasillaAgujero(int tipo, int posicion) {
	super(tipo, posicion);
	
	}

	public void enviarJugadorAgujero(Jugador jugador) {
        // Aqui se retrocede la posicion del jugador 3 casillas
        int nuevaPosicion = jugador.getPosicionActual() - 3;
        
        // Evitar que el jugador retroceda más allá de la posición 0
        if (nuevaPosicion < 0) {
            nuevaPosicion = 0;
        }
        
        jugador.setPosicionActual(nuevaPosicion); // Actualizar la posición del jugador
        System.out.println(jugador.getNombre() + " ha caido en un agujero y retrocedio a la posicion: " + nuevaPosicion);
    }

}
