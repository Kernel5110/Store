package Objects;

import java.sql.Date;
import java.util.Vector;

public class Sale {
    private Date fecha;
    private Saller vendedor;
    private Vector<Product> producto;
    private double total;

    public Sale(Date fecha, Saller vendedor, Product[] producto, double total) {
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.producto = new Vector<Product>();
        this.total = total;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setVendedor(Saller vendedor) {
        this.vendedor = vendedor;
    }
    public Saller getVendedor() {
        return vendedor;
    }
    public void addProducto(Product p) {
        this.producto.add(p);
    }
    public Vector<Product> getProducto() {
        return producto;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public double getTotal() {
        return total;
    }
}
