/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author lore0
 */


public class CompraProducto {
    private int compra_id;
    private int producto_id;
    private int cantidad;

    public CompraProducto() {}

    public CompraProducto(int compra_id, int producto_id, int cantidad) {
        this.compra_id = compra_id;
        this.producto_id = producto_id;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getCompra_id() { return compra_id; }
    public void setCompra_id(int compra_id) { this.compra_id = compra_id; }

    public int getProducto_id() { return producto_id; }
    public void setProducto_id(int producto_id) { this.producto_id = producto_id; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}