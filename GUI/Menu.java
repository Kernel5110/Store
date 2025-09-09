import java.awt.*;
import javax.swing.*;

public class Menu extends JPanel {
    public JPanel back;
    public Button[] botones;
    public JLabel iconLabel;

    public Menu(int ancho, int alto) {
        // El panel ocupará 1/5 del ancho del frame
        setPreferredSize(new Dimension(ancho / 5, alto));
        setLayout(new BorderLayout());

        // Panel contenedor principal
        back = new JPanel();
        back.setLayout(new GridBagLayout());
        back.setBackground(Color.WHITE);

        // Layout manager
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 1;

        // Icono arriba (logo del menú)
        ImageIcon logo = new ImageIcon(new ImageIcon("logo.png").getImage()
                .getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        iconLabel = new JLabel(logo);
        gbc.gridy = 0;
        back.add(iconLabel, gbc);

        // Crear arreglo de botones con iconos
        String[] nombres = {"Inicio", "Perfil", "Configuración", "Salir"};
        String[] iconos = {"home.png", "user.png", "settings.png", "exit.png"};
        botones = new Button[nombres.length];

        for (int i = 0; i < nombres.length; i++) {
            // Cargar icono
            ImageIcon icono = new ImageIcon(new ImageIcon(iconos[i]).getImage()
                    .getScaledInstance(24, 24, Image.SCALE_SMOOTH));

            // Crear botón con texto + icono
            botones[i] = new Button(nombres[i], icono);
            botones[i].setPreferredSize(new Dimension(180, 70));
            botones[i].setFocusPainted(false);
            botones[i].setHorizontalAlignment(SwingConstants.LEFT); // Texto alineado a la izquierda
            botones[i].setIconTextGap(10); // Espacio entre icono y texto

            gbc.gridy = i + 1;
            back.add(botones[i], gbc);
        }

        add(back, BorderLayout.CENTER);
    }

    // Método de prueba
    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo Menu con Iconos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Panel de contenido para acompañar al menú
        JPanel contenido = new JPanel();
        contenido.setBackground(new Color(220, 220, 220));

        // Añadir menú (1/5 del ancho) y contenido (4/5 del ancho)
        frame.setLayout(new BorderLayout());
        frame.add(new Menu(800, 600), BorderLayout.WEST);
        frame.add(contenido, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
