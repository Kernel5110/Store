import java.awt.*;
import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Cuestionario extends JPanel {
    private JTextField[] textFields;
    private String[] placeholders;
    private JLabel iconLabel;

    public Cuestionario(int ancho, int alto, String[] nombres) {
        this.placeholders = nombres;
        setPreferredSize(new Dimension(ancho / 4, alto));
        setLayout(new BorderLayout());

        // Panel contenedor principal
        JPanel back = new JPanel();
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

        // Crear arreglo de JTextField con placeholders
        textFields = new JTextField[nombres.length];
        for (int i = 0; i < nombres.length; i++) {
            final int index = i; // Para usar en el listener
            textFields[i] = new JTextField(nombres[i], 40);
            textFields[i].setPreferredSize(new Dimension(180, 70));
            textFields[i].setHorizontalAlignment(SwingConstants.LEFT);

            // Añadir listener para manejar el focus
            textFields[i].addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (textFields[index].getText().equals(placeholders[index])) {
                        textFields[index].setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (textFields[index].getText().isEmpty()) {
                        textFields[index].setText(placeholders[index]);
                    }
                }
            });

            gbc.gridy = i + 1;
            back.add(textFields[i], gbc);
        }

        add(back, BorderLayout.CENTER);
    }

    // Método para obtener los datos ingresados en los JTextField
    public String[] getDatos() {
        String[] datos = new String[textFields.length];
        for (int i = 0; i < textFields.length; i++) {
            String texto = textFields[i].getText();
            datos[i] = texto.equals(placeholders[i]) ? "" : texto;
        }
        return datos;
    }

    // Método de prueba
    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo Cuestionario Personalizable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Ejemplo de uso: pasar un arreglo de nombres para los placeholders
        String[] nombres = {"Nombre", "Edad", "Correo", "Teléfono"};
        Cuestionario cuestionario = new Cuestionario(800, 800, nombres);

        // Panel de contenido
        JPanel contenido = new JPanel();
        contenido.setBackground(new Color(220, 220, 220));

        // Botón para probar la recuperación de datos
        JButton btnObtenerDatos = new JButton("Obtener Datos");
        btnObtenerDatos.addActionListener(e -> {
            String[] datos = cuestionario.getDatos();
            for (String dato : datos) {
                System.out.println(dato);
            }
        });

        contenido.add(btnObtenerDatos);

        // Añadir menú y contenido
        frame.setLayout(new BorderLayout());
        frame.add(cuestionario, BorderLayout.EAST);
        frame.add(contenido, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
