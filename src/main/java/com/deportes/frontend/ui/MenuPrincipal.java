package com.deportes.frontend.ui;

import com.deportes.frontend.FrontendApplication;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("🏃 Sistema de Reservas Deportivas - CQRS");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
    }

    private void crearInterfaz() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(63, 81, 181));
        headerPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel titleLabel = new JLabel("🏃 SISTEMA DE RESERVAS DEPORTIVAS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Arquitectura CQRS con Event Sourcing");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(200, 200, 255));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Status Panel
        JPanel statusPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statusPanel.setBackground(new Color(245, 245, 250));
        statusPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        statusPanel.add(crearStatusCard("🟢 Command Service", "Puerto 8080", new Color(76, 175, 80)));
        statusPanel.add(crearStatusCard("🟢 Query Service", "Puerto 8081", new Color(33, 150, 243)));
        statusPanel.add(crearStatusCard("🟢 Kafka Bus", "Eventos Activos", new Color(255, 152, 0)));

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        JButton btnCliente = crearBotonMenu("👤 GESTIÓN DE RESERVAS",
                "Crear y gestionar reservas deportivas",
                new Color(33, 150, 243), new Color(25, 118, 210));
        btnCliente.addActionListener(e -> abrirGestionReservas());

        JButton btnConsultas = crearBotonMenu("📊 CONSULTAS Y REPORTES",
                "Ver reservas, estadísticas y reportes",
                new Color(76, 175, 80), new Color(56, 142, 60));
        btnConsultas.addActionListener(e -> abrirConsultas());

        JButton btnMonitor = crearBotonMenu("📡 MONITOR DE EVENTOS",
                "Visualizar eventos en tiempo real",
                new Color(156, 39, 176), new Color(123, 31, 162));
        btnMonitor.addActionListener(e -> abrirMonitor());

        JButton btnConfig = crearBotonMenu("⚙️ CONFIGURACIÓN",
                "Ajustes del sistema y conexiones",
                new Color(255, 152, 0), new Color(245, 124, 0));
        btnConfig.addActionListener(e -> abrirConfiguracion());

        buttonsPanel.add(btnCliente, gbc);
        buttonsPanel.add(btnConsultas, gbc);
        buttonsPanel.add(btnMonitor, gbc);
        buttonsPanel.add(btnConfig, gbc);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footerPanel.setBackground(new Color(245, 245, 250));

        JButton btnSalir = new JButton("🚪 Salir del Sistema");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(new Color(244, 67, 54));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setPreferredSize(new Dimension(200, 40));
        btnSalir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea cerrar el sistema?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                FrontendApplication.shutdown();
            }
        });

        footerPanel.add(btnSalir);

        // Assembly
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(statusPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel crearStatusCard(String titulo, String detalle, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(color);

        JLabel lblDetalle = new JLabel(detalle, SwingConstants.CENTER);
        lblDetalle.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDetalle.setForeground(Color.GRAY);

        card.add(lblTitulo, BorderLayout.CENTER);
        card.add(lblDetalle, BorderLayout.SOUTH);

        return card;
    }

    private JButton crearBotonMenu(String texto, String descripcion, Color colorNormal, Color colorHover) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(10, 5));
        btn.setBackground(colorNormal);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(500, 70));

        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(new Font("Arial", Font.BOLD, 18));
        lblTexto.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(230, 230, 230));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        textPanel.add(lblTexto, BorderLayout.NORTH);
        textPanel.add(lblDesc, BorderLayout.CENTER);

        btn.add(textPanel, BorderLayout.CENTER);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(colorHover);
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(colorNormal);
            }
        });

        return btn;
    }

    private void abrirGestionReservas() {
        new PantallaReservas().setVisible(true);
    }

    private void abrirConsultas() {
        new PantallaConsultas().setVisible(true);
    }

    private void abrirMonitor() {
        new PantallaMonitor().setVisible(true);
    }

    private void abrirConfiguracion() {
        JOptionPane.showMessageDialog(this,
                "🔧 Configuración del Sistema\n\n" +
                        "Command Service: http://localhost:8080\n" +
                        "Query Service: http://localhost:8081\n" +
                        "Kafka: localhost:9092\n" +
                        "PostgreSQL: localhost:5433\n" +
                        "MongoDB: localhost:27018",
                "Información de Configuración",
                JOptionPane.INFORMATION_MESSAGE);
    }
}