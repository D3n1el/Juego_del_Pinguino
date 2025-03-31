package Modelo;

import Controlador.ControladorJugador;

public class Jugador extends ControladorJugador{

	private String idJugador;
	private String Nombre;
	private int PosicionActual;
	private Inventario inventario;
	private boolean esCPU;
	
	public Jugador(Jugador jugador, String idJugador, String Nombre, int PosicionActual, Inventario inventario, boolean esCPU) {
		super(jugador);
		this.idJugador = idJugador;
		this.Nombre = Nombre;
		this.PosicionActual = PosicionActual;
		this.inventario = inventario;
		this.esCPU = esCPU;
	}
	
	public void usarBolaDeNieve(Jugador jugador) {
		
	}
	public void gestionarTurno() {
		
	}
	public void mover(int posicion) {
		
	}
	public void afectarPorCasilla (Casilla casilla) {
		
	}

}

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             