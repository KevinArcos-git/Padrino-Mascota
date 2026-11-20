/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Nestor Pino
 * @version 1.0 - 16 de noviembre de 2026
 */

/*
 * Padrino: persona que apadrina mascotas del refugio "Amor verdadero".
 * Restricciones de los datos (se validan en la Vista, antes de crear el objeto):
 *      codigo: consecutivo entero generado por el sistema
 *      cedula: 6 a 11 caracteres
 *      correo: 15 a 50 caracteres
 *      telefono: 7 a 12 caracteres
 *      ciudad: 3 a 15 caracteres
 *      fechaHoraRegistro: formato dd/mm/aaaa hh:mm
 *      susApadrinamientos: mínimo 0, máximo 5 (se agregan posteriormente)
*/
public class Padrino implements Serializable{
    /*
     * Atributos
    */
    private int codigo;
    private String cedula;
    private String correo;
    private String telefono;
    private String ciudad;
    private LocalDateTime fechaHoraRegistro;
    private ArrayList<Apadrinamiento> susApadrinamientos; //mín 0, máx 5

    /*
     * Constructor completo
     * Los apadrinamientos se agregan posteriormente, por eso inicia con la lista vacía.
    */
    public Padrino(int codigo, String cedula, String correo, String telefono, String ciudad, LocalDateTime fechaHoraRegistro) {
        this.codigo = codigo;
        this.cedula = cedula;
        this.correo = correo;
        this.telefono = telefono;
        this.ciudad = ciudad;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.susApadrinamientos = new ArrayList<>();
    }

    /*
     * Getter and Setter
    */

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public ArrayList<Apadrinamiento> getSusApadrinamientos() {
        return susApadrinamientos;
    }

    public void setSusApadrinamientos(ArrayList<Apadrinamiento> susApadrinamientos) {
        this.susApadrinamientos = susApadrinamientos;
    }

    /*
     * toString
     * Solo incluye la información personal del padrino. Los apadrinamientos y el total
     * de aportes los conforma la Vista en cada consulta.
    */
    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Padrino{"
                + "Código: " + codigo
                + ", Cédula: " + cedula
                + ", Correo: " + correo
                + ", Teléfono: " + telefono
                + ", Ciudad: " + ciudad
                + ", Fecha y hora de registro: " + fechaHoraRegistro.format(formato)
                + '}';
    }

    /*
     * Métodos de negocio
    */

    /*
     * agregarApadrinamiento: agrega el apadrinamiento a la lista del padrino siempre que
     * no se haya alcanzado el máximo de 5. Retorna true si lo agregó, false si no.
    */
    public boolean agregarApadrinamiento(Apadrinamiento unApadrinamiento){
        boolean resultado = false;
        if(susApadrinamientos.size() < 5){
            susApadrinamientos.add(unApadrinamiento);
            resultado = true;
        }
        return resultado;
    }

    /*
     * calcularValorTotalAportes: sumatoria del valor total de aportes de cada uno de sus
     * apadrinamientos (COP).
    */
    public double calcularValorTotalAportes(){
        double resultado = 0.0;
        for(Apadrinamiento objeto : susApadrinamientos){
            resultado += objeto.calcularValorTotalAportes();
        }
        return resultado;
    }

}//fin class