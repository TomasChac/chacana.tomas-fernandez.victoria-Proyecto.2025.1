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
    private boolean modoEdicion = false;
    private Producto productoAEditar;

    public void setMainController(InventarioViewController mainController, Inventario inventario) {
        this.mainController = mainController;
        this.inventario = inventario;
    }

    /**
     * Recibe un producto y llena los campos del formulario con sus datos.
     * @param producto El producto a editar.
     */
    public void setProductoParaEditar(Producto producto) {
        this.modoEdicion = true;
        this.productoAEditar = producto;

        // Llenar los campos del formulario
        idField.setText(producto.getId());
        idField.setEditable(false); // El ID no se puede cambiar
        nombreField.setText(producto.getNombre());
        precioField.setText(String.valueOf(producto.getPrecio()));
        stockField.setText(String.valueOf(producto.getStock()));

        // Llenar campos específicos según el tipo de producto
        if (producto instanceof Libro) {
            tipoProductoComboBox.setValue("Libro");
            campoExtra1Field.setText(((Libro) producto).getAutor());
            campoExtra2Field.setText(((Libro) producto).getIsbn());
        } else if (producto instanceof Ropa) {
            tipoProductoComboBox.setValue("Ropa");
            campoExtra1Field.setText(((Ropa) producto).getTalla());
            campoExtra2Field.setText(((Ropa) producto).getColor());
        }
        tipoProductoComboBox.setDisable(true); // El tipo no se puede cambiar
    }

    @FXML
    private void initialize() {
        tipoProductoComboBox.getItems().addAll("Libro", "Ropa");
        tipoProductoComboBox.getSelectionModel().selectFirst();
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
        String nombre = nombreField.getText();
        if (nombre.isEmpty()) {
            mostrarAlerta("Error de Validación", "El campo Nombre no puede estar vacío.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioField.getText());
            int stock = Integer.parseInt(stockField.getText());
            // validaciones de precio y stock

            // Creamos un objeto temporal con los nuevos datos
            Producto productoActualizado;
            if ("Libro".equals(tipoProductoComboBox.getValue())) {
                productoActualizado = new Libro(idField.getText(), nombre, precio, stock, campoExtra1Field.getText(), campoExtra2Field.getText());
            } else {
                productoActualizado = new Ropa(idField.getText(), nombre, precio, stock, campoExtra1Field.getText(), campoExtra2Field.getText());
            }
            
            if (modoEdicion) {
                // Si estamos en modo edición, llamamos al método para modificar
                inventario.modificarProducto(productoAEditar.getId(), productoActualizado);
            } else {
                // Si no, llamamos al método para agregar
                if (!inventario.agregarProducto(productoActualizado)) {
                     mostrarAlerta("Error de Lógica", "El ID del producto ya existe en el inventario.");
                     return;
                }
            }

            mainController.actualizarTabla();
            onBotonCancelarClick();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Por favor, ingrese un número válido para Precio y Stock.");
        }
    }

    @FXML
    private void onBotonCancelarClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}