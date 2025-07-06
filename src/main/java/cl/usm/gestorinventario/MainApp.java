package cl.usm.gestorinventario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class MainApp extends Application {

    /**
     * @param primaryStage La ventana principal de la aplicación, proporcionada por JavaFX.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Obtenemos la ruta del archivo FXML desde la carpeta de recursos.
        URL fxmlLocation = getClass().getResource("InventarioView.fxml");

        // Verificacion.
        if (fxmlLocation == null) {
            System.err.println("Error crítico: No se pudo encontrar el archivo FXML 'InventarioView.fxml'. Asegúrate de que esté en la carpeta de recursos correcta.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 900, 600);

        primaryStage.setTitle("Sistema Gestor de Inventario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Llama a launch(), que inicia el ciclo de vida de la aplicación JavaFX.
     * @param args Argumentos de la línea de comandos (no los usaremos).
     */
    public static void main(String[] args) {
        launch(args);
    }
}