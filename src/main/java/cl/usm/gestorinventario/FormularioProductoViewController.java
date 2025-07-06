package cl.usm.gestorinventario;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    public void setMainController(InventarioViewController mainController, Inventario inventario) {
        this.mainController = mainController;
        this.inventario = inventario;
    }

    @FXML
    private void initialize() {
        tipoProductoComboBox.getItems().addAll("Libro", "Ropa");
        tipoProductoComboBox.getSelectionModel().selectFirst();

        // Listener para cambiar los labels de los campos extra dinámicamente
        tipoProductoComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switch (newVal) {
                    case "Libro":
                        labelCampoExtra1.setText("Autor:");
                        labelCampoExtra2.setText("ISBN:");
                        break;
                    case "Ropa":
                        labelCampoExtra1.setText("Talla:");
                        labelCampoExtra2.setText("Color:");
                        break;
                }
            }
        });
    }

    @FXML
    private void onBotonGuardarClick() {
        // 1. Leer los datos del formulario
        String tipo = tipoProductoComboBox.getValue();
        String id = idField.getText();
        String nombre = nombreField.getText();

        // 2. Validar la entrada
        if (id.isEmpty() || nombre.isEmpty()) {
            mostrarAlerta("Error de Validación", "Los campos ID y Nombre no pueden estar vacíos.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String extra1 = campoExtra1Field.getText();
            String extra2 = campoExtra2Field.getText();

            if (precio < 0 || stock < 0) {
                mostrarAlerta("Error de Validación", "El precio y el stock no pueden ser negativos.");
                return;
            }

            // 3. Crear el objeto Producto adecuado
            Producto nuevoProducto = null;
            if ("Libro".equals(tipo)) {
                nuevoProducto = new Libro(id, nombre, precio, stock, extra1, extra2);
            } else if ("Ropa".equals(tipo)) {
                nuevoProducto = new Ropa(id, nombre, precio, stock, extra1, extra2);
            }

            // 4. Llamar al backend para agregar el producto
            if (nuevoProducto != null && inventario.agregarProducto(nuevoProducto)) {
                // 5. Actualizar la tabla en la ventana principal
                mainController.actualizarTabla();
                // 6. Cerrar la ventana del formulario
                onBotonCancelarClick();
            } else {
                mostrarAlerta("Error de Lógica", "El ID del producto ya existe en el inventario.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Por favor, ingrese un número válido para Precio y Stock.");
        }
    }

    @FXML
    private void onBotonCancelarClick() {
        // Cierra la ventana (Stage) que contiene el botón.
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    // Método de ayuda para mostrar alertas de forma sencilla
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}