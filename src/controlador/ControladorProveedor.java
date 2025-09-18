/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.ProveedorDAO;
import modelo.Proveedor;
import ui.ProveedorF;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

public class ControladorProveedor implements ActionListener {

    private ProveedorF vista;
    private ProveedorDAO dao;

    public ControladorProveedor(ProveedorF vista) {
        this.vista = vista;
        this.dao = new ProveedorDAO();

        // Registrar botones
        this.vista.btnAgregarP.addActionListener(this);
        this.vista.btnModificarP.addActionListener(this);
        this.vista.btnEliminarP.addActionListener(this);
        this.vista.btnActualizarP.addActionListener(this);

        // Cargar tabla inicial
        this.vista.listarProveedores();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAgregarP) {
            agregarProveedor();
        } else if (e.getSource() == vista.btnModificarP) {
            modificarProveedor();
        } else if (e.getSource() == vista.btnEliminarP) {
            eliminarProveedor();
        } else if (e.getSource() == vista.btnActualizarP) {
            vista.listarProveedores();
            vista.limpiarCampos();
        }
    }

    private void agregarProveedor() {
        Proveedor p = obtenerProveedorDesdeVista();
        if (p == null) return;

        if (dao.existeProveedor(p.getNombre())) {
            JOptionPane.showMessageDialog(vista, "Ya existe un proveedor con ese nombre");
            return;
        }

        if (dao.insertarProveedor(p)) {
            JOptionPane.showMessageDialog(vista, "Proveedor agregado correctamente");
            vista.listarProveedores();
            vista.limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al agregar proveedor");
        }
    }

    private void modificarProveedor() {
        if (vista.txtIdProveedor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un proveedor de la tabla para modificar");
            return;
        }

        Proveedor p = obtenerProveedorDesdeVista();
        if (p == null) return;

        try {
            p.setIdProveedor(Integer.parseInt(vista.txtIdProveedor.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "ID de proveedor no válido");
            return;
        }

        if (dao.actualizarProveedor(p)) {
            JOptionPane.showMessageDialog(vista, "Proveedor modificado correctamente");
            vista.listarProveedores();
            vista.limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al modificar proveedor");
        }
    }

    private void eliminarProveedor() {
        if (vista.txtIdProveedor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un proveedor de la tabla para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro de que desea eliminar este proveedor?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(vista.txtIdProveedor.getText());
                if (dao.eliminarProveedor(id)) {
                    JOptionPane.showMessageDialog(vista, "Proveedor eliminado correctamente");
                    vista.listarProveedores();
                    vista.limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al eliminar proveedor");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "ID de proveedor no válido");
            }
        }
    }

    // Método común para extraer datos del formulario
    private Proveedor obtenerProveedorDesdeVista() {
        String nombre = vista.txtNombreP.getText().trim();
        String empresa = vista.txtEmpresa.getText().trim();
        String telefono = vista.txtTelefono.getText().trim();
        String precioMinimoStr = vista.txtPrecioMinimo.getText().trim();
        String plazoEntregaStr = vista.txtPlazoEntrega.getText().trim();
        String contacto = vista.txtContacto.getText().trim();

        // Validar campos obligatorios
        if (nombre.isEmpty() || empresa.isEmpty() || telefono.isEmpty() ||
            precioMinimoStr.isEmpty() || plazoEntregaStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete todos los campos obligatorios");
            return null;
        }

        // Validar longitud
        if (nombre.length() > 100) {
            JOptionPane.showMessageDialog(vista, "El nombre no puede exceder 100 caracteres");
            return null;
        }
        if (empresa.length() > 100) {
            JOptionPane.showMessageDialog(vista, "La empresa no puede exceder 100 caracteres");
            return null;
        }
        if (telefono.length() > 20) {
            JOptionPane.showMessageDialog(vista, "El teléfono no puede exceder 20 caracteres");
            return null;
        }
        if (contacto.length() > 100) {
            JOptionPane.showMessageDialog(vista, "El contacto no puede exceder 100 caracteres");
            return null;
        }

        BigDecimal precioMinimo;
        int plazoEntrega;

        try {
            precioMinimo = new BigDecimal(precioMinimoStr);
            if (precioMinimo.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(vista, "El precio mínimo no puede ser negativo");
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Formato de precio mínimo inválido");
            return null;
        }

        try {
            plazoEntrega = Integer.parseInt(plazoEntregaStr);
            if (plazoEntrega < 0) {
                JOptionPane.showMessageDialog(vista, "El plazo de entrega no puede ser negativo");
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Formato de plazo de entrega inválido");
            return null;
        }

        Proveedor p = new Proveedor();
        p.setNombre(nombre);
        p.setEmpresa(empresa);
        p.setTelefono(telefono);
        p.setPrecioMinimo(precioMinimo);
        p.setPlazoEntrega(plazoEntrega);
        p.setContacto(contacto);

        return p;
    }
}