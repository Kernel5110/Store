package Objects;

public class Admin extends User {
    private int id_admin;
    private String usuario;
    private String contrasena;

    
    public Admin(int id_admin, String nombre, String apellidoPaterno, String apellidoMaterno,
                 int telefono, String correo, String usuario, String contrasena) {
        super(nombre, apellidoPaterno, apellidoMaterno, telefono, correo, "Administrador");
        this.id_admin=id_admin;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }
    public int getId_admin() {
        return id_admin;
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
