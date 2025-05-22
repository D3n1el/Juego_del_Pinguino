package Vista;

import java.util.Random;

import Controlador.saveCon;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * En esta clase se define el código que permite el funcionamiento del tablero y los elementos presentes en la misma ventana.
 * @author DAVO
 * @version 16/05/2025
 */
public class pantallaJuegoController {
	
	/**
	 * Define cada tipo posible de casilla
	 * @param NORMAL define el tipo de casilla normal
	 */
	public enum TipoCasilla { //ES UN ENNUMERADOR DE VARIABLES ESTATICAS, ES COMO UN DESPLEGABLE DE OPCIONES
		NORMAL,
		AGUJERO,
		INTERROGANTE,
		OSO,
		TRINEO,
		MOTO,
		SUELO_QUEBRADIZO,
		META,
	}

    // Menu items
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;
    
    // Buttons
    @FXML private Button dado;
    @FXML private Button rapido;
    @FXML private Button lento;
    @FXML private Button peces;
    @FXML private Button nieve;
    @FXML private Button inventario;

    // Texts
    @FXML private Text dadoResultText;
    @FXML private Text inventario_t;
    @FXML private Text rapido_t;
    @FXML private Text lento_t;
    @FXML private Text peces_t;
    @FXML private Text nieve_t;
    @FXML private Text eventos;

    // Game board and player pieces
    @FXML private GridPane tablero;
    @FXML private Circle P1;
    
    
    //ONLY FOR TESTING!!!
    private int p1Position = 0; // Tracks current position (from 0 to 49 in a 5x10 grid)
    private final int COLUMNS = 5; //"final" significa que el valor no puede cambiar despues de su inicializaciÃ³n
    private Random rand = new Random(); //PARA LAS CASILLAS
    
    private static final int TOTAL_CASILLAS = 50; //ES UNA CADENA CONSTANTE 
    private TipoCasilla[] tableroCasillas = new TipoCasilla[TOTAL_CASILLAS]; //Array PARA LAS CASILLAS. El tamaño lo define la variable TOTAL_CASILLAS
    private IntegerProperty cantidadPeces = new SimpleIntegerProperty(0); //Se usa IntegerProperty porque se trata de una propiedad observable que se usa en JavaFX. 
    private IntegerProperty cantidadNieve = new SimpleIntegerProperty(0); // " = new SimpleIntegerProperty(0);" Está creando una instancia de SimpleIntegerProperty, que es una implementación de IntegerProperty, y su valor inicial se establece en 0 la cantidad de bolas de nieve inicial.
    private IntegerProperty cantidadDadosRapidos = new SimpleIntegerProperty(0); //
    private IntegerProperty cantidadDadosLentos= new SimpleIntegerProperty(0); //
    
    /**
     * Inicializa la interfaz gráfica y los valores del juego.
     * Este método es llamado automáticamente después de cargar el archivo FXML.
     * <p>
     * Realiza las siguientes acciones:
     * <ul>
     *   <li>Establece el mensaje inicial indicando que el juego ha comenzado.</li>
     *   <li>Enlaza las propiedades de texto de los elementos gráficos con los valores dinámicos de los recursos.</li>
     *   <li>Llama al método {@code inicializarTablero()} para configurar el tablero de juego.</li>
     * </ul>
     */
    @FXML
    private void initialize() {
    	
        // This method is called automatically after the FXML is loaded
        // You can set initial values or add listeners here
        eventos.setText("¡El juego ha comenzado!");
        peces_t.textProperty().bind(Bindings.concat("Peces: ", cantidadPeces.asString())); //Muestra la cantidad disponible de peces. bind() hace que el texto actualiza cada vez que cambie el valor al que está enlazado. Bindings.concat permite concatenar valores.
        nieve_t.textProperty().bind(Bindings.concat("Bolas De Nieve: ", cantidadNieve.asString()));
        lento_t.textProperty().bind(Bindings.concat("Dado Lento: ", cantidadDadosLentos.asString()));
        rapido_t.textProperty().bind(Bindings.concat("Dado Rapido: ", cantidadDadosRapidos.asString()));
        
        inicializarTablero();//INICIALIZAR EL TABLERO
    }
    
    /**
     * Inicializa el tablero de juego con las casillas predeterminadas y especiales.
     * 
     * <p>Primero establece todas las casillas como NORMALES por defecto usando Arrays.fill(),
     * luego coloca las casillas especiales en posiciones aleatorias según las cantidades especificadas.
     * La casilla inicial (posición 0) siempre será NORMAL y la última casilla (posición 49) será la META.</p>
     * 
     * <p>Finalmente, llama a los métodos para mostrar las imágenes correspondientes a cada tipo de casilla especial.</p>
     * 
     * <p>Distribución de casillas especiales:</p>
     * <ul>
     *   <li>4 casillas de tipo AGUJERO</li>
     *   <li>5 casillas de tipo INTERROGANTE</li>
     *   <li>2 casillas de tipo OSO</li>
     *   <li>4 casillas de tipo TRINEO</li>
     *   <li>2 casillas de tipo MOTO</li>
     *   <li>3 casillas de tipo SUELO_QUEBRADIZO (cantidad ajustable)</li>
     * </ul>
     * 
     * @see TipoCasilla
     * @see #colocarCasillasEspeciales(TipoCasilla, int)
     * @see #mostrarImagenesAgujero()
     * @see #mostrarImagenesOso()
     * @see #mostrarImagenesInterrogante()
     * @see #mostrarImagenesTrineo()
     * @see #mostrarImagenesMoto()
     * @see #mostrarImagenesMeta()
     * @see #mostrarImagenesSueloQ()
     */
    private void inicializarTablero() {
    	Arrays.fill(tableroCasillas, TipoCasilla.NORMAL); //ESTO HACE QUE TODAS LAS CASILLAS SEAN NORMALES POR DEFECTO
    	
    	//ESTO ES UNA DISTRIBUCION DE CASILLAS ESPECIALES (SE PUEDE AJUSTAR A MENOS CASILLAS O MAS)
    	colocarCasillasEspeciales(TipoCasilla.AGUJERO, 4);
    	colocarCasillasEspeciales(TipoCasilla.INTERROGANTE, 5);
    	colocarCasillasEspeciales(TipoCasilla.OSO, 2);
    	colocarCasillasEspeciales(TipoCasilla.TRINEO, 4);
    	colocarCasillasEspeciales(TipoCasilla.MOTO, 2);
    	colocarCasillasEspeciales(TipoCasilla.SUELO_QUEBRADIZO, 3); // Puedes ajustar la cantidad

    	//LA CASILLA INICIAL SIEMPRE SERA UNA CASILLA NORMAL
    	tableroCasillas[0] = TipoCasilla.NORMAL;
    	tableroCasillas[49] = TipoCasilla.META;
    	
    	//PARA MOSTRAR LAS IMAGENES EN EL TABLERO
    	mostrarImagenesAgujero();
    	mostrarImagenesOso();
    	mostrarImagenesInterrogante();
    	mostrarImagenesTrineo();
    	mostrarImagenesMoto();
    	mostrarImagenesMeta();
    	mostrarImagenesSueloQ();
    }
    
    /**
     * Inicializa el tablero de juego con casillas normales por defecto y distribuye 
     * casillas especiales según la configuración establecida.
     * <p>
     * Realiza las siguientes acciones:
     * <ul>
     *   <li>Coloca casillas especiales de tipo agujero, interrogante, oso y trineo.</li>
     *   <li>Establece la casilla inicial como normal y la última como meta.</li>
     *   <li>Invoca métodos para mostrar imágenes en el tablero.</li>
     * </ul>
     */
    private void colocarCasillasEspeciales(TipoCasilla tipo, int cantidad) { //Garantiza que solo se modifiquen casillas que actualmente son de tipo NORMAL, preservando otros tipos de casillas especiales que ya pudieran estar en el tablero.
    	for (int i = 0; i < cantidad; i++) {
    		
    			int posicion;
    			
    		do {
    			posicion = rand.nextInt(tableroCasillas.length - 1) + 1; //NO PONER EN CASILLA 0 rand.nextInt GENERA UNA POSICION aleatoria y la guarda en una variable.
    			
    		} while(tableroCasillas[posicion] != TipoCasilla.NORMAL); //Este do-while no parará hasta encontrar aleatoriamente una casilla tipo NORMAL.
    			tableroCasillas[posicion] = tipo;
    	}
    }
    
    /**
     * Aplica el efecto correspondiente según el tipo de casilla en la que se encuentra el jugador.
     * <p>
     * Dependiendo de la casilla en la posición indicada, se ejecutan diferentes acciones:
     * <ul>
     *   <li><b>Agujero:</b> Si hay un agujero anterior, el jugador retrocede hasta él. 
     *       Si no hay agujero anterior, se queda en la misma casilla.</li>
     *   <li><b>Interrogante:</b> Se genera un evento aleatorio en el que el jugador 
     *       puede obtener bolas de nieve, peces o dados rápidos/lentos.</li>
     *   <li><b>Oso:</b> Si el jugador tiene peces, puede sobornar al oso para seguir 
     *       avanzando. De lo contrario, vuelve al inicio.</li>
     *   <li><b>Trineo:</b> Si hay otro trineo adelante, el jugador avanza a la siguiente 
     *       casilla especial de este tipo. Si no hay más trineos, permanece en su posición.</li>
     *   <li><b>Meta:</b> Se muestra un mensaje de felicitación, indicando que el juego 
     *       ha finalizado.</li>
     *   <li><b>Moto:</b> Avanza hasta la siguiente moto.</li>
     *   <li><b>Suelo quebradizo:</b> Dependiendo de los objetos que tenga, vuelve al inicio o pierde algunos objetos</li>
     * </ul>
     * 
     * @param posicion Define la posición actual del jugador en el tablero.
     */
    private void aplicarEfectoCasilla(int posicion) {
    	TipoCasilla casilla = tableroCasillas[posicion];
    	
    	switch(casilla) {
    	case AGUJERO:
    		aplicandoEfecto = true;
    	    
    	    int anteriorAgujero = encontrarAnteriorAgujero(posicion);
    	    if (anteriorAgujero != posicion) { //Si hay anteriormente un agujero...
    	        // Calculamos nueva posición (retrocedemos al agujero anterior)
    	        int nuevaPosicion = anteriorAgujero;
    	        eventos.setText("¡Agujero! Retrocedes a la casilla " + nuevaPosicion);
    	        
    	        // Movemos directamente a la nueva posición
    	        p1Position = nuevaPosicion;
    	        int row = p1Position / COLUMNS;
    	        int col = p1Position % COLUMNS;
    	        GridPane.setRowIndex(P1, row); //GridPane es un contenedor. En este caso, asigna una posicion a un jugador en la posicion de la variable "row".
    	        GridPane.setColumnIndex(P1, col);
    	        
    	        // Volvemos a activar los efectos
    	        aplicandoEfecto = false;
    	    } else { //Si no hay anteriormente ningún agujero...
    	        eventos.setText("¡Agujero! No hay agujeros anteriores, te quedas aquí.");
    	        aplicandoEfecto = false;
    	    }
    	    break;
    	case INTERROGANTE: 
    		int probabilidad = rand.nextInt(4) + 1;
    		if(probabilidad == 1) {
    			if(cantidadNieve.get() >= 6) { //COMPROBAR QUE NO SUPERE EL MAXIMO DE BOLAS DE NIEVE
    				cantidadNieve.set(6);
    				eventos.setText("Ya tienes el maximo de Nieve possible " + cantidadNieve.get());
    			}else { //EN CASO DE QUE NO SUPERE EL LIMITE
    				int nieve = rand.nextInt(3) + 1;
        			cantidadNieve.set(cantidadNieve.get() + nieve);
        			eventos.setText("Has conseguido " + nieve + " Bolas de Nieve!!!");
    			}
    		}else if (probabilidad == 2){
    			if(cantidadPeces.get() >= 2 ) { //COMPROBAR QUE NO TENGA MAS DE 2 PECES
    				eventos.setText("Ya tienes el maximo de peces " + cantidadPeces.get());
    			}else { //EN CASO DE QUE TENGA MAS DE 2 PECES
    				cantidadPeces.set(cantidadPeces.get() + 1);
        			eventos.setText("Has conseguido 1 Pez!!!");
    			}
    		}else if(probabilidad == 3) {
    			if(cantidadDadosRapidos.get()  >= 3) {
    				cantidadDadosRapidos.set(3);
    				eventos.setText("Ya tienes los dados maximos disponibles");
    			}else {
    				cantidadDadosRapidos.set(cantidadDadosRapidos.get() + 1);
    				eventos.setText("Has conseguido 1 dado Rapido");
    			}
    		}else {
    			if(cantidadDadosLentos.get() >= 3 ) {
    				cantidadDadosLentos.set(3);
    				eventos.setText("Ya tienes el maximo de dados Lentos");
    			}else {
    				cantidadDadosLentos.set(cantidadDadosLentos.get() + 1);
    				eventos.setText("Has conseguido 1 dado Lento");
    			}
    		}
    		break;
    	case OSO:
    		if (cantidadPeces.get() > 0) { 
    			//Platform.runLater() es un método en JavaFX que se utiliza para ejecutar un fragmento de código en el hilo de la interfaz de usuario (UI thread) de JavaFX. 
                Platform.runLater(() -> { //-> es una expresion lambda. Esto se usa para definir un bloque de codigo que se utilizará en JavaFX.
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("¡Encontraste un oso polar!");
                    alert.setHeaderText("¿Quieres darle 1 pez para que te deje pasar?");
                    alert.setContentText("Peces disponibles: " + cantidadPeces.get());
                    
                    ButtonType siButton = new ButtonType("Sí", ButtonBar.ButtonData.YES);
                    ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                    alert.getButtonTypes().setAll(siButton, noButton);
                    
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == siButton) {
                        cantidadPeces.set(cantidadPeces.get() - 1);
                        eventos.setText("Has sobornado al oso con 1 pez.");
                    } else {
                        volverAlInicio();
                    }
                });
            } else {
                volverAlInicio();
            }
            break;
    	case TRINEO:
    		int siguienteTrineo = encontrarSiguienteTrineo(posicion);
    	    if (siguienteTrineo > posicion) {
    	        int distancia = siguienteTrineo - posicion;
    	        eventos.setText("Trineo mágico! Avanzas " + distancia + " casillas.");
    	        moveP1(distancia);
    	    } else {
    	        eventos.setText("Este es el último trineo. Te quedas aquí.");
    	    }
    	    break;
    	case MOTO:
    		int siguienteMoto = encontrarSiguienteMoto(posicion);
    	    if (siguienteMoto > posicion) {
    	        int distanciaM = siguienteMoto - posicion;
    	        eventos.setText("Moto de Nieve! Avanzas " + distanciaM + " casillas.");
    	        moveP1(distanciaM);
    	    } else {
    	        eventos.setText("Esta es la ultima Moto de Nieve. Te quedas aquí.");
    	    }
    		break;
    	case META:
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Final...");
    		alert.setHeaderText("Felicidades...");
    		alert.setContentText("Juego finalizado!!!");
    		alert.showAndWait();
    		break;
    	case SUELO_QUEBRADIZO:
    	    int totalObjetos = contarTotalObjetos();
    	    
    	    if (totalObjetos > 5) {
    	        eventos.setText("¡Tienes demasiados objetos! El suelo quebradizo se rompe y vuelves al inicio.");
    	        p1Position = 0;
    	        GridPane.setRowIndex(P1, 0);
    	        GridPane.setColumnIndex(P1, 0);
    	    } else {
    	        eventos.setText("¡Pasas por el suelo quebradizo sin problema!");
    	    }

    	    perderObjetoAleatorio(); // Aplica el evento adicional
    	    break;

    	}
    	
    }
    
    /**
     * Calcula el número total de objetos recolectados en el juego sumando todos los tipos disponibles.
     * 
     * <p>Este método suma los valores actuales de todos los contadores de objetos:</p>
     * <ul>
     *   <li>Peces ({@code cantidadPeces})</li>
     *   <li>Nieve ({@code cantidadNieve})</li>
     *   <li>Dados rápidos ({@code cantidadDadosRapidos})</li>
     *   <li>Dados lentos ({@code cantidadDadosLentos})</li>
     * </ul>
     * 
     * @return La suma total de todos los objetos recolectados como un entero.
     * 
     * @see #cantidadPeces
     * @see #cantidadNieve
     * @see #cantidadDadosRapidos
     * @see #cantidadDadosLentos
     */
    private int contarTotalObjetos() {
        return cantidadPeces.get() + cantidadNieve.get() + cantidadDadosRapidos.get() + cantidadDadosLentos.get();
    }
    
    /**
     * Elimina un objeto aleatorio del inventario del jugador y muestra un mensaje indicando cuál se perdió.
     * 
     * <p>El método selecciona aleatoriamente entre los tipos de objetos disponibles ("pez", "nieve", "dadoRapido", "dadoLento")
     * y verifica si el jugador tiene al menos una unidad de ese objeto. Si es así, lo elimina y muestra un mensaje
     * en el componente {@code eventos}. Si no hay objetos de ese tipo, el bucle continúa hasta encontrar uno que sí exista.</p>
     * 
     * <p>El método garantiza que siempre se eliminará un objeto si hay al menos uno disponible en el inventario.</p>
     * 
     * <p><strong>Objetos posibles:</strong></p>
     * <ul>
     *   <li><b>pez</b> - Reduce {@code cantidadPeces} en 1 si hay disponibles.</li>
     *   <li><b>nieve</b> - Reduce {@code cantidadNieve} en 1 si hay disponibles.</li>
     *   <li><b>dadoRapido</b> - Reduce {@code cantidadDadosRapidos} en 1 si hay disponibles.</li>
     *   <li><b>dadoLento</b> - Reduce {@code cantidadDadosLentos} en 1 si hay disponibles.</li>
     * </ul>
     * 
     * @implNote Este método utiliza un bucle {@code while} para asegurarse de que se elimine un objeto válido.
     * Si el objeto seleccionado aleatoriamente no está disponible, el bucle continúa hasta encontrar uno que sí lo esté.
     * 
     * @see java.util.Random
     * @see #cantidadPeces
     * @see #cantidadNieve
     * @see #cantidadDadosRapidos
     * @see #cantidadDadosLentos
     * @see #eventos
     */
    private void perderObjetoAleatorio() {
        String[] objetos = new String[] {"pez", "nieve", "dadoRapido", "dadoLento"};
        boolean objetoEliminado = false;
        boolean noPez = false;
        boolean noNieve = false;
        boolean noDadoRapido = false;
        boolean noDadoLento = false;
        
        while (!objetoEliminado || (noPez && noNieve && noDadoRapido && noDadoLento)) {
            int eleccion = rand.nextInt(objetos.length);
            switch (objetos[eleccion]) {
                case "pez":
                    if (cantidadPeces.get() > 0) {
                        cantidadPeces.set(cantidadPeces.get() - 1);
                        eventos.setText("¡Has perdido un pez!");
                        objetoEliminado = true;
                    } else {
                    	 noPez = true;
                    }
                    break;
                case "nieve":
                    if (cantidadNieve.get() > 0) {
                        cantidadNieve.set(cantidadNieve.get() - 1);
                        eventos.setText("¡Has perdido una bola de nieve!");
                        objetoEliminado = true;
                    } else {
                    	noNieve = true;
                    }
         
                    break;
                case "dadoRapido":
                    if (cantidadDadosRapidos.get() > 0) {
                        cantidadDadosRapidos.set(cantidadDadosRapidos.get() - 1);
                        eventos.setText("¡Has perdido un dado rápido!");
                        objetoEliminado = true;
                    } else {
                    	noDadoRapido = true;
                    }
                    break;
                case "dadoLento":
                    if (cantidadDadosLentos.get() > 0) {
                        cantidadDadosLentos.set(cantidadDadosLentos.get() - 1);
                        eventos.setText("¡Has perdido un dado lento!");
                        objetoEliminado = true;
                    } else {
                    	noDadoLento = true;
                    }
                    break;
            }
        }
    }
    
    /**
     * Reinicia la posición del jugador al inicio del tablero.
     * <p>
     * Este método se ejecuta cuando el jugador es atrapado por un oso y debe volver a la casilla inicial.
     * <ul>
     *   <li>Establece la posición del jugador en la casilla 0.</li>
     *   <li>Actualiza la ubicación del jugador en el {@code GridPane} de la interfaz gráfica.</li>
     *   <li>Muestra un mensaje indicando que el jugador ha sido atrapado por un oso y ha regresado al inicio.</li>
     * </ul>
     */
    private void volverAlInicio() {
    	p1Position = 0;
    	GridPane.setRowIndex(P1, 0);
    	GridPane.setColumnIndex(P1, 0);
    	eventos.setText("Un oso te ha atrapado, vuelves al INICIO");
    }
    
    /**
     * Encuentra la posición del siguiente trineo en el tablero a partir de la posición actual.
     * <p>
     * Recorre el tablero en busca de la próxima casilla de tipo {@code TRINEO}, después de la posición
     * dada. Si no encuentra otro trineo, retorna la misma posición actual.
     * <ul>
     *   <li>Identifica el trineo en la posición actual.</li>
     *   <li>Continúa buscando el siguiente trineo en el tablero.</li>
     *   <li>Si encuentra uno, devuelve su posición.</li>
     *   <li>Si no hay más trineos disponibles, mantiene la posición actual.</li>
     * </ul>
     * 
     * @param posActual La posición actual del jugador en el tablero.
     * @return La posición del siguiente trineo, o la misma posición si no hay más trineos.
     */
    private int encontrarSiguienteTrineo(int posActual) {
        boolean encontradoActual = false;

        for (int i = 0; i < tableroCasillas.length; i++) {
            if (tableroCasillas[i] == TipoCasilla.TRINEO) {
                if (!encontradoActual && i == posActual) {
                    encontradoActual = true; // hemos encontrado el trineo actual
                } else if (encontradoActual) {
                    return i; // este es el siguiente trineo, nos detenemos aquí
                }
            }
        }

        // Si no hay siguiente trineo, se queda donde está
        return posActual;
    }
    
    /**
     * Encuentra la siguiente casilla de tipo MOTO en el tablero después de la posición actual.
     *
     * <p>Este método recorre el arreglo {@code tableroCasillas} buscando la próxima
     * casilla de tipo {@code TipoCasilla.MOTO} que aparezca después de la posición
     * especificada. La búsqueda comienza desde el inicio del tablero y devuelve la
     * primera casilla MOTO encontrada después de pasar la posición actual.</p>
     *
     * <p>Si no se encuentra ninguna casilla MOTO posterior, el método devuelve
     * la posición original.</p>
     *
     * <p><strong>Lógica de búsqueda:</strong></p>
     * <ol>
     *   <li>Recorre todas las casillas del tablero comenzando desde el índice 0</li>
     *   <li>Identifica la casilla MOTO en la posición actual (si existe)</li>
     *   <li>Devuelve la siguiente casilla MOTO encontrada después de la posición actual</li>
     *   <li>Si no existe otra casilla MOTO, devuelve la posición original</li>
     * </ol>
     *
     * @param posActual La posición actual en el tablero (índice base 0)
     * @return La posición de la siguiente casilla MOTO, o la posición actual si no se encuentra otra
     *
     * @see TipoCasilla
     * @see #tableroCasillas
     */
    private int encontrarSiguienteMoto(int posActual) {
        boolean encontradoActual = false;

        for (int i = 0; i < tableroCasillas.length; i++) {
            if (tableroCasillas[i] == TipoCasilla.MOTO) {
                if (!encontradoActual && i == posActual) {
                    encontradoActual = true; // hemos encontrado el trineo actual
                } else if (encontradoActual) {
                    return i; // este es el siguiente trineo, nos detenemos aquí
                }
            }
        }

        // Si no hay siguiente trineo, se queda donde está
        return posActual;
    }
    
    /**
     * Busca el agujero más cercano antes de la posición actual en el tablero.
     * <p>
     * Recorre el tablero en dirección inversa desde la posición actual hasta la primera casilla, 
     * identificando la última casilla de tipo {@code AGUJERO} antes de la posición dada.
     * <ul>
     *   <li>Si encuentra un agujero anterior, devuelve su posición.</li>
     *   <li>Si no hay agujeros anteriores, mantiene la posición actual.</li>
     * </ul>
     * 
     * @param posActual La posición actual del jugador en el tablero.
     * @return La posición del agujero más cercano antes de la actual, o la misma posición si no hay agujeros anteriores.
     */
    private int encontrarAnteriorAgujero(int posActual) {
        // Buscamos desde la posición actual hacia atrás
        for (int i = posActual - 1; i >= 0; i--) {
            if (tableroCasillas[i] == TipoCasilla.AGUJERO) {
                return i; // Retorna el agujero anterior más cercano
            }
        }
        return posActual; // Si no hay agujeros anteriores, se queda donde está
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Button and menu actions
    
    /**
     * Maneja la creación de una nueva partida, ofreciendo al jugador la opción de guardar antes de reiniciar el juego.
     * <p>
     * Muestra una ventana de confirmación en la que el jugador puede elegir:
     * <ul>
     *   <li><b>Nueva Partida/Guardar:</b> Guarda la partida actual y luego reinicia el juego.</li>
     *   <li><b>Nueva Partida/No Guardar:</b> Reinicia el juego sin guardar.</li>
     *   <li><b>Cancelar:</b> Cierra la ventana sin realizar ningún cambio.</li>
     * </ul>
     * 
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleNewGame() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Nueva Partida");
        alert.setHeaderText("¿Deseas guardar esta partida antes de crear una nueva?");
        alert.setContentText("Elige una opcion");
        
        ButtonType buttonGuardar = new ButtonType("Nueva Partida/Guardar");
        ButtonType buttonNoGuardar = new ButtonType("Nueva Partida/No Guardar");
        ButtonType buttonCancelar = new ButtonType("Cancelar",  ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(buttonGuardar, buttonNoGuardar);
        Optional<ButtonType> Resultado = alert.showAndWait();
        
        if(Resultado.isPresent()) {
        	if(Resultado.get() == buttonGuardar) {
            	handleSaveGame();
            }
        resetGame();
            
        }
        
    }
    
    ///////////////////////////////////////////////////////////////////
    
    /**
     * Guarda la partida actual en la base de datos, incluyendo inventario y casillas especiales.
     * <p>
     * Este método realiza las siguientes operaciones dentro de una transacción:
     * <ul>
     *   <li>Guarda la partida en la tabla {@code PARTIDA} y obtiene su ID generado.</li>
     *   <li>Guarda el inventario del jugador en la tabla {@code INVENTARIO}.</li>
     *   <li>Guarda las casillas especiales en la tabla {@code CASILLA}.</li>
     *   <li>Si ocurre un error, revierte la transacción para evitar datos corruptos.</li>
     * </ul>
     * <p>
     * Manejo de errores:
     * <ul>
     *   <li>Si hay un problema al obtener el ID de partida, lanza una excepción.</li>
     *   <li>Si ocurre un error en cualquier paso, se realiza un {@code rollback} y 
     *       se restablece {@code setAutoCommit(true)}.</li>
     * </ul>
     * 
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleSaveGame() {
        try (Connection con = saveCon.getConexion()) {
            // Desactivar autocommit para manejar la transacción manualmente
            con.setAutoCommit(false);

            try {
                // 1. Guardar partida y obtener ID
                int idPartida;
                try (PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO PARTIDA (NUM_PARTIDA, DATA_PARTIDA, HORA) " +
                    "VALUES (NUM_PARTIDA_AUTO.NEXTVAL, SYSDATE, TO_CHAR(SYSDATE, 'HH24:MI'))",
                    new String[] {"NUM_PARTIDA"})) {
                    
                    pst.executeUpdate();
                    
                    try (ResultSet rs = pst.getGeneratedKeys()) {
                        if (rs.next()) {
                            idPartida = rs.getInt(1);
                        } else {
                            throw new SQLException("No se pudo obtener el ID de partida generado");
                        }
                    }
                }

                // 2. Guardar el inventario
                try (PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO INVENTARIO (ID_INVENTARIO, BOLAS_NIEVE, PECES, DADOSTOT, DADOSR, DADOSL, ID_JUGADOR, NUM_PARTIDA) " +
                    "VALUES (INVENTARIO_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)")) {
                    
                    pst.setInt(1, cantidadNieve.get());
                    pst.setInt(2, cantidadPeces.get());
                    pst.setInt(3, cantidadDadosRapidos.get() + cantidadDadosLentos.get());
                    pst.setInt(4, cantidadDadosRapidos.get());
                    pst.setInt(5, cantidadDadosLentos.get());
                    pst.setInt(6, 1); // Asumiendo ID_JUGADOR = 1 para el jugador actual
                    pst.setInt(7, idPartida);
                    pst.executeUpdate();
                }

                // 3. Guardar las casillas especiales
                try (PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO CASILLA (NUM_CASILLA, TIPO, NUM_PARTIDA) VALUES (?, ?, ?)")) {
                    
                    for (int i = 0; i < tableroCasillas.length; i++) {
                        if (tableroCasillas[i] != TipoCasilla.NORMAL) {
                            pst.setInt(1, i);
                            pst.setString(2, tableroCasillas[i].toString());
                            pst.setInt(3, idPartida);
                            pst.addBatch();
                        }
                    }
                    pst.executeBatch();
                }

                con.commit();
                eventos.setText("Partida guardada correctamente!");
            } catch (SQLException e) {
                con.rollback();
                eventos.setText("Error al guardar: " + e.getMessage());
                e.printStackTrace();
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            eventos.setText("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
    ///////////////////////////////////////////////////////////////////
    
    /**
     * Carga la última partida guardada desde la base de datos y restaura el estado del juego.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Carga la partida más reciente desde la tabla {@code PARTIDA}.</li>
     *   <li>Recupera la posición del jugador y sus recursos.</li>
     *   <li>Carga las casillas especiales guardadas en la tabla {@code CASILLA}.</li>
     *   <li>Actualiza la interfaz gráfica con los datos cargados.</li>
     * </ul>
     * <p>
     * Manejo de errores:
     * <ul>
     *   <li>Si no hay partidas guardadas, muestra un mensaje indicando que no se encontraron datos.</li>
     *   <li>Si ocurre un error en la consulta o conexión, muestra el mensaje de error correspondiente.</li>
     * </ul>
     * 
     * @param event Evento de acción que desencadena la carga de la partida.
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleLoadGame(ActionEvent event) {
        try (Connection con = saveCon.getConexion()) {
            // 1. Cargar datos principales de la partida
            try (PreparedStatement pst = con.prepareStatement(
                "SELECT * FROM PARTIDA ORDER BY NUM_PARTIDA DESC FETCH FIRST 1 ROWS ONLY")) {
                
                ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    // Restaurar estado principal
                    p1Position = rs.getInt("P1_POSITION");
                    cantidadPeces.set(rs.getInt("CANTIDAD_PECES"));
                    cantidadNieve.set(rs.getInt("CANTIDAD_NIEVE"));
                    int idPartida = rs.getInt("NUM_PARTIDA");

                    // 2. Cargar las casillas especiales
                    try (PreparedStatement pstCasillas = con.prepareStatement(
                        "SELECT NUM_CASILLA, TIPO FROM CASILLA WHERE NUM_PARTIDA = ?")) {
                        
                        pstCasillas.setInt(1, idPartida);
                        ResultSet rsCasillas = pstCasillas.executeQuery();
                        
                        // Resetear todas las casillas a normales
                        Arrays.fill(tableroCasillas, TipoCasilla.NORMAL);
                        
                        // Cargar casillas especiales guardadas
                        while (rsCasillas.next()) {
                            int posicion = rsCasillas.getInt("NUM_CASILLA");
                            String tipo = rsCasillas.getString("TIPO");
                            tableroCasillas[posicion] = TipoCasilla.valueOf(tipo);
                        }
                    }

                    // Actualizar UI
                    Platform.runLater(() -> {
                        // Mover jugador
                        int row = p1Position / COLUMNS;
                        int col = p1Position % COLUMNS;
                        GridPane.setRowIndex(P1, row);
                        GridPane.setColumnIndex(P1, col);
                        
                        // Actualizar contadores
                        peces_t.setText("Peces: " + cantidadPeces.get());
                        nieve_t.setText("Bolas De Nieve: " + cantidadNieve.get());
                        
                    });
                    
                    eventos.setText("Partida cargada con éxito");
                } else {
                    eventos.setText("No hay partidas guardadas");
                }
            }
        } catch (SQLException e) {
            eventos.setText("Error al cargar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Maneja el proceso de salir del juego, permitiendo al jugador guardar la partida antes de cerrar la aplicación.
     * <p>
     * Este método muestra una ventana de confirmación con las siguientes opciones:
     * <ul>
     *   <li><b>Guardar y salir:</b> Guarda la partida actual y luego cierra la aplicación.</li>
     *   <li><b>Salir sin guardar:</b> Cierra la aplicación sin guardar los datos.</li>
     *   <li><b>Cancelar:</b> Cancela la acción de salida y mantiene el juego en ejecución.</li>
     * </ul>
     * <p>
     * Manejo de errores:
     * <ul>
     *   <li>Si el jugador elige guardar antes de salir, se llama al método {@code handleSaveGame()}.</li>
     *   <li>El cierre del juego se realiza con {@code Platform.exit()}.</li>
     * </ul>
     * 
     * @param event Evento de acción que desencadena la salida del juego.
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleQuitGame(ActionEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION); //TIPO DE ALERTA -- en este caso CONFIRMATION
    	//COMPOSICION DE LA ALERTA
    	alert.setTitle("Salir del Juego");
    	alert.setHeaderText("¿Deseas guardar la partida antes de salir?");
    	alert.setContentText("Elige una Opcion:");
    	
    	//BUTTONS QUE VAN A SALIR EN LA PANTALLA SI SELECCIONAMOS ESTA OPCION 
    	ButtonType buttonGuardar = new ButtonType("Guardar y salir");
    	ButtonType buttonSalir = new ButtonType("Salir sin Guardar");
    	ButtonType buttonCancelar = new ButtonType("Cancelar",  ButtonBar.ButtonData.CANCEL_CLOSE);
    	
    	//DONDE SE VAN A MOSTRAR ESTOS BOTONES
    	alert.getButtonTypes().setAll(buttonGuardar, buttonSalir);
    	Optional<ButtonType> resultado = alert.showAndWait(); //HAY QUE IMPORTAR EL OPTIONAL
    	
    	if(resultado.isPresent()) { //ESTO ES YA QUE SOLO SE VA A ENTRAR EN ESTE IF EN CASO DE UNA ACCIÓN
    		if(resultado.get() == buttonGuardar) {//GUARDAR Y SALIR
    			handleSaveGame(); //GUARDAR
    			Platform.exit(); //SALIR
    		} else if (resultado.get() == buttonSalir) { //SALIR SIN GUARDAR
    			Platform.exit(); //SALIR
    		}
    	}
    }
    
    /**
     * Esta variable evita la ejecución simultánea de múltiples efectos en una misma acción, asegurando que solo se procese un efecto a la vez.
     */
    private boolean aplicandoEfecto = false;
    
    /**
     * Mueve al jugador P1 una cantidad determinada de casillas y actualiza su posición en la interfaz gráfica.
     * <p>
     * Este método realiza los siguientes pasos:
     * <ul>
     *   <li>Incrementa la posición del jugador según los pasos dados.</li>
     *   <li>Limita la posición máxima del jugador dentro del tablero.</li>
     *   <li>Calcula la fila y columna correspondientes en la cuadrícula.</li>
     *   <li>Actualiza la posición del jugador en la interfaz de usuario {@code GridPane}.</li>
     *   <li>Si no hay un efecto en curso, activa y aplica el efecto de la casilla actual.</li>
     * </ul>
     * 
     * @param steps Número de casillas que debe avanzar el jugador.
     */
    private void moveP1(int steps) {
        p1Position += steps;

        //Bound player
        if (p1Position >= 50) {
            p1Position = 49; // 5 columns * 10 rows = 50 cells (index 0 to 49)
        }

        //Check row and column
        int row = p1Position / COLUMNS;
        int col = p1Position % COLUMNS;

        //Change P1 property to match row and column
        GridPane.setRowIndex(P1, row);
        GridPane.setColumnIndex(P1, col);
        
        if (!aplicandoEfecto) {
            aplicandoEfecto = true;
            aplicarEfectoCasilla(p1Position);
            aplicandoEfecto = false;
        }
        
        
        //aplicarEfectoCasilla(p1Position);
    }

    /////////////////////////////////DADOS//////////////////////////////////////////
    
    /**
     * Gestiona el lanzamiento de un dado y actualiza la posición del jugador según el resultado.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Genera un número aleatorio entre 1 y 6 simulando el lanzamiento de un dado.</li>
     *   <li>Actualiza el texto de la interfaz para mostrar el resultado obtenido.</li>
     *   <li>Desplaza al jugador en el tablero según el número obtenido en el dado.</li>
     * </ul>
     * 
     * @param event Evento de acción que desencadena el lanzamiento del dado.
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleDado(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;

        // Update the Text 
        dadoResultText.setText("Ha salido: " + diceResult);

        // Update the position
        moveP1(diceResult);
    }
    
    /**
     * Maneja el lanzamiento de un dado rápido, generando un número aleatorio entre 5 y 10
     * y actualizando la posición del jugador en el tablero.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Verifica si el jugador tiene dados rápidos disponibles.</li>
     *   <li>Si no hay dados rápidos, muestra un mensaje de error y finaliza la ejecución.</li>
     *   <li>Genera un número aleatorio entre 5 y 10 simulando el lanzamiento de un dado rápido.</li>
     *   <li>Actualiza el texto de la interfaz con el resultado obtenido.</li>
     *   <li>Desplaza al jugador en el tablero según el número obtenido.</li>
     *   <li>Reduce en uno la cantidad de dados rápidos disponibles.</li>
     * </ul>
     * 
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleRapido() {
        if (cantidadDadosRapidos.get() <= 0) {
        	 eventos.setText("No tienes dados Rapidos");
            return;
        }

        Random r = new Random();
        int Result = r.nextInt(10) + 5; // 5-10
        
        dadoResultText.setText("Ha salido: " + Result);
        moveP1(Result);
        
        //RESTAR EL DADO CUANDO SE EJECUTE LA FUNCION 
        cantidadDadosRapidos.set(cantidadDadosRapidos.get() - 1);
    }
    
    /**
     * Maneja el lanzamiento de un dado lento, generando un número aleatorio entre 1 y 3
     * y actualizando la posición del jugador en el tablero.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Verifica si el jugador tiene dados lentos disponibles.</li>
     *   <li>Si no hay dados lentos, muestra un mensaje y finaliza la ejecución.</li>
     *   <li>Genera un número aleatorio entre 1 y 3 simulando el lanzamiento de un dado lento.</li>
     *   <li>Actualiza el texto de la interfaz con el resultado obtenido.</li>
     *   <li>Desplaza al jugador en el tablero según el número obtenido.</li>
     *   <li>Reduce en uno la cantidad de dados lentos disponibles.</li>
     * </ul>
     * 
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleLento() {
    	if (cantidadDadosLentos.get() <= 0) {
            eventos.setText("No tienes dados Lentos");
            return;
        }
    	
        Random r = new Random();
        int Result = r.nextInt(3)+1; //DADO QUE NOS DA DEL 1 -3
        
        dadoResultText.setText("Ha salido: " + Result); //MOSTRAR MENSAGE
        
        moveP1(Result); //ACTUALIZA LA POSICION
        cantidadDadosLentos.set(cantidadDadosLentos.get() -1);
    }

    ///////////////////////////////////FIN DADOS///////////////////////////////////
    
    /**
     * Muestra el inventario actual del jugador en una ventana de información.
     * <p>
     * Este método presenta los siguientes datos en un cuadro de diálogo:
     * <ul>
     *   <li>Cantidad de dados rápidos.</li>
     *   <li>Cantidad de dados lentos.</li>
     *   <li>Cantidad de peces.</li>
     *   <li>Cantidad de bolas de nieve.</li>
     * </ul>
     * <p>
     * El inventario se muestra en un {@code TextArea} dentro de un cuadro de diálogo {@code Alert}.
     * Se configura para ser solo de lectura y ajustable en tamaño.
     * 
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleInventario() {
    	Alert inventario = new Alert(AlertType.INFORMATION);
    	inventario.setTitle("Inventario");
    	inventario.setHeaderText(null);
    	String contenido = 
    			"Dado Rapido: " + cantidadDadosRapidos.get() + 
    			"\nDados Lentos: " + cantidadDadosLentos.get() + 
    			"\nPeces " + cantidadPeces.get() + 
    			"\nBolas De Nieve " + cantidadNieve.get();
    	TextArea area = new TextArea(contenido);
    	area.setWrapText(true);
    	area.setEditable(false);
    	
    	inventario.getDialogPane().setContent(area);
    	inventario.getDialogPane().setContent(area);
    	inventario.setResizable(true);
    	inventario.setHeaderText(null);
    	inventario.showAndWait();
    }
    
    /**
     * Incrementa en uno la cantidad de peces disponibles en el inventario del jugador.
     * <p>
     * Este método se utiliza para actualizar la cantidad de peces, reflejando el aumento en la interfaz.
     * 
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handlePeces() {
        cantidadPeces.set(cantidadPeces.get() + 1);
    }
    
    /**
     * Incrementa en uno la cantidad de bolas de nieve disponibles en el inventario del jugador.
     * <p>
     * Este método se utiliza para actualizar la cantidad de bolas de nieve, reflejando el aumento en la interfaz.
     */
    @FXML
    private void handleNieve() {
        cantidadNieve.set(cantidadNieve.get() + 1);
    }
    
    /**
     * Reinicia el estado del juego, restaurando los valores iniciales del jugador y el tablero.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Restablece la posición del jugador a la casilla inicial.</li>
     *   <li>Resetea los recursos del inventario del jugador (bolas de nieve, peces, dados rápidos y lentos).</li>
     *   <li>Elimina todas las imágenes del tablero para limpiarlo visualmente.</li>
     *   <li>Inicializa nuevamente el tablero con sus configuraciones predeterminadas.</li>
     *   <li>Actualiza la interfaz gráfica con un mensaje indicando que una nueva partida ha comenzado.</li>
     * </ul>
     */
    public void resetGame() {
    	p1Position = 0;
    	
    	GridPane.setRowIndex(P1, 0);
        GridPane.setColumnIndex(P1, 0);
    	
        cantidadNieve.set(0);
        cantidadPeces.set(0);
        cantidadDadosLentos.set(0);
        cantidadDadosRapidos.set(0);
        
        //LIMPIAR EL TABLERO
        tablero.getChildren().removeIf(node -> node instanceof ImageView); //NODE REPRESENTA (imagenes/botones...) node instanceof ImageView EVALUA SI EL nodo ES UNA IMAGEN 
        inicializarTablero();//PONE OTRA VEZ LAS IMAGENES DEL TABLERO
        
        dadoResultText.setText("");
        eventos.setText("Nueva Partida Iniciada");
    }
    
    ///////////////////PARA CARGAR IMAGENES///////////////////////////////
    
    /**
     * Muestra las imágenes de los agujeros en el tablero, colocando una representación visual en cada casilla de tipo {@code AGUJERO}.
     * <p>
     * Este método recorre el tablero buscando casillas de tipo {@code AGUJERO} y, por cada una encontrada:
     * <ul>
     *   <li>Calcula su posición en la cuadrícula.</li>
     *   <li>Carga la imagen correspondiente desde los recursos.</li>
     *   <li>Configura el tamaño de la imagen para ajustarse correctamente.</li>
     *   <li>Agrega la imagen al tablero en la posición calculada.</li>
     * </ul>
     */
    private void mostrarImagenesAgujero() {
    	for(int i = 0; i < tableroCasillas.length; i++) {
    		if(tableroCasillas[i] == TipoCasilla.AGUJERO) {
    			int row = i / COLUMNS;
                int col = i % COLUMNS;
                
                Image image = new Image(getClass().getResource("/Resources/agujero.png").toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40); //PARA AJUSTAR EL TAMAÑO
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);

                tablero.add(imageView, col, row);
    		}
    	}
    }
    
    /**
     * Muestra las imágenes de los osos en el tablero, colocando una representación visual en cada casilla de tipo {@code OSO}.
     * <p>
     * Este método recorre el tablero buscando casillas de tipo {@code OSO} y, por cada una encontrada:
     * <ul>
     *   <li>Calcula su posición en la cuadrícula.</li>
     *   <li>Carga la imagen correspondiente desde los recursos.</li>
     *   <li>Configura el tamaño de la imagen para ajustarse correctamente.</li>
     *   <li>Agrega la imagen al tablero en la posición calculada.</li>
     * </ul>
     */
    private void mostrarImagenesOso() {
    	for(int i = 0; i < tableroCasillas.length; i++) {
    		if(tableroCasillas[i] == TipoCasilla.OSO) {
    			int row = i / COLUMNS;
    			int col = i % COLUMNS;
    			
    			Image image = new Image(getClass().getResource("/Resources/oso.png").toExternalForm());
    			ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40); //PARA AJUSTAR EL TAMAÑO
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                
                tablero.add(imageView, col, row);
    		}
    	}
    }
    
    /**
     * Muestra las imágenes de las casillas de interrogante en el tablero, colocando una representación visual en cada casilla de tipo {@code INTERROGANTE}.
     * <p>
     * Este método recorre el tablero buscando casillas de tipo {@code INTERROGANTE} y, por cada una encontrada:
     * <ul>
     *   <li>Calcula su posición en la cuadrícula.</li>
     *   <li>Carga la imagen correspondiente desde los recursos.</li>
     *   <li>Configura el tamaño de la imagen para ajustarse correctamente.</li>
     *   <li>Agrega la imagen al tablero en la posición calculada.</li>
     * </ul>
     */
    private void mostrarImagenesInterrogante() {
    	for(int i = 0; i < tableroCasillas.length; i++) {
    		if(tableroCasillas[i] == TipoCasilla.INTERROGANTE) {
    			int row = i / COLUMNS;
    			int col = i % COLUMNS;
    			
    			Image image = new Image(getClass().getResource("/Resources/interrogante.png").toExternalForm());
    			ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40); //PARA AJUSTAR EL TAMAÑO
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                
                tablero.add(imageView, col, row);
    		}
    	}
    }
    
    /**
     * Muestra las imágenes de los trineos en el tablero, colocando una representación visual en cada casilla de tipo {@code TRINEO}.
     * <p>
     * Este método recorre el tablero buscando casillas de tipo {@code TRINEO} y, por cada una encontrada:
     * <ul>
     *   <li>Calcula su posición en la cuadrícula.</li>
     *   <li>Carga la imagen correspondiente desde los recursos.</li>
     *   <li>Configura el tamaño de la imagen para ajustarse correctamente.</li>
     *   <li>Agrega la imagen al tablero en la posición calculada.</li>
     * </ul>
     */
    private void mostrarImagenesTrineo() {
    	for(int i = 0; i < tableroCasillas.length; i++) {
    		if(tableroCasillas[i] == TipoCasilla.TRINEO) {
    			int row = i / COLUMNS;
    			int col = i % COLUMNS;
    			
    			Image image = new Image(getClass().getResource("/Resources/trineo.png").toExternalForm());
    			ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40); //PARA AJUSTAR EL TAMAÑO
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                
                tablero.add(imageView, col, row);
    		}
    	}
    }
    
    /**
     * Muestra las imágenes de moto en las casillas correspondientes del tablero.
     * 
     * <p>Este método recorre todas las casillas del tablero ({@code tableroCasillas}) y para cada
     * casilla de tipo {@code TipoCasilla.MOTO}, carga la imagen de una moto desde los recursos
     * y la añade al componente gráfico del tablero en la posición correspondiente.</p>
     * 
     * <p><strong>Funcionamiento:</strong></p>
     * <ul>
     *   <li>Recorre todas las casillas del tablero mediante un bucle</li>
     *   <li>Para cada casilla de tipo MOTO:
     *     <ul>
     *       <li>Calcula su posición (fila y columna) en el grid del tablero</li>
     *       <li>Carga la imagen "moto.png" desde la carpeta de recursos</li>
     *       <li>Crea un ImageView con la imagen y ajusta su tamaño (40x40 píxeles)</li>
     *       <li>Añade la imagen al tablero en la posición calculada</li>
     *     </ul>
     *   </li>
     * </ul>
     * 
     * <p>La imagen se redimensiona manteniendo su relación de aspecto original.</p>
     * 
     * @see TipoCasilla#MOTO
     * @see #tableroCasillas
     * @see #tablero
     * @see javafx.scene.image.Image
     * @see javafx.scene.image.ImageView
     */
    private void mostrarImagenesMoto() {
    	for(int i = 0; i < tableroCasillas.length; i++) {
    		if(tableroCasillas[i] == TipoCasilla.MOTO) {
    			int row = i / COLUMNS;
    			int col = i % COLUMNS;
    			
    			Image image = new Image(getClass().getResource("/Resources/moto.png").toExternalForm());
    			ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40); //PARA AJUSTAR EL TAMAÑO
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                
                tablero.add(imageView, col, row);
    		}
    	}
    }
    
    /**
     * Muestra la imagen de la meta en el tablero, colocando una representación visual en cada casilla de tipo {@code META}.
     * <p>
     * Este método recorre el tablero buscando casillas de tipo {@code META} y, por cada una encontrada:
     */
    private void mostrarImagenesMeta() {
    	for(int i = 0; i  < tableroCasillas.length; i++) {
    		if(tableroCasillas[i] == TipoCasilla.META) {
    			int row = i / COLUMNS;
    			int col = i % COLUMNS;
    			
    			Image image = new Image(getClass().getResource("/Resources/meta.png").toExternalForm());
    			ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40); //PARA AJUSTAR EL TAMAÑO
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                
                tablero.add(imageView, col, row);
    		}
    	}
    }
    
    /**
     * Muestra las imágenes de suelo quebradizo en las casillas correspondientes del tablero.
     * 
     * <p>Este método recorre todas las casillas del tablero ({@code tableroCasillas}) buscando
     * las de tipo {@code TipoCasilla.SUELO_QUEBRADIZO} para colocar la imagen representativa
     * en cada una de ellas.</p>
     * 
     * <p><strong>Proceso detallado:</strong></p>
     * <ul>
     *   <li>Itera a través de todas las casillas del tablero</li>
     *   <li>Para cada casilla de tipo SUELO_QUEBRADIZO:
     *     <ul>
     *       <li>Calcula su posición (fila y columna) en el grid del tablero</li>
     *       <li>Carga la imagen "sueloQ.png" desde la carpeta de recursos (/Resources)</li>
     *       <li>Crea un ImageView con la imagen cargada</li>
     *       <li>Configura el tamaño de la imagen a 40x40 píxeles manteniendo la proporción</li>
     *       <li>Añade la imagen al componente {@code tablero} en la posición calculada</li>
     *     </ul>
     *   </li>
     * </ul>
     * 
     * <p>La imagen se ajusta al tamaño especificado conservando su relación de aspecto original.</p>
     * 
     * @see TipoCasilla#SUELO_QUEBRADIZO
     * @see #tableroCasillas
     * @see #tablero
     * @see #COLUMNS
     * @see javafx.scene.image.Image
     * @see javafx.scene.image.ImageView
     */
    private void mostrarImagenesSueloQ() {
    	for(int i = 0; i  < tableroCasillas.length; i++) {
    		if(tableroCasillas[i] == TipoCasilla.SUELO_QUEBRADIZO) {
    			int row = i / COLUMNS;
    			int col = i % COLUMNS;
    			
    			Image image = new Image(getClass().getResource("/Resources/sueloQ.png").toExternalForm());
    			ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40); //PARA AJUSTAR EL TAMAÑO
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                
                tablero.add(imageView, col, row);
    		}
    	}
    }
    
    /////////////////////////////PARA pantallaMenu handleLoadGame //////////////////////
    
    /**
     * Carga una partida previamente guardada, estableciendo la posición del jugador y sus recursos.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Establece la posición del jugador en el tablero.</li>
     *   <li>Actualiza la cantidad de peces y bolas de nieve en el inventario.</li>
     *   <li>Posiciona gráficamente al jugador en la cuadrícula correspondiente.</li>
     *   <li>Actualiza los textos de la interfaz para reflejar los valores restaurados.</li>
     *   <li>Muestra un mensaje indicando que la partida ha sido cargada correctamente.</li>
     * </ul>
     * 
     * @param posicion La posición del jugador en el tablero.
     * @param peces La cantidad de peces disponibles en el inventario del jugador.
     * @param nieve La cantidad de bolas de nieve disponibles en el inventario del jugador.
     */
    public void iniciarPartidaCargada(int posicion, int peces, int nieve) {
        // 1. Posición y recursos
        p1Position = posicion;
        cantidadPeces.set(peces);
        cantidadNieve.set(nieve);
        
        // 2. Posicionar jugador
        int fila = posicion / COLUMNS;
        int columna = posicion % COLUMNS;
        GridPane.setRowIndex(P1, fila);
        GridPane.setColumnIndex(P1, columna);
        
        // 3. Actualizar textos
        peces_t.setText("Peces: " + peces);
        nieve_t.setText("Nieve: " + nieve);
        eventos.setText("Partida cargada!");
    }
    
}