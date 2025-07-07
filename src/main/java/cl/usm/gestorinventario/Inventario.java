package cl.usm.gestorinventario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Gestiona todas las operaciones del inventario (CRUD, ventas, reportes).
 * Implementa el patrón de diseño Singleton y la persistencia de datos vía CSV.
 *
 * @author Tomás Ch., Victoria F., Jossefa Z.
 * @version 1.2
 */
public class Inventario {

    private static Inventario instancia;
    private List<Producto> listaProductos;

    /**
     * El constructor es privado para implementar el patrón Singleton.
     * Al crearse la instancia, intenta cargar los datos desde el archivo "inventario.csv".
     */
    private Inventario() {
        this.listaProductos = new ArrayList<>();
        cargarInventarioCSV();
    }

    /**
     * Devuelve la única instancia de la clase Inventario (Singleton).
     * @return La única instancia de Inventario.
     */
    public static Inventario getInstancia() {
        if (instancia == null) {
            instancia = new Inventario();
        }
        return instancia;
    }

    /**
     * Guarda el estado actual del inventario en un archivo CSV llamado "inventario.csv".
     * Sobrescribe el archivo si ya existe y usa Locale.US para formato de número universal.
     */
    public void guardarInventarioCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("inventario.csv"))) {
            writer.println("id,tipo,nombre,precio,stock,extra1,extra2");
            for (Producto p : this.listaProductos) {
                String tipo = "", extra1 = "", extra2 = "";
                if (p instanceof Libro) {
                    tipo = "Libro";
                    extra1 = ((Libro) p).getAutor();
                    extra2 = ((Libro) p).getIsbn();
                } else if (p instanceof Ropa) {
                    tipo = "Ropa";
                    extra1 = ((Ropa) p).getTalla();
                    extra2 = ((Ropa) p).getColor();
                } else if (p instanceof Alimento) {
                    tipo = "Alimento";
                    extra1 = ((Alimento) p).getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    extra2 = "";
                }
                String lineaCSV = String.format(Locale.US, "\"%s\",%s,\"%s\",%.2f,%d,\"%s\",\"%s\"",
                        p.getId(), tipo, p.getNombre(), p.getPrecio(), p.getStock(), extra1, extra2);
                writer.println(lineaCSV);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el inventario en CSV: " + e.getMessage());
        }
    }

    /**
     * Carga los datos del inventario desde "inventario.csv" al iniciar.
     */
    private void cargarInventarioCSV() {
        File archivo = new File("inventario.csv");
        if (!archivo.exists()) return;
        this.listaProductos.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea = reader.readLine();
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (campos.length < 7) continue;
                for (int i = 0; i < campos.length; i++) campos[i] = campos[i].replaceAll("^\"|\"$", "");
                Producto p = null;
                if ("Libro".equals(campos[1])) {
                    p = new Libro(campos[0], campos[2], Double.parseDouble(campos[3]), Integer.parseInt(campos[4]), campos[5], campos[6]);
                } else if ("Ropa".equals(campos[1])) {
                    p = new Ropa(campos[0], campos[2], Double.parseDouble(campos[3]), Integer.parseInt(campos[4]), campos[5], campos[6]);
                } else if ("Alimento".equals(campos[1])) {
                    LocalDate fecha = LocalDate.parse(campos[5], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    p = new Alimento(campos[0], campos[2], Double.parseDouble(campos[3]), Integer.parseInt(campos[4]), fecha);
                }
                if (p != null) this.listaProductos.add(p);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el inventario desde CSV: " + e.getMessage());
        }
    }

    /**
     * Agrega un nuevo producto al inventario.
     * @param nuevoProducto El producto a agregar.
     * @return true si el producto fue agregado, false si el ID ya existía.
     */
    public boolean agregarProducto(Producto nuevoProducto) {
        if (buscarProductoPorId(nuevoProducto.getId()).isPresent()) return false;
        return this.listaProductos.add(nuevoProducto);
    }

    /**
     * Busca un producto en el inventario por su ID.
     * @param id El ID del producto a buscar.
     * @return Un Optional que contiene el Producto si se encuentra, o un Optional vacío si no.
     */
    public Optional<Producto> buscarProductoPorId(String id) {
        return listaProductos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    /**
     * Busca productos cuyo nombre contenga el texto proporcionado.
     * @param texto El texto a buscar en el nombre del producto.
     * @return Una lista de productos que coinciden con la búsqueda.
     */
    public List<Producto> buscarProductosPorNombre(String texto) {
        return listaProductos.stream().filter(p -> p.getNombre().toLowerCase().contains(texto.toLowerCase())).collect(Collectors.toList());
    }

    /**
     * Elimina un producto del inventario usando su ID.
     * @param id El ID del producto a eliminar.
     * @return true si el producto fue encontrado y eliminado, false en caso contrario.
     */
    public boolean eliminarProducto(String id) {
        return this.listaProductos.removeIf(p -> p.getId().equals(id));
    }

    /**
     * Modifica un producto existente.
     * @param id El ID del producto a modificar.
     * @param datosNuevos Un objeto Producto con los nuevos datos.
     * @return true si el producto fue encontrado y modificado, false en caso contrario.
     */
    public boolean modificarProducto(String id, Producto datosNuevos) {
        Optional<Producto> productoOpt = buscarProductoPorId(id);
        if (productoOpt.isPresent()) {
            Producto p = productoOpt.get();
            p.setNombre(datosNuevos.getNombre());
            p.setPrecio(datosNuevos.getPrecio());
            p.setStock(datosNuevos.getStock());
            if (p instanceof Libro && datosNuevos instanceof Libro) {
                ((Libro) p).setAutor(((Libro) datosNuevos).getAutor());
                ((Libro) p).setIsbn(((Libro) datosNuevos).getIsbn());
            } else if (p instanceof Ropa && datosNuevos instanceof Ropa) {
                ((Ropa) p).setTalla(((Ropa) datosNuevos).getTalla());
                ((Ropa) p).setColor(((Ropa) datosNuevos).getColor());
            } else if (p instanceof Alimento && datosNuevos instanceof Alimento) {
                ((Alimento) p).setFechaVencimiento(((Alimento) datosNuevos).getFechaVencimiento());
            }
            return true;
        }
        return false;
    }

    /**
     * Registra una venta, buscando el producto y reduciendo su stock.
     * @param productoId El ID del producto vendido.
     * @param cantidad La cantidad de unidades vendidas.
     * @return Un Optional que contiene el objeto Venta si fue exitosa, o vacío si no.
     */
    public Optional<Venta> registrarVenta(String productoId, int cantidad) {
        Optional<Producto> productoOpt = buscarProductoPorId(productoId);
        if (productoOpt.isPresent() && productoOpt.get().getStock() >= cantidad) {
            Producto producto = productoOpt.get();
            producto.reducirStock(cantidad);
            return Optional.of(new Venta(producto, cantidad));
        }
        return Optional.empty();
    }

    /**
     * Devuelve una lista de productos cuyo stock es menor o igual a un umbral.
     * @param umbral El nivel de stock para ser considerado bajo.
     * @return Una nueva lista con los productos que tienen stock bajo.
     */
    public List<Producto> getProductosConStockBajo(int umbral) {
        return listaProductos.stream().filter(p -> p.getStock() <= umbral).collect(Collectors.toList());
    }

    /**
     * Devuelve una copia de la lista de todos los productos en el inventario.
     * @return Una lista con todos los productos.
     */
    public List<Producto> getTodosLosProductos() {
        return new ArrayList<>(this.listaProductos);
    }
}