package cl.usm.gestorinventario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Venta {

    private final String idVenta;
    private final LocalDateTime fecha;
    private final Producto productoVendido;
    private final int cantidadVendida;
    private final double totalVenta;

    public Venta(Producto producto, int cantidad) {
        this.idVenta = UUID.randomUUID().toString().substring(0, 8); // ID más corto
        this.fecha = LocalDateTime.now();
        this.productoVendido = producto;
        this.cantidadVendida = cantidad;
        this.totalVenta = producto.getPrecio() * cantidad;
    }
    
    // Getters para todos los atributos...
    public String getIdVenta() { return idVenta; }
    public LocalDateTime getFecha() { return fecha; }
    public Producto getProductoVendido() { return productoVendido; }
    public int getCantidadVendida() { return cantidadVendida; }
    public double getTotalVenta() { return totalVenta; }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format("Venta [ID: %s, Fecha: %s, Producto: %s, Cantidad: %d, Total: $%.2f]",
                idVenta, fecha.format(formatter), productoVendido.getNombre(), cantidadVendida, totalVenta);
    }
}