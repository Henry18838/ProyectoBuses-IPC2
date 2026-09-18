package com.buses.dao;

import com.buses.model.Bus;
import com.buses.model.RegistroSalida;
import com.buses.util.ConexionDB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RegistroViajeDAO {

    private final ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();
    private final BusDAO busDAO = new BusDAO();

    public boolean tieneSalida(int viajeId) throws SQLException {
        return existeEn("registro_salida", viajeId);
    }

    public boolean tieneLlegada(int viajeId) throws SQLException {
        return existeEn("registro_llegada", viajeId);
    }

    private boolean existeEn(String tabla, int viajeId) throws SQLException {
        String sql = "SELECT 1 FROM " + tabla + " WHERE viaje_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, viajeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public RegistroSalida obtenerSalida(int viajeId) throws SQLException {
        String sql = "SELECT * FROM registro_salida WHERE viaje_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, viajeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RegistroSalida rsSalida = new RegistroSalida();
                    rsSalida.setId(rs.getInt("id"));
                    rsSalida.setViajeId(rs.getInt("viaje_id"));
                    rsSalida.setHoraRealSalida(rs.getTimestamp("hora_real_salida").toLocalDateTime());
                    rsSalida.setKmSalida(rs.getBigDecimal("km_salida"));
                    return rsSalida;
                }
            }
        }
        return null;
    }

    /**
     * Registra la salida de un viaje. Es INMUTABLE: no se exponen metodos
     * de update/delete a proposito. Retorna un mensaje de error, o null si
     * se guardo correctamente.
     */
    public String registrarSalida(int viajeId, int busId, LocalDateTime horaReal, BigDecimal kmSalida) throws SQLException {
        if (tieneSalida(viajeId)) {
            return "Este viaje ya tiene un registro de salida.";
        }

        Bus bus = busDAO.buscarPorId(busId);
        if (bus != null && kmSalida.compareTo(bus.getKilometrajeActual()) < 0) {
            return "El kilometraje de salida no puede ser menor al kilometraje actual del bus ("
                    + bus.getKilometrajeActual() + ").";
        }

        String sql = "INSERT INTO registro_salida (viaje_id, hora_real_salida, km_salida) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, viajeId);
            ps.setTimestamp(2, Timestamp.valueOf(horaReal));
            ps.setBigDecimal(3, kmSalida);
            ps.executeUpdate();
        }
        return null;
    }

    /**
     * Registra la llegada de un viaje. Calcula y GUARDA la depreciacion usando
     * el monto vigente en configuracion en este momento (no se recalcula despues).
     * Tambien actualiza el kilometraje_actual del bus. INMUTABLE una vez guardado.
     */
    public String registrarLlegada(int viajeId, int busId, LocalDateTime horaReal,
                                    BigDecimal kmLlegada, BigDecimal gastoCombustible) throws SQLException {
        if (!tieneSalida(viajeId)) {
            return "No se puede registrar la llegada: primero debe registrarse la salida.";
        }
        if (tieneLlegada(viajeId)) {
            return "Este viaje ya tiene un registro de llegada.";
        }

        RegistroSalida salida = obtenerSalida(viajeId);
        if (kmLlegada.compareTo(salida.getKmSalida()) < 0) {
            return "El kilometraje de llegada no puede ser menor al de salida (" + salida.getKmSalida() + ").";
        }

        BigDecimal kmRecorridos = kmLlegada.subtract(salida.getKmSalida());
        BigDecimal montoPorKm = configuracionDAO.obtenerMontoDepreciacionActual();
        BigDecimal montoDepreciacion = kmRecorridos.multiply(montoPorKm);

        try (Connection con = ConexionDB.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                String sqlInsert = "INSERT INTO registro_llegada (viaje_id, hora_real_llegada, km_llegada, gasto_combustible, monto_depreciacion) " +
                                    "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                    ps.setInt(1, viajeId);
                    ps.setTimestamp(2, Timestamp.valueOf(horaReal));
                    ps.setBigDecimal(3, kmLlegada);
                    ps.setBigDecimal(4, gastoCombustible);
                    ps.setBigDecimal(5, montoDepreciacion);
                    ps.executeUpdate();
                }

                String sqlUpdateBus = "UPDATE bus SET kilometraje_actual = ? WHERE id = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlUpdateBus)) {
                    ps.setBigDecimal(1, kmLlegada);
                    ps.setInt(2, busId);
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
        return null;
    }
}
