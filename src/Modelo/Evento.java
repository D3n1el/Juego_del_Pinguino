package Modelo;

import java.util.Random;

public class Evento {

    private final Random random;

    // Constructor
    public Evento() {
        this.random = new Random();
    }

    /*
     * Ejecuta un evento aleatorio o normal para el jugador que ha caído en una casilla interrogante.
     * @param jugador el jugador que ha caído en la casilla
     */
    public void ejecutarEvento(Jugador jugador) {
        // Aquí se genera un número aleatorio para decidir qué tipo de evento se ejecuta
        int tipoEvento = random.nextInt(100); // Genera un número aleatorio entre 0 y 99

        if (tipoEvento < 30) {
            // Evento: Obtener un pez
            ejecutarEventoPez(jugador);
        } else if (tipoEvento < 60) {
            // Evento: Obtener entre 1 y 3 bolas de nieve
            ejecutarEventoBolasDeNieve(jugador);
        } else if (tipoEvento < 75) {
            // Evento: Obtener dado rápido (probabilidad baja)
            ejecutarEventoDadoRapido(jugador);
        } else {
            // Evento: Obtener dado lento (probabilidad más alta)
            ejecutarEventoDadoLento(jugador);
        }
    }

    // Evento: Obtener un pez
    private void ejecutarEventoPez(Jugador jugador) {
        if (jugador.getInventario().getPeces() < 2) {  // Aseguramos que no haya más de 2 peces
            jugador.getInventario().añadirPez();
            System.out.println(jugador.getNombre() + " ha encontrado un pez 🐟.");
        } else {
            System.out.println(jugador.getNombre() + " ya tiene el máximo de peces.");
        }
    }

    // Evento: Obtener entre 1 y 3 bolas de nieve
    private void ejecutarEventoBolasDeNieve(Jugador jugador) {
        
        jugador.getInventario().añadirBolasNieve();
     
    }

    // Evento: Obtener dado rápido (probabilidad baja)
    private void ejecutarEventoDadoRapido(Jugador jugador) {
        if (jugador.getInventario().puedeAñadirDado()) {
            jugador.getInventario().añadirDado("rapido");
            System.out.println(jugador.getNombre() + " ha encontrado un dado rápido 🚀.");
        } else {
            System.out.println(jugador.getNombre() + " no puede guardar más dados.");
        }
    }

    // Evento: Obtener dado lento (probabilidad más alta)
    private void ejecutarEventoDadoLento(Jugador jugador) {
        if (jugador.getInventario().puedeAñadirDado()) {
            jugador.getInventario().añadirDado("lento");
            System.out.println(jugador.getNombre() + " ha conseguido un dado lento 🐢.");
        } else {
            System.out.println(jugador.getNombre() + " no puede guardar más dados.");
        }
    }
}
