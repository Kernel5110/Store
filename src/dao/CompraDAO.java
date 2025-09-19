/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Compra;
import modelo.CompraProducto;
import modelo.Proveedor;
import modelo.Producto;
import db.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    private Connection connection;

    private Connection getConnection() {
        return ConexionBD.getConexion();
    }

    public boolean insertarCompra(Compra compra, List<CompraProducto> detalles) {
        String sqlCompra = "INSERT INTO Compra (proveedor_id, fecha) VALUES (?, ?)";
        String sqlDetalle = "INSERT INTO Compra_Producto (compra_id, producto_id, cantidad) VALUES (?, ?, ?)";
        String sqlUpdateStock = "UPDATE Inventario SET cantidad_actual = cantidad_actual + ? WHERE producto_id = ? AND almacen_id = ?";

        try (PreparedStatement psCompra = connection.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psDetalle = connection.prepareStatement(sqlDetalle);
             PreparedStatement psUpdate = connection.prepareStatement(sqlUpdateStock)) {

            connection.setAutoCommit(false);

            // Insertar compra
            psCompra.setInt(1, compra.getProveedor_id());
            psCompra.setDate(2, new java.sql.Date(compra.getFecha().getTime()));
            psCompra.executeUpdate();

            ResultSet rs = psCompra.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("No se pudo obtener el ID de la compra");
            int compraId = rs.getInt(1);

            // Insertar detalles y actualizar inventario
            for (CompraProducto detalle : detalles) {
                psDetalle.setInt(1, compraId);
                psDetalle.setInt(2, detalle.getProducto_id());
                psDetalle.setInt(3, detalle.getCantidad());
                psDetalle.addBatch();

                // Actualizar stock (asumimos almacen_id = 1, puedes mejorar esto)
                psUpdate.setInt(1, detalle.getCantidad());
                psUpdate.setInt(2, detalle.getProducto_id());
                psUpdate.setInt(3, 1); // ← Asume almacen_id = 1. ¡Mejorable!
                psUpdate.addBatch();
            }

            psDetalle.executeBatch();
            psUpdate.executeBatch();

            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Compra> listarCompras() {
        List<Compra> lista = new ArrayList<>();
        String sql = """
            SELECT c.id_compra, c.proveedor_id, c.fecha, p.nombre AS nombreProveedor
            FROM Compra c
            JOIN Proveedor p ON c.proveedor_id = p.id_proveedor
            ORDER BY c.fecha DESC
            """;

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Compra c = new Compra();
                c.setId_compra(rs.getInt("id_compra"));
                c.setProveedor_id(rs.getInt("proveedor_id"));
                c.setFecha(rs.getDate("fecha"));
                c.setNombreProveedor(rs.getString("nombreProveedor"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<CompraProducto> obtenerDetallesCompra(int compraId) {
        List<CompraProducto> detalles = new ArrayList<>();
        String sql = """
            SELECT cp.compra_id, cp.producto_id, cp.cantidad, p.nombre AS producto_nombre
            FROM Compra_Producto cp
            JOIN Producto p ON cp.producto_id = p.id_producto
            WHERE cp.compra_id = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, compraId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CompraProducto dp = new CompraProducto();
                dp.setCompra_id(rs.getInt("compra_id"));
                dp.setProducto_id(rs.getInt("producto_id"));
                dp.setCantidad(rs.getInt("cantidad"));
                detalles.add(dp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }
    public List<Compra> listarComprasPorProveedor(int idProveedor) {
       List<Compra> lista = new ArrayList<>();
       String sql = """
           SELECT c.id_compra, c.proveedor_id, c.fecha, p.nombre AS nombreProveedor
           FROM Compra c
           JOIN Proveedor p ON c.proveedor_id = p.id_proveedor
           WHERE c.proveedor_id = ?
           ORDER BY c.fecha DESC
           """;
       try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
           ps.setInt(1, idProveedor);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               Compra c = new Compra();
               c.setId_compra(rs.getInt("id_compra"));
               c.setProveedor_id(rs.getInt("proveedor_id"));
               c.setFecha(rs.getDate("fecha"));
               c.setNombreProveedor(rs.getString("nombreProveedor"));
               lista.add(c);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return lista;
   }

   public List<CompraProducto> listarDetallesCompra(int idCompra) {
       List<CompraProducto> detalles = new ArrayList<>();
       String sql = """
           SELECT cp.compra_id, cp.producto_id, cp.cantidad
           FROM Compra_Producto cp
           WHERE cp.compra_id = ?
           """;
       try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
           ps.setInt(1, idCompra);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               CompraProducto cp = new CompraProducto();
               cp.setCompra_id(rs.getInt("compra_id"));
               cp.setProducto_id(rs.getInt("producto_id"));
               cp.setCantidad(rs.getInt("cantidad"));
               detalles.add(cp);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return detalles;
   }

   public boolean actualizarCompra(int idCompra, int idProveedor, List<CompraProducto> detalles) {
        String sqlUpdateCompra = "UPDATE Compra SET proveedor_id = ?, fecha = ? WHERE id_compra = ?";
        String sqlDeleteDetalles = "DELETE FROM Compra_Producto WHERE compra_id = ?";
        String sqlInsertDetalle = "INSERT INTO Compra_Producto (compra_id, producto_id, cantidad) VALUES (?, ?, ?)";
        String sqlUpdateStock = """
            UPDATE Inventario
            SET cantidad_actual = cantidad_actual + ?
            WHERE producto_id = ? AND almacen_id = 1
            """; // Asume almacen_id = 1 (ajusta según tu lógica)

        Connection con = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            // 1. Actualizar datos de la compra (fecha y proveedor)
            try (PreparedStatement psUpdateCompra = con.prepareStatement(sqlUpdateCompra)) {
                psUpdateCompra.setInt(1, idProveedor);
                psUpdateCompra.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                psUpdateCompra.setInt(3, idCompra);
                psUpdateCompra.executeUpdate();
            }

            // 2. Eliminar detalles antiguos de la compra
            try (PreparedStatement psDeleteDetalles = con.prepareStatement(sqlDeleteDetalles)) {
                psDeleteDetalles.setInt(1, idCompra);
                psDeleteDetalles.executeUpdate();
            }

            // 3. Insertar nuevos detalles y actualizar stock
            try (PreparedStatement psInsertDetalle = con.prepareStatement(sqlInsertDetalle);
                 PreparedStatement psUpdateStock = con.prepareStatement(sqlUpdateStock)) {

                for (CompraProducto detalle : detalles) {
                    // Insertar detalle de compra
                    psInsertDetalle.setInt(1, idCompra);
                    psInsertDetalle.setInt(2, detalle.getProducto_id());
                    psInsertDetalle.setInt(3, detalle.getCantidad());
                    psInsertDetalle.addBatch();

                    // Actualizar stock en inventario
                    psUpdateStock.setInt(1, detalle.getCantidad());
                    psUpdateStock.setInt(2, detalle.getProducto_id());
                    psUpdateStock.addBatch();
                }

                psInsertDetalle.executeBatch();
                psUpdateStock.executeBatch();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close(); // Cerrar la conexión
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
