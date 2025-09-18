/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase modelo para Almacen
 * @author lore0
 */
public class Almacen {
    private int idAlmacen;
    private String nombre;
    private String ubicacion;

    // Constructor vacío
    public Almacen() {
    }

    // Constructor con parámetros
    public Almacen(int idAlmacen, String nombre, String ubicacion) {
        this.idAlmacen = idAlmacen;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    // Constructor sin ID (para insertar)
    public Almacen(String nombre, String ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Almacen{" +
                "idAlmacen=" + idAlmacen +
                ", nombre='" + nombre + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}
