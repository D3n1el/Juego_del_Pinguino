package Modelo;

import java.util.ArrayList;

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
	
	
	
	public String getNombre() {
        return Nombre;
    }

    public int getPosicionActual() {
        return PosicionActual;
    }

    public void setPosicionActual(int posicionActual) {
        this.PosicionActual = posicionActual;
    }
	
	
	
	
	
	
	//parte Oriol
	public void usarBolaDeNieve(Jugador jugador) { //Método que permite a un usuario usar bolas de nieve
		
	}
	public void gestionarTurno() { //Método que permite tomar una decision en el turno de un jugador seleccionado
		
	}
	public void mover(int posicion) { //Método que permite al jugador moverse
		
	}
	public void afectarPorCasilla (Casilla casilla) {
		
	}

}

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             