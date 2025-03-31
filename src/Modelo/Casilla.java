package Modelo;

import java.util.ArrayList;

public class Casilla extends Tablero {

	private String Tipo;
	private int Posicion;

	public Casilla(String tipo, int posicion, ArrayList<Casilla> casillas) {
        super(casillas); 
        this.Tipo = Tipo;
        this.Posicion = Posicion;
    }

	public String getTipo() {
    return Tipo;
    }

	public void setTipo(String tipo) {
    this.Tipo = tipo;
    }

	public int getPosicion() {
    return Posicion;
    }

	public void setPosicion(int posicion) {
        this.Posicion = posicion;
    }
	

	public String obtenerEstado() {
        return "Tipo: " + Tipo + ", Posición: " + Posicion;
    }
	
}
