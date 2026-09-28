# Semana 06 — Laboratorio 06: AOP y control de acceso

## Objetivo

Aplicar programación orientada a aspectos (AOP) para auditar las operaciones de productos y añadir validación de acceso por rol en la API REST.

## Versión 1: auditoría con AOP

- Se creó el registro `auditoria_log` para guardar la acción, el método, la fecha y el detalle de cada evento.
- Los aspectos interceptan las operaciones para crear, actualizar, eliminar y listar productos.
- La auditoría registra información dinámica, como el ID del producto afectado y la cantidad de productos listados.
- `ErrorAspect` captura excepciones de los servicios y registra el evento de error en la base de datos.
- Se documentaron pruebas CRUD en Postman y la verificación de los registros en MySQL.

## Versión 2: validación por roles

- La creación de productos valida el encabezado HTTP `Rol` y permite los roles `ADMIN` y `USER`, según la actividad de la segunda entrega.
- Si falta el encabezado, la API devuelve HTTP 401; si el rol no está permitido, devuelve HTTP 403.
- Se añadieron excepciones específicas y manejadores globales para esas respuestas.
- Se conserva la auditoría AOP y la inyección por constructor en los servicios y aspectos.

Los informes del laboratorio documentan ambas etapas y sus evidencias de Postman y base de datos.

## Herramientas

Java 17, Spring Boot, Spring AOP/AspectJ, Spring Web MVC, Spring Data JPA, Bean Validation, MySQL y Maven.

## Código fuente

El proyecto de esta semana está en [api-productos-lab06](api-productos-lab06/). El repositorio original es [api-productos-lab06 en GitHub](https://github.com/JesusCastillo2026/api-productos-lab06).
