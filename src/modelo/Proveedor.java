/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author lore0
 */


import java.math.BigDecimal;

/**
 * Clase modelo para Proveedor
 * @author lore0
 */
public class Proveedor {
    private int idProveedor;
    private String nombre;
    private String empresa;
    private String telefono;
    private BigDecimal precioMinimo;
    private int plazoEntrega;
    private String contacto;

    // Constructor vacío
    public Proveedor() {
    }

    // Constructor con parámetros
    public Proveedor(int idProveedor, String nombre, String empresa, String telefono, 
                    BigDecimal precioMinimo, int plazoEntrega, String contacto) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.empresa = empresa;
        this.telefono = telefono;
        this.precioMinimo = precioMinimo;
        this.plazoEntrega = plazoEntrega;
        this.contacto = contacto;
    }

    // Constructor sin ID (para insertar)
    public Proveedor(String nombre, String empresa, String telefono, 
                    BigDecimal precioMinimo, int plazoEntrega, String contacto) {
        this.nombre = nombre;
        this.empresa = empresa;
        this.telefono = telefono;
        this.precioMinimo = precioMinimo;
        this.plazoEntrega = plazoEntrega;
        this.contacto = contacto;
    }

    // Getters y Setters
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(BigDecimal precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public int getPlazoEntrega() {
        return plazoEntrega;
    }

    public void setPlazoEntrega(int plazoEntrega) {
        this.plazoEntrega = plazoEntrega;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Proveedor{" +
                "idProveedor=" + idProveedor +
                ", nombre='" + nombre + '\'' +
                ", empresa='" + empresa + '\'' +
                ", telefono='" + telefono + '\'' +
                ", precioMinimo=" + precioMinimo +
                ", plazoEntrega=" + plazoEntrega +
                ", contacto='" + contacto + '\'' +
                '}';
    }

    public int getId_proveedor() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}