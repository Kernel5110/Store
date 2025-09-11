package ConexionMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    // We use 'static final' for constants to be accessible without creating an instance.
    private static final String URL = "jdbc:mysql://localhost:3306/MISCELANEA";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "12345";
    
    /**
     * Obtains a new database connection.
     * This method is static so it can be called directly from other classes.
     * @return a new Connection object or null if the connection fails.
     */
    public static Connection getConnection() {
        Connection conexion = null;
        try {
            // No need to explicitly load the driver for modern JDBC versions (Java 6+).
            // DriverManager will automatically find the driver.
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión exitosa a la base de datos MySQL.");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }
    
    /**
     * Closes the database connection.
     * This method is static to promote a clean and reusable API.
     * @param conexion The Connection object to be closed.
     */
    public static void closeConnection(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        // Test the connection
        Connection conn = ConexionMySQL.getConnection();
        System.out.println("conexion creada");
        if (conn != null) {
            // Do something with the connection if needed
            ConexionMySQL.closeConnection(conn);
        }else{
            System.out.println("No se pudo establecer la conexión.");
        }
    }
}