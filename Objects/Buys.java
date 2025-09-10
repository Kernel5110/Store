package Objects;

import java.sql.Date;

public class Buys {
    private Product producto;
    private int cantidad;
    private double total;
    private Date fechaCompra;

    public Buys(Product producto, int cantidad, double total, Date fechaCompra) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.total = total;
        this.fechaCompra = fechaCompra;
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
    public Date getFechaCompra() {
        return fechaCompra;
    }
    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    public Product[] getCompras() {
        // Lógica para obtener las compras
        return new Product[0]; // Retorna un arreglo vacío como marcador de posición
    }
    public void actualizarStock(int nuevaCantidad) {
        // Lógica para actualizar el stock del producto
        // Por ejemplo, podrías restar la nuevaCantidad del stock actual
    }
}
