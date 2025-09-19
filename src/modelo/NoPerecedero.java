/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author lore0
 */
public class NoPerecedero extends Producto {
    private String fechaEntrada;

    public NoPerecedero(String nombre, String tipo, double precio, String fechaEntrada) {
        super(nombre, tipo, precio);
        this.fechaEntrada = fechaEntrada;
    }
    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }
    public String getFechaEntrada() {
        return fechaEntrada;
    }
    
}
