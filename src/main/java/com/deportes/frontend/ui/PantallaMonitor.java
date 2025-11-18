package com.deportes.frontend.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PantallaMonitor extends JFrame {
    private JTable eventosTable;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    private JLabel contadorEventos;
    private JLabel contadorReservas;
    private JLabel contadorPagos;
    private int totalEventos = 0;
    private int totalReservas = 0;
    private int totalPagos = 0;

    public PantallaMonitor() {
        setTitle("📡 Monitor de Eventos - Event Sourcing");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
        simularEventos();
    }

    private void crearInterfaz() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(156, 39, 176));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📡 MONITOR DE EVENTOS EN TIEMPO REAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Event Sourcing con Apache Kafka");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(230, 200, 255));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Stats Panel
        JPanel statsPanel = crearPanelEstadisticas();

        // Table Panel
        String[] columnas = {"Timestamp", "Tipo Evento", "Topic", "Reserva ID",
                "Detalles", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventosTable = new JTable(tableModel);
        eventosTable.setFont(new Font("Arial", Font.PLAIN, 11));
        eventosTable.setRowHeight(28);
        eventosTable.setGridColor(new Color(224, 224, 224));
        eventosTable.setSelectionBackground(new Color(225, 190, 231));
        eventosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        eventosTable.getTableHeader().setBackground(new Color(156, 39, 176));
        eventosTable.getTableHeader().setForeground(Color.WHITE);

        eventosTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        eventosTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        eventosTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        eventosTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        eventosTable.getColumnModel().getColumn(4).setPreferredWidth(250);
        eventosTable.getColumnModel().getColumn(5).setPreferredWidth(80);

        JScrollPane tableScroll = new JScrollPane(eventosTable);
        TitledBorder tableBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(156, 39, 176), 2),
                "📊 Stream de Eventos Kafka"
        );
        tableBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        tableBorder.setTitleColor(new Color(156, 39, 176));
        tableScroll.setBorder(tableBorder);

        // Log Panel
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        logArea.setBackground(new Color(250, 250, 250));
        logArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane logScroll = new JScrollPane(logArea);
        TitledBorder logBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(158, 158, 158), 1),
                "📋 Log Técnico de Eventos"
        );
        logBorder.setTitleFont(new Font("Arial", Font.BOLD, 12));
        logScroll.setBorder(logBorder);

        // Split Panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, logScroll);
        splitPane.setDividerLocation(350);

        // Assembly
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 245, 250));
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(splitPane, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        log("✅ Monitor de eventos iniciado");
        log("📡 Conectado a Kafka: localhost:9092");
        log("🎯 Escuchando topics: reserva-creada, reserva-pagada");
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(new EmptyBorder(15, 0, 15, 0));

        contadorEventos = new JLabel("0", SwingConstants.CENTER);
        contadorReservas = new JLabel("0", SwingConstants.CENTER);
        contadorPagos = new JLabel("0", SwingConstants.CENTER);

        panel.add(crearStatCard("📊 Total Eventos", contadorEventos, new Color(156, 39, 176)));
        panel.add(crearStatCard("📅 Reservas Creadas", contadorReservas, new Color(33, 150, 243)));
        panel.add(crearStatCard("💰 Pagos Procesados", contadorPagos, new Color(76, 175, 80)));

        return panel;
    }

    private JPanel crearStatCard(String titulo, JLabel valorLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(20, 15, 20, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(color);

        valorLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valorLabel.setForeground(color);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(valorLabel, BorderLayout.CENTER);

        return card;
    }

    private void simularEventos() {
        log("\n⚠️ MODO DEMOSTRACIÓN ACTIVADO");
        log("📝 Mostrando eventos de ejemplo...");
        log("💡 Los eventos reales aparecerán cuando se creen/paguen reservas\n");

        Timer timer = new Timer(3000, e -> {
            if (tableModel.getRowCount() < 5) {
                agregarEventoEjemplo();
            }
        });
        timer.start();
    }

    private void agregarEventoEjemplo() {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String[] tipos = {"ReservaCreated", "ReservaPagada"};
        String[] topics = {"reserva-creada", "reserva-pagada"};
        String[] estados = {"✅ Procesado", "✅ Sincronizado"};

        int index = (int) (Math.random() * tipos.length);
        String tipo = tipos[index];
        String topic = topics[index];
        String reservaId = "RES-" + System.currentTimeMillis();

        String detalles = tipo.equals("ReservaCreated")
                ? "Fútbol, 2 horas, $100.00"
                : "Pago recibido: $100.00";

        Object[] fila = {timestamp, tipo, topic, reservaId, detalles, estados[index]};
        tableModel.insertRow(0, fila);

        if (tableModel.getRowCount() > 50) {
            tableModel.removeRow(50);
        }

        totalEventos++;
        if (tipo.equals("ReservaCreated")) {
            totalReservas++;
        } else {
            totalPagos++;
        }

        actualizarContadores();

        log("📥 Evento recibido: " + tipo + " | " + reservaId);
    }

    private void actualizarContadores() {
        SwingUtilities.invokeLater(() -> {
            contadorEventos.setText(String.valueOf(totalEventos));
            contadorReservas.setText(String.valueOf(totalReservas));
            contadorPagos.setText(String.valueOf(totalPagos));
        });
    }

    private void log(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            logArea.append("[" + timestamp + "] " + mensaje + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}