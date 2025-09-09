package ConexionMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/MISCELANEA";
        String usuario = "root";
        String contrasena = "12345";   

        try {
            // Cargar el driver explícitamente (opcional en versiones recientes de JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, usuario, contrasena)) {
                System.out.println("Conexión exitosa a la base de datos MISCELANEA");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
    }
}