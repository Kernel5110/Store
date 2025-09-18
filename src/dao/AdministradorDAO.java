/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author lore0
 */

import modelo.Administrador;
import db.ConexionBD;
import java.sql.*;

public class AdministradorDAO {

    // Método para obtener una nueva conexión cada vez que se necesite
    private Connection getConnection() {
        return ConexionBD.getConexion();
    }

    public boolean insertarAdministrador(Administrador admin) {
        String sql = "INSERT INTO administrador(id_admin, nombre, telefono, correo, usuario, contrasena, rol) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, admin.getId_admin());
            ps.setString(2, admin.getNombre()); 
            ps.setString(3, admin.getTelefono()); 
            ps.setString(4, admin.getCorreo()); 
            ps.setString(5, admin.getUsuario());
            ps.setString(6, admin.getContrasena());
            ps.setString(7, admin.getRol()); 

            int result = ps.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar administrador: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarAdministrador(Administrador admin) {
        String sql = "UPDATE administrador SET nombre=?, telefono=?, correo=?, usuario=?, "
                   + "contrasena=?, rol=? WHERE id_admin=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admin.getNombre());
            ps.setString(2, admin.getTelefono());
            ps.setString(3, admin.getCorreo());
            ps.setString(4, admin.getUsuario());
            ps.setString(5, admin.getContrasena());
            ps.setString(6, admin.getRol());
            ps.setInt(7, admin.getId_admin());

            int result = ps.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar administrador: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarAdministrador(int id) {
        String sql = "DELETE FROM administrador WHERE id_admin=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int result = ps.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar administrador: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Administrador buscarAdministradorPorId(int id) {
        String sql = "SELECT id_admin, nombre, telefono, correo, usuario, contrasena, rol "
                   + "FROM administrador WHERE id_admin = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Administrador admin = new Administrador(
                        rs.getInt("id_admin"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getString("rol")
                    );
                    return admin;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar administrador por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public Administrador buscarAdministradorPorUsuario(String usuario) {
        String sql = "SELECT id_admin, nombre, telefono, correo, usuario, contrasena, rol "
                   + "FROM administrador WHERE usuario = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Administrador admin = new Administrador(
                        rs.getInt("id_admin"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getString("rol")
                    );
                    return admin;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar administrador por usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean validarCredenciales(String usuario, String contrasena) {
        String sql = "SELECT COUNT(*) FROM administrador WHERE usuario = ? AND contrasena = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al validar credenciales: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public int consultarStock() {
        // Implementación específica para consultar stock desde la base de datos
        String sql = "SELECT SUM(cantidad) AS total_stock FROM producto"; // Ejemplo genérico

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total_stock");
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar stock: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}