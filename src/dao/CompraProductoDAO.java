/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.CompraProducto;
import db.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraProductoDAO {

    // Método para obtener conexión
    private Connection getConnection() throws SQLException {
        return ConexionBD.getConexion();
    }

    /**
     * Inserta un registro en la tabla Compra_Producto
     * @param cp Objeto CompraProducto con los datos a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertarCompraProducto(CompraProducto cp) {
        String sql = "INSERT INTO Compra_Producto (compra_id, producto_id, cantidad) VALUES (?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cp.getCompra_id());
            ps.setInt(2, cp.getProducto_id());
            ps.setInt(3, cp.getCantidad());

            int result = ps.executeUpdate();

            // Actualizar el inventario después de la compra
            if (result > 0) {
                actualizarInventario(cp.getProducto_id(), cp.getCantidad());
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("Error al insertar en Compra_Producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lista todos los productos asociados a una compra específica
     * @param compraId ID de la compra
     * @return Lista de CompraProducto
     */
    public List<CompraProducto> listarPorCompra(int compraId) {
        List<CompraProducto> lista = new ArrayList<>();
        String sql = "SELECT compra_id, producto_id, cantidad FROM Compra_Producto WHERE compra_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, compraId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CompraProducto cp = new CompraProducto(
                        rs.getInt("compra_id"),
                        rs.getInt("producto_id"),
                        rs.getInt("cantidad")
                    );
                    lista.add(cp);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos de compra: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Actualiza la cantidad de un producto en una compra
     * @param cp Objeto CompraProducto con los nuevos datos
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarCantidad(CompraProducto cp) {
        // Primero obtenemos la cantidad anterior para ajustar el inventario
        int cantidadAnterior = getCantidadAnterior(cp.getCompra_id(), cp.getProducto_id());
        if (cantidadAnterior == -1) return false;

        String sql = "UPDATE Compra_Producto SET cantidad = ? WHERE compra_id = ? AND producto_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cp.getCantidad());
            ps.setInt(2, cp.getCompra_id());
            ps.setInt(3, cp.getProducto_id());

            int result = ps.executeUpdate();

            if (result > 0) {
                // Ajustar el inventario según la diferencia
                int diferencia = cp.getCantidad() - cantidadAnterior;
                actualizarInventario(cp.getProducto_id(), diferencia);
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("Error al actualizar cantidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un producto de una compra
     * @param compraId ID de la compra
     * @param productoId ID del producto
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminarCompraProducto(int compraId, int productoId) {
        // Primero obtenemos la cantidad para ajustar el inventario
        int cantidad = getCantidadAnterior(compraId, productoId);
        if (cantidad == -1) return false;

        String sql = "DELETE FROM Compra_Producto WHERE compra_id = ? AND producto_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, compraId);
            ps.setInt(2, productoId);

            int result = ps.executeUpdate();

            if (result > 0) {
                // Reducir el inventario
                actualizarInventario(productoId, -cantidad);
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto de compra: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la cantidad anterior de un producto en una compra
     */
    private int getCantidadAnterior(int compraId, int productoId) {
        String sql = "SELECT cantidad FROM Compra_Producto WHERE compra_id = ? AND producto_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, compraId);
            ps.setInt(2, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener cantidad anterior: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Actualiza el inventario después de una compra o modificación
     * @param productoId ID del producto
     * @param cantidad Cantidad a agregar (puede ser negativa para restar)
     */
    private void actualizarInventario(int productoId, int cantidad) {
        String sql = "UPDATE Inventario SET cantidad_actual = cantidad_actual + ? WHERE producto_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, productoId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar inventario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica si un producto existe en una compra
     * @param compraId ID de la compra
     * @param productoId ID del producto
     * @return true si existe
     */
    public boolean existeEnCompra(int compraId, int productoId) {
        String sql = "SELECT COUNT(*) FROM Compra_Producto WHERE compra_id = ? AND producto_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, compraId);
            ps.setInt(2, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar existencia: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtiene todos los productos comprados a un proveedor específico
     * @param proveedorId ID del proveedor
     * @return Lista de CompraProducto
     */
    public List<CompraProducto> listarPorProveedor(int proveedorId) {
        List<CompraProducto> lista = new ArrayList<>();
        String sql = "SELECT cp.compra_id, cp.producto_id, cp.cantidad " +
                     "FROM Compra_Producto cp " +
                     "JOIN Compra c ON cp.compra_id = c.id_compra " +
                     "WHERE c.proveedor_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, proveedorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CompraProducto cp = new CompraProducto(
                        rs.getInt("compra_id"),
                        rs.getInt("producto_id"),
                        rs.getInt("cantidad")
                    );
                    lista.add(cp);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar por proveedor: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene el total de unidades compradas de un producto específico
     * @param productoId ID del producto
     * @return Total de unidades compradas
     */
    public int getTotalCompradoPorProducto(int productoId) {
        String sql = "SELECT SUM(cantidad) AS total FROM Compra_Producto WHERE producto_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener total comprado: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
