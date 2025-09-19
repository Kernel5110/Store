package controlador;

import dao.ProductoDAO;
import modelo.Producto;
import ui.ProductoF;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ControladorProducto implements ActionListener {

    private ProductoF vista;
    private ProductoDAO dao;

    public ControladorProducto(ProductoF vista) {
        this.vista = vista;
        this.dao = new ProductoDAO();

        // Registrar botones
        this.vista.btnAgregarp.addActionListener(this);
        this.vista.btnModificarp.addActionListener(this);
        this.vista.btnEliminarp.addActionListener(this);
        this.vista.btnActualizarp.addActionListener(this);

        // Cargar tabla inicial
        this.vista.listarProductos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAgregarp) {
            agregarProducto();
        } else if (e.getSource() == vista.btnModificarp) {
            modificarProducto();
        } else if (e.getSource() == vista.btnEliminarp) {
            eliminarProducto();
        } else if (e.getSource() == vista.btnActualizarp) {
            vista.listarProductos();
        }
    }

  private void agregarProducto() {
      System.out.println("mirame");
    try {
        String nombre = vista.txtNombreP.getText().trim();
        String tipo = vista.txtTipop.getText().trim();
        String precioStr = vista.txtPreciop.getText().trim();

        // Validar campos
        if (nombre.isEmpty() || tipo.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete todos los campos");
            return;
        }

        // Validar precio
        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                JOptionPane.showMessageDialog(vista, "El precio debe ser mayor que 0");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese un precio válido");
            return;
        }

        // Validar tipo
        if (!tipo.equals("perecedero") && !tipo.equals("no_perecedero")) {
            JOptionPane.showMessageDialog(vista, "El tipo debe ser 'perecedero' o 'no_perecedero'");
            return;
        }

        // Crear objeto Producto
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setTipo(tipo);

        // Insertar
        if (dao.insertarProducto(p)) {
            JOptionPane.showMessageDialog(vista, "Producto agregado correctamente");
            vista.listarProductos();
            vista.limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al agregar producto");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
    }
}


    private void modificarProducto() {
        try {
            Producto p = new Producto();
            p.setIdProducto(Integer.parseInt(vista.txtidproducto.getText()));
            p.setNombre(vista.txtNombreP.getText());
            p.setPrecio(Double.parseDouble(vista.txtPreciop.getText()));
            p.setTipo(vista.txtTipop.getText());

            if (dao.actualizarProducto(p)) {
                JOptionPane.showMessageDialog(vista, "Producto modificado correctamente");
                vista.listarProductos();
                vista.limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al modificar producto");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
        }
    }

    private void eliminarProducto() {
        try {
            int id = Integer.parseInt(vista.txtidproducto.getText());
            if (dao.eliminarProducto(id)) {
                JOptionPane.showMessageDialog(vista, "Producto eliminado correctamente");
                vista.listarProductos();
                vista.limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar producto");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
        }
    }
}
