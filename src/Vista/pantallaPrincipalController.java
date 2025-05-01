package Vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Controlador.saveCon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import javafx.event.ActionEvent;

public class pantallaPrincipalController {

	
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;

    @FXML private TextField userField;
    @FXML private PasswordField passField;

    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button quitButton;

    @FXML private ComboBox<String> colorBox;
    
    @FXML
    private void initialize() {
        // This method is called automatically after the FXML is loaded
        // You can set initial values or add listeners here
        System.out.println("pantallaPrincipalController initialized");
        colorBox.getItems().addAll("Rojo", "Verde", "Azul", "Amarillo");
    }

    @FXML
    private void handleNewGame() {
        System.out.println("New Game clicked");
        // TODO
    }

    @FXML
    private void handleSaveGame() {
        System.out.println("Save Game clicked");
        // TODO
    }

    @FXML
    private void handleLoadGame() {
        System.out.println("Load Game clicked");
        // TODO
    }

    @FXML
    private void handleQuitGame(ActionEvent event) {//Añade el parámetro
        Platform.exit();  //Otra forma para que salga de la app
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = userField.getText();
        String password = passField.getText();

        System.out.println("Login pressed: " + username + " / " + password);

        // Basic check (just for demo, replace with real login logic)
        if (!username.isEmpty() && !password.isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/pantallaJuego.fxml"));
                Parent pantallaJuegoRoot = loader.load();

                Scene pantallaJuegoScene = new Scene(pantallaJuegoRoot);

                // Get the current stage using the event
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(pantallaJuegoScene);
                stage.setTitle("Pantalla de Juego");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please. Enter user and password.");
        }
    }


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

    private boolean existeUsuario(Connection con, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM JUGADOR WHERE NICKNAME = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
}