package cl.usm.gestorinventario;

/**
 * Representa un producto de tipo Ropa.
 * Hereda de la clase Producto y añade atributos específicos como talla y color.
 *
 * @author Tomás Ch., Victoria F., Jossefa Z.
 * @version 1.0
 */
public class Ropa extends Producto {

    private String talla;
    private String color;

    /**
     * Constructor para la clase Ropa.
     * @param id El ID de la prenda.
     * @param nombre El nombre de la prenda (ej. "Polera Manga Corta").
     * @param precio El precio de la prenda.
     * @param stock El stock disponible.
     * @param talla La talla de la prenda (ej. "M", "L", "XL").
     * @param color El color de la prenda.
     */
    public Ropa(String id, String nombre, double precio, int stock, String talla, String color) {
        super(id, nombre, precio, stock);
        this.talla = talla;
        this.color = color;
    }

    /**
     * Obtiene la talla de la prenda.
     * @return La talla.
     */
    public String getTalla() {
        return talla;
    }

    /**
     * Establece la talla de la prenda.
     * @param talla La nueva talla.
     */
    public void setTalla(String talla) {
        this.talla = talla;
    }

    /**
     * Obtiene el color de la prenda.
     * @return El color.
     */
    public String getColor() {
        return color;
    }

    /**
     * Establece el color de la prenda.
     * @param color El nuevo color.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Implementación concreta del método abstracto heredado de Producto.
     * Provee una descripción detallada y específica para una prenda de Ropa.
     * @return La descripción de la prenda.
     */
    @Override
    public String getDescripcionDetallada() {
        return "Ropa: " + nombre + ", Color: " + color + ", Talla: " + talla;
    }
}