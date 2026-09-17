package com.buses.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilidad centralizada para obtener conexiones a la base de datos.
 * Ajusta URL, usuario y password segun tu entorno local de MySQL.
 */
public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/buses_ipc2?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "champet2020";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontro el driver de MySQL", e);
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
