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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import Controlador.saveCon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import javafx.event.ActionEvent;

public class pantallaMenuController {
	@FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;

    @FXML private TextField userField;
    @FXML private PasswordField passField;

    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML
    private void initialize() {
        // This method is called automatically after the FXML is loaded
        // You can set initial values or add listeners here
        System.out.println("pantallaPrincipalController initialized");
    }

    @FXML
    private void handleNewGame(ActionEvent event) {
        try {
            // 1. Crear nueva partida en la base de datos
            try (Connection con = saveCon.getConexion();
                 PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO PARTIDA (NUM_PARTIDA, DATA_PARTIDA, HORA) " +
                     "VALUES (NUM_PARTIDA_AUTO.NEXTVAL, SYSDATE, CURRENT_TIMESTAMP)",
                     new String[]{"NUM_PARTIDA"})) {  // Especifica el nombre de la columna para el ID
                
                pst.executeUpdate();
                
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
                        
                        System.out.println("Nueva partida creada con ID: " + idPartida);
                    }
                }
            }

            // 2. Cargar la pantalla de juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/pantallaJuego.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    private void handleLoadGame() {
        System.out.println("Load Game clicked");
        // TODO
    }

    @FXML
    private void handleQuitGame(ActionEvent event) {
        Platform.exit();
    }
    
}