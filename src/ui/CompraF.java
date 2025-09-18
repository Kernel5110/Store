/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author lore0
 */


import dao.CompraDAO;
import dao.ProductoDAO;
import dao.ProveedorDAO;
import modelo.Compra;
import modelo.CompraProducto;
import modelo.Producto;
import modelo.Proveedor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import controlador.ControladorProducto;
public class CompraF extends javax.swing.JFrame {

    private CompraDAO dao;
    private ProductoDAO productoDao;
    private ProveedorDAO proveedorDao;

    public CompraF() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.dao = new CompraDAO();
        this.productoDao = new ProductoDAO();
        this.proveedorDao = new ProveedorDAO();
        cargarProveedores();
        cargarProductos();
        listarCompras();
    }

    private void cargarProveedores() {
        cmbProveedor.removeAllItems();
        for (Proveedor p : proveedorDao.listarProveedores()) {
            cmbProveedor.addItem(p.getNombre() + " (" + p.getEmpresa() + ")");
        }
    }

    private void cargarProductos() {
        lstProductos.setModel(new DefaultListModel<>());
        for (Producto p : productoDao.listarProductos()) {
            ((DefaultListModel<String>) lstProductos.getModel()).addElement(
                p.getNombre() + " - $" + p.getPrecio() + " | Stock: " + p.getStockTotal()
            );
        }
    }

    private void listarCompras() {
        DefaultTableModel modelo = (DefaultTableModel) tblCompras.getModel();
        modelo.setRowCount(0);

        for (Compra c : dao.listarCompras()) {
            modelo.addRow(new Object[]{
                c.getId_compra(),
                c.getNombreProveedor(),
                c.getFecha()
            });
        }
    }

    private void agregarProductoACarrito() {
        int index = lstProductos.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto");
            return;
        }

        String selected = lstProductos.getSelectedValue();
        String[] parts = selected.split(" - ");
        String nombre = parts[0];

        Producto p = productoDao.buscarPorNombre(nombre);
        if (p != null) {
            DefaultTableModel model = (DefaultTableModel) tblCarrito.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(p.getNombre())) {
                    int nuevaCantidad = Integer.parseInt(model.getValueAt(i, 2).toString()) + 1;
                    model.setValueAt(nuevaCantidad, i, 2);
                    return;
                }
            }
            model.addRow(new Object[]{p.getNombre(), p.getPrecio(), 1, p.getIdProducto()});
        }
    }

    private void realizarCompra() {
        if (tblCarrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío");
            return;
        }

        int proveedorIndex = cmbProveedor.getSelectedIndex();
        if (proveedorIndex == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor");
            return;
        }

        Proveedor proveedor = proveedorDao.listarProveedores().get(proveedorIndex);

        List<CompraProducto> detalles = new java.util.ArrayList<>();
        for (int i = 0; i < tblCarrito.getRowCount(); i++) {
            String nombre = tblCarrito.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(tblCarrito.getValueAt(i, 2).toString());
            Producto p = productoDao.buscarPorNombre(nombre);
            if (p != null) {
                detalles.add(new CompraProducto(0, (int) p.getIdProducto(), cantidad));
            }
        }

        Compra compra = new Compra(0, proveedor.getId_proveedor(), new java.sql.Date(System.currentTimeMillis()));
        if (dao.insertarCompra(compra, detalles)) {
            JOptionPane.showMessageDialog(this, "Compra registrada correctamente");
            limpiarCarrito();
            listarCompras();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar compra");
        }
    }

    private void limpiarCarrito() {
        DefaultTableModel model = (DefaultTableModel) tblCarrito.getModel();
        model.setRowCount(0);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCompras = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lstProductos = new javax.swing.JList<>();
        btnAgregar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCarrito = new javax.swing.JTable();
        btnRealizarCompra = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Compras");

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        btnVolver.setText("Volver");
        btnVolver.addActionListener(e -> {
            new Menu().setVisible(true);
            dispose();
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnVolver)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 276, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(195, 195, 195)
                .addComponent(btnCerrar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnVolver)
                    .addComponent(btnCerrar))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(230, 230, 255));

        tblCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String[]{
                "ID", "Proveedor", "Fecha"
            }
        ));
        jScrollPane1.setViewportView(tblCompras);

        jPanel3.setBackground(new java.awt.Color(240, 240, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de Compra"));

        jLabel2.setText("Proveedor:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbProveedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(240, 240, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Productos Disponibles"));

        jLabel3.setText("Selecciona producto:");

        btnAgregar.setText("Agregar al Carrito");
        btnAgregar.addActionListener(e -> agregarProductoACarrito());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(btnAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lstProductos, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lstProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(240, 240, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Carrito de Compras"));

        jLabel4.setText("Productos seleccionados:");

        tblCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{
                {null, null, null, null}
            },
            new String[]{
                "Producto", "Precio Unitario", "Cantidad", "ID Producto"
            }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblCarrito);

        btnRealizarCompra.setText("Realizar Compra");
        btnRealizarCompra.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnRealizarCompra.setBackground(new java.awt.Color(0, 153, 51));
        btnRealizarCompra.setForeground(new java.awt.Color(255, 255, 255));
        btnRealizarCompra.addActionListener(e -> realizarCompra());

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRealizarCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRealizarCompra)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    // Variables declaration
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnRealizarCompra;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> lstProductos;
    private javax.swing.JTable tblCarrito;
    private javax.swing.JTable tblCompras;
    // End of variables declaration
}