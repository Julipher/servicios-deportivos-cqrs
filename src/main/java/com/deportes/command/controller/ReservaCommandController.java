package com.deportes.command.controller;

import com.deportes.command.dto.CrearReservaCommand;
import com.deportes.command.dto.PagarReservaCommand;
import com.deportes.command.entity.Reserva;
import com.deportes.command.service.ReservaCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/command/reservas")
@CrossOrigin(origins = "*")
public class ReservaCommandController {
    private final ReservaCommandService reservaCommandService;

    public ReservaCommandController(ReservaCommandService reservaCommandService) {
        this.reservaCommandService = reservaCommandService;
    }

    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@Valid @RequestBody CrearReservaCommand command) {
        System.out.println("POST /api/command/reservas - Crear reserva");
        Reserva reserva = reservaCommandService.crearReserva(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    @PostMapping("/pagar")
    public ResponseEntity<String> pagarReserva(@Valid @RequestBody PagarReservaCommand command) {
        System.out.println("POST /api/command/reservas/pagar - Pagar reserva");
        reservaCommandService.pagarReserva(command);
        return ResponseEntity.ok("Pago procesado correctamente");
    }
}
