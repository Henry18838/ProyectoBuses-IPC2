package com.buses.servlet;

import com.buses.dao.UsuarioDAO;
import com.buses.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String correo = req.getParameter("correo");
        String password = req.getParameter("password");

        if (correo == null || correo.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Debes ingresar correo y contraseña.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.validarLogin(correo.trim(), password);

            if (usuario == null) {
                req.setAttribute("error", "Correo o contraseña incorrectos, o usuario desactivado.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("rol", usuario.getRol());
            session.setMaxInactiveInterval(30 * 60); // 30 minutos

            switch (usuario.getRol()) {
                case "ADMIN_SISTEMA":
                    resp.sendRedirect(req.getContextPath() + "/admin/sucursales");
                    break;
                case "ADMIN_SUCURSAL":
                    resp.sendRedirect(req.getContextPath() + "/sucursal/inicio");
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/cliente/inicio");
            }

        } catch (SQLException e) {
            req.setAttribute("error", "Error de conexión con la base de datos: " + e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
