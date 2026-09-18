-- ============================================================
-- Incremento 05: tabla viaje
-- Ejecutar DESPUES de sql/incrementos/04_ruta.sql
-- ============================================================
USE buses_ipc2;

CREATE TABLE viaje (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('REGULAR','ALQUILER') NOT NULL,
    sucursal_id INT NOT NULL,          -- sucursal que gestiona el viaje (dueña del bus/chofer)
    ruta_id INT NULL,                  -- solo aplica si tipo = REGULAR
    bus_id INT NOT NULL,
    chofer_id INT NOT NULL,

    -- Solo aplican si tipo = ALQUILER
    origen_texto VARCHAR(150) NULL,
    destino_texto VARCHAR(150) NULL,
    num_pasajeros INT NULL,
    precio_alquiler DECIMAL(10,2) NULL,

    fecha_hora_salida_prog DATETIME NOT NULL,
    fecha_hora_llegada_prog DATETIME NOT NULL,

    CONSTRAINT fk_viaje_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursal(id),
    CONSTRAINT fk_viaje_ruta FOREIGN KEY (ruta_id) REFERENCES ruta(id),
    CONSTRAINT fk_viaje_bus FOREIGN KEY (bus_id) REFERENCES bus(id),
    CONSTRAINT fk_viaje_chofer FOREIGN KEY (chofer_id) REFERENCES chofer(id)
);

-- Dato de prueba: viaje regular usando la ruta de prueba (id 1), bus de prueba (id 1), chofer de prueba (id 1)
INSERT INTO viaje (tipo, sucursal_id, ruta_id, bus_id, chofer_id, fecha_hora_salida_prog, fecha_hora_llegada_prog)
VALUES ('REGULAR', 1, 1, 1, 1, '2026-09-20 08:00:00', '2026-09-20 11:30:00');
