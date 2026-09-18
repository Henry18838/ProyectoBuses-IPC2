package com.buses.dao;

import com.buses.model.Ruta;
import com.buses.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RutaDAO {

    private static final String SELECT_BASE =
        "SELECT r.*, so.nombre AS origen_nombre, sd.nombre AS destino_nombre " +
        "FROM ruta r " +
        "JOIN sucursal so ON r.sucursal_origen_id = so.id " +
        "JOIN sucursal sd ON r.sucursal_destino_id = sd.id ";

    /** Rutas gestionadas por una sucursal (donde esa sucursal es el origen). */
    public List<Ruta> listarPorSucursalOrigen(int sucursalOrigenId) throws SQLException {
        List<Ruta> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE r.sucursal_origen_id = ? ORDER BY sd.nombre";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sucursalOrigenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Ruta buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE r.id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    /** Cuenta viajes asociados a una ruta (para permitir o no eliminarla). */
    public int contarViajesAsociados(int rutaId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM viaje WHERE ruta_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rutaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public int crear(Ruta r) throws SQLException {
        String sql = "INSERT INTO ruta (sucursal_origen_id, sucursal_destino_id, distancia_km, precio_boleto) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getSucursalOrigenId());
            ps.setInt(2, r.getSucursalDestinoId());
            ps.setBigDecimal(3, r.getDistanciaKm());
            ps.setBigDecimal(4, r.getPrecioBoleto());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void actualizar(Ruta r) throws SQLException {
        // El origen no se permite cambiar (es lo que define quien la gestiona); si se quiere
        // cambiar el origen, se elimina y se crea una nueva.
        String sql = "UPDATE ruta SET sucursal_destino_id = ?, distancia_km = ?, precio_boleto = ? WHERE id = ? AND sucursal_origen_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getSucursalDestinoId());
            ps.setBigDecimal(2, r.getDistanciaKm());
            ps.setBigDecimal(3, r.getPrecioBoleto());
            ps.setInt(4, r.getId());
            ps.setInt(5, r.getSucursalOrigenId());
            ps.executeUpdate();
        }
    }

    /** Elimina la ruta solo si no tiene viajes asociados. Retorna false si no se pudo eliminar. */
    public boolean eliminar(int id) throws SQLException {
        if (contarViajesAsociados(id) > 0) {
            return false;
        }
        String sql = "DELETE FROM ruta WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        return true;
    }

    private Ruta mapear(ResultSet rs) throws SQLException {
        Ruta r = new Ruta();
        r.setId(rs.getInt("id"));
        r.setSucursalOrigenId(rs.getInt("sucursal_origen_id"));
        r.setSucursalOrigenNombre(rs.getString("origen_nombre"));
        r.setSucursalDestinoId(rs.getInt("sucursal_destino_id"));
        r.setSucursalDestinoNombre(rs.getString("destino_nombre"));
        r.setDistanciaKm(rs.getBigDecimal("distancia_km"));
        r.setPrecioBoleto(rs.getBigDecimal("precio_boleto"));
        return r;
    }
}
