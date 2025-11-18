package com.deportes.query.controller;

import com.deportes.query.dto.ReservaDto;
import com.deportes.query.service.ReservaQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/query/reservas")
@CrossOrigin(origins = "*")
public class ReservaQueryController {
    private final ReservaQueryService reservaQueryService;

    public ReservaQueryController(ReservaQueryService reservaQueryService) {
        this.reservaQueryService = reservaQueryService;
    }

    @GetMapping
    public ResponseEntity<List<ReservaDto>> obtenerTodasLasReservas() {
        System.out.println("GET /api/query/reservas - Obtener todas las reservas");
        return ResponseEntity.ok(reservaQueryService.obtenerTodasLasReservas());
    }

    @GetMapping("/{reservaId}")
    public ResponseEntity<ReservaDto> obtenerReservaPorId(@PathVariable String reservaId) {
        System.out.println("GET /api/query/reservas/" + reservaId);
        Optional<ReservaDto> reserva = reservaQueryService.obtenerReservaPorId(reservaId);
        return reserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ReservaDto>> obtenerReservasPorCliente(@PathVariable String clienteId) {
        System.out.println("GET /api/query/reservas/cliente/" + clienteId);
        return ResponseEntity.ok(reservaQueryService.obtenerReservasPorCliente(clienteId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaDto>> obtenerReservasPorEstado(@PathVariable String estado) {
        System.out.println("GET /api/query/reservas/estado/" + estado);
        return ResponseEntity.ok(reservaQueryService.obtenerReservasPorEstado(estado));
    }
}