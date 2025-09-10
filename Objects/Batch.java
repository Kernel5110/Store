package Objects;


import java.util.*;

public class Batch {
    private int idLote;
    private Vector<Product> productos;
    private String fechaCaducidad;
    private int cantidad;

    public Batch(int idLote, String fechaCaducidad, int cantidad) {
        this.idLote = idLote;
        this.fechaCaducidad = fechaCaducidad;
        this.cantidad = cantidad;
        this.productos = new Vector<Product>(); ;
    }
    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }
    public int getIdLote() {
        return idLote;
    }
    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }
    public String getFechaCaducidad() {
        return fechaCaducidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void addProducto(Product producto) {
        this.productos.add(producto);
    }
    public Vector<Product> getProductos() {
        return productos;
    }
}
