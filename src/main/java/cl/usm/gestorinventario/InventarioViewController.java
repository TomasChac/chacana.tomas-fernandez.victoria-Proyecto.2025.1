package cl.usm.gestorinventario;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class InventarioViewController {

    // --- Conexión con el FXML ---
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn<Producto, String> columnaId;
    @FXML
    private TableColumn<Producto, String> columnaNombre;
    @FXML
    private TableColumn<Producto, Integer> columnaStock;
    @FXML
    private TableColumn<Producto, Double> columnaPrecio;
    @FXML
    private TableColumn<Producto, String> columnaTipo;

    // --- Conexión con el Backend (Modelo) ---
    // Obtenemos la instancia única de nuestro inventario.
    private Inventario inventario = Inventario.getInstancia();

    /**
     * Este método especial es llamado por JavaFX después de que el FXML ha sido cargado.
     * Es el lugar perfecto para configurar la tabla y cargar los datos iniciales.
     */
    @FXML
    public void initialize() {
        // PASO 1: Configurar las columnas.
        // Le decimos a cada columna qué atributo del objeto Producto debe mostrar.
        // El String "id", "nombre", etc., debe coincidir con el nombre del método getter
        // correspondiente en la clase Producto (ej. "id" -> getId()).
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        // Para la última columna, usaremos el método que describe el tipo de producto.
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("descripcionDetallada"));


        // PASO 2: Cargar datos de prueba en el backend.
        cargarDatosDePrueba();

        // PASO 3: Cargar los datos del backend en la tabla de la interfaz.
        actualizarTabla();
        // Se añade un "Listener" a la propiedad de texto del campo de búsqueda.
        // Este código se ejecutará CADA VEZ que el texto en la barra de búsqueda cambie.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        // 1. Llama al método del backend para obtener la lista filtrada.
        List<Producto> productosFiltrados = inventario.buscarProductosPorNombre(newValue);
        // 2. Actualiza la tabla para mostrar solo los resultados filtrados.
        tablaProductos.getItems().setAll(productosFiltrados);
    });

        System.out.println("Vista inicializada y tabla cargada con datos.");
    }

    /**
     * Método de ayuda para refrescar la tabla con los datos actuales del inventario.
     * Lo usaremos cada vez que hagamos un cambio (agregar, modificar, eliminar).
     */
    public void actualizarTabla() {
        tablaProductos.getItems().clear(); // Limpiamos la tabla
        tablaProductos.getItems().addAll(inventario.getTodosLosProductos()); // Añadimos los datos frescos
    }

    /**
     * Método de ayuda para llenar el inventario con algunos productos iniciales.
     */
    private void cargarDatosDePrueba() {
        // Solo agrega productos si el inventario está vacío, para no duplicarlos.
        if (inventario.getTodosLosProductos().isEmpty()) {
            inventario.agregarProducto(new Libro("L001", "El Señor de los Anillos", 25000, 10, "J.R.R. Tolkien", "978-0618640157"));
            inventario.agregarProducto(new Ropa("R001", "Polera de Algodón", 15000, 20, "M", "Azul"));
            inventario.agregarProducto(new Ropa("R002", "Pantalón Cargo", 35000, 5, "44", "Beige"));
        }
    }

    // --- Métodos de los Botones (por ahora solo imprimen en consola) ---

    @FXML
    protected void onBotonBuscarClick() {
        String textoBusqueda = searchField.getText();
        List<Producto> productosFiltrados = inventario.buscarProductosPorNombre(textoBusqueda);
        tablaProductos.getItems().setAll(productosFiltrados);
        System.out.println("Buscando: " + textoBusqueda);
    }

    @FXML
    protected void onBotonAgregarClick() {
        try {
            // 1. Cargar el FXML del formulario.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioProductoView.fxml"));
            Parent root = loader.load();

            // 2. Crear una nueva ventana (Stage) para el diálogo.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Agregar Nuevo Producto");
            dialogStage.initModality(Modality.WINDOW_MODAL); // Bloquea la ventana principal.
            dialogStage.initOwner(tablaProductos.getScene().getWindow()); // Asigna el dueño.

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // 3. Pasar el controlador principal al nuevo controlador del formulario.
            FormularioProductoViewController controller = loader.getController();
            controller.setMainController(this, inventario);

            // 4. Mostrar el diálogo y esperar a que el usuario lo cierre.
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            // Mostrar una alerta de error si no se puede cargar el FXML.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No se pudo abrir el formulario para agregar productos.");
            alert.showAndWait();
        }
    }

    @FXML
    protected void onBotonModificarClick() {
        System.out.println("Botón 'Modificar' presionado.");
    }

    @FXML
    protected void onBotonEliminarClick() {
    // PASO 1: Obtener el producto que el usuario ha seleccionado en la tabla.
    Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();

    // PASO 2: Verificar si el usuario realmente seleccionó algo.
    if (productoSeleccionado == null) {
        // Si no seleccionó nada, mostramos una alerta de información.
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText("Por favor, seleccione un producto de la tabla para eliminar.");
        alerta.showAndWait();
        return; // Salimos del método.
    }

    // PASO 3: Pedir confirmación antes de borrar. Es una buena práctica.
    Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
    alertaConfirmacion.setTitle("Confirmar Eliminación");
    alertaConfirmacion.setHeaderText("Está a punto de eliminar el producto: " + productoSeleccionado.getNombre());
    alertaConfirmacion.setContentText("¿Está seguro de que desea continuar?");

    Optional<ButtonType> resultado = alertaConfirmacion.showAndWait();
    
    // PASO 4: Si el usuario presiona "Aceptar" (OK)...
    if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
        // ...llamamos al método del backend que ya habíamos creado y probado.
        boolean eliminado = inventario.eliminarProducto(productoSeleccionado.getId());

        if (eliminado) {
            // PASO 5: Si se eliminó con éxito, refrescamos la tabla para que el cambio se vea.
            actualizarTabla();
            System.out.println("Producto eliminado: " + productoSeleccionado.getNombre());
        } else {
            // Esto no debería pasar si el item estaba en la tabla, pero es una buena verificación.
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("No se pudo eliminar el producto seleccionado.");
            errorAlert.showAndWait();
        }
    }
}
}