package com.buses.dao;

import com.buses.model.Usuario;
import com.buses.util.ConexionDB;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    /**
     * Busca un usuario por correo. Retorna null si no existe.
     */
    public Usuario buscarPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    /**
     * Valida credenciales. Retorna el Usuario si el login es correcto y esta activo,
     * o null si las credenciales son invalidas o el usuario esta desactivado.
     */
    public Usuario validarLogin(String correo, String passwordPlano) throws SQLException {
        Usuario usuario = buscarPorCorreo(correo);
        if (usuario == null || !usuario.isActivo()) {
            return null;
        }
        if (BCrypt.checkpw(passwordPlano, usuario.getPasswordHash())) {
            return usuario;
        }
        return null;
    }

    public int crear(Usuario u, String passwordPlano) throws SQLException {
        String hash = BCrypt.hashpw(passwordPlano, BCrypt.gensalt(10));
        String sql = "INSERT INTO usuario (nombre_completo, correo, password_hash, rol, dpi, nit, telefono, direccion, sucursal_id, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getCorreo());
            ps.setString(3, hash);
            ps.setString(4, u.getRol());
            ps.setString(5, u.getDpi());
            ps.setString(6, u.getNit());
            ps.setString(7, u.getTelefono());
            ps.setString(8, u.getDireccion());
            if (u.getSucursalId() != null) {
                ps.setInt(9, u.getSucursalId());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Usuario> listarAdminsSucursal() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE rol = 'ADMIN_SUCURSAL' ORDER BY nombre_completo";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void cambiarEstado(int id, boolean activo) throws SQLException {
        String sql = "UPDATE usuario SET activo = ? WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setCorreo(rs.getString("correo"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRol(rs.getString("rol"));
        u.setDpi(rs.getString("dpi"));
        u.setNit(rs.getString("nit"));
        u.setTelefono(rs.getString("telefono"));
        u.setDireccion(rs.getString("direccion"));
        u.setSaldoCartera(rs.getBigDecimal("saldo_cartera"));
        int sucId = rs.getInt("sucursal_id");
        u.setSucursalId(rs.wasNull() ? null : sucId);
        u.setActivo(rs.getBoolean("activo"));
        return u;
    }
}
