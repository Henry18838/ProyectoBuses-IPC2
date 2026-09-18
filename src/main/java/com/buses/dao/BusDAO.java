package com.buses.dao;

import com.buses.model.Bus;
import com.buses.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    /**
     * Lista los buses de una sucursal especifica. Opcionalmente filtra por estado operativo.
     * @param estado puede ser null para traer todos los estados
     */
    public List<Bus> listarPorSucursal(int sucursalId, String estado) throws SQLException {
        List<Bus> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM bus WHERE sucursal_id = ?");
        if (estado != null && !estado.isEmpty()) {
            sql.append(" AND estado_operativo = ?");
        }
        sql.append(" ORDER BY num_placa");

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setInt(1, sucursalId);
            if (estado != null && !estado.isEmpty()) {
                ps.setString(2, estado);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public Bus buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM bus WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    /** Cuenta cuantos viajes de este bus aun no tienen registro de llegada
     *  (es decir, siguen programados o en transito) -- para poder desactivarlo. */
    public int contarViajesActivos(int busId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM viaje v " +
                     "LEFT JOIN registro_llegada rl ON v.id = rl.viaje_id " +
                     "WHERE v.bus_id = ? AND rl.id IS NULL";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, busId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public int crear(Bus b) throws SQLException {
        String sql = "INSERT INTO bus (foto, num_placa, marca, modelo, anio_fabricacion, capacidad_pasajeros, " +
                     "estado_operativo, kilometraje_actual, sucursal_id, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getFoto());
            ps.setString(2, b.getNumPlaca());
            ps.setString(3, b.getMarca());
            ps.setString(4, b.getModelo());
            ps.setInt(5, b.getAnioFabricacion());
            ps.setInt(6, b.getCapacidadPasajeros());
            ps.setString(7, b.getEstadoOperativo());
            ps.setBigDecimal(8, b.getKilometrajeActual());
            ps.setInt(9, b.getSucursalId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void actualizar(Bus b) throws SQLException {
        String sql = "UPDATE bus SET foto = ?, marca = ?, modelo = ?, anio_fabricacion = ?, capacidad_pasajeros = ?, " +
                     "estado_operativo = ?, kilometraje_actual = ? WHERE id = ? AND sucursal_id = ?";
        // num_placa no se permite modificar una vez creado el bus, ni el sucursal_id
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, b.getFoto());
            ps.setString(2, b.getMarca());
            ps.setString(3, b.getModelo());
            ps.setInt(4, b.getAnioFabricacion());
            ps.setInt(5, b.getCapacidadPasajeros());
            ps.setString(6, b.getEstadoOperativo());
            ps.setBigDecimal(7, b.getKilometrajeActual());
            ps.setInt(8, b.getId());
            ps.setInt(9, b.getSucursalId());
            ps.executeUpdate();
        }
    }

    /** Desactiva un bus solo si no tiene viajes programados o en transito. */
    public boolean desactivar(int id) throws SQLException {
        if (contarViajesActivos(id) > 0) {
            return false;
        }
        String sql = "UPDATE bus SET activo = 0 WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        return true;
    }

    public void activar(int id) throws SQLException {
        String sql = "UPDATE bus SET activo = 1 WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Bus mapear(ResultSet rs) throws SQLException {
        Bus b = new Bus();
        b.setId(rs.getInt("id"));
        b.setFoto(rs.getString("foto"));
        b.setNumPlaca(rs.getString("num_placa"));
        b.setMarca(rs.getString("marca"));
        b.setModelo(rs.getString("modelo"));
        b.setAnioFabricacion(rs.getInt("anio_fabricacion"));
        b.setCapacidadPasajeros(rs.getInt("capacidad_pasajeros"));
        b.setEstadoOperativo(rs.getString("estado_operativo"));
        b.setKilometrajeActual(rs.getBigDecimal("kilometraje_actual"));
        b.setSucursalId(rs.getInt("sucursal_id"));
        b.setActivo(rs.getBoolean("activo"));
        return b;
    }
}
