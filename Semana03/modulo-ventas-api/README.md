# Laboratorio 03 - Módulo de Ventas

API REST construida con Spring Boot para administrar categorías, productos, clientes, empleados, ventas y detalles de venta. Usa MySQL, JPA/Hibernate y una arquitectura Controller -> Service -> Repository.

## Requisitos y ejecución

- JDK 17 o superior.
- XAMPP con MySQL iniciado en el puerto `3306`.
- Visual Studio Code y Postman.

En una terminal ubicada en esta carpeta, ejecutar:

```powershell
.\gradlew.bat bootRun
```

La configuración predeterminada usa MySQL `root` sin contraseña y crea la base `laboratorio_api` si el servidor lo permite. Hibernate crea o actualiza las tablas. Si todas están vacías, la aplicación registra datos de demostración para las seis entidades. Para crear la base y el esquema manualmente, importar `database/laboratorio_api.sql` desde phpMyAdmin.

Al ver que Spring Boot inició en `8080`, abrir Postman y usar `http://localhost:8080` como URL base. La colección está en `postman/Semana03-Modulo-Ventas.postman_collection.json`.

## Entidades y relaciones

- `Categoria` 1 a muchos `Producto`.
- `Cliente` 1 a muchos `Venta`.
- `Empleado` 1 a muchos `Venta`.
- `Venta` 1 a muchos `DetalleVenta`.
- `Producto` 1 a muchos `DetalleVenta`.

Cada venta registra al cliente y empleado responsables, sus detalles, precio unitario histórico y total calculado. Al registrar o cambiar detalles se valida el stock y se actualiza en la misma transacción.

## Endpoints CRUD

| Recurso | Base | CRUD |
|---|---|---|
| Categorías | `/api/categorias` | GET lista, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Productos | `/api/productos` | GET lista, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Clientes | `/api/clientes` | GET lista, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Empleados | `/api/empleados` | GET lista, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Ventas | `/api/ventas` | GET lista, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Detalles | `/api/ventas/{ventaId}/detalles` | GET lista, GET `/{detalleId}`, POST, PUT `/{detalleId}`, DELETE `/{detalleId}` |

Las rutas retornan `201` al crear, `204` al eliminar, `404` si no se encuentra el registro, `400` si falla la validación y `409` para restricciones de integridad o stock insuficiente.

## Ejemplos de cuerpos JSON

Producto (categoría existente):

```json
{
  "nombre": "Monitor",
  "precio": 50.00,
  "stock": 20,
  "categoriaId": 1
}
```

Venta con un detalle:

```json
{
  "clienteId": 1,
  "empleadoId": 1,
  "estado": "REGISTRADA",
  "detalles": [
    { "productoId": 1, "cantidad": 2 }
  ]
}
```

## Organización del código

- `model`: entidades JPA y relaciones persistentes.
- `repository`: interfaces `JpaRepository`.
- `service`: lógica de negocio, búsquedas, stock y transacciones.
- `controller`: endpoints REST y respuestas HTTP.
- `dto`: contratos de entrada y validaciones.
- `config/DemoDataInitializer`: datos de ejemplo solo cuando la BD está vacía.
- `controller/ApiExceptionHandler`: respuestas JSON para errores frecuentes.
