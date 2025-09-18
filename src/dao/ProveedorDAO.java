/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author lore0
 */


import modelo.Proveedor;
import db.ConexionBD;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {
    
    // Método para obtener una nueva conexión cada vez que se necesite
    private Connection getConnection() {
        return ConexionBD.getConexion();
    }
    
    public boolean insertarProveedor(Proveedor p) {
        String sql = "INSERT INTO proveedor(nombre, empresa, telefono, precio_minimo, plazo_entrega, contacto) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getEmpresa());
            ps.setString(3, p.getTelefono());
            ps.setBigDecimal(4, p.getPrecioMinimo());
            ps.setInt(5, p.getPlazoEntrega());
            ps.setString(6, p.getContacto());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al insertar proveedor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Proveedor> listarProveedores() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT id_proveedor, nombre, empresa, telefono, precio_minimo, plazo_entrega, contacto FROM proveedor ORDER BY id_proveedor";
        
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Proveedor p = new Proveedor();
                p.setIdProveedor(rs.getInt("id_proveedor"));
                p.setNombre(rs.getString("nombre"));
                p.setEmpresa(rs.getString("empresa"));
                p.setTelefono(rs.getString("telefono"));
                p.setPrecioMinimo(rs.getBigDecimal("precio_minimo"));
                p.setPlazoEntrega(rs.getInt("plazo_entrega"));
                p.setContacto(rs.getString("contacto"));
                lista.add(p);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar proveedores: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public boolean actualizarProveedor(Proveedor p) {
        String sql = "UPDATE proveedor SET nombre=?, empresa=?, telefono=?, precio_minimo=?, plazo_entrega=?, contacto=? WHERE id_proveedor=?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getEmpresa());
            ps.setString(3, p.getTelefono());
            ps.setBigDecimal(4, p.getPrecioMinimo());
            ps.setInt(5, p.getPlazoEntrega());
            ps.setString(6, p.getContacto());
            ps.setInt(7, p.getIdProveedor());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar proveedor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarProveedor(int id) {
        String sql = "DELETE FROM proveedor WHERE id_proveedor=?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al eliminar proveedor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Proveedor buscarProveedorPorId(int id) {
        String sql = "SELECT id_proveedor, nombre, empresa, telefono, precio_minimo, plazo_entrega, contacto FROM proveedor WHERE id_proveedor = ?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Proveedor p = new Proveedor();
                    p.setIdProveedor(rs.getInt("id_proveedor"));
                    p.setNombre(rs.getString("nombre"));
                    p.setEmpresa(rs.getString("empresa"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setPrecioMinimo(rs.getBigDecimal("precio_minimo"));
                    p.setPlazoEntrega(rs.getInt("plazo_entrega"));
                    p.setContacto(rs.getString("contacto"));
                    return p;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error al buscar proveedor por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean existeProveedor(String nombre) {
        String sql = "SELECT COUNT(*) FROM proveedor WHERE nombre = ?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia de proveedor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}
