package Modelo;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Inventario extends Jugador {

	//ATRIBUTOS
	private int dados;
	private int peces;
	private int bolasDeNieve;
	
	//CONSTRUCTOR
	public Inventario(int dados, int peces, int bolasDeNieve, Jugador jugador, int idJugador, String Nombre, int PosicionActual, Inventario inventario, boolean esCPU) {
		super(jugador, idJugador, Nombre, PosicionActual, inventario, esCPU);
		this.dados = dados;
		this.peces = peces;
		this.bolasDeNieve = bolasDeNieve;
	}
	
	//GETTERS
	public int getDados() {
		return this.dados;
	}
	public int getPeces() {
		return this.peces;
	}
	public int getBolasDeNieve() {
		return this.bolasDeNieve;
	}
	
	//SETTERS
	public void setDados(int dados) {
		this.dados = dados;
	}
	public void setPeces(int peces) {
		this.peces = peces;
	}
	public void setBolasDeNieve(int bolasDeNieve) {
		this.bolasDeNieve = bolasDeNieve;
	}
	
	//FUNCIONES
	public void agregarObjetos(String tipo) {
		
	}
	public void quitarObjetos(String tipo) {
		
	}
	public void comprobarObjetos() {
		if(dados > 3) {
			System.out.println("No se puede obtener mas de 3 dados");
			dados = 3;
			return;
		}
		if(peces > 2) {
			System.out.println("No se pueden obtener mas de 2 peces");
			peces = 2;
			return;
		}
		if(bolasDeNieve > 6) {
			System.out.println("No puedes obtener mas de 6 bolas de nieve");
			bolasDeNieve = 6;
			return;
		}
	}
	
}
