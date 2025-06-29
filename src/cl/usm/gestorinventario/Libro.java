package cl.usm.gestorinventario;

public class Libro extends Producto {

    private String autor;
    private String isbn;

    public Libro(String id, String nombre, double precio, int stock, String autor, String isbn) {
        super(id, nombre, precio, stock);
        this.autor = autor;
        this.isbn = isbn;
    }

    // Getters
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }

    // Setters
    public void setAutor(String autor) { this.autor = autor; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    @Override
    public String getDescripcionDetallada() {
        return "Libro: " + nombre + " por " + autor + " (ISBN: " + isbn + ")";
    }
}