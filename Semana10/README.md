# Semana 10 - Laboratorio de ambientes Spring

**Estado:** Código implementado en los dos casos de la guía y compilación Maven verificada. La ejecución contra MySQL y las solicitudes Postman quedan pendientes de validar con MySQL de XAMPP iniciado.

## Objetivo

Configurar múltiples ambientes con Spring Profiles y variables de entorno. Se conserva una copia independiente de la API de productos de Semana 5 y se implementa el caso CRUD de empleados solicitado en la actividad.

## Proyectos

- [API de productos por perfil](api-productos-perfiles/README.md): copia del proyecto de Semana 5, con perfiles `dev`, `qa`, `test` y `prod`, bases `productos_*` y endpoint `/config`.
- [Gestión de empleados por ambiente](gestion-empleados-api/README.md): CRUD REST `/empleados` y endpoint `/configuracion` con ambiente, perfil, puerto y base de datos activos.

## Flujo de configuración

`PROFILE_ACTIVE` selecciona un perfil. Cada archivo `application-<perfil>.properties` define el puerto y el nombre del ambiente; `DB_URL`, `DB_USER` y `DB_PASSWORD` eligen la conexión. Desde VS Code, **Run and Debug** contiene una configuración lista para cada perfil.

## Evidencias e informe

El informe se guarda en [Documentos/Laboratorios/Semana 10/Semana 10.pdf](../Documentos/Laboratorios/Semana%2010/Semana%2010.pdf). Incluye los bloques principales de código explicados, el flujo de configuración y el estado comprobable. Las capturas de ejecución real de VS Code, MySQL/phpMyAdmin y Postman deben añadirse cuando se pueda iniciar MySQL; no se sustituyen por imágenes simuladas.
