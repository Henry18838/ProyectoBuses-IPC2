-- ============================================================
-- Incremento 04: tabla ruta
-- ============================================================
USE buses_ipc2;

CREATE TABLE ruta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sucursal_origen_id INT NOT NULL,
    sucursal_destino_id INT NOT NULL,
    distancia_km DECIMAL(10,2) NOT NULL,
    precio_boleto DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_ruta_origen FOREIGN KEY (sucursal_origen_id) REFERENCES sucursal(id),
    CONSTRAINT fk_ruta_destino FOREIGN KEY (sucursal_destino_id) REFERENCES sucursal(id),
    CONSTRAINT chk_ruta_distinta CHECK (sucursal_origen_id <> sucursal_destino_id)
);
-- Nota: la ruta la gestiona el administrador de la sucursal_origen_id (es "su" ruta).

-- Dato de prueba: ruta de la sucursal 1 (Quetzaltenango) a la sucursal 2 (Guatemala)
INSERT INTO ruta (sucursal_origen_id, sucursal_destino_id, distancia_km, precio_boleto) VALUES
 (1, 2, 210.50, 75.00);
