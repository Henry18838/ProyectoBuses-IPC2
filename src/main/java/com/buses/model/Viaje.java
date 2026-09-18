package com.buses.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Viaje {
    private int id;
    private String tipo; // REGULAR, ALQUILER
    private int sucursalId;

    private Integer rutaId;          // solo REGULAR
    private String rutaDescripcion;  // solo para mostrar (origen -> destino), via join

    private int busId;
    private String busPlaca;         // solo para mostrar, via join

    private int choferId;
    private String choferNombre;     // solo para mostrar, via join

    private String origenTexto;      // solo ALQUILER
    private String destinoTexto;     // solo ALQUILER
    private Integer numPasajeros;    // solo ALQUILER
    private BigDecimal precioAlquiler; // solo ALQUILER

    private LocalDateTime fechaHoraSalidaProg;
    private LocalDateTime fechaHoraLlegadaProg;

    public Viaje() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getSucursalId() { return sucursalId; }
    public void setSucursalId(int sucursalId) { this.sucursalId = sucursalId; }

    public Integer getRutaId() { return rutaId; }
    public void setRutaId(Integer rutaId) { this.rutaId = rutaId; }

    public String getRutaDescripcion() { return rutaDescripcion; }
    public void setRutaDescripcion(String rutaDescripcion) { this.rutaDescripcion = rutaDescripcion; }

    public int getBusId() { return busId; }
    public void setBusId(int busId) { this.busId = busId; }

    public String getBusPlaca() { return busPlaca; }
    public void setBusPlaca(String busPlaca) { this.busPlaca = busPlaca; }

    public int getChoferId() { return choferId; }
    public void setChoferId(int choferId) { this.choferId = choferId; }

    public String getChoferNombre() { return choferNombre; }
    public void setChoferNombre(String choferNombre) { this.choferNombre = choferNombre; }

    public String getOrigenTexto() { return origenTexto; }
    public void setOrigenTexto(String origenTexto) { this.origenTexto = origenTexto; }

    public String getDestinoTexto() { return destinoTexto; }
    public void setDestinoTexto(String destinoTexto) { this.destinoTexto = destinoTexto; }

    public Integer getNumPasajeros() { return numPasajeros; }
    public void setNumPasajeros(Integer numPasajeros) { this.numPasajeros = numPasajeros; }

    public BigDecimal getPrecioAlquiler() { return precioAlquiler; }
    public void setPrecioAlquiler(BigDecimal precioAlquiler) { this.precioAlquiler = precioAlquiler; }

    public LocalDateTime getFechaHoraSalidaProg() { return fechaHoraSalidaProg; }
    public void setFechaHoraSalidaProg(LocalDateTime fechaHoraSalidaProg) { this.fechaHoraSalidaProg = fechaHoraSalidaProg; }

    public LocalDateTime getFechaHoraLlegadaProg() { return fechaHoraLlegadaProg; }
    public void setFechaHoraLlegadaProg(LocalDateTime fechaHoraLlegadaProg) { this.fechaHoraLlegadaProg = fechaHoraLlegadaProg; }
}
