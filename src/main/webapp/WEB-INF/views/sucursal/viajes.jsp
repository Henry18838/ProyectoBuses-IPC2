<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Viajes - Administrador de Sucursal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-dark bg-dark px-3">
    <span class="navbar-brand">Sistema de Buses — Administrador de Sucursal</span>
    <div>
        <a href="${pageContext.request.contextPath}/sucursal/buses" class="btn btn-outline-light btn-sm">Buses</a>
        <a href="${pageContext.request.contextPath}/sucursal/choferes" class="btn btn-outline-light btn-sm">Choferes</a>
        <a href="${pageContext.request.contextPath}/sucursal/rutas" class="btn btn-outline-light btn-sm">Rutas</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Cerrar sesión</a>
    </div>
</nav>

<div class="container mt-4">
    <h4>Gestión de viajes</h4>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card mb-4">
        <div class="card-body">
            <h6 class="card-title">
                <c:choose>
                    <c:when test="${not empty viaje}">Editar viaje (tipo: ${viaje.tipo}, no modificable)</c:when>
                    <c:otherwise>Nuevo viaje</c:otherwise>
                </c:choose>
            </h6>
            <form method="post" action="${pageContext.request.contextPath}/sucursal/viajes" class="row g-2" id="formViaje">
                <input type="hidden" name="id" value="${viaje.id}">

                <div class="col-md-3">
                    <label class="form-label">Tipo de viaje</label>
                    <c:choose>
                        <c:when test="${not empty viaje}">
                            <input type="text" class="form-control" value="${viaje.tipo}" disabled>
                        </c:when>
                        <c:otherwise>
                            <select name="tipo" id="tipoSelect" class="form-select" onchange="actualizarCampos()" required>
                                <option value="REGULAR">Regular</option>
                                <option value="ALQUILER">Alquiler privado</option>
                            </select>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="col-md-3">
                    <label class="form-label">Bus</label>
                    <select name="busId" class="form-select" required>
                        <c:forEach var="b" items="${buses}">
                            <c:if test="${b.activo}">
                                <option value="${b.id}" ${viaje.busId == b.id ? 'selected' : ''}>${b.numPlaca} (${b.marca})</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Chofer</label>
                    <select name="choferId" class="form-select" required>
                        <c:forEach var="c" items="${choferes}">
                            <c:if test="${c.activo}">
                                <option value="${c.id}" ${viaje.choferId == c.id ? 'selected' : ''}>${c.nombreCompleto}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>

                <!-- Campos exclusivos de REGULAR -->
                <div class="col-md-3" id="grupoRuta">
                    <label class="form-label">Ruta</label>
                    <select name="rutaId" class="form-select">
                        <c:forEach var="r" items="${rutas}">
                            <option value="${r.id}" ${viaje.rutaId == r.id ? 'selected' : ''}>${r.sucursalOrigenNombre} -> ${r.sucursalDestinoNombre}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Campos exclusivos de ALQUILER -->
                <div class="col-md-3" id="grupoOrigen">
                    <label class="form-label">Origen (alquiler)</label>
                    <input type="text" name="origenTexto" class="form-control" value="${viaje.origenTexto}">
                </div>
                <div class="col-md-3" id="grupoDestino">
                    <label class="form-label">Destino (alquiler)</label>
                    <input type="text" name="destinoTexto" class="form-control" value="${viaje.destinoTexto}">
                </div>
                <div class="col-md-2" id="grupoPasajeros">
                    <label class="form-label">Pasajeros</label>
                    <input type="number" name="numPasajeros" class="form-control" value="${viaje.numPasajeros}">
                </div>
                <div class="col-md-2" id="grupoPrecio">
                    <label class="form-label">Precio alquiler (Q)</label>
                    <input type="number" step="0.01" name="precioAlquiler" class="form-control" value="${viaje.precioAlquiler}">
                </div>

                <div class="col-md-3">
                    <label class="form-label">Fecha/hora salida</label>
                    <input type="datetime-local" name="fechaHoraSalidaProg" class="form-control" value="${viaje.fechaHoraSalidaProg}" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Fecha/hora llegada estimada</label>
                    <input type="datetime-local" name="fechaHoraLlegadaProg" class="form-control" value="${viaje.fechaHoraLlegadaProg}" required>
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
            <th>Tipo</th>
            <th>Ruta / Origen-Destino</th>
            <th>Bus</th>
            <th>Chofer</th>
            <th>Salida prog.</th>
            <th>Llegada prog.</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="v" items="${viajes}">
            <tr>
                <td>${v.tipo}</td>
                <td>
                    <c:choose>
                        <c:when test="${v.tipo == 'REGULAR'}">${v.rutaDescripcion}</c:when>
                        <c:otherwise>${v.origenTexto} -> ${v.destinoTexto} (${v.numPasajeros} pax)</c:otherwise>
                    </c:choose>
                </td>
                <td>${v.busPlaca}</td>
                <td>${v.choferNombre}</td>
                <td>${v.fechaHoraSalidaProg}</td>
                <td>${v.fechaHoraLlegadaProg}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/sucursal/viajes?accion=editar&id=${v.id}" class="btn btn-sm btn-warning">Editar</a>
                    <a href="${pageContext.request.contextPath}/sucursal/viajes?accion=eliminar&id=${v.id}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('¿Eliminar este viaje?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    function actualizarCampos() {
        var tipoSelect = document.getElementById('tipoSelect');
        var tipo = tipoSelect ? tipoSelect.value : '${viaje.tipo}';
        var esRegular = (tipo === 'REGULAR');

        document.getElementById('grupoRuta').style.display = esRegular ? 'block' : 'none';
        document.getElementById('grupoOrigen').style.display = esRegular ? 'none' : 'block';
        document.getElementById('grupoDestino').style.display = esRegular ? 'none' : 'block';
        document.getElementById('grupoPasajeros').style.display = esRegular ? 'none' : 'block';
        document.getElementById('grupoPrecio').style.display = esRegular ? 'none' : 'block';
    }
    // Ejecutar al cargar la pagina para dejar los campos correctos desde el inicio
    actualizarCampos();
</script>
</body>
</html>
