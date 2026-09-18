package com.buses.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistroLlegada {
    private int id;
    private int viajeId;
    private LocalDateTime horaRealLlegada;
    private BigDecimal kmLlegada;
    private BigDecimal gastoCombustible;
    private BigDecimal montoDepreciacion;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getViajeId() { return viajeId; }
    public void setViajeId(int viajeId) { this.viajeId = viajeId; }

    public LocalDateTime getHoraRealLlegada() { return horaRealLlegada; }
    public void setHoraRealLlegada(LocalDateTime horaRealLlegada) { this.horaRealLlegada = horaRealLlegada; }

    public BigDecimal getKmLlegada() { return kmLlegada; }
    public void setKmLlegada(BigDecimal kmLlegada) { this.kmLlegada = kmLlegada; }

    public BigDecimal getGastoCombustible() { return gastoCombustible; }
    public void setGastoCombustible(BigDecimal gastoCombustible) { this.gastoCombustible = gastoCombustible; }

    public BigDecimal getMontoDepreciacion() { return montoDepreciacion; }
    public void setMontoDepreciacion(BigDecimal montoDepreciacion) { this.montoDepreciacion = montoDepreciacion; }
}
