package com.deportes.frontend;

import com.deportes.frontend.ui.MenuPrincipal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;

@SpringBootApplication
@ComponentScan(basePackages = {"com.deportes"})
public class FrontendApplication {

    private static ConfigurableApplicationContext commandContext;
    private static ConfigurableApplicationContext queryContext;

    public static void main(String[] args) {
        try {
            // Configurar Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            System.out.println("=================================================");
            System.out.println("    INICIANDO SISTEMA CQRS - SERVICIOS DEPORTIVOS");
            System.out.println("=================================================\n");

            // Iniciar Command Service en un hilo separado
            Thread commandThread = new Thread(() -> {
                System.out.println("🚀 Iniciando Command Service (Puerto 8080)...");
                System.setProperty("spring.profiles.active", "command");
                commandContext = SpringApplication.run(com.deportes.CqrsApplication.class, args);
                System.out.println("✅ Command Service iniciado correctamente\n");
            });
            commandThread.setDaemon(false);
            commandThread.start();

            // Esperar 3 segundos antes de iniciar Query Service
            Thread.sleep(15000);

            // Iniciar Query Service en un hilo separado
            Thread queryThread = new Thread(() -> {
                System.out.println("🚀 Iniciando Query Service (Puerto 8081)...");
                System.setProperty("spring.profiles.active", "query");
                queryContext = SpringApplication.run(com.deportes.CqrsApplication.class,
                        "--server.port=8081",
                        "--spring.profiles.active=query");
                System.out.println("✅ Query Service iniciado correctamente\n");
            });
            queryThread.setDaemon(false);
            queryThread.start();

            // Esperar a que ambos servicios estén listos
            Thread.sleep(5000);

            // Iniciar interfaz gráfica
            SwingUtilities.invokeLater(() -> {
                System.out.println("🎨 Iniciando Interfaz Gráfica...");
                System.out.println("=================================================\n");
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            });

        } catch (Exception e) {
            System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void shutdown() {
        System.out.println("\n🛑 Cerrando servicios...");
        if (commandContext != null) {
            commandContext.close();
        }
        if (queryContext != null) {
            queryContext.close();
        }
        System.out.println("✅ Servicios cerrados correctamente");
        System.exit(0);
    }
}