CREATE DATABASE IF NOT EXISTS demo_spring
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE demo_spring;

CREATE TABLE IF NOT EXISTS usuario (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  email VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS producto (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  precio DOUBLE,
  PRIMARY KEY (id)
);

INSERT INTO usuario (nombre, email)
SELECT 'Jesus', 'jesus@gmail.com'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'jesus@gmail.com');

INSERT INTO usuario (nombre, email)
SELECT 'Joe', 'joe@gmail.com'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'joe@gmail.com');

INSERT INTO producto (nombre, precio)
SELECT 'Laptop HP Victus 15', 3500.5
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Laptop HP Victus 15');

INSERT INTO producto (nombre, precio)
SELECT 'Xiaomi 15', 3150.0
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Xiaomi 15');

INSERT INTO producto (nombre, precio)
SELECT 'Audífonos Redmi', 180.0
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Audífonos Redmi');
