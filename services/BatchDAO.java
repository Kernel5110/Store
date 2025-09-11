package services;

import ConexionMySQL.ConexionMySQL;
import Objects.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class BatchDAO {

    /**
     * Inserta un nuevo lote en la base de datos.
     * @param idAlmacen El ID del almacén al que pertenece el lote.
     * @return El ID del lote insertado, o -1 si falló.
     */
    public int insertarLote(int idAlmacen) {
        String query = "INSERT INTO Lote (id_almacen) VALUES (?)";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idAlmacen);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar lote: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Obtiene un lote por su ID.
     * @param idLote El ID del lote a buscar.
     * @return El ID del almacén asociado al lote, o -1 si no existe.
     */
    public int obtenerAlmacenPorLote(int idLote) {
        String query = "SELECT id_almacen FROM Lote WHERE id = ?";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idLote);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_almacen");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener lote por ID: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Obtiene todos los lotes de un almacén.
     * @param idAlmacen El ID del almacén.
     * @return Un Vector con los IDs de los lotes.
     */
    public Vector<Integer> obtenerLotesPorAlmacen(int idAlmacen) {
        Vector<Integer> lotes = new Vector<>();
        String query = "SELECT id FROM Lote WHERE id_almacen = ?";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idAlmacen);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lotes.add(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener lotes por almacén: " + e.getMessage());
        }
        return lotes;
    }

    /**
     * Elimina un lote de la base de datos por su ID.
     * @param idLote El ID del lote a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarLote(int idLote) {
        String query = "DELETE FROM Lote WHERE id = ?";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idLote);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lote: " + e.getMessage());
            return false;
        }
    }

    // --- Métodos para manejar productos perecederos en un lote ---
    // (Asumiendo que los productos perecederos se asocian a un lote mediante su id_producto)

    /**
     * Asocia un producto perecedero a un lote.
     * @param idLote El ID del lote.
     * @param idProducto El ID del producto perecedero.
     * @param fechaCaducidad La fecha de caducidad del producto.
     * @return true si la asociación fue exitosa, false en caso contrario.
     */
    public boolean agregarProductoPerecederoALote(int idLote, int idProducto, String fechaCaducidad) {
        String query = "INSERT INTO Perecedero (id_producto, fecha_caducidad) VALUES (?, ?)";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idProducto);
            stmt.setString(2, fechaCaducidad);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar producto perecedero al lote: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los productos perecederos asociados a un lote.
     * @param idLote El ID del lote.
     * @return Un Vector con los productos perecederos del lote.
     */
    public Vector<Product> obtenerProductosPerecederosPorLote(int idLote) {
        Vector<Product> productos = new Vector<>();
        String query = "SELECT p.id, p.nombre, pe.fecha_caducidad " +
                       "FROM Producto p " +
                       "JOIN Perecedero pe ON p.id = pe.id_producto " +
                       "WHERE p.id IN (SELECT id_producto FROM Perecedero WHERE id_producto IN " +
                       "(SELECT id_producto FROM LoteProducto WHERE id_lote = ?))";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idLote);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    
                    // Puedes agregar la fecha de caducidad como un campo adicional en tu clase Product si es necesario
                    
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos perecederos del lote: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Actualiza la fecha de caducidad de un producto perecedero en un lote.
     * @param idProducto El ID del producto perecedero.
     * @param nuevaFechaCaducidad La nueva fecha de caducidad.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarFechaCaducidadProductoPerecedero(int idProducto, String nuevaFechaCaducidad) {
        String query = "UPDATE Perecedero SET fecha_caducidad = ? WHERE id_producto = ?";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuevaFechaCaducidad);
            stmt.setInt(2, idProducto);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar fecha de caducidad: " + e.getMessage());
            return false;
        }
    }
}
