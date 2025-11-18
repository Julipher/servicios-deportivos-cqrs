package com.deportes.shared.events;

import java.time.LocalDateTime;

public class ReservaCreatedEvent {
    private String reservaId;
    private String clienteId;
    private String deporte;
    private String fecha;
    private String entrenador;
    private Integer horas;
    private Double monto;
    private LocalDateTime timestamp;

    public ReservaCreatedEvent() {}

    public ReservaCreatedEvent(String reservaId, String clienteId, String deporte, String fecha,
                               String entrenador, Integer horas, Double monto, LocalDateTime timestamp) {
        this.reservaId = reservaId;
        this.clienteId = clienteId;
        this.deporte = deporte;
        this.fecha = fecha;
        this.entrenador = entrenador;
        this.horas = horas;
        this.monto = monto;
        this.timestamp = timestamp;
    }

    public String getReservaId() { return reservaId; }
    public void setReservaId(String reservaId) { this.reservaId = reservaId; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getDeporte() { return deporte; }
    public void setDeporte(String deporte) { this.deporte = deporte; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEntrenador() { return entrenador; }
    public void setEntrenador(String entrenador) { this.entrenador = entrenador; }

    public Integer getHoras() { return horas; }
    public void setHoras(Integer horas) { this.horas = horas; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
