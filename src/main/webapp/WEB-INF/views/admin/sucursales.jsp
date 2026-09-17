<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Sucursales - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-dark bg-dark px-3">
    <span class="navbar-brand">Sistema de Buses — Administrador de Sistema</span>
    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Cerrar sesión</a>
</nav>

<div class="container mt-4">
    <h4>Gestión de sucursales</h4>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card mb-4">
        <div class="card-body">
            <h6 class="card-title">
                <c:choose>
                    <c:when test="${not empty sucursal}">Editar sucursal</c:when>
                    <c:otherwise>Nueva sucursal</c:otherwise>
                </c:choose>
            </h6>
            <form method="post" action="${pageContext.request.contextPath}/admin/sucursales" class="row g-2">
                <input type="hidden" name="id" value="${sucursal.id}">
                <div class="col-md-4">
                    <input type="text" name="nombre" class="form-control" placeholder="Nombre" value="${sucursal.nombre}" required>
                </div>
                <div class="col-md-5">
                    <input type="text" name="direccion" class="form-control" placeholder="Dirección" value="${sucursal.direccion}" required>
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-primary w-100">Guardar</button>
                </div>
            </form>
        </div>
    </div>

    <table class="table table-striped table-bordered bg-white">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Dirección</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="s" items="${sucursales}">
            <tr>
                <td>${s.id}</td>
                <td>${s.nombre}</td>
                <td>${s.direccion}</td>
                <td>
                    <c:choose>
                        <c:when test="${s.activo}"><span class="badge bg-success">Activa</span></c:when>
                        <c:otherwise><span class="badge bg-secondary">Inactiva</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/sucursales?accion=editar&id=${s.id}" class="btn btn-sm btn-warning">Editar</a>
                    <c:choose>
                        <c:when test="${s.activo}">
                            <a href="${pageContext.request.contextPath}/admin/sucursales?accion=desactivar&id=${s.id}" class="btn btn-sm btn-danger">Desactivar</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/admin/sucursales?accion=activar&id=${s.id}" class="btn btn-sm btn-success">Activar</a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
