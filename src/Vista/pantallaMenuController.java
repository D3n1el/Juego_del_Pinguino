package Vista;

/**
 * Clase que define el código para el funcionamiento de la ventana donde salen los botones de cargar, guardar y nueva partida en front end.
 * 
 * @author DAVO
 * @version 16/05/2025
 * 
 * @see javafx.fxml.FXML; importa la anotación "@FXML" de JavaFX.
 * @see import javafx.scene.control.Button; Importa la clase Button de JavaFX, que permite crear y usar botones en front end.
 */
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
    
    /**
     * Método de inicialización que se ejecuta automáticamente después de que el archivo FXML ha sido cargado.
     * <p>
     * Este método se usa para establecer valores iniciales, agregar listeners o realizar configuraciones necesarias antes
     * de que la interfaz gráfica esté completamente interactiva.
     * <ul>
     *   <li>Se ejecuta después de que los componentes de FXML han sido instanciados.</li>
     *   <li>Ideal para inicializar datos y ajustar elementos de la interfaz.</li>
     *   <li>Puede registrar eventos y configurar el estado inicial de la aplicación.</li>
     * </ul>
     * 
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML y es llamado automáticamente.
     */
    @FXML
    private void initialize() {
        // This method is called automatically after the FXML is loaded
        // You can set initial values or add listeners here
        System.out.println("pantallaPrincipalController initialized");
    }
    
    /**
     * Crea una nueva partida en la base de datos y carga la pantalla de juego.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Obtiene el usuario actual.</li>
     *   <li>Inserta una nueva partida en la tabla {@code PARTIDA} y obtiene su ID.</li>
     *   <li>Actualiza la cantidad de partidas jugadas del usuario en la tabla {@code JUGADOR}.</li>
     *   <li>Carga la pantalla de juego desde el archivo FXML.</li>
     *   <li>Maneja posibles excepciones de base de datos, entrada/salida y formato incorrecto.</li>
     * </ul>
     * <p>
     * Manejo de errores:
     * <ul>
     *   <li>Si ocurre un error de SQL, se muestra un mensaje de alerta.</li>
     *   <li>Si hay un fallo al cargar la pantalla, se lanza una alerta correspondiente.</li>
     *   <li>Si el ID de partida tiene un formato incorrecto, se maneja con una excepción {@code NumberFormatException}.</li>
     * </ul>
     * 
     * @param event Evento de acción que desencadena la creación de una nueva partida.
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
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
    
    /**
     * Muestra un cuadro de alerta con un mensaje de error en la interfaz gráfica.
     * <p>
     * Este método crea una alerta de tipo {@code ERROR}, establece su título y contenido,
     * y la muestra al usuario hasta que la cierre.
     * <ul>
     *   <li>El título de la alerta se define con el parámetro {@code titulo}.</li>
     *   <li>El contenido del mensaje se define con el parámetro {@code mensaje}.</li>
     *   <li>La alerta se muestra con {@code showAndWait()}, lo que significa que la aplicación esperará 
     *       hasta que el usuario la cierre antes de continuar.</li>
     * </ul>
     * 
     * @param titulo El título de la ventana de alerta.
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    /**
     * Muestra las reglas del juego en un cuadro de diálogo emergente.
     * <p>
     * Este método presenta un mensaje detallado sobre la mecánica del juego, incluyendo:
     * <ul>
     *   <li>El objetivo del juego.</li>
     *   <li>El inventario disponible para cada jugador.</li>
     *   <li>La mecánica de turnos y acciones posibles.</li>
     *   <li>Los distintos tipos de casillas en el tablero y sus efectos.</li>
     *   <li>Los eventos aleatorios que pueden ocurrir durante la partida.</li>
     *   <li>El proceso de guardado y carga de la partida desde la base de datos.</li>
     * </ul>
     * <p>
     * La información se muestra dentro de un {@code Alert} de tipo {@code CONFIRMATION}, utilizando 
     * un {@code TextArea} dentro de un {@code GridPane} para mejorar la legibilidad.
     * 
     * @param event Evento de acción que desencadena la visualización de las reglas.
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
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
        		+ "        - Casilla interrogante: Activa un evento aleatorio.\r\n"
        		+ "        - Casilla moto de nieve: Salta a la siguiente moto.\r\n"
        		+ "        - Casilla suelo quebradizo: Si tienes más de 5 objetos vuelves a inicio. Si tienes menos, no pasa nada\r\n"
        		+ "          → Selecciona aleatoriamente un objeto a eliminar.\r\n"
        		+ "          → Si no tienes de ese objeto, no pasa nada.\r\n"
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

    /**
     * Carga la última partida guardada y restaura el estado del juego.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Obtiene la conexión a la base de datos.</li>
     *   <li>Consulta la partida más reciente en la tabla {@code PARTIDA}.</li>
     *   <li>Si hay una partida guardada, recupera la posición del jugador y sus recursos.</li>
     *   <li>Carga la pantalla de juego desde el archivo FXML.</li>
     *   <li>Pasa los datos restaurados al controlador del juego.</li>
     *   <li>Muestra la pantalla de juego con los valores cargados.</li>
     * </ul>
     * <p>
     * Manejo de errores:
     * <ul>
     *   <li>Si no hay partidas guardadas, se muestra un mensaje informativo.</li>
     *   <li>Si ocurre un error durante la carga de datos, se muestra un mensaje de alerta.</li>
     * </ul>
     * 
     * @param event Evento de acción que desencadena la carga de la partida.
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
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
    
    /**
     * Cierra la aplicación de manera inmediata.
     * <p>
     * Este método utiliza {@code Platform.exit()} para cerrar la aplicación sin realizar acciones adicionales.
     * Se recomienda asegurarse de que cualquier dato importante haya sido guardado antes de invocar este método.
     * 
     * @param event Evento de acción que desencadena la salida del juego.
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleQuitGame(ActionEvent event) {
        Platform.exit();
    }
    
}