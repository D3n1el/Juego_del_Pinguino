package Modelo;
import java.util.Scanner;
import java.util.ArrayList;
import Controlador.ControladorJugador;

public class Jugador extends ControladorJugador{
	
	//Declaración de atributos
	private int idJugador;
	private String Nombre;
	private int PosicionActual;
	private Inventario inventario;
	private boolean esCPU;
	
	//Constructor
	public Jugador(Jugador jugador, int idJugador, String Nombre, int PosicionActual, Inventario inventario, boolean esCPU) {
		super(jugador);
		this.idJugador = idJugador;
		this.Nombre = Nombre;
		this.PosicionActual = PosicionActual;
		this.inventario = inventario;
		this.esCPU = esCPU;
	}
	
	//GETTERS 
	public int getIdJugador() {
		return this.idJugador;
	}
	public String getNombre() {
		return this.Nombre;
	}
	public int getPosicionActual() {
		return this.PosicionActual;
	}
	public Inventario getInventario() {
		return this.inventario;
	}
	public boolean getEsCPU() {
		return this.esCPU;
	}
	
	//SETTERS 
	public void setIdJugador(int idJugador) {
		this.idJugador = idJugador;
	}
	public void setNombre(String Nombre) {
		this.Nombre = Nombre;
	}
	public void setPosicionActual(int f, int c) {
		this.PosicionActual = f * 10 + c;
	}
	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}
	public void setEsCPU(boolean esCPU) {
		this.esCPU = esCPU;
	}
	
	//FUNCIONES
	/*
	public void usarBolaDeNieve(ArrayList<Jugador> jugadores) { //Método que permite a un usuario usar bolas de nieve
		
		boolean bolasJ = false;
		for (Jugador jugador : jugadores) {
			if(jugador.getInventario().getBolasDeNieve() > 0) {
				bolasJ = true;
			}
		}
		
		if(!bolasJ) {
			System.out.println("Ningun de los dos jugadores tiene bolas de nieve");
			return;
		}
		
		int max = 0;
		Jugador ganador = null;
		
		for (Jugador jugador : jugadores) {
			int bolas = jugador.getInventario().getBolasDeNieve();
			System.out.println("El jugador " + jugador.getNombre() + " Tiene " + bolas + " bolas");
			
			if(bolas > max) {
				max = bolas;
				ganador = jugador;
			}else {
				ganador = null;
			}
			
			jugador.inventario.setBolasDeNieve(0);
		}
		
		if(ganador != null) {
			System.out.println("El ganador ha sido " + ganador.getNombre());
		}else {
			System.out.println("Hay empate");
		}
		
		for (Jugador jugador : jugadores) {
			if(jugador != ganador) {
				System.out.println("El jugador " + jugador + " va a retroceder " + max + " casillas");
				jugador.PosicionActual -= max; //RETROCEDIRA TANTAS CASILLAS COMO TENGA EL JUGADOR CON MAS BOLAS DE NIEVE
			}
		}
		
		
	}
	public void gestionarTurno() { //Método que permite tomar una decision en el turno de un jugador seleccionado
		Scanner x = new Scanner(System.in);
		
		if(this.esCPU){
			
		} else {
			System.out.println("-- ES TURNO DE " + this.Nombre + " --");
			System.out.println("1- Tirar dado");
			System.out.println("2- Saltar turno");
			System.out.print("inserta: ");
			int opcion = x.nextInt();
			
			while(opcion < 1 && opcion > 2) {
				System.out.print("Opcion incorrecta, intente de nuevo: ");
				opcion = x.nextInt();
			}
			
			switch(opcion) {
			case 1:
				System.out.println("Has decidido tirar el dado!!");
				//lanzar();
				//System.out.println("resultado del dado: " + lanzar());
			case 2:
				System.out.println("Has saltado tu turno");
				break;
			}
		}
	}
	*/
	
	public void mover(int posicion) { //Método que permite al jugador moverse
		int nuevaPosicion = this.PosicionActual + posicion;
		
		if(nuevaPosicion >= 50) {
			nuevaPosicion = 50;
		}else if(nuevaPosicion <= 0) {
			nuevaPosicion = 0;
		}
		
		this.PosicionActual = nuevaPosicion;
		System.out.println("El Jugador: " + this.Nombre + " esta en la casilla " + this.PosicionActual);
		
	}
	public void afectarPorCasilla (Casilla casilla) {
		switch(casilla.getTipo()) {
		case 0:
			break;
		case 1:
			((CasillaAgujero)casilla).enviarJugadorAgujero(this, null);
			break;
		case 2:	
			((CasillaTrineo)casilla).enviarJugadorTrineo(this, null);
			break;
		case 3:
			((CasillaOso)casilla).regresarInicio(this);
			break;
		case 4:
			((CasillaInterrogante)casilla).activarEventoAleatorio(this);
			break;
		}
		this.PosicionActual = casilla.getPosicion();
	}
	
}

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             