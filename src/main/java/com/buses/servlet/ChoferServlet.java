package com.buses.servlet;

import com.buses.dao.ChoferDAO;
import com.buses.model.Chofer;
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
import java.time.LocalDate;
import java.util.List;

@WebServlet("/sucursal/choferes")
public class ChoferServlet extends HttpServlet {

    private final ChoferDAO dao = new ChoferDAO();

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
                Chofer c = dao.buscarPorId(id);
                if (c != null && c.getSucursalId() == sucursalId) {
                    req.setAttribute("chofer", c);
                }
            }
            if ("desactivar".equals(accion)) {
                dao.cambiarEstado(Integer.parseInt(req.getParameter("id")), false);
            }
            if ("activar".equals(accion)) {
                dao.cambiarEstado(Integer.parseInt(req.getParameter("id")), true);
            }

            List<Chofer> choferes = dao.listarPorSucursal(sucursalId);
            req.setAttribute("choferes", choferes);
            req.getRequestDispatcher("/WEB-INF/views/sucursal/choferes.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Error al consultar choferes", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sucursalId = sucursalDelUsuario(req);
        String idParam = req.getParameter("id");

        String nombre = req.getParameter("nombreCompleto");
        String licencia = req.getParameter("numLicencia");
        String tipoLicencia = req.getParameter("tipoLicencia");
        String fechaVencStr = req.getParameter("fechaVencimientoLicencia");
        String telefono = req.getParameter("telefono");
        String salarioStr = req.getParameter("salarioBase");
        String foto = req.getParameter("foto");

        if (isVacio(nombre) || isVacio(licencia) || isVacio(tipoLicencia) || isVacio(fechaVencStr)
                || isVacio(telefono) || isVacio(salarioStr)) {
            req.setAttribute("error", "Todos los campos son obligatorios excepto la foto.");
            doGet(req, resp);
            return;
        }

        Chofer c = new Chofer();
        c.setNombreCompleto(nombre.trim());
        c.setNumLicencia(licencia.trim());
        c.setTipoLicencia(tipoLicencia.trim());
        c.setTelefono(telefono.trim());
        c.setFoto(foto);
        c.setSucursalId(sucursalId);

        try {
            c.setFechaVencimientoLicencia(LocalDate.parse(fechaVencStr));
            c.setSalarioBase(new BigDecimal(salarioStr));
        } catch (Exception e) {
            req.setAttribute("error", "Fecha o salario con formato inválido.");
            doGet(req, resp);
            return;
        }

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                c.setId(Integer.parseInt(idParam));
                dao.actualizar(c);
            } else {
                dao.crear(c);
            }
            resp.sendRedirect(req.getContextPath() + "/sucursal/choferes");
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                req.setAttribute("error", "Ya existe un chofer registrado con ese número de licencia.");
                doGet(req, resp);
            } else {
                throw new ServletException("Error al guardar el chofer", e);
            }
        }
    }

    private boolean isVacio(String s) {
        return s == null || s.trim().isEmpty();
    }
}
