package Modelo;

import java.util.ArrayList;

import Controlador.ControladorTablero;
import Vista.TableroVista;

public class Tablero extends TableroVista {
	
	private int valor;
	
	public Tablero(int valor) {
		super(null);
		this.valor = valor;
	}

	//Metodos
	public void generarTablero() {
		ArrayList<Tablero[][]> tableros = new ArrayList<>();
		
		Tablero[][] tablero1 = {
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)},
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)},
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)},
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)},
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)}
		    };
		
		tableros.add(tablero1);
		
		Tablero[][] tablero2 = {
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)},
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)},
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)},
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)},
		        {new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1), new Tablero(1)}
		    };
		tableros.add(tablero2);
	}
	
	
	public void obtenerEstadoTablero() {
		
	}
	
	}
