package com.myapp.chinelin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

public class RealizarVenta extends JFrame {

    private JComboBox<String> comboProductos;
    private JTextField txtCantidad;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private JTable tabla;

    public RealizarVenta() {
        setTitle("Realizar Venta");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(40, 40, 40));

        // Panel superior
        JPanel panelTop = new JPanel(new FlowLayout());
        panelTop.setBackground(new Color(40, 40, 40));

        comboProductos = new JComboBox<>();
        cargarProductos();
        txtCantidad = new JTextField(5);
        JButton btnAgregar = new JButton("Agregar");

        panelTop.add(new JLabel("Producto:"));
        panelTop.add(comboProductos);
        panelTop.add(new JLabel("Cantidad:"));
        panelTop.add(txtCantidad);
        panelTop.add(btnAgregar);

        add(panelTop, BorderLayout.NORTH);

        // Tabla de detalle de venta
        String[] columnas = {"ID", "Nombre", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBottom.setBackground(new Color(40, 40, 40));
        lblTotal = new JLabel("Total: S/. 0.00");
        lblTotal.setForeground(Color.WHITE);
        JButton btnRegistrar = new JButton("Registrar Venta");
        JButton btnEliminar = new JButton("Eliminar Selección");

        panelBottom.add(lblTotal);
        panelBottom.add(btnEliminar);
        panelBottom.add(btnRegistrar);
        add(panelBottom, BorderLayout.SOUTH);

        // Acción agregar
        btnAgregar.addActionListener(e -> agregarProducto());
        btnRegistrar.addActionListener(e -> registrarVenta());
        btnEliminar.addActionListener(e -> eliminarProducto());
    }

    private void cargarProductos() {
        try (Connection conn = ConexionBD.obtenerConexion()) {
            String sql = "SELECT id, nombre FROM productos WHERE stock > 0";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboProductos.addItem(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }

    private void agregarProducto() {
        String selected = (String) comboProductos.getSelectedItem();
        if (selected == null || txtCantidad.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto y cantidad.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.");
            return;
        }

        int id = Integer.parseInt(selected.split(" - ")[0]);
        String nombre = selected.split(" - ")[1];

        try (Connection conn = ConexionBD.obtenerConexion()) {
            String sql = "SELECT precio, stock FROM productos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");

                // Verificar si el producto ya está en la tabla
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    int idTabla = (int) modeloTabla.getValueAt(i, 0);
                    if (idTabla == id) {
                        int cantidadExistente = (int) modeloTabla.getValueAt(i, 2);
                        int nuevaCantidad = cantidadExistente + cantidad;

                        if (nuevaCantidad > stock) {
                            JOptionPane.showMessageDialog(this, "No hay suficiente stock disponible.");
                            return;
                        }

                        double nuevoSubtotal = nuevaCantidad * precio;
                        modeloTabla.setValueAt(nuevaCantidad, i, 2);
                        modeloTabla.setValueAt(nuevoSubtotal, i, 4);
                        actualizarTotal();
                        txtCantidad.setText("");
                        return;
                    }
                }

                if (cantidad > stock) {
                    JOptionPane.showMessageDialog(this, "Stock insuficiente.");
                    return;
                }

                double subtotal = cantidad * precio;
                modeloTabla.addRow(new Object[]{id, nombre, cantidad, precio, subtotal});
                actualizarTotal();
                txtCantidad.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener producto: " + e.getMessage());
        }
    }

    private void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            total += (double) modeloTabla.getValueAt(i, 4);
        }
        lblTotal.setText(String.format("Total: S/. %.2f", total));
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            modeloTabla.removeRow(fila);
            actualizarTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.");
        }
    }

    private void registrarVenta() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue productos antes de registrar.");
            return;
        }

        double total = 0;
        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                total += (double) modeloTabla.getValueAt(i, 4);
            }

            String ventaSql = "INSERT INTO ventas (fecha, total) VALUES (?, ?)";
            PreparedStatement ventaStmt = conn.prepareStatement(ventaSql, Statement.RETURN_GENERATED_KEYS);
            ventaStmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ventaStmt.setDouble(2, total);
            ventaStmt.executeUpdate();

            ResultSet generatedKeys = ventaStmt.getGeneratedKeys();
            int ventaId = 0;
            if (generatedKeys.next()) {
                ventaId = generatedKeys.getInt(1);
            }

            String detalleSql = "INSERT INTO detalle_venta (venta_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement detalleStmt = conn.prepareStatement(detalleSql);

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                int productoId = (int) modeloTabla.getValueAt(i, 0);
                int cantidad = (int) modeloTabla.getValueAt(i, 2);
                double subtotal = (double) modeloTabla.getValueAt(i, 4);

                detalleStmt.setInt(1, ventaId);
                detalleStmt.setInt(2, productoId);
                detalleStmt.setInt(3, cantidad);
                detalleStmt.setDouble(4, subtotal);
                detalleStmt.addBatch();

                // Actualizar stock
                PreparedStatement updateStock = conn.prepareStatement("UPDATE productos SET stock = stock - ? WHERE id = ?");
                updateStock.setInt(1, cantidad);
                updateStock.setInt(2, productoId);
                updateStock.executeUpdate();
            }

            detalleStmt.executeBatch();
            conn.commit();

            JOptionPane.showMessageDialog(this, "Venta registrada exitosamente.");
            modeloTabla.setRowCount(0);
            actualizarTotal();
            comboProductos.removeAllItems();
            cargarProductos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar venta: " + e.getMessage());
        }
    }
}
