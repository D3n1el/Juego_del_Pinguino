package Controlador;

import Modelo.Casilla;
import Modelo.Tablero;
import Vista.TableroVista;

public class ControladorTablero extends TableroVista  {
	
	public ControladorTablero(Tablero tablero) {
		super(tablero);
	}
	
	public Tablero getTablero() {
		return this.tablero;	
	}
	
	public void setTablero(Tablero TABLERO) {
		this.tablero = TABLERO;
	}
	
	public void actualizarTablero() {
		
	}
	
	public void obtenerEstadoTablero() {
		
	}

	
}



