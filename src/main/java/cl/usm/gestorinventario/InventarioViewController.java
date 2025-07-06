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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class InventarioViewController {

    @FXML private TextField searchField;
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> columnaId;
    @FXML private TableColumn<Producto, String> columnaNombre;
    @FXML private TableColumn<Producto, Integer> columnaStock;
    @FXML private TableColumn<Producto, Double> columnaPrecio;
    @FXML private TableColumn<Producto, String> columnaTipo;

    private Inventario inventario = Inventario.getInstancia();

    @FXML
    public void initialize() {
        // 1. Configurar las columnas de la tabla
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("descripcionDetallada"));

        // 2. Cargar datos de prueba
        cargarDatosDePrueba();

        // 3. Cargar datos iniciales en la tabla
        actualizarTabla();

        // 4. Añadir listener para busqueda dinamica
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Producto> productosFiltrados = inventario.buscarProductosPorNombre(newValue);
            tablaProductos.getItems().setAll(productosFiltrados);
        });

        System.out.println("Vista inicializada y lista para usar.");
    }

    public void actualizarTabla() {
        tablaProductos.getItems().clear();
        tablaProductos.getItems().addAll(inventario.getTodosLosProductos());
    }

    private void cargarDatosDePrueba() {
        if (inventario.getTodosLosProductos().isEmpty()) {
            inventario.agregarProducto(new Libro("L001", "El Señor de los Anillos", 25000, 10, "J.R.R. Tolkien", "978-0618640157"));
            inventario.agregarProducto(new Ropa("R001", "Polera de Algodón", 15000, 20, "M", "Azul"));
            inventario.agregarProducto(new Ropa("R002", "Pantalón Cargo", 35000, 5, "44", "Beige"));
        }
    }

    // Logica de los Botones

    @FXML
    protected void onBotonBuscarClick() {
        String textoBusqueda = searchField.getText();
        List<Producto> productosFiltrados = inventario.buscarProductosPorNombre(textoBusqueda);
        tablaProductos.getItems().setAll(productosFiltrados);
    }

    @FXML
    protected void onBotonAgregarClick() {
        abrirFormularioProducto(null); // Pasamos null porque no estamos editando
    }

    @FXML
    protected void onBotonModificarClick() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlertaSimple("Información", "Por favor, seleccione un producto de la tabla para modificar.");
            return;
        }
        abrirFormularioProducto(productoSeleccionado);
    }

    @FXML
    protected void onBotonEliminarClick() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlertaSimple("Información", "Por favor, seleccione un producto de la tabla para eliminar.");
            return;
        }

        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle("Confirmar Eliminación");
        alertaConfirmacion.setHeaderText("Eliminar: " + productoSeleccionado.getNombre());
        alertaConfirmacion.setContentText("¿Está seguro de que desea continuar?");
        Optional<ButtonType> resultado = alertaConfirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (inventario.eliminarProducto(productoSeleccionado.getId())) {
                actualizarTabla();
            }
        }
    }
    
    // Metodos de Ayuda

    private void abrirFormularioProducto(Producto productoAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormularioProductoView.fxml"));
            Parent root = loader.load();
            
            FormularioProductoViewController controller = loader.getController();
            controller.setMainController(this, inventario);
            
            String titulo;
            if(productoAEditar != null) {
                titulo = "Modificar Producto";
                controller.setProductoParaEditar(productoAEditar);
            } else {
                titulo = "Agregar Nuevo Producto";
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(titulo);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tablaProductos.getScene().getWindow());
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaSimple("Error", "No se pudo abrir el formulario de producto.");
        }
    }

    private void mostrarAlertaSimple(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}