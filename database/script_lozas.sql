-- ============================================================================
-- PROYECTO: SISTEMA DE GESTIÓN DE ALQUILER DE LOZAS DEPORTIVAS
-- CURSO: PROGRAMACIÓN ORIENTADA A OBJETOS
-- BASE DE DATOS: MYSQL
-- ============================================================================

DROP DATABASE IF EXISTS db_gestion_lozas;
CREATE DATABASE db_gestion_lozas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_gestion_lozas;

-- 1. Tabla de Usuarios (Personal del complejo: Administrador / Recepcionista)
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombre VARCHAR(60) NOT NULL,
    apellido VARCHAR(60) NOT NULL,
    telefono VARCHAR(15),
    email VARCHAR(100),
    username VARCHAR(30) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,        -- 'ADMIN' o 'RECEP'
    detalle_rol VARCHAR(50) NOT NULL -- Cargo o Turno
);

-- 2. Tabla de Clientes
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombre VARCHAR(60) NOT NULL,
    apellido VARCHAR(60) NOT NULL,
    telefono VARCHAR(15),
    email VARCHAR(100)
);

-- 3. Tabla de Lozas Deportivas (Fútbol / Vóley)
CREATE TABLE lozas (
    id_loza INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    tarifa_hora DECIMAL(10, 2) NOT NULL,
    tipo_deporte VARCHAR(20) NOT NULL,   -- 'FUTBOL' o 'VOLEY'
    especificacion VARCHAR(40) NOT NULL, -- 'Sintético', 'Natural', 'Parquet'
    estado VARCHAR(20) DEFAULT 'Disponible'
);

-- 4. Tabla de Reservas (Incluye los datos del Pago compuesto)
CREATE TABLE reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_loza INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Confirmada',
    monto_total DECIMAL(10, 2) NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL,
    comprobante VARCHAR(30) NOT NULL,
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE CASCADE,
    CONSTRAINT fk_reserva_loza FOREIGN KEY (id_loza) REFERENCES lozas(id_loza)
);

-- ============================================================================
-- CARGA DE DATOS INICIALES (DATOS DE PRUEBA)
-- ============================================================================

INSERT INTO usuarios (dni, nombre, apellido, telefono, email, username, contrasena, rol, detalle_rol) VALUES
('11111111', 'Carlos', 'Gómez', '900000001', 'admin@lozas.com', 'admin', 'admin123', 'ADMIN', 'Gerente General'),
('22222222', 'Lucía', 'Vargas', '900000002', 'lucia@lozas.com', 'recep', 'recep123', 'RECEP', 'Turno Tarde');

INSERT INTO lozas (nombre, tarifa_hora, tipo_deporte, especificacion) VALUES
('Cancha Sintética El Gol', 40.00, 'FUTBOL', 'Sintético'),
('Cancha Cemento Futsal', 25.00, 'FUTBOL', 'Natural'),
('Losa Principal Vóley', 30.00, 'VOLEY', 'Parquet');

INSERT INTO clientes (dni, nombre, apellido, telefono, email) VALUES
('71234567', 'Marcos', 'Prado', '987654321', 'marcos@gmail.com'),
('44556677', 'Rosa', 'Mendoza', '912345678', 'rosa@gmail.com'),
('75432198', 'Diego', 'Salazar', '945612378', 'diego.s@gmail.com');

INSERT INTO reservas (id_cliente, id_loza, fecha, hora_inicio, hora_fin, estado, monto_total, metodo_pago, comprobante) VALUES
(1, 1, '2026-11-20', '18:00:00', '20:00:00', 'Confirmada', 110.00, 'Yape', 'B001-0001'),
(2, 3, '2026-11-21', '16:00:00', '18:00:00', 'Confirmada', 60.00, 'Efectivo', 'B001-0002');
