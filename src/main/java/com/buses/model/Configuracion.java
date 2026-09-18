package com.buses.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Configuracion {
    private int id;
    private BigDecimal montoDepreciacionKm;
    private LocalDate fechaActualizacion;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public BigDecimal getMontoDepreciacionKm() { return montoDepreciacionKm; }
    public void setMontoDepreciacionKm(BigDecimal montoDepreciacionKm) { this.montoDepreciacionKm = montoDepreciacionKm; }

    public LocalDate getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDate fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
