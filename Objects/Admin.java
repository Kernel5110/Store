package Objects;

public class Admin extends User {
    private String usuario;
    private String contrasena;

    public Admin(String nombre, String apellidoPaterno, String apellidoMaterno,
                 int telefono, String correo, String usuario, String contrasena) {
        super(nombre, apellidoPaterno, apellidoMaterno, telefono, correo, "Administrador");
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getContrasena() {
        return contrasena;
    }

    @Override
    protected int consultarStock() {
        // Implementación específica para el administrador
        // Por ejemplo, podría devolver un valor fijo o realizar una consulta a la base de datos
        return 0; // Placeholder
    }
    
}
