/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
/*
 * Author Kevin cristino Salazar Arcos
 */
import static Modelo.Aporte.FORMATO;
import java.time.LocalDate;

public final class AporteArticulo extends Aporte {

    private String categoria;        // alimento, medicamento, producto aseo, otro
    private int valorUnitario;       // COP
    private LocalDate fechaVencimiento;
    private boolean perecedero;

    public AporteArticulo(LocalDate fechaEntrega, String modoEntrega, double cantidad, String moneda,
                          String categoria, int valorUnitario, LocalDate fechaVencimiento,
                          boolean perecedero) {
        super(fechaEntrega, modoEntrega, cantidad, moneda);
        setCategoria(categoria);
        this.valorUnitario = valorUnitario;
        this.fechaVencimiento = fechaVencimiento;
        this.perecedero = perecedero;
    }

    @Override
    public double calcularValorTotal() {
        return cantidad * valorUnitario;
    }

    // Getters
    public String getCategoria() { return categoria; }
    public int getValorUnitario() { return valorUnitario; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public boolean isPerecedero() { return perecedero; }

    // Setters
    public void setCategoria(String categoria) {
        String c = categoria == null ? "" : categoria.toLowerCase();
        if (!c.equals("alimento") && !c.equals("medicamento")
                && !c.equals("producto aseo") && !c.equals("otro")) {
            throw new IllegalArgumentException(
                "Categoría inválida. Use: alimento, medicamento, producto aseo u otro.");
        }
        this.categoria = c;
    }

    public void setValorUnitario(int valorUnitario) { this.valorUnitario = valorUnitario; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public void setPerecedero(boolean perecedero) { this.perecedero = perecedero; }

    @Override
    public String toString() {
        return super.toString()
                + ", Categoría: " + categoria
                + ", Valor unitario: " + valorUnitario + " COP"
                + ", Vence: " + (fechaVencimiento != null ? fechaVencimiento.format(FORMATO) : "N/A")
                + ", Perecedero: " + (perecedero ? "Sí" : "No");
    }
}
