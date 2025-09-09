import java.awt.*;
import javax.swing.*;

public class Login extends JPanel {

    public JPanel back;
    public JTextField correoField;
    public JPasswordField contrasenaField;
    public JLabel loginJLabel, olvideContJLabel, nuevoUserJLabel;
    public Button loginButton;

    public Login(int x, int y) {
        setPreferredSize(new Dimension(x, y));
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Panel contenedor con esquinas redondeadas
        back = new RoundedPanel(x / 2, y / 2, 20, new Color(255, 255, 255, 200));
        back.setLayout(new GridBagLayout());

        // Layout manager
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        loginJLabel = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        loginJLabel.setFont(new Font("Montserrat", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        back.add(loginJLabel, gbc);

        // Correo
        correoField = new JTextField("Correo Electronico", 20);
        correoField.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        back.add(correoField, gbc);

        // Contraseña
        contrasenaField = new JPasswordField("Contraseña", 20);
        contrasenaField.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        back.add(contrasenaField, gbc);

        // Botón
        loginButton = new Button("Ingresar");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        back.add(loginButton, gbc);

        olvideContJLabel = new JLabel("Olvide mi contraseña");
        olvideContJLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 4;
        back.add(olvideContJLabel, gbc);

        nuevoUserJLabel = new JLabel("No tengo cuenta");
        nuevoUserJLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 5;
        back.add(nuevoUserJLabel, gbc);

        // Añadir al panel principal
        add(back);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        // Gradiente del fondo
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(175, 64, 255),
                getWidth(), getHeight(), new Color(0, 221, 235)
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
    }

    // Clase para panel con esquinas redondeadas
    class RoundedPanel extends JPanel {
        private int arcWidth;
        private int arcHeight;
        private Color backgroundColor;

        public RoundedPanel(int width, int height, int arc, Color bgColor) {
            super();
            setPreferredSize(new Dimension(width, height));
            arcWidth = arc;
            arcHeight = arc;
            backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            g2.dispose();
        }
    }

    // Método para probar el Login en un JFrame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new Login(1000, 800));
            frame.setVisible(true);
        });
    }
}
