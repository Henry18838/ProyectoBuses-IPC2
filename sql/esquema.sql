-- ============================================================
-- Esquema inicial - Proyecto 1 IPC2 - Gestion de flota de buses
-- ============================================================
CREATE DATABASE IF NOT EXISTS buses_ipc2
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE buses_ipc2;

-- -------------------------------------------------------------
-- Tabla: sucursal
-- -------------------------------------------------------------
CREATE TABLE sucursal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    latitud DOUBLE NULL,
    longitud DOUBLE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- -------------------------------------------------------------
-- Tabla: usuario  (admin de sistema, admin de sucursal, cliente)
-- -------------------------------------------------------------
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN_SISTEMA','ADMIN_SUCURSAL','CLIENTE') NOT NULL,
    dpi VARCHAR(20) NULL,
    nit VARCHAR(20) NULL,
    telefono VARCHAR(20) NULL,
    direccion VARCHAR(200) NULL,
    saldo_cartera DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    sucursal_id INT NULL,              -- solo aplica si rol = ADMIN_SUCURSAL
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_usuario_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursal(id)
);

-- -------------------------------------------------------------
-- Tabla: configuracion (monto de depreciacion por km)
-- -------------------------------------------------------------
CREATE TABLE configuracion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    monto_depreciacion_km DECIMAL(10,4) NOT NULL,
    fecha_actualizacion DATE NOT NULL
);

-- -------------------------------------------------------------
-- Datos de prueba
-- -------------------------------------------------------------
INSERT INTO sucursal (nombre, direccion) VALUES
 ('Quetzaltenango Central', '4a calle 12-30 zona 1, Quetzaltenango'),
 ('Guatemala Terminal', '18 calle 4-35 zona 1, Guatemala');

-- Cuenta de prueba: correo admin@buses.com / password: admin123
INSERT INTO usuario (nombre_completo, correo, password_hash, rol, activo) VALUES
 ('Admin Sistema', 'admin@buses.com', '$2a$10$7mzqkgEvuvhx1aYCbGQplO10VLqaYAs2DQH62JF73Kqbfgje4Yn6.', 'ADMIN_SISTEMA', 1);

INSERT INTO configuracion (monto_depreciacion_km, fecha_actualizacion) VALUES (0.50, CURDATE());
