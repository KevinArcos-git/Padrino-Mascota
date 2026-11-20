/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
/*
 * Kevin cristino Salazar Arcos
 */
import java.time.LocalDate;

public final class AporteEfectivo extends Aporte {

    private int tasaCambio; // COP

    public AporteEfectivo(LocalDate fechaEntrega, String modoEntrega, double cantidad,
                          String moneda, int tasaCambio) {
        super(fechaEntrega, modoEntrega, cantidad, moneda);
        this.tasaCambio = tasaCambio;
    }

    @Override
    public double calcularValorTotal() {
        // Si ya está en COP no hay conversión; si no, se multiplica por la tasa de cambio
        if ("COP".equalsIgnoreCase(moneda)) {
            return cantidad;
        }
        return cantidad * tasaCambio;
    }

    // Getter y Setter
    public int getTasaCambio() { return tasaCambio; }
    public void setTasaCambio(int tasaCambio) { this.tasaCambio = tasaCambio; }

    @Override
    public String toString() {
        return super.toString() + ", Tasa de cambio: " + tasaCambio + " COP";
    }
}
