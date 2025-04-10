package Modelo;

import java.util.ArrayList;

public class Casilla { //Subclase de la clase "Tablero"

	//Declaración de atributos
	private int Tipo;
	private int Posicion;
	
	
	//Constructor
	public Casilla(int tipo, int posicion) {
        this.Tipo = Tipo;
        this.Posicion = Posicion;
    }
	
	public int getTipo() { //Getter del atributo "Tipo"
    return Tipo;
    }
	
	public void setTipo(int tipo) { //Setter del atributo "Tipo"
    this.Tipo = tipo;
    }
	
	public int getPosicion() { //Setter del atributo "Posicion"
    return Posicion;
    }

	public void setPosicion(int posicion) { //Setter del atributo "Posicion"
        this.Posicion = posicion;
    }
	

	public String obtenerEstado() { //Método que muestra los datos
        return "Tipo: " + Tipo + ", Posición: " + Posicion;
    }
	
	//Método que gestiona el comportamiento según el tipo de casilla

	 public void activarCasilla(Jugador jugador, Tablero tablero) {
		 
	    switch (Tipo) {
	    case 1: // Agujero
	    	
	    ((CasillaAgujero)this).enviarJugadorAgujero(jugador, tablero);

	    
	    break;

	    
        case 4: // Interrogante
            ((CasillaInterrogante) this).activarEventoAleatorio(jugador);
            break;
            
	    // aqui añadir más cases con otros efectos de casillas (tipo 2, 3...)

	    default:
	    System.out.println("Casilla normal. No pasa nada.");
	    break;
	        }
	    }
	
}

