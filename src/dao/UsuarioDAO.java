/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author lore0
 */

import db.ConexionBD;
import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Insertar usuario
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario(nombre, numero_telefono, correo_electronico) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getTelefono());
            ps.setString(3, usuario.getCorreo());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }
    
    public enum RolUsuario {
        ADMINISTRADOR,
        PROVEEDOR,
        VENDEDOR,
        DESCONOCIDO // Para usuarios sin un rol asignado
    }

    
    
    // ... (El resto de tus métodos: autenticarUsuario, insertarUsuario, etc., van aquí sin cambios)
    
    // En tu clase UsuarioDAO.java

public Usuario autenticarUsuario(String correo, String telefono) {
    // La consulta ahora también selecciona la columna 'rol'
    String sql = "SELECT * FROM usuario WHERE correo_electronico = ? AND numero_telefono = ?";
    try (Connection con = ConexionBD.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, correo);
        ps.setString(2, telefono);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Creamos el objeto Usuario usando el constructor actualizado
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("numero_telefono"),
                        rs.getString("correo_electronico"),
                        rs.getString("rol") // <-- OBTENEMOS EL ROL DIRECTAMENTE
                );
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al autenticar usuario: " + e.getMessage());
    }
    return null;
}

// Recuerda también actualizar tus métodos de INSERTAR y ACTUALIZAR para incluir el rol.

    // Listar usuarios
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("numero_telefono"),
                        rs.getString("correo_electronico"),
                        rs.getString("rol")
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // Actualizar usuario
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre=?, numero_telefono=?, correo_electronico=? WHERE id_usuario=?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getTelefono());
            ps.setString(3, usuario.getCorreo());
            ps.setInt(4, usuario.getIdUsuario());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // Eliminar usuario
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario=?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}

