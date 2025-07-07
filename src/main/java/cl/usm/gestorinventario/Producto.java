package cl.usm.gestorinventario;

/**
 * Representa la base para cualquier producto en el inventario.
 * Es una clase abstracta, lo que significa que no se pueden crear instancias
 * directas de Producto, solo de sus clases hijas.
 *
 * @author Tomás Ch., Victoria F., Jossefa Z.
 * @version 1.0
 */
public abstract class Producto {

    protected String id;
    protected String nombre;
    protected double precio;
    protected int stock;

    /**
     * Constructor para inicializar los atributos comunes de todo producto.
     * @param id El identificador único del producto.
     * @param nombre El nombre del producto.
     * @param precio El precio de venta del producto.
     * @param stock La cantidad inicial en inventario.
     */
    public Producto(String id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    /**
     * Reduce el stock del producto en una cantidad determinada.
     * No permite que el stock sea negativo.
     * @param cantidad La cantidad a reducir.
     */
    public void reducirStock(int cantidad) {
        if (cantidad > 0 && this.stock >= cantidad) {
            this.stock -= cantidad;
        }
    }

    /**
     * Aumenta el stock del producto en una cantidad determinada.
     * @param cantidad La cantidad a aumentar.
     */
    public void aumentarStock(int cantidad) {
        if (cantidad > 0) {
            this.stock += cantidad;
        }
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    // --- Setters ---
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }


    /**
     * Método abstracto que obliga a las clases hijas a proporcionar una descripción detallada.
     * @return Una cadena de texto con la descripción específica del producto.
     */
    public abstract String getDescripcionDetallada();
}