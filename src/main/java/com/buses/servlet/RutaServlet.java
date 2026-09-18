package com.buses.servlet;

import com.buses.dao.RutaDAO;
import com.buses.dao.SucursalDAO;
import com.buses.model.Ruta;
import com.buses.model.Sucursal;
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

@WebServlet("/sucursal/rutas")
public class RutaServlet extends HttpServlet {

    private final RutaDAO dao = new RutaDAO();
    private final SucursalDAO sucursalDAO = new SucursalDAO();

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
                Ruta r = dao.buscarPorId(id);
                if (r != null && r.getSucursalOrigenId() == sucursalId) {
                    req.setAttribute("ruta", r);
                }
            }
            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                boolean ok = dao.eliminar(id);
                if (!ok) {
                    req.setAttribute("error", "No se puede eliminar: la ruta tiene viajes asociados.");
                }
            }

            List<Ruta> rutas = dao.listarPorSucursalOrigen(sucursalId);
            List<Sucursal> sucursales = sucursalDAO.listarTodas();
            req.setAttribute("rutas", rutas);
            req.setAttribute("sucursales", sucursales);
            req.setAttribute("sucursalPropiaId", sucursalId);
            req.getRequestDispatcher("/WEB-INF/views/sucursal/rutas.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Error al consultar rutas", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sucursalId = sucursalDelUsuario(req);
        String idParam = req.getParameter("id");

        String destinoStr = req.getParameter("sucursalDestinoId");
        String distanciaStr = req.getParameter("distanciaKm");
        String precioStr = req.getParameter("precioBoleto");

        if (isVacio(destinoStr) || isVacio(distanciaStr) || isVacio(precioStr)) {
            req.setAttribute("error", "Todos los campos son obligatorios.");
            doGet(req, resp);
            return;
        }

        int destinoId = Integer.parseInt(destinoStr);
        if (destinoId == sucursalId) {
            req.setAttribute("error", "La sucursal de destino no puede ser igual a la de origen.");
            doGet(req, resp);
            return;
        }

        Ruta r = new Ruta();
        r.setSucursalOrigenId(sucursalId);
        r.setSucursalDestinoId(destinoId);

        try {
            r.setDistanciaKm(new BigDecimal(distanciaStr));
            r.setPrecioBoleto(new BigDecimal(precioStr));
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Distancia y precio deben ser numéricos.");
            doGet(req, resp);
            return;
        }

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                r.setId(Integer.parseInt(idParam));
                dao.actualizar(r);
            } else {
                dao.crear(r);
            }
            resp.sendRedirect(req.getContextPath() + "/sucursal/rutas");
        } catch (SQLException e) {
            throw new ServletException("Error al guardar la ruta", e);
        }
    }

    private boolean isVacio(String s) {
        return s == null || s.trim().isEmpty();
    }
}
