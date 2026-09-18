package com.buses.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistroSalida {
    private int id;
    private int viajeId;
    private LocalDateTime horaRealSalida;
    private BigDecimal kmSalida;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getViajeId() { return viajeId; }
    public void setViajeId(int viajeId) { this.viajeId = viajeId; }

    public LocalDateTime getHoraRealSalida() { return horaRealSalida; }
    public void setHoraRealSalida(LocalDateTime horaRealSalida) { this.horaRealSalida = horaRealSalida; }

    public BigDecimal getKmSalida() { return kmSalida; }
    public void setKmSalida(BigDecimal kmSalida) { this.kmSalida = kmSalida; }
}
