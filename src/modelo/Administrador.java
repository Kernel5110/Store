/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author lore0
 */
public class Administrador extends Usuario {
    private int id_admin;
    private String usuario;
    private String contrasena;

    
    public Administrador(int id_admin, String nombre,String telefono, String correo, String usuario, String contrasena, String rol) {
        super(id_admin, nombre, telefono, correo, rol);
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
    
    protected int consultarStock() {
        // Implementación específica para el administrador
        // Por ejemplo, podría devolver un valor fijo o realizar una consulta a la base de datos
        return 0; // Placeholder
    }
    
}
