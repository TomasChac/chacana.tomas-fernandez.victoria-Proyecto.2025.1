package cl.usm.gestorinventario;

public class Ropa extends Producto {

    private String talla;
    private String color;

    public Ropa(String id, String nombre, double precio, int stock, String talla, String color) {
        super(id, nombre, precio, stock);
        this.talla = talla;
        this.color = color;
    }

    // Getters
    public String getTalla() { return talla; }
    public String getColor() { return color; }

    // Setters
    public void setTalla(String talla) { this.talla = talla; }
    public void setColor(String color) { this.color = color; }

    @Override
    public String getDescripcionDetallada() {
        return "Ropa: " + nombre + ", Color: " + color + ", Talla: " + talla;
    }
}