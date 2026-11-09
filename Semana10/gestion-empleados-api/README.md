# Gestión de empleados por ambientes

API REST para el caso TechSolutions SAC. Implementa CRUD de empleados y configura una conexión independiente por perfil Spring.

## Ambientes

| Perfil | Base predeterminada | Puerto | `ddl-auto` |
|---|---|---:|---|
| `dev` | `empleados_dev` | 8080 | `update` |
| `qa` | `empleados_qa` | 8082 | `update` |
| `test` | `empleados_test` | 8084 | `update` |
| `prod` | `empleados_prod` | 8086 | `validate` |

## Ejecutar en Visual Studio Code

1. Abrir esta carpeta en Visual Studio Code e iniciar MySQL en XAMPP.
2. En la vista **Run and Debug**, elegir `Empleados - DEV`, `Empleados - QA`, `Empleados - TEST` o `Empleados - PROD`.
3. Presionar **Start Debugging**. Las opciones guardadas en `.vscode/launch.json` configuran `PROFILE_ACTIVE`, `DB_URL`, `DB_USER`, `DB_PASSWORD` y `SERVER_PORT`.
4. Comprobar el perfil con `http://localhost:<puerto>/configuracion`.

Desde PowerShell también se puede elegir un perfil antes de iniciar Maven:

```powershell
$env:PROFILE_ACTIVE='qa'
$env:DB_URL='jdbc:mysql://localhost:3306/empleados_qa?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC'
$env:DB_USER='root'
$env:DB_PASSWORD=''
$env:SERVER_PORT='8082'
.\mvnw.cmd spring-boot:run
```

## Endpoints

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/empleados` | Lista registros |
| GET | `/empleados/{id}` | Consulta un empleado |
| POST | `/empleados` | Crea (201) |
| PUT | `/empleados/{id}` | Actualiza |
| DELETE | `/empleados/{id}` | Elimina (204) |
| GET | `/configuracion` | Informa ambiente, perfil, puerto y base activa |

Ejemplo de empleado:

```json
{"nombres":"Ana","apellidos":"Torres","email":"ana.torres@example.com","sueldo":2500.00}
```

La colección `postman/Lab10-Empleados.postman_collection.json` contiene solicitudes CRUD, validación de datos y consulta de configuración. El archivo `database/empleados_ambientes.sql` crea las cuatro bases y la tabla necesaria para producción. Se puede usar `createDatabaseIfNotExist=true` en los perfiles no productivos si MySQL autoriza al usuario.
