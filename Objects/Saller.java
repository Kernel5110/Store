package Objects;

public class Saller extends User {
    private String usuario;

    public Saller(String nombre, String apellidoPaterno, String apellidoMaterno, int telefono, String correo, String rol, String usuario) {
        super(nombre, apellidoPaterno, apellidoMaterno, telefono, correo, rol);
        this.usuario = usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getUsuario() {
        return usuario;
    }

    @Override
    protected int consultarStock() {
        // Implementación específica para el vendedor
        // Por ejemplo, podría devolver un valor fijo o realizar una consulta a la base de datos
        return 0; // Placeholder
    }
    
}
