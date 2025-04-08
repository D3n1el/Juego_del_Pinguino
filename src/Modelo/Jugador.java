package Modelo;

import Controlador.ControladorJugador;

public class Jugador extends ControladorJugador{
	
	//Declaración de atributos
	private String idJugador;
	private String Nombre;
	private int PosicionActual;
	private Inventario inventario;
	private boolean esCPU;
	
	//Constructor
	public Jugador(Jugador jugador, String idJugador, String Nombre, int PosicionActual, Inventario inventario, boolean esCPU) {
		super(jugador);
		this.idJugador = idJugador;
		this.Nombre = Nombre;
		this.PosicionActual = PosicionActual;
		this.inventario = inventario;
		this.esCPU = esCPU;
	}
	
	public void usarBolaDeNieve(Jugador jugador) { //Método que permite a un usuario usar bolas de nieve
		
	}
	public void gestionarTurno() { //Método que permite tomar una decision en el turno de un jugador seleccionado
		
	}
	public void setPosicionActual(int f, int c) {
	    this.PosicionActual = f * 10 + c; //10 columnas por fila
	}
	
	public void afectarPorCasilla (Casilla casilla) {
		
	}

	public void setPosicionActual(int posicion) {
		// TODO Auto-generated method stub
		
	}

}

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             