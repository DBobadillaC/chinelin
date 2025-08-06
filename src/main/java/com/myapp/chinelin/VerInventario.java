package com.myapp.chinelin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;

public class VerInventario extends JFrame {

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private TableRowSorter<DefaultTableModel> sorter;

    public VerInventario() {
        setTitle("Inventario de Productos");
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Fuente y colores
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        Color fondoClaro = Color.WHITE;
        Color grisSuave = new Color(240, 240, 240);
        Color grisTexto = new Color(60, 60, 60);

        // Panel superior con buscador
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(grisSuave);
        JLabel lblBuscar = new JLabel("Buscar producto:");
        lblBuscar.setForeground(grisTexto);
        lblBuscar.setFont(font);

        txtBuscar = new JTextField();
        txtBuscar.setFont(font);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.add(lblBuscar, BorderLayout.WEST);
        panelSuperior.add(txtBuscar, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // Definir columnas
        String[] columnas = {"ID", "Nombre", "Descripción", "Precio", "Stock"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(font);
        tablaProductos.setRowHeight(26);
        tablaProductos.setSelectionBackground(new Color(220, 230, 240));
        tablaProductos.setSelectionForeground(Color.BLACK);
        tablaProductos.setGridColor(new Color(200, 200, 200));
        tablaProductos.setBackground(fondoClaro);
        tablaProductos.setForeground(grisTexto);

        tablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaProductos.getTableHeader().setBackground(new Color(220, 220, 220));
        tablaProductos.getTableHeader().setForeground(Color.BLACK);
        tablaProductos.getTableHeader().setReorderingAllowed(false);

        sorter = new TableRowSorter<>(modeloTabla);
        tablaProductos.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Evento de búsqueda
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarTabla(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarTabla(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarTabla(); }
        });

        cargarProductos();
    }

    private void cargarProductos() {
        try (Connection conn = ConexionBD.obtenerConexion()) {
            String sql = "SELECT id, nombre, descripcion, precio, stock FROM productos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                };
                modeloTabla.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarTabla() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1)); // Filtrar por nombre
        }
    }
}