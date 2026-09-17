-- ============================================================
-- Incremento 02: tabla bus + usuario administrador de sucursal
-- Ejecutar DESPUES de sql/esquema.sql
-- ============================================================
USE buses_ipc2;

-- -------------------------------------------------------------
-- Tabla: bus
-- -------------------------------------------------------------
CREATE TABLE bus (
    id INT AUTO_INCREMENT PRIMARY KEY,
    foto VARCHAR(255) NULL,              -- por ahora una URL/ruta de imagen, no BLOB
    num_placa VARCHAR(20) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio_fabricacion INT NOT NULL,
    capacidad_pasajeros INT NOT NULL,
    estado_operativo ENUM('OPERATIVO','EN_MANTENIMIENTO','FUERA_DE_SERVICIO') NOT NULL DEFAULT 'OPERATIVO',
    kilometraje_actual DECIMAL(10,2) NOT NULL DEFAULT 0,
    sucursal_id INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_bus_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursal(id)
);

-- -------------------------------------------------------------
-- Usuario de prueba: administrador de sucursal (ligado a sucursal 1)
-- correo: sucursal1@buses.com / password: admin123
-- -------------------------------------------------------------
INSERT INTO usuario (nombre_completo, correo, password_hash, rol, sucursal_id, activo) VALUES
 ('Admin Quetzaltenango', 'sucursal1@buses.com', '$2a$10$F8Z8jsLjdS1BF.uRBmk.a.3SxBdFj9NTCtBwnUCa4Fxt9fM/od5gu', 'ADMIN_SUCURSAL', 1, 1);

-- -------------------------------------------------------------
-- Dato de prueba: un bus para la sucursal 1
-- -------------------------------------------------------------
INSERT INTO bus (num_placa, marca, modelo, anio_fabricacion, capacidad_pasajeros, estado_operativo, kilometraje_actual, sucursal_id) VALUES
 ('P-123ABC', 'Mercedes-Benz', 'OF-1721', 2020, 45, 'OPERATIVO', 15000, 1);
