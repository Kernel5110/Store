package Objects;

public class Perishable extends Product {
    private String expirationDate;

    public Perishable(String nombre, String descripcion, double precio, String expirationDate) {
        super(nombre, descripcion, precio);
        this.expirationDate = expirationDate;
    }
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    public String getExpirationDate() {
        return expirationDate;
    }
    
}
