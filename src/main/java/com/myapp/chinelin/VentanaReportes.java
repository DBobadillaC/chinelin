package com.myapp.chinelin;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VentanaReportes extends JFrame {

    public VentanaReportes() {
        setTitle("Reportes de Ventas");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Reportes Generales de Ventas", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // Panel de gráficos
        JPanel panelGraficos = new JPanel(new GridLayout(1, 2));
        panelGraficos.add(new ChartPanel(generarGraficoBarrasProductos()));
        panelGraficos.add(new ChartPanel(generarGraficoLineasGanancias()));
        add(panelGraficos, BorderLayout.CENTER);

        // Panel estadísticas
        JPanel panelEstadisticas = new JPanel(new GridLayout(1, 3, 20, 10));
        panelEstadisticas.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTotalVentas = new JLabel("Total de Ventas: " + obtenerTotalVentas());
        JLabel lblProductosVendidos = new JLabel("Productos Vendidos: " + obtenerTotalProductosVendidos());
        JLabel lblIngresosTotales = new JLabel("Ingresos Totales: S/ " + obtenerIngresosTotales());

        for (JLabel lbl : new JLabel[]{lblTotalVentas, lblProductosVendidos, lblIngresosTotales}) {
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panelEstadisticas.add(lbl);
        }

        add(panelEstadisticas, BorderLayout.SOUTH);
    }

    // Gráfico de barras: Productos vs cantidad vendida
    private JFreeChart generarGraficoBarrasProductos() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sql = "SELECT p.nombre, SUM(dv.cantidad) as total " +
                     "FROM detalle_venta dv " +
                     "JOIN productos p ON dv.producto_id = p.id " +
                     "GROUP BY p.nombre";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dataset.addValue(rs.getInt("total"), "Ventas", rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ChartFactory.createBarChart(
                "Ventas por Producto",
                "Producto",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
    }

    // Gráfico de líneas: Día del mes actual vs ingresos
 private JFreeChart generarGraficoLineasGanancias() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    Map<String, Double> ingresosPorDia = new LinkedHashMap<>();

    // Generar los últimos 7 días como claves con 0.0 por defecto
    LocalDate hoy = LocalDate.now();
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM");

    for (int i = 6; i >= 0; i--) {
        LocalDate fecha = hoy.minusDays(i);
        ingresosPorDia.put(fecha.format(formato), 0.0);
    }

    // Consulta para obtener ingresos por día (últimos 7 días)
    String sql = "SELECT DATE(fecha) AS dia, SUM(total) AS ingresos " +
                 "FROM ventas " +
                 "WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                 "GROUP BY DATE(fecha)";

    try (Connection conn = ConexionBD.obtenerConexion();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            LocalDate fecha = rs.getDate("dia").toLocalDate();
            String diaFormato = fecha.format(formato);
            double ingresos = rs.getDouble("ingresos");
            ingresosPorDia.put(diaFormato, ingresos);  // reemplaza si hay datos
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Agregar los datos al dataset
    for (Map.Entry<String, Double> entry : ingresosPorDia.entrySet()) {
        dataset.addValue(entry.getValue(), "Ganancias", entry.getKey());
    }

    return ChartFactory.createBarChart(
            "Ganancias Diarias (últimos 7 días)",
            "Día",
            "Ingresos (S/.)",
            dataset
    );
}


    private int obtenerTotalVentas() {
        return obtenerEntero("SELECT COUNT(*) FROM ventas");
    }

    private int obtenerTotalProductosVendidos() {
        return obtenerEntero("SELECT SUM(cantidad) FROM detalle_venta");
    }

    private double obtenerIngresosTotales() {
        return obtenerDecimal("SELECT SUM(total) FROM ventas");
    }

    private int obtenerEntero(String sql) {
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double obtenerDecimal(String sql) {
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
