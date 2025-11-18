package com.deportes.query.service;

import com.deportes.query.dto.ReservaDto;
import com.deportes.query.model.ReservaQueryModel;
import com.deportes.query.repository.ReservaQueryRepository;
import com.deportes.shared.events.ReservaCreatedEvent;
import com.deportes.shared.events.ReservaPagadaEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaQueryService {
    private final ReservaQueryRepository reservaQueryRepository;

    public ReservaQueryService(ReservaQueryRepository reservaQueryRepository) {
        this.reservaQueryRepository = reservaQueryRepository;
    }

    public List<ReservaDto> obtenerTodasLasReservas() {
        System.out.println("Obteniendo todas las reservas");
        return reservaQueryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ReservaDto> obtenerReservaPorId(String reservaId) {
        System.out.println("Obteniendo reserva: " + reservaId);
        return reservaQueryRepository.findByReservaId(reservaId)
                .map(this::toDto);
    }

    public List<ReservaDto> obtenerReservasPorCliente(String clienteId) {
        System.out.println("Obteniendo reservas del cliente: " + clienteId);
        return reservaQueryRepository.findByClienteId(clienteId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ReservaDto> obtenerReservasPorEstado(String estado) {
        System.out.println("Obteniendo reservas con estado: " + estado);
        return reservaQueryRepository.findByEstado(estado).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @KafkaListener(topics = "reserva-creada", groupId = "reserva-query-group")
    public void procesarReservaCreada(ReservaCreatedEvent event) {
        System.out.println("Evento recibido: ReservaCreated - " + event.getReservaId());

        ReservaQueryModel modelo = new ReservaQueryModel();
        modelo.setReservaId(event.getReservaId());
        modelo.setClienteId(event.getClienteId());
        modelo.setDeporte(event.getDeporte());
        modelo.setFecha(event.getFecha());
        modelo.setEntrenador(event.getEntrenador());
        modelo.setHoras(event.getHoras());
        modelo.setMonto(event.getMonto());
        modelo.setEstado("PENDIENTE");
        modelo.setFechaCreacion(event.getTimestamp());

        reservaQueryRepository.save(modelo);
        System.out.println("Modelo de lectura actualizado para reserva: " + event.getReservaId());
    }

    @KafkaListener(topics = "reserva-pagada", groupId = "reserva-query-group")
    public void procesarReservaPagada(ReservaPagadaEvent event) {
        System.out.println("Evento recibido: ReservaPagada - " + event.getReservaId());

        Optional<ReservaQueryModel> opcional = reservaQueryRepository.findByReservaId(event.getReservaId());
        if (opcional.isPresent()) {
            ReservaQueryModel modelo = opcional.get();
            modelo.setEstado(event.getEstado());
            modelo.setFechaPago(event.getTimestamp());
            reservaQueryRepository.save(modelo);
            System.out.println("Reserva actualizada como pagada: " + event.getReservaId());
        }
    }

    private ReservaDto toDto(ReservaQueryModel modelo) {
        ReservaDto dto = new ReservaDto();
        dto.setReservaId(modelo.getReservaId());
        dto.setClienteId(modelo.getClienteId());
        dto.setDeporte(modelo.getDeporte());
        dto.setFecha(modelo.getFecha());
        dto.setEntrenador(modelo.getEntrenador());
        dto.setHoras(modelo.getHoras());
        dto.setMonto(modelo.getMonto());
        dto.setEstado(modelo.getEstado());
        dto.setFechaCreacion(modelo.getFechaCreacion());
        dto.setFechaPago(modelo.getFechaPago());
        return dto;
    }
}