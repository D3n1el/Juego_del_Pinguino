package Modelo;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;


public class CPU extends Jugador{
	
	//ATRIBUTOS
	private String dificultad;
	
	//CONSTRUCTOR
	public CPU(String dificultad, Jugador jugador, int idJugador, String Nombre, int PosicionActual, Inventario inventario, boolean esCPU) {
		super(jugador, idJugador, Nombre, PosicionActual, inventario, esCPU);
		this.dificultad = dificultad;
	}
	
	//GETTERS 
	public String getDificultad() {
		return this.dificultad;
	}
	
	//SETTERS 
	public void setDificultad(String dificultad) {
		this.dificultad = dificultad;
	}
	
	//FUNCIONES
	public void realizarMovimiento() {
		
	}
	public void determinarEstrategia() {
		
	}
	
}
