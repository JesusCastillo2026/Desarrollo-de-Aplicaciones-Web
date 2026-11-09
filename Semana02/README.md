# Semana 02 — Laboratorio 2: Inversión de Control y Gestión de Dependencias

**Estado:** Laboratorio 2 completado. El proyecto está en [Semana_02_api_01_D](Semana_02_api_01_D/), y el PDF con la guía y evidencias se encuentra en [Documentos del curso](../Documentos/README.md).

## Objetivo

Comprender cómo Spring administra los objetos mediante IoC e inyecta dependencias, y aplicar esos conceptos en una aplicación con persistencia MySQL y arquitectura por capas. El sistema muestra usuarios y productos en vistas Thymeleaf y ofrece sus datos mediante endpoints REST.

## Cómo se logró

Se organizaron entidades, repositorios, servicios y controladores. `UsuarioRepository` y `ProductoRepository` heredan operaciones CRUD de `JpaRepository`; sus servicios consultan los datos y los controladores los exponen como JSON y como vistas web. La configuración conecta el proyecto a `demo_spring`, Hibernate actualiza el esquema y los datos de demostración se insertan solo si las tablas están vacías. También se incluyó un endpoint breve para observar la inyección del servicio por constructor.

## Herramientas

Java 17, Spring Boot, Gradle, Spring Data JPA, Hibernate, MySQL de XAMPP, Thymeleaf, Bootstrap y Visual Studio Code.

## Acceso y ejecución

Abrir `Semana_02_api_01_D` en Visual Studio Code, iniciar MySQL desde XAMPP y ejecutar en la terminal del proyecto:

```powershell
.\gradlew.bat bootRun
```

La aplicación crea `demo_spring` si el servidor MySQL acepta la creación de bases de datos, actualiza las tablas y carga ejemplos al iniciar. Alternativamente, el script `database/demo_spring.sql` crea la base, sus tablas y los datos demostrativos. Configuración local predeterminada: usuario `root`, contraseña vacía y puerto MySQL `3306`.

## Rutas principales

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Inicio con accesos a las vistas y APIs. |
| GET | `/usuario` | Ejemplo de IoC/DI con un usuario simulado. |
| GET | `/usuarios/usuarios-view` | Lista de usuarios en Thymeleaf. |
| GET | `/productos/vista` | Lista de productos en Thymeleaf. |
| GET | `/api/usuarios` | Usuarios en JSON desde MySQL. |
| GET | `/api/productos` | Productos en JSON desde MySQL. |

El detalle de clases, dependencias, ejecución y estructura está en el [README del proyecto](Semana_02_api_01_D/README.md).
