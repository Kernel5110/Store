/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Perecedero;
import db.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerecederoDAO {

    // Método para obtener conexión
    private Connection getConnection() throws SQLException {
        return ConexionBD.getConexion();
    }

    /**
     * Inserta un nuevo producto perecedero en la base de datos
     * @param perecedero Objeto Perecedero a insertar
     * @return true si la inserción fue exitosa
     */
    public boolean insertarPerecedero(Perecedero perecedero) {
        // Primero insertamos en la tabla Producto
        String sqlProducto = "INSERT INTO Producto (nombre, tipo, precio) VALUES (?, 'perecedero', ?)";

        // Luego insertamos en la tabla Perecedero
        String sqlPerecedero = "INSERT INTO Perecedero (fecha_caducidad, producto_id) VALUES (?, ?)";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement psProducto = con.prepareStatement(sqlProducto, Statement.RETURN_GENERATED_KEYS)) {
                psProducto.setString(1, perecedero.getNombre());
                psProducto.setDouble(2, perecedero.getPrecio());

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

                // Insertar en Perecedero
                try (PreparedStatement psPerecedero = con.prepareStatement(sqlPerecedero)) {
                    psPerecedero.setString(1, perecedero.getCaducidad());
                    psPerecedero.setInt(2, productoId);

                    int afectadosP = psPerecedero.executeUpdate();

                    if (afectadosP == 0) {
                        con.rollback();
                        return false;
                    }
                }
            }

            con.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar producto perecedero: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un producto perecedero
     * @param perecedero Objeto Perecedero con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarPerecedero(Perecedero perecedero) {
        // Actualizar en Producto
        String sqlProducto = "UPDATE Producto SET nombre = ?, precio = ? WHERE id_producto = ?";

        // Actualizar en Perecedero
        String sqlPerecedero = "UPDATE Perecedero SET fecha_caducidad = ? WHERE producto_id = ?";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false); // Iniciar transacción

            // Actualizar Producto
            try (PreparedStatement psProducto = con.prepareStatement(sqlProducto)) {
                psProducto.setString(1, perecedero.getNombre());
                psProducto.setDouble(2, perecedero.getPrecio());
                psProducto.setInt(3, perecedero.getIdProducto());

                int afectados = psProducto.executeUpdate();
                if (afectados == 0) {
                    con.rollback();
                    return false;
                }
            }

            // Actualizar Perecedero
            try (PreparedStatement psPerecedero = con.prepareStatement(sqlPerecedero)) {
                psPerecedero.setString(1, perecedero.getCaducidad());
                psPerecedero.setInt(2, perecedero.getIdProducto());

                int afectados = psPerecedero.executeUpdate();
                if (afectados == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto perecedero: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un producto perecedero
     * @param productoId ID del producto a eliminar
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminarPerecedero(int productoId) {
        // La eliminación en cascada está configurada en la BD
        String sql = "DELETE FROM Producto WHERE id_producto = ? AND tipo = 'perecedero'";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productoId);
            int afectados = ps.executeUpdate();
            return afectados > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto perecedero: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene un producto perecedero por su ID
     * @param productoId ID del producto
     * @return Objeto Perecedero o null si no existe
     */
    public Perecedero obtenerPorId(int productoId) {
        String sql = "SELECT p.id_producto, p.nombre, p.precio, pe.fecha_caducidad " +
                     "FROM Producto p " +
                     "JOIN Perecedero pe ON p.id_producto = pe.producto_id " +
                     "WHERE p.id_producto = ? AND p.tipo = 'perecedero'";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Perecedero perecedero = new Perecedero(
                        rs.getString("nombre"),
                        "", // Descripción no está en el schema, se pasa vacío
                        rs.getDouble("precio"),
                        rs.getString("fecha_caducidad")
                    );
                    // Establecer el ID del producto
                    perecedero.setIdProducto(rs.getInt("id_producto"));
                    return perecedero;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener producto perecedero: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lista todos los productos perecederos
     * @return Lista de productos perecederos
     */
    public List<Perecedero> listarTodos() {
        List<Perecedero> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, pe.fecha_caducidad " +
                     "FROM Producto p " +
                     "JOIN Perecedero pe ON p.id_producto = pe.producto_id " +
                     "WHERE p.tipo = 'perecedero' " +
                     "ORDER BY pe.fecha_caducidad";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Perecedero perecedero = new Perecedero(
                    rs.getString("nombre"),
                    "", // Descripción no está en el schema
                    rs.getDouble("precio"),
                    rs.getString("fecha_caducidad")
                );
                perecedero.setIdProducto(rs.getInt("id_producto"));
                lista.add(perecedero);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos perecederos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Lista productos perecederos próximos a caducar
     * @param diasLimite Número de días para considerar "próximo a caducar"
     * @return Lista de productos próximos a caducar
     */
    public List<Perecedero> listarProximosACaducar(int diasLimite) {
        List<Perecedero> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, pe.fecha_caducidad, " +
                     "DATEDIFF(pe.fecha_caducidad, CURDATE()) AS dias_restantes " +
                     "FROM Producto p " +
                     "JOIN Perecedero pe ON p.id_producto = pe.producto_id " +
                     "WHERE p.tipo = 'perecedero' AND " +
                     "DATEDIFF(pe.fecha_caducidad, CURDATE()) BETWEEN 0 AND ? " +
                     "ORDER BY dias_restantes";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, diasLimite);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Perecedero perecedero = new Perecedero(
                        rs.getString("nombre"),
                        "", // Descripción no está en el schema
                        rs.getDouble("precio"),
                        rs.getString("fecha_caducidad")
                    );
                    perecedero.setIdProducto(rs.getInt("id_producto"));
                    lista.add(perecedero);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos próximos a caducar: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Lista productos perecederos ya caducados
     * @return Lista de productos caducados
     */
    public List<Perecedero> listarCaducados() {
        List<Perecedero> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, pe.fecha_caducidad, " +
                     "DATEDIFF(CURDATE(), pe.fecha_caducidad) AS dias_caducado " +
                     "FROM Producto p " +
                     "JOIN Perecedero pe ON p.id_producto = pe.producto_id " +
                     "WHERE p.tipo = 'perecedero' AND pe.fecha_caducidad < CURDATE() " +
                     "ORDER BY dias_caducado DESC";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Perecedero perecedero = new Perecedero(
                    rs.getString("nombre"),
                    "", // Descripción no está en el schema
                    rs.getDouble("precio"),
                    rs.getString("fecha_caducidad")
                );
                perecedero.setIdProducto(rs.getInt("id_producto"));
                lista.add(perecedero);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos caducados: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Busca productos perecederos por nombre (búsqueda parcial)
     * @param nombre Parte del nombre a buscar
     * @return Lista de productos que coinciden con la búsqueda
     */
    public List<Perecedero> buscarPorNombre(String nombre) {
        List<Perecedero> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, pe.fecha_caducidad " +
                     "FROM Producto p " +
                     "JOIN Perecedero pe ON p.id_producto = pe.producto_id " +
                     "WHERE p.tipo = 'perecedero' AND p.nombre LIKE ? " +
                     "ORDER BY pe.fecha_caducidad";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Perecedero perecedero = new Perecedero(
                        rs.getString("nombre"),
                        "", // Descripción no está en el schema
                        rs.getDouble("precio"),
                        rs.getString("fecha_caducidad")
                    );
                    perecedero.setIdProducto(rs.getInt("id_producto"));
                    lista.add(perecedero);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar productos por nombre: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene productos perecederos con stock bajo
     * @param stockMinimo Nivel mínimo de stock para considerar "bajo"
     * @return Lista de productos con stock bajo
     */
    public List<Perecedero> listarConStockBajo(int stockMinimo) {
        List<Perecedero> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, pe.fecha_caducidad, " +
                     "COALESCE(SUM(i.cantidad_actual), 0) AS stock_actual " +
                     "FROM Producto p " +
                     "JOIN Perecedero pe ON p.id_producto = pe.producto_id " +
                     "LEFT JOIN Inventario i ON p.id_producto = i.producto_id " +
                     "WHERE p.tipo = 'perecedero' " +
                     "GROUP BY p.id_producto, p.nombre, p.precio, pe.fecha_caducidad " +
                     "HAVING stock_actual < ? " +
                     "ORDER BY pe.fecha_caducidad, stock_actual";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, stockMinimo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Perecedero perecedero = new Perecedero(
                        rs.getString("nombre"),
                        "", // Descripción no está en el schema
                        rs.getDouble("precio"),
                        rs.getString("fecha_caducidad")
                    );
                    perecedero.setIdProducto(rs.getInt("id_producto"));
                    lista.add(perecedero);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos con stock bajo: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene productos perecederos ordenados por fecha de caducidad
     * @param orden "ASC" para más recientes primero, "DESC" para más antiguos primero
     * @return Lista de productos ordenados
     */
    public List<Perecedero> listarOrdenadosPorCaducidad(String orden) {
        List<Perecedero> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, pe.fecha_caducidad " +
                     "FROM Producto p " +
                     "JOIN Perecedero pe ON p.id_producto = pe.producto_id " +
                     "WHERE p.tipo = 'perecedero' " +
                     "ORDER BY pe.fecha_caducidad " + orden;

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Perecedero perecedero = new Perecedero(
                    rs.getString("nombre"),
                    "", // Descripción no está en el schema
                    rs.getDouble("precio"),
                    rs.getString("fecha_caducidad")
                );
                perecedero.setIdProducto(rs.getInt("id_producto"));
                lista.add(perecedero);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos ordenados por caducidad: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }
}
