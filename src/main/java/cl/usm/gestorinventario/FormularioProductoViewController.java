package cl.usm.gestorinventario;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controlador para la ventana del formulario de productos (FormularioProductoView.fxml).
 * Maneja la lógica para agregar un nuevo producto o modificar uno existente.
 *
 * @author Tomás Ch., Victoria F., Jossefa Z.
 * @version 1.2
 */
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

    /**
     * Inyecta el controlador principal y la instancia del inventario.
     * Permite la comunicación entre la ventana de formulario y la principal.
     * @param mainController El controlador de la vista principal.
     * @param inventario La instancia del modelo de datos.
     */
    public void setMainController(InventarioViewController mainController, Inventario inventario) {
        this.mainController = mainController;
        this.inventario = inventario;
    }

    /**
     * Configura el formulario para editar un producto existente.
     * Rellena los campos con los datos del producto y entra en "Modo Edición".
     * @param producto El producto a editar.
     */
    public void setProductoParaEditar(Producto producto) {
        this.modoEdicion = true;
        this.productoAEditar = producto;

        idField.setText(producto.getId());
        idField.setEditable(false);
        nombreField.setText(producto.getNombre());
        precioField.setText(String.valueOf(producto.getPrecio()));
        stockField.setText(String.valueOf(producto.getStock()));

        if (producto instanceof Libro) {
            tipoProductoComboBox.setValue("Libro");
            campoExtra1Field.setText(((Libro) producto).getAutor());
            campoExtra2Field.setText(((Libro) producto).getIsbn());
        } else if (producto instanceof Ropa) {
            tipoProductoComboBox.setValue("Ropa");
            campoExtra1Field.setText(((Ropa) producto).getTalla());
            campoExtra2Field.setText(((Ropa) producto).getColor());
        } else if (producto instanceof Alimento) {
            tipoProductoComboBox.setValue("Alimento");
            campoExtra1Field.setText(((Alimento) producto).getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            labelCampoExtra2.setVisible(false);
            campoExtra2Field.setVisible(false);
        }
        tipoProductoComboBox.setDisable(true);
    }

    /**
     * Se ejecuta al cargar el FXML. Configura el ComboBox y su listener.
     */
    @FXML
    private void initialize() {
        tipoProductoComboBox.getItems().addAll("Libro", "Ropa", "Alimento");
        tipoProductoComboBox.getSelectionModel().selectFirst();
        tipoProductoComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            labelCampoExtra1.setVisible(true);
            campoExtra1Field.setVisible(true);
            labelCampoExtra2.setVisible(true);
            campoExtra2Field.setVisible(true);
            if (newVal != null) {
                switch (newVal) {
                    case "Libro": labelCampoExtra1.setText("Autor:"); labelCampoExtra2.setText("ISBN:"); break;
                    case "Ropa": labelCampoExtra1.setText("Talla:"); labelCampoExtra2.setText("Color:"); break;
                    case "Alimento":
                        labelCampoExtra1.setText("F. Vencimiento (dd-mm-yyyy):");
                        labelCampoExtra2.setVisible(false);
                        campoExtra2Field.setVisible(false);
                        break;
                }
            }
        });
    }

    /**
     * Maneja el clic del botón "Guardar". Valida los datos y agrega o modifica el producto.
     */
    @FXML
    private void onBotonGuardarClick() {
        try {
            if (idField.getText().isEmpty() || nombreField.getText().isEmpty()) {
                mostrarAlerta("Error de Validación", "Los campos ID y Nombre no pueden estar vacíos.");
                return;
            }
            double precio = Double.parseDouble(precioField.getText());
            int stock = Integer.parseInt(stockField.getText());
            if (precio < 0 || stock < 0) {
                mostrarAlerta("Error de Validación", "El precio y el stock no pueden ser negativos.");
                return;
            }

            Producto productoActualizado = crearProductoDesdeFormulario();
            if (productoActualizado == null) return; // Error de parsing de fecha manejado internamente

            boolean exito = modoEdicion
                ? inventario.modificarProducto(productoAEditar.getId(), productoActualizado)
                : inventario.agregarProducto(productoActualizado);
            
            if (exito) {
                mainController.actualizarTabla();
                onBotonCancelarClick();
            } else {
                mostrarAlerta("Error de Lógica", "El ID del producto ya existe en el inventario.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Por favor, ingrese un número válido para Precio y Stock.");
        }
    }

    /**
     * Crea un objeto Producto a partir de los datos del formulario.
     * @return El objeto Producto creado, o null si hay un error.
     */
    private Producto crearProductoDesdeFormulario() {
        try {
            String tipo = tipoProductoComboBox.getValue();
            String id = idField.getText();
            String nombre = nombreField.getText();
            double precio = Double.parseDouble(precioField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String extra1 = campoExtra1Field.getText();
            String extra2 = campoExtra2Field.getText();

            switch (tipo) {
                case "Libro": return new Libro(id, nombre, precio, stock, extra1, extra2);
                case "Ropa": return new Ropa(id, nombre, precio, stock, extra1, extra2);
                case "Alimento":
                    LocalDate fecha = LocalDate.parse(extra1, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    return new Alimento(id, nombre, precio, stock, fecha);
                default: return null;
            }
        } catch (DateTimeParseException e) {
            mostrarAlerta("Error de Formato", "Formato de fecha incorrecto. Use dd-mm-yyyy.");
            return null;
        }
    }

    /**
     * Maneja el clic del botón "Cancelar". Cierra la ventana del formulario.
     */
    @FXML
    private void onBotonCancelarClick() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
    
    /**
     * Muestra una alerta de error.
     * @param titulo El título de la alerta.
     * @param contenido El mensaje de la alerta.
     */
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}