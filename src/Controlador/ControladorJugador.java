package Controlador;

import Modelo.Jugador;
import Modelo.Tablero;

public class ControladorJugador {
	
	private Jugador jugador;
	//private Tablero tablero;
	
	//Constructor
	public ControladorJugador (Jugador jugador){
		this.jugador = jugador;
		//this.tablero = tablero;
	}
	
	//Metodos
	public void moverJugador(int posicion) {
		jugador.mover(posicion);
	}
	/*
	public void usarBolasDeNieve(Jugador objetivo) {
	}
	public void gestionarTurno() {
    }
	*/
}
