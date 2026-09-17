package com.buses.dao;

import com.buses.model.Sucursal;
import com.buses.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SucursalDAO {

    public List<Sucursal> listarTodas() throws SQLException {
        List<Sucursal> lista = new ArrayList<>();
        String sql = "SELECT * FROM sucursal ORDER BY nombre";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Sucursal buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM sucursal WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public int crear(Sucursal s) throws SQLException {
        String sql = "INSERT INTO sucursal (nombre, direccion, latitud, longitud, activo) VALUES (?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDireccion());
            if (s.getLatitud() != null) ps.setDouble(3, s.getLatitud()); else ps.setNull(3, java.sql.Types.DOUBLE);
            if (s.getLongitud() != null) ps.setDouble(4, s.getLongitud()); else ps.setNull(4, java.sql.Types.DOUBLE);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void actualizar(Sucursal s) throws SQLException {
        String sql = "UPDATE sucursal SET nombre = ?, direccion = ?, latitud = ?, longitud = ? WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDireccion());
            if (s.getLatitud() != null) ps.setDouble(3, s.getLatitud()); else ps.setNull(3, java.sql.Types.DOUBLE);
            if (s.getLongitud() != null) ps.setDouble(4, s.getLongitud()); else ps.setNull(4, java.sql.Types.DOUBLE);
            ps.setInt(5, s.getId());
            ps.executeUpdate();
        }
    }

    public void cambiarEstado(int id, boolean activo) throws SQLException {
        String sql = "UPDATE sucursal SET activo = ? WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private Sucursal mapear(ResultSet rs) throws SQLException {
        Sucursal s = new Sucursal();
        s.setId(rs.getInt("id"));
        s.setNombre(rs.getString("nombre"));
        s.setDireccion(rs.getString("direccion"));
        double lat = rs.getDouble("latitud");
        s.setLatitud(rs.wasNull() ? null : lat);
        double lon = rs.getDouble("longitud");
        s.setLongitud(rs.wasNull() ? null : lon);
        s.setActivo(rs.getBoolean("activo"));
        return s;
    }
}
