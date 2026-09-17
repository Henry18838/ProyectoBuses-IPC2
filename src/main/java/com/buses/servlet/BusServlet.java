package com.buses.servlet;

import com.buses.dao.BusDAO;
import com.buses.model.Bus;
import com.buses.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/sucursal/buses")
public class BusServlet extends HttpServlet {

    private final BusDAO dao = new BusDAO();

    /** Obtiene la sucursal del admin logueado desde la sesion. */
    private int sucursalDelUsuario(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u.getSucursalId();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sucursalId = sucursalDelUsuario(req);
        String accion = req.getParameter("accion");

        try {
            if ("editar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Bus b = dao.buscarPorId(id);
                if (b != null && b.getSucursalId() == sucursalId) {
                    req.setAttribute("bus", b);
                }
            }
            if ("desactivar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                boolean ok = dao.desactivar(id);
                if (!ok) {
                    req.setAttribute("error", "No se puede desactivar: el bus tiene viajes programados o en tránsito.");
                }
            }
            if ("activar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.activar(id);
            }

            String filtroEstado = req.getParameter("estado");
            List<Bus> buses = dao.listarPorSucursal(sucursalId, filtroEstado);
            req.setAttribute("buses", buses);
            req.setAttribute("filtroEstado", filtroEstado);
            req.getRequestDispatcher("/WEB-INF/views/sucursal/buses.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Error al consultar buses", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sucursalId = sucursalDelUsuario(req);
        String idParam = req.getParameter("id");

        String placa = req.getParameter("numPlaca");
        String marca = req.getParameter("marca");
        String modelo = req.getParameter("modelo");
        String anioStr = req.getParameter("anioFabricacion");
        String capacidadStr = req.getParameter("capacidadPasajeros");
        String estado = req.getParameter("estadoOperativo");
        String kmStr = req.getParameter("kilometrajeActual");
        String foto = req.getParameter("foto");

        if (isVacio(placa) || isVacio(marca) || isVacio(modelo) || isVacio(anioStr)
                || isVacio(capacidadStr) || isVacio(estado) || isVacio(kmStr)) {
            req.setAttribute("error", "Todos los campos son obligatorios excepto la foto.");
            doGet(req, resp);
            return;
        }

        Bus b = new Bus();
        b.setNumPlaca(placa.trim());
        b.setMarca(marca.trim());
        b.setModelo(modelo.trim());
        b.setFoto(foto);
        b.setSucursalId(sucursalId);

        try {
            b.setAnioFabricacion(Integer.parseInt(anioStr));
            b.setCapacidadPasajeros(Integer.parseInt(capacidadStr));
            b.setKilometrajeActual(new BigDecimal(kmStr));
            b.setEstadoOperativo(estado);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Año, capacidad y kilometraje deben ser numéricos.");
            doGet(req, resp);
            return;
        }

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                b.setId(Integer.parseInt(idParam));
                dao.actualizar(b);
            } else {
                dao.crear(b);
            }
            resp.sendRedirect(req.getContextPath() + "/sucursal/buses");
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                req.setAttribute("error", "Ya existe un bus registrado con esa placa.");
                doGet(req, resp);
            } else {
                throw new ServletException("Error al guardar el bus", e);
            }
        }
    }

    private boolean isVacio(String s) {
        return s == null || s.trim().isEmpty();
    }
}
