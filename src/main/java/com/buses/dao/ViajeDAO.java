package com.buses.dao;

import com.buses.model.Viaje;
import com.buses.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViajeDAO {

    private static final String SELECT_BASE =
        "SELECT v.*, b.num_placa, c.nombre_completo AS chofer_nombre, " +
        "       CONCAT(so.nombre, ' -> ', sd.nombre) AS ruta_desc, " +
        "       (SELECT COUNT(*) FROM registro_salida rs WHERE rs.viaje_id = v.id) AS tiene_salida, " +
        "       (SELECT COUNT(*) FROM registro_llegada rl WHERE rl.viaje_id = v.id) AS tiene_llegada " +
        "FROM viaje v " +
        "JOIN bus b ON v.bus_id = b.id " +
        "JOIN chofer c ON v.chofer_id = c.id " +
        "LEFT JOIN ruta r ON v.ruta_id = r.id " +
        "LEFT JOIN sucursal so ON r.sucursal_origen_id = so.id " +
        "LEFT JOIN sucursal sd ON r.sucursal_destino_id = sd.id ";

    public List<Viaje> listarPorSucursal(int sucursalId) throws SQLException {
        List<Viaje> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE v.sucursal_id = ? ORDER BY v.fecha_hora_salida_prog DESC";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sucursalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Viaje buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE v.id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    /** Indica si el viaje ya tiene registro de salida (para bloquear edicion de tipo / eliminacion). */
    public boolean tieneRegistroSalida(int viajeId) throws SQLException {
        String sql = "SELECT 1 FROM registro_salida WHERE viaje_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, viajeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Indica si el viaje ya fue pagado (boleto vendido o alquiler pagado).
     *  Las tablas boleto / pago de alquiler aun no existen -- por ahora retorna false. */
    public boolean estaPagado(int viajeId) throws SQLException {
        return false; // TODO: consultar boleto / alquiler cuando existan esas tablas
    }

    public int crear(Viaje v) throws SQLException {
        String sql = "INSERT INTO viaje (tipo, sucursal_id, ruta_id, bus_id, chofer_id, origen_texto, destino_texto, " +
                     "num_pasajeros, precio_alquiler, fecha_hora_salida_prog, fecha_hora_llegada_prog) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            llenarParametros(ps, v);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    /** Actualiza los datos basicos del viaje. El TIPO nunca se modifica (regla de negocio). */
    public void actualizar(Viaje v) throws SQLException {
        String sql = "UPDATE viaje SET ruta_id = ?, bus_id = ?, chofer_id = ?, origen_texto = ?, destino_texto = ?, " +
                     "num_pasajeros = ?, precio_alquiler = ?, fecha_hora_salida_prog = ?, fecha_hora_llegada_prog = ? " +
                     "WHERE id = ? AND sucursal_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (v.getRutaId() != null) ps.setInt(1, v.getRutaId()); else ps.setNull(1, Types.INTEGER);
            ps.setInt(2, v.getBusId());
            ps.setInt(3, v.getChoferId());
            ps.setString(4, v.getOrigenTexto());
            ps.setString(5, v.getDestinoTexto());
            if (v.getNumPasajeros() != null) ps.setInt(6, v.getNumPasajeros()); else ps.setNull(6, Types.INTEGER);
            if (v.getPrecioAlquiler() != null) ps.setBigDecimal(7, v.getPrecioAlquiler()); else ps.setNull(7, Types.DECIMAL);
            ps.setTimestamp(8, Timestamp.valueOf(v.getFechaHoraSalidaProg()));
            ps.setTimestamp(9, Timestamp.valueOf(v.getFechaHoraLlegadaProg()));
            ps.setInt(10, v.getId());
            ps.setInt(11, v.getSucursalId());
            ps.executeUpdate();
        }
    }

    /** Elimina el viaje (y todo lo relacionado, por ahora no hay tablas hijas) solo si
     *  no ha sido iniciado ni pagado. Retorna false si no se pudo eliminar. */
    public boolean eliminar(int id) throws SQLException {
        if (tieneRegistroSalida(id) || estaPagado(id)) {
            return false;
        }
        String sql = "DELETE FROM viaje WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        return true;
    }

    private void llenarParametros(PreparedStatement ps, Viaje v) throws SQLException {
        ps.setString(1, v.getTipo());
        ps.setInt(2, v.getSucursalId());
        if (v.getRutaId() != null) ps.setInt(3, v.getRutaId()); else ps.setNull(3, Types.INTEGER);
        ps.setInt(4, v.getBusId());
        ps.setInt(5, v.getChoferId());
        ps.setString(6, v.getOrigenTexto());
        ps.setString(7, v.getDestinoTexto());
        if (v.getNumPasajeros() != null) ps.setInt(8, v.getNumPasajeros()); else ps.setNull(8, Types.INTEGER);
        if (v.getPrecioAlquiler() != null) ps.setBigDecimal(9, v.getPrecioAlquiler()); else ps.setNull(9, Types.DECIMAL);
        ps.setTimestamp(10, Timestamp.valueOf(v.getFechaHoraSalidaProg()));
        ps.setTimestamp(11, Timestamp.valueOf(v.getFechaHoraLlegadaProg()));
    }

    private Viaje mapear(ResultSet rs) throws SQLException {
        Viaje v = new Viaje();
        v.setId(rs.getInt("id"));
        v.setTipo(rs.getString("tipo"));
        v.setSucursalId(rs.getInt("sucursal_id"));
        int rutaId = rs.getInt("ruta_id");
        v.setRutaId(rs.wasNull() ? null : rutaId);
        v.setRutaDescripcion(rs.getString("ruta_desc"));
        v.setBusId(rs.getInt("bus_id"));
        v.setBusPlaca(rs.getString("num_placa"));
        v.setChoferId(rs.getInt("chofer_id"));
        v.setChoferNombre(rs.getString("chofer_nombre"));
        v.setOrigenTexto(rs.getString("origen_texto"));
        v.setDestinoTexto(rs.getString("destino_texto"));
        int pasajeros = rs.getInt("num_pasajeros");
        v.setNumPasajeros(rs.wasNull() ? null : pasajeros);
        v.setPrecioAlquiler(rs.getBigDecimal("precio_alquiler"));
        Timestamp salida = rs.getTimestamp("fecha_hora_salida_prog");
        v.setFechaHoraSalidaProg(salida != null ? salida.toLocalDateTime() : null);
        Timestamp llegada = rs.getTimestamp("fecha_hora_llegada_prog");
        v.setFechaHoraLlegadaProg(llegada != null ? llegada.toLocalDateTime() : null);
        v.setTieneSalida(rs.getInt("tiene_salida") > 0);
        v.setTieneLlegada(rs.getInt("tiene_llegada") > 0);
        return v;
    }
}
