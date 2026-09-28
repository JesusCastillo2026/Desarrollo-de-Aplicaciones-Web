# Semana 06 — Laboratorio 06: Implementación con AOP

## Objetivo
Aplicar programación orientada a aspectos (AOP) para registrar auditoría de operaciones realizadas sobre productos sin concentrar esa lógica en cada endpoint.

## Cómo se logró
Se definieron aspectos de auditoría con pointcuts y advice que observan operaciones para guardar, actualizar, eliminar y listar productos. El aspecto registra acciones y datos del resultado, como el identificador del producto o la cantidad de registros. La API mantiene la separación por controlador, servicio y repositorio.

## Herramientas
Java 17, Spring Boot, Spring AOP/AspectJ, Spring Web MVC, Spring Data JPA, Bean Validation, MySQL y Maven. La configuración y dependencias se identificaron en el proyecto.

## Código fuente
La copia está en [api-productos-lab06](api-productos-lab06/). [Repositorio original](https://github.com/JesusCastillo2026/api-productos-lab06).
