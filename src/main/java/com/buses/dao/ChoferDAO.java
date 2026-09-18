package com.buses.dao;

import com.buses.model.Chofer;
import com.buses.util.ConexionDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChoferDAO {

    public List<Chofer> listarPorSucursal(int sucursalId) throws SQLException {
        List<Chofer> lista = new ArrayList<>();
        String sql = "SELECT * FROM chofer WHERE sucursal_id = ? ORDER BY nombre_completo";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sucursalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public Chofer buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM chofer WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public int crear(Chofer c) throws SQLException {
        String sql = "INSERT INTO chofer (foto, nombre_completo, num_licencia, tipo_licencia, fecha_vencimiento_licencia, " +
                     "telefono, salario_base, sucursal_id, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getFoto());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getNumLicencia());
            ps.setString(4, c.getTipoLicencia());
            ps.setDate(5, Date.valueOf(c.getFechaVencimientoLicencia()));
            ps.setString(6, c.getTelefono());
            ps.setBigDecimal(7, c.getSalarioBase());
            ps.setInt(8, c.getSucursalId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void actualizar(Chofer c) throws SQLException {
        String sql = "UPDATE chofer SET foto = ?, nombre_completo = ?, tipo_licencia = ?, fecha_vencimiento_licencia = ?, " +
                     "telefono = ?, salario_base = ? WHERE id = ? AND sucursal_id = ?";
        // num_licencia no se modifica una vez creado el registro
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getFoto());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getTipoLicencia());
            ps.setDate(4, Date.valueOf(c.getFechaVencimientoLicencia()));
            ps.setString(5, c.getTelefono());
            ps.setBigDecimal(6, c.getSalarioBase());
            ps.setInt(7, c.getId());
            ps.setInt(8, c.getSucursalId());
            ps.executeUpdate();
        }
    }

    public void cambiarEstado(int id, boolean activo) throws SQLException {
        String sql = "UPDATE chofer SET activo = ? WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private Chofer mapear(ResultSet rs) throws SQLException {
        Chofer c = new Chofer();
        c.setId(rs.getInt("id"));
        c.setFoto(rs.getString("foto"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setNumLicencia(rs.getString("num_licencia"));
        c.setTipoLicencia(rs.getString("tipo_licencia"));
        Date fv = rs.getDate("fecha_vencimiento_licencia");
        c.setFechaVencimientoLicencia(fv != null ? fv.toLocalDate() : null);
        c.setTelefono(rs.getString("telefono"));
        c.setSalarioBase(rs.getBigDecimal("salario_base"));
        c.setSucursalId(rs.getInt("sucursal_id"));
        c.setActivo(rs.getBoolean("activo"));
        return c;
    }
}
