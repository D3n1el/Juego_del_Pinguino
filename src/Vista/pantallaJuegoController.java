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

public class pantallaJuegoController {

	public enum TipoCasilla { //ES UN ENNUMERADOR DE VARIABLES ESTATICAS, ES COMO UN DESPLEGABLE DE OPCIONES
		NORMAL,
		AGUJERO,
		INTERROGANTE,
		OSO,
		TRINEO,
		META
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
    @FXML private Circle P2;
    @FXML private Circle P3;
    @FXML private Circle P4;
    
    
    
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

    private void inicializarTablero() {
    	Arrays.fill(tableroCasillas, TipoCasilla.NORMAL); //ESTO HACE QUE TODAS LAS CASILLAS SEAN NORMALES POR DEFECTO
    	
    	//ESTO ES UNA DISTRIBUCION DE CASILLAS ESPECIALES (SE PUEDE AJUSTAR A MENOS CASILLAS O MAS)
    	colocarCasillasEspeciales(TipoCasilla.AGUJERO, 4);
    	colocarCasillasEspeciales(TipoCasilla.INTERROGANTE, 5);
    	colocarCasillasEspeciales(TipoCasilla.OSO, 2);
    	colocarCasillasEspeciales(TipoCasilla.TRINEO, 4);
    	
    	//LA CASILLA INICIAL SIEMPRE SERA UNA CASILLA NORMAL
    	tableroCasillas[0] = TipoCasilla.NORMAL;
    	tableroCasillas[49] = TipoCasilla.META;
    	
    	//PARA MOSTRAR LAS IMAGENES EN EL TABLERO
    	mostrarImagenesAgujero();
    	mostrarImagenesOso();
    	mostrarImagenesInterrogante();
    	mostrarImagenesTrineo();
    	mostrarImagenesMeta();
    }
    
    private void colocarCasillasEspeciales(TipoCasilla tipo, int cantidad) { //Garantiza que solo se modifiquen casillas que actualmente son de tipo NORMAL, preservando otros tipos de casillas especiales que ya pudieran estar en el tablero.
    	for (int i = 0; i < cantidad; i++) {
    		
    			int posicion;
    			
    		do {
    			posicion = rand.nextInt(tableroCasillas.length - 1) + 1; //NO PONER EN CASILLA 0 rand.nextInt GENERA UNA POSICION aleatoria y la guarda en una variable.
    			
    		} while(tableroCasillas[posicion] != TipoCasilla.NORMAL); //Este do-while no parará hasta encontrar aleatoriamente una casilla tipo NORMAL.
    			tableroCasillas[posicion] = tipo;
    	}
    }
    
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
    	case META:
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Final...");
    		alert.setHeaderText("Felicidades...");
    		alert.setContentText("Juego finalizado!!!");
    		alert.showAndWait();
    	}
    }
    
    private void volverAlInicio() {
    	p1Position = 0;
    	GridPane.setRowIndex(P1, 0);
    	GridPane.setColumnIndex(P1, 0);
    	eventos.setText("Un oso te ha atrapado, vuelves al INICIO");
    }
    
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
    @FXML
    private void handleSaveGame() {
        try (Connection con = saveCon.getConexion()) {
            // 1. Guardar partida y obtener ID
            int idPartida;
            try (PreparedStatement pst = con.prepareStatement( //PreparedStatement se utiliza para ejecutar consultas SQL precompiladas
                "INSERT INTO PARTIDA (NUM_PARTIDA, DATA_PARTIDA, HORA, P1_POSITION, CANTIDAD_PECES, CANTIDAD_NIEVE) " +
                "VALUES (NUM_PARTIDA_AUTO.NEXTVAL, SYSDATE, TO_CHAR(SYSDATE, 'HH24:MI'), ?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS)) {
                
                pst.setInt(1, p1Position); //Representa el primer "?"
                pst.setInt(2, cantidadPeces.get());
                pst.setInt(3, cantidadNieve.get());
                pst.executeUpdate();
                
                try (ResultSet rs = pst.getGeneratedKeys()) { //Recupera claves generadas automÃ¡ticamente por la base de datos (en este caso, el valor de NUM_PARTIDA generado por NUM_PARTIDA_AUTO.NEXTVAL)
                    if (rs.next()) { //Si consigue obtener una clave, se obtiene el ID con rs.getInt(1) y se almacena en idPartida.
                        idPartida = rs.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de partida generado");
                    }
                }
            }

            // 2. Guardar TODAS las casillas especiales
            try (PreparedStatement pst = con.prepareStatement(
                "INSERT INTO CASILLA (NUM_CASILLA, TIPO, NUM_PARTIDA) VALUES (?, ?, ?)")) {
                
                for (int i = 0; i < tableroCasillas.length; i++) {
                    if (tableroCasillas[i] != TipoCasilla.NORMAL) {
                        pst.setInt(1, i); // Posición en el tablero
                        pst.setString(2, tableroCasillas[i].toString()); // Tipo de casilla
                        pst.setInt(3, idPartida); // ID de partida
                        pst.addBatch();
                    }
                }
                pst.executeBatch();
            }

            eventos.setText("Partida guardada correctamente!");
        } catch (SQLException e) {
            eventos.setText("Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    ///////////////////////////////////////////////////////////////////
    
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
    
    private boolean aplicandoEfecto = false;
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
    
    @FXML
    private void handleDado(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;

        // Update the Text 
        dadoResultText.setText("Ha salido: " + diceResult);

        // Update the position
        moveP1(diceResult);
    }
    
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
    
    @FXML
    private void handlePeces() {
        cantidadPeces.set(cantidadPeces.get() + 1);
    }

    @FXML
    private void handleNieve() {
        cantidadNieve.set(cantidadNieve.get() + 1);
    }
    
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
    
    /////////////////////////////PARA pantallaMenu handleLoadGame //////////////////////
    
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