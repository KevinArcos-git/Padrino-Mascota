/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Jacobo
 * @version 1.0 - 16 de noviembre de 2026
 */

/*
 * Restricciones de los datos (se validan en la Vista, antes de crear el objeto):
 *      numero: consecutivo entero generado automáticamente por el sistema
 *      fechaHoraRegistro: formato dd/mm/aaaa hh:mm
 *      suMascota: una mascota (código de 8 caracteres)
 *      susAportes: mínimo 1
*/
public class Apadrinamiento implements Serializable{
    /*
     * Atributos
    */
    private int numero;
    private LocalDateTime fechaHoraRegistro;
    private Mascota suMascota;
    private ArrayList<Aporte> susAportes; //mín 1

    /*
     * Constructor completo
     * Recibe el primer aporte porque un apadrinamiento requiere mínimo 1 aporte;
     * los demás se agregan después con agregarAporte.
    */
    public Apadrinamiento(int numero, LocalDateTime fechaHoraRegistro, Mascota suMascota, Aporte susAportes) {
        this.numero = numero;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.suMascota = suMascota;
        this.susAportes = new ArrayList<>();
        this.susAportes.add(susAportes);
    }

    /*
     * Getter and Setter
    */

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public Mascota getSuMascota() {
        return suMascota;
    }

    public void setSuMascota(Mascota suMascota) {
        this.suMascota = suMascota;
    }

    public ArrayList<Aporte> getSusAportes() {
        return susAportes;
    }

    public void setSusAportes(ArrayList<Aporte> susAportes) {
        this.susAportes = susAportes;
    }

    /*
     * toString
     * Incluye los datos del apadrinamiento y de su mascota. Los aportes y su valor total
     * los conforma la Vista en cada consulta.
    */
    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Apadrinamiento{"
                + "Número: " + numero
                + ", Fecha y hora de registro: " + fechaHoraRegistro.format(formato)
                + ", Mascota: " + suMascota.toString()
                + '}';
    }

    /*
     * Métodos de negocio
    */

    /*
     * agregarAporte: agrega un aporte a la lista de aportes del apadrinamiento.
    */
    public void agregarAporte(Aporte unAporte){
        susAportes.add(unAporte);
    }

    /*
     * calcularValorTotalAportes: sumatoria del valor total de cada uno de sus aportes (COP).
    */
    public double calcularValorTotalAportes(){
        double resultado = 0.0;
        for(Aporte objeto : susAportes){
            resultado += objeto.calcularValorTotal();
        }
        return resultado;
    }

    /*
     * estaEnRangoFechas: indica si la fecha de registro del apadrinamiento está dentro del
     * rango de fechas dado, incluyendo la fecha de inicio y la fecha final.
    */
    public boolean estaEnRangoFechas(LocalDate fechaInicio, LocalDate fechaFin){
        boolean resultado = false;
        LocalDate fechaRegistro = fechaHoraRegistro.toLocalDate();
        if(!fechaRegistro.isBefore(fechaInicio) && !fechaRegistro.isAfter(fechaFin)){
            resultado = true;
        }
        return resultado;
    }

}//fin class