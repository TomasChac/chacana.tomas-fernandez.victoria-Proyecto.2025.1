package cl.usm.gestorinventario;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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

        System.out.println("Vista inicializada y tabla cargada con datos.");
    }

    /**
     * Método de ayuda para refrescar la tabla con los datos actuales del inventario.
     * Lo usaremos cada vez que hagamos un cambio (agregar, modificar, eliminar).
     */
    private void actualizarTabla() {
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
        System.out.println("Botón 'Buscar' presionado.");
    }

    @FXML
    protected void onBotonAgregarClick() {
        System.out.println("Botón 'Agregar' presionado.");
    }

    @FXML
    protected void onBotonModificarClick() {
        System.out.println("Botón 'Modificar' presionado.");
    }

    @FXML
    protected void onBotonEliminarClick() {
        System.out.println("Botón 'Eliminar' presionado.");
    }
}