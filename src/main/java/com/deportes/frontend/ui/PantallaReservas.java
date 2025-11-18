package com.deportes.frontend.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PantallaReservas extends JFrame {
    private JComboBox<String> deporteCombo;
    private JTextField fechaField;
    private JComboBox<String> entrenadorCombo;
    private JSpinner horasSpinner;
    private JTextField clienteIdField;
    private JLabel montoLabel;
    private JTextArea logArea;
    private ObjectMapper objectMapper;

    private static final String COMMAND_URL = "http://localhost:8080/api/command/reservas";

    public PantallaReservas() {
        objectMapper = new ObjectMapper();

        setTitle("👤 Gestión de Reservas - Command Side");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
    }

    private void crearInterfaz() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("👤 GESTIÓN DE RESERVAS DEPORTIVAS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Command Side - Escritura de Datos");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 230, 255));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Form Panel
        JPanel formPanel = crearFormulario();

        // Log Panel
        logArea = new JTextArea(12, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logArea.setBackground(new Color(250, 250, 250));
        logArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane logScroll = new JScrollPane(logArea);
        TitledBorder logBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(158, 158, 158), 1),
                "📋 Registro de Operaciones"
        );
        logBorder.setTitleFont(new Font("Arial", Font.BOLD, 12));
        logScroll.setBorder(logBorder);

        // Assembly
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(logScroll, BorderLayout.SOUTH);

        add(mainPanel);

        log("✅ Pantalla de reservas iniciada");
        log("🔗 Conectado a Command Service: " + COMMAND_URL);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
                "📝 Formulario de Nueva Reserva"
        );
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        border.setTitleColor(new Color(33, 150, 243));
        panel.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(15, 15, 15, 15)));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cliente ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        fieldsPanel.add(crearLabel("👤 Cliente ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        clienteIdField = new JTextField("CLI-" + System.currentTimeMillis());
        clienteIdField.setFont(new Font("Arial", Font.PLAIN, 13));
        fieldsPanel.add(clienteIdField, gbc);

        // Deporte
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        fieldsPanel.add(crearLabel("⚽ Deporte:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        deporteCombo = new JComboBox<>(new String[]{"Fútbol", "Tenis", "Natación", "Basketball"});
        deporteCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        deporteCombo.addActionListener(e -> calcularMonto());
        fieldsPanel.add(deporteCombo, gbc);

        // Fecha
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        fieldsPanel.add(crearLabel("📅 Fecha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fechaField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        fechaField.setFont(new Font("Arial", Font.PLAIN, 13));
        fieldsPanel.add(fechaField, gbc);

        // Entrenador
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        fieldsPanel.add(crearLabel("👨‍🏫 Entrenador:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        entrenadorCombo = new JComboBox<>(new String[]{
                "Carlos Pérez", "María García", "Juan Rodríguez", "Ana Martínez", "Luis Fernández"
        });
        entrenadorCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        fieldsPanel.add(entrenadorCombo, gbc);

        // Horas
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        fieldsPanel.add(crearLabel("⏰ Horas:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        horasSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        horasSpinner.setFont(new Font("Arial", Font.PLAIN, 13));
        horasSpinner.addChangeListener(e -> calcularMonto());
        fieldsPanel.add(horasSpinner, gbc);

        // Monto
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel montoPanel = new JPanel(new BorderLayout());
        montoPanel.setBackground(new Color(232, 245, 233));
        montoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel montoTitleLabel = new JLabel("💰 MONTO TOTAL:", SwingConstants.CENTER);
        montoTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        montoLabel = new JLabel("$50.00", SwingConstants.CENTER);
        montoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        montoLabel.setForeground(new Color(76, 175, 80));

        montoPanel.add(montoTitleLabel, BorderLayout.NORTH);
        montoPanel.add(montoLabel, BorderLayout.CENTER);
        fieldsPanel.add(montoPanel, gbc);

        panel.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonsPanel.setBackground(Color.WHITE);

        JButton btnCrear = crearBoton("📅 Crear Reserva", new Color(33, 150, 243));
        btnCrear.addActionListener(e -> crearReserva());

        JButton btnLimpiar = crearBoton("🔄 Limpiar", new Color(158, 158, 158));
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        buttonsPanel.add(btnCrear);
        buttonsPanel.add(btnLimpiar);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        return label;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void calcularMonto() {
        String deporte = (String) deporteCombo.getSelectedItem();
        int horas = (int) horasSpinner.getValue();

        double tarifaPorHora = switch(deporte) {
            case "Fútbol" -> 50.0;
            case "Tenis" -> 60.0;
            case "Natación" -> 45.0;
            case "Basketball" -> 55.0;
            default -> 50.0;
        };

        double monto = tarifaPorHora * horas;
        montoLabel.setText(String.format("$%.2f", monto));
    }

    private void crearReserva() {
        try {
            log("\n🚀 Creando nueva reserva...");

            // Crear JSON del comando
            String json = String.format(
                    "{\"clienteId\":\"%s\",\"deporte\":\"%s\",\"fecha\":\"%s\"," +
                            "\"entrenador\":\"%s\",\"horas\":%d}",
                    clienteIdField.getText(),
                    deporteCombo.getSelectedItem(),
                    fechaField.getText(),
                    entrenadorCombo.getSelectedItem(),
                    horasSpinner.getValue()
            );

            log("📤 Enviando comando al Command Service...");
            log("🔗 URL: " + COMMAND_URL);
            log("📋 Payload: " + json);

            // Enviar petición HTTP
            URL url = new URL(COMMAND_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 201 || responseCode == 200) {
                log("✅ Reserva creada exitosamente");
                log("📊 Estado: " + responseCode + " - " + conn.getResponseMessage());
                log("🎉 Evento publicado en Kafka");

                JOptionPane.showMessageDialog(this,
                        "✅ Reserva creada exitosamente\n\n" +
                                "La reserva ha sido procesada y el evento\n" +
                                "ha sido publicado en el bus de eventos.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
            } else {
                log("❌ Error al crear reserva: " + responseCode);
                JOptionPane.showMessageDialog(this,
                        "Error al crear la reserva\nCódigo: " + responseCode,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            conn.disconnect();

        } catch (Exception e) {
            log("❌ Excepción: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage() +
                            "\n\nAsegúrese de que el Command Service esté activo.",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        clienteIdField.setText("CLI-" + System.currentTimeMillis());
        deporteCombo.setSelectedIndex(0);
        fechaField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        entrenadorCombo.setSelectedIndex(0);
        horasSpinner.setValue(1);
        calcularMonto();
        log("🔄 Formulario limpiado");
    }

    private void log(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            logArea.append("[" + timestamp + "] " + mensaje + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}