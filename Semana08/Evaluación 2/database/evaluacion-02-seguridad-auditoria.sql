-- Ampliación aditiva para Evaluación 02. No elimina ni reemplaza tablas de Evaluación 01.
-- La aplicación también crea/actualiza estas tablas mediante Hibernate (ddl-auto=update).
USE hospital_db;

CREATE TABLE IF NOT EXISTS roles (
    id_rol BIGINT NOT NULL AUTO_INCREMENT,
    nombre_rol VARCHAR(40) NOT NULL,
    descripcion VARCHAR(180) NOT NULL,
    activo BIT NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_rol),
    CONSTRAINT uk_roles_nombre UNIQUE (nombre_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT,
    nombre_usuario VARCHAR(60) NOT NULL,
    correo VARCHAR(120) NOT NULL,
    nombres VARCHAR(80) NOT NULL,
    apellidos VARCHAR(80) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    activo BIT NOT NULL DEFAULT b'1',
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (id_usuario),
    CONSTRAINT uk_usuarios_nombre_usuario UNIQUE (nombre_usuario),
    CONSTRAINT uk_usuarios_correo UNIQUE (correo),
    CONSTRAINT fk_usuarios_rol FOREIGN KEY (rol_id) REFERENCES roles(id_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS auditoria_operaciones (
    id_auditoria BIGINT NOT NULL AUTO_INCREMENT,
    usuario VARCHAR(80) NOT NULL,
    fecha_hora DATETIME(6) NOT NULL,
    operacion VARCHAR(40) NOT NULL,
    entidad VARCHAR(80) NOT NULL,
    registro_id VARCHAR(80) NULL,
    detalle VARCHAR(180) NULL,
    PRIMARY KEY (id_auditoria),
    INDEX idx_auditoria_fecha (fecha_hora),
    INDEX idx_auditoria_entidad_registro (entidad, registro_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
