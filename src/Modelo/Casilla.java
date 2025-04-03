package Modelo;

import java.util.ArrayList;

public class Casilla { //Subclase de la clase "Tablero"

	//Declaración de atributos
	private String Tipo;
	private int Posicion;
	
	//Constructor
	public Casilla(String tipo, int posicion, ArrayList<Casilla> casillas) {
        this.Tipo = Tipo;
        this.Posicion = Posicion;
    }
	
	public String getTipo() { //Getter del atributo "Tipo"
    return Tipo;
    }
	
	public void setTipo(String tipo) { //Setter del atributo "Tipo"
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
