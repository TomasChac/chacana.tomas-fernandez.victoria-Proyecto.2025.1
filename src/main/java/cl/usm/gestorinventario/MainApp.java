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
 * y guardar los datos al cerrar.
 */
public class MainApp extends Application {

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
     * Este método es llamado automáticamente por JavaFX justo antes
     * de que la aplicación se cierre. Es el lugar perfecto para guardar datos.
     */
    @Override
    public void stop() {
        System.out.println("Guardando el estado del inventario antes de cerrar...");
        Inventario.getInstancia().guardarInventarioCSV();
        System.out.println("Inventario guardado.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}