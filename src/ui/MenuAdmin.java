package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ui.*;

public class MenuAdmin extends JFrame {

    public MenuAdmin() {
        setTitle("Panel de Administración");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla

        // Usar un panel principal y un layout de cuadrícula para los botones
        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15)); // 3 filas, 2 columnas, con espaciado
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Añadir márgenes

        // --- Crear los botones del menú ---
        JButton btnGestionarUsuarios = new JButton("Gestionar Usuarios");
        JButton btnGestionarProductos = new JButton("Gestionar Productos");
        JButton btnGestionarProveedores = new JButton("Gestionar Proveedores");
        JButton btnGestionarAlmacen = new JButton("Gestionar Almacén");
        JButton btnGestionarCompras = new JButton("Gestionar Compras");
        // Puedes agregar más botones aquí si es necesario

        // --- Asignar acciones a cada botón ---
        btnGestionarUsuarios.addActionListener(e -> {
                new usuario().setVisible(true);
                dispose();}
                 ); // Asumiendo que 'usuario' es un JFrame
        btnGestionarProductos.addActionListener(e ->{
            new ProductoF().setVisible(true);
                dispose();
                });
        btnGestionarProveedores.addActionListener(e -> { 
            new ProveedorF().setVisible(true);
                dispose();
                }
        );
        btnGestionarAlmacen.addActionListener(e -> {
            new AlmacenF().setVisible(true);
            dispose();
                });
        btnGestionarCompras.addActionListener(e -> {
            new CompraF().setVisible(true);
            dispose();
                });

        // --- Añadir botones al panel ---
        panel.add(btnGestionarUsuarios);
        panel.add(btnGestionarProductos);
        panel.add(btnGestionarProveedores);
        panel.add(btnGestionarAlmacen);
        panel.add(btnGestionarCompras);

        // Añadir el panel al frame
        add(panel);
    }

    // Método main para probar esta ventana de forma aislada (opcional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuAdmin().setVisible(true));
    }
}