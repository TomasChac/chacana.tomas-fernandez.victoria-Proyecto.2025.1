package cl.usm.gestorinventario;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormularioProductoViewController {

    @FXML private ComboBox<String> tipoProductoComboBox;
    @FXML private TextField idField;
    @FXML private TextField nombreField;
    @FXML private TextField precioField;
    @FXML private TextField stockField;
    @FXML private Label labelCampoExtra1;
    @FXML private TextField campoExtra1Field;
    @FXML private Label labelCampoExtra2;
    @FXML private TextField campoExtra2Field;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Inventario inventario;
    private InventarioViewController mainController;

    // Este método será llamado por el controlador principal para darnos acceso
    // al inventario y poder refrescar la tabla.
    public void setMainController(InventarioViewController mainController, Inventario inventario) {
        this.mainController = mainController;
        this.inventario = inventario;
    }
    
    @FXML
    private void initialize() {
        // Llenamos el ComboBox con los tipos de productos que manejamos.
        tipoProductoComboBox.getItems().addAll("Libro", "Ropa");
        // Por defecto, seleccionamos el primero.
        tipoProductoComboBox.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void onBotonGuardarClick() {
        // La lógica para guardar el producto irá aquí.
        System.out.println("Guardar presionado. ¡Aún no implementado!");
    }
    
    @FXML
    private void onBotonCancelarClick() {
        // Cierra la ventana del formulario.
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}