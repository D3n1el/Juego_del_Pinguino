package Modelo;

import java.util.ArrayList;

public class Tablero {
	
	private Casilla[][] casillas;
	
	public Tablero() {
		generarTablero();
	}
	
	//Metodos
	public void generarTablero() {
        // Inicializar la matriz con las casillas necesarias
        casillas = new Casilla[10][5];
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                casillas[i][j] = new Casilla("TipoEjemplo", i * 10 + j); // Creación de casillas
            }
        }
    }
	
	public void obtenerCasillaTablero() {
		Casilla casilla_obtenida;
	}
	
}
