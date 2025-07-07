package cl.usm.gestorinventario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Representa un producto de tipo Alimento.
 * Hereda de la clase Producto y añade una fecha de vencimiento.
 *
 * @author Tomás Ch., Victoria F., Jossefa Z.
 * @version 1.0
 */
public class Alimento extends Producto {

    private LocalDate fechaVencimiento;

    /**
     * Constructor para la clase Alimento.
     * @param id El ID del alimento.
     * @param nombre El nombre del alimento.
     * @param precio El precio del alimento.
     * @param stock El stock disponible.
     * @param fechaVencimiento La fecha de vencimiento del producto.
     */
    public Alimento(String id, String nombre, double precio, int stock, LocalDate fechaVencimiento) {
        super(id, nombre, precio, stock);
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Obtiene la fecha de vencimiento del alimento.
     * @return La fecha de vencimiento.
     */
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * Establece la fecha de vencimiento del alimento.
     * @param fechaVencimiento La nueva fecha de vencimiento.
     */
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Implementación concreta del método abstracto para un Alimento.
     * @return La descripción detallada del alimento, incluyendo su fecha de vencimiento.
     */
    @Override
    public String getDescripcionDetallada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Alimento: " + nombre + " (Vence: " + fechaVencimiento.format(formatter) + ")";
    }
}