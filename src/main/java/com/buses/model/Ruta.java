package com.buses.model;

import java.math.BigDecimal;

public class Ruta {
    private int id;
    private int sucursalOrigenId;
    private String sucursalOrigenNombre; // solo para mostrar en listados (join)
    private int sucursalDestinoId;
    private String sucursalDestinoNombre; // solo para mostrar en listados (join)
    private BigDecimal distanciaKm;
    private BigDecimal precioBoleto;

    public Ruta() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSucursalOrigenId() { return sucursalOrigenId; }
    public void setSucursalOrigenId(int sucursalOrigenId) { this.sucursalOrigenId = sucursalOrigenId; }

    public String getSucursalOrigenNombre() { return sucursalOrigenNombre; }
    public void setSucursalOrigenNombre(String sucursalOrigenNombre) { this.sucursalOrigenNombre = sucursalOrigenNombre; }

    public int getSucursalDestinoId() { return sucursalDestinoId; }
    public void setSucursalDestinoId(int sucursalDestinoId) { this.sucursalDestinoId = sucursalDestinoId; }

    public String getSucursalDestinoNombre() { return sucursalDestinoNombre; }
    public void setSucursalDestinoNombre(String sucursalDestinoNombre) { this.sucursalDestinoNombre = sucursalDestinoNombre; }

    public BigDecimal getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(BigDecimal distanciaKm) { this.distanciaKm = distanciaKm; }

    public BigDecimal getPrecioBoleto() { return precioBoleto; }
    public void setPrecioBoleto(BigDecimal precioBoleto) { this.precioBoleto = precioBoleto; }
}
