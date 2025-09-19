/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.NoPerecedero;
import db.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoPerecederoDAO {

    // Método para obtener conexión
    private Connection getConnection() throws SQLException {
        return ConexionBD.getConexion();
    }

    /**
     * Inserta un nuevo producto no perecedero en la base de datos
     * @param np Objeto NoPerecedero a insertar
     * @return true si la inserción fue exitosa
     */
    public boolean insertarNoPerecedero(NoPerecedero np) {
        // Primero insertamos en la tabla Producto
        String sqlProducto = "INSERT INTO Producto (nombre, tipo, precio) VALUES (?, 'no_perecedero', ?)";

        // Luego insertamos en la tabla NoPerecedero
        String sqlNoPerecedero = "INSERT INTO NoPerecedero (fecha_compra, producto_id) VALUES (?, ?)";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement psProducto = con.prepareStatement(sqlProducto, Statement.RETURN_GENERATED_KEYS)) {
                psProducto.setString(1, np.getNombre());
                psProducto.setDouble(2, np.getPrecio());

                int afectados = psProducto.executeUpdate();

                if (afectados == 0) {
                    con.rollback();
                    return false;
                }

                // Obtener el ID generado para el producto
                int productoId;
                try (ResultSet generatedKeys = psProducto.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productoId = generatedKeys.getInt(1);
                    } else {
                        con.rollback();
                        return false;
                    }
                }

                // Insertar en NoPerecedero
                try (PreparedStatement psNoPerecedero = con.prepareStatement(sqlNoPerecedero)) {
                    psNoPerecedero.setString(1, np.getFechaEntrada());
                    psNoPerecedero.setInt(2, productoId);

                    int afectadosNP = psNoPerecedero.executeUpdate();

                    if (afectadosNP == 0) {
                        con.rollback();
                        return false;
                    }
                }
            }

            con.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar producto no perecedero: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un producto no perecedero
     * @param np Objeto NoPerecedero con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarNoPerecedero(NoPerecedero np) {
        // Actualizar en Producto
        String sqlProducto = "UPDATE Producto SET nombre = ?, precio = ? WHERE id_producto = ?";

        // Actualizar en NoPerecedero
        String sqlNoPerecedero = "UPDATE NoPerecedero SET fecha_compra = ? WHERE producto_id = ?";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false); // Iniciar transacción

            // Actualizar Producto
            try (PreparedStatement psProducto = con.prepareStatement(sqlProducto)) {
                psProducto.setString(1, np.getNombre());
                psProducto.setDouble(2, np.getPrecio());
                psProducto.setInt(3, np.getIdProducto());

                int afectados = psProducto.executeUpdate();
                if (afectados == 0) {
                    con.rollback();
                    return false;
                }
            }

            // Actualizar NoPerecedero
            try (PreparedStatement psNoPerecedero = con.prepareStatement(sqlNoPerecedero)) {
                psNoPerecedero.setString(1, np.getFechaEntrada());
                psNoPerecedero.setInt(2, np.getIdProducto());

                int afectados = psNoPerecedero.executeUpdate();
                if (afectados == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto no perecedero: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un producto no perecedero
     * @param productoId ID del producto a eliminar
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminarNoPerecedero(int productoId) {
        // La eliminación en cascada está configurada en la BD
        String sql = "DELETE FROM Producto WHERE id_producto = ? AND tipo = 'no_perecedero'";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productoId);
            int afectados = ps.executeUpdate();
            return afectados > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto no perecedero: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene un producto no perecedero por su ID
     * @param productoId ID del producto
     * @return Objeto NoPerecedero o null si no existe
     */
    public NoPerecedero obtenerPorId(int productoId) {
        String sql = "SELECT p.id_producto, p.nombre, p.tipo, p.precio, np.fecha_compra " +
                     "FROM Producto p " +
                     "JOIN NoPerecedero np ON p.id_producto = np.producto_id " +
                     "WHERE p.id_producto = ? AND p.tipo = 'no_perecedero'";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NoPerecedero np = new NoPerecedero(
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getDouble("precio"),
                        rs.getString("fecha_compra")
                    );
                    // Establecer el ID del producto (que no está en el constructor)
                    np.setIdProducto(rs.getInt("id_producto"));
                    return np;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener producto no perecedero: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lista todos los productos no perecederos
     * @return Lista de productos no perecederos
     */
    public List<NoPerecedero> listarTodos() {
        List<NoPerecedero> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.tipo, p.precio, np.fecha_compra " +
                     "FROM Producto p " +
                     "JOIN NoPerecedero np ON p.id_producto = np.producto_id " +
                     "WHERE p.tipo = 'no_perecedero' " +
                     "ORDER BY p.nombre";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                NoPerecedero np = new NoPerecedero(
                    rs.getString("nombre"),
                    rs.getString("tipo"),
                    rs.getDouble("precio"),
                    rs.getString("fecha_compra")
                );
                np.setIdProducto(rs.getInt("id_producto"));
                lista.add(np);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos no perecederos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Lista productos no perecederos con stock bajo
     * @param stockMinimo Nivel mínimo de stock para considerar "bajo"
     * @return Lista de productos con stock bajo
     */
    public List<NoPerecedero> listarConStockBajo(int stockMinimo) {
        List<NoPerecedero> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.tipo, p.precio, np.fecha_compra, " +
                     "COALESCE(SUM(i.cantidad_actual), 0) AS stock_actual " +
                     "FROM Producto p " +
                     "JOIN NoPerecedero np ON p.id_producto = np.producto_id " +
                     "LEFT JOIN Inventario i ON p.id_producto = i.producto_id " +
                     "WHERE p.tipo = 'no_perecedero' " +
                     "GROUP BY p.id_producto, p.nombre, p.tipo, p.precio, np.fecha_compra " +
                     "HAVING stock_actual < ? " +
                     "ORDER BY stock_actual";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, stockMinimo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NoPerecedero np = new NoPerecedero(
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getDouble("precio"),
                        rs.getString("fecha_compra")
                    );
                    np.setIdProducto(rs.getInt("id_producto"));
                    lista.add(np);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos con stock bajo: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Busca productos no perecederos por nombre (búsqueda parcial)
     * @param nombre Parte del nombre a buscar
     * @return Lista de productos que coinciden con la búsqueda
     */
    public List<NoPerecedero> buscarPorNombre(String nombre) {
        List<NoPerecedero> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.tipo, p.precio, np.fecha_compra " +
                     "FROM Producto p " +
                     "JOIN NoPerecedero np ON p.id_producto = np.producto_id " +
                     "WHERE p.tipo = 'no_perecedero' AND p.nombre LIKE ? " +
                     "ORDER BY p.nombre";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NoPerecedero np = new NoPerecedero(
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getDouble("precio"),
                        rs.getString("fecha_compra")
                    );
                    np.setIdProducto(rs.getInt("id_producto"));
                    lista.add(np);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar productos por nombre: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene productos no perecederos comprados en un rango de fechas
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de productos comprados en el rango
     */
    public List<NoPerecedero> listarPorRangoFechas(String fechaInicio, String fechaFin) {
        List<NoPerecedero> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT p.id_producto, p.nombre, p.tipo, p.precio, np.fecha_compra " +
                     "FROM Producto p " +
                     "JOIN NoPerecedero np ON p.id_producto = np.producto_id " +
                     "JOIN Compra_Producto cp ON p.id_producto = cp.producto_id " +
                     "JOIN Compra c ON cp.compra_id = c.id_compra " +
                     "WHERE p.tipo = 'no_perecedero' AND c.fecha BETWEEN ? AND ? " +
                     "ORDER BY p.nombre";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NoPerecedero np = new NoPerecedero(
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getDouble("precio"),
                        rs.getString("fecha_compra")
                    );
                    np.setIdProducto(rs.getInt("id_producto"));
                    lista.add(np);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar por rango de fechas: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }
}
