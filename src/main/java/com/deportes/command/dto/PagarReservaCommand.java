package com.deportes.command.dto;

import jakarta.validation.constraints.NotBlank;

public class PagarReservaCommand {
    @NotBlank(message = "ID de reserva requerido")
    private String reservaId;

    @NotBlank(message = "Método de pago requerido")
    private String metodoPago;

    public PagarReservaCommand() {}

    public PagarReservaCommand(String reservaId, String metodoPago) {
        this.reservaId = reservaId;
        this.metodoPago = metodoPago;
    }

    public String getReservaId() { return reservaId; }
    public void setReservaId(String reservaId) { this.reservaId = reservaId; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}
