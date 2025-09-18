package controlador;

import dao.AlmacenDAO;
import modelo.Almacen;
import ui.AlmacenF;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ControladorAlmacen implements ActionListener {

    private AlmacenF vista;
    private AlmacenDAO dao;

    public ControladorAlmacen(AlmacenF vista) {
        this.vista = vista;
        this.dao = new AlmacenDAO();

        // Registrar botones
        this.vista.btnAgregarA.addActionListener(this);
        this.vista.btnModificarA.addActionListener(this);
        this.vista.btnEliminarA.addActionListener(this);
        this.vista.btnActualizarA.addActionListener(this);

        // Cargar tabla inicial
        this.vista.listarAlmacenes();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAgregarA) {
            agregarAlmacen();
        } else if (e.getSource() == vista.btnModificarA) {
            modificarAlmacen();
        } else if (e.getSource() == vista.btnEliminarA) {
            eliminarAlmacen();
        } else if (e.getSource() == vista.btnActualizarA) {
            vista.listarAlmacenes();
            vista.limpiarCampos();
        }
    }

    private void agregarAlmacen() {
        try {
            String nombre = vista.txtNombreA.getText().trim();
            String ubicacion = vista.txtUbicacionA.getText().trim();

            // Validar campos
            if (nombre.isEmpty() || ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Complete todos los campos");
                return;
            }

            // Validar longitud de campos
            if (nombre.length() > 100) {
                JOptionPane.showMessageDialog(vista, "El nombre no puede exceder 100 caracteres");
                return;
            }

            if (ubicacion.length() > 100) {
                JOptionPane.showMessageDialog(vista, "La ubicación no puede exceder 100 caracteres");
                return;
            }

            // Verificar si ya existe un almacén con ese nombre
            if (dao.existeAlmacen(nombre)) {
                JOptionPane.showMessageDialog(vista, "Ya existe un almacén con ese nombre");
                return;
            }

            // Crear objeto Almacen
            Almacen a = new Almacen();
            a.setNombre(nombre);
            a.setUbicacion(ubicacion);

            // Insertar
            if (dao.insertarAlmacen(a)) {
                JOptionPane.showMessageDialog(vista, "Almacén agregado correctamente");
                vista.listarAlmacenes();
                vista.limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al agregar almacén");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
        }
    }

    private void modificarAlmacen() {
        try {
            // Validar que se haya seleccionado un almacén
            if (vista.txtIdAlmacen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Seleccione un almacén de la tabla para modificar");
                return;
            }

            String nombre = vista.txtNombreA.getText().trim();
            String ubicacion = vista.txtUbicacionA.getText().trim();

            // Validar campos
            if (nombre.isEmpty() || ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Complete todos los campos");
                return;
            }

            // Validar longitud de campos
            if (nombre.length() > 100) {
                JOptionPane.showMessageDialog(vista, "El nombre no puede exceder 100 caracteres");
                return;
            }

            if (ubicacion.length() > 100) {
                JOptionPane.showMessageDialog(vista, "La ubicación no puede exceder 100 caracteres");
                return;
            }

            // Crear objeto Almacen
            Almacen a = new Almacen();
            a.setIdAlmacen(Integer.parseInt(vista.txtIdAlmacen.getText()));
            a.setNombre(nombre);
            a.setUbicacion(ubicacion);

            // Actualizar
            if (dao.actualizarAlmacen(a)) {
                JOptionPane.showMessageDialog(vista, "Almacén modificado correctamente");
                vista.listarAlmacenes();
                vista.limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al modificar almacén");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "ID de almacén no válido");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
        }
    }

    private void eliminarAlmacen() {
        try {
            // Validar que se haya seleccionado un almacén
            if (vista.txtIdAlmacen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Seleccione un almacén de la tabla para eliminar");
                return;
            }

            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de que desea eliminar este almacén?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(vista.txtIdAlmacen.getText());
                
                if (dao.eliminarAlmacen(id)) {
                    JOptionPane.showMessageDialog(vista, "Almacén eliminado correctamente");
                    vista.listarAlmacenes();
                    vista.limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al eliminar almacén");
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "ID de almacén no válido");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
        }
    }
}