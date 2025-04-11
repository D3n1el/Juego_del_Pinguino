package Modelo;

public class CasillaTrineo extends Casilla {
    // Constructor
    public CasillaTrineo(int tipo, int posicion) {
        super(tipo, posicion);
    }

    // Método que envía a un jugador seleccionado de punto A a punto B en el trayecto de un trineo
    public void enviarJugadorTrineo(Jugador jugador, Tablero tablero) {
        boolean encontrado = false;
        int posicion;
        int f = 5;
        int c = 10;

        // Verifica filas mientras no se encuentre la casilla y haya filas
        while (!encontrado && f < tablero.getCasillas().length) {
            c = 0; // Reinicia la columna al empezar una nueva fila
            // Verifica columnas mientras no se encuentre la casilla y haya columnas
            while (!encontrado && c < tablero.getCasillas()[f].length) {
                if (tablero.getCasillas()[f][c].getTipo() == 2) {
                    encontrado = true; // Se encontró la casilla de tipo 2
                    // Actualiza la posición del jugador en el tablero
                    jugador.setPosicionActual(f, c);
                    System.out.println("Jugador movido a la casilla (" + f + ", " + c + ")");
                    return;
                }
                c++; // Avanza a la siguiente columna
            }
            f++; // Avanza a la siguiente fila
        }

        if (!encontrado) {
            System.out.println("No se encontró una casilla de tipo 2 en el tablero.");
        }
    }
}
