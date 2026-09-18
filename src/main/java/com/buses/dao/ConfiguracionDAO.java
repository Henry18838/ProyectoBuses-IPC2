package com.buses.dao;

import com.buses.util.ConexionDB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracionDAO {

    /** Obtiene el monto de depreciacion por km vigente (el mas reciente). */
    public BigDecimal obtenerMontoDepreciacionActual() throws SQLException {
        String sql = "SELECT monto_depreciacion_km FROM configuracion ORDER BY id DESC LIMIT 1";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal("monto_depreciacion_km");
        }
        return BigDecimal.ZERO;
    }

    /** El administrador del sistema puede cambiar el monto en cualquier momento.
     *  Insertamos una fila nueva en vez de hacer UPDATE para conservar el historial
     *  y NO afectar los registros de depreciacion ya calculados y guardados. */
    public void actualizarMontoDepreciacion(BigDecimal nuevoMonto) throws SQLException {
        String sql = "INSERT INTO configuracion (monto_depreciacion_km, fecha_actualizacion) VALUES (?, CURDATE())";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, nuevoMonto);
            ps.executeUpdate();
        }
    }
}
