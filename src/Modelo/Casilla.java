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
	

	public String obtenerEstado() { //Método que muestra datos
        return "Tipo: " + Tipo + ", Posición: " + Posicion;
    }
	
}
