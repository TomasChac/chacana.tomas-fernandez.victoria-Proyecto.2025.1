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
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("descripcionDetallada"));

        // Cargar datos de prueba solo si el inventario está vacío (la primera vez)
        if(inventario.getTodosLosProductos().isEmpty()){
            cargarDatosDePrueba();
        }

        actualizarTabla();

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
        inventario.agregarProducto(new Libro("L001", "El Señor de los Anillos", 25000, 10, "J.R.R. Tolkien", "978-0618640157"));
        inventario.agregarProducto(new Ropa("R001", "Polera de Algodón", 15000, 20, "M", "Azul"));
        inventario.agregarProducto(new Ropa("R002", "Pantalón Cargo", 35000, 5, "44", "Beige"));
    }

    // Lógica de los Botones

    @FXML
    protected void onBotonBuscarClick() {
        String textoBusqueda = searchField.getText();
        List<Producto> productosFiltrados = inventario.buscarProductosPorNombre(textoBusqueda);
        tablaProductos.getItems().setAll(productosFiltrados);
    }

    @FXML
    protected void onBotonAgregarClick() {
        abrirFormularioProducto(null);
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
    
    /**
     * Logica para el botón Vender.
     */
    @FXML
    protected void onBotonVenderClick() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlertaSimple("Información", "Por favor, seleccione un producto para vender.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Registrar Venta");
        dialog.setHeaderText("Vendiendo: " + productoSeleccionado.getNombre() + "\nStock disponible: " + productoSeleccionado.getStock());
        dialog.setContentText("Por favor, ingrese la cantidad a vender:");

        Optional<String> resultado = dialog.showAndWait();

        resultado.ifPresent(cantidadStr -> {
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    mostrarAlertaSimple("Error", "La cantidad debe ser un número positivo.");
                    return;
                }

                Optional<Venta> ventaExitosa = inventario.registrarVenta(productoSeleccionado.getId(), cantidad);

                if (ventaExitosa.isPresent()) {
                    actualizarTabla();
                    mostrarAlertaSimple("Venta Exitosa", "Venta registrada correctamente.\n" + ventaExitosa.get().toString());
                } else {
                    mostrarAlertaSimple("Error de Venta", "No hay stock suficiente para realizar la venta.");
                }

            } catch (NumberFormatException e) {
                mostrarAlertaSimple("Error de Formato", "Por favor, ingrese un número válido.");
            }
        });
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
    
    // Metodos de Ayuda.

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