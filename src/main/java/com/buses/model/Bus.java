package com.buses.model;

import java.math.BigDecimal;

public class Bus {
    private int id;
    private String foto;
    private String numPlaca;
    private String marca;
    private String modelo;
    private int anioFabricacion;
    private int capacidadPasajeros;
    private String estadoOperativo; // OPERATIVO, EN_MANTENIMIENTO, FUERA_DE_SERVICIO
    private BigDecimal kilometrajeActual;
    private int sucursalId;
    private boolean activo;

    public Bus() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getNumPlaca() { return numPlaca; }
    public void setNumPlaca(String numPlaca) { this.numPlaca = numPlaca; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnioFabricacion() { return anioFabricacion; }
    public void setAnioFabricacion(int anioFabricacion) { this.anioFabricacion = anioFabricacion; }

    public int getCapacidadPasajeros() { return capacidadPasajeros; }
    public void setCapacidadPasajeros(int capacidadPasajeros) { this.capacidadPasajeros = capacidadPasajeros; }

    public String getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(String estadoOperativo) { this.estadoOperativo = estadoOperativo; }

    public BigDecimal getKilometrajeActual() { return kilometrajeActual; }
    public void setKilometrajeActual(BigDecimal kilometrajeActual) { this.kilometrajeActual = kilometrajeActual; }

    public int getSucursalId() { return sucursalId; }
    public void setSucursalId(int sucursalId) { this.sucursalId = sucursalId; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
