package com.myapp.chinelin;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistrarProducto extends JFrame {

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JButton btnGuardar;

    public RegistrarProducto() {
        setTitle("Registrar Producto");
        setSize(400, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(33, 33, 33));

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 30, 80, 25);
        lblNombre.setForeground(Color.WHITE);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(150, 30, 180, 25);
        add(txtNombre);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(50, 70, 80, 25);
        lblDescripcion.setForeground(Color.WHITE);
        add(lblDescripcion);

        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(150, 70, 180, 25);
        add(txtDescripcion);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(50, 110, 80, 25);
        lblPrecio.setForeground(Color.WHITE);
        add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(150, 110, 180, 25);
        add(txtPrecio);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setBounds(50, 150, 80, 25);
        lblStock.setForeground(Color.WHITE);
        add(lblStock);

        txtStock = new JTextField();
        txtStock.setBounds(150, 150, 180, 25);
        add(txtStock);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 210, 100, 30);
        btnGuardar.setBackground(new Color(25, 118, 210));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarProducto());
    }

    private void guardarProducto() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        double precio;
        int stock;

        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
            stock = Integer.parseInt(txtStock.getText().trim());

            if (nombre.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection conn = ConexionBD.obtenerConexion();
            String sql = "INSERT INTO productos (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDouble(3, precio);
            stmt.setInt(4, stock);

            int resultado = stmt.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }

            stmt.close();
            conn.close();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio o stock no válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }
}
