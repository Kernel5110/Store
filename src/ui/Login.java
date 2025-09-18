package ui;

import dao.UsuarioDAO; // Importar la clase DAO
import modelo.Usuario; // Importar el modelo Usuario
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter; // Usaremos FocusAdapter para simplificar
import java.awt.event.FocusEvent;

public class Login extends JPanel {

    public JPanel back;
    public JTextField correoField;
    public JPasswordField contrasenaField;
    public JLabel loginJLabel, olvideContJLabel, nuevoUserJLabel;
    public JButton loginButton;

    public Login(int x, int y) {
        setPreferredSize(new Dimension(x, y));
        setLayout(new GridBagLayout());
        setOpaque(false);

        back = new RoundedPanel(x / 2, y / 2, 20, new Color(255, 255, 255, 200));
        back.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        loginJLabel = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        loginJLabel.setFont(new Font("Montserrat", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        back.add(loginJLabel, gbc);

        // --- CAMPO CORREO CON FOCUS LISTENER ---
        correoField = new JTextField("Correo Electronico", 20);
        correoField.setFont(new Font("Arial", Font.BOLD, 20));
        addPlaceholderFocus(correoField, "Correo Electronico"); // Llama al método para añadir el listener
        gbc.gridx = 0;
        gbc.gridy = 1;
        back.add(correoField, gbc);

        // --- CAMPO CONTRASEÑA (TELÉFONO) CON FOCUS LISTENER ---
        contrasenaField = new JPasswordField("Contraseña", 20);
        contrasenaField.setEchoChar((char) 0); // Hace visible el texto del placeholder
        contrasenaField.setFont(new Font("Arial", Font.BOLD, 20));
        addPlaceholderFocus(contrasenaField, "Contraseña"); // Llama al método para añadir el listener
        gbc.gridx = 0;
        gbc.gridy = 2;
        back.add(contrasenaField, gbc);

        loginButton = new JButton("Ingresar");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        back.add(loginButton, gbc);

        // --- ACTION LISTENER ACTUALIZADO ---
        // Dentro de la clase Login.java, en el constructor...


loginButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String email = correoField.getText();
        String telefono = new String(contrasenaField.getPassword());

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioAutenticado = usuarioDAO.autenticarUsuario(email, telefono);

        if (usuarioAutenticado != null) {
            // El rol ahora viene directamente en el objeto Usuario
            String rol = usuarioAutenticado.getRol();

            // Cerramos la ventana de login
            JFrame frameActual = (JFrame) SwingUtilities.getWindowAncestor(Login.this);
            frameActual.dispose();

            // Redirigimos usando un switch con los roles del tipo String
            switch (rol) {
                case "ADMINISTRADOR":
                    new MenuAdmin().setVisible(true);
                    break;
                case "PROVEEDOR":
                    new ProveedorF().setVisible(true);
                    break;
                case "VENDEDOR":
                    new CompraF().setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Rol no reconocido: " + rol, "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                    break;
            }

        } else {
            JOptionPane.showMessageDialog(back, "Correo o contraseña (teléfono) incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
});
        // ... resto de tu código sin cambios ...
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

        add(back);
    }
    
    /**
     * Método para agregar el efecto de placeholder a los campos de texto y contraseña.
     */
    private void addPlaceholderFocus(JComponent component, String placeholder) {
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (component instanceof JTextField) {
                    JTextField field = (JTextField) component;
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        // Si es el campo de contraseña, oculta el texto al escribir
                        if (field instanceof JPasswordField) {
                            ((JPasswordField) field).setEchoChar('•');
                        }
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                 if (component instanceof JTextField) {
                    JTextField field = (JTextField) component;
                    if (field.getText().isEmpty()) {
                        field.setText(placeholder);
                         // Si es el campo de contraseña, vuelve a mostrar el texto del placeholder
                        if (field instanceof JPasswordField) {
                            ((JPasswordField) field).setEchoChar((char) 0);
                        }
                    }
                }
            }
        });
    }
    
    // ... tu código de paintComponent, RoundedPanel y main ...
    // ... (no necesita cambios, es igual al de la respuesta anterior) ...

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(175, 64, 255),
                getWidth(), getHeight(), new Color(0, 221, 235)
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
    }

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