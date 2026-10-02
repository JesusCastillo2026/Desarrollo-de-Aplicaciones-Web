# Laboratorio 07 — API REST con Spring Security

Proyecto independiente de Spring Boot que autentica usuarios de MySQL con HTTP Basic y autoriza solicitudes según los roles `ROLE_USER`, `ROLE_ADMIN` y `ROLE_MANAGER`.

## Implementación

- Las entidades `User` y `Role` se relacionan mediante la tabla `user_roles`.
- `CustomUserDetailsService` carga el usuario y convierte sus roles en autoridades de Spring Security.
- `SecurityConfig` define `SecurityFilterChain`, `AuthenticationManager` y el codificador BCrypt.
- `DataInitializer` agrega los roles y las cuentas de ejemplo solo si no existen.
- La actividad reemplazó `/public/hello` por `/api/free`, `/user/dashboard` por `/client/home` y `/admin/panel` por `/management/dashboard`; además creó `/manager/reportes`.

## Requisitos y ejecución local

Se necesita Java 17 y MySQL en XAMPP. El proyecto usa Spring Boot 4.1.1 y el Maven Wrapper incluido.

1. Iniciar MySQL desde XAMPP.
2. Crear la base de datos `securitydb` en phpMyAdmin, o ejecutar:

   ```sql
   CREATE DATABASE securitydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. Revisar `src/main/resources/application.properties`. La configuración incluida usa MySQL en `localhost:3306`, usuario `root` y contraseña vacía; ajustarla si la instalación local tiene otras credenciales.
4. Desde la carpeta del proyecto en Windows, iniciar la API:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

La API escucha en `http://localhost:8080`. JPA crea o actualiza las tablas dentro de `securitydb` al iniciar. El inicializador crea tres roles, tres usuarios y sus relaciones. Si una cuenta ya existe, no reemplaza su contraseña.

## Rutas

| Método y ruta | Permiso requerido | Respuesta esperada con acceso |
|---|---|---|
| `GET /api/free` | Ninguno | `200 OK` |
| `GET /client/home` | `ROLE_USER` o `ROLE_ADMIN` | `200 OK` |
| `GET /management/dashboard` | `ROLE_ADMIN` | `200 OK` |
| `GET /manager/reportes` | `ROLE_MANAGER` | `200 OK` |

## Cuentas de prueba locales

| Usuario | Contraseña | Rol |
|---|---|---|
| `user` | `User@S07` | `ROLE_USER` |
| `admin` | `Admin@S07` | `ROLE_ADMIN` |
| `manager` | `Manager@S07` | `ROLE_MANAGER` |

Estas cuentas son ejemplos para el laboratorio local. `DataInitializer` guarda sus contraseñas cifradas con BCrypt.

## Pruebas en Postman

Usar solicitudes GET sin body. En la pestaña **Authorization**, seleccionar **No Auth** o **Basic Auth** según el caso.

| Solicitud | Autorización | Estado esperado |
|---|---|---|
| `/api/free` | No Auth | `200` |
| `/client/home` | No Auth | `401` |
| `/client/home` | `user` | `200` |
| `/management/dashboard` | `user` | `403` |
| `/management/dashboard` | `admin` | `200` |
| `/manager/reportes` | No Auth | `401` |
| `/manager/reportes` | `manager` | `200` |
| `/manager/reportes` | `admin` | `403` |
| `/client/home` | `user` con contraseña incorrecta | `401` |

En phpMyAdmin se pueden revisar las tablas `roles`, `users` y `user_roles` de la base de datos `securitydb`.
