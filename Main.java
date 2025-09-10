import javax.swing.JFrame;
import java.awt.Color;

public class Main extends JFrame{

    JFrame frame;

    public Main(){
        frame = new JFrame();
        // Elimina la barra de título y los bordes de la ventana
        frame.setUndecorated(true);

        // Establece el estado del JFrame para que se maximice en ambas direcciones (ancho y alto)
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Opcional: Establece un color de fondo para ver que la ventana es de pantalla completa
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        // Hace que la ventana sea visible
        frame.setVisible(true);

        // Cierra la aplicación cuando se cierra la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        Main m= new Main();
    }
}