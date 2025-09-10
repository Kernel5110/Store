package Objects;

import java.util.Date;

public class PurchaseReport {
    private Date fecha;
    private Product producto;
    private int cantidad;  
    private double total;

    public PurchaseReport(Date fecha, Product producto, int cantidad, double total) {
        this.fecha = fecha;
        this.producto = producto;
        this.cantidad = cantidad;
        this.total = total;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Product getProducto() {
        return producto;
    }
    public void setProducto(Product producto) {
        this.producto = producto;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public Buys[] getCompras() {
        // Lógica para obtener las compras
        return new Buys[0]; // Retorna un arreglo vacío como marcador de posición
    }
    public Buys[] getCompras(Date fechaCompra) {
        // Lógica para obtener las ventas
        return new Buys[0]; // Retorna un arreglo vacío como marcador de posición
    }
}
