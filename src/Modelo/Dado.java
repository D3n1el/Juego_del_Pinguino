package Modelo;

import java.util.Random;

/*
 * Clase que representa un dado con distintos tipos: normal, rápido, lento.
 */
public class Dado {

    private String tipo; // normal, rápido, lento
    private int carasMin;
    private int carasMax;

    public Dado(String tipo) {
        this.tipo = tipo.toLowerCase();

        // Definimos los valores según el tipo de dado
        switch (this.tipo) {
            case "rapido": // dado especial que avanza mucho
                this.carasMin = 5;
                this.carasMax = 10;
                break;
            case "lento": // dado con poca movilidad
                this.carasMin = 1;
                this.carasMax = 3;
                break;
            default: // dado normal
                this.carasMin = 1;
                this.carasMax = 6;
                break;
        }
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /*
     * Función para lanzar el dado. Devuelve el resultado del lanzamiento.
     */
    public int lanzar() {
        Random r = new Random();
        int resultado = r.nextInt(carasMax - carasMin + 1) + carasMin;
        System.out.println("Has lanzado un dado " + tipo + " y has sacado un " + resultado);
        return resultado;
    }

    /*
     * Función que vincula el lanzamiento del dado con el movimiento de un jugador.
     * Recibe el jugador y la cantidad total de casillas, y mueve al jugador según el resultado.
     */
    public void moverJugador(Jugador jugador, Tablero tablero) {
        // Lanza el dado
        int resultado = lanzar();
        
        // Mueve al jugador según el resultado del dado
        jugador.mover(resultado); // Mueve al jugador en el tablero
    }
}
