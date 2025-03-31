package Controlador;

import Modelo.Partida;
import Vista.JuegoVista;

public class ControladorPartida extends JuegoVista {

	public ControladorPartida(Partida partida) {
		super(partida);
	}
	
	public Partida getPartida() {
		return this.partida;	
	}
	
	public void setPartida(Partida PARTIDA) {
		this.partida = PARTIDA;
	}
	
	public void iniciarPartida() {
		
	}
	
	public void finalizarPartida() {
		
	}
	
	public void guardarPartida() {
	
	}
	
	
}
