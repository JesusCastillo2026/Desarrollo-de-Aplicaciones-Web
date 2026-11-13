-- Ejecutar en phpMyAdmin o MySQL con un usuario que pueda crear bases de datos.
-- Cada microservicio tiene una base propia. El perfil dev usa sufijo _dev.
CREATE DATABASE IF NOT EXISTS bd_categoria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_categoria_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_producto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_producto_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_cliente CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_cliente_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_pedido CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_pedido_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_paciente CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_paciente_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_medico CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_medico_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_cita CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_cita_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- DDL de la actividad clínica. Hibernate también crea/actualiza tablas en dev.
USE bd_paciente;
CREATE TABLE IF NOT EXISTS paciente (
  id_paciente BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  dni VARCHAR(8) NOT NULL UNIQUE,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  fecha_nacimiento DATE NULL,
  sexo CHAR(1) NULL,
  telefono VARCHAR(15) NULL,
  direccion VARCHAR(200) NULL,
  correo VARCHAR(100) NULL,
  estado CHAR(1) NULL
) ENGINE=InnoDB;

USE bd_medico;
CREATE TABLE IF NOT EXISTS especialidad (
  id_especialidad BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(200) NULL,
  estado CHAR(1) NULL
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS medico (
  id_medico BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  cmp VARCHAR(20) NOT NULL UNIQUE,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  telefono VARCHAR(15) NULL,
  correo VARCHAR(100) NULL,
  id_especialidad BIGINT NOT NULL,
  estado CHAR(1) NULL,
  CONSTRAINT fk_medico_especialidad FOREIGN KEY (id_especialidad) REFERENCES especialidad(id_especialidad)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS horario_medico (
  id_horario BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id_medico BIGINT NOT NULL,
  dia_semana VARCHAR(15) NOT NULL,
  hora_inicio TIME NULL,
  hora_fin TIME NULL,
  CONSTRAINT fk_horario_medico FOREIGN KEY (id_medico) REFERENCES medico(id_medico)
) ENGINE=InnoDB;

USE bd_cita;
CREATE TABLE IF NOT EXISTS cita (
  id_cita BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  fecha DATE NOT NULL,
  hora TIME NOT NULL,
  id_paciente BIGINT NOT NULL,
  id_medico BIGINT NOT NULL,
  motivo VARCHAR(300) NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA'
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS atencion (
  id_atencion BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id_cita BIGINT NOT NULL UNIQUE,
  diagnostico LONGTEXT NULL,
  tratamiento LONGTEXT NULL,
  observaciones LONGTEXT NULL,
  fecha_registro DATETIME NULL,
  CONSTRAINT fk_atencion_cita FOREIGN KEY (id_cita) REFERENCES cita(id_cita)
) ENGINE=InnoDB;
