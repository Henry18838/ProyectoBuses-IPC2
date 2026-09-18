-- ============================================================
-- Incremento 03: tabla chofer
-- Ejecutar DESPUES de sql/incrementos/02_bus.sql
-- ============================================================
USE buses_ipc2;

CREATE TABLE chofer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    foto VARCHAR(255) NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    num_licencia VARCHAR(30) NOT NULL UNIQUE,
    tipo_licencia VARCHAR(10) NOT NULL,
    fecha_vencimiento_licencia DATE NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    salario_base DECIMAL(10,2) NOT NULL,
    sucursal_id INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_chofer_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursal(id)
);

-- Dato de prueba
INSERT INTO chofer (nombre_completo, num_licencia, tipo_licencia, fecha_vencimiento_licencia, telefono, salario_base, sucursal_id) VALUES
 ('Carlos Ramírez López', 'A-12345678', 'A', '2027-06-30', '55512345', 3500.00, 1);
