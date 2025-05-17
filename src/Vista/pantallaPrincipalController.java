package Vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import Controlador.bbdd;
import Controlador.saveCon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import javafx.event.ActionEvent;

/**
 * Esta clase define el codigo de la pantalla principal, la primera pantalla en aparecer una vez arrancas el programa, donde se inicia sesión o se registra.
 * 
 * @author DAVO
 * @version 16/05/2025
 */
public class pantallaPrincipalController {
	
	/**
	 * Controlador gestiona la interfaz principal del juego, incluyendo la conexión a la base de datos,
	 * autenticación de usuario y la navegación por el menú.
	 * <ul>
	 *   <li>Define los elementos de la interfaz vinculados a FXML.</li>
	 *   <li>Inicializa valores y configura la conexión a la base de datos.</li>
	 *   <li>Permite iniciar, cargar, guardar y salir de una partida.</li>
	 *   <li>Gestiona el inicio de sesión y validación de usuario en la base de datos.</li>
	 * </ul>
	 */
	private Vista.pantallaJuegoController pantallaJuegoController;
	
	/** Menú de opciones de juego */
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;
    

    /** Campos de usuario y contraseña */
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    

    /** Botones de interacción */
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button quitButton;
    
    /** Selector de color de jugador */
    @FXML private ComboBox<String> colorBox;
    
    /** Selector de color de jugador */
    private Connection con;
    
    /**
     * Inicializa la pantalla principal del juego después de cargar el archivo FXML.
     * <p>
     * Establece la conexión con la base de datos y configura los valores iniciales de la interfaz.
     */
    @FXML
    private void initialize() {
    	con = bbdd.conectarBaseDatos();
        // This method is called automatically after the FXML is loaded
        // You can set initial values or add listeners here
        System.out.println("pantallaPrincipalController initialized");
        colorBox.getItems().addAll("Rojo", "Verde", "Azul", "Amarillo");
    }
    
    /**
     * Cierra la aplicación de manera inmediata.
     * 
     * @param event Evento de acción que desencadena la salida del juego.
     */
    @FXML
    private void handleQuitGame(ActionEvent event) {//Añade el parámetro
        Platform.exit();  //Otra forma para que salga de la app
    }
    
    /**
     * Gestiona el inicio de sesión verificando el usuario y la contraseña en la base de datos.
     * <p>
     * Si el usuario existe y la contraseña es correcta, se carga la pantalla del menú.
     * Si la autenticación falla, se muestra una alerta con el mensaje de error.
     * 
     * @param event Evento de acción que desencadena el inicio de sesión.
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String usuario = userField.getText();
        String contraseña = passField.getText();
        
        saveCon.setUser(usuario);
        try {
            Connection con = saveCon.getConexion();
            
            // Consulta SQL para verificar usuario y contraseña
            String sql = "SELECT * FROM JUGADOR WHERE NICKNAME = ? AND CONTRASENYA = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, usuario); //nomenclatura de PreparedStatement.
            pstmt.setString(2, contraseña);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Usuario válido - abre pantalla de juego
                Parent juego = FXMLLoader.load(getClass().getResource("/Resources/pantallaMenu.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(juego));
            } else {
                // Usuario no existe o contraseña incorrecta
                mostrarAlerta("Error", "Usuario o contraseña incorrectos");
            }
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Problema al conectar con la BD");
            e.printStackTrace();
        }
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Obtiene el nombre de usuario, la contraseña y el color seleccionado.</li>
     *   <li>Valida que todos los campos estén completos y cumplan con las restricciones de longitud.</li>
     *   <li>Verifica si el nombre de usuario ya está en uso en la base de datos.</li>
     *   <li>Si el usuario no existe, lo registra en la tabla {@code JUGADOR}.</li>
     *   <li>Muestra un mensaje de éxito o error según el resultado de la inserción.</li>
     * </ul>
     * <p>
     * Manejo de errores:
     * <ul>
     *   <li>Si algún campo está vacío, se muestra una alerta indicando que se deben completar todos los campos.</li>
     *   <li>Si el nombre de usuario ya está registrado, se muestra una alerta de error.</li>
     *   <li>Si ocurre un error al ejecutar la consulta, se captura y se muestra un mensaje con el detalle del problema.</li>
     * </ul>
     * 
     * @FXML Este método está vinculado a la interfaz gráfica mediante FXML.
     */
    @FXML
    private void handleRegister() {
        String username = userField.getText().trim();
        String password = passField.getText().trim();
        String color = colorBox.getValue();

        // Validaciones básicas
        if (username.isEmpty() || password.isEmpty() || color == null) {
            mostrarAlerta("Error", "Por favor complete todos los campos");
            return;
        }

        if (username.length() > 15) {
            mostrarAlerta("Error", "El nombre de usuario no puede exceder 15 caracteres");
            return;
        }

        if (password.length() > 10) {
            mostrarAlerta("Error", "La contraseña no puede exceder 10 caracteres");
            return;
        }

        try {
            Connection con = saveCon.getConexion();
            
            // Verificar si el usuario ya existe
            if (existeUsuario(con, username)) {
                mostrarAlerta("Error", "El nombre de usuario ya está en uso");
                return;
            }

            // Insertar nuevo usuario
            String sql = "INSERT INTO JUGADOR (ID_JUGADOR, NICKNAME, CONTRASENYA, NUM_PARTIDAS_JUGADAS, COLOR) " +
                         "VALUES (id_jugador_auto.NEXTVAL, ?, ?, 0, ?)";
            
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, color);
                
                int affectedRows = pstmt.executeUpdate();
                
                if (affectedRows > 0) {
                    mostrarAlerta("Éxito", "Usuario registrado exitosamente");
                } else {
                    mostrarAlerta("Error", "No se pudo registrar el usuario");
                }
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica si un nombre de usuario ya está registrado en la base de datos.
     * <p>
     * Este método consulta la tabla {@code JUGADOR} para comprobar si el nombre de usuario ya existe.
     * <ul>
     *   <li>Utiliza una consulta SQL con {@code COUNT(*)} para contar las coincidencias.</li>
     *   <li>Si el resultado es mayor que 0, significa que el usuario ya está registrado.</li>
     *   <li>Utiliza {@code PreparedStatement} para evitar riesgos de inyección SQL.</li>
     * </ul>
     * 
     * @param con Conexión activa a la base de datos.
     * @param username Nombre de usuario a verificar.
     * @return {@code true} si el usuario existe, {@code false} en caso contrario.
     * @throws SQLException Si ocurre un error en la ejecución de la consulta SQL.
     */
    private boolean existeUsuario(Connection con, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM JUGADOR WHERE NICKNAME = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery(); //es un método de la interfaz PreparedStatement en Java, que se utiliza para ejecutar una consulta SQL que devuelve un conjunto de resultados.
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    /**
     * Muestra un cuadro de alerta con un mensaje informativo en la interfaz gráfica.
     * <p>
     * Este método crea una alerta de tipo {@code INFORMATION}, establece su título y contenido,
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
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
}