package com.deportes.frontend.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PantallaConsultas extends JFrame {
    private JTable reservasTable;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    private JComboBox<String> filtroCombo;
    private JTextField buscarField;
    private ObjectMapper objectMapper;

    private static final String QUERY_URL = "http://localhost:8081/api/query/reservas";

    public PantallaConsultas() {
        objectMapper = new ObjectMapper();

        setTitle("📊 Consultas y Reportes - Query Side");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
        cargarReservas();
    }

    private void crearInterfaz() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(76, 175, 80));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📊 CONSULTAS Y REPORTES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Query Side - Lectura Optimizada");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 255, 200));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Filter Panel
        JPanel filterPanel = crearPanelFiltros();

        // Table Panel
        String[] columnas = {"ID Reserva", "Cliente", "Deporte", "Fecha", "Entrenador",
                "Horas", "Monto", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservasTable = new JTable(tableModel);
        reservasTable.setFont(new Font("Arial", Font.PLAIN, 12));
        reservasTable.setRowHeight(30);
        reservasTable.setGridColor(new Color(224, 224, 224));
        reservasTable.setSelectionBackground(new Color(200, 230, 201));
        reservasTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        reservasTable.getTableHeader().setBackground(new Color(76, 175, 80));
        reservasTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScroll = new JScrollPane(reservasTable);
        TitledBorder tableBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
                "📋 Listado de Reservas"
        );
        tableBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        tableBorder.setTitleColor(new Color(76, 175, 80));
        tableScroll.setBorder(tableBorder);

        // Log Panel
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logArea.setBackground(new Color(250, 250, 250));
        logArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane logScroll = new JScrollPane(logArea);
        TitledBorder logBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(158, 158, 158), 1),
                "📋 Registro de Consultas"
        );
        logBorder.setTitleFont(new Font("Arial", Font.BOLD, 12));
        logScroll.setBorder(logBorder);

        // Split Panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, logScroll);
        splitPane.setDividerLocation(400);

        // Assembly
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(splitPane, BorderLayout.SOUTH);

        add(mainPanel);

        log("✅ Pantalla de consultas iniciada");
        log("🔗 Conectado a Query Service: " + QUERY_URL);
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblFiltro = new JLabel("🔍 Filtrar por:");
        lblFiltro.setFont(new Font("Arial", Font.BOLD, 13));

        filtroCombo = new JComboBox<>(new String[]{
                "Todas", "Por Cliente", "Por Estado", "Por Deporte"
        });
        filtroCombo.setFont(new Font("Arial", Font.PLAIN, 13));

        buscarField = new JTextField(20);
        buscarField.setFont(new Font("Arial", Font.PLAIN, 13));
        buscarField.setToolTipText("Ingrese el valor a buscar");

        JButton btnBuscar = crearBoton("🔍 Buscar", new Color(76, 175, 80));
        btnBuscar.addActionListener(e -> realizarBusqueda());

        JButton btnRefrescar = crearBoton("🔄 Actualizar", new Color(33, 150, 243));
        btnRefrescar.addActionListener(e -> cargarReservas());

        JButton btnExportar = crearBoton("📥 Exportar", new Color(255, 152, 0));
        btnExportar.addActionListener(e -> exportarDatos());

        panel.add(lblFiltro);
        panel.add(filtroCombo);
        panel.add(buscarField);
        panel.add(btnBuscar);
        panel.add(btnRefrescar);
        panel.add(btnExportar);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(130, 35));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cargarReservas() {
        try {
            log("\n📥 Cargando reservas desde Query Service...");
            tableModel.setRowCount(0);

            URL url = new URL(QUERY_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonNode reservas = objectMapper.readTree(response.toString());

                for (JsonNode reserva : reservas) {
                    Object[] fila = {
                            reserva.get("reservaId").asText(),
                            reserva.get("clienteId").asText(),
                            reserva.get("deporte").asText(),
                            reserva.get("fecha").asText(),
                            reserva.get("entrenador").asText(),
                            reserva.get("horas").asInt() + " hrs",
                            "$" + String.format("%.2f", reserva.get("monto").asDouble()),
                            reserva.get("estado").asText()
                    };
                    tableModel.addRow(fila);
                }

                log("✅ Cargadas " + reservas.size() + " reservas");
                log("📊 Datos obtenidos de MongoDB (Query Side)");

            } else {
                log("❌ Error al cargar reservas: " + responseCode);
            }

            conn.disconnect();

        } catch (Exception e) {
            log("❌ Excepción: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar reservas: " + e.getMessage() +
                            "\n\nAsegúrese de que el Query Service esté activo.",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarBusqueda() {
        String filtro = (String) filtroCombo.getSelectedItem();
        String valor = buscarField.getText().trim();

        if (filtro.equals("Todas")) {
            cargarReservas();
            return;
        }

        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un valor de búsqueda",
                    "Campo Requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            log("\n🔍 Realizando búsqueda: " + filtro + " = " + valor);
            tableModel.setRowCount(0);

            String endpoint = QUERY_URL;
            if (filtro.equals("Por Cliente")) {
                endpoint += "/cliente/" + valor;
            } else if (filtro.equals("Por Estado")) {
                endpoint += "/estado/" + valor;
            }

            log("🔗 Endpoint: " + endpoint);

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonNode reservas = objectMapper.readTree(response.toString());

                for (JsonNode reserva : reservas) {
                    Object[] fila = {
                            reserva.get("reservaId").asText(),
                            reserva.get("clienteId").asText(),
                            reserva.get("deporte").asText(),
                            reserva.get("fecha").asText(),
                            reserva.get("entrenador").asText(),
                            reserva.get("horas").asInt() + " hrs",
                            "$" + String.format("%.2f", reserva.get("monto").asDouble()),
                            reserva.get("estado").asText()
                    };
                    tableModel.addRow(fila);
                }

                log("✅ Encontradas " + reservas.size() + " reservas");

            } else {
                log("❌ Error en búsqueda: " + responseCode);
            }

            conn.disconnect();

        } catch (Exception e) {
            log("❌ Excepción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void exportarDatos() {
        StringBuilder csv = new StringBuilder();
        csv.append("ID Reserva,Cliente,Deporte,Fecha,Entrenador,Horas,Monto,Estado\n");

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                csv.append(tableModel.getValueAt(i, j));
                if (j < tableModel.getColumnCount() - 1) {
                    csv.append(",");
                }
            }
            csv.append("\n");
        }

        JTextArea textArea = new JTextArea(csv.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Datos Exportados (CSV)", JOptionPane.INFORMATION_MESSAGE);

        log("📥 Datos exportados a formato CSV");
    }

    private void log(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            logArea.append("[" + timestamp + "] " + mensaje + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}