package com.deportes.shared.events;

import java.time.LocalDateTime;

public class ReservaPagadaEvent {
    private String reservaId;
    private Double monto;
    private String metodoPago;
    private LocalDateTime timestamp;
    private String estado;

    public ReservaPagadaEvent() {}

    public ReservaPagadaEvent(String reservaId, Double monto, String metodoPago, LocalDateTime timestamp, String estado) {
        this.reservaId = reservaId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.timestamp = timestamp;
        this.estado = estado;
    }

    public String getReservaId() { return reservaId; }
    public void setReservaId(String reservaId) { this.reservaId = reservaId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
