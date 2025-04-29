package Controlador;

import Modelo.*;

public class ControladorTablero {
    private Tablero tablero;
    private Partida partida;
    
    public ControladorTablero(Tablero tablero, Partida partida) {
        this.tablero = tablero;
        this.partida = partida;
    }
    
    /**
     * Mueve al jugador usando el dado (usa solo métodos existentes)
     */
    public void moverJugador(Jugador jugador, String tipoDado) {
        Dado dado = new Dado(tipoDado);
        dado.moverJugador(jugador, tablero); // Usa el método existente de Dado
    }
    
    /**
     * Muestra el estado actual del juego (solo con métodos existentes)
     */
    public void mostrarEstado(Jugador jugadorActual) {
        System.out.println("\n=== ESTADO DEL JUEGO ===");
        
        // Mostrar info del jugador actual (usando solo getters existentes)
        System.out.println("Jugador actual: " + jugadorActual.getNombre());
        System.out.println("Posición: " + jugadorActual.getPosicionActual());
        System.out.println("Peces: " + jugadorActual.getInventario().getPeces());
        System.out.println("Bolas de nieve: " + jugadorActual.getInventario().getBolasDeNieve());
        
        // Mostrar tipo de casilla actual
        System.out.println("\nCasilla actual: " + obtenerTipoCasilla(jugadorActual.getPosicionActual()));
    }
    
    /**
     * Método auxiliar para obtener tipo de casilla (usando solo lo existente)
     */
    private String obtenerTipoCasilla(int posicion) {
        // Accede directamente al array de casillas del tablero
        for (Casilla[] fila : tablero.getCasillas()) {
            for (Casilla casilla : fila) {
                if (casilla.getPosicion() == posicion) {
                    switch(casilla.getTipo()) {
                        case 0: return "Normal";
                        case 1: return "Agujero";
                        case 2: return "Trineo";
                        case 3: return "Oso";
                        case 4: return "Interrogante";
                        default: return "Desconocida";
                    }
                }
            }
        }
        return "No encontrada";
    }
    
    /**
     * Activa el efecto de la casilla donde está el jugador
     */
    public void activarEfectoCasilla(Jugador jugador) {
        // Busca la casilla directamente en el array del tablero
        for (Casilla[] fila : tablero.getCasillas()) {
            for (Casilla casilla : fila) {
                if (casilla.getPosicion() == jugador.getPosicionActual()) {
                    casilla.activarCasilla(jugador, tablero); // Usa método existente
                    return;
                }
            }
        }
    }
}



