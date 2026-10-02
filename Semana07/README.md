# Semana 07 — Laboratorio 07: seguridad en una API REST

## Objetivo

Proteger una API REST con Spring Security, autenticación HTTP Basic y autorización por roles. Los usuarios y roles se guardan en MySQL, y las contraseñas se cifran con BCrypt.

## Trabajo realizado

- Se crearon las entidades `User` y `Role` y su relación muchos a muchos mediante `user_roles`.
- `CustomUserDetailsService` busca usuarios en la base de datos y entrega sus roles a Spring Security.
- `SecurityConfig` configura `SecurityFilterChain`, `AuthenticationManager`, HTTP Basic y `BCryptPasswordEncoder`.
- `DataInitializer` carga los roles `ROLE_USER`, `ROLE_ADMIN` y `ROLE_MANAGER` y tres usuarios de prueba cuando aún no existen.
- Se actualizaron las rutas de la actividad y se añadió `/manager/reportes`.

## Rutas y permisos

| Ruta GET | Acceso |
|---|---|
| `/api/free` | Público |
| `/client/home` | `ROLE_USER` o `ROLE_ADMIN` |
| `/management/dashboard` | `ROLE_ADMIN` |
| `/manager/reportes` | `ROLE_MANAGER` |

La matriz de pruebas para Postman contempla acceso permitido (`200`), falta de autenticación (`401`) y acceso con un rol insuficiente (`403`).

## Herramientas

Java 17, Spring Boot 4.1.1, Spring Security, Spring Web MVC, Spring Data JPA, MySQL de XAMPP, Maven, Visual Studio Code y Postman.

## Código fuente

El proyecto está en [lab07-security](lab07-security/). Su [README](lab07-security/README.md) incluye la configuración local, las cuentas de prueba y los casos para Postman.
