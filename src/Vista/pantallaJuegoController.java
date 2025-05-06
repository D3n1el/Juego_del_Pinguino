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
		TRINEO
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

    // Texts
    @FXML private Text dadoResultText;
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
    private final int COLUMNS = 5;
    private Random rand = new Random(); //PARA LAS CASILLAS
    
    private static final int TOTAL_CASILLAS = 50; //ES UNA CADENA CONSTANTE 
    private TipoCasilla[] tableroCasillas = new TipoCasilla[TOTAL_CASILLAS]; //PARA LAS CASILLAS
    private IntegerProperty cantidadPeces = new SimpleIntegerProperty(0);
    private IntegerProperty cantidadNieve = new SimpleIntegerProperty(0);

    @FXML
    private void initialize() {
        // This method is called automatically after the FXML is loaded
        // You can set initial values or add listeners here
        eventos.setText("¡El juego ha comenzado!");
        peces_t.textProperty().bind(Bindings.concat("Peces: ", cantidadPeces.asString()));
        nieve_t.textProperty().bind(Bindings.concat("Bolas De Nieve: ", cantidadNieve.asString()));
        
        inicializarTablero();//INICIALIZAR EL TABLERO
    }

    private void inicializarTablero() {
    	Arrays.fill(tableroCasillas, TipoCasilla.NORMAL);//ESTO HACE QUE TODAS LAS CASILLAS SEAN NORMALES POR DEFECTO
    	
    	//ESTO ES UNA DISTRIBUCION DE CASILLAS ESPECIALES (SE PUEDE AJUSTAR A MENOS CASILLAS O MAS)
    	colocarCasillasEspeciales(TipoCasilla.AGUJERO, 8);
    	colocarCasillasEspeciales(TipoCasilla.INTERROGANTE, 10);
    	colocarCasillasEspeciales(TipoCasilla.OSO, 3);
    	colocarCasillasEspeciales(TipoCasilla.TRINEO, 4);
    	
    	//LA CASILLA INICIAL SIEMPRE SERA UNA CASILLA NORMAL
    	tableroCasillas[0] = TipoCasilla.NORMAL;
    	
    	//PARA MOSTRAR LAS IMAGENES EN EL TABLERO
    	mostrarImagenesAgujero();
    	mostrarImagenesOso();
    	mostrarImagenesInterrogante();
    	mostrarImagenesTrineo();
    }
    
    private void colocarCasillasEspeciales(TipoCasilla tipo, int cantidad) {
    	for(int i = 0; i < cantidad; i++) {
    		int posicion;
    		do {
    			posicion = rand.nextInt(tableroCasillas.length - 1) + 1; //NO PONER EN CASILLA 0
    			
    		}while(tableroCasillas[posicion] != TipoCasilla.NORMAL);
    		
    			tableroCasillas[posicion] = tipo;
    		
    	}
    }
    
    private void aplicarEfectoCasilla(int posicion) {
    	TipoCasilla casilla = tableroCasillas[posicion];
    	
    	switch(casilla) {
    	case AGUJERO:
    		eventos.setText("Caiste en un Agujero..." + " Retrocedes 1 Casilla");
    		moveP1(-1); //CANVIAR AL APLICAR MAS PERSONAS
    		break;
    	case INTERROGANTE: 
    		if(rand.nextBoolean()) {
    			if(cantidadNieve.get() >= 6) { //COMPROBAR QUE NO SUPERE EL MAXIMO DE BOLAS DE NIEVE
    				cantidadNieve.set(6);
    				eventos.setText("Ya tienes el maximo de Nieve possible " + cantidadNieve.get());
    			}else { //EN CASO DE QUE NO SUPERE EL LIMITE
    				int nieve = rand.nextInt(3) + 1;
        			cantidadNieve.set(cantidadNieve.get() + nieve);
        			eventos.setText("Has conseguido " + nieve + " Bolas de Nieve!!!");
    			}
    		}else {
    			if(cantidadPeces.get() >= 2 ) { //COMPROBAR QUE NO TENGA MAS DE 2 PECES
    				eventos.setText("Ya tienes el maximo de peces " + cantidadPeces.get());
    			}else { //EN CASO DE QUE TENGA MAS DE 2 PECES
    				cantidadPeces.set(cantidadPeces.get() + 1);
        			eventos.setText("Has conseguido 1 Pez!!!");
    			}
    		}
    		break;
    	case OSO:
    		if (cantidadPeces.get() > 0) {
                Platform.runLater(() -> {
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
    		int distancia = siguienteTrineo - posicion;
    		eventos.setText("Avanzas " + distancia + " Casillas");
    		moveP1(distancia);
    		break;
    	}
    }
    
    private void volverAlInicio() {
    	p1Position = 0;
    	GridPane.setRowIndex(P1, 0);
    	GridPane.setColumnIndex(P1, 0);
    	eventos.setText("Un oso te ha atrapado, vuelves al INICIO");
    }
    
    private int encontrarSiguienteTrineo(int posActual) {
    	for (int i = posActual + 1; i < tableroCasillas.length; i++) {
            if (tableroCasillas[i] == TipoCasilla.TRINEO) {
                return i;
            }
        }
    	return tableroCasillas.length - 1;
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
            	resetGame();
            }else if(Resultado.get() == buttonNoGuardar) {
            	resetGame();
            }
        }
        
    }

    ///////////////////////////////////////////////////////////////////
    @FXML
    private void handleSaveGame() {
        try (Connection con = saveCon.getConexion()) {
            // 1. Guardar partida y obtener ID
            int idPartida;
            try (PreparedStatement pst = con.prepareStatement(
                "INSERT INTO PARTIDA (NUM_PARTIDA, DATA_PARTIDA, HORA, P1_POSITION, CANTIDAD_PECES, CANTIDAD_NIEVE) " +
                "VALUES (NUM_PARTIDA_AUTO.NEXTVAL, SYSDATE, CURRENT_TIMESTAMP, ?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS)) {
                
                pst.setInt(1, p1Position);
                pst.setInt(2, cantidadPeces.get());
                pst.setInt(3, cantidadNieve.get());
                pst.executeUpdate();
                
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
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
    		}else if(resultado.get() == buttonSalir) { //SALIR SIN GUARDAR
    			Platform.exit(); //SALIR
    		}
    	}
    }

    @FXML
    private void handleDado(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;

        // Update the Text 
        dadoResultText.setText("Ha salido: " + diceResult);

        // Update the position
        moveP1(diceResult);
    }

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
        
        aplicarEfectoCasilla(p1Position);
    }

    @FXML
    private void handleRapido() {
        Random r = new Random();
        int Result = r.nextInt(10) + 5; //DADO QUE NOS DA DEL 5 - 10
        
        dadoResultText.setText("Dado Rapido, Resultado: " + Result); //MOSTRAMOS MENSAGE
        
        moveP1(Result); //ACTUALIZAMOS LA POSICION
    }

    @FXML
    private void handleLento() {
        Random r = new Random();
        int Result = r.nextInt(3)+1; //DADO QUE NOS DA DEL 1 -3
        
        dadoResultText.setText("Dado Lento, Resultado: " + Result); //MOSTRAR MENSAGE
        
        moveP1(Result); //ACTUALIZA LA POSICION
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
    	
        cantidadPeces.set(0);
        cantidadPeces.set(0);
        
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
    			
    			Image image = new Image(getClass().getResource("/Resources/oso.jpg").toExternalForm());
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
}