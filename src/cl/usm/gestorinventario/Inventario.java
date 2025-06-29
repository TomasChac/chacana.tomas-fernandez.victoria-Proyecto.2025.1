package cl.usm.gestorinventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Inventario {

    private static Inventario instancia;
    private List<Producto> listaProductos;

    private Inventario() {
        this.listaProductos = new ArrayList<>();
    }

    public static Inventario getInstancia() {
        if (instancia == null) {
            instancia = new Inventario();
        }
        return instancia;
    }

    public boolean agregarProducto(Producto nuevoProducto) {
        if (buscarProductoPorId(nuevoProducto.getId()).isPresent()) {
            System.err.println("Error: Ya existe un producto con el ID " + nuevoProducto.getId());
            return false;
        }
        this.listaProductos.add(nuevoProducto);
        return true;
    }

    public Optional<Producto> buscarProductoPorId(String id) {
        return listaProductos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * NUEVO: Busca productos por nombre (insensible a mayúsculas/minúsculas).
     * @param texto El texto a buscar en el nombre del producto.
     * @return Una lista de productos que coinciden con la búsqueda.
     */
    public List<Producto> buscarProductosPorNombre(String texto) {
        String textoBusqueda = texto.toLowerCase();
        return listaProductos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());
    }

    /**
     * NUEVO: Elimina un producto del inventario por su ID.
     * @param id El ID del producto a eliminar.
     * @return true si el producto fue encontrado y eliminado, false en caso contrario.
     */
    public boolean eliminarProducto(String id) {
        return this.listaProductos.removeIf(p -> p.getId().equals(id));
    }

    /**
     * NUEVO: Modifica un producto existente.
     * @param id El ID del producto a modificar.
     * @param datosNuevos Un objeto Producto con los nuevos datos.
     * @return true si el producto fue encontrado y modificado, false en caso contrario.
     */
    public boolean modificarProducto(String id, Producto datosNuevos) {
        Optional<Producto> productoOpt = buscarProductoPorId(id);
        if (productoOpt.isPresent()) {
            Producto productoAModificar = productoOpt.get();
            // Actualiza los campos comunes
            productoAModificar.setNombre(datosNuevos.getNombre());
            productoAModificar.setPrecio(datosNuevos.getPrecio());
            productoAModificar.setStock(datosNuevos.getStock());

            // Actualiza los campos específicos usando instanceof
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

    /**
     * MODIFICADO: Ahora devuelve un Optional<Venta> para un mejor manejo.
     */
    public Optional<Venta> registrarVenta(String productoId, int cantidad) {
        Optional<Producto> productoOpt = buscarProductoPorId(productoId);
        if (productoOpt.isPresent() && productoOpt.get().getStock() >= cantidad) {
            Producto producto = productoOpt.get();
            producto.reducirStock(cantidad);
            Venta nuevaVenta = new Venta(producto, cantidad);
            return Optional.of(nuevaVenta);
        }
        return Optional.empty(); // Devuelve un Optional vacío si falla
    }

    public List<Producto> getProductosConStockBajo(int umbral) {
        return listaProductos.stream()
                .filter(p -> p.getStock() <= umbral)
                .collect(Collectors.toList());
    }

    public List<Producto> getTodosLosProductos() {
        return new ArrayList<>(this.listaProductos);
    }
}