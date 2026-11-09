# API de productos con ambientes

Clon independiente de la API de productos de Semana 5, preparada para cambiar de base de datos y puerto mediante Spring Profiles y variables de entorno.

## Perfiles

| Perfil | Base de datos predeterminada | Puerto | `ddl-auto` |
|---|---|---:|---|
| `dev` | `productos_dev` | 8080 | `update` |
| `qa` | `productos_qa` | 8082 | `update` |
| `test` | `productos_test` | 8084 | `update` |
| `prod` | `productos_prod` | 8086 | `validate` |

Los valores de `DB_URL`, `DB_USER`, `DB_PASSWORD` y `SERVER_PORT` pueden reemplazarse desde el entorno. `PROFILE_ACTIVE` selecciona el perfil; si no se configura, se usa `dev`.

## Ejecutar en Visual Studio Code

1. Abrir esta carpeta en Visual Studio Code.
2. Iniciar MySQL desde XAMPP.
3. En **Run and Debug**, seleccionar `Productos - DEV`, `Productos - QA`, `Productos - TEST` o `Productos - PROD`.
4. Presionar **Start Debugging**. Cada configuración establece variables de entorno distintas en `.vscode/launch.json`.
5. Verificar el ambiente activo en `http://localhost:<puerto>/config`.

También se puede ejecutar con Maven:

```powershell
$env:PROFILE_ACTIVE='qa'
$env:DB_URL='jdbc:mysql://localhost:3306/productos_qa?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC'
$env:DB_USER='root'
$env:DB_PASSWORD=''
$env:SERVER_PORT='8082'
.\mvnw.cmd spring-boot:run
```

## API heredada de Semana 5

Se conservan las rutas CRUD `/api/productos` y la búsqueda `/api/productos/buscar?nombre=...`. Importar `postman/Lab10-Productos.postman_collection.json` en Postman para probarlas.

## Bases de datos

El script `database/productos_ambientes.sql` crea las bases de desarrollo, calidad, pruebas y producción. La aplicación puede crear la base activa si el usuario MySQL tiene permisos. Producción utiliza `validate`, por lo que se debe importar el esquema antes de iniciar el perfil.
