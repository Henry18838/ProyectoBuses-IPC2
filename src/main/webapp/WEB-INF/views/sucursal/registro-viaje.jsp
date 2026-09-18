<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro de <c:out value="${tipo}"/> - Viaje #${viaje.id}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-dark bg-dark px-3">
    <span class="navbar-brand">Sistema de Buses — Administrador de Sucursal</span>
    <a href="${pageContext.request.contextPath}/sucursal/viajes" class="btn btn-outline-light btn-sm">Volver a viajes</a>
</nav>

<div class="container mt-4" style="max-width: 600px;">
    <h4>
        <c:choose>
            <c:when test="${tipo == 'salida'}">Registrar salida</c:when>
            <c:otherwise>Registrar llegada</c:otherwise>
        </c:choose>
        — Viaje #${viaje.id}
    </h4>

    <div class="alert alert-warning">
        <strong>Atención:</strong> este registro es <strong>permanente</strong>. Una vez guardado
        no se podrá editar ni eliminar. Verifica bien los datos antes de guardar.
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card mb-3">
        <div class="card-body">
            <p class="mb-1"><strong>Bus:</strong> ${viaje.busPlaca}</p>
            <p class="mb-1"><strong>Chofer:</strong> ${viaje.choferNombre}</p>
            <p class="mb-0"><strong>Tipo de viaje:</strong> ${viaje.tipo}</p>
        </div>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/sucursal/registro-viaje">
        <input type="hidden" name="viajeId" value="${viaje.id}">
        <input type="hidden" name="busId" value="${viaje.busId}">
        <input type="hidden" name="tipo" value="${tipo}">

        <div class="mb-3">
            <label class="form-label">
                <c:choose>
                    <c:when test="${tipo == 'salida'}">Hora real de salida</c:when>
                    <c:otherwise>Hora real de llegada</c:otherwise>
                </c:choose>
            </label>
            <input type="datetime-local" name="horaReal" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">
                <c:choose>
                    <c:when test="${tipo == 'salida'}">Kilometraje al momento de partir</c:when>
                    <c:otherwise>Kilometraje final del bus</c:otherwise>
                </c:choose>
            </label>
            <input type="number" step="0.01" name="km" class="form-control" required>
        </div>

        <c:if test="${tipo != 'salida'}">
            <div class="mb-3">
                <label class="form-label">Gasto total en combustible del viaje (Q)</label>
                <input type="number" step="0.01" name="gastoCombustible" class="form-control" required>
            </div>
        </c:if>

        <button type="submit" class="btn btn-primary"
                onclick="return confirm('Este registro no se podrá modificar después. ¿Confirmas los datos?');">
            Guardar registro
        </button>
    </form>
</div>
</body>
</html>
