package cl.usm.gestorinventario;

import java.util.List;
import java.util.Optional;

public class TestBackend {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO PRUEBA DEL BACKEND ---");

        // 1. Obtenemos la instancia única del inventario (Singleton)
        Inventario inventario = Inventario.getInstancia();

        // 2. Agregamos productos de diferentes tipos (Herencia y Polimorfismo)
        System.out.println("\n[ACCIÓN] Agregando productos al inventario...");
        inventario.agregarProducto(new Libro("L001", "El Señor de los Anillos", 25000, 10, "J.R.R. Tolkien", "978-0618640157"));
        inventario.agregarProducto(new Ropa("R001", "Polera de Algodón", 15000, 20, "M", "Azul"));
        inventario.agregarProducto(new Ropa("R002", "Pantalón Cargo", 35000, 5, "44", "Beige"));

        // 3. Mostramos el estado inicial del inventario
        imprimirInventario("--- ESTADO INICIAL DEL INVENTARIO ---", inventario);

        // 4. Probamos la búsqueda
        System.out.println("\n[ACCIÓN] Buscando productos que contengan 'polera'...");
        List<Producto> resultadosBusqueda = inventario.buscarProductosPorNombre("polera");
        System.out.println("Resultados de la búsqueda:");
        resultadosBusqueda.forEach(p -> System.out.println(" - " + p.getDescripcionDetallada()));

        // 5. Probamos la modificación
        System.out.println("\n[ACCIÓN] Modificando el precio y stock de la Polera (R001)...");
        Ropa poleraModificada = new Ropa("R001", "Polera de Algodón Premium", 18000, 25, "M", "Azul Marino");
        inventario.modificarProducto("R001", poleraModificada);

        // 6. Probamos una venta
        System.out.println("\n[ACCIÓN] Registrando la venta de 3 Pantalones (R002)...");
        Optional<Venta> ventaRealizada = inventario.registrarVenta("R002", 3);
        if (ventaRealizada.isPresent()) {
            System.out.println("Venta exitosa: " + ventaRealizada.get().toString());
        } else {
            System.out.println("La venta no se pudo realizar.");
        }
        
        // 7. Probamos eliminar un producto
        System.out.println("\n[ACCIÓN] Eliminando 'El Señor de los Anillos' (L001) del inventario...");
        inventario.eliminarProducto("L001");


        // 8. Mostramos el estado final del inventario
        imprimirInventario("\n--- ESTADO FINAL DEL INVENTARIO ---", inventario);

        // 9. Probamos el reporte de stock bajo
        System.out.println("\n[ACCIÓN] Generando reporte de stock bajo (umbral <= 5)...");
        List<Producto> stockBajo = inventario.getProductosConStockBajo(5);
        System.out.println("Productos con stock bajo:");
        stockBajo.forEach(p -> System.out.println(" - " + p.getNombre() + ", Stock: " + p.getStock()));
        
        System.out.println("\n--- PRUEBA DEL BACKEND FINALIZADA ---");
    }

    /**
     * Método de ayuda para imprimir el estado actual del inventario de forma ordenada.
     */
    public static void imprimirInventario(String titulo, Inventario inv) {
        System.out.println(titulo);
        System.out.println("------------------------------------");
        if (inv.getTodosLosProductos().isEmpty()) {
            System.out.println("El inventario está vacío.");
        } else {
            for (Producto p : inv.getTodosLosProductos()) {
                System.out.printf("ID: %-5s | Nombre: %-25s | Stock: %-3d | Precio: $%-8.0f | Desc: %s\n",
                        p.getId(), p.getNombre(), p.getStock(), p.getPrecio(), p.getDescripcionDetallada());
            }
        }
        System.out.println("------------------------------------");
    }
}