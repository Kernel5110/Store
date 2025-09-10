package Objects;

public abstract class User {
    protected String nombre;
    protected String apellidoPaterno;
    protected String apellidoMaterno;

    protected int telefono;
    protected String correo;

    protected String rol;
    
    public User(String nombre, String apellidoPaterno, String apellidoMaterno,
                int telefono, String correo, String rol) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.correo = correo;
        this.rol = rol;
    }

    protected void setNombre(String nombre) {
        this.nombre = nombre;
    }
    protected String getNombre() {
        return nombre;
    }
    protected void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    protected String getApellidoMaterno() {
        return apellidoMaterno;
    }
    protected void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }
    protected String getApellidoPaterno() {
        return apellidoPaterno;
    }
    protected void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    protected int getTelefono() {
        return telefono;
    }
    protected void setCorreo(String correo) {
        this.correo = correo;
    }
    protected String getCorreo() {
        return correo;
    }
    protected abstract int consultarStock();
}