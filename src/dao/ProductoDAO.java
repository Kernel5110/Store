package dao;

import modelo.Producto;
import db.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    
    // Método para obtener una nueva conexión cada vez que se necesite
    private Connection getConnection() {
        return ConexionBD.getConexion();
    }
    
    public boolean insertarProducto(Producto p) {
        String sql = "INSERT INTO producto(nombre, precio, tipo) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getTipo());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, precio, tipo FROM producto";
        
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setTipo(rs.getString("tipo"));
                lista.add(p);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public boolean actualizarProducto(Producto p) {
        String sql = "UPDATE producto SET nombre=?, precio=?, tipo=? WHERE id_producto=?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getTipo());
            ps.setInt(4, p.getIdProducto());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM producto WHERE id_producto=?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Método adicional para buscar un producto por ID
    public Producto buscarProductoPorId(int id) {
        String sql = "SELECT id_producto, nombre, precio, tipo FROM producto WHERE id_producto = ?";
        
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setTipo(rs.getString("tipo"));
                    return p;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error al buscar producto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}