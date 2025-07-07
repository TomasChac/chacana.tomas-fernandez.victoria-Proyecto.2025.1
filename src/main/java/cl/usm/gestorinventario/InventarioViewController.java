package cl.usm.gestorinventario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controlador principal de la aplicación. Maneja la lógica de la vista
 * principal (InventarioView.fxml), incluyendo la tabla de productos y
 * todas las acciones de los botones.
 *
 * @author Tomás Ch., Victoria F., Jossefa Z.
 * @version 1.2
 */
public class InventarioViewController {

    // --- Componentes de la UI inyectados desde el FXML ---
    @FXML private TextField searchField;
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> columnaId;
    @FXML private TableColumn<Producto, String> columnaNombre;
    @FXML private TableColumn<Producto, Integer> columnaStock;
    @FXML private TableColumn<Producto, Double> columnaPrecio;
    @FXML private TableColumn<Producto, String> columnaTipo;

    // --- Conexión con el Modelo ---
    private final Inventario inventario = Inventario.getInstancia();

    /**
     * Se ejecuta automáticamente después de que el archivo FXML ha sido cargado.
     * Es el lugar ideal para configurar el estado inicial de la vista.
     */
    @FXML
    public void initialize() {
        // 1. Configurar las columnas para que sepan qué datos mostrar
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("descripcionDetallada"));

        // 2. Cargar datos iniciales si el inventario está vacío (primera ejecución)
        if (inventario.getTodosLosProductos().isEmpty()) {
            cargarDatosDePrueba();
        }

        // 3. Poblar la tabla con los datos del inventario
        actualizarTabla();

        // 4. Añadir listener para la búsqueda dinámica
        searchField.textProperty().addListener((obs, oldVal, newVal) -> onBotonBuscarClick());
        System.out.println("Vista inicializada y lista para usar.");
    }

    /**
     * Refresca la TableView para que muestre los datos más recientes del inventario.
     * Este método es público para poder ser llamado desde otros controladores.
     */
    public void actualizarTabla() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        tablaProductos.getItems().clear();
        tablaProductos.getItems().addAll(inventario.getTodosLosProductos());
        if (seleccionado != null) {
            tablaProductos.getSelectionModel().select(seleccionado);
        }
    }

    /**
     * Carga un conjunto de productos de prueba en el inventario.
     * Se utiliza solo si no existe un archivo de guardado previo.
     */
    private void cargarDatosDePrueba() {
        inventario.agregarProducto(new Libro("L001", "El Señor de los Anillos", 25000, 10, "J.R.R. Tolkien", "978-0618640157"));
        inventario.agregarProducto(new Ropa("R001", "Polera de Algodón", 15000, 20, "M", "Azul"));
        inventario.agregarProducto(new Ropa("R002", "Pantalón Cargo", 35000, 5, "44", "Beige"));
    }

    /**
     * Maneja el evento de clic del botón "Buscar".
     * Filtra la tabla según el texto ingresado en el campo de búsqueda.
     */
    @FXML
    protected void onBotonBuscarClick() {
        tablaProductos.getItems().setAll(inventario.buscarProductosPorNombre(searchField.getText()));
    }

    /**
     * Maneja el evento de clic del botón "Reporte Stock Bajo".
     * Filtra la tabla para mostrar solo productos con 10 o menos unidades.
     */
    @FXML
    protected void onBotonReporteClick() {
        tablaProductos.getItems().setAll(inventario.getProductosConStockBajo(10));
    }

    /**
     * Restaura la tabla para mostrar todos los productos del inventario.
     */
    @FXML
    protected void onBotonMostrarTodosClick() {
        searchField.clear();
        actualizarTabla();
    }

    /**
     * Maneja el evento de clic del botón "Agregar...".
     * Lanza el formulario para crear un nuevo producto.
     */
    @FXML
    protected void onBotonAgregarClick() {
        abrirFormularioProducto(null);
    }

    /**
     * Maneja el evento de clic del botón "Modificar...".
     * Lanza el formulario para editar el producto seleccionado.
     */
    @FXML
    protected void onBotonModificarClick() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlertaSimple("Información", "Por favor, seleccione un producto para modificar.");
            return;
        }
        abrirFormularioProducto(productoSeleccionado);
    }
    
    /**
     * Maneja el evento de clic del botón "Vender...".
     * Pide una cantidad y registra la venta, actualizando el stock.
     */
    @FXML
    protected void onBotonVenderClick() {
        Producto p = tablaProductos.getSelectionModel().getSelectedItem();
        if (p == null) {
            mostrarAlertaSimple("Información", "Por favor, seleccione un producto para vender.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Registrar Venta");
        dialog.setHeaderText("Vendiendo: " + p.getNombre() + "\nStock disponible: " + p.getStock());
        dialog.setContentText("Ingrese la cantidad a vender:");
        Optional<String> resultado = dialog.showAndWait();

        resultado.ifPresent(cantidadStr -> {
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    mostrarAlertaSimple("Error", "La cantidad debe ser un número positivo.");
                    return;
                }
                Optional<Venta> v = inventario.registrarVenta(p.getId(), cantidad);
                if (v.isPresent()) {
                    actualizarTabla();
                    mostrarAlertaSimple("Venta Exitosa", "Venta registrada correctamente.");
                } else {
                    mostrarAlertaSimple("Error de Venta", "No hay stock suficiente.");
                }
            } catch (NumberFormatException e) {
                mostrarAlertaSimple("Error de Formato", "Por favor, ingrese un número válido.");
            }
        });
    }

    /**
     * Maneja el evento de clic del botón "Eliminar".
     * Pide confirmación y elimina el producto seleccionado.
     */
    @FXML
    protected void onBotonEliminarClick() {
        Producto p = tablaProductos.getSelectionModel().getSelectedItem();
        if (p == null) {
            mostrarAlertaSimple("Información", "Por favor, seleccione un producto para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Eliminación");
        confirm.setHeaderText("Eliminar: " + p.getNombre());
        confirm.setContentText("¿Está seguro?");
        Optional<ButtonType> resultado = confirm.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (inventario.eliminarProducto(p.getId())) {
                actualizarTabla();
            }
        }
    }
    
    /**
     * Abre la ventana del formulario para agregar o modificar un producto.
     * @param productoAEditar El producto a editar, o null si se va a agregar uno nuevo.
     */
    private void abrirFormularioProducto(Producto productoAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioProductoView.fxml"));
            Parent root = loader.load();
            FormularioProductoViewController controller = loader.getController();
            controller.setMainController(this, inventario);
            
            String titulo = (productoAEditar != null) ? "Modificar Producto" : "Agregar Nuevo Producto";
            if (productoAEditar != null) {
                controller.setProductoParaEditar(productoAEditar);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(titulo);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tablaProductos.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaSimple("Error", "No se pudo abrir el formulario de producto.");
        }
    }

    /**
     * Método de ayuda para mostrar una alerta simple al usuario.
     * @param titulo El título de la ventana de alerta.
     * @param contenido El mensaje principal de la alerta.
     */
    private void mostrarAlertaSimple(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}