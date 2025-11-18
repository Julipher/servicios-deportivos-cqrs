package com.deportes.command.service;

import com.deportes.command.dto.CrearReservaCommand;
import com.deportes.command.dto.PagarReservaCommand;
import com.deportes.command.entity.Reserva;
import com.deportes.command.repository.ReservaRepository;
import com.deportes.shared.events.ReservaCreatedEvent;
import com.deportes.shared.events.ReservaPagadaEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReservaCommandService {
    private final ReservaRepository reservaRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_RESERVA_CREADA = "reserva-creada";
    private static final String TOPIC_RESERVA_PAGADA = "reserva-pagada";

    public ReservaCommandService(ReservaRepository reservaRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.reservaRepository = reservaRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Reserva crearReserva(CrearReservaCommand command) {
        System.out.println("Creando reserva para cliente: " + command.getClienteId());

        Double monto = calcularMonto(command.getDeporte(), command.getHoras());

        Reserva reserva = new Reserva();
        reserva.setReservaId("RES-" + UUID.randomUUID().toString());
        reserva.setClienteId(command.getClienteId());
        reserva.setDeporte(command.getDeporte());
        reserva.setFecha(command.getFecha());
        reserva.setEntrenador(command.getEntrenador());
        reserva.setHoras(command.getHoras());
        reserva.setMonto(monto);

        Reserva reservaGuardada = reservaRepository.save(reserva);
        System.out.println("Reserva creada: " + reservaGuardada.getReservaId());

        ReservaCreatedEvent event = new ReservaCreatedEvent(
                reservaGuardada.getReservaId(),
                reservaGuardada.getClienteId(),
                reservaGuardada.getDeporte(),
                reservaGuardada.getFecha(),
                reservaGuardada.getEntrenador(),
                reservaGuardada.getHoras(),
                reservaGuardada.getMonto(),
                LocalDateTime.now()
        );

        kafkaTemplate.send(TOPIC_RESERVA_CREADA, event);
        System.out.println("Evento de creación publicado: " + reservaGuardada.getReservaId());

        return reservaGuardada;
    }

    @Transactional
    public void pagarReserva(PagarReservaCommand command) {
        System.out.println("Procesando pago para reserva: " + command.getReservaId());

        Reserva reserva = reservaRepository.findByReservaId(command.getReservaId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (reserva.getEstado() != Reserva.EstadoReserva.PENDIENTE) {
            throw new RuntimeException("Reserva no está en estado PENDIENTE");
        }

        reserva.setEstado(Reserva.EstadoReserva.PAGADA);
        reserva.setFechaPago(LocalDateTime.now());
        reservaRepository.save(reserva);

        ReservaPagadaEvent event = new ReservaPagadaEvent(
                reserva.getReservaId(),
                reserva.getMonto(),
                command.getMetodoPago(),
                LocalDateTime.now(),
                "PAGADA"
        );

        kafkaTemplate.send(TOPIC_RESERVA_PAGADA, event);
        System.out.println("Evento de pago publicado: " + reserva.getReservaId());
    }

    private Double calcularMonto(String deporte, Integer horas) {
        Double tarifaPorHora = switch(deporte) {
            case "Fútbol" -> 50.0;
            case "Tenis" -> 60.0;
            case "Natación" -> 45.0;
            case "Basketball" -> 55.0;
            default -> 50.0;
        };
        return tarifaPorHora * horas;
    }
}
