package cl.usm.gestorinventario;

/**
 * Representa un producto de tipo Libro.
 * Hereda de la clase Producto y añade atributos específicos como autor e ISBN.
 *
 * @author Tomás Ch., Victoria F., Jossefa Z.
 * @version 1.0
 */
public class Libro extends Producto {

    private String autor;
    private String isbn;

    /**
     * Constructor para la clase Libro.
     * @param id El ID del libro.
     * @param nombre El título del libro.
     * @param precio El precio del libro.
     * @param stock El stock disponible.
     * @param autor El autor del libro.
     * @param isbn El código ISBN del libro.
     */
    public Libro(String id, String nombre, double precio, int stock, String autor, String isbn) {
        super(id, nombre, precio, stock);
        this.autor = autor;
        this.isbn = isbn;
    }

    /**
     * Obtiene el autor del libro.
     * @return El nombre del autor.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Establece el autor del libro.
     * @param autor El nuevo autor.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Obtiene el ISBN del libro.
     * @return El código ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Establece el ISBN del libro.
     * @param isbn El nuevo ISBN.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Implementación concreta del método abstracto heredado de Producto.
     * Provee una descripción detallada y específica para un Libro.
     * @return La descripción del libro.
     */
    @Override
    public String getDescripcionDetallada() {
        return "Libro: " + nombre + " por " + autor + " (ISBN: " + isbn + ")";
    }
}