package com.deportes.command.dto;

import jakarta.validation.constraints.*;

public class CrearReservaCommand {
    @NotBlank(message = "El cliente ID es requerido")
    private String clienteId;

    @NotBlank(message = "El deporte es requerido")
    private String deporte;

    @NotBlank(message = "La fecha es requerida")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Formato: YYYY-MM-DD")
    private String fecha;

    @NotBlank(message = "El entrenador es requerido")
    private String entrenador;

    @Min(value = 1, message = "Las horas deben ser al menos 1")
    @Max(value = 8, message = "Las horas no pueden exceder 8")
    private Integer horas;

    public CrearReservaCommand() {}

    public CrearReservaCommand(String clienteId, String deporte, String fecha, String entrenador, Integer horas) {
        this.clienteId = clienteId;
        this.deporte = deporte;
        this.fecha = fecha;
        this.entrenador = entrenador;
        this.horas = horas;
    }

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
}
