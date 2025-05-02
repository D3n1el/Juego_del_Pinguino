package Vista;

import java.util.Random;

import Controlador.saveCon;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.util.Optional;

public class pantallaJuegoController {

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
    
    private IntegerProperty cantidadPeces = new SimpleIntegerProperty(0);
    private IntegerProperty cantidadNieve = new SimpleIntegerProperty(0);
    
    @FXML
    private void initialize() {
        // This method is called automatically after the FXML is loaded
        // You can set initial values or add listeners here
        eventos.setText("¡El juego ha comenzado!");
        peces_t.textProperty().bind(Bindings.concat("Peces: ", cantidadPeces.asString()));
        nieve_t.textProperty().bind(Bindings.concat("Bolas De Nieve: ", cantidadNieve.asString()));
    }

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

    @FXML
    private void handleSaveGame() {
    	Connection con = saveCon.getConexion(); //No se si hace falta realmente en este apartado
    	
    	String sql = "INSERT INTO PARTIDA (NUM_PARTIDA, DATA_PARTIDA, HORA) " +
                "VALUES (NUM_PARTIDA_AUTO.NEXTVAL, TRUNC(SYSDATE), TO_CHAR(SYSDATE, 'HH24:MI:SS'))";
    }

    @FXML
    private void handleLoadGame() {
        System.out.println("Loaded game.");
        // TODO
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
    
    private void resetGame() {
    	p1Position = 0;
    	
    	GridPane.setRowIndex(P1, 0);
        GridPane.setColumnIndex(P1, 0);
    	
        cantidadPeces.set(0);
        cantidadPeces.set(0);
        
        dadoResultText.setText("");
        eventos.setText("Nueva Partida Iniciada");
    }
    
}
