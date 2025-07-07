package cl.usm.gestorinventario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Representa una única transacción de venta de un producto.
 * Contiene información sobre el momento de la venta, el producto vendido,
 * su cantidad y el valor total de la transacción.
 *
 * @author Tomás Ch., Victoria F., Jossefa Z.
 * @version 1.0
 */
public class Venta {

    private final String idVenta;
    private final LocalDateTime fecha;
    private final Producto productoVendido;
    private final int cantidadVendida;
    private final double totalVenta;

    /**
     * Constructor para crear una nueva Venta.
     * Genera automáticamente un ID único y la fecha/hora actual.
     * Calcula el total de la venta basado en el producto y la cantidad.
     *
     * @param producto El producto que fue vendido.
     * @param cantidad La cantidad de unidades vendidas.
     */
    public Venta(Producto producto, int cantidad) {
        this.idVenta = UUID.randomUUID().toString().substring(0, 8);
        this.fecha = LocalDateTime.now();
        this.productoVendido = producto;
        this.cantidadVendida = cantidad;
        this.totalVenta = producto.getPrecio() * cantidad;
    }

    /**
     * Obtiene el ID único de la venta.
     * @return El ID de la venta.
     */
    public String getIdVenta() {
        return idVenta;
    }

    /**
     * Obtiene la fecha y hora en que se realizó la venta.
     * @return La fecha y hora de la venta.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Obtiene el objeto Producto que fue vendido.
     * @return El producto vendido.
     */
    public Producto getProductoVendido() {
        return productoVendido;
    }

    /**
     * Obtiene la cantidad de unidades que se vendieron.
     * @return La cantidad vendida.
     */
    public int getCantidadVendida() {
        return cantidadVendida;
    }

    /**
     * Obtiene el monto total de la venta.
     * @return El total de la venta.
     */
    public double getTotalVenta() {
        return totalVenta;
    }

    /**
     * Provee una representación en formato de texto de la Venta,
     * útil para depuración y para mostrar los detalles en la consola o en alertas.
     * @return Un String con el resumen de la venta.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format("Venta [ID: %s, Fecha: %s, Producto: %s, Cantidad: %d, Total: $%.2f]",
                idVenta, fecha.format(formatter), productoVendido.getNombre(), cantidadVendida, totalVenta);
    }
}