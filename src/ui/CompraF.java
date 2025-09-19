package ui;

import dao.CompraDAO;
import dao.ProductoDAO;
import dao.ProveedorDAO;
import modelo.Compra;
import modelo.CompraProducto;
import modelo.Producto;
import modelo.Proveedor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

public class CompraF extends javax.swing.JFrame {
    private CompraDAO dao;
    private ProductoDAO productoDao;
    private ProveedorDAO proveedorDao;
    private int idCompraSeleccionada = -1;

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
        List<Proveedor> proveedores = proveedorDao.listarProveedores();
        if (proveedores.isEmpty()) {
            cmbProveedor.addItem("No hay proveedores registrados");
        } else {
            for (Proveedor p : proveedores) {
                cmbProveedor.addItem(p.getNombre() + " (" + p.getEmpresa() + ")");
            }
        }
    }

    private void cargarProductos() {
        DefaultListModel<String> model = new DefaultListModel<>();
        List<Producto> productos = productoDao.listarProductosStock();

        if (productos.isEmpty()) {
            model.addElement("No hay productos disponibles");
        } else {
            for (Producto p : productos) {
                model.addElement(p.getNombre() + " - $" + p.getPrecio() + " | Stock: " + p.getStock());
            }
        }
        lstProductos.setModel(model);
    }

    private void cargarProductosPorProveedor(int idProveedor) {
        DefaultListModel<String> model = new DefaultListModel<>();
        List<Producto> productos = productoDao.listarProductosPorProveedor(idProveedor);

        if (productos.isEmpty()) {
            model.addElement("No hay productos para este proveedor");
        } else {
            for (Producto p : productos) {
                model.addElement(p.getNombre() + " - $" + p.getPrecio() + " | Stock: " + p.getStock());
            }
        }
        lstProductos.setModel(model);
    }

    private void listarCompras() {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[][]{}, new String[]{"ID", "Proveedor", "Fecha"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Compra> compras = dao.listarCompras();
        for (Compra c : compras) {
            modelo.addRow(new Object[]{
                c.getId_compra(),
                c.getNombreProveedor(),
                c.getFecha()
            });
        }
        tblCompras.setModel(modelo);
        tblCompras.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void listarComprasPorProveedor(int idProveedor) {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[][]{}, new String[]{"ID", "Proveedor", "Fecha"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Compra> compras = dao.listarComprasPorProveedor(idProveedor);
        for (Compra c : compras) {
            modelo.addRow(new Object[]{
                c.getId_compra(),
                c.getNombreProveedor(),
                c.getFecha()
            });
        }
        tblCompras.setModel(modelo);
        tblCompras.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void agregarProductoACarrito() {
        int index = lstProductos.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selected = lstProductos.getSelectedValue();
        if (selected.equals("No hay productos disponibles") || selected.equals("No hay productos para este proveedor")) {
            return;
        }

        String[] parts = selected.split(" - ");
        String nombre = parts[0];
        Producto p = productoDao.buscarPorNombre(nombre);

        if (p != null) {
            DefaultTableModel model = (DefaultTableModel) tblCarrito.getModel();
            boolean encontrado = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 3).equals(p.getIdProducto())) {
                    int nuevaCantidad = Integer.parseInt(model.getValueAt(i, 2).toString()) + 1;
                    model.setValueAt(nuevaCantidad, i, 2);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                model.addRow(new Object[]{p.getNombre(), p.getPrecio(), 1, p.getIdProducto()});
            }
        }
    }

    private void actualizarProductoEnCarrito() {
        int filaSeleccionada = tblCarrito.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto en el carrito para actualizar",
                                         "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String cantidadStr = JOptionPane.showInputDialog(this,
                "Ingresa la nueva cantidad para " + tblCarrito.getValueAt(filaSeleccionada, 0) + ":",
                "Actualizar cantidad", JOptionPane.PLAIN_MESSAGE);

            if (cantidadStr == null) return; // Usuario canceló

            int nuevaCantidad = Integer.parseInt(cantidadStr);
            if (nuevaCantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero",
                                             "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tblCarrito.setValueAt(nuevaCantidad, filaSeleccionada, 2);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingresa un número válido",
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProductoDelCarrito() {
        int filaSeleccionada = tblCarrito.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto en el carrito para eliminar",
                                         "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblCarrito.getModel();
        model.removeRow(filaSeleccionada);
    }

    private void realizarCompra() {
        if (tblCarrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío",
                                         "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int proveedorIndex = cmbProveedor.getSelectedIndex();
        if (proveedorIndex == -1 || cmbProveedor.getItemCount() == 0 ||
            cmbProveedor.getSelectedItem().equals("No hay proveedores registrados")) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor válido",
                                         "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Proveedor proveedor = proveedorDao.listarProveedores().get(proveedorIndex);
        List<CompraProducto> detalles = new java.util.ArrayList<>();

        for (int i = 0; i < tblCarrito.getRowCount(); i++) {
            int idProducto = (int) tblCarrito.getValueAt(i, 3);
            int cantidad = Integer.parseInt(tblCarrito.getValueAt(i, 2).toString());
            detalles.add(new CompraProducto(0, idProducto, cantidad));
        }

        boolean exito;
        if (idCompraSeleccionada != -1) {
            exito = dao.actualizarCompra(idCompraSeleccionada, proveedor.getIdProveedor(), detalles);
        } else {
            Compra compra = new Compra(0, proveedor.getIdProveedor(), new java.sql.Date(System.currentTimeMillis()));
            exito = dao.insertarCompra(compra, detalles);
        }

        if (exito) {
            JOptionPane.showMessageDialog(this,
                idCompraSeleccionada != -1 ? "Compra actualizada correctamente" : "Compra registrada correctamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCarrito();
            listarComprasPorProveedor(proveedor.getIdProveedor());
            idCompraSeleccionada = -1;
        } else {
            JOptionPane.showMessageDialog(this, "Error al procesar la compra",
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCarrito() {
        DefaultTableModel model = (DefaultTableModel) tblCarrito.getModel();
        model.setRowCount(0);
    }

    private void cargarDetallesCompraEnCarrito(int idCompra) {
        idCompraSeleccionada = idCompra;
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{}, new String[]{"Producto", "Precio Unitario", "Cantidad", "ID Producto"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la columna de cantidad es editable
            }
        };

        List<CompraProducto> detalles = dao.listarDetallesCompra(idCompra);
        for (CompraProducto cp : detalles) {
            Producto p = productoDao.buscarProductoPorId(cp.getProducto_id());
            if (p != null) {
                model.addRow(new Object[]{
                    p.getNombre(),
                    p.getPrecio(),
                    cp.getCantidad(),
                    p.getIdProducto()
                });
            }
        }
        tblCarrito.setModel(model);
        tblCarrito.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Paneles principales
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();

        jPanel2 = new javax.swing.JPanel();
        jPanelCompras = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCompras = new javax.swing.JTable();

        jPanelDatos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();

        jPanelProductos = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstProductos = new javax.swing.JList<>();
        btnAgregarProducto = new javax.swing.JButton();

        jPanelCarrito = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCarrito = new javax.swing.JTable();
        btnActualizarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnRealizarCompra = new javax.swing.JButton();

        // Configuración de la ventana
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestión de Compras");
        setPreferredSize(new java.awt.Dimension(900, 700));

        // Panel superior (título y botones)
        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel1.setText("Gestión de Compras");

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        // ✅ CORREGIDO: Cambiado Menu() por MenuAdmin() y orden de operaciones
        btnVolver.setText("Volver");
        btnVolver.addActionListener(e -> {
            MenuAdmin menu = new MenuAdmin();
            menu.setVisible(true);
            this.dispose();
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnVolver)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(btnCerrar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnVolver)
                    .addComponent(btnCerrar))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        // Panel de compras (tabla)
        jPanelCompras.setBorder(javax.swing.BorderFactory.createTitledBorder("Compras Registradas"));
        jPanelCompras.setPreferredSize(new java.awt.Dimension(850, 150));

        tblCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{}, new String[]{"ID", "Proveedor", "Fecha"}
        ));
        tblCompras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblCompras);

        tblCompras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int filaSeleccionada = tblCompras.getSelectedRow();
                if (filaSeleccionada != -1) {
                    idCompraSeleccionada = (int) tblCompras.getValueAt(filaSeleccionada, 0);
                    cargarDetallesCompraEnCarrito(idCompraSeleccionada);
                }
            }
        });

        javax.swing.GroupLayout jPanelComprasLayout = new javax.swing.GroupLayout(jPanelCompras);
        jPanelCompras.setLayout(jPanelComprasLayout);
        jPanelComprasLayout.setHorizontalGroup(
            jPanelComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelComprasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelComprasLayout.setVerticalGroup(
            jPanelComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelComprasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addContainerGap())
        );

        // Panel de datos (proveedor)
        jPanelDatos.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de Compra"));
        jPanelDatos.setPreferredSize(new java.awt.Dimension(850, 70));

        jLabel2.setText("Proveedor:");

        cmbProveedor.setPreferredSize(new java.awt.Dimension(300, 30));
        cmbProveedor.addActionListener(e -> {
            if (cmbProveedor.getSelectedIndex() != -1 &&
                !cmbProveedor.getSelectedItem().equals("No hay proveedores registrados")) {
                Proveedor proveedorSeleccionado = proveedorDao.listarProveedores().get(cmbProveedor.getSelectedIndex());
                listarComprasPorProveedor(proveedorSeleccionado.getIdProveedor());
                cargarProductosPorProveedor(proveedorSeleccionado.getIdProveedor());
                idCompraSeleccionada = -1;
                limpiarCarrito();
            }
        });

        javax.swing.GroupLayout jPanelDatosLayout = new javax.swing.GroupLayout(jPanelDatos);
        jPanelDatos.setLayout(jPanelDatosLayout);
        jPanelDatosLayout.setHorizontalGroup(
            jPanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(450, Short.MAX_VALUE))
        );
        jPanelDatosLayout.setVerticalGroup(
            jPanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        // Panel de productos disponibles
        jPanelProductos.setBorder(javax.swing.BorderFactory.createTitledBorder("Productos Disponibles"));
        jPanelProductos.setPreferredSize(new java.awt.Dimension(850, 180));

        jLabel3.setText("Productos:");

        lstProductos.setVisibleRowCount(5);
        lstProductos.setFixedCellHeight(20);
        jScrollPane3.setViewportView(lstProductos);

        btnAgregarProducto.setText("Agregar al Carrito");
        btnAgregarProducto.setPreferredSize(new java.awt.Dimension(120, 30));
        btnAgregarProducto.addActionListener(e -> agregarProductoACarrito());

        javax.swing.GroupLayout jPanelProductosLayout = new javax.swing.GroupLayout(jPanelProductos);
        jPanelProductos.setLayout(jPanelProductosLayout);
        jPanelProductosLayout.setHorizontalGroup(
            jPanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelProductosLayout.setVerticalGroup(
            jPanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                .addContainerGap())
        );

        // Panel del carrito
        jPanelCarrito.setBorder(javax.swing.BorderFactory.createTitledBorder("Carrito de Compras"));
        jPanelCarrito.setPreferredSize(new java.awt.Dimension(850, 200));

        jLabel4.setText("Productos seleccionados:");

        tblCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{}, new String[]{"Producto", "Precio Unitario", "Cantidad", "ID Producto"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        });
        tblCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tblCarrito);

        btnActualizarProducto.setText("Actualizar Producto");
        btnActualizarProducto.setPreferredSize(new java.awt.Dimension(130, 30));
        btnActualizarProducto.addActionListener(e -> actualizarProductoEnCarrito());

        btnEliminarProducto.setText("Eliminar Producto");
        btnEliminarProducto.setPreferredSize(new java.awt.Dimension(130, 30));
        btnEliminarProducto.addActionListener(e -> eliminarProductoDelCarrito());

        btnRealizarCompra.setText("Realizar Compra");
        btnRealizarCompra.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnRealizarCompra.setBackground(new java.awt.Color(0, 153, 51));
        btnRealizarCompra.setForeground(new java.awt.Color(255, 255, 255));
        btnRealizarCompra.setPreferredSize(new java.awt.Dimension(150, 30));
        btnRealizarCompra.addActionListener(e -> realizarCompra());

        javax.swing.GroupLayout jPanelCarritoLayout = new javax.swing.GroupLayout(jPanelCarrito);
        jPanelCarrito.setLayout(jPanelCarritoLayout);
        jPanelCarritoLayout.setHorizontalGroup(
            jPanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCarritoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .addGroup(jPanelCarritoLayout.createSequentialGroup()
                        .addComponent(btnActualizarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnRealizarCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelCarritoLayout.setVerticalGroup(
            jPanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCarritoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(jPanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizarProducto)
                    .addComponent(btnEliminarProducto)
                    .addComponent(btnRealizarCompra))
                .addContainerGap())
        );

        // Panel principal
        jPanel2.setBackground(new java.awt.Color(240, 240, 255));
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelCompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelCarrito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanelProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanelCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

    
  

        // Layout principal
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
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    // Variables declaration
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnActualizarProducto;
    private javax.swing.JButton btnEliminarProducto;
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
    private javax.swing.JPanel jPanelCompras;
    private javax.swing.JPanel jPanelDatos;
    private javax.swing.JPanel jPanelProductos;
    private javax.swing.JPanel jPanelCarrito;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> lstProductos;
    private javax.swing.JTable tblCarrito;
    private javax.swing.JTable tblCompras;
    // End of variables declaration
}
