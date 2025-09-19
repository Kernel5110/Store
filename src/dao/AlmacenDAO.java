/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Almacen;
import db.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlmacenDAO {
    
    // Método para obtener una nueva conexión cada vez que se necesite
    private Connection getConnection() {
        return ConexionBD.getConexion();
    }
    
    public boolean insertarAlmacen(Almacen a) {
        String sql = "INSERT INTO almacen(nombre, ubicacion) VALUES (?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getUbicacion());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al insertar almacén: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Almacen> listarAlmacenes() {
        List<Almacen> lista = new ArrayList<>();
        String sql = "SELECT id_almacen, nombre, ubicacion FROM almacen ORDER BY id_almacen";
        
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Almacen a = new Almacen();
                a.setIdAlmacen(rs.getInt("id_almacen"));
                a.setNombre(rs.getString("nombre"));
                a.setUbicacion(rs.getString("ubicacion"));
                lista.add(a);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar almacenes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public boolean actualizarAlmacen(Almacen a) {
        String sql = "UPDATE almacen SET nombre=?, ubicacion=? WHERE id_almacen=?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getUbicacion());
            ps.setInt(3, a.getIdAlmacen());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar almacén: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarAlmacen(int id) {
        String sql = "DELETE FROM almacen WHERE id_almacen=?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al eliminar almacén: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Almacen buscarAlmacenPorId(int id) {
        String sql = "SELECT id_almacen, nombre, ubicacion FROM almacen WHERE id_almacen = ?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Almacen a = new Almacen();
                    a.setIdAlmacen(rs.getInt("id_almacen"));
                    a.setNombre(rs.getString("nombre"));
                    a.setUbicacion(rs.getString("ubicacion"));
                    return a;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error al buscar almacén por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean existeAlmacen(String nombre) {
        String sql = "SELECT COUNT(*) FROM almacen WHERE nombre = ?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia de almacén: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}