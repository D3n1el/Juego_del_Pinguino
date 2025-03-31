package Modelo;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Dado {

	//ATRIBUTO
	private String tipo;
	
	//CONSTRUCTOR
	public Dado(String tipo) {
		this.tipo = tipo;
	}
	
	//GETTER
	public String getTipo() {
		return this.tipo;
	}
	
	//SETTER
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	//FUNCIONES
	
	public int lanzar() {
		Random r = new Random();
		int i = r.nextInt(3) + 1;
		
		return i;
	}
}
