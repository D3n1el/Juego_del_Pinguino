package Vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import Controlador.bbdd;
import Controlador.saveCon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import javafx.event.ActionEvent;

public class pantallaMenuController {
	@FXML private MenuItem newGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;

    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button Reglas;
    
    @FXML private Text eventos;

    @FXML
    private void initialize() {
        // This method is called automatically after the FXML is loaded
        // You can set initial values or add listeners here
        System.out.println("pantallaPrincipalController initialized");
    }

    @FXML
    private void handleNewGame(ActionEvent event) {
    	String usuario = saveCon.getUser();
        try {
            // 1. Crear nueva partida en la base de datos
            try (Connection con = saveCon.getConexion();
                 PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO PARTIDA (NUM_PARTIDA, DATA_PARTIDA, HORA) " +
                     "VALUES (NUM_PARTIDA_AUTO.NEXTVAL, SYSDATE, CURRENT_TIMESTAMP)",
                     new String[]{"NUM_PARTIDA"})) {  // Especifica el nombre de la columna para el ID
                
                pst.executeUpdate(); //Ejecuta la sentencia SQL seleccionada con preparedStatement.
                
                String sqlUpdatePJugadas = "UPDATE JUGADOR SET NUM_PARTIDAS_JUGADAS = NUM_PARTIDAS_JUGADAS + 1 WHERE nickname = '" + usuario + "'";
                bbdd.update(con, sqlUpdatePJugadas);
                
                
                
                // Obtener el ID de la nueva partida
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        // Asegúrate de leer el tipo correcto (puede ser String o Number)
                        Object idObj = rs.getObject(1);
                        int idPartida;
                        
                        if (idObj instanceof Number) {
                            idPartida = ((Number)idObj).intValue();
                        } else {
                            // Si es String, intenta convertirlo
                            idPartida = Integer.parseInt(idObj.toString());
                        }
                        saveCon.setNumPartida(idPartida);
                        System.out.println("Nueva partida creada con ID: " + idPartida);
                    }
                }
            }

            // 2. Cargar la pantalla de juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/pantallaJuego.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Esta línea obtiene el Stage (ventana principal) asociado al nodo que desencadenó el evento.
            /* getSource Devuelve el objeto que desencadenó el evento, como un botón.
             * getScene Devuelve la ventana que contiene la escena.
             * getWindow Devuelve la ventana que contiene la escena.
             */
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (SQLException e) {
            mostrarAlerta("Error de Base de Datos", "No se pudo crear la partida: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            mostrarAlerta("Error de Aplicación", "No se pudo cargar la pantalla de juego");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Datos", "Formato incorrecto del ID de partida");
            e.printStackTrace();
        }
    }

    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    @FXML
    public void handleReglas(ActionEvent event) {
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Reglas del Juego");
        alerta.setHeaderText(" Reglas del El Juego del Pingüino ");

        String contenido = 
        		" Objetivo del juego:\r\n"
        		+ "        Ser el primer jugador en llegar a la casilla 50, avanzando por el tablero, superando obstáculos y usando tu inventario con estrategia.\r\n"
        		+ "\r\n"
        		+ "        Inventario de cada jugador:\r\n"
        		+ "        - Hasta 3 dados normales o especiales (guardados en base de datos).\r\n"
        		+ "        - Hasta 2 peces, que sirven para sobornar al oso y evitar volver al inicio.\r\n"
        		+ "        - Hasta 6 bolas de nieve, que puedes usar para hacer retroceder a otros jugadores.\r\n"
        		+ "\r\n"
        		+ "        Mecánica de turnos:\r\n"
        		+ "        En cada turno, puedes:\r\n"
        		+ "        - Lanzar un dado normal o uno especial.\r\n"
        		+ "        - Usar una bola de nieve para atacar a otro jugador.\r\n"
        		+ "\r\n"
        		+ "        Tipos de casillas en el tablero:\r\n"
        		+ "        - Pingüino: Representa la ficha del jugador (color, nombre...).\r\n"
        		+ "        - Oso polar: Si te atrapa, vuelves al inicio del juego.\r\n"
        		+ "          → Puedes sobornarlo con un pez para evitarlo.\r\n"
        		+ "        - Agujero en el hielo: Te hace retroceder al agujero anterior.\r\n"
        		+ "        - Trineo: Te permite avanzar al siguiente trineo del tablero.\r\n"
        		+ "        - Casilla de interrogación: Activa un evento aleatorio.\r\n"
        		+ "\r\n"
        		+ "        Eventos aleatorios:\r\n"
        		+ "        - Ganar 1 pez.\r\n"
        		+ "        - Ganar 1 a 3 bolas de nieve.\r\n"
        		+ "        - Ganar un dado rápido (avanza entre 5 y 10 casillas, baja probabilidad).\r\n"
        		+ "        - Ganar un dado lento (avanza entre 1 y 3 casillas, alta probabilidad).\r\n"
        		+ "\r\n"
        		+ "        Guardado y carga de la partida:\r\n"
        		+ "        Puedes guardar y cargar la partida desde el menú.\r\n"
        		+ "        La información se guarda en una base de datos encriptada e incluye:\r\n"
        		+ "        - Posición de los jugadores.\r\n"
        		+ "        - Estado del tablero.\r\n"
        		+ "        - Inventario de cada jugador.";
            
            

        TextArea textArea = new TextArea(contenido);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane content = new GridPane();
        content.setMaxWidth(Double.MAX_VALUE);
        content.add(textArea, 0, 0);

        alerta.getDialogPane().setContent(content);
        alerta.showAndWait();
    }


    @FXML
    private void handleLoadGame(ActionEvent event) {
        try {
            // 1. Cargar datos básicos de la última partida
            Connection con = saveCon.getConexion();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT * FROM PARTIDA ORDER BY NUM_PARTIDA DESC FETCH FIRST 1 ROWS ONLY");

            
            if (rs.next()) {
                // 2. Obtener datos guardados
                int posicion = rs.getInt("P1_POSITION");
                int peces = rs.getInt("CANTIDAD_PECES");
                int nieve = rs.getInt("CANTIDAD_NIEVE");

                // 3. Cargar pantalla de juego
                FXMLLoader loader = new FXMLLoader(getClass().getResource("pantallaJuego.fxml"));
                Parent root = loader.load();
                
                // 4. Pasar datos al controlador del juego
                pantallaJuegoController juegoController = loader.getController();
                juegoController.iniciarPartidaCargada(posicion, peces, nieve);
                
                // 5. Mostrar pantalla de juego
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
                
            } else {
            	
            	mostrarAlerta("Información", "No hay partidas guardadas");
            	
            }
        } catch (Exception e) {
        	mostrarAlerta("Error", "Error al cargar partida: " + e.getMessage()); 
            e.printStackTrace(); //proporciona información detallada sobre la excepción que ocurrió, incluyendo el tipo de excepción y un mensaje descriptivo. Esto ayuda a identificar rápidamente qué salió mal.
        }
    }

    @FXML
    private void handleQuitGame(ActionEvent event) {
        Platform.exit();
    }
    
}