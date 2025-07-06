package cl.usm.gestorinventario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Gestiona todas las operaciones del inventario.
 * Implementa el patrón Singleton y ahora incluye persistencia de datos vía CSV
 */
public class Inventario {

    private static Inventario instancia;
    private List<Producto> listaProductos;

    private Inventario() {
        this.listaProductos = new ArrayList<>();
        cargarInventarioCSV();
    }

    public static Inventario getInstancia() {
        if (instancia == null) {
            instancia = new Inventario();
        }
        return instancia;
    }

    //  Metodos de persistencia

    /**
     * Guarda el estado actual del inventario en un archivo CSV.
     */
    public void guardarInventarioCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("inventario.csv"))) {
            writer.println("id,tipo,nombre,precio,stock,extra1,extra2");

            for (Producto p : this.listaProductos) {
                String tipo = "";
                String extra1 = "";
                String extra2 = "";

                if (p instanceof Libro) {
                    tipo = "Libro";
                    extra1 = ((Libro) p).getAutor();
                    extra2 = ((Libro) p).getIsbn();
                } else if (p instanceof Ropa) {
                    tipo = "Ropa";
                    extra1 = ((Ropa) p).getTalla();
                    extra2 = ((Ropa) p).getColor();
                }
                
                // Forzamos el formato de números con punto decimal usando Locale.US
                String lineaCSV = String.format(Locale.US, "\"%s\",%s,\"%s\",%.2f,%d,\"%s\",\"%s\"",
                        p.getId(), tipo, p.getNombre(), p.getPrecio(), p.getStock(), extra1, extra2);
                
                writer.println(lineaCSV);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el inventario en CSV: " + e.getMessage());
        }
    }

    /**
     * Carga los datos del inventario desde "inventario.csv" si existe.
     */
    private void cargarInventarioCSV() {
        File archivo = new File("inventario.csv");
        if (!archivo.exists()) {
            System.out.println("No se encontró 'inventario.csv'. Se iniciará con un inventario vacío.");
            return;
        }

        this.listaProductos.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea = reader.readLine();

            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (campos.length < 7) continue;

                for (int i = 0; i < campos.length; i++) {
                    campos[i] = campos[i].replaceAll("^\"|\"$", "");
                }
                
                String id = campos[0];
                String tipo = campos[1];
                String nombre = campos[2];
                double precio = Double.parseDouble(campos[3]);
                int stock = Integer.parseInt(campos[4]);
                String extra1 = campos[5];
                String extra2 = campos[6];

                Producto p = null;
                if ("Libro".equals(tipo)) {
                    p = new Libro(id, nombre, precio, stock, extra1, extra2);
                } else if ("Ropa".equals(tipo)) {
                    p = new Ropa(id, nombre, precio, stock, extra1, extra2);
                }

                if (p != null) {
                    this.listaProductos.add(p);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar el inventario desde CSV: " + e.getMessage());
        }
    }

    // Metodos de logica del negocio

    public boolean agregarProducto(Producto nuevoProducto) {
        if (buscarProductoPorId(nuevoProducto.getId()).isPresent()) {
            System.err.println("Error: Ya existe un producto con el ID " + nuevoProducto.getId());
            return false;
        }
        this.listaProductos.add(nuevoProducto);
        return true;
    }

    public Optional<Producto> buscarProductoPorId(String id) {
        return listaProductos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<Producto> buscarProductosPorNombre(String texto) {
        String textoBusqueda = texto.toLowerCase();
        return listaProductos.stream().filter(p -> p.getNombre().toLowerCase().contains(textoBusqueda)).collect(Collectors.toList());
    }

    public boolean eliminarProducto(String id) {
        return this.listaProductos.removeIf(p -> p.getId().equals(id));
    }

    public boolean modificarProducto(String id, Producto datosNuevos) {
        Optional<Producto> productoOpt = buscarProductoPorId(id);
        if (productoOpt.isPresent()) {
            Producto productoAModificar = productoOpt.get();
            productoAModificar.setNombre(datosNuevos.getNombre());
            productoAModificar.setPrecio(datosNuevos.getPrecio());
            productoAModificar.setStock(datosNuevos.getStock());
            if (productoAModificar instanceof Libro && datosNuevos instanceof Libro) {
                ((Libro) productoAModificar).setAutor(((Libro) datosNuevos).getAutor());
                ((Libro) productoAModificar).setIsbn(((Libro) datosNuevos).getIsbn());
            } else if (productoAModificar instanceof Ropa && datosNuevos instanceof Ropa) {
                ((Ropa) productoAModificar).setTalla(((Ropa) datosNuevos).getTalla());
                ((Ropa) productoAModificar).setColor(((Ropa) datosNuevos).getColor());
            }
            return true;
        }
        return false;
    }

    public Optional<Venta> registrarVenta(String productoId, int cantidad) {
        Optional<Producto> productoOpt = buscarProductoPorId(productoId);
        if (productoOpt.isPresent() && productoOpt.get().getStock() >= cantidad) {
            Producto producto = productoOpt.get();
            producto.reducirStock(cantidad);
            Venta nuevaVenta = new Venta(producto, cantidad);
            return Optional.of(nuevaVenta);
        }
        return Optional.empty();
    }

    public List<Producto> getProductosConStockBajo(int umbral) {
        return listaProductos.stream().filter(p -> p.getStock() <= umbral).collect(Collectors.toList());
    }

    public List<Producto> getTodosLosProductos() {
        return new ArrayList<>(this.listaProductos);
    }
}