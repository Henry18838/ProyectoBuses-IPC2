package com.buses.servlet;

import com.buses.dao.RegistroViajeDAO;
import com.buses.dao.ViajeDAO;
import com.buses.model.Usuario;
import com.buses.model.Viaje;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/sucursal/registro-viaje")
public class RegistroViajeServlet extends HttpServlet {

    private final RegistroViajeDAO dao = new RegistroViajeDAO();
    private final ViajeDAO viajeDAO = new ViajeDAO();

    private int sucursalDelUsuario(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u.getSucursalId();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sucursalId = sucursalDelUsuario(req);
        String tipo = req.getParameter("tipo"); // salida | llegada
        int viajeId = Integer.parseInt(req.getParameter("viajeId"));

        try {
            Viaje viaje = viajeDAO.buscarPorId(viajeId);
            if (viaje == null || viaje.getSucursalId() != sucursalId) {
                resp.sendRedirect(req.getContextPath() + "/sucursal/viajes");
                return;
            }
            req.setAttribute("viaje", viaje);
            req.setAttribute("tipo", tipo);
            req.getRequestDispatcher("/WEB-INF/views/sucursal/registro-viaje.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error al consultar el viaje", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sucursalId = sucursalDelUsuario(req);
        String tipo = req.getParameter("tipo");
        int viajeId = Integer.parseInt(req.getParameter("viajeId"));
        int busId = Integer.parseInt(req.getParameter("busId"));

        try {
            Viaje viaje = viajeDAO.buscarPorId(viajeId);
            if (viaje == null || viaje.getSucursalId() != sucursalId) {
                resp.sendRedirect(req.getContextPath() + "/sucursal/viajes");
                return;
            }

            String horaStr = req.getParameter("horaReal");
            LocalDateTime horaReal = LocalDateTime.parse(horaStr);
            String error;

            if ("salida".equals(tipo)) {
                BigDecimal kmSalida = new BigDecimal(req.getParameter("km"));
                error = dao.registrarSalida(viajeId, busId, horaReal, kmSalida);
            } else {
                BigDecimal kmLlegada = new BigDecimal(req.getParameter("km"));
                BigDecimal gastoCombustible = new BigDecimal(req.getParameter("gastoCombustible"));
                error = dao.registrarLlegada(viajeId, busId, horaReal, kmLlegada, gastoCombustible);
            }

            if (error != null) {
                req.setAttribute("error", error);
                req.setAttribute("viaje", viaje);
                req.setAttribute("tipo", tipo);
                req.getRequestDispatcher("/WEB-INF/views/sucursal/registro-viaje.jsp").forward(req, resp);
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/sucursal/viajes");
        } catch (SQLException e) {
            throw new ServletException("Error al guardar el registro", e);
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            req.setAttribute("error", "Datos con formato inválido.");
            doGet(req, resp);
        }
    }
}
