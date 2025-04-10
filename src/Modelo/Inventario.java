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
	public void añadirPez() {
		int cantidad = 1;
		this.peces += cantidad;
		
		if(this.peces >= 2) {
			this.peces = 2;
		}else if(this.peces <= 0) {
			this.peces = 0;
		}
		System.out.println("El jugador " + this.getNombre() + " ha conseguido " + cantidad + " Peces. ");
	}
	
	public void añadirDados(String tipo) {
		
		if(tipo == "Rapido") {
			
		}else if(tipo == "Lento") {
			
		}else if(tipo == "Normal") {
			
		}
		
	}
	
	public void añadirBolasNieve() {
		Random r = new Random();
		int cantidad = r.nextInt(3)+1;
		bolasDeNieve += cantidad;
		
		System.out.println("El jugador " + this.getNombre() + " Ha conseguido " + cantidad + " Bolas de Nieve.");
		
		if(this.bolasDeNieve >= 6) {
			this.bolasDeNieve = 6;
		}else if(this.bolasDeNieve <= 0) {
			this.bolasDeNieve = 0;
		}
		
		System.out.println("El jugador " + this.getNombre() + " Tiene " + this.bolasDeNieve + " Bolas de nieve. ");
	}
	
	public void quitarPez() {
		int cantidad = 1;
		
		if(this.peces >= 1) {
			this.peces -= cantidad;
		}else {
			System.out.println("No tienes suficientes peces");
		}
		
		System.out.println("El jugador " + this.getNombre() + " tiene " + this.peces + " Peces. ");
	}
	
	public void comprobarObjetos() {
		if(dados > 3) {
			System.out.println("No se puede obtener mas de 3 dados");
			dados = 3;
			return;
		}else if(dados < 1) {
			dados = 1;
		}
		
		if(peces > 2) {
			System.out.println("No se pueden obtener mas de 2 peces");
			peces = 2;
			return;
		}else if(peces < 0) {
			peces = 0;
		}
		
		if(bolasDeNieve > 6) {
			System.out.println("No puedes obtener mas de 6 bolas de nieve");
			bolasDeNieve = 6;
			return;
		}else if(bolasDeNieve < 0) {
			bolasDeNieve = 0;
		}
	}
	
}