package cl.usm.gestorinventario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Clase principal que inicia la aplicación JavaFX.
 * Su única responsabilidad es cargar la vista principal desde el archivo FXML
 * y mostrarla en la ventana.
 */
public class MainApp extends Application {

    /**
     * El método principal de cualquier aplicación JavaFX. Se ejecuta al inicio.
     * @param primaryStage La ventana principal de la aplicación, proporcionada por JavaFX.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Obtenemos la ruta del archivo FXML desde la carpeta de recursos.
        // Usar getClass().getResource() es la forma correcta para que funcione tanto
        // en el IDE como en un archivo .jar ejecutable.
        URL fxmlLocation = getClass().getResource("InventarioView.fxml");

        // Una verificación de seguridad para asegurar que el archivo FXML fue encontrado.
        if (fxmlLocation == null) {
            System.err.println("Error crítico: No se pudo encontrar el archivo FXML 'InventarioView.fxml'. Asegúrate de que esté en la carpeta de recursos correcta.");
            return;
        }

        // FXMLLoader es el objeto que lee nuestro archivo FXML y construye la interfaz.
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Parent root = fxmlLoader.load();

        // Creamos la "escena" con el contenido del FXML y le damos un tamaño inicial.
        Scene scene = new Scene(root, 900, 600);

        // Configuramos la ventana principal (el "escenario").
        primaryStage.setTitle("Sistema Gestor de Inventario");
        primaryStage.setScene(scene);
        primaryStage.show(); // ¡Mostramos la ventana!
    }

    /**
     * El punto de entrada estándar para una aplicación Java.
     * Llama a launch(), que inicia el ciclo de vida de la aplicación JavaFX.
     * @param args Argumentos de la línea de comandos (no los usaremos).
     */
    public static void main(String[] args) {
        launch(args);
    }
}