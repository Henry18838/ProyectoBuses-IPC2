package com.buses.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Protege las rutas /admin/*, /sucursal/* y /cliente/* segun el rol
 * guardado en sesion tras el login. Si no hay sesion, redirige a /login.
 * Si el rol no corresponde a la seccion solicitada, redirige a /login con mensaje.
 */
@WebFilter(urlPatterns = {"/admin/*", "/sucursal/*", "/cliente/*"})
public class SeguridadFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        String rol = (String) session.getAttribute("rol");
        String seccion = uri.substring(contextPath.length()).split("/")[1]; // admin | sucursal | cliente

        boolean autorizado =
                (seccion.equals("admin") && rol.equals("ADMIN_SISTEMA")) ||
                (seccion.equals("sucursal") && rol.equals("ADMIN_SUCURSAL")) ||
                (seccion.equals("cliente") && rol.equals("CLIENTE"));

        if (!autorizado) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
