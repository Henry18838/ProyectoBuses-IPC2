-- ============================================================
-- Incremento 06: registro_salida y registro_llegada (inmutables)
-- ============================================================
USE buses_ipc2;

CREATE TABLE registro_salida (
    id INT AUTO_INCREMENT PRIMARY KEY,
    viaje_id INT NOT NULL UNIQUE,
    hora_real_salida DATETIME NOT NULL,
    km_salida DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_salida_viaje FOREIGN KEY (viaje_id) REFERENCES viaje(id)
);

CREATE TABLE registro_llegada (
    id INT AUTO_INCREMENT PRIMARY KEY,
    viaje_id INT NOT NULL UNIQUE,
    hora_real_llegada DATETIME NOT NULL,
    km_llegada DECIMAL(10,2) NOT NULL,
    gasto_combustible DECIMAL(10,2) NOT NULL,
    monto_depreciacion DECIMAL(10,2) NOT NULL,  -- calculado y GUARDADO al momento del registro
    CONSTRAINT fk_llegada_viaje FOREIGN KEY (viaje_id) REFERENCES viaje(id)
);
