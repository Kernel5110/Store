package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Define los datos de tu conexión
    private static final String URL = "jdbc:mysql://localhost:3306/miselanea?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // <-- Cambia a tu usuario de MySQL
    private static final String PASSWORD = ""; // <-- Cambia a tu contraseña

    // Método estático para obtener la conexión
    public static Connection getConexion() {
        Connection con = null;
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver no encontrado. " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            // Imprimimos el mensaje específico que viste: "Public Key Retrieval is not allowed"
            System.out.println("Error SQL: " + e.getMessage());
        }
        return con;
    }

    // Puedes agregar un método main para probar la conexión directamente
    public static void main(String[] args) {
        Connection con = getConexion();
        if (con != null) {
            try {
                con.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}