/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author lore0
 */
public class Perecedero extends Producto{
    private String expirationDate;

    public Perecedero(String nombre, String tipo, double precio, String expirationDate) {
        super(nombre, tipo, precio);
        this.expirationDate = expirationDate;
    }
    public void setCaducidad(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    public String getCaducidad() {
        return expirationDate;
    }
}
