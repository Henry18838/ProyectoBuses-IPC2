package com.buses.servlet;

import com.buses.dao.SucursalDAO;
import com.buses.model.Sucursal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/sucursales")
public class SucursalServlet extends HttpServlet {

    private final SucursalDAO dao = new SucursalDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        try {
            if ("editar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Sucursal s = dao.buscarPorId(id);
                req.setAttribute("sucursal", s);
            }
            if ("desactivar".equals(accion) || "activar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.cambiarEstado(id, "activar".equals(accion));
            }
            List<Sucursal> lista = dao.listarTodas();
            req.setAttribute("sucursales", lista);
            req.getRequestDispatcher("/WEB-INF/views/admin/sucursales.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error al consultar sucursales", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String nombre = req.getParameter("nombre");
        String direccion = req.getParameter("direccion");

        if (nombre == null || nombre.trim().isEmpty() || direccion == null || direccion.trim().isEmpty()) {
            req.setAttribute("error", "Nombre y dirección son obligatorios.");
            doGet(req, resp);
            return;
        }

        Sucursal s = new Sucursal();
        s.setNombre(nombre.trim());
        s.setDireccion(direccion.trim());

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                s.setId(Integer.parseInt(idParam));
                dao.actualizar(s);
            } else {
                dao.crear(s);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/sucursales");
        } catch (SQLException e) {
            throw new ServletException("Error al guardar sucursal", e);
        }
    }
}
