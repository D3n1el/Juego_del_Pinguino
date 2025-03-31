package Modelo;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Partida {
	
	//ATRIBUTOS
	private ArrayList<Jugador> Jugadores;
	private int turnoActual;
	private Tablero tablero;
	
	//CONSTRUCTOR
	public Partida(ArrayList Jugadores, int turnoActual, Tablero tablero) {
		this.Jugadores = Jugadores;
		this.turnoActual = turnoActual;
		this.tablero = tablero;
	}
	
	//GETTERS
	public int getTurnoActual() {
		return this.turnoActual;
	}
	public Tablero getTablero() {
		return this.tablero;
	}
	
	//SETTERS
	public void setTurnoActual(int turnoActual) {
		this.turnoActual = turnoActual;
	}
	public void setTablero(Tablero tablero) {
		this.tablero = tablero;
	}
	
	//FUNCIONES
	public void guardarPartida() {
		
	}
	public void cargarPartida() { 
		
	}
	public void gestionarTurno() {
		
	}
	public void lanzarDado() {
		
	}
}
