package Controlador;
import java.sql.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Este código es la clase principal (main) de una aplicación JavaFX que funciona como el punto de entrada.
 * @author DAVO
 * @version 16/05/2025
 */
public class main extends Application {

	@Override
	/**
	 * Este método representa el punto de entrada al Juego del pinüino en JavaFX.
	 * Además, también establece uan conexión con la base de datos del juego.
	 * 
	 * @param primaryStage Representa la ventana primaria del juego.
	 * @throws Exception En caso de que no consiga hacer la conexión con los archivos FXML ni con la BD.
	 */
	public void start(Stage primaryStage) throws Exception {
		//Establece y guarda la conexión a la BD
		Connection con = bbdd.conectarBaseDatos();
		saveCon.setConexion(con);
		
		//Carga la interfaz principal de los archivos FXML
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/pantallaPrincipal.fxml"));
	    Parent root = loader.load();
 
	    //Configura y muestra la pantalla primaria
	    Scene scene = new Scene(root);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("El Juego del Pingüino");
	    primaryStage.show();
	}
	
	/**
	 * Es el main del proyecto
	 * @param args Inicia el motor de JavaFX y llama al método start()
	 */
    public static void main(String[] args) {
        launch(args);
    }
}
