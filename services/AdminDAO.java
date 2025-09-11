package services;

import ConexionMySQL.ConexionMySQL;
import Objects.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    
    /**
     * Inserta un nuevo objeto Admin en la base de datos.
     * @param admin El objeto Admin a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertar(Admin admin) {
        String query = "INSERT INTO Administrador (nombre, apellidoPaterno, apellidoMaterno, telefono, correo, usuario, contrasena) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, admin.getNombre());
            stmt.setString(2, admin.getApellidoPaterno());
            stmt.setString(3, admin.getApellidoMaterno());
            stmt.setInt(4, admin.getTelefono());
            stmt.setString(5, admin.getCorreo());
            
            stmt.setString(6, admin.getUsuario());
            stmt.setString(7, admin.getContrasena());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar administrador: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca y obtiene un objeto Admin por su nombre de usuario.
     * @param usuario El nombre de usuario del administrador a buscar.
     * @return El objeto Admin si se encuentra, o null si no existe.
     */
    public Admin obtenerPorUsuario(String usuario) {
        String query = "SELECT * FROM Administrador WHERE usuario = ?";
        
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, usuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                        rs.getInt("id_admin"),
                        rs.getString("nombre"),
                        rs.getString("apellidoPaterno"),
                        rs.getString("apellidoMaterno"),
                        rs.getInt("telefono"),
                        rs.getString("correo"),
                        rs.getString("usuario"),
                        rs.getString("contrasena")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener administrador por usuario: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene una lista de todos los administradores en la base de datos.
     * @return Una lista de objetos Admin.
     */
    public List<Admin> obtenerTodos() {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM Administrador";
        
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Admin admin = new Admin(
                    rs.getInt("id_admin"),
                    rs.getString("nombre"),
                    rs.getString("apellidoPaterno"),
                    rs.getString("apellidoMaterno"),
                    rs.getInt("telefono"),
                    rs.getString("correo"),
                    rs.getString("usuario"),
                    rs.getString("contrasena")
                );
                admins.add(admin);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los administradores: " + e.getMessage());
        }
        return admins;
    }

    // ---
    
    /**
     * Actualiza un registro de administrador existente en la base de datos.
     * @param admin El objeto Admin con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizar(Admin admin) {
        String query = "UPDATE Administrador SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, telefono=?, correo=?, contrasena=? WHERE usuario=?";
        
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, admin.getNombre());
            stmt.setString(2, admin.getApellidoPaterno());
            stmt.setString(3, admin.getApellidoMaterno());
            stmt.setInt(4, admin.getTelefono());
            stmt.setString(5, admin.getCorreo());
            stmt.setString(6, admin.getContrasena());
            
            // La cláusula WHERE usa el usuario como identificador único.
            stmt.setString(7, admin.getUsuario());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar administrador: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un registro de administrador de la base de datos por su nombre de usuario.
     * @param usuario El nombre de usuario del administrador a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminar(String usuario) {
        String query = "DELETE FROM Administrador WHERE usuario=?";
        
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, usuario);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar administrador: " + e.getMessage());
            return false;
        }
    }
}