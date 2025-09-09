import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Button extends JButton {

    // Constructor con solo texto
    public Button(String text) {
        super(text);
        initStyle();
    }

    // Constructor con texto + icono
    public Button(String text, Icon icon) {
        super(text, icon);
        initStyle();
        setHorizontalAlignment(SwingConstants.RIGHT); // Texto e icono alineados a la izquierda
        setIconTextGap(10); // Espacio entre icono y texto
    }

    private void initStyle() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(180, 60));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(new Color(0, 221, 235));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.WHITE);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                setFont(new Font("Arial", Font.BOLD, 14));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                setFont(new Font("Arial", Font.BOLD, 16));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Gradiente del borde
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(175, 64, 255), // #af40ff
                getWidth(), getHeight(), new Color(0, 221, 235) // #00ddeb
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        // Fondo interno
        g2.setColor(new Color(113, 105, 187)); // rgb(113, 105, 187)
        g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 10, 10);
        // Texto del botón
        super.paintComponent(g);
        g2.dispose();
    }
}

class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo Botón con Icono");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        // Cargar icono
        Icon homeIcon = new ImageIcon(new ImageIcon("home.png").getImage()
                .getScaledInstance(24, 24, Image.SCALE_SMOOTH));

        // Crear botones
        Button btn1 = new Button("Con Icono", homeIcon);

        // Añadir al frame
        frame.add(btn1);
            frame.setVisible(true);
        
    }
}
