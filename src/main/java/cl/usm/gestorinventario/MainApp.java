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
 * Su responsabilidad es cargar la vista principal desde el archivo FXML
 * y guardar los datos del inventario al cerrar la aplicación.
 *
 * @author Tomás Ch., Victoria F., Jossefa Z.
 * @version 1.1
 */
public class MainApp extends Application {

    /**
     * El método principal del ciclo de vida de una aplicación JavaFX.
     * Se ejecuta al inicio para configurar y mostrar la ventana principal.
     * @param primaryStage La ventana principal (Stage) proporcionada por JavaFX.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlLocation = getClass().getResource("InventarioView.fxml");
        if (fxmlLocation == null) {
            System.err.println("Error crítico: No se pudo encontrar el archivo FXML 'InventarioView.fxml'.");
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
     * Este método se ejecuta automáticamente cuando la aplicación está a punto de cerrarse.
     * Se utiliza para invocar el guardado de datos del inventario.
     */
    @Override
    public void stop() {
        System.out.println("Guardando el estado del inventario antes de cerrar...");
        Inventario.getInstancia().guardarInventarioCSV();
        System.out.println("Inventario guardado.");
    }

    /**
     * El punto de entrada estándar para una aplicación Java.
     * Llama a launch(), que inicia el ciclo de vida de la aplicación JavaFX.
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        launch(args);
    }
}