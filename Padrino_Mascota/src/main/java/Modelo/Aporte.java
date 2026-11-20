/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/*
 * Author Kevin cristino Salazar Arcos
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Aporte implements java.io.Serializable {

    protected static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected LocalDate fechaEntrega;
    protected String modoEntrega;   // hasta 50 caracteres
    protected double cantidad;      // 2 cifras decimales
    protected String moneda;        // ej. COP, USD

    public Aporte(LocalDate fechaEntrega, String modoEntrega, double cantidad, String moneda) {
        setFechaEntrega(fechaEntrega);
        setModoEntrega(modoEntrega);
        setCantidad(cantidad);
        setMoneda(moneda);
    }

    public abstract double calcularValorTotal();

    // Getters
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public String getModoEntrega() { return modoEntrega; }
    public double getCantidad() { return cantidad; }
    public String getMoneda() { return moneda; }

    // Setters
    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public void setModoEntrega(String modoEntrega) {
        if (modoEntrega != null && modoEntrega.length() > 50) {
            throw new IllegalArgumentException("El modo de entrega no puede superar 50 caracteres.");
        }
        this.modoEntrega = modoEntrega;
    }

    public void setCantidad(double cantidad) {
        // Se redondea a 2 cifras decimales
        this.cantidad = Math.round(cantidad * 100.0) / 100.0;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Override
    public String toString() {
        return "Fecha de entrega: " + fechaEntrega.format(FORMATO)
                + ", Modo de entrega: " + modoEntrega
                + ", Cantidad: " + String.format("%.2f", cantidad)
                + ", Moneda: " + moneda;
    }
}