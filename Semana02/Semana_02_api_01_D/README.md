# Laboratorio 2 — IoC, dependencias y persistencia

Aplicación Spring Boot para demostrar inversión de control e inyección de dependencias y consultar usuarios y productos persistidos en MySQL.

## Requisitos

- Java 17 o superior.
- MySQL iniciado desde XAMPP en el puerto `3306`.
- Visual Studio Code con soporte para Java.

## Ejecución

Desde una terminal abierta en esta carpeta:

```powershell
.\gradlew.bat bootRun
```

La aplicación usa `root` sin contraseña por defecto y la base `demo_spring`. `createDatabaseIfNotExist=true` crea esa base al iniciar si el usuario tiene permisos; Hibernate crea o actualiza las tablas. Si se prefiere preparar la base manualmente, ejecutar `database/demo_spring.sql` desde phpMyAdmin o la consola MySQL antes de arrancar.

Cuando la terminal muestre que Spring Boot inició en el puerto `8080`, abrir `http://localhost:8080/`.

## Rutas

| Método | Ruta | Uso |
|---|---|---|
| GET | `/` | Inicio de la aplicación. |
| GET | `/usuario` | Ejemplo simple de IoC y DI. |
| GET | `/usuarios/usuarios-view` | Vista web de usuarios. |
| GET | `/productos/vista` | Vista web de productos. |
| GET | `/api/usuarios` | Listado JSON de usuarios. |
| GET | `/api/productos` | Listado JSON de productos. |

## Arquitectura

- `Model`: entidades JPA `Usuario` y `Producto`.
- `Repository`: interfaces `JpaRepository` para persistencia.
- `Services`: lógica de consulta y servicio demostrativo para el ejemplo IoC/DI.
- `Controller`: rutas para vistas Thymeleaf y recursos REST.
- `Config/DataInitializer`: carga ejemplos solo cuando cada tabla está vacía.
- `src/main/resources/templates`: páginas de inicio, usuarios y productos.
- `database/demo_spring.sql`: alternativa para crear la base, tablas y datos desde phpMyAdmin.

Los datos de ejemplo coinciden con la evidencia entregada: dos usuarios y tres productos.
