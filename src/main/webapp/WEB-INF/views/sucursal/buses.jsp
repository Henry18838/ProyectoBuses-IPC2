<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Buses - Administrador de Sucursal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-dark bg-dark px-3">
    <span class="navbar-brand">Sistema de Buses — Administrador de Sucursal</span>
    <div>
        <a href="${pageContext.request.contextPath}/sucursal/choferes" class="btn btn-outline-light btn-sm">Choferes</a>
        <a href="${pageContext.request.contextPath}/sucursal/rutas" class="btn btn-outline-light btn-sm">Rutas</a>
        <a href="${pageContext.request.contextPath}/sucursal/viajes" class="btn btn-outline-light btn-sm">Viajes</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Cerrar sesión</a>
    </div>
</nav>

<div class="container mt-4">
    <h4>Gestión de buses</h4>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card mb-4">
        <div class="card-body">
            <h6 class="card-title">
                <c:choose>
                    <c:when test="${not empty bus}">Editar bus</c:when>
                    <c:otherwise>Registrar nuevo bus</c:otherwise>
                </c:choose>
            </h6>
            <form method="post" action="${pageContext.request.contextPath}/sucursal/buses" class="row g-2">
                <input type="hidden" name="id" value="${bus.id}">

                <div class="col-md-3">
                    <label class="form-label">Placa</label>
                    <input type="text" name="numPlaca" class="form-control" value="${bus.numPlaca}"
                           ${not empty bus ? 'readonly' : ''} required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Marca</label>
                    <input type="text" name="marca" class="form-control" value="${bus.marca}" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Modelo</label>
                    <input type="text" name="modelo" class="form-control" value="${bus.modelo}" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Año</label>
                    <input type="number" name="anioFabricacion" class="form-control" value="${bus.anioFabricacion}" required>
                </div>

                <div class="col-md-3">
                    <label class="form-label">Capacidad de pasajeros</label>
                    <input type="number" name="capacidadPasajeros" class="form-control" value="${bus.capacidadPasajeros}" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Kilometraje actual</label>
                    <input type="number" step="0.01" name="kilometrajeActual" class="form-control" value="${bus.kilometrajeActual}" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Estado operativo</label>
                    <select name="estadoOperativo" class="form-select" required>
                        <option value="OPERATIVO" ${bus.estadoOperativo == 'OPERATIVO' ? 'selected' : ''}>Operativo</option>
                        <option value="EN_MANTENIMIENTO" ${bus.estadoOperativo == 'EN_MANTENIMIENTO' ? 'selected' : ''}>En mantenimiento</option>
                        <option value="FUERA_DE_SERVICIO" ${bus.estadoOperativo == 'FUERA_DE_SERVICIO' ? 'selected' : ''}>Fuera de servicio</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">URL de foto (opcional)</label>
                    <input type="text" name="foto" class="form-control" value="${bus.foto}" placeholder="https://...">
                </div>

                <div class="col-12 mt-2">
                    <button type="submit" class="btn btn-primary">Guardar</button>
                </div>
            </form>
        </div>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/sucursal/buses" class="row g-2 mb-3">
        <div class="col-md-3">
            <select name="estado" class="form-select" onchange="this.form.submit()">
                <option value="">Todos los estados</option>
                <option value="OPERATIVO" ${filtroEstado == 'OPERATIVO' ? 'selected' : ''}>Operativo</option>
                <option value="EN_MANTENIMIENTO" ${filtroEstado == 'EN_MANTENIMIENTO' ? 'selected' : ''}>En mantenimiento</option>
                <option value="FUERA_DE_SERVICIO" ${filtroEstado == 'FUERA_DE_SERVICIO' ? 'selected' : ''}>Fuera de servicio</option>
            </select>
        </div>
    </form>

    <table class="table table-striped table-bordered bg-white">
        <thead class="table-dark">
        <tr>
            <th>Placa</th>
            <th>Marca / Modelo</th>
            <th>Año</th>
            <th>Capacidad</th>
            <th>Km actual</th>
            <th>Estado</th>
            <th>Activo</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="b" items="${buses}">
            <tr>
                <td>${b.numPlaca}</td>
                <td>${b.marca} ${b.modelo}</td>
                <td>${b.anioFabricacion}</td>
                <td>${b.capacidadPasajeros}</td>
                <td>${b.kilometrajeActual}</td>
                <td>${b.estadoOperativo}</td>
                <td>
                    <c:choose>
                        <c:when test="${b.activo}"><span class="badge bg-success">Sí</span></c:when>
                        <c:otherwise><span class="badge bg-secondary">No</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/sucursal/buses?accion=editar&id=${b.id}" class="btn btn-sm btn-warning">Editar</a>
                    <c:choose>
                        <c:when test="${b.activo}">
                            <a href="${pageContext.request.contextPath}/sucursal/buses?accion=desactivar&id=${b.id}" class="btn btn-sm btn-danger">Desactivar</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/sucursal/buses?accion=activar&id=${b.id}" class="btn btn-sm btn-success">Activar</a>
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
