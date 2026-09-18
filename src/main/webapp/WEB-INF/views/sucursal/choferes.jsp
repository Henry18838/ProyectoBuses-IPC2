<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Choferes - Administrador de Sucursal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-dark bg-dark px-3">
    <span class="navbar-brand">Sistema de Buses — Administrador de Sucursal</span>
    <div>
        <a href="${pageContext.request.contextPath}/sucursal/buses" class="btn btn-outline-light btn-sm">Buses</a>
        <a href="${pageContext.request.contextPath}/sucursal/rutas" class="btn btn-outline-light btn-sm">Rutas</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Cerrar sesión</a>
    </div>
</nav>

<div class="container mt-4">
    <h4>Gestión de choferes</h4>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card mb-4">
        <div class="card-body">
            <h6 class="card-title">
                <c:choose>
                    <c:when test="${not empty chofer}">Editar chofer</c:when>
                    <c:otherwise>Registrar nuevo chofer</c:otherwise>
                </c:choose>
            </h6>
            <form method="post" action="${pageContext.request.contextPath}/sucursal/choferes" class="row g-2">
                <input type="hidden" name="id" value="${chofer.id}">

                <div class="col-md-4">
                    <label class="form-label">Nombre completo</label>
                    <input type="text" name="nombreCompleto" class="form-control" value="${chofer.nombreCompleto}" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Número de licencia</label>
                    <input type="text" name="numLicencia" class="form-control" value="${chofer.numLicencia}"
                           ${not empty chofer ? 'readonly' : ''} required>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Tipo</label>
                    <input type="text" name="tipoLicencia" class="form-control" value="${chofer.tipoLicencia}" placeholder="A, B, C..." required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Vencimiento licencia</label>
                    <input type="date" name="fechaVencimientoLicencia" class="form-control" value="${chofer.fechaVencimientoLicencia}" required>
                </div>

                <div class="col-md-3">
                    <label class="form-label">Teléfono</label>
                    <input type="text" name="telefono" class="form-control" value="${chofer.telefono}" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Salario base por viaje</label>
                    <input type="number" step="0.01" name="salarioBase" class="form-control" value="${chofer.salarioBase}" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label">URL de foto (opcional)</label>
                    <input type="text" name="foto" class="form-control" value="${chofer.foto}" placeholder="https://...">
                </div>

                <div class="col-12 mt-2">
                    <button type="submit" class="btn btn-primary">Guardar</button>
                </div>
            </form>
        </div>
    </div>

    <table class="table table-striped table-bordered bg-white">
        <thead class="table-dark">
        <tr>
            <th>Nombre</th>
            <th>Licencia</th>
            <th>Tipo</th>
            <th>Vence</th>
            <th>Teléfono</th>
            <th>Salario base</th>
            <th>Activo</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${choferes}">
            <tr>
                <td>${c.nombreCompleto}</td>
                <td>${c.numLicencia}</td>
                <td>${c.tipoLicencia}</td>
                <td>${c.fechaVencimientoLicencia}</td>
                <td>${c.telefono}</td>
                <td>Q${c.salarioBase}</td>
                <td>
                    <c:choose>
                        <c:when test="${c.activo}"><span class="badge bg-success">Sí</span></c:when>
                        <c:otherwise><span class="badge bg-secondary">No</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/sucursal/choferes?accion=editar&id=${c.id}" class="btn btn-sm btn-warning">Editar</a>
                    <c:choose>
                        <c:when test="${c.activo}">
                            <a href="${pageContext.request.contextPath}/sucursal/choferes?accion=desactivar&id=${c.id}" class="btn btn-sm btn-danger">Desactivar</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/sucursal/choferes?accion=activar&id=${c.id}" class="btn btn-sm btn-success">Activar</a>
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
