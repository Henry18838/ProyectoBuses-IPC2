<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Rutas - Administrador de Sucursal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-dark bg-dark px-3">
    <span class="navbar-brand">Sistema de Buses — Administrador de Sucursal</span>
    <div>
        <a href="${pageContext.request.contextPath}/sucursal/buses" class="btn btn-outline-light btn-sm">Buses</a>
        <a href="${pageContext.request.contextPath}/sucursal/choferes" class="btn btn-outline-light btn-sm">Choferes</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Cerrar sesión</a>
    </div>
</nav>

<div class="container mt-4">
    <h4>Gestión de rutas (origen: tu sucursal)</h4>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card mb-4">
        <div class="card-body">
            <h6 class="card-title">
                <c:choose>
                    <c:when test="${not empty ruta}">Editar ruta</c:when>
                    <c:otherwise>Nueva ruta</c:otherwise>
                </c:choose>
            </h6>
            <form method="post" action="${pageContext.request.contextPath}/sucursal/rutas" class="row g-2">
                <input type="hidden" name="id" value="${ruta.id}">

                <div class="col-md-4">
                    <label class="form-label">Sucursal destino</label>
                    <select name="sucursalDestinoId" class="form-select" required>
                        <c:forEach var="s" items="${sucursales}">
                            <c:if test="${s.id != sucursalPropiaId}">
                                <option value="${s.id}" ${ruta.sucursalDestinoId == s.id ? 'selected' : ''}>${s.nombre}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Distancia (km)</label>
                    <input type="number" step="0.01" name="distanciaKm" class="form-control" value="${ruta.distanciaKm}" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Precio del boleto (Q)</label>
                    <input type="number" step="0.01" name="precioBoleto" class="form-control" value="${ruta.precioBoleto}" required>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">Guardar</button>
                </div>
            </form>
        </div>
    </div>

    <table class="table table-striped table-bordered bg-white">
        <thead class="table-dark">
        <tr>
            <th>Origen</th>
            <th>Destino</th>
            <th>Distancia (km)</th>
            <th>Precio boleto</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${rutas}">
            <tr>
                <td>${r.sucursalOrigenNombre}</td>
                <td>${r.sucursalDestinoNombre}</td>
                <td>${r.distanciaKm}</td>
                <td>Q${r.precioBoleto}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/sucursal/rutas?accion=editar&id=${r.id}" class="btn btn-sm btn-warning">Editar</a>
                    <a href="${pageContext.request.contextPath}/sucursal/rutas?accion=eliminar&id=${r.id}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('¿Eliminar esta ruta? Esta acción no se puede deshacer.');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
