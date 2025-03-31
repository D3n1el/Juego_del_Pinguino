package Modelo;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Inventario {

	//ATRIBUTOS
	private int dados;
	private int peces;
	private int bolasDeNieve;
	
	//CONSTRUCTOR
	public Inventario(int dados, int peces, int bolasDeNieve) {
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
}
