/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author lore0
 */


public class Producto {
    private int idProducto;
    private String nombre;
    private double precio;
    private String tipo; // "perecedero" o "no_perecedero"
    private int stock;

    public Producto() {
    }
    public Producto(int idProducto, String nombre, double precio, String tipo, int stock ) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
        this.stock = stock;
    }
    public Producto(String nombre, String tipo, double precio){
        this.nombre=nombre;
        this.tipo=tipo;
        this.precio=precio;
    }
    public Producto(int id, String nombre, String tipo, double precio){
        this.idProducto=id;
        this.nombre=nombre;
        this.tipo=tipo;
        this.precio=precio;
    }
    // Getters y setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}

