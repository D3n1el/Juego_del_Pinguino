package Modelo;

import java.util.ArrayList;

public class Partida {

    private ArrayList<Jugador> jugadores;
    private Tablero tablero;

    public Partida(ArrayList<Jugador> jugadores, Tablero tablero) {
        this.jugadores = jugadores;
        this.tablero = tablero;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    /*
     * Gestiona el turno de un jugador: lanzar dado o usar bolas de nieve
     *
    public void gestionarTurno(Jugador jugador) {
        // Crear un dado normal para el turno del jugador
        Dado dado = new Dado("normal"); // Por defecto, dado normal

        // Mueve al jugador según el dado lanzado usando el método 'moverJugador' en la clase Dado
        dado.moverJugador(jugador, tablero); // Esto mueve al jugador según el dado lanzado

        // Comprobar si ha caído en una casilla con evento
        Casilla casilla = tablero.getCasilla(jugador.getPosicion());
        if (casilla instanceof CasillaInterrogante) {
            Evento evento = new Evento();
            evento.ejecutarEvento(jugador); // Ejecutar el evento para el jugador
        }
    }
     */

    public void guardarPartida() {
        // Lógica para guardar en base de datos con encriptación
    }

    public void cargarPartida() {
        // Lógica para cargar y desencriptar datos
    }
}
