/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
 /*
 * Author Isabella Rodriguez Granja- 17-11-2026
 */
public class Mascota implements java.io.Serializable {

    // Longitudes máximas indicadas en el diagrama
    private static final int LONG_CODIGO = 8;
    private static final int LONG_NOMBRE = 20;

    // Atributos
    private String codigo;   // 8 caracteres
    private String nombre;   // 20 caracteres
    private byte edad;
    private String color;
    private String caracter;

    // Constructor
    public Mascota(String codigo, String nombre, byte edad, String color, String caracter) {
        setCodigo(codigo);
        setNombre(nombre);
        setEdad(edad);
        setColor(color);
        setCaracter(caracter);
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public byte getEdad() {
        return edad;
    }

    public String getColor() {
        return color;
    }

    public String getCaracter() {
        return caracter;
    }

    // Setters
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.length() != LONG_CODIGO) {
            throw new IllegalArgumentException(
                "El código debe tener exactamente " + LONG_CODIGO + " caracteres");
        }
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank() || nombre.length() > LONG_NOMBRE) {
            throw new IllegalArgumentException(
                "El nombre debe tener entre 1 y " + LONG_NOMBRE + " caracteres");
        }
        this.nombre = nombre;
    }

    public void setEdad(byte edad) {
        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa");
        }
        this.edad = edad;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    @Override
    public String toString() {
        return "Mascota{"
                + "Código: " + codigo
                + ", Nombre: " + nombre
                + ", Edad: " + edad
                + ", Color: " + color
                + ", Carácter: " + caracter
                + '}';
    }
}