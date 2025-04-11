package Modelo;

import java.util.ArrayList;
import java.util.Random;

public class Tablero extends Casilla {
	
	private Casilla[][] casillas = new Casilla[10][5];
	
	public Tablero(int tipo, int posicion) {
		super(tipo, posicion);
		generarTablero();
	}
	
	public Casilla[][] getCasillas() {
	       return casillas;
	}
	
	//Metodos
	public void generarTablero() {
		int cont = 0;
		Random r = new Random();
        // Inicializar la matriz con las casillas necesarias
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
            	if (cont == 3) {
            		cont = 0;
            		int rand = r.nextInt(5);
            		if (rand == 0) { //Casilla vacía (No ocurre nada)
            			casillas[i][j] = new Casilla(0, i * casillas[i].length + j);
            		} else if (rand == 1) { //Casilla agujero
            			casillas[i][j] = new Casilla(1, i * casillas[i].length + j);
            		} else if (rand == 2) { //Casilla trineo
            			casillas[i][j] = new Casilla(2, i * casillas[i].length + j);
            		} else if (rand == 3) { //Casilla Oso
            			casillas[i][j] = new Casilla(3, i * casillas[i].length + j);
            		} else if (rand == 4) { //Casilla interrogante
            			casillas[i][j] = new Casilla(4, i * casillas[i].length + j);
            		}
            		cont++;
            	} else {
            		cont++;
                	casillas[i][j] = new Casilla(0, i * casillas[i].length + j);
            	}
            }
        }
    }
	
	//Método que busca y devuelve una casilla del tablero según su posición	
	private Casilla obtenerCasilla(int fila, int columna) {
		for (int i = 0; i < casillas.length; i++) {
			for (int j = 0; j < casillas[i].length; j++) {
				if (casillas[i][j].getPosicion() == getPosicion()) {
					return casillas[i][j];
			       }
			   }
		}
		return null;
	}

	public void moverJugador(Jugador jugador, int fila, int columna) {
	    Casilla casillaDestino = obtenerCasilla(fila, columna);
	    jugador.setPosicionActual(casillaDestino.getPosicion()); // Actualizas posición
	    casillaDestino.activarCasilla(jugador); // Activar efecto según tipo de casilla
	}

}
