package Objects;

public class NonPerishable extends Product {
    private String fechaEntrada;

    public NonPerishable(String nombre, String descripcion, double precio, String fechaEntrada) {
        super(nombre, descripcion, precio);
        this.fechaEntrada = fechaEntrada;
    }
    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }
    public String getFechaEntrada() {
        return fechaEntrada;
    }
    
}
