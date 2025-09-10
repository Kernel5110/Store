package Objects;

public class Supplier {
    private String nombre;
    private String empresa;
    private int telefono;
    private String correo;

    public Supplier(String nombre, String empresa, int telefono, String correo) {
        this.nombre = nombre;
        this.empresa = empresa;
        this.telefono = telefono;
        this.correo = correo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }
}
