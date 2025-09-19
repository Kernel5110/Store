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
    
    public List<Producto> listarProductosPorProveedor(int idProveedor) {
    List<Producto> productos = new ArrayList<>();
        String sql = """
            SELECT
                p.id_producto,
                p.nombre,
                p.precio,
                cp.cantidad
            FROM
                Producto p
            LEFT JOIN
                compra_producto cp ON p.id_producto = cp.producto_id
            LEFT JOIN
                compra c ON cp.compra_id = c.id_compra  -- JOIN con la tabla compra
            WHERE
                c.proveedor_id = ?  -- Filtrar por proveedor
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProveedor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("cantidad"));
                productos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }
    
    public List<Producto> listarProductosStock() {
        List<Producto> lista = new ArrayList<>();
        String sql = """
            SELECT
                p.id_producto,
                p.nombre,
                p.precio,
                p.tipo,
                COALESCE(SUM(cp.cantidad), 0) - COALESCE(SUM(vp.cantidad), 0) AS stock_actual
            FROM
                producto AS p
            LEFT JOIN
                compra_producto AS cp ON p.id_producto = cp.producto_id
            LEFT JOIN
                venta_producto AS vp ON p.id_producto = vp.producto_id
            GROUP BY
                p.id_producto, p.nombre, p.precio, p.tipo
            ORDER BY
                p.id_producto
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setTipo(rs.getString("tipo"));
                p.setStock(rs.getInt("stock_actual"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos con stock: " + e.getMessage());
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
        String sql = """
            SELECT
                p.id_producto,
                p.nombre,
                p.precio,
                p.tipo,
                COALESCE(SUM(cp.cantidad), 0) - COALESCE(SUM(vp.cantidad), 0) AS stock_actual
            FROM
                producto AS p
            LEFT JOIN
                compra_producto AS cp ON p.id_producto = cp.producto_id
            LEFT JOIN
                venta_producto AS vp ON p.id_producto = vp.producto_id
            WHERE
                p.nombre = ?
            GROUP BY
                p.id_producto, p.nombre, p.precio, p.tipo
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setTipo(rs.getString("tipo"));
                    p.setStock(rs.getInt("stock_actual"));
                    return p;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto por nombre exacto: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public int getStock(int idProducto) {
        int stockComprado = 0;
        int stockVendido = 0;

        // Consulta para obtener la cantidad comprada de un producto
        String sqlCompra = "SELECT COALESCE(SUM(cantidad), 0) AS total_comprado FROM compra_producto WHERE producto_id = ?";

        // Consulta para obtener la cantidad vendida de un producto
        String sqlVenta = "SELECT COALESCE(SUM(cantidad), 0) AS total_vendido FROM venta_producto WHERE producto_id = ?";

        try (Connection con = getConnection()) {

            // Obtener la cantidad total comprada
            try (PreparedStatement psCompra = con.prepareStatement(sqlCompra)) {
                psCompra.setInt(1, idProducto);
                try (ResultSet rs = psCompra.executeQuery()) {
                    if (rs.next()) {
                        stockComprado = rs.getInt("total_comprado");
                    }
                }
            }

            // Obtener la cantidad total vendida
            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta)) {
                psVenta.setInt(1, idProducto);
                try (ResultSet rs = psVenta.executeQuery()) {
                    if (rs.next()) {
                        stockVendido = rs.getInt("total_vendido");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al calcular stock para el producto " + idProducto);
            e.printStackTrace();
        }

        return stockComprado - stockVendido;
    }
}