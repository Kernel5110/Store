import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

public class NewUser extends JPanel {
    public JPanel back;
    public JTextField correoField, nombreJField, apellidoPaternoField,
                      apellidoMaternoField;
    public JPasswordField passwordField;
    public JLabel loginJLabel;
    public Button loginButton;

    public NewUser(int x, int y) {
        setPreferredSize(new Dimension(x, y));

        // Panel contenedor
        back = new JPanel();
        back.setLayout(new GridBagLayout()); 
        back.setPreferredSize(new Dimension(x / 2, y / 2));
        back.setBackground(Color.WHITE);

        // Layout manager
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        loginJLabel = new JLabel("Crear Usuario", SwingConstants.CENTER);
        loginJLabel.setFont(new Font("Montserrat", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        back.add(loginJLabel, gbc);

        // Nombre
        nombreJField = new JTextField("Nombre", 20);
        nombreJField.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        back.add(nombreJField, gbc);

        // Apellido Paterno
        apellidoPaternoField = new JTextField("Apellido Paterno", 20);
        apellidoPaternoField.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        back.add(apellidoPaternoField, gbc);

        // Apellido Materno (Corregido)
        apellidoMaternoField = new JTextField("Apellido Materno", 20);
        apellidoMaternoField.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 3;
        back.add(apellidoMaternoField, gbc);

        // Correo
        correoField = new JTextField("Correo Electronico", 20);
        correoField.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 5;
        back.add(correoField, gbc);

        // Contrasena
        passwordField = new JPasswordField("Contrasena", 20);
        passwordField.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 6;
        back.add(passwordField, gbc);

        // Botón
        loginButton = new Button("Ingresar");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        back.add(loginButton, gbc);

        // Añadir al panel principal
        setLayout(new GridBagLayout());
        add(back);
        
        // Agregar los FocusListeners
        addFocusListeners();
    }
    
    // Método para agregar los FocusListeners a todos los campos
    private void addFocusListeners() {
        // Campos de texto
        JTextField[] textFields = {
            nombreJField, apellidoPaternoField, apellidoMaternoField, correoField
        };

        for (JTextField field : textFields) {
            field.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    field.setText("");
                }

                @Override
                public void focusLost(FocusEvent e) {
                    // No hacer nada
                }
            });
        }

        // Campo de contraseña
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Para JPasswordField, use setText para borrar el texto
                passwordField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                // No hacer nada
            }
        });
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
        JFrame frame = new JFrame("Login Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new NewUser(1000, 800));
        frame.setVisible(true);
    }
}