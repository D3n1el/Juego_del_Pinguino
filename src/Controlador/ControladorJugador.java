package Controlador;

import Modelo.Jugador;
import Vista.JugadorVista;

public class ControladorJugador extends JugadorVista {
	
	public ControladorJugador (Jugador jugador){
		super(jugador);
	}
	
	public Jugador getJugador() {
		return this.jugador;
	}
	
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}
	
	public void iniciarPartida() {
		
	}
	
	public void finalizarPartida() {
		
	}
	
	public void guardarPartida() {
		
	}
}
