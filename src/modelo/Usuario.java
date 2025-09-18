/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author lore0
 */




public class Usuario {
    protected int idUsuario;
    protected String nombre;
    protected String numeroTelefono;
    protected String correoElectronico;
    protected String rol;

    // Constructor vacío
    public Usuario() {
    }
    
    public Usuario(int ID, String NOMBRE, String NUM, String CORREO) {
        this.idUsuario=ID;
        this.nombre=NOMBRE;
        this.correoElectronico=CORREO;
        this.numeroTelefono=NUM;
    }

    // Constructor completo
    public Usuario(int idUsuario, String nombre, String numeroTelefono, String correoElectronico, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.numeroTelefono = numeroTelefono;
        this.correoElectronico = correoElectronico;
        this.rol = rol;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return numeroTelefono;
    }

    public void setTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCorreo() {
        return correoElectronico;
    }

    public void setCorreo(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
     public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

