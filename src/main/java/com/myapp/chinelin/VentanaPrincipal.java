package com.myapp.chinelin;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema Chinelín");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Fondo oscuro
        getContentPane().setBackground(new Color(40, 40, 40));

        // Fuente elegante
        Font font = new Font("Segoe UI", Font.BOLD, 16);

        // Panel con layout vertical
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 40, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setLayout(new GridLayout(4, 1, 15, 15));

        // Crear botones con estilo personalizado
        JButton btnProducto = createStyledButton("Registrar Producto", font);
        JButton btnVenta = createStyledButton("Realizar Venta", font);
        JButton btnInventario = createStyledButton("Ver Inventario", font);
        JButton btnSalir = createStyledButton("Salir", font);
        btnSalir.addActionListener(e -> System.exit(0));
        btnProducto.addActionListener(e -> {
            RegistrarProducto ventanaProducto = new RegistrarProducto();
            ventanaProducto.setVisible(true);
        });
        btnInventario.addActionListener(e -> {
            VerInventario ventanaInventario = new VerInventario();
            ventanaInventario.setVisible(true);
        });

        // Añadir al panel
        panel.add(btnProducto);
        panel.add(btnVenta);
        panel.add(btnInventario);
        panel.add(btnSalir);

        add(panel);
    }

    // Método para crear botones estilizados
    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
