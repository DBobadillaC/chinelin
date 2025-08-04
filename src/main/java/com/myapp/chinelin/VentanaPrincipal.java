package chanelin.gui;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {
    public VentanaPrincipal() {
        setTitle("Sistema Chinelín");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JButton btnProducto = new JButton("Registrar Producto");
        btnProducto.setBounds(100, 30, 200, 30);
        add(btnProducto);

        JButton btnVenta = new JButton("Realizar Venta");
        btnVenta.setBounds(100, 70, 200, 30);
        add(btnVenta);

        JButton btnInventario = new JButton("Ver Inventario");
        btnInventario.setBounds(100, 110, 200, 30);
        add(btnInventario);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(100, 150, 200, 30);
        btnSalir.addActionListener(e -> System.exit(0));
        add(btnSalir);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
