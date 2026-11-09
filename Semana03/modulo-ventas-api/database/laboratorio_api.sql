CREATE DATABASE IF NOT EXISTS laboratorio_api
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE laboratorio_api;

CREATE TABLE IF NOT EXISTS categorias (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(80) NOT NULL,
  descripcion VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT uk_categoria_nombre UNIQUE (nombre)
);

CREATE TABLE IF NOT EXISTS productos (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL,
  categoria_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE IF NOT EXISTS clientes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombres VARCHAR(80) NOT NULL,
  apellidos VARCHAR(80) NOT NULL,
  correo VARCHAR(120) NOT NULL,
  documento VARCHAR(20) NOT NULL,
  telefono VARCHAR(30),
  PRIMARY KEY (id),
  CONSTRAINT uk_cliente_correo UNIQUE (correo),
  CONSTRAINT uk_cliente_documento UNIQUE (documento)
);

CREATE TABLE IF NOT EXISTS empleados (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombres VARCHAR(80) NOT NULL,
  apellidos VARCHAR(80) NOT NULL,
  correo VARCHAR(120) NOT NULL,
  cargo VARCHAR(80) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_empleado_correo UNIQUE (correo)
);

CREATE TABLE IF NOT EXISTS ventas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha DATETIME(6) NOT NULL,
  estado VARCHAR(20) NOT NULL,
  cliente_id BIGINT NOT NULL,
  empleado_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
  CONSTRAINT fk_venta_empleado FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

CREATE TABLE IF NOT EXISTS detalles_venta (
  id BIGINT NOT NULL AUTO_INCREMENT,
  venta_id BIGINT NOT NULL,
  producto_id BIGINT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
  CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);
