package ConexionMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class ConexionMySQL {
    // Datos de conexión a MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/MISCELANEA";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "12345";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión exitosa a la base de datos MySQL.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }

    public static void main(String[] args) {
        Connection conexion = conectar();
        if (conexion != null) {
            try {
                // Ejemplo: Consulta simple para listar tablas (opcional)
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery("SHOW TABLES");
                System.out.println("Tablas en la base de datos MISCELANEA:");
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
                rs.close();
                stmt.close();
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
