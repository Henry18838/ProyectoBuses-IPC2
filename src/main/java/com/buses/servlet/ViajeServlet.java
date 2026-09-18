package com.buses.servlet;

import com.buses.dao.*;
import com.buses.model.*;

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
import java.util.List;

@WebServlet("/sucursal/viajes")
public class ViajeServlet extends HttpServlet {

    private final ViajeDAO dao = new ViajeDAO();
    private final BusDAO busDAO = new BusDAO();
    private final ChoferDAO choferDAO = new ChoferDAO();
    private final RutaDAO rutaDAO = new RutaDAO();

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
                Viaje v = dao.buscarPorId(id);
                if (v != null && v.getSucursalId() == sucursalId) {
                    req.setAttribute("viaje", v);
                }
            }
            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(req.getParameter("id"));
                boolean ok = dao.eliminar(id);
                if (!ok) {
                    req.setAttribute("error", "No se puede eliminar: el viaje ya inició o ya fue pagado.");
                }
            }

            List<Viaje> viajes = dao.listarPorSucursal(sucursalId);
            List<Bus> buses = busDAO.listarPorSucursal(sucursalId, null);
            List<Chofer> choferes = choferDAO.listarPorSucursal(sucursalId);
            List<Ruta> rutas = rutaDAO.listarPorSucursalOrigen(sucursalId);

            req.setAttribute("viajes", viajes);
            req.setAttribute("buses", buses);
            req.setAttribute("choferes", choferes);
            req.setAttribute("rutas", rutas);
            req.getRequestDispatcher("/WEB-INF/views/sucursal/viajes.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Error al consultar viajes", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sucursalId = sucursalDelUsuario(req);
        String idParam = req.getParameter("id");
        boolean esEdicion = idParam != null && !idParam.trim().isEmpty();

        String tipo = req.getParameter("tipo");
        String rutaIdStr = req.getParameter("rutaId");
        String busIdStr = req.getParameter("busId");
        String choferIdStr = req.getParameter("choferId");
        String origen = req.getParameter("origenTexto");
        String destino = req.getParameter("destinoTexto");
        String pasajerosStr = req.getParameter("numPasajeros");
        String precioStr = req.getParameter("precioAlquiler");
        String salidaStr = req.getParameter("fechaHoraSalidaProg");
        String llegadaStr = req.getParameter("fechaHoraLlegadaProg");

        if (isVacio(busIdStr) || isVacio(choferIdStr) || isVacio(salidaStr) || isVacio(llegadaStr)) {
            req.setAttribute("error", "Bus, chofer y fechas/horas son obligatorios.");
            doGet(req, resp);
            return;
        }

        Viaje v = new Viaje();
        v.setSucursalId(sucursalId);
        v.setBusId(Integer.parseInt(busIdStr));
        v.setChoferId(Integer.parseInt(choferIdStr));

        try {
            v.setFechaHoraSalidaProg(LocalDateTime.parse(salidaStr));
            v.setFechaHoraLlegadaProg(LocalDateTime.parse(llegadaStr));
        } catch (Exception e) {
            req.setAttribute("error", "Formato de fecha/hora inválido.");
            doGet(req, resp);
            return;
        }

        if (esEdicion) {
            // El tipo NO se puede modificar: se recupera el que ya tenia el viaje en BD
            try {
                Viaje existente = dao.buscarPorId(Integer.parseInt(idParam));
                if (existente == null || existente.getSucursalId() != sucursalId) {
                    req.setAttribute("error", "Viaje no encontrado.");
                    doGet(req, resp);
                    return;
                }
                tipo = existente.getTipo();
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }

        if (isVacio(tipo)) {
            req.setAttribute("error", "Debes seleccionar el tipo de viaje.");
            doGet(req, resp);
            return;
        }
        v.setTipo(tipo);

        if ("REGULAR".equals(tipo)) {
            if (isVacio(rutaIdStr)) {
                req.setAttribute("error", "Los viajes regulares requieren una ruta.");
                doGet(req, resp);
                return;
            }
            v.setRutaId(Integer.parseInt(rutaIdStr));
        } else { // ALQUILER
            if (isVacio(origen) || isVacio(destino) || isVacio(pasajerosStr)) {
                req.setAttribute("error", "Los viajes de alquiler requieren origen, destino y número de pasajeros.");
                doGet(req, resp);
                return;
            }
            v.setOrigenTexto(origen.trim());
            v.setDestinoTexto(destino.trim());
            v.setNumPasajeros(Integer.parseInt(pasajerosStr));
            if (!isVacio(precioStr)) {
                v.setPrecioAlquiler(new BigDecimal(precioStr));
            }
        }

        try {
            if (esEdicion) {
                v.setId(Integer.parseInt(idParam));
                dao.actualizar(v);
            } else {
                dao.crear(v);
            }
            resp.sendRedirect(req.getContextPath() + "/sucursal/viajes");
        } catch (SQLException e) {
            throw new ServletException("Error al guardar el viaje", e);
        }
    }

    private boolean isVacio(String s) {
        return s == null || s.trim().isEmpty();
    }
}
