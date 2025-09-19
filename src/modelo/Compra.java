/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.math.BigDecimal;
import java.sql.Date;

public class Compra {
    private int id_compra;
    private int proveedor_id;
    private Date fecha;
    private String nombreProveedor; // Solo para mostrar en tabla

    public Compra() {}

    public Compra(int id_compra, int proveedor_id, Date fecha) {
        this.id_compra = id_compra;
        this.proveedor_id = proveedor_id;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId_compra() { return id_compra; }
    public void setId_compra(int id_compra) { this.id_compra = id_compra; }

    public int getProveedor_id() { return proveedor_id; }
    public void setProveedor_id(int proveedor_id) { this.proveedor_id = proveedor_id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }

    @Override
    public String toString() {
        return "Compra{" +
                "id_compra=" + id_compra +
                ", proveedor_id=" + proveedor_id +
                ", fecha=" + fecha +
                ", nombreProveedor='" + nombreProveedor + '\'' +
                '}';
    }
}