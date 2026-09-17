package com.buses.model;

import java.math.BigDecimal;

public class Usuario {
    private int id;
    private String nombreCompleto;
    private String correo;
    private String passwordHash;
    private String rol; // ADMIN_SISTEMA, ADMIN_SUCURSAL, CLIENTE
    private String dpi;
    private String nit;
    private String telefono;
    private String direccion;
    private BigDecimal saldoCartera;
    private Integer sucursalId; // solo si rol = ADMIN_SUCURSAL
    private boolean activo;

    public Usuario() {
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public BigDecimal getSaldoCartera() { return saldoCartera; }
    public void setSaldoCartera(BigDecimal saldoCartera) { this.saldoCartera = saldoCartera; }

    public Integer getSucursalId() { return sucursalId; }
    public void setSucursalId(Integer sucursalId) { this.sucursalId = sucursalId; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
